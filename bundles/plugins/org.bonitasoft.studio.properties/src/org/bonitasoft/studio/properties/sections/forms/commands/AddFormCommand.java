/**
 * Copyright (C) 2009-2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.studio.properties.sections.forms.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bonitasoft.engine.bpm.document.DocumentValue;
import org.bonitasoft.studio.common.ExpressionConstants;
import org.bonitasoft.studio.common.NamingUtils;
import org.bonitasoft.studio.common.emf.tools.ExpressionHelper;
import org.bonitasoft.studio.common.emf.tools.ModelHelper;
import org.bonitasoft.studio.common.emf.tools.WidgetModifiersSwitch;
import org.bonitasoft.studio.model.expression.Expression;
import org.bonitasoft.studio.model.expression.ExpressionFactory;
import org.bonitasoft.studio.model.expression.Operation;
import org.bonitasoft.studio.model.expression.Operator;
import org.bonitasoft.studio.model.form.CheckBoxMultipleFormField;
import org.bonitasoft.studio.model.form.CheckBoxSingleFormField;
import org.bonitasoft.studio.model.form.ComboFormField;
import org.bonitasoft.studio.model.form.DateFormField;
import org.bonitasoft.studio.model.form.FileWidget;
import org.bonitasoft.studio.model.form.FileWidgetInputType;
import org.bonitasoft.studio.model.form.Form;
import org.bonitasoft.studio.model.form.FormFactory;
import org.bonitasoft.studio.model.form.FormField;
import org.bonitasoft.studio.model.form.ListFormField;
import org.bonitasoft.studio.model.form.MultipleValuatedFormField;
import org.bonitasoft.studio.model.form.PasswordFormField;
import org.bonitasoft.studio.model.form.RadioFormField;
import org.bonitasoft.studio.model.form.SelectFormField;
import org.bonitasoft.studio.model.form.TextAreaFormField;
import org.bonitasoft.studio.model.form.TextFormField;
import org.bonitasoft.studio.model.form.Widget;
import org.bonitasoft.studio.model.form.WidgetLayoutInfo;
import org.bonitasoft.studio.model.process.Data;
import org.bonitasoft.studio.model.process.Document;
import org.bonitasoft.studio.model.process.Element;
import org.bonitasoft.studio.model.process.EnumType;
import org.bonitasoft.studio.model.process.PageFlow;
import org.bonitasoft.studio.model.process.Pool;
import org.bonitasoft.studio.model.process.ProcessPackage;
import org.bonitasoft.studio.model.process.diagram.form.part.ValidateAction;
import org.bonitasoft.studio.model.process.diagram.form.providers.ElementInitializers;
import org.bonitasoft.studio.properties.i18n.Messages;
import org.bonitasoft.studio.properties.sections.forms.FormsUtils;
import org.bonitasoft.studio.properties.sections.forms.FormsUtils.WidgetEnum;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;

/**
 * 
 * Add a new Form to a pageFlow It also create the Diagram and open it
 * 
 * @author Baptiste Mesta
 * @author Aurelien Pupier - use AbstractTransactionalCommand to avoid memory leaks (instead of Command)
 */
public class AddFormCommand extends AbstractTransactionalCommand {

    private final String formName;
    private final Map<Element, WidgetEnum> vars;
    private final Element pageFlow;
    private Form form = null;
    private final String description;
    private final Expression label;
    private final EStructuralFeature feature;

    /**
     * 
     * @param pageFlow2
     *            the pageFlow to add a form to
     * @param feature
     * 				where to add the form
     * @param formName
     *            the name of the form
     * @param vars
     *            type of widget of each data
     * @param editingDomain
     */
    public AddFormCommand(Element pageFlow2, EStructuralFeature feature, String formName, String description, Map<Element, WidgetEnum> vars,
            TransactionalEditingDomain editingDomain) {
        super(editingDomain, Messages.formAddFormCommandLabel, getWorkspaceFiles(pageFlow2));
        this.formName = NamingUtils.toJavaIdentifier(formName,true);
        Expression expr = ExpressionFactory.eINSTANCE.createExpression();
        expr.setName(formName);
        expr.setContent(formName);
        expr.setType(ExpressionConstants.CONSTANT_TYPE);
        expr.setReturnType(String.class.getName());
        expr.setReturnTypeFixed(true);
        label = expr;
        this.description = description;
        this.vars = new HashMap<Element, WidgetEnum>(vars);
        pageFlow = pageFlow2;
        this.feature = feature;
    }

    /**
     * @param myForm
     * @return
     */
    private int calculateAndSetNumColumn(Form myForm) {
        int res = 1;
        if (((List<?>) pageFlow.eGet(feature)).size() == 0) {
            res = 1;
        } else {
            res = 2;
        }
        myForm.setNColumn(res);
        return res;
    }

    public String getDescription() {
        return Messages.formAddFormCommandDescription;
    }

    @Override
    protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
            throws ExecutionException {
        Form myForm;
        /* set the numColumn (which will be the horizontal span for widgets) */
        int nCol;
        if(feature.equals(ProcessPackage.Literals.PAGE_FLOW__FORM)){
            myForm = FormFactory.eINSTANCE.createForm();
            nCol = calculateAndSetNumColumn(myForm);
        }else{
            myForm = FormFactory.eINSTANCE.createViewForm();
            // submit button
            nCol = 1;
        }
        myForm.setName(formName);
        myForm.setPageLabel(label);
        myForm.setShowPageLabel(true);
        myForm.setDocumentation(description);


        Widget tempWidget = null;
        
        Set<Entry<Element,WidgetEnum>> entrySet = vars.entrySet();
        List<Entry<Element,WidgetEnum>> entryList = new ArrayList<Entry<Element,WidgetEnum>>(entrySet);
        		//new ArrayList(vars.entrySet());
        Collections.sort(entryList, new Comparator<Entry<Element,WidgetEnum>>() {
			@Override
			public int compare(Entry<Element, WidgetEnum> o1,
					Entry<Element, WidgetEnum> o2) {
				return o1.getKey().getName().compareTo(o2.getKey().getName());
			}
		});
        
        for (Entry<Element, WidgetEnum> entry : entryList) {
            final Element key = entry.getKey();
            switch (entry.getValue()) {
                case TEXT_AREA:
                    tempWidget = FormFactory.eINSTANCE.createTextAreaFormField();
                    ElementInitializers.getInstance().init_TextAreaFormField_3113((TextAreaFormField) tempWidget);
                    break;
                case TEXT:
                    tempWidget = FormFactory.eINSTANCE.createTextFormField();
                    ElementInitializers.getInstance().init_TextFormField_3112((TextFormField) tempWidget);
                    break;
                case COMBO:
                    tempWidget = FormFactory.eINSTANCE.createComboFormField();
                    ElementInitializers.getInstance().init_ComboFormField_3103((ComboFormField) tempWidget);
                    break;
                case CHECKBOX:
                    tempWidget = FormFactory.eINSTANCE.createCheckBoxSingleFormField();
                    ElementInitializers.getInstance().init_CheckBoxSingleFormField_3118((CheckBoxSingleFormField) tempWidget);
                    break;
                case CHECKBOX_LIST:
                    tempWidget = FormFactory.eINSTANCE.createCheckBoxMultipleFormField();
                    ElementInitializers.getInstance().init_CheckBoxMultipleFormField_3120((CheckBoxMultipleFormField) tempWidget);
                    break;
                case RADIO:
                    tempWidget = FormFactory.eINSTANCE.createRadioFormField();
                    ElementInitializers.getInstance().init_RadioFormField_3110((RadioFormField) tempWidget);
                    break;
                case DATE:
                    tempWidget = FormFactory.eINSTANCE.createDateFormField();
                    ElementInitializers.getInstance().init_DateFormField_3105((DateFormField) tempWidget);
                    break;
                case LIST:
                    tempWidget = FormFactory.eINSTANCE.createListFormField();
                    ElementInitializers.getInstance().init_ListFormField_3107((ListFormField) tempWidget);
                    break;
                case PASSWORD:
                    tempWidget = FormFactory.eINSTANCE.createPasswordFormField();
                    ElementInitializers.getInstance().init_PasswordFormField_3109((PasswordFormField) tempWidget);
                    break;
                case SELECT:
                    tempWidget = FormFactory.eINSTANCE.createSelectFormField();
                    ElementInitializers.getInstance().init_SelectFormField_3111((SelectFormField) tempWidget);
                    break;
                case FILE:
                    tempWidget = FormFactory.eINSTANCE.createFileWidget();
                    ElementInitializers.getInstance().init_FileWidget_3119((FileWidget) tempWidget);
                    if(key instanceof Document){
                        ((FileWidget) tempWidget).setInputType(FileWidgetInputType.DOCUMENT);
                        ((FileWidget) tempWidget).setDocument((Document) key);
                    }
                    break;
            }

            String name  = computeName(key.getName());
            tempWidget.setName(name);
            Expression displayLabel = ExpressionFactory.eINSTANCE.createExpression();
            displayLabel.setName(key.getName());
            displayLabel.setType(ExpressionConstants.CONSTANT_TYPE);
            displayLabel.setReturnType(String.class.getName());
            displayLabel.setReturnTypeFixed(true);
            displayLabel.setContent(key.getName());
            tempWidget.setDisplayLabel(displayLabel);
            Expression insertWidgetIf = createInsertWidgetIfScript(); 
            tempWidget.setInjectWidgetScript(insertWidgetIf);
            WidgetLayoutInfo wLayout = FormFactory.eINSTANCE.createWidgetLayoutInfo();
            wLayout.setLine(myForm.getWidgets().size());
            wLayout.setHorizontalSpan(nCol);
            tempWidget.setWidgetLayoutInfo(wLayout);

            if (tempWidget instanceof FormField) {

                // add expression
                Expression currentExpression = ExpressionFactory.eINSTANCE.createExpression();
                currentExpression.setContent(key.getName()) ;
                currentExpression.setName(key.getName()) ;
                currentExpression.setType(ExpressionConstants.VARIABLE_TYPE) ;
                currentExpression.getReferencedElements().add(EcoreUtil.copy(key)) ;

                if(key instanceof Data){
                    Data data = (Data)key;
                    currentExpression.setReturnType(org.bonitasoft.studio.common.DataUtil.getTechnicalTypeFor(data)) ;
                    
                    if(tempWidget instanceof CheckBoxSingleFormField){
                    	currentExpression.setReturnTypeFixed(true);
                    }                    
                    
                    if (data.getDataType() instanceof EnumType && tempWidget instanceof MultipleValuatedFormField) {
                    	if(!(feature.equals(ProcessPackage.Literals.PAGE_FLOW__FORM) && pageFlow instanceof Pool && !ProcessPackage.Literals.PAGE_FLOW__TRANSIENT_DATA.equals(data.eContainingFeature()))){
                            ((MultipleValuatedFormField) tempWidget).setDefaultExpression(currentExpression);
                    	}
                        currentExpression = ExpressionHelper.createExpressionFromEnumType((EnumType) data.getDataType());
                        ((FormField) tempWidget).setInputExpression(currentExpression);
                    }else if(!(feature.equals(ProcessPackage.Literals.PAGE_FLOW__FORM) && pageFlow instanceof Pool && !ProcessPackage.Literals.PAGE_FLOW__TRANSIENT_DATA.equals(data.eContainingFeature()))){ //Do not set input expression if we are in an instantiation form
                        ((FormField) tempWidget).setInputExpression(currentExpression);
                    }


                    final String type = org.bonitasoft.studio.common.DataUtil.getTechnicalTypeFor(data);
                    Collection<String> supportedModifiers = new WidgetModifiersSwitch().doSwitch(tempWidget);
                    if(supportedModifiers.contains(type)){
                        tempWidget.setReturnTypeModifier(type);
                    }else{
                    	tempWidget.setReturnTypeModifier(null);
                    }
                    if(!isDataPageFlowTransient(data)) {
                        final Operation action = createDataOutputOperation(tempWidget, data);
                        ((FormField) tempWidget).setAction(action) ;
                    }
                }else if(key instanceof Document && tempWidget instanceof FileWidget){
                    final Document doc = (Document) key;
                    currentExpression.setReturnType(String.class.getName()) ;
                    currentExpression.setType(ExpressionConstants.DOCUMENT_REF_TYPE);
                    if(!(feature.equals(ProcessPackage.Literals.PAGE_FLOW__FORM) && pageFlow instanceof Pool)){ //Do not set input expression if we are in an instantiation form
                        ((FormField) tempWidget).setInputExpression(currentExpression);
                    }
                    final Operation action = createDocumentOutputOperation(tempWidget, doc);
                    ((FormField) tempWidget).setAction(action) ;
                }
                myForm.getWidgets().add(tempWidget);
            }
        }
        WidgetLayoutInfo wLayout;
        // where to put buttons
        int minButtonLine = 2;

        // add a "previous" button if there is more than one form
        if (((List<?>) pageFlow.eGet(feature)).size() != 0) {
            // myForm.setNColumn(2);
            tempWidget = FormFactory.eINSTANCE.createPreviousFormButton();
            String previousName = NamingUtils.getInstance(pageFlow).generateName(tempWidget, pageFlow);
            Expression previousLabel = ExpressionFactory.eINSTANCE.createExpression();
            previousLabel.setName(previousName);
            previousLabel.setType(ExpressionConstants.CONSTANT_TYPE);
            previousLabel.setReturnType(String.class.getName());
            previousLabel.setContent(previousName);
            previousLabel.setReturnTypeFixed(true);
            tempWidget.setDisplayLabel(previousLabel);
            tempWidget.setName(NamingUtils.convertToId(previousName, tempWidget));
            Expression insertWidgetIf = createInsertWidgetIfScript(); 
            tempWidget.setInjectWidgetScript(insertWidgetIf);
            wLayout = FormFactory.eINSTANCE.createWidgetLayoutInfo();
            wLayout.setLine(Math.max(myForm.getWidgets().size(), minButtonLine));
            tempWidget.setWidgetLayoutInfo(wLayout);
            myForm.getWidgets().add(tempWidget);
        }

        if (feature.equals(ProcessPackage.Literals.PAGE_FLOW__FORM)) {
            // add a submit button
            tempWidget = FormFactory.eINSTANCE.createSubmitFormButton();
            String submitButtonName = NamingUtils.getInstance(pageFlow).generateName(tempWidget, pageFlow);
            tempWidget.setName(NamingUtils.convertToId(submitButtonName, tempWidget));
           
         
            Expression submitLabel = ExpressionFactory.eINSTANCE.createExpression();
            submitLabel.setName(submitButtonName);
            submitLabel.setType(ExpressionConstants.CONSTANT_TYPE);
            submitLabel.setReturnType(String.class.getName());
            submitLabel.setReturnTypeFixed(true);
            submitLabel.setContent(submitButtonName);
            tempWidget.setDisplayLabel(submitLabel);
           
            Expression insertWidgetIf = createInsertWidgetIfScript(); 
            tempWidget.setInjectWidgetScript(insertWidgetIf);
            wLayout = FormFactory.eINSTANCE.createWidgetLayoutInfo();
            wLayout.setLine(Math.max(((List<?>) pageFlow.eGet(feature)).size() != 0 ? myForm.getWidgets().size() - 1 : myForm.getWidgets().size(), minButtonLine));
            if (((List<?>) pageFlow.eGet(feature)).size() != 0) {
                wLayout.setColumn(1);
            }
            tempWidget.setWidgetLayoutInfo(wLayout);

            myForm.getWidgets().add(tempWidget);
        }

        myForm.setNLine(Math.max(vars.size() + 1, minButtonLine + 1));

        form = myForm;

        // add the form to the pageFlow
        ((List) pageFlow.eGet(feature)).add(myForm);

        FormsUtils.createDiagram(myForm, getEditingDomain(), pageFlow);
        DiagramEditor editor = FormsUtils.openDiagram(myForm,getEditingDomain());
        ValidateAction.runValidation(editor.getDiagramEditPart(),editor.getDiagram());
        editor.getDiagramGraphicalViewer().select(editor.getDiagramEditPart());
        return CommandResult.newOKCommandResult(form);
    }

    protected Operation createDocumentOutputOperation(Widget tempWidget,Document doc) {
    	Operation action = ExpressionFactory.eINSTANCE.createOperation() ;
        Operator assignment = ExpressionFactory.eINSTANCE.createOperator();
        assignment.setType(ExpressionConstants.SET_DOCUMENT_OPERATOR) ;
        action.setOperator(assignment) ;
        Expression storageExpression = ExpressionFactory.eINSTANCE.createExpression();
        storageExpression.setContent(doc.getName()) ;
        storageExpression.setName(doc.getName()) ;
        storageExpression.setType(ExpressionConstants.DOCUMENT_REF_TYPE) ;
        storageExpression.setReturnType(String.class.getName()) ;
        storageExpression.getReferencedElements().add(EcoreUtil.copy(doc)) ;
        action.setLeftOperand(storageExpression) ;

        Expression actionExpression = ExpressionFactory.eINSTANCE.createExpression();
        actionExpression.setContent("field_"+tempWidget.getName()) ;
        actionExpression.setName("field_"+tempWidget.getName()) ;
        actionExpression.setType(ExpressionConstants.FORM_FIELD_TYPE) ;
        actionExpression.setReturnType(DocumentValue.class.getName()) ;
        actionExpression.getReferencedElements().add(EcoreUtil.copy(tempWidget)) ;
        action.setRightOperand(actionExpression) ;
        return action;
	}

	protected Operation createDataOutputOperation(Widget tempWidget, Data data) {
        Operation action = ExpressionFactory.eINSTANCE.createOperation() ;
        Operator assignment = ExpressionFactory.eINSTANCE.createOperator();
        assignment.setType(ExpressionConstants.ASSIGNMENT_OPERATOR) ;
        action.setOperator(assignment) ;
        Expression storageExpression = ExpressionFactory.eINSTANCE.createExpression();
        storageExpression.setContent(data.getName()) ;
        storageExpression.setName(data.getName()) ;
        storageExpression.setType(ExpressionConstants.VARIABLE_TYPE) ;
        storageExpression.setReturnType(org.bonitasoft.studio.common.DataUtil.getTechnicalTypeFor(data)) ;
        storageExpression.getReferencedElements().add(EcoreUtil.copy(data)) ;
        action.setLeftOperand(storageExpression) ;

        Expression actionExpression = ExpressionFactory.eINSTANCE.createExpression();
        actionExpression.setContent("field_"+tempWidget.getName()) ;
        actionExpression.setName("field_"+tempWidget.getName()) ;
        actionExpression.setType(ExpressionConstants.FORM_FIELD_TYPE) ;
        if(tempWidget.getReturnTypeModifier() != null){
            actionExpression.setReturnType(tempWidget.getReturnTypeModifier()) ;
        }else{
            actionExpression.setReturnType(tempWidget.getAssociatedReturnType()) ;
        }

        actionExpression.getReferencedElements().add(EcoreUtil.copy(tempWidget)) ;
        action.setRightOperand(actionExpression) ;
        return action;
    }

    /**
     * @param data
     * @return
     */
    private boolean isDataPageFlowTransient(Data data) {
        EReference reference = data.eContainmentFeature();
        if(reference!=null){
            return reference.equals(ProcessPackage.Literals.PAGE_FLOW__TRANSIENT_DATA)
                    || reference.equals(ProcessPackage.Literals.VIEW_PAGE_FLOW__VIEW_TRANSIENT_DATA)
                    || reference.equals(ProcessPackage.Literals.RECAP_FLOW__RECAP_TRANSIENT_DATA);
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.emf.workspace.AbstractEMFOperation#canUndo()
     */
    @Override
    public boolean canUndo() {
        return false;//avoid issues when a form was created an en editor is open on it
    }
    
    private Expression createInsertWidgetIfScript(){
    	Expression exp = org.bonitasoft.studio.common.NamingUtils.generateConstantExpression("");
    	exp.setReturnType(Boolean.class.getName());
    	exp.setReturnTypeFixed(true);
    	return exp ;	
    }
    
    private String computeName(String key){
    	PageFlow pf = (PageFlow)pageFlow;
    	if (pf !=null){
    		int number = NamingUtils.getMaxElements((Element) pageFlow, key);
			number++;
			key +=number;		
    	}
    return key;
    }
}

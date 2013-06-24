/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.studio.properties.form.sections.actions.contributions;

import org.bonitasoft.studio.common.ExpressionConstants;
import org.bonitasoft.studio.common.properties.ExtensibleGridPropertySection;
import org.bonitasoft.studio.common.properties.IExtensibleGridPropertySectionContribution;
import org.bonitasoft.studio.expression.editor.filter.HiddenExpressionTypeFilter;
import org.bonitasoft.studio.expression.editor.viewer.ExpressionViewer;
import org.bonitasoft.studio.form.properties.i18n.Messages;
import org.bonitasoft.studio.model.expression.Expression;
import org.bonitasoft.studio.model.expression.ExpressionFactory;
import org.bonitasoft.studio.model.form.ComboFormField;
import org.bonitasoft.studio.model.form.FormPackage;
import org.bonitasoft.studio.model.form.MultipleValuatedFormField;
import org.bonitasoft.studio.model.form.RadioFormField;
import org.bonitasoft.studio.model.form.SelectFormField;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

/**
 * 
 * @author Baptiste Mesta
 *
 */
public class DefaultValueContribution implements IExtensibleGridPropertySectionContribution {

    protected TransactionalEditingDomain editingDomain;
    protected MultipleValuatedFormField widget;
    protected ExpressionViewer defaultExpressionViewer;
    protected EMFDataBindingContext dataBindingContext;
    protected final HiddenExpressionTypeFilter filterVariableType = new HiddenExpressionTypeFilter(new String[]{ExpressionConstants.VARIABLE_TYPE,ExpressionConstants.SEARCH_INDEX_TYPE});


    public void createControl(Composite composite, TabbedPropertySheetWidgetFactory widgetFactory, ExtensibleGridPropertySection extensibleGridPropertySection) {

        GridLayout layout = new GridLayout(2, false);

        layout.marginHeight = InitialValueContribution.MARGIN_HEIGHT;
        layout.marginWidth = InitialValueContribution.MARGIN_WIDTH;
        composite.setLayout(layout);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(composite);

        defaultExpressionViewer = new ExpressionViewer(composite,SWT.BORDER,widgetFactory,editingDomain, FormPackage.Literals.MULTIPLE_VALUATED_FORM_FIELD__DEFAULT_EXPRESSION, true) ;

        if(widget instanceof RadioFormField ||widget instanceof  ComboFormField ||widget instanceof  SelectFormField){
            defaultExpressionViewer.setMessage(Messages.data_tooltip_text,IStatus.INFO);
        }else{
            defaultExpressionViewer.setMessage(Messages.data_tooltip_listonly,IStatus.INFO);
        }
        defaultExpressionViewer.getControl().setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());




        bindWidgets();
    }


    protected void bindWidgets() {
        updateViewerInput() ;
    }

    public void dispose() {
        if(dataBindingContext!=null) {
            dataBindingContext.dispose();
        }
    }

    public String getLabel() {
        return Messages.Action_SelectedValue;
    }

    public boolean isRelevantFor(EObject eObject) {
        return eObject instanceof MultipleValuatedFormField;
    }

    public void refresh() {

    }

    public void setEObject(EObject object) {
        widget = (MultipleValuatedFormField) object;
    }

    public void setEditingDomain(TransactionalEditingDomain editingDomain) {
        this.editingDomain = editingDomain;
    }

    public void setSelection(ISelection selection) {

    }

    protected void updateViewerInput(){
        if(defaultExpressionViewer != null && !defaultExpressionViewer.getControl().isDisposed()){

            if(dataBindingContext != null){
                dataBindingContext.dispose() ;
            }
            dataBindingContext = new EMFDataBindingContext() ;

            Expression input = widget.getDefaultExpression() ;
            if(input == null){
                input = ExpressionFactory.eINSTANCE.createExpression() ;
                editingDomain.getCommandStack().execute(SetCommand.create(editingDomain, widget, FormPackage.Literals.MULTIPLE_VALUATED_FORM_FIELD__DEFAULT_EXPRESSION, input));
            }
            dataBindingContext.bindValue(
                    ViewerProperties.singleSelection().observe(defaultExpressionViewer),
                    EMFEditProperties.value(editingDomain, FormPackage.Literals.MULTIPLE_VALUATED_FORM_FIELD__DEFAULT_EXPRESSION).observe(widget));
            defaultExpressionViewer.setEditingDomain(editingDomain) ;
            defaultExpressionViewer.setInput(widget) ;
        }
    }


}
/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.studio.connector.model.definition.wizard.support;


import org.bonitasoft.studio.connector.model.definition.ConnectorDefinitionPackage;
import org.bonitasoft.studio.connector.model.i18n.Messages;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
/**
 * @author Romain Bioteau
 *
 */
public class InputMandatoryEditingSupport extends ObservableValueEditingSupport {

    private static  String[] mandatoryChoices = new String[]{
        Messages.mandatory,
        Messages.optional
    } ;
    private final DataBindingContext context;

    public InputMandatoryEditingSupport(ColumnViewer viewer,DataBindingContext context) {
        super(viewer,context);
        this.context = context ;
    }

    @Override
    protected Binding createBinding(IObservableValue target,final IObservableValue model) {
        UpdateValueStrategy targetToModel =  new UpdateValueStrategy(UpdateValueStrategy.POLICY_CONVERT) ;
        targetToModel.setConverter(new Converter(String.class,Boolean.class){

            @Override
            public Object convert(Object from) {
                if(from.toString().equals(Messages.mandatory)){
                    return true ;
                }else{
                    return false ;
                }
            }

        }) ;

        UpdateValueStrategy modeltoTarget =  new UpdateValueStrategy(UpdateValueStrategy.POLICY_CONVERT) ;
        modeltoTarget.setConverter(new Converter(Boolean.class,String.class){

            @Override
            public Object convert(Object from) {
                if((Boolean) from){
                    return Messages.mandatory ;
                }else{
                    return Messages.optional ;
                }
            }

        }) ;
        return context.bindValue(target, model,targetToModel , modeltoTarget);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.EditingSupport#getCellEditor(java.lang.Object)
     */
    @Override
    protected CellEditor getCellEditor(final Object element) {
        ComboBoxCellEditor editor = new ComboBoxCellEditor((Composite) getViewer().getControl(), mandatoryChoices,SWT.READ_ONLY) ;
        return  editor;
    }

    @Override
    protected IObservableValue doCreateCellEditorObservable(CellEditor cellEditor) {
        return SWTObservables.observeText(cellEditor.getControl());
    }



    @Override
    protected IObservableValue doCreateElementObservable(Object element, ViewerCell cell) {
        return EMFObservables.observeValue((EObject) element, ConnectorDefinitionPackage.Literals.INPUT__MANDATORY);
    }



}
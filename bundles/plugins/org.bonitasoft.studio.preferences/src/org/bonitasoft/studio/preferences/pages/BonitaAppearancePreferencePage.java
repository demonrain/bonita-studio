/*
 * Copyright (C) 2009-2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * 
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
package org.bonitasoft.studio.preferences.pages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bonitasoft.studio.common.extension.BonitaStudioExtensionRegistryManager;
import org.bonitasoft.studio.common.log.BonitaStudioLog;
import org.bonitasoft.studio.pics.Pics;
import org.bonitasoft.studio.pics.PicsConstants;
import org.bonitasoft.studio.preferences.BonitaCoolBarPreferenceConstant;
import org.bonitasoft.studio.preferences.BonitaStudioPreferencesPlugin;
import org.bonitasoft.studio.preferences.extension.IPreferenceFieldEditorContribution;
import org.bonitasoft.studio.preferences.i18n.Messages;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;

/**
 * @author Romain Bioteau 
 *
 */

public class BonitaAppearancePreferencePage extends AbstractBonitaPreferencePage implements IWorkbenchPreferencePage {

	private RadioGroupFieldEditor radioFiled;
	private List<IPreferenceFieldEditorContribution> contributions;
	private List<FieldEditor> fieldEditors;


	public BonitaAppearancePreferencePage() {
		super(GRID);
		setPreferenceStore(BonitaStudioPreferencesPlugin.getDefault().getPreferenceStore());
		contributions = new ArrayList<IPreferenceFieldEditorContribution>();
		fieldEditors = new ArrayList<FieldEditor>() ;
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {

		createTitleBar(Messages.BonitaPreferenceDialog_appearance,Pics.getImage(PicsConstants.preferenceAppearance),false);

		Label coolBarLabel = new Label(getFieldEditorParent(),SWT.NONE) ;
		coolBarLabel.setText(Messages.defaultCoolbarAppearance) ;

		Composite radioComposite = new Composite(getFieldEditorParent(), SWT.NONE) ;
		radioComposite.setLayoutData(GridDataFactory.fillDefaults().grab(true,false).create()) ;

		radioFiled = new RadioGroupFieldEditor(BonitaCoolBarPreferenceConstant.COOLBAR_DEFAULT_SIZE,"", 3, new String[][] {
				{ Messages.normal, BonitaCoolBarPreferenceConstant.NORMAL },
				{Messages.small, BonitaCoolBarPreferenceConstant.SMALL }}, radioComposite) ;

		radioComposite.setLayout(new GridLayout(3, false)) ;
		radioFiled.getLabelControl(radioComposite).dispose();
		radioFiled.getRadioBoxControl(radioComposite).setLayoutData(GridDataFactory.fillDefaults().create()) ;

		addField(radioFiled) ;
		fieldEditors.add(radioFiled) ;

		IConfigurationElement[] elems = BonitaStudioExtensionRegistryManager.getInstance().getConfigurationElements("org.bonitasoft.studio.preferences.prefrenceFieldEditorContribution"); //$NON-NLS-1$
		IPreferenceFieldEditorContribution prefEditorContrib = null;
		for (IConfigurationElement elem : elems){
			try {
				prefEditorContrib = (IPreferenceFieldEditorContribution) elem.createExecutableExtension("class"); //$NON-NLS-1$
			} catch (CoreException e) {
				BonitaStudioLog.error(e);
			} 

			if(prefEditorContrib.appliesTo("Appearance")){

				new Label(getFieldEditorParent(),SWT.NONE) ;
				new Label(getFieldEditorParent(),SWT.NONE) ;

				Label separator = new Label(getFieldEditorParent(),SWT.SEPARATOR | SWT.HORIZONTAL) ;
				separator.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).span(2, 1).create()) ;

				new Label(getFieldEditorParent(),SWT.NONE) ;
				new Label(getFieldEditorParent(),SWT.NONE) ;

				contributions.add(prefEditorContrib) ;
				for(FieldEditor fe : prefEditorContrib.createFieldEditors(getFieldEditorParent())){
					addField(fe);
					fieldEditors.add(fe); 
				}
			}
		}

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

	@Override
	public boolean performOk() {
		boolean ok = true ;
		for(IPreferenceFieldEditorContribution contrib : contributions){
			if(ok){
				ok = contrib.performOk();
			}
		}
		
		boolean res = super.performOk() ;
		String value = BonitaStudioPreferencesPlugin.getDefault().getPreferenceStore().getString(BonitaCoolBarPreferenceConstant.COOLBAR_DEFAULT_SIZE) ; 
		try{
			ICommandService service = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
			if (value.equals(BonitaCoolBarPreferenceConstant.SMALL)){
				service.getCommand("org.bonitasoft.studio.application.smallCoolbar").executeWithChecks(new ExecutionEvent()) ;
			}else if(value.equals(BonitaCoolBarPreferenceConstant.NORMAL)){
				service.getCommand("org.bonitasoft.studio.application.normalCoolbar").executeWithChecks(new ExecutionEvent()) ;
				
			}
		}catch (Exception e) {
			BonitaStudioLog.error(e) ;
		}
		return res && ok;
	}

	@Override
	protected void initialize() {
		
		if (fieldEditors != null) {
			Iterator<FieldEditor> e = fieldEditors.iterator();
			while (e.hasNext()) {
				FieldEditor pe = e.next();
				pe.setPage(this);
				pe.setPropertyChangeListener(this);
				if(pe.getPreferenceStore() == null){
					pe.setPreferenceStore(getPreferenceStore());
				}
				pe.load();
			}
		}

	}
}
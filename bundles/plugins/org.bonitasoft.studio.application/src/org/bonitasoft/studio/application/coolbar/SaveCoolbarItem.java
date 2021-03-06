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
package org.bonitasoft.studio.application.coolbar;

import java.util.Collections;

import org.bonitasoft.studio.application.i18n.Messages;
import org.bonitasoft.studio.common.extension.IBonitaContributionItem;
import org.bonitasoft.studio.common.log.BonitaStudioLog;
import org.bonitasoft.studio.pics.Pics;
import org.bonitasoft.studio.pics.PicsConstants;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.internal.handlers.DirtyStateTracker;

/**
 * @author Romain Bioteau
 *
 */
public class SaveCoolbarItem implements IBonitaContributionItem {

	private ToolItem item;
	private DirtyStateTracker dirtyStateTracker;


	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IContributionItem#dispose()
	 */
	@Override
	public void dispose() {}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IContributionItem#fill(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void fill(Composite parent) {}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IContributionItem#fill(org.eclipse.swt.widgets.Menu, int)
	 */
	@Override
	public void fill(Menu parent, int index) {}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IContributionItem#fill(org.eclipse.swt.widgets.ToolBar, int)
	 */
	@Override
	public void fill(ToolBar parent, int index) {

	}

	private Command getCommand() {
		ICommandService service = (ICommandService)PlatformUI.getWorkbench().getService(ICommandService.class);
		Command cmd = service.getCommand("org.eclipse.ui.file.save") ;
		return cmd;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IContributionItem#fill(org.eclipse.swt.widgets.CoolBar, int)
	 */
	@Override
	public void fill(CoolBar parent, int index) {}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IContributionItem#getId()
	 */
	@Override
	public String getId() {
		return "org.bonitasoft.studio.coolbar.save";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IContributionItem#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		Command cmd = getCommand();
		return cmd.isEnabled();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IContributionItem#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IContributionItem#isDynamic()
	 */
	@Override
	public boolean isDynamic() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IContributionItem#isGroupMarker()
	 */
	@Override
	public boolean isGroupMarker() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IContributionItem#isSeparator()
	 */
	@Override
	public boolean isSeparator() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IContributionItem#isVisible()
	 */
	@Override
	public boolean isVisible() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IContributionItem#saveWidgetState()
	 */
	@Override
	public void saveWidgetState() {


	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IContributionItem#setParent(org.eclipse.jface.action.IContributionManager)
	 */
	@Override
	public void setParent(IContributionManager parent) {


	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IContributionItem#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {


	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IContributionItem#update()
	 */
	@Override
	public void update() {


	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IContributionItem#update(java.lang.String)
	 */
	@Override
	public void update(String id) {

	}

	@Override
	public void fill(ToolBar toolbar, int index, int iconSize) {
		item = new ToolItem(toolbar,  SWT.PUSH) ;
		item.setToolTipText(Messages.SaveProcessButtonLabel) ;
		if(iconSize < 0 ){
			item.setText(Messages.SaveProcessButtonLabel) ;
			item.setImage(Pics.getImage(PicsConstants.coolbar_save_48)) ;
			item.setDisabledImage(Pics.getImage(PicsConstants.coolbar_save_disabled_48));
		}else{
			item.setImage(Pics.getImage(PicsConstants.coolbar_save_16)) ;
			item.setDisabledImage(Pics.getImage(PicsConstants.coolbar_save_disabled_16));
		}
		 item.setEnabled(false);
		item.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() ;
				if(editor != null){
					if(editor instanceof DiagramEditor){
						IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
						Command command = getCommand() ;
						ExecutionEvent executionEvent = new ExecutionEvent(command, Collections.EMPTY_MAP, null, handlerService.getClass());
						try {
							command.executeWithChecks(executionEvent);
						} catch (Exception e1){
							BonitaStudioLog.error(e1);
						}

					}else{
						editor.doSave(null);
					}
				}
			}
		}) ;
	}

	public DirtyStateTracker getDirtyStateTracker() {
		return dirtyStateTracker;
	}

	public void createDirtyStateTracker() {
		dirtyStateTracker = new DirtyStateTracker(){
			@Override
			public void propertyChanged(final Object source, int propID) {
				if (source instanceof ISaveablePart && propID == ISaveablePart.PROP_DIRTY) {
					update();
					if(item != null && !item.isDisposed()){
						item.setEnabled(((ISaveablePart) source).isDirty());
					}
				}
			}
		};
	}


}

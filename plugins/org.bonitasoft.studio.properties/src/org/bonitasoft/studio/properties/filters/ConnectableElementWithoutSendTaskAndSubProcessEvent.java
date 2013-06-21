/**
 * Copyright (C) 2010 BonitaSoft S.A.
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
package org.bonitasoft.studio.properties.filters;

import org.bonitasoft.studio.model.process.ConnectableElement;
import org.bonitasoft.studio.model.process.ErrorEvent;
import org.bonitasoft.studio.model.process.MainProcess;
import org.bonitasoft.studio.model.process.SendTask;
import org.bonitasoft.studio.model.process.SignalEvent;
import org.bonitasoft.studio.model.process.TimerEvent;
import org.bonitasoft.studio.profiles.manager.BonitaProfilesManager;
import org.bonitasoft.studio.profiles.manager.IBonitaActivitiesCategory;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.jface.viewers.IFilter;

/**
 * @author Aurelien Pupier
 *
 */
public class ConnectableElementWithoutSendTaskAndSubProcessEvent implements IFilter {


	public boolean select(Object object) {

		if (object instanceof IGraphicalEditPart) {
			IGraphicalEditPart editPart = (IGraphicalEditPart) object;
			Object model = editPart.resolveSemanticElement();	
			if(model instanceof MainProcess){
				return false ;
			}
			return BonitaProfilesManager.getInstance().isEnabled(IBonitaActivitiesCategory.CONNECTORS)  
			&& model instanceof ConnectableElement
			&& !(model instanceof SendTask)
			&& !(model instanceof ErrorEvent)
			&& !(model instanceof TimerEvent)
			&& !(model instanceof SignalEvent);
		}
		return false;
	}

}
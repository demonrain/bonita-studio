/**
 * Copyright (C) 2009 BonitaSoft S.A.
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
package org.bonitasoft.studio.dependencies.handler;

import java.util.List;

import org.bonitasoft.studio.dependencies.ui.dialog.ManageJarDialog;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.widgets.Display;

public class ManageJarsHandler extends AbstractHandler implements IHandler {

	public static final String JAR_NAME = "_jarName_";
	public static final String JAR_BYTES = "_jarBytes_";
	
	/**
	 * @return the {@link List} of jar names that were added, or null
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {		
		new ManageJarDialog(Display.getDefault().getActiveShell()).open();
		return null;
	}

//	/**
//	 * @return
//	 */
//	public static List<String> getEntries() {
//		List<String> jarList = new ArrayList<String>();
//		try {
//			for (IClasspathEntry entry : ExtensionProjectUtil.getJavaProject().getRawClasspath()) {
//				if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
//					File jar = entry.getPath().toFile();
//					if (! jar.exists()) {
//						jar = new File(ResourcesPlugin.getWorkspace().getRoot().getLocation() + entry.getPath().toString());
//					}
// 					if (jar.exists() && ! jar.getName().startsWith("bonita-server") && ! jar.getName().startsWith("bonita-client")) { // jar location relative to project
//						jarList.add(jar.getName());
//					}
//				}
//			}
//		} catch (Exception ex) {
//			BonitaStudioLog.log(ex);
//		}
//		return jarList;
//	}


}
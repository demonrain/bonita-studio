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

package org.bonitasoft.studio.groovy.ui.viewer;

import org.codehaus.groovy.eclipse.editor.GroovyColorManager;
import org.codehaus.groovy.eclipse.editor.GroovyEditor;
import org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * @author Romain Bioteau
 *
 */
public class BonitaGroovyEditor extends GroovyEditor {

	public BonitaGroovyEditor(IPreferenceStore store){
		setPreferenceStore(store);
	}

	public void editorContextMenuAboutToShow(IMenuManager menu) {
		menu.dispose() ;
	}

	@Override
	protected void createUndoRedoActions() {

	}
	
	@Override
	public JavaSourceViewerConfiguration createJavaSourceViewerConfiguration() {
		return new BonitaGroovyConfiguration(new GroovyColorManager(), getPreferenceStore(),this)  ;
	}
	
}
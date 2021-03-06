/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.studio.importer.bar.tests.messagesImport;

import java.io.File;
import java.net.URL;
import java.util.List;

import junit.framework.TestCase;

import org.bonitasoft.studio.common.emf.tools.ModelHelper;
import org.bonitasoft.studio.common.jface.FileActionDialog;
import org.bonitasoft.studio.importer.bar.tests.BarImporterTestUtil;
import org.bonitasoft.studio.model.process.MainProcess;
import org.bonitasoft.studio.model.process.Message;
import org.bonitasoft.studio.model.process.MessageFlow;
import org.bonitasoft.studio.model.process.ProcessPackage;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class CorrelationMigrationTest extends TestCase {
	
	private static boolean disablepopup;



	@BeforeClass
	public static void disablePopup(){
		disablepopup = FileActionDialog.getDisablePopup();
		FileActionDialog.setDisablePopup(true);
	}

	@AfterClass
	public static void resetdisablePopup(){
		FileActionDialog.setDisablePopup(disablepopup);
	}

	@Test
	public void testImportMessage59Bar() throws Exception{
		URL fileURL2 = FileLocator.toFileURL(CorrelationMigrationTest.class.getResource("sendMessage--1.0.bar")); //$NON-NLS-1$
		File migratedProcess = BarImporterTestUtil.migrateBar(fileURL2);
		 assertNotNull("Fail to migrate bar file", migratedProcess);
	     assertNotNull("Fail to migrate bar file", migratedProcess.exists());
	     final Resource resource = BarImporterTestUtil.assertIsLoadable(migratedProcess);
	     final MainProcess mainProcess = BarImporterTestUtil.getMainProcess(resource);
	     final List<MessageFlow> messageFlows =  ModelHelper.getAllItemsOfType(mainProcess, ProcessPackage.Literals.MESSAGE_FLOW);
	     assertTrue("main Process should contains 1 messageFlow at least",!messageFlows.isEmpty());
	     final MessageFlow flow = messageFlows.get(0);
	     final Message message = ModelHelper.findEvent(flow.getTarget(), flow.getName());
	     assertNotNull("correlation should exist",message.getCorrelation());
	     assertEquals("2 correlation key should be defined",2,message.getCorrelation().getCorrelationAssociation().getExpressions().size());
	}

}

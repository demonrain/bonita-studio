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
package org.bonitasoft.studio.exporter.handler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bonitasoft.studio.common.ModelVersion;
import org.bonitasoft.studio.common.NamingUtils;
import org.bonitasoft.studio.common.emf.tools.ModelHelper;
import org.bonitasoft.studio.common.extension.BonitaStudioExtensionRegistryManager;
import org.bonitasoft.studio.common.log.BonitaStudioLog;
import org.bonitasoft.studio.common.repository.RepositoryManager;
import org.bonitasoft.studio.common.repository.model.IRepositoryFileStore;
import org.bonitasoft.studio.common.repository.model.IRepositoryStore;
import org.bonitasoft.studio.common.repository.provider.IBOSArchiveFileStoreProvider;
import org.bonitasoft.studio.common.repository.ui.wizard.ExportRepositoryWizard;
import org.bonitasoft.studio.configuration.ConfigurationPlugin;
import org.bonitasoft.studio.configuration.ConfigurationSynchronizer;
import org.bonitasoft.studio.configuration.preferences.ConfigurationPreferenceConstants;
import org.bonitasoft.studio.diagram.custom.repository.ProcessConfigurationRepositoryStore;
import org.bonitasoft.studio.exporter.Messages;
import org.bonitasoft.studio.model.configuration.Configuration;
import org.bonitasoft.studio.model.configuration.ConfigurationFactory;
import org.bonitasoft.studio.model.process.AbstractProcess;
import org.bonitasoft.studio.model.process.MainProcess;
import org.bonitasoft.studio.model.process.Pool;
import org.bonitasoft.studio.model.process.ProcessPackage;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;


/**
 * @author Romain Bioteau
 *
 */
public class ExportBosArchiveHandler extends AbstractHandler {

    private static final String BOS_ARCHIVE_PROVIDERS_EXTENSION_POINT = "org.bonitasoft.studio.bosArchiveProvider";

    /* (non-Javadoc)
     * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        if(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().saveAllEditors(true)){
            Set<Object> selectedFiles = new HashSet<Object>() ;
            MainProcess diagram = getDiagramInEditor();
            if(diagram != null){
                selectedFiles = getAllDiagramRelatedFiles(diagram);
            }else{
                for(IRepositoryStore store : RepositoryManager.getInstance().getCurrentRepository().getAllExportableStores()){
                    List<IRepositoryFileStore> files = store.getChildren() ;
                    if( files != null){
                        files.remove(null) ;
                        selectedFiles.addAll(files) ;
                    }
                }
            }

            final ExportRepositoryWizard wizard = new ExportRepositoryWizard(RepositoryManager.getInstance().getCurrentRepository().getAllExportableStores(),true,selectedFiles,getDefaultName(),Messages.ExportButtonLabel) ;
            WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(),wizard){
            	protected void initializeBounds() {
            		super.initializeBounds();
            		getShell().setSize(600, 500); 
            	}
            };
            dialog.setTitle(Messages.ExportButtonLabel);
            dialog.open() ;
        }
        return null;
    }

    private String getDefaultName() {
        final MainProcess diagram = getDiagramInEditor() ;
        if(diagram == null){
            return RepositoryManager.getInstance().getCurrentRepository().getName() + "_" + new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date()) + ".bos";
        }
        return NamingUtils.toDiagramFilename(diagram).replace(".proc", ".bos");
    }

    public Set<Object> getAllDiagramRelatedFiles(MainProcess diagram) {
        final Set<Object> result = new HashSet<Object>() ;
        final List<Pool> processes =  ModelHelper.getAllItemsOfType(diagram, ProcessPackage.Literals.POOL) ;
        final List<IBOSArchiveFileStoreProvider> fileStoreProvider = getFileStoreProviders() ;

        for(Pool p : processes){
            Configuration conf = getConfiguration(p, ConfigurationPreferenceConstants.LOCAL_CONFIGURAITON) ;
            for(IBOSArchiveFileStoreProvider provider : fileStoreProvider){
                result.addAll(provider.getFileStoreForConfiguration(p, conf)) ;
                for(Configuration config : p.getConfigurations()){
                    result.addAll(provider.getFileStoreForConfiguration(p, config)) ;
                }
            }
        }

        return result;
    }

    private List<IBOSArchiveFileStoreProvider> getFileStoreProviders() {
        List<IBOSArchiveFileStoreProvider> res = new ArrayList<IBOSArchiveFileStoreProvider>();
        IConfigurationElement[] extensions = BonitaStudioExtensionRegistryManager.getInstance().getConfigurationElements(BOS_ARCHIVE_PROVIDERS_EXTENSION_POINT);
        for (IConfigurationElement extension : extensions) {
            try {
                res.add((IBOSArchiveFileStoreProvider)extension.createExecutableExtension("providerClass"));
            } catch (Exception ex) {
                BonitaStudioLog.error(ex);
            }
        }
        return res;
    }

    protected MainProcess getDiagramInEditor() {
        if( PlatformUI.getWorkbench().getWorkbenchWindows() == null ||  PlatformUI.getWorkbench().getWorkbenchWindows().length == 0){
            return null ;
        }
        IEditorPart editor = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getActivePage().getActiveEditor() ;
        boolean isADiagram = editor != null && editor instanceof DiagramEditor;
        if(isADiagram){
            EObject root = ((DiagramEditor)editor).getDiagramEditPart().resolveSemanticElement() ;
            MainProcess mainProc = ModelHelper.getMainProcess(root) ;
            return mainProc ;
        }

        return null;
    }

    public Configuration getConfiguration(final AbstractProcess process,String configurationId) {
        Configuration configuration = null ;
        final ProcessConfigurationRepositoryStore processConfStore = (ProcessConfigurationRepositoryStore) RepositoryManager.getInstance().getRepositoryStore(ProcessConfigurationRepositoryStore.class) ;
        if(configurationId == null){
            configurationId = ConfigurationPlugin.getDefault().getPreferenceStore().getString(ConfigurationPreferenceConstants.DEFAULT_CONFIGURATION) ;
        }
        if(configurationId.equals(ConfigurationPreferenceConstants.LOCAL_CONFIGURAITON)){
            String id = ModelHelper.getEObjectID(process) ;
            IRepositoryFileStore file = processConfStore.getChild(id+".conf") ;
            if(file == null){
                file = processConfStore.createRepositoryFileStore(id+".conf") ;
                Configuration conf = ConfigurationFactory.eINSTANCE.createConfiguration();
                conf.setVersion(ModelVersion.CURRENT_VERSION);
                file.save(conf);
            }
            configuration = (Configuration) file.getContent();
        }else{
            for(Configuration conf : process.getConfigurations()){
                if(configurationId.equals(conf.getName())){
                    configuration = conf ;
                }
            }
        }
        if(configuration == null){
            configuration = ConfigurationFactory.eINSTANCE.createConfiguration() ;
            configuration.setName(configurationId) ;
            configuration.setVersion(ModelVersion.CURRENT_VERSION);
        }
        //Synchronize configuration with definition
        new ConfigurationSynchronizer(process, configuration).synchronize() ;
        return configuration ;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



}
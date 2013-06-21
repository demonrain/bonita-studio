/**
 * Copyright (C) 2009-2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.studio.properties.sections.message.wizards;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bonitasoft.studio.common.ExpressionConstants;
import org.bonitasoft.studio.common.emf.tools.ModelHelper;
import org.bonitasoft.studio.common.repository.RepositoryManager;
import org.bonitasoft.studio.diagram.custom.repository.DiagramRepositoryStore;
import org.bonitasoft.studio.expression.editor.filter.AvailableExpressionTypeFilter;
import org.bonitasoft.studio.expression.editor.provider.IExpressionNatureProvider;
import org.bonitasoft.studio.expression.editor.viewer.ExpressionCollectionViewer;
import org.bonitasoft.studio.expression.editor.viewer.ExpressionViewer;
import org.bonitasoft.studio.model.expression.Expression;
import org.bonitasoft.studio.model.expression.ExpressionFactory;
import org.bonitasoft.studio.model.expression.ListExpression;
import org.bonitasoft.studio.model.expression.TableExpression;
import org.bonitasoft.studio.model.process.AbstractProcess;
import org.bonitasoft.studio.model.process.CorrelationTypeActive;
import org.bonitasoft.studio.model.process.MainProcess;
import org.bonitasoft.studio.model.process.Message;
import org.bonitasoft.studio.model.process.ProcessFactory;
import org.bonitasoft.studio.model.process.ProcessPackage;
import org.bonitasoft.studio.model.process.ThrowMessageEvent;
import org.bonitasoft.studio.pics.Pics;
import org.bonitasoft.studio.properties.i18n.Messages;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.ValidationStatusProvider;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * @author Romain Bioteau
 * @author Aurelien Pupier - integrate Correlation andf use databinding
 * @author Aurelie Zara -integrate MessageContent Id and validation
 */
public class AddMessageEventWizardPage extends WizardPage implements IWizardPage {

	private final ThrowMessageEvent element;
	private final Message originalMessage;
	private String throwEvent;
	private GridData gd;

	private ExpressionViewer elementExpressionViewer;
	private ExpressionViewer processExpressionViewer;

	private final Message workingCopyMessage;
	protected DataBindingContext databindingContext;
	protected boolean allowSortOnCorrelation = true;
	private CatchMessageEventNamesExpressionNatureProvider catchEventNatureProvider;

	private WizardPageSupport pageSupport;

	protected MainProcess diagram;
	private  Text nameText;
	/**
	 * @param performer
	 * @param pageName
	 */
	protected AddMessageEventWizardPage(MainProcess diagram,final ThrowMessageEvent element,Message originalMessage, Message workingCopyMessage) {
		super(Messages.messageEventAddWizardPageName,Messages.messageEventAddWizardPageTitle,Pics.getWizban());
		setDescription(Messages.messageEventAddWizardPageDesc);
		this.element = element ;
		this.originalMessage = originalMessage;
		if(originalMessage != null){
			if(originalMessage.getCorrelation() == null){
				originalMessage.setCorrelation(ProcessFactory.eINSTANCE.createCorrelation());
			}
		}
		if(workingCopyMessage == null){
			workingCopyMessage = ProcessFactory.eINSTANCE.createMessage();
		}
		if(workingCopyMessage.getCorrelation() == null){
			workingCopyMessage.setCorrelation(ProcessFactory.eINSTANCE.createCorrelation());
		}
		this.workingCopyMessage = workingCopyMessage;
		this.diagram=diagram;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		databindingContext = new DataBindingContext();


		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).spacing(15, 10).create());

		createNameLine(composite);

		createDescriptionLine(composite);

		createProcessExpressionViewer(composite);

		createComboEventLine(composite);

		createCorrelationLine(composite);

		createDataLine(composite);

		pageSupport = WizardPageSupport.create(this, databindingContext);
		setControl(composite);
	}

	private void createCorrelationLine(Composite composite) {
		/* final Label correlationLabel = new Label(composite, SWT.NONE);
        correlationLabel.setLayoutData(GridDataFactory.fillDefaults().align(SWT.END, SWT.TOP).create());
        correlationLabel.setText(Messages.correlation);*/

		final Group correlationGroup = new Group(composite, SWT.NONE);
		correlationGroup.setText(Messages.correlation);
		correlationGroup.setLayout(GridLayoutFactory.fillDefaults().margins(10, 10).create());
		correlationGroup.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).span(2,1).create());

		final Composite buttonCorrelationComposite = new Composite(correlationGroup, SWT.NONE);
		buttonCorrelationComposite.setLayout(GridLayoutFactory.fillDefaults().numColumns(3).create());
		buttonCorrelationComposite.setLayoutData(GridDataFactory.swtDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).create());
		final Button inactiveButton = new Button(buttonCorrelationComposite, SWT.RADIO);
		inactiveButton.setText(Messages.noCorrelation);
		inactiveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				if(inactiveButton.getSelection()){
					workingCopyMessage.getCorrelation().setCorrelationType(CorrelationTypeActive.INACTIVE);
				}
			}
		});

		final Button useCorrelationButton = new Button(buttonCorrelationComposite, SWT.RADIO);
		useCorrelationButton.setText(Messages.useCorrelationkeys);
		useCorrelationButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				if(useCorrelationButton.getSelection()){
					workingCopyMessage.getCorrelation().setCorrelationType(CorrelationTypeActive.KEYS);
					updateValidationStatus(AddMessageEventWizardPage.this.hasAtLeastOneCorrelation(workingCopyMessage.getCorrelation().getCorrelationAssociation()));
				}
			}
		});

		if(CorrelationTypeActive.KEYS.equals(workingCopyMessage.getCorrelation().getCorrelationType())){
			useCorrelationButton.setSelection(true);
			updateValidationStatus(AddMessageEventWizardPage.this.hasAtLeastOneCorrelation(workingCopyMessage.getCorrelation().getCorrelationAssociation()));
		} else {
			inactiveButton.setSelection(true);
		}

		final ControlDecoration correlationHelp = new ControlDecoration(useCorrelationButton,SWT.RIGHT);
		correlationHelp.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_INFO_TSK));
		// correlationHelp.setMarginWidth(5);
		correlationHelp.setDescriptionText(Messages.correlationHelp);



		final Composite correlationComposite = new Composite(correlationGroup, SWT.NONE);
		correlationComposite.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create()) ;
		correlationComposite.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).create());

		final List<String> captions = new ArrayList<String>(2);
		captions.add(Messages.correlationKey);
		captions.add(Messages.correlationValue);

		final ExpressionCollectionViewer ecv = new ExpressionCollectionViewer(correlationComposite, 5, false, 2, true, captions, false, true);
		ecv.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		ecv.setAddRowLabel(Messages.AddCorrelation);
		ecv.setRemoveRowLabel(Messages.removeCorrelation);
		final List<ViewerFilter> filters = new ArrayList<ViewerFilter>(2);
		filters.add(new AvailableExpressionTypeFilter(new String[]{ ExpressionConstants.CONSTANT_TYPE})) ;
		filters.add(new AvailableExpressionTypeFilter(new String[]{ ExpressionConstants.CONSTANT_TYPE,
				ExpressionConstants.PARAMETER_TYPE,
				ExpressionConstants.SCRIPT_TYPE,
				ExpressionConstants.VARIABLE_TYPE})); //Second column has everything except Form field and simulation type
		ecv.setViewerFilters(filters);
		ecv.setInput(element);

		final TableExpression correlationAssociation = getCorrelationTable();
		ecv.setSelection(correlationAssociation);
		ecv.addModifyListener(new Listener() {
			@Override
			public void handleEvent(Event event) {
				IStatus status = AddMessageEventWizardPage.this.validateId(workingCopyMessage.getCorrelation().getCorrelationAssociation(),Messages.correlation);
				updateValidationStatus(status);
			}
		});

		/*I would prefer to bind to composite parent but the enablement is not propagated to children*/
		final ISWTObservableValue observeSelectionUseCorrelation = SWTObservables.observeSelection(useCorrelationButton);


		databindingContext.bindValue(SWTObservables.observeEnabled(ecv.getViewer().getControl()), observeSelectionUseCorrelation);
		databindingContext.bindValue(SWTObservables.observeEnabled(ecv.getAddRowButton()), observeSelectionUseCorrelation);
		databindingContext.bindValue(SWTObservables.observeEnabled(ecv.getRemoveRowButton()), observeSelectionUseCorrelation);


	}


	protected TableExpression getCorrelationTable() {
		TableExpression correlationAssociation = workingCopyMessage.getCorrelation().getCorrelationAssociation();
		if(correlationAssociation == null){
			workingCopyMessage.getCorrelation().setCorrelationAssociation(ExpressionFactory.eINSTANCE.createTableExpression());
			correlationAssociation = workingCopyMessage.getCorrelation().getCorrelationAssociation();
		}
		return correlationAssociation;
	}

	protected void updateValidationStatus(IStatus status) {
		if(status.isOK()){
			setErrorMessage(null);
			Iterator<?> it = databindingContext.getValidationStatusProviders().iterator();
			while (it.hasNext()) {
				final ValidationStatusProvider provider = (ValidationStatusProvider) it.next();
				final IStatus iStatus = (IStatus)provider.getValidationStatus().getValue();
				if(!iStatus.isOK()){
					setErrorMessage(iStatus.getMessage());
				} else {
					setPageComplete(true);
				}
			}
			setPageComplete(isPageComplete());
		}else{
			setErrorMessage(status.getMessage());
			setPageComplete(false);
		}
	}

	private void createDataLine(Composite composite) {
		/*final Label addDataLabel =  new Label(composite, SWT.NONE);
        addDataLabel.setLayoutData(GridDataFactory.fillDefaults().align(SWT.END, SWT.TOP).create()) ;
        addDataLabel.setText(Messages.addMessageContent);*/

		Group group = new Group(composite, SWT.NONE);
		group.setLayout(GridLayoutFactory.fillDefaults().margins(10, 10).create());
		group.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).span(2,1).create());
		group.setText(Messages.addMessageContent);
		final List<String> captions = new ArrayList<String>(2);
		captions.add(Messages.messageContentID);
		captions.add(Messages.expressionName);

		final ExpressionCollectionViewer ecv = new ExpressionCollectionViewer(group,0, false, 2, true, captions, false, false);
		ecv.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		ecv.setAddRowLabel(Messages.addMessageContentButton);
		ecv.setRemoveRowLabel(Messages.removeMessageContent);

		final List<ViewerFilter> filters = new ArrayList<ViewerFilter>(2);
		filters.add(new AvailableExpressionTypeFilter(new String[]{ ExpressionConstants.CONSTANT_TYPE})) ;
		filters.add(new AvailableExpressionTypeFilter(new String[]{ExpressionConstants.CONSTANT_TYPE,ExpressionConstants.SCRIPT_TYPE,ExpressionConstants.PARAMETER_TYPE,ExpressionConstants.VARIABLE_TYPE}));
		ecv.setViewerFilters(filters);
		ecv.setInput(element);

		final TableExpression messageContent = getMessageContentTable();
		ecv.setSelection(messageContent);

		ecv.addModifyListener(new Listener() {
			@Override
			public void handleEvent(Event event) {
				final IStatus status = AddMessageEventWizardPage.this.validateId(workingCopyMessage.getMessageContent(),Messages.addMessageContent);
				updateValidationStatus(status);
			}
		});
		ecv.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
	}


	protected TableExpression getMessageContentTable() {
		TableExpression messageContent = workingCopyMessage.getMessageContent();
		if(messageContent == null){
			workingCopyMessage.setMessageContent(ExpressionFactory.eINSTANCE.createTableExpression());
			messageContent = workingCopyMessage.getMessageContent();
		}
		return messageContent;
	}


	protected IStatus hasAtLeastOneCorrelation(TableExpression expTable){
		if (expTable==null){
			return ValidationStatus.error(Messages.oneCorrelationAtLeastNeeded);
		} else
			if (expTable.getExpressions().isEmpty()){
				return ValidationStatus.error(Messages.oneCorrelationAtLeastNeeded);
			} 
		return Status.OK_STATUS;
	}

	protected IStatus validateId(TableExpression expTable,String tableName) {
		final Set<String> ids = new HashSet<String>();
		String duplicateId = null;

		for (ListExpression row : expTable.getExpressions()){
			if(row.getExpressions().size() > 0){
				final Expression expr = row.getExpressions().get(0);
				final Expression value = row.getExpressions().get(1);
				if (ExpressionConstants.CONSTANT_TYPE.equals(expr.getType())){
					final String id = expr.getName();
					if (id != null  && ids.contains(id)){
						duplicateId = id;
						return ValidationStatus.error(Messages.bind(Messages.dublicateIdErrorMessage,duplicateId, tableName));
					} else {
						if (ids!=null && id!=null && !id.isEmpty()){
							ids.add(id);
						}
						if (id!=null && !id.isEmpty() && value.getName()==null){
							return ValidationStatus.error(Messages.bind(Messages.valueShouldBeDefined,id));
						}
						if ((id==null || id.isEmpty()) && value.getName()!=null){
							return ValidationStatus.error(Messages.bind(Messages.idShouldBeDefined, value.getName()));
						}
					}
				}
			}
			if (tableName.equals(Messages.correlation)){
				if (CorrelationTypeActive.KEYS.equals(workingCopyMessage.getCorrelation().getCorrelationType())){
					if (ids.isEmpty()){
						return ValidationStatus.error(Messages.oneCorrelationAtLeastNeeded);
					}
				}
			}
			if(elementExpressionViewer==null || elementExpressionViewer.getTextControl().getText().isEmpty()){
				return ValidationStatus.error(Messages.eventNameLabel+ " "+Messages.isMandatory);
			}
			if(processExpressionViewer==null || processExpressionViewer.getTextControl().getText().isEmpty()){
				return ValidationStatus.error(Messages.processNameLabel+" "+ Messages.isMandatory);
			}
			if(nameText.getText() == null || nameText.getText().isEmpty()){
				return ValidationStatus.error(Messages.emptyName);
			}
		}



		return Status.OK_STATUS ;
	}




	private void createComboEventLine(Composite composite) {
		Label eventNameLabel = new Label(composite, SWT.NONE);
		eventNameLabel.setText(Messages.eventNameLabel+" *");
		eventNameLabel.setLayoutData(GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).create()) ;

		elementExpressionViewer = new ExpressionViewer(composite, SWT.BORDER,ProcessPackage.Literals.MESSAGE__TARGET_ELEMENT_EXPRESSION);
		elementExpressionViewer.getControl().setLayoutData(gd);
		elementExpressionViewer.addFilter(new AvailableExpressionTypeFilter(new String[]{ExpressionConstants.CONSTANT_TYPE,ExpressionConstants.SCRIPT_TYPE,ExpressionConstants.PARAMETER_TYPE,ExpressionConstants.VARIABLE_TYPE}));
		elementExpressionViewer.setMessage(Messages.targetEventMessageHint,IStatus.INFO);
		elementExpressionViewer.setMandatoryField(Messages.eventNameLabel,databindingContext);
		elementExpressionViewer.setContext(element);
		catchEventNatureProvider = new CatchMessageEventNamesExpressionNatureProvider();
		elementExpressionViewer.setExpressionNatureProvider(catchEventNatureProvider);
		if(workingCopyMessage.getTargetElementExpression() == null){
			final Expression createExpression = ExpressionFactory.eINSTANCE.createExpression();
			createExpression.setReturnTypeFixed(true);
			createExpression.setReturnType(String.class.getName());
			workingCopyMessage.setTargetElementExpression(createExpression);
		}
		elementExpressionViewer.setInput(workingCopyMessage);

		refreshTargetEventContent() ;

		databindingContext.bindValue(ViewersObservables.observeSingleSelection(elementExpressionViewer), EMFObservables.observeValue(workingCopyMessage, ProcessPackage.Literals.MESSAGE__TARGET_ELEMENT_EXPRESSION));

	}

	private void createProcessExpressionViewer(Composite composite) {
		final Label processNameLabel = new Label(composite, SWT.NONE);
		processNameLabel.setLayoutData(GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).create()) ;
		processNameLabel.setText(Messages.processNameLabel + " *");

		processExpressionViewer = new ExpressionViewer(composite, SWT.BORDER,ProcessPackage.Literals.MESSAGE__TARGET_PROCESS_EXPRESSION);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		processExpressionViewer.getControl().setLayoutData(gd);
		processExpressionViewer.addFilter(new AvailableExpressionTypeFilter(new String[]{ExpressionConstants.CONSTANT_TYPE,ExpressionConstants.SCRIPT_TYPE,ExpressionConstants.PARAMETER_TYPE,ExpressionConstants.VARIABLE_TYPE}));
		processExpressionViewer.setMandatoryField(Messages.processNameLabel,databindingContext);
		processExpressionViewer.setMessage(Messages.targetProcessMessageHint,IStatus.INFO);
		final IExpressionNatureProvider provider = new ProcessNamesExpressionNatureProviderForMessage();
		provider.setContext(element);
		processExpressionViewer.setExpressionNatureProvider(provider);
		processExpressionViewer.setContext(element);
		if(workingCopyMessage.getTargetProcessExpression() == null){
			final Expression createExpression = ExpressionFactory.eINSTANCE.createExpression();
			createExpression.setReturnTypeFixed(true);
			createExpression.setReturnType(String.class.getName());
			workingCopyMessage.setTargetProcessExpression(createExpression);
		}
		processExpressionViewer.setInput(workingCopyMessage);

		databindingContext.bindValue(ViewersObservables.observeSingleSelection(processExpressionViewer), EMFObservables.observeValue(workingCopyMessage, ProcessPackage.Literals.MESSAGE__TARGET_PROCESS_EXPRESSION));
		processExpressionViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				refreshTargetEventContent();
			}
		});
	}


	private Text createDescriptionLine(Composite composite) {
		Label descLabel = new Label(composite, SWT.NONE);
		descLabel.setLayoutData(GridDataFactory.fillDefaults().align(SWT.END, SWT.TOP).create());
		descLabel.setText(Messages.dataDescriptionLabel);
		final Text descText = new Text(composite, SWT.MULTI | SWT.BORDER);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.heightHint = 45 ;
		descText.setLayoutData(gd);
		databindingContext.bindValue(SWTObservables.observeDelayedValue(200, SWTObservables.observeText(descText, SWT.Modify)), EMFObservables.observeValue(workingCopyMessage, ProcessPackage.Literals.ELEMENT__DOCUMENTATION));
		return descText;
	}

	private Text createNameLine(Composite composite) {
		Label nameLabel = new Label(composite, SWT.NONE);
		nameLabel.setLayoutData(GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).create());
		nameLabel.setText(Messages.dataNameLabel);

		nameText = new Text(composite, SWT.BORDER);
		nameText.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		IValidator nameValidator = new IValidator() {

			@Override
			public IStatus validate(Object arg0) {
				if(arg0 instanceof String){
					String s = (String) arg0;
					if(s == null || s.isEmpty()){
						return ValidationStatus.error(Messages.emptyName);
					} else {
						List<Message> events = ModelHelper.getAllItemsOfType(ModelHelper.getMainProcess(element), ProcessPackage.eINSTANCE.getMessage()) ;
						for(Message ev : events){
							if(!ev.equals(originalMessage) && ev.getName().equals(s)){
								return ValidationStatus.error(Messages.messageEventAddWizardNameAlreadyExists) ;
							}
						}
					}
				}
				return ValidationStatus.ok();
			}
		};

		UpdateValueStrategy uvs = new UpdateValueStrategy(/*UpdateValueStrategy.POLICY_CONVERT*/);
		uvs.setBeforeSetValidator(nameValidator);
		databindingContext.bindValue(SWTObservables.observeDelayedValue(200, SWTObservables.observeText(nameText, SWT.Modify)), EMFObservables.observeValue(workingCopyMessage, ProcessPackage.Literals.ELEMENT__NAME),uvs,null);
		return nameText;
	}

	/**
	 * Refresh target combo content
	 */
	private void refreshTargetEventContent(){
		if(processExpressionViewer.getSelection() != null && !processExpressionViewer.getSelection().isEmpty()){
			Expression procName = (Expression) ((StructuredSelection) processExpressionViewer.getSelection()).getFirstElement() ;
			if(procName.getType().equals(ExpressionConstants.CONSTANT_TYPE)){
				DiagramRepositoryStore store = 	(DiagramRepositoryStore) RepositoryManager.getInstance().getRepositoryStore(DiagramRepositoryStore.class);
				List<AbstractProcess> otherProcesses = store.findProcesses(procName.getContent());
				AbstractProcess proc = getProcessOnDiagram(ModelHelper.getMainProcess(element),procName.getContent())  ;
				if(proc != null){
					otherProcesses.add(proc);
				}
				catchEventNatureProvider.setFoundProcesses(otherProcesses);	
			}else{
				catchEventNatureProvider.setFoundProcesses(null);
			}
			elementExpressionViewer.updateAutocompletionProposals();
		}
	}


	private AbstractProcess getProcessOnDiagram(MainProcess mainProcess,String procName) {
		for(AbstractProcess proc : ModelHelper.getAllProcesses(mainProcess)){
			if(proc.getName().equals(procName)) {
				return proc;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.DialogPage#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		if(pageSupport != null){
			pageSupport.dispose();
		}
		if(databindingContext != null){
			databindingContext.dispose();
		}
	}

	public String getThrowEvent() {
		return throwEvent;
	}



}
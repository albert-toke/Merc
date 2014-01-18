package merc.controller.listeners;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import merc.gui.enums.ActionButtonOptionsEnum;
import merc.gui.enums.SearchOptionsEnum;
import merc.gui.views.MercView;
import merc.gui.views.MercViewBidHelper;
import merc.gui.views.MercViewDetailsHelper;
import merc.gui.views.MercViewMessageHelper;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import proxy.Proxy;

import common.wrappers.Bid;
import common.wrappers.BidRequest;
import common.wrappers.Job;
import common.wrappers.Message;
import common.wrappers.Project;

import constants.and.enums.DirectionEnum;
import constants.and.enums.JobStatusEnum;
import constants.and.enums.MercPluginConstants;
import exceptions.BusinessException;

public class MercSelectionListener implements SelectionListener {

    private static final Logger LOGGER = Logger.getLogger(MercPluginConstants.LOGGER_NAME);

    private MercView view;
    private SearchListenerHelper helper;
    private Proxy proxy;
    private IEclipsePreferences preferences;

    private static final String EMPTY = "";

    public MercSelectionListener() {
	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	view = (MercView) page.findView(MercView.ID);
	helper = SearchListenerHelper.getInstance();
	proxy = Proxy.getInstance();
	preferences = ConfigurationScope.INSTANCE.getNode("merc.plugin.preferences");
    }

    @Override
    public void widgetSelected(SelectionEvent e) {

	if (e.getSource() == view.getSearchButton() && !view.getSearchCombo().getText().isEmpty()) {
	    helper.handleSearch();
	} else if (e.getSource() == view.getSearchCombo()) {
	    searchComboBoxLogic();
	} else if (e.getSource() == MercViewDetailsHelper.getReturnButton()) {
	    view.createTableViewer();
	} else if (e.getSource() == MercViewBidHelper.getBidButton()) {
	    switch (ActionButtonOptionsEnum.getEnumByValue(MercViewBidHelper.getBidButton().getText())) {
	    case PLACE_BID:
		placeBidLogic();
		break;
	    case ACCEPT:
		acceptBidWon();
		break;
	    case WITHDRAW:
		withdrawBid();
		break;
	    default:
		throw new RuntimeException("Unknown case for Action Button!");
	    }
	} else if (e.getSource() == MercViewBidHelper.getDeclineButton()) {
	    declineBidWon();
	} else if (e.getSource() == MercViewMessageHelper.getSendButton()) {
	    sendMessage();
	} else if (e.getSource() == MercViewMessageHelper.getClearButton()) {
	    clearMessage();
	}
    }

    // The logic for the search combo box.
    private void searchComboBoxLogic() {
	String selection = view.getSearchCombo().getText();
	if (selection.equals(SearchOptionsEnum.BY_TYPE.getStringValue())
		|| selection.equals(SearchOptionsEnum.BY_KEYWORD.getStringValue())) {
	    view.getSearchBar().setEnabled(true);
	    if (view.getSearchBar().getText().isEmpty()) {
		view.getSearchButton().setEnabled(false);
	    }
	    view.getTableViewer().getTable().clearAll();
	    view.getTableViewer().getTable().forceFocus();
	} else if (selection.equals(SearchOptionsEnum.PROJECTS_BID_ON.getStringValue())
		|| selection.equals(SearchOptionsEnum.PROJECTS_WORKING_ON.getStringValue())
		|| selection.equals(SearchOptionsEnum.FAVOURITES.getStringValue())) {
	    view.getSearchBar().setEnabled(false);
	    view.getSearchButton().setEnabled(true);
	    view.getSearchBar().setText("");
	    helper.handleSearch();
	    view.getTableViewer().getTable().forceFocus();
	}
    }

    private boolean placeBidLogic() {
	try {
	    BidRequest bidRequest = validateFields();
	    if (bidRequest == null) {
		return false;
	    }
	    proxy.placeBid(bidRequest);
	    view.createInfoDialog("Bid Placed", "Your bid has been placed Succesfully!");
	    MercViewBidHelper.setFieldsEditableState(false);
	    MercViewBidHelper.getBidButton().setText(ActionButtonOptionsEnum.WITHDRAW.getStringValue());
	} catch (BusinessException e) {
	    LOGGER.severe("Exception thrown in MercSelectionListener.placeBidLogic:" + e.getMessage());
	    view.createErrorDialog("Bid place error", "Your bid could not be placed!");
	    return false;
	}
	return true;
    }

    private BidRequest validateFields() {
	BidRequest bidRequest = new BidRequest();
	boolean succesful = true;
	try {
	    double bidAmmount = Double.parseDouble(MercViewBidHelper.getBidAmountText().getText());
	    bidRequest.setAmount(bidAmmount);
	    MercViewBidHelper.hideBidAmountDecorator();
	} catch (NumberFormatException ex) {
	    MercViewBidHelper.showBidAmountDecorator();
	    succesful = false;
	}
	try {
	    int bidDays = Integer.parseInt(MercViewBidHelper.getBidReqTimeText().getText());
	    bidRequest.setDays(bidDays);
	    MercViewBidHelper.hideBidReqTimeDecorator();
	} catch (NumberFormatException ex) {
	    MercViewBidHelper.showBidReqTimeDecorator();
	    succesful = false;
	}

	String description = MercViewBidHelper.getBidDescriptionText().getText();
	if (description.length() >= 10 && description.length() <= 200) {
	    bidRequest.setDescription(description);
	    MercViewBidHelper.hideBidDescriptionDecorator();
	} else {
	    MercViewBidHelper.showBidDescriptionDecorator();
	    succesful = false;
	}

	if (succesful) {
	    bidRequest.setProjectId(MercViewDetailsHelper.getProject().getId());
	    bidRequest.setProvider(MercViewDetailsHelper.getProject().getProvider());
	    return bidRequest;
	} else {
	    return null;
	}
    }

    private void acceptBidWon() {
	try {
	    proxy.acceptBidWon(MercViewDetailsHelper.getProject().getId(), MercViewDetailsHelper.getProject().getProvider());
	    view.createInfoDialog("Project Accepted", "You have accepted the won project!");
	    MercViewDetailsHelper.disposeProjectComposites();
	    MercViewBidHelper.disposeProjectComposites();
	    Project project = MercViewDetailsHelper.getProject();
	    Job job = MercViewDetailsHelper.getJob();
	    job.setStatus(JobStatusEnum.ACTIVE);
	    List<Message> messages = proxy.getMessages(project.getId(), project.getBuyerId(), project.getProvider());
	    long userId = preferences.getLong("freelancer-userId", 0);
	    Bid myBid = null;
	    if (userId != 0) {
		for (Bid bid : proxy.getBidsForProject(project.getId(), project.getProvider())) {
		    if (bid.getProviderUserId() == userId) {
			myBid = bid;
			break;
		    }
		}
	    }
	    view.createProjectDetailsWithMessages(project, job, this, messages, myBid);
	} catch (BusinessException e) {
	    LOGGER.severe("Exception thrown in MercSelectionListener.acceptBidWon:" + e.getMessage());
	    view.createErrorDialog("Project Accept Error", "You could not accept the bid project!");
	}

    }

    private void declineBidWon() {
	try {
	    proxy.declineBidWon(MercViewDetailsHelper.getProject().getId(), MercViewDetailsHelper.getProject().getProvider());
	    view.createInfoDialog("Project Declined", "You have decline the won project!");
	    // return to main window
	    view.createTableViewer();
	} catch (BusinessException e) {
	    LOGGER.severe("Exception thrown in MercSelectionListener.declineBidWon:" + e.getMessage());
	    view.createErrorDialog("Project Decline Error", "You could not decline the bid project!");
	}
    }

    private void withdrawBid() {
	try {
	    proxy.retractBidFromProject(MercViewDetailsHelper.getProject().getId(), MercViewDetailsHelper.getProject()
		    .getProvider());
	    view.createInfoDialog("Bid withdrawal", "You have withdrawn your bid from the project!");
	    MercViewBidHelper.getBidButton().setText(ActionButtonOptionsEnum.PLACE_BID.getStringValue());
	    MercViewBidHelper.getBidDescriptionText().setText(EMPTY);
	    MercViewBidHelper.getBidAmountText().setText(EMPTY);
	    MercViewBidHelper.getBidReqTimeText().setText(EMPTY);
	    MercViewBidHelper.setFieldsEditableState(true);
	} catch (BusinessException e) {
	    LOGGER.severe("Exception thrown in MercSelectionListener.withdrawBid:" + e.getMessage());
	    view.createInfoDialog("Bid withdrawal Error", "You could not withdraw your bid from the project!");
	}
    }

    private void sendMessage() {
	Message msg = validateMessage();
	if (msg == null) {
	    return;
	}

	try {
	    proxy.sendMessage(msg);
	    MercViewMessageHelper.addNewOutgoingMessageToComposite(msg);
	    clearMessage();
	} catch (BusinessException e) {
	    LOGGER.severe("Exception thrown in MercSelectionListener.sendMessage:" + e.getMessage());
	    view.createInfoDialog("Message Sending Error", "There occurred an Error while trying to send the message!");
	}

    }

    private Message validateMessage() {
	Message msg = new Message();
	boolean succesful = true;

	String stringMsg = MercViewMessageHelper.getMessageTextArea().getText();
	if (stringMsg.length() > 0) {
	    msg.setText(stringMsg);
	    MercViewMessageHelper.hideMessageDecorator();
	} else {
	    MercViewMessageHelper.showMessageDecorator();
	    succesful = false;
	}

	if (succesful) {

	    msg.setProjectId(MercViewDetailsHelper.getProject().getId());
	    msg.setProvider(MercViewDetailsHelper.getProject().getProvider());
	    msg.setUsername(MercViewDetailsHelper.getProject().getBuyerUserName());
	    msg.setDirection(DirectionEnum.OUTGOING);
	    msg.setDate(new Date());
	    return msg;
	} else {
	    // TODO should not return null.
	    return null;
	}
    }

    private void clearMessage() {
	MercViewMessageHelper.getMessageTextArea().setText("");
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {

    }

}

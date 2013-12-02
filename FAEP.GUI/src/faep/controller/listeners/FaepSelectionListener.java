package faep.controller.listeners;

import java.util.Date;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import proxy.Proxy;

import common.wrappers.BidRequest;
import common.wrappers.Message;

import enums.DirectionEnum;
import exceptions.BusinessException;
import faep.gui.enums.ActionButtonOptionsEnum;
import faep.gui.enums.SearchOptionsEnum;
import faep.gui.views.FaepView;
import faep.gui.views.FaepViewBidHelper;
import faep.gui.views.FaepViewMessageHelper;

public class FaepSelectionListener implements SelectionListener {

    private FaepView view;
    private SearchListenerHelper helper;
    private Proxy proxy;

    private static final String EMPTY = "";

    public FaepSelectionListener() {
	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	view = (FaepView) page.findView(FaepView.ID);
	helper = SearchListenerHelper.getInstance();
	proxy = Proxy.getInstance();
    }

    @Override
    public void widgetSelected(SelectionEvent e) {

	if (e.getSource() == view.getSearchButton() && !view.getSearchCombo().getText().isEmpty()) {
	    searchButtonLogic();
	} else if (e.getSource() == view.getSearchCombo()) {
	    searchComboBoxLogic();
	} else if (e.getSource() == FaepViewBidHelper.getReturnButton()) {
	    view.createTableViewer();
	} else if (e.getSource() == FaepViewBidHelper.getBidButton()) {
	    switch (ActionButtonOptionsEnum.getEnumByValue(FaepViewBidHelper.getBidButton().getText())) {
	    case PLACE_BID:
		placeBidLogic();
		break;
	    case ACCEPT:
		acceptBidWon();
		break;
	    case WITHDRAW:
		withdrawBid();
		break;
	    case REQUEST_CANCEL:
		// TODO cancel project - necessary data through pop-up
		break;
	    default:
		throw new RuntimeException("Unknown case for Action Button!");
	    }
	} else if (e.getSource() == FaepViewBidHelper.getDeclineButton()) {
	    declineBidWon();
	} else if (e.getSource() == FaepViewMessageHelper.getSendButton()) {
	    sendMessage();
	} else if (e.getSource() == FaepViewMessageHelper.getClearButton()) {
	    clearMessage();
	}
    }

    // The logic for the 'GO' button.
    private void searchButtonLogic() {
	helper.handleSearch();
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
	    FaepViewBidHelper.setFieldsEditableState(false);
	    FaepViewBidHelper.getBidButton().setText(ActionButtonOptionsEnum.WITHDRAW.getStringValue());
	} catch (BusinessException e) {
	    e.printStackTrace();
	    view.createErrorDialog("Bid place error", "Your bid could not be placed!");
	    return false;
	}
	return true;
    }

    private BidRequest validateFields() {
	BidRequest bidRequest = new BidRequest();
	boolean succesful = true;
	try {
	    double bidAmmount = Double.parseDouble(FaepViewBidHelper.getBidAmountText().getText());
	    bidRequest.setAmount(bidAmmount);
	    FaepViewBidHelper.hideBidAmountDecorator();
	} catch (NumberFormatException ex) {
	    FaepViewBidHelper.showBidAmountDecorator();
	    succesful = false;
	}
	try {
	    int bidDays = Integer.parseInt(FaepViewBidHelper.getBidReqTimeText().getText());
	    bidRequest.setDays(bidDays);
	    FaepViewBidHelper.hideBidReqTimeDecorator();
	} catch (NumberFormatException ex) {
	    FaepViewBidHelper.showBidReqTimeDecorator();
	    succesful = false;
	}

	String description = FaepViewBidHelper.getBidDescriptionText().getText();
	if (description.length() >= 10 && description.length() <= 200) {
	    bidRequest.setDescription(description);
	    FaepViewBidHelper.hideBidDescriptionDecorator();
	} else {
	    FaepViewBidHelper.showBidDescriptionDecorator();
	    succesful = false;
	}

	if (succesful) {
	    bidRequest.setProjectId(FaepViewBidHelper.getProject().getId());
	    bidRequest.setProvider(FaepViewBidHelper.getProject().getProvider());
	    return bidRequest;
	} else {
	    return null;
	}
    }

    private void acceptBidWon() {
	try {
	    proxy.acceptBidWon(FaepViewBidHelper.getProject().getId(), FaepViewBidHelper.getProject().getProvider());
	    view.createInfoDialog("Project Accepted", "You have accepted the won project!");
	    // TODO Redraw details pane to active project
	    // TODO getMessages
	} catch (BusinessException e) {
	    e.printStackTrace();
	    view.createErrorDialog("Project Accept Error", "You could not accept the bid project!");
	}
    }

    private void declineBidWon() {
	try {
	    proxy.declineBidWon(FaepViewBidHelper.getProject().getId(), FaepViewBidHelper.getProject().getProvider());
	    view.createInfoDialog("Project Declined", "You have decline the won project!");
	    // TODO return to main window
	} catch (BusinessException e) {
	    e.printStackTrace();
	    view.createErrorDialog("Project Decline Error", "You could not decline the bid project!");
	}
    }

    private void withdrawBid() {
	try {
	    proxy.retractBidFromProject(FaepViewBidHelper.getProject().getId(), FaepViewBidHelper.getProject().getProvider());
	    view.createInfoDialog("Bid withdrawal", "You have withdrawn your bid from the project!");
	    FaepViewBidHelper.getBidButton().setText(ActionButtonOptionsEnum.PLACE_BID.getStringValue());
	    FaepViewBidHelper.getBidDescriptionText().setText(EMPTY);
	    FaepViewBidHelper.getBidAmountText().setText(EMPTY);
	    FaepViewBidHelper.getBidReqTimeText().setText(EMPTY);
	} catch (BusinessException e) {
	    e.printStackTrace();
	    view.createInfoDialog("Bid withdrawal Error", "You could not withdraw your bid from the project!");
	}
    }

    private void sendMessage() {
	Message msg = new Message();
	msg.setText(FaepViewMessageHelper.getMessageTextArea().getText());
	msg.setProjectId(FaepViewBidHelper.getProject().getId());
	msg.setProvider(FaepViewBidHelper.getProject().getProvider());
	msg.setUsername(FaepViewBidHelper.getProject().getBuyerUserName());
	msg.setDirection(DirectionEnum.OUTGOING);
	msg.setDate(new Date());

	try {
	    proxy.sendMessage(msg);
	    FaepViewMessageHelper.addNewOutgoingMessageToComposite(msg);
	    clearMessage();
	} catch (BusinessException e) {
	    e.printStackTrace();
	    view.createInfoDialog("Message Sending Error", "There occurred an Error while trying to send the message!");
	}

    }

    private void clearMessage() {
	FaepViewMessageHelper.getMessageTextArea().setText("");
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {

    }

}

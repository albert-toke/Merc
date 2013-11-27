package faep.controller.listeners;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import proxy.Proxy;

import common.wrappers.BidRequest;

import exceptions.BusinessException;
import faep.gui.enums.ActionButtonOptionsEnum;
import faep.gui.enums.SearchOptionsEnum;
import faep.gui.views.FaepView;
import faep.gui.views.FaepViewHelper;

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
	} else if (e.getSource() == FaepViewHelper.getReturnButton()) {
	    view.createTableViewer();
	} else if (e.getSource() == FaepViewHelper.getBidButton()) {
	    switch (ActionButtonOptionsEnum.getEnumByValue(FaepViewHelper.getBidButton().getText())) {
	    case PLACE_BID:
		placeBidLogic();
		break;
	    case ACCEPT:
		acceptBidWon();
		break;
	    case WITHDRAW:
		withdrawBid();
		break;
	    case POST_MESSAGE:
		// TODO post message
		break;
	    default:
		throw new RuntimeException("Unknown case for Action Button!");
	    }
	} else if (e.getSource() == FaepViewHelper.getDeclineButton()) {
	    declineBidWon();
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

	} else if (selection.equals(SearchOptionsEnum.PROJECTS_BID_ON.getStringValue())
		|| selection.equals(SearchOptionsEnum.PROJECTS_WORKING_ON.getStringValue())
		|| selection.equals(SearchOptionsEnum.FAVOURITES.getStringValue())) {
	    view.getSearchBar().setEnabled(false);
	    view.getSearchButton().setEnabled(true);
	    view.getSearchBar().setText("");
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
	    FaepViewHelper.dissableMyBidWidgets();
	    FaepViewHelper.getBidButton().setText(ActionButtonOptionsEnum.WITHDRAW.getStringValue());
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
	    double bidAmmount = Double.parseDouble(FaepViewHelper.getBidAmountText().getText());
	    bidRequest.setAmount(bidAmmount);
	    FaepViewHelper.hideBidAmountDecorator();
	} catch (NumberFormatException ex) {
	    FaepViewHelper.showBidAmountDecorator();
	    succesful = false;
	}
	try {
	    int bidDays = Integer.parseInt(FaepViewHelper.getBidReqTimeText().getText());
	    bidRequest.setDays(bidDays);
	    FaepViewHelper.hideBidReqTimeDecorator();
	} catch (NumberFormatException ex) {
	    FaepViewHelper.showBidReqTimeDecorator();
	    succesful = false;
	}

	String description = FaepViewHelper.getBidDescriptionText().getText();
	if (description.length() >= 10 && description.length() <= 200) {
	    bidRequest.setDescription(description);
	    FaepViewHelper.hideBidDescriptionDecorator();
	} else {
	    FaepViewHelper.showBidDescriptionDecorator();
	    succesful = false;
	}

	if (succesful) {
	    bidRequest.setProjectId(FaepViewHelper.getProjectId());
	    bidRequest.setProvider(FaepViewHelper.getProvider());
	    return bidRequest;
	} else {
	    return null;
	}
    }

    private void acceptBidWon() {
	try {
	    proxy.acceptBidWon(FaepViewHelper.getProjectId(), FaepViewHelper.getProvider());
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
	    proxy.declineBidWon(FaepViewHelper.getProjectId(), FaepViewHelper.getProvider());
	    view.createInfoDialog("Project Declined", "You have decline the won project!");
	    // TODO return to main window
	} catch (BusinessException e) {
	    e.printStackTrace();
	    view.createErrorDialog("Project Decline Error", "You could not decline the bid project!");
	}
    }

    private void withdrawBid() {
	try {
	    proxy.retractBidFromProject(FaepViewHelper.getProjectId(), FaepViewHelper.getProvider());
	    view.createInfoDialog("Bid withdrawal", "You have withdrawn your bid from the project!");
	    FaepViewHelper.getBidButton().setText(ActionButtonOptionsEnum.PLACE_BID.getStringValue());
	    FaepViewHelper.getBidDescriptionText().setText(EMPTY);
	    FaepViewHelper.getBidAmountText().setText(EMPTY);
	    FaepViewHelper.getBidReqTimeText().setText(EMPTY);
	} catch (BusinessException e) {
	    e.printStackTrace();
	    view.createInfoDialog("Bid withdrawal Error", "You could not withdraw your bid from the project!");
	}
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {

    }

}

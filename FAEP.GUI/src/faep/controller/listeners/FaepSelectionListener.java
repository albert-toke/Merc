package faep.controller.listeners;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import faep.gui.enums.SearchOptionsEnum;
import faep.gui.views.FaepView;
import faep.gui.views.FaepViewHelper;

public class FaepSelectionListener implements SelectionListener {

    private FaepView view;
    private SearchListenerHelper helper;

    public FaepSelectionListener() {
	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	view = (FaepView) page.findView(FaepView.ID);
	helper = SearchListenerHelper.getInstance();
    }

    @Override
    public void widgetSelected(SelectionEvent e) {

	if (e.getSource() == view.getSearchButton() && !view.getSearchCombo().getText().isEmpty()) {
	    searchButtonLogic();
	} else if (e.getSource() == view.getSearchCombo()) {
	    searchComboBoxLogic();
	} else if (e.getSource() == FaepViewHelper.getReturnButton()) {
	    view.createTableViewer();
	}
    }

    // The logic for the 'GO' button.
    private void searchButtonLogic() {
	// view.removeExpandItems();
	helper.handleSearch();
    }

    // The logic for the search combo box.
    private void searchComboBoxLogic() {
	String selection = view.getSearchCombo().getText();
	if (selection.equals(SearchOptionsEnum.BY_TYPE.getStringValue())
		|| selection.equals(SearchOptionsEnum.BY_KEYWORD.getStringValue())) {
	    view.getSearchBar().setEnabled(true);
	} else if (selection.equals(SearchOptionsEnum.PROJECTS_BID_ON.getStringValue())
		|| selection.equals(SearchOptionsEnum.PROJECTS_WORKING_ON.getStringValue())) {
	    view.getSearchBar().setEnabled(false);
	}
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {

    }

}

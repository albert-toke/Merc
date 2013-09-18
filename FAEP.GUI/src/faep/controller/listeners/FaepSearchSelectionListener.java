package faep.controller.listeners;

import java.util.List;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import proxy.Proxy;

import common.wrappers.Job;
import common.wrappers.JobSearch;

import exceptions.BusinessException;
import faep.gui.enums.SearchOptionsEnum;
import faep.gui.views.FaepView;

public class FaepSearchSelectionListener implements SelectionListener {

    private FaepView view;
    private Proxy proxy;

    public FaepSearchSelectionListener() {
	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	view = (FaepView) page.findView(FaepView.ID);
	proxy = Proxy.getInstance();
    }

    @Override
    public void widgetSelected(SelectionEvent e) {

	if (e.getSource() == view.getSearchButton() && !view.getSearchCombo().getText().isEmpty()) {
	    searchButtonLogic();
	} else if (e.getSource() == view.getSearchCombo()) {
	    searchComboBoxLogic();
	}
    }

    // The logic for the 'GO' button.
    private void searchButtonLogic() {
	view.removeExpandItems();
	JobSearch searchParams = new JobSearch();
	searchParams.setCount(30);
	String searchType = view.getSearchCombo().getText();
	String searchWord = view.getSearchBar().getText();

	if (searchType.equals(SearchOptionsEnum.BY_TYPE.getStringValue())) {
	    searchParams.setSearchJobTypeCSV(searchWord);
	    searchParams.setSearchKeyword("");
	    addJobSearhResultsToView(view, searchParams);
	} else if (searchType.equals(SearchOptionsEnum.BY_KEYWORD.getStringValue())) {
	    searchParams.setSearchKeyword(searchWord);
	    searchParams.setSearchJobTypeCSV("");
	    addJobSearhResultsToView(view, searchParams);
	} else if (searchType.equals(SearchOptionsEnum.PROJECTS_BID_ON.getStringValue())) {
	    addBiddedProjectsToView(view);
	} else if (searchType.equals(SearchOptionsEnum.PROJECTS_WORKING_ON.getStringValue())) {
	}
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

    private void addJobSearhResultsToView(FaepView view, JobSearch searchParams) {
	List<Job> jobList = proxy.searchJobs(searchParams);
	view.addExpandItems(jobList);
    }

    private void addBiddedProjectsToView(FaepView view) {
	List<Job> jobList;
	try {
	    jobList = proxy.getBiddedProjects();
	    view.addExpandItems(jobList);
	} catch (BusinessException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {

    }

}

package faep.controller.listeners;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import proxy.Proxy;

import common.wrappers.Job;
import common.wrappers.JobSearch;

import exceptions.BusinessException;
import faep.gui.enums.SearchOptionsEnum;
import faep.gui.views.FaepView;

public class SearchBarListener implements TraverseListener {

    private FaepView view;
    private Proxy proxy;

    public SearchBarListener() {
	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	view = (FaepView) page.findView(FaepView.ID);
	proxy = Proxy.getInstance();
    }

    @Override
    public void keyTraversed(TraverseEvent e) {
	if (e.detail == SWT.TRAVERSE_RETURN) {
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

    }

    private void addJobSearhResultsToView(FaepView view, JobSearch searchParams) {

	List<Job> jobList = proxy.searchJobs(searchParams);
	view.getTableViewer().setInput(jobList);
	// view.addExpandItems(jobList);
    }

    private void addBiddedProjectsToView(FaepView view) {
	@SuppressWarnings("unused")
	List<Job> jobList;
	try {
	    jobList = proxy.getBiddedProjects();
	    // view.addExpandItems(jobList);
	} catch (BusinessException e) {
	    e.printStackTrace();
	}
    }

}

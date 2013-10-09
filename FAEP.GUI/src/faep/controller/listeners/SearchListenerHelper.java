package faep.controller.listeners;

import java.util.List;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import proxy.Proxy;

import common.wrappers.Job;
import common.wrappers.JobSearch;

import exceptions.BusinessException;
import faep.gui.enums.SearchOptionsEnum;
import faep.gui.views.FaepView;

public class SearchListenerHelper {

    private static SearchListenerHelper helper;
    private List<Job> jobList;
    private FaepView view;
    private Proxy proxy;

    private SearchListenerHelper() {
	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	view = (FaepView) page.findView(FaepView.ID);
	proxy = Proxy.getInstance();
    }

    public static SearchListenerHelper getInstance() {
	if (helper == null) {
	    helper = new SearchListenerHelper();
	}
	return helper;
    }

    public void handleSearch() {
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

    private void addJobSearhResultsToView(FaepView view, JobSearch searchParams) {
	jobList = proxy.searchJobs(searchParams);
	view.getTableViewer().setInput(jobList);
    }

    private void addBiddedProjectsToView(FaepView view) {
	try {
	    jobList = proxy.getBiddedProjects();
	} catch (BusinessException e) {
	    e.printStackTrace();
	}
    }
}

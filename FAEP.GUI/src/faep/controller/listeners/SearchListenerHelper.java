package faep.controller.listeners;

import java.util.List;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import proxy.Proxy;
import business.services.HistoryBS;

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
    private IEclipsePreferences preferences;

    private SearchListenerHelper() {
	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	view = (FaepView) page.findView(FaepView.ID);
	proxy = Proxy.getInstance();
	preferences = ConfigurationScope.INSTANCE.getNode("faep.plugin.preferences");
    }

    public static SearchListenerHelper getInstance() {
	if (helper == null) {
	    helper = new SearchListenerHelper();
	}
	return helper;
    }

    public void handleSearch() {
	JobSearch searchParams = new JobSearch();
	searchParams.setCount(75);
	String searchType = view.getSearchCombo().getText();
	String searchWord = view.getSearchBar().getText();
	if (searchType.equals(SearchOptionsEnum.FAVOURITES.getStringValue())) {
	    searchParams.setSearchJobTypeCSV(preferences.get("jobList", ""));
	    searchParams.setSearchKeyword("");
	    addJobSearhResultsToView(view, searchParams);
	} else if (searchType.equals(SearchOptionsEnum.BY_TYPE.getStringValue())) {
	    searchParams.setSearchJobTypeCSV(searchWord);
	    searchParams.setSearchKeyword("");
	    addJobSearhResultsToView(view, searchParams);
	    try {
		HistoryBS.getInstance().addNewSearchEntry(searchWord);
	    } catch (BusinessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
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

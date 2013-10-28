package faep.controller.listeners;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import proxy.Proxy;

import common.wrappers.Bid;
import common.wrappers.Job;
import common.wrappers.Project;

import exceptions.BusinessException;
import faep.controller.FaepController;
import faep.gui.enums.SearchOptionsEnum;
import faep.gui.views.FaepView;

public class DoubleClickListener implements IDoubleClickListener {

    private FaepView view;
    private Proxy proxy;
    private FaepController controller;
    private IEclipsePreferences preferences;

    public DoubleClickListener() {
	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	view = (FaepView) page.findView(FaepView.ID);
	proxy = Proxy.getInstance();
	controller = FaepController.getInstance();
	preferences = ConfigurationScope.INSTANCE.getNode("faep.plugin.preferences");
    }

    /**
     * Listens for double-click events on the TableViewer.
     */
    @Override
    public void doubleClick(DoubleClickEvent event) {
	// The only type of element that can be part of the selection is Job, hence there is no verification if the selection is
	// instance of the Job Class.
	IStructuredSelection selection = (IStructuredSelection) event.getSelection();
	Job job = (Job) selection.getFirstElement();

	String comboSelection = view.getSearchCombo().getText();
	if (comboSelection.equals(SearchOptionsEnum.BY_TYPE.getStringValue())
		|| comboSelection.equals(SearchOptionsEnum.BY_KEYWORD.getStringValue())) {
	    getInfoForKeywordAndJobtypeSelection(job);
	} else if (comboSelection.equals(SearchOptionsEnum.PROJECTS_BID_ON.getStringValue())) {
	    // TODO my bid projects
	} else if (comboSelection.equals(SearchOptionsEnum.PROJECTS_WORKING_ON.getStringValue())) {
	    // TODO actual projects
	}

    }

    private void getInfoForKeywordAndJobtypeSelection(Job job) {
	Project project;
	List<Bid> bidList = new ArrayList<Bid>();
	try {
	    project = proxy.getProjectDetails(job.getProjectId(), job.getProvider());
	    bidList = proxy.getBidsForProject(job.getProjectId(), job.getProvider());
	    long userId = preferences.getLong("freelancer-userId", 0);
	    Bid myBid = null;
	    if (userId != 0) {
		for (Bid bid : bidList) {
		    if (bid.getProviderUserId() == userId) {
			myBid = bid;
			break;
		    }
		}
		if (myBid != null) {
		    bidList.remove(myBid);
		}
	    }

	    view.createProjectDetailsWithBids(project, job, controller.getSelectionListener(), bidList, myBid);

	} catch (BusinessException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}

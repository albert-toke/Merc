package faep.controller.listeners;

import java.util.ArrayList;
import java.util.List;

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
import faep.gui.views.FaepView;

public class DoubleClickListener implements IDoubleClickListener {

    private FaepView view;
    private Proxy proxy;

    public DoubleClickListener() {
	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	view = (FaepView) page.findView(FaepView.ID);
	proxy = Proxy.getInstance();
    }

    /**
     * Listens for double-click events on the TableViewer.
     */
    @Override
    public void doubleClick(DoubleClickEvent event) {
	IStructuredSelection selection = (IStructuredSelection) event.getSelection();
	if (selection.getFirstElement() instanceof Job) {
	    Job job = (Job) selection.getFirstElement();
	    Project project;
	    List<Bid> bidList = new ArrayList<Bid>();
	    try {
		project = proxy.getProjectDetails(job.getProjectId(), job.getProvider());
		bidList = proxy.getBidsForProject(job.getProjectId(), job.getProvider());
		// List<Bid> bidList;
		view.createProjectDetailsWithBids(project, job, FaepController.getInstance().getSelectionListener(), bidList,
			false);

	    } catch (BusinessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	}

    }

}

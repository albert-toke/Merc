package merc.controller;

import java.util.List;

import merc.controller.listeners.DoubleClickListener;
import merc.controller.listeners.MercPreferenceListener;
import merc.controller.listeners.MercSelectionListener;
import merc.controller.listeners.SearchBarKeyListener;
import merc.controller.listeners.SearchBarTraverseListener;
import merc.controller.provider.SearchHistoryContentProposal;
import merc.gui.views.MercView;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;

import proxy.Proxy;

import common.wrappers.Job;

import exceptions.BusinessException;

public class MercController {

    private static MercController controller;
    private IEclipsePreferences preferences;
    private MercSelectionListener searchListener;
    private DoubleClickListener doubleListener;
    private SearchBarTraverseListener searchBarListener;
    private SearchHistoryContentProposal contentProposal;
    private SearchBarKeyListener keyListener;

    private Proxy proxy;
    private List<Job> projectsBidOn;

    private MercController() {
	this.proxy = Proxy.getInstance();
    }

    /**
     * Singleton class accessing method.
     * 
     * @return Instance of the controller if initialized, otherwise initializes a new instance of Controller class.
     */
    public static MercController getInstance() {
	if (controller == null) {
	    controller = new MercController();
	}
	return controller;
    }

    /**
     * Sets this class as the controller for MercView
     * 
     * @param view
     *            Instance of the freelancer view.
     */
    public void setView(MercView view) {
	if (searchListener == null) {
	    searchListener = new MercSelectionListener();
	}
	if (doubleListener == null) {
	    doubleListener = new DoubleClickListener();
	}
	if (searchBarListener == null) {
	    searchBarListener = new SearchBarTraverseListener();
	}
	if (contentProposal == null) {
	    contentProposal = new SearchHistoryContentProposal();
	}
	if (keyListener == null) {
	    keyListener = new SearchBarKeyListener();
	}
	if (view.getSearchButton() != null && !view.getSearchButton().isDisposed() && view.getSearchCombo() != null
		&& !view.getSearchCombo().isDisposed() && view.getSearchButton().getListeners(SWT.Selection).length == 0) {
	    view.getSearchButton().addSelectionListener(searchListener);
	    view.getSearchCombo().addSelectionListener(searchListener);
	    view.getTableViewer().addDoubleClickListener(doubleListener);
	    view.getSearchBar().addTraverseListener(searchBarListener);
	    view.getSearchBar().addKeyListener(keyListener);
	    ContentProposalAdapter adapter = new ContentProposalAdapter(view.getSearchBar(), new TextContentAdapter(),
		    contentProposal, null, null);
	    adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
	}
    }

    /**
     * Sets the preference change listener from the controller the listener for the applications preference node.
     */
    public void addPreferenceChangeListener() {
	preferences = ConfigurationScope.INSTANCE.getNode("merc.plugin.preferences");
	MercPreferenceListener listener = new MercPreferenceListener();
	preferences.addPreferenceChangeListener(listener);
    }

    public MercSelectionListener getSelectionListener() {
	if (searchListener == null) {
	    searchListener = new MercSelectionListener();
	}
	return searchListener;
    }

    public DoubleClickListener getDoubleClickListener() {
	if (doubleListener == null) {
	    doubleListener = new DoubleClickListener();
	}
	return doubleListener;
    }

    // TODO maybe move this part into Proxy???
    public List<Job> getProjectsBidOn() {
	if (projectsBidOn == null) {
	    try {
		projectsBidOn = proxy.getBiddedProjects();
	    } catch (BusinessException e) {
		e.printStackTrace();
	    }
	}
	return projectsBidOn;
    }

    public void refreshProjectsBidOnList() {
	try {
	    projectsBidOn = proxy.getBiddedProjects();
	} catch (BusinessException e) {
	    e.printStackTrace();
	}
    }

    public boolean verifyProjectAlreadyBidOn(Job job) {
	if (projectsBidOn == null) {
	    try {
		projectsBidOn = proxy.getBiddedProjects();
	    } catch (BusinessException e) {
		e.printStackTrace();
	    }
	}
	if (job != null) {
	    for (Job j : projectsBidOn) {
		if (j.getProjectId() == job.getProjectId()) {
		    return true;
		}
	    }
	}
	return false;
    }
}

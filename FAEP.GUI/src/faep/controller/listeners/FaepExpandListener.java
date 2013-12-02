package faep.controller.listeners;

import java.util.List;

import org.eclipse.swt.events.ExpandEvent;
import org.eclipse.swt.events.ExpandListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import proxy.Proxy;

import common.wrappers.Bid;
import common.wrappers.Message;
import common.wrappers.Project;

import exceptions.BusinessException;
import faep.custom.widgets.FaepExpandItem;
import faep.gui.enums.SearchOptionsEnum;
import faep.gui.views.FaepView;

/**
 * Listener responsible for the lazy loading of the projects.
 * 
 * @author Jindster
 * 
 */
public class FaepExpandListener implements ExpandListener {

    /**
     * At expansion loads the data for the given project and initiates all the components.
     */
    @Override
    public void itemExpanded(ExpandEvent e) {
	FaepExpandItem item = (FaepExpandItem) e.item;
	Project project;
	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	FaepView view = (FaepView) page.findView(FaepView.ID);
	try {
	    Proxy proxy = Proxy.getInstance();
	    project = proxy.getProjectDetails(item.getId(), item.getProvider());
	    List<Bid> bidList;
	    List<Message> messageList;
	    String state = view.getSearchCombo().getText();
	    if (state.equals(SearchOptionsEnum.BY_TYPE.getStringValue())
		    || state.equals(SearchOptionsEnum.BY_KEYWORD.getStringValue())) {
		bidList = proxy.getBidsForProject(item.getId(), item.getProvider());
		item.initComponentsWithBids(project, bidList, null);
	    } else if (state.equals(SearchOptionsEnum.PROJECTS_WORKING_ON.getStringValue())) {
		// messageList = proxy.getMessages(item.getId(), item.getProvider());
		// item.initComponentsWithMessages(project, messageList, null);
	    } else if (state.equals(SearchOptionsEnum.PROJECTS_BID_ON.getStringValue())) {
		// TODO my bid highlighted
		bidList = proxy.getBidsForProject(item.getId(), item.getProvider());
		Bid myBid = getBidByUser(proxy.getUserIdByProvider(item.getProvider()), bidList);
		item.initComponentsWithBids(project, bidList, myBid);

	    }
	} catch (BusinessException e1) {
	    e1.printStackTrace();
	}
    }

    private Bid getBidByUser(long userId, List<Bid> bidList) {
	Bid myBid = null;
	for (Bid bid : bidList) {
	    if (bid.getProviderUserId() == userId) {
		myBid = bid;
		break;
	    }
	}
	return myBid;
    }

    /**
     * At collapse disposes the components and data from the given expand item.
     */
    @Override
    public void itemCollapsed(ExpandEvent e) {
	FaepExpandItem item = (FaepExpandItem) e.item;
	item.disposeComponents();
    }

    // Dummy method for creating test data
    // private Project createProjectTestData() {
    // Project project;
    // try {
    // project = ImporterClass.getProjectFromFile("D:/progz/eclipse-RCP-64-Kepler/workspace/FAEP.GUI/resources");
    // } catch (IOException | BusinessException e) {
    // project = null;
    // e.printStackTrace();
    // }
    //
    // return project;
    // }

    // private List<Bid> createBidTestData() {
    // List<Bid> bidlist = new ArrayList<Bid>();
    // for (int i = 0; i < 5; i++) {
    // Bid bid = new Bid();
    // bid.setBidAmount(300);
    // bid.setDescription("Hi, I am really interested in the project and I believe that I can complete it as per your requirement an d satisfaction. Please find my message in your inbox.");
    // bid.setMilestone("300");
    // bid.setProvider("Teh jindster");
    // bid.setRating("5");
    // bid.setSubmitTime("3 days ago");
    // bidlist.add(bid);
    // }
    // return bidlist;
    // }
}

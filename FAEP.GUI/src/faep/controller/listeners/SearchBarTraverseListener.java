package faep.controller.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import faep.gui.views.FaepView;

public class SearchBarTraverseListener implements TraverseListener {

    private SearchListenerHelper helper;
    private FaepView view;

    public SearchBarTraverseListener() {
	helper = SearchListenerHelper.getInstance();
	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	view = (FaepView) page.findView(FaepView.ID);
    }

    @Override
    public void keyTraversed(TraverseEvent e) {
	if (e.detail == SWT.TRAVERSE_RETURN && view.getSearchButton().isEnabled()) {
	    helper.handleSearch();
	}
    }

}

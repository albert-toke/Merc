package merc.controller.listeners;

import merc.gui.views.MercView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public class SearchBarTraverseListener implements TraverseListener {

    private SearchListenerHelper helper;
    private MercView view;

    public SearchBarTraverseListener() {
	helper = SearchListenerHelper.getInstance();
	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	view = (MercView) page.findView(MercView.ID);
    }

    @Override
    public void keyTraversed(TraverseEvent e) {
	if (e.detail == SWT.TRAVERSE_RETURN && view.getSearchButton().isEnabled()) {
	    helper.handleSearch();
	}
    }

}

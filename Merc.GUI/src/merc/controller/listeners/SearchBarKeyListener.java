package merc.controller.listeners;

import merc.gui.views.MercView;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public class SearchBarKeyListener implements KeyListener {

    private MercView view;

    public SearchBarKeyListener() {
	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	view = (MercView) page.findView(MercView.ID);
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
	if (e.widget == view.getSearchBar()) {
	    String searchBarContent = view.getSearchBar().getText();
	    if (!view.getSearchButton().isEnabled() && searchBarContent != null && !searchBarContent.isEmpty()) {
		view.getSearchButton().setEnabled(true);
	    } else if (searchBarContent == null || searchBarContent.isEmpty()) {
		view.getSearchButton().setEnabled(false);
	    }
	}
    }

}

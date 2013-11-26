package faep.controller.listeners;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import faep.gui.views.FaepView;

public class SearchBarKeyListener implements KeyListener {

    private FaepView view;

    public SearchBarKeyListener() {
	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	view = (FaepView) page.findView(FaepView.ID);
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

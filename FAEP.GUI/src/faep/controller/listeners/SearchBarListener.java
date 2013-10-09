package faep.controller.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;

public class SearchBarListener implements TraverseListener {

    private SearchListenerHelper helper;

    public SearchBarListener() {
	helper = SearchListenerHelper.getInstance();
    }

    @Override
    public void keyTraversed(TraverseEvent e) {
	if (e.detail == SWT.TRAVERSE_RETURN) {
	    helper.handleSearch();
	}
    }

}

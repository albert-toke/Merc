package faep.controller.listeners;

import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import faep.gui.views.FaepView;

public class FaepPreferenceListener implements IPreferenceChangeListener {

    private FaepView view;

    @Override
    public void preferenceChange(PreferenceChangeEvent event) {
	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	view = (FaepView) page.findView(FaepView.ID);
	if (event.getKey().equals("goodToGo")) {
	    boolean value = Boolean.parseBoolean((String) event.getNewValue());
	    disposeChildren();
	    if (value) {
		view.createFreelancerView();
	    } else {
		view.createErrorView();
	    }
	    view.getParent().layout(true);
	}
    }

    private void disposeChildren() {
	for (Control control : view.getParent().getChildren()) {
	    control.dispose();
	}
    }
}

package merc.controller.listeners;

import merc.gui.views.MercView;

import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public class MercPreferenceListener implements IPreferenceChangeListener {

    private MercView view;

    @Override
    public void preferenceChange(PreferenceChangeEvent event) {
	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	view = (MercView) page.findView(MercView.ID);
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

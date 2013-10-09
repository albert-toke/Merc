package faep.controller;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.swt.SWT;

import faep.controller.listeners.DoubleClickListener;
import faep.controller.listeners.FaepPreferenceListener;
import faep.controller.listeners.FaepSelectionListener;
import faep.controller.listeners.SearchBarListener;
import faep.gui.views.FaepView;

public class FaepController {

    private static FaepController controller;
    private IEclipsePreferences preferences;
    private FaepSelectionListener searchListener;
    private DoubleClickListener doubleListener;
    private SearchBarListener searchBarListener;

    private FaepController() {

    }

    /**
     * Singleton class accessing method.
     * 
     * @return Instance of the controller if initialized, otherwise initializes a new instance of Controller class.
     */
    public static FaepController getInstance() {
	if (controller == null) {
	    controller = new FaepController();
	}
	return controller;
    }

    /**
     * Sets this class as the controller for FaepView
     * 
     * @param view
     *            Instance of the freelancer view.
     */
    public void setView(FaepView view) {
	if (searchListener == null) {
	    searchListener = new FaepSelectionListener();
	}
	if (doubleListener == null) {
	    doubleListener = new DoubleClickListener();
	}
	if (searchBarListener == null) {
	    searchBarListener = new SearchBarListener();
	}
	if (view.getSearchButton() != null && view.getSearchCombo() != null
		&& view.getSearchButton().getListeners(SWT.Selection).length == 0) {
	    view.getSearchButton().addSelectionListener(searchListener);
	    view.getSearchCombo().addSelectionListener(searchListener);
	    view.getTableViewer().addDoubleClickListener(doubleListener);
	    view.getSearchBar().addTraverseListener(searchBarListener);
	}
    }

    /**
     * Sets the preference change listener from the controller the listener for the applications preference node.
     */
    public void addPreferenceChangeListener() {
	preferences = ConfigurationScope.INSTANCE.getNode("faep.plugin.preferences");
	FaepPreferenceListener listener = new FaepPreferenceListener();
	preferences.addPreferenceChangeListener(listener);
    }

    public FaepSelectionListener getSelectionListener() {
	if (searchListener == null) {
	    searchListener = new FaepSelectionListener();
	}
	return searchListener;
    }

    public DoubleClickListener getDoubleClickListener() {
	if (doubleListener == null) {
	    doubleListener = new DoubleClickListener();
	}
	return doubleListener;
    }
}

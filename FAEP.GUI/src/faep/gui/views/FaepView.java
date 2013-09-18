package faep.gui.views;

import java.util.List;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import common.wrappers.Job;

import faep.controller.listeners.ExpandBarAdapter;
import faep.controller.listeners.FaepExpandListener;
import faep.custom.widgets.FaepExpandItem;
import faep.gui.Activator;
import faep.gui.enums.SearchOptionsEnum;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view shows data obtained from the model. The sample
 * creates a dummy model on the fly, but a real implementation would connect to the model available either in this or another
 * plug-in (e.g. the workspace). The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be presented in the view. Each view can present the same
 * model objects using different labels and icons, if needed. Alternatively, a single label provider can be shared between views
 * in order to ensure that objects of the same type are presented in the same way everywhere.
 * <p>
 */

public class FaepView extends ViewPart {

    /**
     * The ID of the view as specified by the extension.
     */
    public static final String ID = "faep.views.FaepView";
    private Composite parent;
    private Text searchBar;
    private Button searchButton;
    private ExpandBar expandBar;

    private String[] comboOptions = SearchOptionsEnum.getAllStringValues();
    private Combo searchCombo;

    private static final String SETTINGS_MESSAGE = "Your account authorization is not complete! \n"
	    + "Please open Window -> Preferences -> FAEP Preferences and complete the authorization process.";

    private IEclipsePreferences preferences;

    /**
     * The constructor.
     */
    public FaepView() {
    }

    @Override
    public void init(IViewSite site) throws PartInitException {
	preferences = ConfigurationScope.INSTANCE.getNode("faep.plugin.preferences");
	super.init(site);
    }

    /**
     * This is a callback that will allow us to create the viewer and initialize it.
     */
    public void createPartControl(Composite top) {
	this.parent = top;
	if (preferences.getBoolean("goodToGo", true)) {
	    createFreelancerView();
	} else {
	    createErrorView();
	}
    }

    public void createFreelancerView() {
	ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL);
	scrolledComposite.setExpandHorizontal(true);
	scrolledComposite.setExpandVertical(true);

	// Composite which contains the Searchbar and the Expandbar
	Composite composite = new Composite(scrolledComposite, SWT.NONE);
	GridLayout glComposite = new GridLayout(4, false);
	glComposite.verticalSpacing = 1;
	composite.setLayout(glComposite);

	Label searchLabel = new Label(composite, SWT.NONE);
	searchLabel.setText("Search");

	searchCombo = new Combo(composite, SWT.READ_ONLY);
	searchCombo.setItems(comboOptions);

	searchBar = new Text(composite, SWT.BORDER);
	searchBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

	searchButton = new Button(composite, SWT.NONE);
	GridData buttonGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	buttonGridData.widthHint = 100;
	searchButton.setLayoutData(buttonGridData);
	searchButton.setText("GO");

	// Expandbar which contains all the Job Entries
	expandBar = new ExpandBar(composite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
	GridData expandBarGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1);
	expandBarGridData.widthHint = 320;
	expandBarGridData.minimumWidth = 320;
	expandBar.setSpacing(4);
	expandBar.setLayoutData(expandBarGridData);

	// lazy loading
	expandBar.addExpandListener(new FaepExpandListener());
	expandBar.addControlListener(new ExpandBarAdapter());

	// for (int i = 0; i < 10; i++) {
	// @SuppressWarnings("unused")
	// FaepExpandItem item = new FaepExpandItem(expandBar, SWT.NONE, 1);
	// }

	// Return to regular code
	new Label(composite, SWT.NONE);
	new Label(composite, SWT.NONE);
	new Label(composite, SWT.NONE);
	new Label(composite, SWT.NONE);
	scrolledComposite.setContent(composite);
	scrolledComposite.setMinWidth(480);// (new Point(480, 480));
	// scrolledComposite.setMinSize(480, 480);
	Activator.assignControllerToView(this);
    }

    public void createErrorView() {
	Composite composite = new Composite(parent, SWT.NONE);
	composite.setLayout(new GridLayout());

	Label message = new Label(composite, SWT.WRAP);
	message.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
	message.setText(SETTINGS_MESSAGE);
	Activator.assignControllerToView(this);
    }

    public void addExpandItems(List<Job> jobList) {
	for (Job job : jobList) {
	    FaepExpandItem item = new FaepExpandItem(expandBar, SWT.NONE, job.getProjectId());
	    item.setText(job.getProvider() + ":" + job.getProjectName());
	    item.setProvider(job.getProvider());
	}
	parent.layout();
    }

    public void removeExpandItems() {
	for (ExpandItem item : expandBar.getItems()) {
	    item.setExpanded(false);
	    item.dispose();
	}
	parent.update();
	parent.layout();
	System.out.println(expandBar.getItemCount());
    }

    @Override
    public void setFocus() {
	if (preferences.getBoolean("goodToGo", false)) {
	    searchBar.setFocus();
	}
    }

    @Override
    public void dispose() {
	super.dispose();
    }

    // Getters for the Controllers
    public Composite getParent() {
	return this.parent;
    }

    public IEclipsePreferences getPreferences() {
	return this.preferences;
    }

    public Text getSearchBar() {
	return this.searchBar;
    }

    public Button getSearchButton() {
	return this.searchButton;
    }

    public Combo getSearchCombo() {
	return this.searchCombo;
    }

}
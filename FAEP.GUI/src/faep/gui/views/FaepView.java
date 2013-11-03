package faep.gui.views;

import java.util.List;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import common.wrappers.Bid;
import common.wrappers.Job;
import common.wrappers.Project;

import faep.gui.Activator;
import faep.gui.enums.SearchOptionsEnum;

public class FaepView extends ViewPart {

    /**
     * The ID of the view as specified by the extension.
     */
    public static final String ID = "faep.views.FaepView";
    private Composite parent;
    private Text searchBar;
    private Button searchButton;
    private TableViewer tableViewer;
    private Composite mainComposite;
    private ScrolledComposite scrolledComposite;

    private String[] comboOptions = SearchOptionsEnum.getAllStringValues();
    private Combo searchOptionsCombo;

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

    /**
     * Initializes the freelancer View with the searchbar and table.
     */
    public void createFreelancerView() {
	scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	scrolledComposite.setExpandHorizontal(true);
	scrolledComposite.setExpandVertical(true);

	// Composite which contains the Searchbar and the Expandbar
	mainComposite = new Composite(scrolledComposite, SWT.NONE);
	GridLayout glComposite = new GridLayout(4, false);
	glComposite.verticalSpacing = 1;
	mainComposite.setLayout(glComposite);

	Label searchLabel = new Label(mainComposite, SWT.NONE);
	searchLabel.setText("Search");

	searchOptionsCombo = new Combo(mainComposite, SWT.READ_ONLY);
	searchOptionsCombo.setItems(comboOptions);
	searchOptionsCombo.select(0);

	searchBar = new Text(mainComposite, SWT.BORDER);
	searchBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

	searchButton = new Button(mainComposite, SWT.NONE);
	GridData buttonGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	buttonGridData.widthHint = 100;
	searchButton.setLayoutData(buttonGridData);
	searchButton.setText("GO");

	createTableViewer();

	scrolledComposite.setContent(mainComposite);
	scrolledComposite.setMinWidth(550);
	scrolledComposite.addControlListener(new ControlAdapter() {
	    public void controlResized(ControlEvent e) {
		Rectangle r = scrolledComposite.getClientArea();
		scrolledComposite.setMinSize(mainComposite.computeSize(r.width, SWT.DEFAULT));
	    }
	});

	Activator.assignControllerToView(this);
    }

    /**
     * Initializes the error view, where the user is notified to configure his account.
     */
    public void createErrorView() {
	Composite composite = new Composite(parent, SWT.NONE);
	composite.setLayout(new GridLayout());

	Label message = new Label(composite, SWT.WRAP);
	message.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
	message.setText(SETTINGS_MESSAGE);
	Activator.assignControllerToView(this);
    }

    /**
     * Creates the Detail Composite alongside the Bids that are placed on this project.
     * 
     * @param project
     *            Current project and the related data.
     * @param job
     * @param sListener
     * @param bidList
     * @param bidPlaced
     */
    public void createProjectDetailsWithBids(Project project, Job job, SelectionListener sListener, List<Bid> bidList,
	    Bid bidPlaced) {
	createProjectDetailsComposite(project, job, sListener, bidPlaced);
	FaepViewHelper.createAllBidComposite(mainComposite, bidList);
	parent.layout(true, true);
	recalculateScrolledCompositeSize();
    }

    private void createProjectDetailsComposite(Project project, Job job, SelectionListener sListener, Bid bidPlaced) {
	tableViewer.getControl().setVisible(false);
	((GridData) tableViewer.getControl().getLayoutData()).heightHint = 0;
	((GridData) tableViewer.getControl().getLayoutData()).grabExcessVerticalSpace = false;
	FaepViewHelper.createInformationBar(mainComposite, job, sListener);
	FaepViewHelper.createDetailsComposite(mainComposite, project, sListener);
	FaepViewHelper.creaeteMyBidComposite(mainComposite, bidPlaced, sListener);
    }

    public void createTableViewer() {
	FaepViewHelper.disposeProjectComposites();

	if (tableViewer == null) {
	    tableViewer = new TableViewer(mainComposite, SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
	    Table table = tableViewer.getTable();
	    table.setHeaderVisible(true);
	    table.setLinesVisible(false);
	    tableViewer.setContentProvider(ArrayContentProvider.getInstance());

	    createColumns(mainComposite);

	    GridData tableGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1);
	    tableGridData.widthHint = 320;
	    tableGridData.minimumWidth = 320;
	    tableViewer.getControl().setLayoutData(tableGridData);
	} else {
	    ((GridData) tableViewer.getControl().getLayoutData()).heightHint = -1;
	    ((GridData) tableViewer.getControl().getLayoutData()).grabExcessVerticalSpace = true;
	    tableViewer.getControl().setVisible(true);
	}

	mainComposite.layout();
    }

    private void createColumns(Composite parent) {
	String[] titles = { "Project Name", "Bid Count", "Start Date", "Job Type" };
	int[] bounds = { 200, 50, 100, 150 };

	TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
	col.setLabelProvider(new ColumnLabelProvider() {
	    @Override
	    public String getText(Object element) {
		if (element instanceof Job) {
		    Job job = (Job) element;
		    return job.getProjectName();
		}
		return super.getText(element);
	    }
	});

	col = createTableViewerColumn(titles[1], bounds[1], 1);
	col.setLabelProvider(new ColumnLabelProvider() {
	    @Override
	    public String getText(Object element) {
		if (element instanceof Job) {
		    Job job = (Job) element;
		    return String.valueOf(job.getBids());
		}
		return super.getText(element);
	    }
	});

	col = createTableViewerColumn(titles[2], bounds[2], 2);
	col.setLabelProvider(new ColumnLabelProvider() {
	    @Override
	    public String getText(Object element) {
		if (element instanceof Job) {
		    Job job = (Job) element;
		    return job.getStartDate().toString();
		}
		return super.getText(element);
	    }
	});

	col = createTableViewerColumn(titles[3], bounds[3], 3);
	col.setLabelProvider(new ColumnLabelProvider() {
	    @Override
	    public String getText(Object element) {
		if (element instanceof Job) {
		    Job job = (Job) element;
		    return job.getJobTypeCSV();
		}
		return super.getText(element);
	    }
	});
    }

    private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
	final TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
	final TableColumn column = viewerColumn.getColumn();
	column.setText(title);
	column.setWidth(bound);
	column.setResizable(true);
	column.setMoveable(true);
	return viewerColumn;
    }

    public void createInfoDialog(String title, String message) {
	MessageDialog.openInformation(getSite().getShell(), title, message);
    }

    public void createErrorDialog(String title, String message) {
	MessageDialog.openError(getSite().getShell(), title, message);
    }

    private void recalculateScrolledCompositeSize() {
	Rectangle r = scrolledComposite.getClientArea();
	scrolledComposite.setMinSize(mainComposite.computeSize(r.width, SWT.DEFAULT));
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
	return this.searchOptionsCombo;
    }

    public TableViewer getTableViewer() {
	return this.tableViewer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFocus() {
	if (preferences.getBoolean("goodToGo", false)) {
	    searchBar.setFocus();
	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
	super.dispose();
    }
}
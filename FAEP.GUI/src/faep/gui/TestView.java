package faep.gui;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import proxy.Proxy;

import common.wrappers.Job;
import common.wrappers.JobSearch;

public class TestView extends ViewPart {

    private TableViewer viewer;

    public TestView() {
	// TODO Auto-generated constructor stub
    }

    @Override
    public void createPartControl(Composite parent) {
	GridLayout layout = new GridLayout(2, false);
	parent.setLayout(layout);

	createTableViewer(parent);

    }

    private void createTableViewer(Composite parent) {
	viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

	Table table = viewer.getTable();
	table.setHeaderVisible(true);
	table.setLinesVisible(false);
	// ArrayContentProvider does not store any state,
	// therefore you can re-use instances

	viewer.setContentProvider(ArrayContentProvider.getInstance());

	createColumns(parent);

	// set the input of the Viewer,
	// this input is send to the content provider
	JobSearch searchParams = new JobSearch();
	searchParams.setCount(30);
	searchParams.setSearchKeyword("");
	searchParams.setSearchJobTypeCSV("android");
	List<Job> jobList = Proxy.getInstance().searchJobs(searchParams);

	viewer.setInput(jobList);
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
	final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
	final TableColumn column = viewerColumn.getColumn();
	column.setText(title);
	column.setWidth(bound);
	column.setResizable(true);
	column.setMoveable(true);
	return viewerColumn;
    }

    @Override
    public void setFocus() {
	// TODO Auto-generated method stub
    }

    public TableViewer getViewer() {
	return this.viewer;
    }

}

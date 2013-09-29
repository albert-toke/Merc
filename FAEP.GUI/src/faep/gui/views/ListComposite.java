package faep.gui.views;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import common.wrappers.Job;

public class ListComposite extends ScrolledComposite {
    private Table table;

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public ListComposite(Composite parent, int style) {
	super(parent, SWT.NONE);

	TableViewer tableViewer = new TableViewer(this, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
	table = tableViewer.getTable();
	table.setHeaderVisible(true);
	table.setLinesVisible(true);
	setContent(table);
	setMinSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	tableViewer.setLabelProvider(new ITableLabelProvider() {

	    @Override
	    public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	    }

	    @Override
	    public void dispose() {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public String getColumnText(Object element, int columnIndex) {
		if (element instanceof Job) {
		    Job job = (Job) element;
		    switch (columnIndex) {
		    case 0:
			// Provider
			return job.getProvider();
		    case 1:
			// Project Name
			return job.getProjectName();
		    case 2:
			// Bid count
			return String.valueOf(job.getBids());
		    case 3:
			// Start Date
			return job.getStartDate().toString();
		    case 4:
			// Job Type
			return job.getJobTypeCSV();
		    case 5:
			// Price
			// TODO implement budget min/max
			return null;
		    }
		}
		return null;
	    }

	    @Override
	    public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	    }
	});
    }

    @Override
    protected void checkSubclass() {
	// Disable the check that prevents subclassing of SWT components
    }

}

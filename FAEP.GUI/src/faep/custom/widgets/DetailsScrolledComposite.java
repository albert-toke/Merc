package faep.custom.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import common.wrappers.Project;

public class DetailsScrolledComposite extends ScrolledComposite {

    private Text text1;
    private Text text9;
    private Text text10;

    public DetailsScrolledComposite(Composite parent, int style) {
	super(parent, style);
	// TODO Auto-generated constructor stub
    }

    public void initMainComposite(Project project) {
	// scrolledComposite = new ScrolledComposite(this.getParent(), SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	// TODO set scrolled composite as controller for ExpandItem
	// this.setControl(scrolledComposite);
	this.setExpandHorizontal(true);
	this.setExpandVertical(true);

	Composite composite = new Composite(this, SWT.NONE);
	// this.setHeight(300);
	composite.setLayout(new GridLayout(4, false));

	text1 = addTextColumns("Status:", project.getState(), composite, false);
	text1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

	Text text5 = addTextColumns("Project Creator:", project.getBuyerUserName(), composite, false);
	text5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

	Text text2 = addTextColumns("Providers:", "Nyest", composite, false);
	text2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

	Text text6 = addTextColumns("Bid Count:", project.getBidCount() + "", composite, false);
	text6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

	Text text3 = addTextColumns("Budget:", project.getMinBudget() + " - " + project.getMaxBudget(), composite, false);
	text3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

	Text text7 = addTextColumns("Avarage Bid:", project.getAverageBidVal() + "", composite, false);
	text7.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

	Text text4 = addTextColumns("Created:", project.getStartDate().toString(), composite, true);
	text4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 2));

	Text text8 = addTextColumns("Ends:", project.getEndDate().toString(), composite, true);
	text8.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 2));

	text9 = new Text(composite, SWT.WRAP | SWT.MULTI | SWT.BORDER);
	text9.setEditable(false);
	text9.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
	text9.setText(project.getShortDescription());

	Label lblJobType = new Label(composite, SWT.NONE);
	lblJobType.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 3));
	lblJobType.setText("Job Type:");

	text10 = new Text(composite, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);
	text10.setEditable(false);
	text10.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 3));
	text10.setText(project.getJobCategoryAsString("\n"));
    }

    private Text addTextColumns(String labelText, String fieldText, Composite parent, boolean multi) {
	Label lblUserId = new Label(parent, SWT.NONE);
	lblUserId.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	lblUserId.setText(labelText);
	Text textField;
	if (multi) {
	    textField = new Text(parent, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);
	} else {
	    textField = new Text(parent, SWT.READ_ONLY);
	}

	textField.setEditable(false);
	textField.setText(fieldText);
	return textField;
    }
}

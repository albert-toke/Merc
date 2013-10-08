package faep.gui.views;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import common.wrappers.Bid;
import common.wrappers.Job;
import common.wrappers.Project;

public class FaepViewHelper {

    private static Text text1;
    private static Text text9;
    private static Text text10;
    private static Button returnButton;
    private static Composite infoComposite;
    private static Composite detailsComposite;
    private static Composite bidPlaceComposite;
    private static Composite bidAllComposite;

    public static Composite createInformationBar(Composite parent, Job job, SelectionListener listener) {
	infoComposite = new Composite(parent, SWT.NONE);
	GridData compGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1);
	compGridData.widthHint = 320;
	compGridData.minimumWidth = 320;
	infoComposite.setLayoutData(compGridData);
	infoComposite.setLayout(new GridLayout(2, false));

	returnButton = new Button(infoComposite, SWT.PUSH);
	returnButton.setText("Back");
	returnButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	returnButton.addSelectionListener(listener);

	Label projectNameLabel = new Label(infoComposite, SWT.NONE);
	projectNameLabel.setText(job.getProjectName());
	projectNameLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));

	FontData[] font = projectNameLabel.getFont().getFontData();
	font[0].setHeight(16);
	font[0].setStyle(SWT.BOLD);
	projectNameLabel.setFont(new Font(Display.getCurrent(), font[0]));

	return infoComposite;
    }

    public static Composite createDetailsComposite(Composite parent, Project project, boolean bidPlaced) {
	detailsComposite = new Composite(parent, SWT.NONE);

	GridData compGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1);
	compGridData.widthHint = 320;
	compGridData.minimumWidth = 320;
	detailsComposite.setLayoutData(compGridData);
	detailsComposite.setLayout(new GridLayout(4, false));

	text1 = addTextColumns("Status:", project.getState(), detailsComposite, false);
	text1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

	Text text5 = addTextColumns("Project Creator:", project.getBuyerUserName(), detailsComposite, false);
	text5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

	Text text2 = addTextColumns("Providers:", "Egyenlore meg nyest", detailsComposite, false);
	text2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

	Text text6 = addTextColumns("Bid Count:", project.getBidCount() + "", detailsComposite, false);
	text6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

	Text text3 = addTextColumns("Budget:", project.getMinBudget() + " - " + project.getMaxBudget(), detailsComposite, false);
	text3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

	Text text7 = addTextColumns("Avarage Bid:", project.getAverageBidVal() + "", detailsComposite, false);
	text7.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

	Text text4 = addTextColumns("Created:", project.getStartDate().toString(), detailsComposite, true);
	text4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 2));

	Text text8 = addTextColumns("Ends:", project.getEndDate().toString(), detailsComposite, true);
	text8.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 2));

	text9 = new Text(detailsComposite, SWT.WRAP | SWT.MULTI | SWT.BORDER);
	text9.setEditable(false);
	text9.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
	text9.setText(project.getShortDescription());

	Label lblJobType = new Label(detailsComposite, SWT.NONE);
	lblJobType.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 3));
	lblJobType.setText("Job Type:");

	text10 = new Text(detailsComposite, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);
	text10.setEditable(false);
	text10.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 3));
	text10.setText(project.getJobCategoryAsString("\n"));

	creaeteBidPlacedComposite(detailsComposite, bidPlaced);
	return detailsComposite;
    }

    private static Text addTextColumns(String labelText, String fieldText, Composite parent, boolean multi) {
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

    private static void creaeteBidPlacedComposite(Composite parent, boolean bidPlaced) {
	bidPlaceComposite = new Composite(parent, SWT.NONE);
	bidPlaceComposite.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false, 2, 3));
	bidPlaceComposite.setLayout(new GridLayout(2, false));

	Label bidAmountLabel = new Label(bidPlaceComposite, SWT.NONE);
	bidAmountLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	bidAmountLabel.setText("Bid Ammount($):");

	Text bidAmountText = new Text(bidPlaceComposite, SWT.NONE);
	bidAmountText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	bidAmountText.setEditable(!bidPlaced);
	bidAmountText.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
	//
	// Label bidCurrencyLabel = new Label(myBidComposite, SWT.NONE);
	// bidCurrencyLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	// bidCurrencyLabel.setText("Currency:");

	Label bidDurationLabel = new Label(bidPlaceComposite, SWT.NONE);
	bidDurationLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	bidDurationLabel.setText("Duration:");

	Text bidReqTimeText = new Text(bidPlaceComposite, SWT.NONE);
	bidReqTimeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	bidReqTimeText.setEditable(!bidPlaced);
	bidReqTimeText.setBackground(new Color(Display.getCurrent(), 255, 255, 255));

	Button bidButton = new Button(bidPlaceComposite, SWT.PUSH);
	bidButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
	bidButton.setSize(130, SWT.DEFAULT);

	bidButton.setText("Bid on Project");

    }

    public static void createAllBidComposite(Composite parent, List<Bid> bidList) {
	bidAllComposite = new Composite(parent, SWT.BORDER);
	bidAllComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
	bidAllComposite.setLayout(new FillLayout(SWT.VERTICAL));

	for (Bid bid : bidList) {
	    Composite comp = new Composite(bidAllComposite, SWT.BORDER);
	    comp.setLayout(new GridLayout(4, false));

	    Text userText = addTextColumns("Freelancer:", bid.getProvider(), comp, false);
	    userText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

	    Text ratingText = addTextColumns("Rating:", bid.getRating(), comp, false);
	    ratingText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

	    Text bidAmountText = addTextColumns("Bid Amount:", bid.getBidAmount() + "", comp, false);
	    bidAmountText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

	    Text milestoneText = addTextColumns("Milestone:", bid.getMilestone(), comp, false);
	    milestoneText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

	    Text submitTimeText = addTextColumns("Submit Time:", bid.getSubmitTime(), comp, false);
	    submitTimeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

	    new Label(comp, SWT.NONE);
	    new Label(comp, SWT.NONE);

	    Text descriptionText = addTextColumns("Description:", bid.getDescription(), comp, true);
	    descriptionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 2));
	}
    }

    public static Button getReturnButton() {
	return returnButton;
    }

    public static void disposeProjectComposites() {
	if (bidAllComposite != null && !bidAllComposite.isDisposed()) {
	    bidAllComposite.dispose();
	}
	if (bidPlaceComposite != null && !bidPlaceComposite.isDisposed()) {
	    bidPlaceComposite.dispose();
	}
	if (detailsComposite != null && !detailsComposite.isDisposed()) {
	    detailsComposite.dispose();
	}
	if (infoComposite != null && !infoComposite.isDisposed()) {
	    infoComposite.dispose();
	}
    }
}

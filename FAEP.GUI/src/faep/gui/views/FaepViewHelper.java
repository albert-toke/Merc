package faep.gui.views;

import java.util.List;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
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

import enums.JobStatusEnum;

public class FaepViewHelper {

    private static Text text1;
    private static Text text9;
    private static Text text10;
    private static Button returnButton;
    private static Button bidButton;
    private static Composite infoComposite;
    private static Composite detailsComposite;
    private static Composite bidPlaceComposite;
    private static Composite bidAllComposite;
    private static Text bidAmountText;
    private static Text bidReqTimeText;
    private static Text bidDescriptionText;
    private static ControlDecoration bidAmountDecorator;
    private static ControlDecoration bidReqTimeDecorator;
    private static ControlDecoration bidDescriptionDecorator;
    private static long projectId;
    private static String provider;

    public static Composite createInformationBar(Composite parent, Job job, SelectionListener listener) {
	infoComposite = new Composite(parent, SWT.NONE);
	GridData compGridData = new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1);
	compGridData.widthHint = 320;
	compGridData.minimumWidth = 320;
	infoComposite.setLayoutData(compGridData);
	infoComposite.setLayout(new GridLayout(2, false));

	returnButton = new Button(infoComposite, SWT.PUSH);
	returnButton.setText("Back");
	returnButton.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
	returnButton.addSelectionListener(listener);

	Text projectNameText = new Text(infoComposite, SWT.WRAP | SWT.MULTI);
	projectNameText.setText(job.getProjectName());
	projectNameText.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1));

	FontData[] font = projectNameText.getFont().getFontData();
	font[0].setHeight(16);
	font[0].setStyle(SWT.BOLD);
	projectNameText.setFont(new Font(Display.getCurrent(), font[0]));
	projectNameText.setEditable(false);
	projectNameText.setEnabled(false);

	projectId = job.getProjectId();
	provider = job.getProvider();
	return infoComposite;
    }

    public static Composite createDetailsComposite(Composite parent, Project project, SelectionListener listener) {
	detailsComposite = new Composite(parent, SWT.NONE);

	GridData compGridData = new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1);
	compGridData.widthHint = 320;
	compGridData.minimumWidth = 320;
	detailsComposite.setLayoutData(compGridData);
	detailsComposite.setLayout(new GridLayout(4, false));

	text1 = addTextColumns("Status:", project.getStatus().getStringValue(), detailsComposite, false);
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

    /**
     * Creates the composite which is responsible for the own bid placement and displaying.
     * 
     * @param parent
     * @param bidPlaced
     * @param sListener
     */
    public static void creaeteMyBidComposite(Composite parent, Bid bidPlaced, SelectionListener sListener, JobStatusEnum jobStatus) {

	bidPlaceComposite = new Composite(parent, SWT.BORDER);
	bidPlaceComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 5, 3));
	bidPlaceComposite.setLayout(new GridLayout(2, false));

	Label infoLabel = new Label(bidPlaceComposite, SWT.NONE);
	infoLabel.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 2, 1));
	infoLabel.setText("My Bid");

	Label bidAmountLabel = new Label(bidPlaceComposite, SWT.NONE);
	bidAmountLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
	bidAmountLabel.setText("Bid Ammount($):");

	bidAmountText = new Text(bidPlaceComposite, SWT.NONE);
	bidAmountText.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

	bidAmountText.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
	bidAmountDecorator = addTextDecorator(bidAmountText);

	Label bidDurationLabel = new Label(bidPlaceComposite, SWT.NONE);
	bidDurationLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
	bidDurationLabel.setText("Duration:");

	bidReqTimeText = new Text(bidPlaceComposite, SWT.NONE);
	bidReqTimeText.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

	bidReqTimeText.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
	bidReqTimeDecorator = addTextDecorator(bidReqTimeText);

	Label bidDescriptionLabel = new Label(bidPlaceComposite, SWT.NONE);
	bidDescriptionLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 3));
	bidDescriptionLabel.setText("Description:");

	bidDescriptionText = new Text(bidPlaceComposite, SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
	GridData descriptionGridData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 3);
	descriptionGridData.heightHint = 75;
	bidDescriptionText.setLayoutData(descriptionGridData);

	bidDescriptionText.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
	bidDescriptionDecorator = addTextDecorator(bidDescriptionText);

	bidButton = new Button(bidPlaceComposite, SWT.PUSH);
	bidButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1));
	bidButton.setSize(130, SWT.DEFAULT);

	bidButton.addSelectionListener(sListener);

	String bidButtonText = "";
	if (bidPlaced == null) {
	    setFieldsEditaleState(true);
	    bidButtonText = "Place Bid";
	} else {
	    if (jobStatus == JobStatusEnum.OPEN) {
		setFieldsEditaleState(false);
		bidButtonText = "Whitdraw Bid";
	    } else if (jobStatus == JobStatusEnum.WON) {
		setFieldsEditaleState(false);
		bidButtonText = "Accept Project";
	    }
	    setBidTextContent(bidPlaced);
	}
	bidButton.setText(bidButtonText);

    }

    private static void setFieldsEditaleState(boolean editable) {
	bidAmountText.setEditable(editable);
	bidReqTimeText.setEditable(editable);
	bidDescriptionText.setEditable(editable);
    }

    /**
     * Creates a Decorator for the specified Text Widget.
     * 
     * @param text
     *            The Widget to whom the Decorator will be associated.
     */
    public static ControlDecoration addTextDecorator(Text text) {
	ControlDecoration decorator = new ControlDecoration(text, SWT.TOP | SWT.LEFT);
	FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
		FieldDecorationRegistry.DEC_ERROR);
	Image img = fieldDecoration.getImage();
	decorator.setImage(img);
	String errorMessage = "";
	if (text == bidAmountText) {
	    errorMessage = "Pls enter only numeric fields";
	} else if (text == bidReqTimeText) {
	    errorMessage = "Pls enter only numeric fields";
	} else if (text == bidDescriptionText) {
	    errorMessage = "The number of characters must be between 10 and 200.";
	}
	decorator.setDescriptionText(errorMessage);
	decorator.hide();
	return decorator;
    }

    private static void setBidTextContent(Bid bidPlaced) {
	bidAmountText.setText(bidPlaced.getBidAmount() + "");
	bidReqTimeText.setText(bidPlaced.getDuration() + "");
	bidDescriptionText.setText(bidPlaced.getDescription());
    }

    public static void showBidAmountDecorator() {
	if (bidAmountDecorator != null) {
	    bidAmountDecorator.show();
	    bidAmountText.setBackground((new Color(Display.getCurrent(), 255, 200, 200)));
	}
    }

    public static void showBidReqTimeDecorator() {
	if (bidReqTimeDecorator != null) {
	    bidReqTimeDecorator.show();
	    bidReqTimeText.setBackground((new Color(Display.getCurrent(), 255, 200, 200)));
	}
    }

    public static void showBidDescriptionDecorator() {
	if (bidDescriptionDecorator != null) {
	    bidDescriptionDecorator.show();
	    bidDescriptionText.setBackground((new Color(Display.getCurrent(), 255, 200, 200)));
	}
    }

    public static void hideBidAmountDecorator() {
	if (bidAmountDecorator != null) {
	    bidAmountDecorator.hide();
	    bidAmountText.setBackground((new Color(Display.getCurrent(), 255, 255, 255)));
	}
    }

    public static void hideBidReqTimeDecorator() {
	if (bidReqTimeDecorator != null) {
	    bidReqTimeDecorator.hide();
	    bidReqTimeText.setBackground((new Color(Display.getCurrent(), 255, 255, 255)));
	}
    }

    public static void hideBidDescriptionDecorator() {
	if (bidDescriptionDecorator != null) {
	    bidDescriptionDecorator.hide();
	    bidDescriptionText.setBackground((new Color(Display.getCurrent(), 255, 255, 255)));
	}
    }

    public static void dissableMyBidWidgets() {
	bidAmountText.setEditable(false);
	bidDescriptionText.setEditable(false);
	bidReqTimeText.setEditable(false);
	bidButton.setText("Cancel Bid");
    }

    public static void createAllBidComposite(Composite parent, List<Bid> bidList) {
	bidAllComposite = new Composite(parent, SWT.BORDER);
	bidAllComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
	bidAllComposite.setLayout(new FillLayout(SWT.VERTICAL));

	for (Bid bid : bidList) {
	    createSingleBidComposite(bidAllComposite, bid);
	}
    }

    private static void createSingleBidComposite(Composite parent, Bid bid) {
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

    public static Button getReturnButton() {
	return returnButton;
    }

    public static Button getBidButton() {
	return bidButton;
    }

    public static Text getBidAmountText() {
	return bidAmountText;
    }

    public static Text getBidReqTimeText() {
	return bidReqTimeText;
    }

    public static Text getBidDescriptionText() {
	return bidDescriptionText;
    }

    public static long getProjectId() {
	return projectId;
    }

    public static String getProvider() {
	return provider;
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

    public static void disposeProjectBidsComposite() {
	if (bidAllComposite != null && !bidAllComposite.isDisposed()) {
	    bidAllComposite.dispose();
	}
    }
}

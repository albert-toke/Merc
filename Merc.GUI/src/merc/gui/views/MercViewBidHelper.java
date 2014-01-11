package merc.gui.views;

import java.util.List;

import merc.gui.constants.MercColor;
import merc.gui.enums.ActionButtonOptionsEnum;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import common.wrappers.Bid;

import enums.JobStatusEnum;

public class MercViewBidHelper {

    private static Button bidButton;
    private static Button declineButton;

    private static Composite bidPlaceComposite;
    private static Composite bidAllComposite;
    private static Text bidAmountText;
    private static Text bidReqTimeText;
    private static Text bidDescriptionText;
    private static ControlDecoration bidAmountDecorator;
    private static ControlDecoration bidReqTimeDecorator;
    private static ControlDecoration bidDescriptionDecorator;

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

	bidAmountText.setBackground(MercColor.WHITE);
	bidAmountDecorator = addTextDecorator(bidAmountText);

	Label bidDurationLabel = new Label(bidPlaceComposite, SWT.NONE);
	bidDurationLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
	bidDurationLabel.setText("Duration:");

	bidReqTimeText = new Text(bidPlaceComposite, SWT.NONE);
	bidReqTimeText.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

	bidReqTimeText.setBackground(MercColor.WHITE);
	bidReqTimeDecorator = addTextDecorator(bidReqTimeText);

	Label bidDescriptionLabel = new Label(bidPlaceComposite, SWT.NONE);
	bidDescriptionLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 3));
	bidDescriptionLabel.setText("Description:");

	bidDescriptionText = new Text(bidPlaceComposite, SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
	GridData descriptionGridData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 3);
	descriptionGridData.heightHint = 75;
	bidDescriptionText.setLayoutData(descriptionGridData);

	bidDescriptionText.setBackground(MercColor.WHITE);
	bidDescriptionDecorator = addTextDecorator(bidDescriptionText);

	bidButton = new Button(bidPlaceComposite, SWT.PUSH);
	bidButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
	// TODO width with grid data
	bidButton.setSize(130, SWT.DEFAULT);

	bidButton.addSelectionListener(sListener);

	String bidButtonText = "";
	if (bidPlaced == null) {
	    setFieldsEditableState(true);
	    bidButtonText = ActionButtonOptionsEnum.PLACE_BID.getStringValue();
	} else {
	    if (jobStatus == JobStatusEnum.OPEN) {
		setFieldsEditableState(false);
		bidButtonText = ActionButtonOptionsEnum.WITHDRAW.getStringValue();
		bidButton.setVisible(true);
	    } else if (jobStatus == JobStatusEnum.WON) {
		setFieldsEditableState(false);
		declineButton = new Button(bidPlaceComposite, SWT.PUSH);
		declineButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		declineButton.setSize(130, SWT.DEFAULT);
		declineButton.setText(ActionButtonOptionsEnum.DECLINE.getStringValue());
		declineButton.addSelectionListener(sListener);
		bidButtonText = ActionButtonOptionsEnum.ACCEPT.getStringValue();
		bidButton.setVisible(true);
	    } else if (jobStatus == JobStatusEnum.ACTIVE) {
		bidButton.setVisible(false);
	    } else if (jobStatus == JobStatusEnum.CLOSED) {
		bidButton.setVisible(false);
		setFieldsEditableState(false);
	    }
	    setBidTextContent(bidPlaced);
	}
	bidButton.setText(bidButtonText);

    }

    public static void setFieldsEditableState(boolean editable) {
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
	    bidAmountText.setBackground(MercColor.TEXT_ERROR);
	}
    }

    public static void showBidReqTimeDecorator() {
	if (bidReqTimeDecorator != null) {
	    bidReqTimeDecorator.show();
	    bidReqTimeText.setBackground(MercColor.TEXT_ERROR);
	}
    }

    public static void showBidDescriptionDecorator() {
	if (bidDescriptionDecorator != null) {
	    bidDescriptionDecorator.show();
	    bidDescriptionText.setBackground(MercColor.TEXT_ERROR);
	}
    }

    public static void hideBidAmountDecorator() {
	if (bidAmountDecorator != null) {
	    bidAmountDecorator.hide();
	    bidAmountText.setBackground((MercColor.WHITE));
	}
    }

    public static void hideBidReqTimeDecorator() {
	if (bidReqTimeDecorator != null) {
	    bidReqTimeDecorator.hide();
	    bidReqTimeText.setBackground((MercColor.WHITE));
	}
    }

    public static void hideBidDescriptionDecorator() {
	if (bidDescriptionDecorator != null) {
	    bidDescriptionDecorator.hide();
	    bidDescriptionText.setBackground((MercColor.WHITE));
	}
    }

    /**
     * Creates the main composite which contains all the bids.
     * 
     * @param parent
     *            Composite of the bidAllCOmposite.
     * @param bidList
     *            List of all the bids regarding the actual project.
     */
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

    public static void disposeProjectComposites() {
	if (bidAllComposite != null && !bidAllComposite.isDisposed()) {
	    bidAllComposite.dispose();
	}
	if (bidPlaceComposite != null && !bidPlaceComposite.isDisposed()) {
	    bidPlaceComposite.dispose();
	}
    }

    public static void disposeProjectBidsComposite() {
	if (bidAllComposite != null && !bidAllComposite.isDisposed()) {
	    bidAllComposite.dispose();
	}
    }

    // Getters

    public static Button getBidButton() {
	return bidButton;
    }

    public static Button getDeclineButton() {
	return declineButton;
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

}

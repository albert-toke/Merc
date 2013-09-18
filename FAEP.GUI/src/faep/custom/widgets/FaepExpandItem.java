package faep.custom.widgets;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import common.wrappers.Bid;
import common.wrappers.Message;
import common.wrappers.Project;

public class FaepExpandItem extends ExpandItem {

    // The id of the project, which is associated to the expand item.
    private long id;
    private String provider;
    private Text text1;
    private Text text9;
    private Text text10;
    private ScrolledComposite scrolledComposite;
    private Text bidAmountText;
    private Text bidReqTimeText;
    private Composite bidsOnProjectComposite;
    private Composite myBidComposite;
    private Button bidButton;
    private Composite newMessageComposite;
    private Composite messageComposite;

    private boolean initialized = false;

    // private Color red;

    public FaepExpandItem(ExpandBar parent, int style, long id) {
	super(parent, style);
	this.id = id;
	// Device device = Display.getCurrent ();
	// Color red = new Color (device, 255, 0, 0);
    }

    /**
     * Initializes all the components and fills them with data from the Project class and the bid list.
     * 
     * @param project
     *            Is the current project in the expand item.
     * @param bidList
     *            Is the list of the bids placed on this project.
     */
    public void initComponentsWithBids(Project project, List<Bid> bidList, Bid myBid) {

	this.initialized = true;
	this.setExpanded(false);
	this.setText(provider + ": " + project.getName());

	Composite composite = initMainComposite(project);

	initBidComposite(composite, myBid);

	createBidListArea(composite, bidList);

	scrolledComposite.setContent(composite);

	calculateCorrectHeight();
	// added some comment here

    }

    /**
     * Initializes all the components and fills them with data from the Project class and the message list.
     * 
     * @param project
     *            Is the current project in the expand item.
     * @param messageList
     *            Is the list of messages sent to the owner of the project.
     */
    public void initComponentsWithMessages(Project project, List<Message> messageList, Bid myBid) {
	this.initialized = true;
	this.setExpanded(false);
	this.setText(provider + ": " + project.getName());

	Composite composite = initMainComposite(project);

	initBidComposite(composite, myBid);

	createNewMessageArea(composite, messageList);

	scrolledComposite.setContent(composite);

	calculateCorrectHeight();
    }

    // Initializes the main Composite which contains all the information regarding the project
    private Composite initMainComposite(Project project) {
	scrolledComposite = new ScrolledComposite(this.getParent(), SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	this.setControl(scrolledComposite);
	scrolledComposite.setExpandHorizontal(true);
	scrolledComposite.setExpandVertical(true);

	Composite composite = new Composite(scrolledComposite, SWT.NONE);
	this.setHeight(300);
	composite.setLayout(new GridLayout(4, false));

	text1 = addTextColumns("Status:", project.getState(), composite, false);

	Text text5 = addTextColumns("Project Creator:", project.getBuyerUserName(), composite, false);
	text5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

	Text text2 = addTextColumns("Providers:", provider, composite, false);
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
	return composite;
    }

    // Initializes the "Make a bid" Container Composite
    private void initBidComposite(Composite parent, Bid myBid) {
	boolean userBidded = myBid != null;
	myBidComposite = new Composite(parent, SWT.NONE);
	myBidComposite.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false, 2, 3));
	myBidComposite.setLayout(new GridLayout(2, false));

	Label bidAmountLabel = new Label(myBidComposite, SWT.NONE);
	bidAmountLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	bidAmountLabel.setText("Bid Ammount($):");

	bidAmountText = new Text(myBidComposite, SWT.NONE);
	bidAmountText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	bidAmountText.setEditable(userBidded);
	bidAmountText.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
	//
	// Label bidCurrencyLabel = new Label(myBidComposite, SWT.NONE);
	// bidCurrencyLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	// bidCurrencyLabel.setText("Currency:");

	Label bidDurationLabel = new Label(myBidComposite, SWT.NONE);
	bidDurationLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	bidDurationLabel.setText("Duration:");

	bidReqTimeText = new Text(myBidComposite, SWT.NONE);
	bidReqTimeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	bidReqTimeText.setEditable(userBidded);
	bidReqTimeText.setBackground(new Color(Display.getCurrent(), 255, 255, 255));

	bidButton = new Button(myBidComposite, SWT.PUSH);
	bidButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
	bidButton.setSize(130, SWT.DEFAULT);

	if (userBidded) {
	    Text currencyText = new Text(myBidComposite, SWT.READ_ONLY);
	    currencyText.setText("$");
	    bidAmountText.setText(myBid.getBidAmount() + "");
	    // TODO verify this bidReqTimeText.setText();
	    bidButton.setText("Cancel Bid");
	} else {
	    // Combo currencyCombo = new Combo(myBidComposite, SWT.READ_ONLY);
	    // currencyCombo.setItems(currencys);
	    bidButton.setText("Bid on Project");
	}

    }

    // Creates the message List alongside the composite responsible for the new message elements.
    private void createNewMessageArea(Composite parent, List<Message> messageList) {
	newMessageComposite = new Composite(parent, SWT.BORDER);
	newMessageComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 4));
	newMessageComposite.setLayout(new GridLayout(2, false));

	Label messageLabel = new Label(newMessageComposite, SWT.NONE);
	messageLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 2));
	messageLabel.setText("Message:");

	Text messageTextArea = new Text(newMessageComposite, SWT.WRAP | SWT.MULTI | SWT.BORDER);
	GridData areaData = new GridData(SWT.FILL, SWT.TOP, true, false, 3, 4);
	areaData.heightHint = 100;
	messageTextArea.setLayoutData(areaData);

	Button sendButton = new Button(newMessageComposite, SWT.PUSH);
	GridData sendData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	sendData.widthHint = 100;
	sendButton.setLayoutData(sendData);
	sendButton.setText("Send");
	sendButton.setEnabled(true);

	Button clearButton = new Button(newMessageComposite, SWT.PUSH);
	GridData clearData = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
	clearData.widthHint = 100;
	clearButton.setLayoutData(clearData);
	clearButton.setText("Clear");
	clearButton.setEnabled(true);
	createMessageListArea(parent, messageList);
    }

    private void createMessageListArea(Composite parent, List<Message> messageList) {
	messageComposite = new Composite(parent, SWT.NONE);
	messageComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 4));
	FillLayout messageLayout = new FillLayout(SWT.VERTICAL);
	messageLayout.spacing = 20;
	messageLayout.marginHeight = 10;
	messageComposite.setLayout(messageLayout);

	for (Message message : messageList) {
	    Composite comp = new Composite(messageComposite, SWT.BORDER);
	    comp.setLayout(new GridLayout(2, false));

	    Text userNameText = new Text(comp, SWT.READ_ONLY);
	    StringBuilder sb = new StringBuilder();

	    sb.append(message.getDirection().getStringValue() + " ");
	    sb.append(message.getUsername());

	    userNameText.setText(sb.toString());
	    userNameText.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));

	    Text dateText = new Text(comp, SWT.READ_ONLY);
	    dateText.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
	    dateText.setText(message.getDate().toString());

	    Text messageTextArea = new Text(comp, SWT.WRAP | SWT.MULTI | SWT.BORDER | SWT.READ_ONLY);
	    GridData areaData = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1);
	    messageTextArea.setLayoutData(areaData);
	    messageTextArea.setText(message.getText());
	}
    }

    // Sets the height of the ExpandItem according to its elements
    public void calculateCorrectHeight() {
	if (this.initialized) {
	    int height = 80;
	    height += 5 * text1.getSize().y;
	    height += text9.getSize().y;
	    if (text10.getSize().y > myBidComposite.getSize().y) {
		height += text10.getSize().y;
	    } else {
		height += myBidComposite.getSize().y;
	    }
	    if (bidsOnProjectComposite != null) {
		height += bidsOnProjectComposite.getSize().y;
	    }
	    if (newMessageComposite != null) {
		height += newMessageComposite.getSize().y;
	    }
	    if (messageComposite != null) {
		height += messageComposite.getSize().y;
	    }
	    this.setHeight(height);
	}
    }

    private void createBidListArea(Composite parent, List<Bid> bidList) {
	bidsOnProjectComposite = new Composite(parent, SWT.BORDER);
	bidsOnProjectComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
	bidsOnProjectComposite.setLayout(new FillLayout(SWT.VERTICAL));

	for (Bid bid : bidList) {
	    Composite comp = new Composite(bidsOnProjectComposite, SWT.BORDER);
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

    /**
     * Disposes all the children of the expand item.
     */
    public void disposeComponents() {
	this.initialized = false;
	scrolledComposite.dispose();
    }

    public long getId() {
	return id;
    }

    public String getProvider() {
	return this.provider;
    }

    public void setProvider(String provider) {
	this.provider = provider;
    }

}

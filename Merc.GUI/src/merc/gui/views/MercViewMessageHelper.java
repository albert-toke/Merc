package merc.gui.views;

import java.util.List;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import common.wrappers.Message;

import enums.DirectionEnum;

public class MercViewMessageHelper {

    private static Composite messageAllComposite;
    private static Composite newMessageComposite;
    private static Button sendButton;
    private static Button clearButton;
    private static Text messageTextArea;
    private static List<Message> messageList;
    private static ScrolledComposite scrolledComposite;
    private static ControlDecoration messageDecorator;
    private static Color blue = new Color(Display.getCurrent(), 144, 207, 255);
    private static Color lightBlue = new Color(Display.getCurrent(), 176, 221, 255);
    private static Color lightOrange = new Color(Display.getCurrent(), 255, 221, 176);
    private static Color orange = new Color(Display.getCurrent(), 255, 197, 121);

    public static void createNewMessageComposite(Composite mainComposite, SelectionListener sListener) {
	newMessageComposite = new Composite(mainComposite, SWT.BORDER);
	newMessageComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 4));
	newMessageComposite.setLayout(new GridLayout(2, false));

	Label messageLabel = new Label(newMessageComposite, SWT.NONE);
	messageLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 2));
	messageLabel.setText("Message:");

	messageTextArea = new Text(newMessageComposite, SWT.WRAP | SWT.MULTI | SWT.BORDER);
	GridData areaData = new GridData(SWT.FILL, SWT.TOP, true, false, 3, 4);
	areaData.heightHint = 100;
	messageTextArea.setLayoutData(areaData);
	messageDecorator = addTextDecorator(messageTextArea);

	sendButton = new Button(newMessageComposite, SWT.PUSH);
	GridData sendData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	sendData.widthHint = 100;
	sendButton.setLayoutData(sendData);
	sendButton.setText("Send");
	sendButton.setEnabled(true);
	sendButton.addSelectionListener(sListener);

	clearButton = new Button(newMessageComposite, SWT.PUSH);
	GridData clearData = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
	clearData.widthHint = 100;
	clearButton.setLayoutData(clearData);
	clearButton.setText("Clear");
	clearButton.setEnabled(true);
	clearButton.addSelectionListener(sListener);

    }

    public static void createAllMessageComposite(ScrolledComposite scrolledParent, Composite parent, List<Message> messageList) {
	MercViewMessageHelper.messageList = messageList;
	messageAllComposite = new Composite(parent, SWT.BORDER);
	messageAllComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
	GridLayout gridLayout = new GridLayout(1, false);
	gridLayout.verticalSpacing = 20;
	messageAllComposite.setLayout(gridLayout);
	for (Message message : messageList) {
	    createSingleMessageComposite(message);
	}
	MercViewMessageHelper.scrolledComposite = scrolledParent;
    }

    private static void createSingleMessageComposite(Message message) {

	Composite comp = new Composite(messageAllComposite, SWT.BORDER);
	comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
	comp.setLayout(new GridLayout(2, false));

	Text userNameText = new Text(comp, SWT.READ_ONLY);

	userNameText.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));

	Text dateText = new Text(comp, SWT.READ_ONLY);
	dateText.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
	dateText.setText(message.getDate().toString());

	Text messageTextArea = new Text(comp, SWT.WRAP | SWT.MULTI | SWT.READ_ONLY);
	GridData areaData = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
	messageTextArea.setLayoutData(areaData);
	messageTextArea.setText(message.getText());
	StringBuilder sb = new StringBuilder();
	if (message.getDirection() == DirectionEnum.INCOMING) {

	    sb.append(message.getDirection().getStringValue() + ": ");
	    sb.append(message.getUsername());

	    userNameText.setText(sb.toString());
	    comp.setBackground(blue);
	    userNameText.setBackground(blue);
	    dateText.setBackground(blue);
	    messageTextArea.setBackground(lightBlue);
	} else {
	    sb.append("Me");
	    userNameText.setText(sb.toString());
	    messageTextArea.setBackground(lightOrange);
	    comp.setBackground(orange);
	    userNameText.setBackground(orange);
	    dateText.setBackground(orange);
	}
    }

    public static void addNewOutgoingMessageToComposite(Message msg) {
	messageList.add(0, msg);
	for (Control child : messageAllComposite.getChildren()) {
	    child.dispose();
	}
	for (Message message : messageList) {
	    createSingleMessageComposite(message);
	}
	Composite parent = messageAllComposite.getParent();
	Rectangle r = scrolledComposite.getClientArea();
	scrolledComposite.setMinSize(parent.computeSize(r.width, SWT.DEFAULT));
	parent.layout();
    }

    public static Button getSendButton() {
	return sendButton;
    }

    public static Button getClearButton() {
	return clearButton;
    }

    public static Text getMessageTextArea() {
	return messageTextArea;
    }

    public static void disposeMessageComposites() {
	if (newMessageComposite != null && !newMessageComposite.isDisposed()) {
	    newMessageComposite.dispose();
	}
	if (messageAllComposite != null && !messageAllComposite.isDisposed()) {
	    messageAllComposite.dispose();
	}
    }

    public static ControlDecoration addTextDecorator(Text text) {
	ControlDecoration decorator = new ControlDecoration(text, SWT.TOP | SWT.LEFT);
	FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
		FieldDecorationRegistry.DEC_ERROR);
	Image img = fieldDecoration.getImage();
	decorator.setImage(img);
	decorator.setDescriptionText("The Message can not be blank!");
	decorator.hide();
	return decorator;
    }

    public static void showMessageDecorator() {
	if (messageDecorator != null) {
	    messageDecorator.show();
	    messageTextArea.setBackground((new Color(Display.getCurrent(), 255, 200, 200)));
	}
    }

    public static void hideMessageDecorator() {
	if (messageDecorator != null) {
	    messageDecorator.hide();
	    messageTextArea.setBackground((new Color(Display.getCurrent(), 255, 255, 255)));
	}
    }
}

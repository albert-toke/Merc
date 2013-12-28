package merc.custom.widgets;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class RejectProjectDialog extends TitleAreaDialog {

    private String[] reasons;
    private String selection;
    private Combo reasonCombo;
    private Text commentText;
    private ControlDecoration commentDecoration;

    public RejectProjectDialog(Shell parentShell) {
	super(parentShell);
    }

    public RejectProjectDialog(Shell parentShell, String[] reasons) {
	super(parentShell);
	this.reasons = reasons;
    }

    @Override
    public void create() {
	setDialogHelpAvailable(false);
	setHelpAvailable(false);
	super.create();
	setTitle("Abort Project Dialog");
	setMessage(
		"Please select the reason for the canceling of the project from the dropdown and introduce further specification in comment area.",
		IMessageProvider.INFORMATION);
    }

    @Override
    public Control createDialogArea(Composite parent) {
	Composite container = (Composite) super.createDialogArea(parent);

	Composite subContainer = new Composite(container, SWT.NONE);
	subContainer.setLayout(new GridLayout(1, false));
	subContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

	Label commentLabel = new Label(subContainer, SWT.NONE);
	commentLabel.setText("Comment:");
	commentLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));

	commentText = new Text(subContainer, SWT.BORDER | SWT.WRAP | SWT.MULTI);
	GridData commentGD = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
	commentGD.horizontalIndent = 10;
	commentText.setLayoutData(commentGD);

	commentDecoration = addTextDecorator(commentText);

	Label reasonLabel = new Label(subContainer, SWT.NONE);
	reasonLabel.setText("Reason:");
	reasonLabel.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));

	reasonCombo = new Combo(subContainer, SWT.READ_ONLY);
	GridData comboGD = new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1);
	comboGD.horizontalIndent = 10;
	reasonCombo.setLayoutData(comboGD);
	reasonCombo.setItems(reasons);
	reasonCombo.select(0);

	return container;
    }

    private static ControlDecoration addTextDecorator(Text text) {
	ControlDecoration decorator = new ControlDecoration(text, SWT.TOP | SWT.LEFT);
	FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
		FieldDecorationRegistry.DEC_ERROR);
	Image img = fieldDecoration.getImage();
	decorator.setImage(img);
	decorator.setDescriptionText("The comment field can not be empty!");
	decorator.hide();
	return decorator;
    }

    // Dialog Size
    @Override
    public Point getInitialSize() {
	return new Point(455, 300);
    }

    @Override
    public void okPressed() {
	if (commentText.getText().isEmpty()) {
	    commentDecoration.show();
	    commentText.setBackground((new Color(Display.getCurrent(), 255, 200, 200)));
	} else {
	    selection = reasonCombo.getText();
	    super.okPressed();
	}

    }

    public String getSelection() {
	return selection;
    }

    public String getComment() {
	return commentText.getText();
    }

}

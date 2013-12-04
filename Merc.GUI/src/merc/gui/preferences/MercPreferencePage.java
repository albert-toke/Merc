package merc.gui.preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import merc.gui.Activator;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import proxy.Proxy;
import business.services.HistoryBS;
import exceptions.BusinessException;

public class MercPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    private Composite providersComposite;
    private Composite jobtypeComposite;
    private Composite browserComposite;
    private Text secretText;
    private Text keyText;
    private Preferences preferences;
    private Combo providerCombo;
    private Text addressText;
    private Browser browser;
    private Label hintArea;
    private Button reqTokenButton;
    private Text authText;
    private String token;
    private String tokenSecret;
    private boolean isOkToSaveProviderPrefs = false;
    private boolean isOkToSaveJobtypePrefs = false;
    private Button addButton;
    private Button removeButton;
    private org.eclipse.swt.widgets.List jobList;
    private List<String> jobTypeList;

    private static final String MODIFICATION_HINT = "In case you wish to modify the Secret and/or the Key you need to request a new Authorization Code and "
	    + "Access Token, otherwise the modifications will NOT be saved.";
    private static final String AUTHORIZE_HINT = "- Please log in the Browser below in the Providers website and authorize the application. \n"
	    + "- After the authorization is succesfull introduce the code in the 'Authorization code' Field and Press the "
	    + "'Request Access Token' button.";
    private static final String ACCESS_SUCCESSFULL_HINT = "Access Token acquired successfuly!";
    private static final String ACCESS_FAILED_HINT = "Acquering the access token failed! Please Check if the Secret, Key and the Authorization Code are correct.";
    private static final String JOB_LIST = "jobList";
    private List<String> providers;

    public MercPreferencePage() {
	super();
	// Set the preference store for the preference page.
	IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	setPreferenceStore(store);
    }

    public MercPreferencePage(String title) {
	super(title);
	// Set the preference store for the preference page.
	IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	setPreferenceStore(store);
    }

    public MercPreferencePage(String title, ImageDescriptor image) {
	super(title, image);
	// Set the preference store for the preference page.
	IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	setPreferenceStore(store);
    }

    @Override
    public void init(IWorkbench workbench) {
	preferences = ConfigurationScope.INSTANCE.getNode("merc.plugin.preferences");
	jobTypeList = new ArrayList<String>();
	String jobString = preferences.get(JOB_LIST, "");
	for (String job : jobString.split(",")) {
	    jobTypeList.add(job);
	}
    }

    @Override
    protected Control createContents(Composite parent) {
	TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
	GridData layoutData = new GridData();
	layoutData.horizontalAlignment = SWT.FILL;
	layoutData.verticalAlignment = SWT.FILL;
	layoutData.grabExcessHorizontalSpace = true;
	layoutData.grabExcessVerticalSpace = true;
	tabFolder.setLayoutData(layoutData);
	// Tab 1 - Freelancing Provider Connection Preferences
	TabItem providerTab = new TabItem(tabFolder, SWT.NONE);
	providerTab.setText("Providers Preferences");

	createProvidersComposite(tabFolder);

	providerTab.setControl(providersComposite);

	// Tab 2 - Job Type Preference
	TabItem jobtypeTab = new TabItem(tabFolder, SWT.NONE);
	jobtypeTab.setText("Job Type Preference");

	createJobPreferenceComposite(tabFolder);

	jobtypeTab.setControl(jobtypeComposite);

	return providersComposite;
    }

    private void createProvidersComposite(TabFolder folder) {
	providersComposite = new Composite(folder, SWT.NONE);
	GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
	providersComposite.setLayoutData(gridData);
	providersComposite.setLayout(new GridLayout(2, false));

	Label providerLabel = new Label(providersComposite, SWT.NONE);
	providerLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
	providerLabel.setText("Provider:");

	providerCombo = new Combo(providersComposite, SWT.READ_ONLY);
	providerCombo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
	providerCombo.addListener(SWT.Selection, new Listener() {
	    @Override
	    public void handleEvent(Event event) {
		browserComposite.setVisible(false);
		hintArea.setText(MODIFICATION_HINT);
		String provider = providerCombo.getText();
		String secret = preferences.get(provider + "-secret", "");
		String key = preferences.get(provider + "-key", "");
		secretText.setText(secret);
		keyText.setText(key);
		providersComposite.layout();
	    }
	});
	providers = Proxy.getInstance().getProviderNames();
	providerCombo.setItems(providers.toArray(new String[providers.size()]));

	Label secretLabel = new Label(providersComposite, SWT.NONE);
	secretLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	secretLabel.setText("Secret:");

	secretText = new Text(providersComposite, SWT.BORDER);
	GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	gd.widthHint = 240;
	gd.heightHint = 18;
	secretText.setLayoutData(gd);

	Label keyLabel = new Label(providersComposite, SWT.NONE);
	keyLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	keyLabel.setText("Key:");

	keyText = new Text(providersComposite, SWT.BORDER);
	keyText.setLayoutData(gd);

	Label authLabel = new Label(providersComposite, SWT.NONE);
	authLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	authLabel.setText("Authorization code:");

	authText = new Text(providersComposite, SWT.BORDER);
	authText.setLayoutData(gd);
	authText.addModifyListener(new ModifyListener() {

	    @Override
	    public void modifyText(ModifyEvent e) {
		if (!authText.getText().isEmpty()) {
		    reqTokenButton.setEnabled(true);
		} else {
		    reqTokenButton.setEnabled(false);
		}
	    }
	});

	Composite buttonComposite = new Composite(providersComposite, SWT.NONE);
	buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
	buttonComposite.setLayout(new GridLayout(2, false));

	Button reqAuthorizationButton = new Button(buttonComposite, SWT.PUSH);
	reqAuthorizationButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	reqAuthorizationButton.setText("Request Authorization Code");
	reqAuthorizationButton.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		if (!providerCombo.getText().isEmpty()) {
		    browserComposite.setVisible(true);
		    String provider = providerCombo.getText();
		    String verificationUrl = Proxy.getInstance().getRequestTokenFromProvider(provider);
		    browser.setUrl(verificationUrl);
		    addressText.setText(verificationUrl);
		    hintArea.setText(AUTHORIZE_HINT);
		    hintArea.setVisible(true);
		    providersComposite.layout();
		}
	    }
	});

	reqTokenButton = new Button(buttonComposite, SWT.PUSH);
	reqTokenButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	reqTokenButton.setText("Request Access Token");
	reqTokenButton.setEnabled(false);
	reqTokenButton.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		try {
		    String provider = providerCombo.getText();
		    String verificationCode = authText.getText();
		    Map<String, String> tokenPair = Proxy.getInstance().getAccessTokenFromProvider(provider, verificationCode);
		    token = tokenPair.get("token");
		    tokenSecret = tokenPair.get("secret");
		    hintArea.setText(ACCESS_SUCCESSFULL_HINT);
		    isOkToSaveProviderPrefs = true;
		    HistoryBS.getInstance().eraseHistory();
		} catch (BusinessException ex) {
		    hintArea.setText(ACCESS_FAILED_HINT);
		    isOkToSaveProviderPrefs = false;
		} finally {
		    providersComposite.layout();
		}
	    }
	});

	hintArea = new Label(providersComposite, SWT.WRAP);
	GridData hintData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
	hintArea.setLayoutData(hintData);
	hintArea.setForeground(new Color(providersComposite.getDisplay(), 0, 0, 0));

	createBrowser();

	addTooltipToFileds("This field contains the Key Code!", this.keyText);
	addTooltipToFileds("This field contains the Secret Code here!", this.secretText);
	addTooltipToFileds("Please introduce Authorization Code here!", this.authText);
    }

    private void createJobPreferenceComposite(TabFolder folder) {
	jobtypeComposite = new Composite(folder, SWT.NONE);

	jobtypeComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	jobtypeComposite.setLayout(new GridLayout(2, false));

	Label preferenceLabel = new Label(jobtypeComposite, SWT.NONE);
	preferenceLabel.setText("Job Types:");
	preferenceLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 2, 1));

	jobList = new org.eclipse.swt.widgets.List(jobtypeComposite, SWT.SINGLE | SWT.BORDER);
	GridData jobGridData = new GridData(SWT.FILL, SWT.TOP, true, true, 1, 2);
	jobGridData.widthHint = 120;
	jobGridData.heightHint = 300;
	jobList.setLayoutData(jobGridData);
	jobList.setItems(jobTypeList.toArray(new String[jobTypeList.size()]));
	// jobList.setItems(new String[] { "Java", "Groovy", "JSF" });
	jobList.addSelectionListener(new SelectionAdapter() {

	    @Override
	    public void widgetSelected(SelectionEvent e) {
		if (jobList.getSelectionCount() > 0) {
		    removeButton.setEnabled(true);
		} else {
		    removeButton.setEnabled(false);
		}
	    }
	});

	addButton = new Button(jobtypeComposite, SWT.PUSH);
	GridData addGridData = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
	addGridData.widthHint = 75;
	addButton.setLayoutData(addGridData);
	addButton.setText("Add Job");
	addButton.addSelectionListener(new SelectionListener() {

	    @Override
	    public void widgetSelected(SelectionEvent e) {
		InputDialog dlg = new InputDialog(Display.getCurrent().getActiveShell(), "New Job Type Entry",
			"Enter a Job type to the preferences:", "", new JobTypeInputValidator());
		if (dlg.open() == Window.OK) {

		    String newJobType = dlg.getValue();
		    boolean found = false;
		    for (String job : jobTypeList) {
			if (job.equalsIgnoreCase(newJobType)) {
			    found = true;
			    break;
			}
		    }
		    if (!found) {
			jobTypeList.add(newJobType);
			jobList.add(newJobType);
			isOkToSaveJobtypePrefs = true;
		    }
		}
	    }

	    @Override
	    public void widgetDefaultSelected(SelectionEvent e) {
	    }
	});

	removeButton = new Button(jobtypeComposite, SWT.PUSH);
	GridData removeGridData = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
	removeGridData.widthHint = 75;
	removeButton.setLayoutData(removeGridData);
	removeButton.setText("Remove Job");
	removeButton.setEnabled(false);
	removeButton.addSelectionListener(new SelectionListener() {

	    @Override
	    public void widgetSelected(SelectionEvent e) {
		String[] selections = jobList.getSelection();
		jobList.remove(selections[0]);
		jobTypeList.remove(selections[0]);
		isOkToSaveJobtypePrefs = true;
	    }

	    @Override
	    public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	    }
	});

    }

    private void createBrowser() {
	browserComposite = new Composite(providersComposite, SWT.BORDER);
	browserComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
	browserComposite.setLayout(new GridLayout(2, false));
	browserComposite.setVisible(false);

	Label addressLabel = new Label(browserComposite, SWT.NONE);
	addressLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
	addressLabel.setText("URL:");

	addressText = new Text(browserComposite, SWT.READ_ONLY | SWT.BORDER);
	addressText.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

	GridData grid = new GridData(SWT.FILL, SWT.TOP, true, true, 2, 1);
	grid.heightHint = 400;

	browser = new Browser(browserComposite, SWT.BORDER);
	browser.setLayoutData(grid);
    }

    private void addTooltipToFileds(String message, Text textFiled) {
	ControlDecoration decorator = new ControlDecoration(textFiled, SWT.TOP | SWT.LEFT);
	FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
		FieldDecorationRegistry.DEC_INFORMATION);
	Image img = fieldDecoration.getImage();
	decorator.setImage(img);
	decorator.setDescriptionText(message);
	decorator.show();

    }

    @Override
    public boolean performOk() {
	if (isOkToSaveProviderPrefs) {
	    try {
		String provider = providerCombo.getText();
		preferences.put(provider + "-secret", secretText.getText());
		preferences.put(provider + "-key", keyText.getText());
		preferences.put(provider + "-token", token);
		preferences.put(provider + "-tokenSecret", tokenSecret);
		preferences.putLong(provider + "-userId", Proxy.getInstance().getUserIdByProvider(provider));
		preferences.putBoolean("goodToGo", true);

		// Forces the application to save the preferences
		preferences.flush();
	    } catch (BackingStoreException e) {
		e.printStackTrace();
	    } catch (BusinessException e) {
		e.printStackTrace();
	    }
	}
	if (isOkToSaveJobtypePrefs) {
	    try {
		StringBuilder stringBuilder = new StringBuilder();
		for (String job : jobTypeList) {
		    stringBuilder.append(job + ",");
		}
		preferences.put(JOB_LIST, stringBuilder.toString());

		preferences.flush();
	    } catch (BackingStoreException e) {
		e.printStackTrace();
	    }
	}
	return super.performOk();
    }

    @Override
    public void dispose() {
	super.dispose();
	providersComposite.dispose();
    }
}

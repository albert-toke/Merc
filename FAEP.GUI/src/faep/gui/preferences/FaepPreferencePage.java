package faep.gui.preferences;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import proxy.Proxy;
import business.services.HistoryBS;
import exceptions.BusinessException;
import faep.gui.Activator;

public class FaepPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    private Composite top;
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
    private boolean isOkToSave = false;

    private static final String MODIFICATION_HINT = "In case you wish to modify the Secret and/or the Key you need to request a new Authorization Code and "
	    + "Access Token, otherwise the modifications will NOT be saved.";
    private static final String AUTHORIZE_HINT = "- Please log in the Browser below in the Providers website and authorize the application. \n"
	    + "- After the authorization is succesfull introduce the code in the 'Authorization code' Field and Press the "
	    + "'Request Access Token' button.";
    private static final String ACCESS_SUCCESSFULL_HINT = "Access Token acquired successfuly!";
    private static final String ACCESS_FAILED_HINT = "Acquering the access token failed! Please Check if the Secret, Key and the Authorization Code are correct.";
    private List<String> providers;

    public FaepPreferencePage() {
	super();
	// Set the preference store for the preference page.
	IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	setPreferenceStore(store);
    }

    public FaepPreferencePage(String title) {
	super(title);
	// Set the preference store for the preference page.
	IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	setPreferenceStore(store);
    }

    public FaepPreferencePage(String title, ImageDescriptor image) {
	super(title, image);
	// Set the preference store for the preference page.
	IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	setPreferenceStore(store);
    }

    @Override
    public void init(IWorkbench workbench) {
	preferences = ConfigurationScope.INSTANCE.getNode("faep.plugin.preferences");
    }

    @Override
    protected Control createContents(Composite parent) {
	top = new Composite(parent, SWT.NONE);

	top.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	top.setLayout(new GridLayout(2, false));

	Label providerLabel = new Label(top, SWT.NONE);
	providerLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
	providerLabel.setText("Provider:");

	providerCombo = new Combo(top, SWT.READ_ONLY);
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
		top.layout();
	    }
	});
	providers = Proxy.getInstance().getProviderNames();
	providerCombo.setItems(providers.toArray(new String[providers.size()]));

	Label secretLabel = new Label(top, SWT.NONE);
	secretLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	secretLabel.setText("Secret:");

	secretText = new Text(top, SWT.BORDER);
	GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	gd.widthHint = 240;
	gd.heightHint = 18;
	secretText.setLayoutData(gd);

	Label keyLabel = new Label(top, SWT.NONE);
	keyLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	keyLabel.setText("Key:");

	keyText = new Text(top, SWT.BORDER);
	keyText.setLayoutData(gd);

	Label authLabel = new Label(top, SWT.NONE);
	authLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	authLabel.setText("Authorization code:");

	authText = new Text(top, SWT.BORDER);
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

	Composite buttonComposite = new Composite(top, SWT.NONE);
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
		    top.layout();
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
		    isOkToSave = true;
		    HistoryBS.getInstance().eraseHistory();
		} catch (BusinessException ex) {
		    hintArea.setText(ACCESS_FAILED_HINT);
		    isOkToSave = false;
		} finally {
		    top.layout();
		}
	    }
	});

	hintArea = new Label(top, SWT.WRAP);
	GridData hintData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
	hintArea.setLayoutData(hintData);
	hintArea.setForeground(new Color(top.getDisplay(), 0, 0, 0));

	createBrowser();

	return top;
    }

    private void createBrowser() {
	browserComposite = new Composite(top, SWT.BORDER);
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

    @Override
    public boolean performOk() {
	if (isOkToSave) {
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
	return super.performOk();
    }

    @Override
    public void dispose() {
	super.dispose();
	top.dispose();
    }
}

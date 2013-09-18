package faep.gui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import faep.controller.FaepController;
import faep.gui.views.FaepView;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    private static FaepController faepController;

    // The plug-in ID
    public static final String PLUGIN_ID = "FAEP.GUI"; //$NON-NLS-1$

    // The shared instance
    private static Activator plugin;

    /**
     * The constructor
     */
    public Activator() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
	super.start(context);
	plugin = this;
	// Preferences preferences = ConfigurationScope.INSTANCE.getNode("faep.plugin.preferences");
	// Proxy proxy = Proxy.getInstance();
	// List<String> providers = proxy.getProviderNames();
	// boolean goodToGo = false;
	// for (String provider : providers) {
	// String secret = preferences.get(provider + "-tokenSecret", "");
	// String token = preferences.get(provider + "-token", "");
	// if (!token.isEmpty() && !secret.isEmpty()) {
	// proxy.getGatewayByProvider(provider).setAccessToken(token, secret);
	// long userId = proxy.getUserIdByProvider(provider);
	// preferences.putLong(provider + "-userId", userId);
	// System.out.println("userid" + userId);
	// goodToGo = true;
	// break;
	// }
	// }
	// // TODO better verification in case it was revoked
	// if (goodToGo) {
	// preferences.putBoolean("goodToGo", true);
	// } else {
	// preferences.putBoolean("goodToGo", false);
	// }
	// preferences.putBoolean("goodToGo", false);
	// try {
	// preferences.flush();
	// } catch (BackingStoreException e) {
	// e.printStackTrace();
	// }
	faepController = FaepController.getInstance();
	faepController.addPreferenceChangeListener();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
	plugin = null;
	super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static Activator getDefault() {
	return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in relative path
     * 
     * @param path
     *            the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
	return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    /**
     * After the view has been initialized this method is called for the assigning of the controller to the specified view.
     * 
     * @param view
     */
    public static void assignControllerToView(ViewPart view) {
	faepController.setView((FaepView) view);
    }
}

package merc.plugin;

import gateway.AbstractApiGateway;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import proxy.Proxy;
import business.services.HistoryBS;
import constants.and.enums.MercPluginConstants;

public class Activator implements BundleActivator {

    private static BundleContext context;
    private static final String EXTENSION_POINT_ID = "merc.plugin.gateway";
    private static final Logger LOGGER = Logger.getLogger(MercPluginConstants.LOGGER_NAME);

    static BundleContext getContext() {
	return context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext bundleContext) throws Exception {
	Activator.context = bundleContext;

	SimpleFormatter loggerFormatter = new SimpleFormatter();
	FileHandler handler = new FileHandler("dropins/nyest.log");
	handler.setFormatter(loggerFormatter);

	LOGGER.addHandler(handler);

	// Getting all the provider gateways through the extension point.
	IExtensionRegistry registry = Platform.getExtensionRegistry();
	IConfigurationElement[] extensions2 = registry.getConfigurationElementsFor(EXTENSION_POINT_ID);

	Proxy proxy = Proxy.getInstance();
	Preferences preferences = ConfigurationScope.INSTANCE.getNode("merc.plugin.preferences");
	boolean goodToGo = false;

	for (int i = 0; i < extensions2.length; i++) {
	    IConfigurationElement element = extensions2[i];
	    AbstractApiGateway gatewayInstance = null;
	    try {
		gatewayInstance = (AbstractApiGateway) element.createExecutableExtension("class");
		// gatewayInstance.initGateway("7dc7ee059324b47ef9c248183279ea0d436c8ec0",
		// "9be5f0f5f81e9de087fd437c25e9007d0e57d6a7");
		String key = preferences.get(gatewayInstance.getProvider() + "-key", "");
		String secret = preferences.get(gatewayInstance.getProvider() + "-secret", "");
		String authSecret = preferences.get(gatewayInstance.getProvider() + "-tokenSecret", "");
		String authToken = preferences.get(gatewayInstance.getProvider() + "-token", "");
		LOGGER.info("Loaded gateway for provider:" + gatewayInstance.getProvider());
		if (!authToken.isEmpty() && !authSecret.isEmpty()) {
		    gatewayInstance.initGateway(key, secret);
		} else {
		    gatewayInstance.initGatewayWithDefaultValues();
		}

		if (!key.isEmpty() && !secret.isEmpty()) {
		    gatewayInstance.setAccessToken(authToken, authSecret);

		    proxy.addGatewayToWorkingGatewaysAndAllGateways(gatewayInstance);

		    long userId = proxy.getUserIdByProvider(gatewayInstance.getProvider());
		    preferences.putLong(gatewayInstance.getProvider() + "-userId", userId);
		    LOGGER.info(gatewayInstance.getProvider() + "userid " + userId);
		    goodToGo = true;

		}
		proxy.addGatewayToAllGateways(gatewayInstance);

	    } catch (CoreException e) {
		LOGGER.severe(e.getMessage());
	    } catch (RuntimeException e) {
		// In case the specific gateway is not properly configured or is unavailable.
		if (gatewayInstance != null) {
		    proxy.addGatewayToAllGateways(gatewayInstance);
		}
		LOGGER.severe("In Plugin Activator:" + e.getMessage());
	    }
	}

	// TODO better verification in case it was revoked
	if (goodToGo) {
	    preferences.putBoolean("goodToGo", true);
	} else {
	    preferences.putBoolean("goodToGo", false);
	}
	try {
	    preferences.flush();
	} catch (BackingStoreException e) {
	    LOGGER.severe("In Plugin Activator:" + e.getMessage());
	}

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext bundleContext) throws Exception {
	// Save search history before exit
	HistoryBS.getInstance().saveSearchHistory();
	Activator.context = null;
    }

}

package faep.plugin;

import gateway.AbstractApiGateway;

import java.util.ArrayList;
import java.util.List;

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

public class Activator implements BundleActivator {

    private static BundleContext context;
    private static final String EXTENSION_POINT_ID = "faep.plugin.gateway";

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

	List<AbstractApiGateway> gatewayList = new ArrayList<AbstractApiGateway>();
	IExtensionRegistry registry = Platform.getExtensionRegistry();
	IConfigurationElement[] extensions2 = registry.getConfigurationElementsFor(EXTENSION_POINT_ID);

	for (int i = 0; i < extensions2.length; i++) {
	    IConfigurationElement element = extensions2[i];
	    try {
		AbstractApiGateway gatewayInstance = (AbstractApiGateway) element.createExecutableExtension("class");
		gatewayInstance.initGateway("7dc7ee059324b47ef9c248183279ea0d436c8ec0",
			"9be5f0f5f81e9de087fd437c25e9007d0e57d6a7");
		gatewayList.add(gatewayInstance);
		System.out.println(gatewayInstance.getProvider());
	    } catch (CoreException e) {
		e.printStackTrace();
	    }
	}

	Proxy proxy = Proxy.getInstance(gatewayList);

	Preferences preferences = ConfigurationScope.INSTANCE.getNode("faep.plugin.preferences");
	List<String> providers = proxy.getProviderNames();
	boolean goodToGo = false;
	for (String provider : providers) {
	    String secret = preferences.get(provider + "-tokenSecret", "");
	    String token = preferences.get(provider + "-token", "");
	    if (!token.isEmpty() && !secret.isEmpty()) {
		proxy.getGatewayByProvider(provider).setAccessToken(token, secret);
		long userId = proxy.getUserIdByProvider(provider);
		preferences.putLong(provider + "-userId", userId);
		System.out.println("userid" + userId);
		goodToGo = true;
		break;
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
	    e.printStackTrace();
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

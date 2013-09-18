package freelancer.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import freelancer.test.util.GatewaysInitializer;
import gateway.AbstractApiGateway;

import java.util.List;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import proxy.Proxy;

public class AccountBSTest {

	private Proxy proxy;
	
	@Before
	public void initalizeProxyWithGateways(){
		List<AbstractApiGateway> gateways = GatewaysInitializer.initGatewaySelecter();
		proxy = Proxy.getInstance(gateways);
	}
	
	@Test
	public void initialisationTestForLocalToken() {
		try {
			assertTrue(proxy.getTokenLocally().isEmpty());
		} catch (Exception e) {
			fail("Failed:" + e.getMessage());
		}
	}

	@Test
	@Ignore("Only use manually + delete token.json")
	public void initialisationTestForRemoteToken() {
		try {
			String verifUrl = proxy.getRequestTokenFromProvider("freelancer");
			assertNotNull(verifUrl);
			System.out.println("Go here and validate:" + verifUrl);
			Scanner in = new Scanner(System.in);
			proxy.getAccessTokenFromProvider("freelancer", in.nextLine());
		} catch (Exception e) {
			fail("" + e.getMessage());
		}
	}
}

package freelancer.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import exceptions.BusinessException;
import freelancer.test.util.GatewaysInitializer;
import gateway.AbstractApiGateway;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import proxy.Proxy;

import common.wrappers.BidRequest;

public class BidBSTest {

	private static final String MYUSERNAME = "jindster7";
	private static final long MYPROJECTID = 959;
	private static final String PROVIDER = "freelancer";
	private Proxy proxy;
	
	@Before
	public void initalizeProxyWithGateways(){
		List<AbstractApiGateway> gateways = GatewaysInitializer.initGatewaySelecter();
		proxy = Proxy.getInstance(gateways);
	}
	
	@Test
	public void placeBidTest() {
		assertTrue(proxy.getTokenLocally().isEmpty());
		BidRequest bid = new BidRequest();
		bid.setProvider(PROVIDER);
		bid.setCurrencyId(1); // Dollar
		bid.setAmount(250);
		bid.setDays(30);
		bid.setDescription("I would gladly accomplish this Project for you!");
		bid.setProjectId(MYPROJECTID);
		try {
			proxy.placeBid(bid);
		} catch (BusinessException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void retractBidTest() {
		assertTrue(proxy.getTokenLocally().isEmpty());
		try {
			proxy.retractBidFromProject(MYPROJECTID, PROVIDER);
		} catch (BusinessException ex) {
			fail(ex.getMessage());
		}
	}

	@Test
	// @Ignore("Just when bid accepted")
	public void acceptBidWonTest() {
		assertTrue(proxy.getTokenLocally().isEmpty());
		int state = 0; // 0 - Decline || 1 - Accept
		try {
			proxy.acceptBidWon(MYPROJECTID, state, PROVIDER);
		} catch (BusinessException ex) {
			fail(ex.getMessage());
		}
	}
}

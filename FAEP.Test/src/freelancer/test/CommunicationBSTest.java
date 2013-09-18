package freelancer.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import exceptions.BusinessException;
import freelancer.test.util.GatewaysInitializer;
import gateway.AbstractApiGateway;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import proxy.Proxy;

import common.wrappers.Message;
import common.wrappers.OutgoingMessage;

public class CommunicationBSTest {

    private static final long MYPROJECT = 959;
    private static final String TOUSERNAME = "jindzo7";
    private static final String PROVIDER = "freelancer";
    private Proxy proxy;

    @Before
    public void initalizeProxyWithGateways() {
	List<AbstractApiGateway> gateways = GatewaysInitializer.initGatewaySelecter();
	proxy = Proxy.getInstance(gateways);
    }

    @Test
    public void getMessagesTest() {
	assertTrue(proxy.getTokenLocally().isEmpty());
	// providerre kiboviteni
	List<Message> messages;
	try {
	    messages = proxy.getMessages(MYPROJECT, PROVIDER);
	    assertNotNull(messages);
	} catch (BusinessException e) {
	    e.printStackTrace();
	    fail(e.getMessage());
	}

    }

    @Test
    public void getUnreadMessagesTest() {
	assertTrue(proxy.getTokenLocally().isEmpty());
	int count = 0;
	try {
	    count = proxy.getUnreadMessageCount();
	} catch (BusinessException e) {
	    fail(e.getMessage());
	}
	System.out.println(count);
    }

    @Test
    public void sendMessageTest() {
	assertTrue(proxy.getTokenLocally().isEmpty());
	OutgoingMessage message = new OutgoingMessage();
	message.setMessageText("test message");
	message.setProjectId(MYPROJECT);
	message.setUrsername(TOUSERNAME);
	message.setProvider(PROVIDER);
	try {
	    proxy.sendMessage(message);
	} catch (BusinessException e) {
	    fail(e.getMessage());
	}
    }

    @Test
    public void getSentMessagesTest() {
	assertTrue(proxy.getTokenLocally().isEmpty());
	try {
	    proxy.getSentMessages();
	} catch (BusinessException e) {
	    e.printStackTrace();
	    fail(e.getMessage());

	}
    }

    @Test
    public void markMessageAsReadTest() {
	assertTrue(proxy.getTokenLocally().isEmpty());
	try {
	    proxy.markMessageAsRead(1627, PROVIDER);
	} catch (BusinessException e) {
	    e.printStackTrace();
	    fail(e.getMessage());
	}
    }
}

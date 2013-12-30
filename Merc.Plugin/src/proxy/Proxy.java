package proxy;

import exceptions.BusinessException;
import gateway.AbstractApiGateway;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import common.wrappers.Bid;
import common.wrappers.BidRequest;
import common.wrappers.Job;
import common.wrappers.JobSearch;
import common.wrappers.Message;
import common.wrappers.Notification;
import common.wrappers.Project;
import common.wrappers.ProjectPostMessage;
import common.wrappers.ProjectPublicMessage;

public class Proxy {

    private static final Logger LOGGER = Logger.getLogger(Proxy.class.getName());
    private static Set<AbstractApiGateway> allGateways;
    private static Set<AbstractApiGateway> workingGateways;
    private static Proxy proxy;

    private Proxy() {
	allGateways = new HashSet<AbstractApiGateway>();
	workingGateways = new HashSet<AbstractApiGateway>();
    }

    public static Proxy getInstance() {
	if (proxy == null) {
	    proxy = new Proxy();
	}
	return proxy;
    }

    public static Proxy getInstance(Set<AbstractApiGateway> gatewayList) {
	if (proxy == null) {
	    proxy = new Proxy();
	}
	allGateways = gatewayList;
	return proxy;
    }

    public void addGatewayToWorkingGatewaysAndAllGateways(AbstractApiGateway gateway) {

	workingGateways.add(gateway);
	allGateways.add(gateway);
    }

    public void addGatewayToAllGateways(AbstractApiGateway gateway) {

	allGateways.add(gateway);
    }

    public void moveGatewayFromAllToWorking(String provider) {
	for (AbstractApiGateway gateway : allGateways) {
	    if (gateway.getProvider().equalsIgnoreCase(provider)) {
		workingGateways.add(gateway);
		break;
	    }
	}
    }

    public List<String> getTokenLocally() {
	List<String> providerLocallyNotFound = new ArrayList<String>();
	for (AbstractApiGateway gateway : workingGateways) {
	    try {
		if (!gateway.initializeGatewayLocally())
		    providerLocallyNotFound.add(gateway.getProvider());
	    } catch (Exception e) {
		providerLocallyNotFound.add(gateway.getProvider());
		e.printStackTrace();
	    }
	}
	return providerLocallyNotFound;
    }

    public long getUserIdByProvider(String provider) throws BusinessException {
	AbstractApiGateway gateway = getGatewayByProvider(provider);
	long id;
	if (gateway.getUserId() != 0) {
	    return gateway.getUserId();
	} else {
	    id = gateway.getUserIdFromProvider();
	    gateway.setUserId(id);
	    return id;
	}
    }

    public String getRequestTokenFromProvider(String provider) {
	AbstractApiGateway gateway = getGatewayByProvider(provider);
	return gateway.getVerificationURL();
    }

    public Map<String, String> getAccessTokenFromProvider(String provider, String verificationCode) throws BusinessException {
	AbstractApiGateway gateway = getGatewayByProvider(provider);
	Map<String, String> accessToken = gateway.getAccessTokenFromProvider(verificationCode);
	return accessToken;
    }

    public List<Job> searchJobs(JobSearch jobSearchDTO) {
	List<Job> jobsList = new ArrayList<Job>();
	for (AbstractApiGateway gateway : workingGateways) {
	    try {
		List<Job> tempList = gateway.searchJobs(jobSearchDTO);
		jobsList.addAll(tempList);
	    } catch (BusinessException e) {
		LOGGER.severe(e.getMessage());
		// TODO handle if one of the sites is offline
	    }
	}
	return jobsList;
    }

    public List<Bid> getBidsForProject(long projectId, String provider) throws BusinessException {
	AbstractApiGateway gateway = getGatewayByProvider(provider);
	List<Bid> bids = gateway.getBidsForProject(projectId);
	return bids;
    }

    public List<ProjectPublicMessage> getPublicMessagesForProject(long projectId, String provider) throws BusinessException {
	AbstractApiGateway gateway = getGatewayByProvider(provider);
	List<ProjectPublicMessage> messages = gateway.getPublicMessagesForProject(projectId);
	return messages;
    }

    public void postPublicMessageOnProject(ProjectPostMessage message) throws BusinessException {
	AbstractApiGateway gateway = getGatewayByProvider(message.getProvider());
	gateway.postPublicMessagesOnProject(message);
    }

    public List<Notification> getNotifications() throws BusinessException {
	List<Notification> notifications = new ArrayList<Notification>();
	for (AbstractApiGateway gateway : workingGateways) {
	    notifications.addAll(gateway.getNotifications());
	}
	return notifications;

    }

    public Project getProjectDetails(long projectId, String provider) throws BusinessException {
	AbstractApiGateway gateway = getGatewayByProvider(provider);
	Project project = gateway.getProjectDetails(projectId);
	return project;
    }

    // ujrairni, nem kell projectid
    public List<Message> getMessages(long projectId, long ownerId, String provider) throws BusinessException {

	List<Message> messages = new ArrayList<Message>();
	AbstractApiGateway gateway = getGatewayByProvider(provider);
	messages.addAll(gateway.getProjectMessages(projectId, ownerId));
	return messages;
    }

    public int getUnreadMessageCount() throws BusinessException {
	int count = 0;
	for (AbstractApiGateway gateway : workingGateways) {
	    count += gateway.getUnreadMessageCount();
	}
	return count;
    }

    public void sendMessage(Message msg) throws BusinessException {
	AbstractApiGateway gateway = getGatewayByProvider(msg.getProvider());
	gateway.sendMessage(msg);
    }

    public void placeBid(BidRequest bid) throws BusinessException {
	AbstractApiGateway gateway = getGatewayByProvider(bid.getProvider());
	gateway.placeBid(bid);
    }

    public void retractBidFromProject(long projectId, String provider) throws BusinessException {
	AbstractApiGateway gateway = getGatewayByProvider(provider);
	gateway.retractBidFromProject(projectId);
    }

    public void acceptBidWon(long projectId, String provider) throws BusinessException {
	AbstractApiGateway gateway = getGatewayByProvider(provider);
	gateway.acceptBidWon(projectId);
    }

    public void declineBidWon(long projectId, String provider) throws BusinessException {
	AbstractApiGateway gateway = getGatewayByProvider(provider);
	gateway.declineBidWon(projectId);
    }

    public List<Message> getSentMessages() throws BusinessException {
	List<Message> messages = new ArrayList<Message>();
	for (AbstractApiGateway gateway : workingGateways) {
	    messages.addAll(gateway.getSentMessages());
	}
	return messages;
    }

    public void markMessageAsRead(long messageId, String provider) throws BusinessException {
	AbstractApiGateway gateway = getGatewayByProvider(provider);
	gateway.markMessageAsRead(messageId);
    }

    public List<Job> getBiddedProjects() throws BusinessException {
	List<Job> projects = new ArrayList<Job>();
	for (AbstractApiGateway gateway : workingGateways) {
	    projects.addAll(gateway.getBiddedProjects());
	}
	return projects;
    }

    public List<Job> getWonBiddedProjects() throws BusinessException {
	List<Job> projects = new ArrayList<Job>();
	for (AbstractApiGateway gateway : workingGateways) {
	    projects.addAll(gateway.getWonBiddedProjects());
	}
	return projects;
    }

    public AbstractApiGateway getGatewayByProvider(String provider) {
	AbstractApiGateway gateway = null;
	for (AbstractApiGateway gate : workingGateways) {
	    if (gate.getProvider().equals(provider)) {
		gateway = gate;
	    }
	}
	return gateway;
    }

    public List<String> getWorkingProviderNames() {
	List<String> providers = new ArrayList<String>();
	for (AbstractApiGateway gt : workingGateways) {
	    providers.add(gt.getProvider());
	}
	return providers;
    }

    public List<String> getAllProviderNames() {
	List<String> providers = new ArrayList<String>();
	for (AbstractApiGateway gt : allGateways) {
	    providers.add(gt.getProvider());
	}
	return providers;
    }

    // Test method
    public void getJobList() {
	for (AbstractApiGateway gateway : allGateways) {
	    gateway.getJobList();
	}
    }
}

package gateway;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import mapper.BiddedProjectStatusEnum;
import mapper.FreelancerMapper;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FreelancerApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.SignatureType;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import common.wrappers.Bid;
import common.wrappers.BidRequest;
import common.wrappers.Job;
import common.wrappers.JobSearch;
import common.wrappers.Message;
import common.wrappers.Notification;
import common.wrappers.OutgoingMessage;
import common.wrappers.Project;
import common.wrappers.ProjectPostMessage;
import common.wrappers.ProjectPublicMessage;

import enums.JobStatusEnum;
import exceptions.BusinessException;

public class FreelancerGateway extends AbstractApiGateway {

    private static final Logger LOGGER = Logger.getLogger(FreelancerGateway.class.getName());

    private FreelancerApi.Sandbox freelancerApi;
    private FreelancerMapper mapper;

    private static final String SCOPE = "code";
    private static final String BASE_URL = "http://api.sandbox.freelancer.com/";

    public FreelancerGateway() {

	// System.setProperty("http.proxyPort", "3128");
	// System.setProperty("http.proxyHost", "proxy.utcluj.ro");
    }

    @Override
    public void initGateway(String apiKey, String apiSecret) {
	super.initGateway(apiKey, apiSecret);
	LOGGER.setLevel(Level.INFO);
	this.freelancerApi = new FreelancerApi.Sandbox();
	service = new ServiceBuilder().provider(FreelancerApi.Sandbox.class).signatureType(SignatureType.QueryString)
		.apiKey(apiKey).apiSecret(apiSecret).scope(SCOPE).build();
	this.mapper = new FreelancerMapper();
    };

    /**
     * {@inheritDoc}
     */
    public boolean initializeGatewayLocally() throws JsonParseException, JsonMappingException, IOException {
	boolean isInitialized = true;

	if (accessToken == null) {
	    isInitialized = false;
	    this.accessToken = dao.getTokenByProvider(getProvider());
	    LOGGER.info("Attempting to acquired Token localy.");
	    if (accessToken != null) {
		isInitialized = true;
		LOGGER.info("Token acquired localy.");
	    }
	}
	return isInitialized;
    }

    /**
     * {@inheritDoc}
     */
    public String getVerificationURL() {
	LOGGER.info("Fetching request Token!");
	requestToken = service.getRequestToken();
	LOGGER.info("Got the Request Token!");
	return this.freelancerApi.getAuthorizationUrl(requestToken);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, String> getAccessTokenFromProvider(String verificationCode) throws BusinessException {
	Verifier verifier = new Verifier(verificationCode);
	LOGGER.info("Trading verifier for acces token!");
	try {
	    accessToken = service.getAccessToken(requestToken, verifier);
	} catch (Exception e) {
	    throw new BusinessException(e);
	}
	Map<String, String> tokenPair = new HashMap<String, String>();
	tokenPair.put("token", accessToken.getToken());
	tokenPair.put("secret", accessToken.getSecret());
	return tokenPair;
    }

    public void getJobList() {
	Response response = null;
	if (accessToken != null) {
	    String category = "Job";
	    String method = "getJobList" + EXT;
	    OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.sandbox.freelancer.com/" + category + "/" + method);
	    service.signRequest(accessToken, request);
	    request.addHeader("GData-Version", "3.0");
	    response = request.send();
	    System.out.println(response.getCode());
	    System.out.println(response.getBody());
	}
    }

    public Response getPublicMessages(String projectId) {
	Response response = null;
	if (accessToken != null) {
	    String method = "getPublicMessages.json";
	    OAuthRequest request = new OAuthRequest(Verb.GET, BASE_URL + FreelancerMethodCategoryEnum.Project + "/" + method);
	    request.addQuerystringParameter("projectid", projectId);
	    service.signRequest(accessToken, request);
	    request.addHeader("GData-Version", "3.0");
	    response = request.send();
	}
	return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Job> searchJobs(JobSearch jobSearchDto) throws BusinessException {
	List<Job> jobDTOList = null;
	String requestUrl = BASE_URL + FreelancerMethodCategoryEnum.Project + "/" + "searchProjects" + EXT;
	HashMap<String, String> parameters = (HashMap<String, String>) mapper.convertProjectSearchDtoToString(jobSearchDto);
	OAuthRequest request = createRequest(Verb.GET, requestUrl, parameters);
	service.signRequest(accessToken, request);
	request.addHeader("GData-Version", "3.0");
	Response response = request.send();
	jobDTOList = mapper.convertProjectSearchResponseToSystem(response.getBody());
	return jobDTOList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Bid> getBidsForProject(long projectId) throws BusinessException {
	List<Bid> bidDTOList = null;
	String requestUrl = BASE_URL + FreelancerMethodCategoryEnum.Project + "/" + "getBidsDetails" + EXT;
	OAuthRequest request = new OAuthRequest(Verb.GET, requestUrl);
	request.addQuerystringParameter("projectid", Long.toString(projectId));
	service.signRequest(accessToken, request);
	request.addHeader("GData-Version", "3.0");
	Response response = request.send();
	bidDTOList = mapper.convertBidListToDTO(response.getBody());
	return bidDTOList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProjectPublicMessage> getPublicMessagesForProject(long projectId) throws BusinessException {
	List<ProjectPublicMessage> publicMessages = null;
	String requestUrl = BASE_URL + FreelancerMethodCategoryEnum.Project + "/" + "getPublicMessages" + EXT;
	OAuthRequest request = new OAuthRequest(Verb.GET, requestUrl);
	request.addQuerystringParameter("projectid", Long.toString(projectId));
	service.signRequest(accessToken, request);
	request.addHeader("GData-Version", "3.0");
	Response response = request.send();
	publicMessages = mapper.convertProjectPublicMessagesToSystem(response.getBody());
	return publicMessages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postPublicMessagesOnProject(ProjectPostMessage message) throws BusinessException {
	String requestUrl = BASE_URL + FreelancerMethodCategoryEnum.Project + "/" + "postPublicMessage" + EXT;
	OAuthRequest request = new OAuthRequest(Verb.GET, requestUrl);
	request.addQuerystringParameter("projectid", Long.toString(message.getProjectId()));
	request.addQuerystringParameter("messagetext", message.getMessage());
	service.signRequest(accessToken, request);
	request.addHeader("GData-Version", "3.0");
	Response response = request.send();
	mapper.convertConfirmation(response.getBody());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Notification> getNotifications() throws BusinessException {
	String requestUrl = BASE_URL + FreelancerMethodCategoryEnum.Notification + "/" + "getNotification" + EXT;
	OAuthRequest request = new OAuthRequest(Verb.GET, requestUrl);
	service.signRequest(accessToken, request);
	request.addHeader("GData-Version", "3.0");
	Response response = request.send();
	List<Notification> notifications = mapper.convertNotificationsToSystem(response.getBody());
	return notifications;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Project getProjectDetails(long projectId) throws BusinessException {
	String requestUrl = BASE_URL + FreelancerMethodCategoryEnum.Project + "/" + "getProjectDetails" + EXT;
	OAuthRequest request = new OAuthRequest(Verb.GET, requestUrl);
	request.addQuerystringParameter("projectid", Long.toString(projectId));
	service.signRequest(accessToken, request);
	request.addHeader("GData-Version", "3.0");
	Response response = request.send();
	Project projectDetails = null;
	projectDetails = mapper.convertProjectDetailsJsonToSystem(response.getBody());
	return projectDetails;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Message> getMessages(long projectId) throws BusinessException {
	String requestUrl = BASE_URL + FreelancerMethodCategoryEnum.Message + "/" + "getInboxMessages" + EXT;
	OAuthRequest request = new OAuthRequest(Verb.GET, requestUrl);
	if (projectId != 0) {
	    request.addQuerystringParameter("projectid", Long.toString(projectId));
	}
	service.signRequest(accessToken, request);
	request.addHeader("GData-Version", "3.0");
	Response response = request.send();
	List<Message> messages = null;
	messages = mapper.convertIncomingMessagesToSystem(response.getBody());
	return messages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(OutgoingMessage msg) throws BusinessException {
	String requestUrl = BASE_URL + FreelancerMethodCategoryEnum.Message + "/" + "sendMessage" + EXT;
	OAuthRequest request = new OAuthRequest(Verb.GET, requestUrl);
	request.addQuerystringParameter("projectid", Long.toString(msg.getProjectId()));
	request.addQuerystringParameter("messagetext", msg.getMessageText());
	request.addQuerystringParameter("username", msg.getUrsername());
	service.signRequest(accessToken, request);
	request.addHeader("GData-Version", "3.0");
	Response response = request.send();
	mapper.convertConfirmation(response.getBody());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void placeBid(BidRequest bid) throws BusinessException {
	String requestUrl = BASE_URL + FreelancerMethodCategoryEnum.Freelancer + "/" + "placeBidOnProject" + EXT;
	OAuthRequest request = new OAuthRequest(Verb.GET, requestUrl);
	request.addQuerystringParameter("amount", Double.toString(bid.getAmount()));
	request.addQuerystringParameter("days", Integer.toString(bid.getDays()));
	request.addQuerystringParameter("description", bid.getDescription());
	request.addQuerystringParameter("projectid", Long.toString(bid.getProjectId()));
	request.addQuerystringParameter("notificationStatus", "1");
	request.addQuerystringParameter("highlightedCurrencyId", "1");
	service.signRequest(accessToken, request);
	request.addHeader("GData-Version", "3.0");
	Response response = request.send();
	mapper.convertConfirmation(response.getBody());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void retractBidFromProject(long projectId) throws BusinessException {
	String requestUrl = BASE_URL + FreelancerMethodCategoryEnum.Freelancer + "/" + "retractBidFromProject" + EXT;
	OAuthRequest request = new OAuthRequest(Verb.GET, requestUrl);
	request.addQuerystringParameter("projectid", Long.toString(projectId));
	service.signRequest(accessToken, request);
	request.addHeader("GData-Version", "3.0");
	Response response = request.send();
	mapper.convertConfirmation(response.getBody());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acceptBidWon(long projectId) throws BusinessException {
	String requestUrl = BASE_URL + FreelancerMethodCategoryEnum.Freelancer + "/" + "acceptBidWon" + EXT;
	OAuthRequest request = new OAuthRequest(Verb.GET, requestUrl);
	request.addQuerystringParameter("projectid", Long.toString(projectId));
	request.addQuerystringParameter("state", "1");
	service.signRequest(accessToken, request);
	request.addHeader("GData-Version", "3.0");
	Response response = request.send();
	mapper.convertConfirmation(response.getBody());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void declineBidWon(long projectId) throws BusinessException {
	String requestUrl = BASE_URL + FreelancerMethodCategoryEnum.Freelancer + "/" + "acceptBidWon" + EXT;
	OAuthRequest request = new OAuthRequest(Verb.GET, requestUrl);
	request.addQuerystringParameter("projectid", Long.toString(projectId));
	request.addQuerystringParameter("state", "0");
	service.signRequest(accessToken, request);
	request.addHeader("GData-Version", "3.0");
	Response response = request.send();
	mapper.convertConfirmation(response.getBody());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getUnreadMessageCount() throws BusinessException {
	String requestUrl = BASE_URL + FreelancerMethodCategoryEnum.Message + "/" + "getUnreadCount" + EXT;
	OAuthRequest request = new OAuthRequest(Verb.GET, requestUrl);
	service.signRequest(accessToken, request);
	request.addHeader("GData-Version", "3.0");
	Response response = request.send();
	return mapper.convertUnreadMessageCount(response.getBody());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Message> getSentMessages() throws BusinessException {
	String requestUrl = BASE_URL + FreelancerMethodCategoryEnum.Message + "/" + "getSentMessages" + EXT;
	OAuthRequest request = new OAuthRequest(Verb.GET, requestUrl);
	service.signRequest(accessToken, request);
	request.addHeader("GData-Version", "3.0");
	Response response = request.send();
	return mapper.convertSentMessagesToSystem(response.getBody());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void markMessageAsRead(long messageId) throws BusinessException {
	String requestUrl = BASE_URL + FreelancerMethodCategoryEnum.Message + "/" + "markMessageAsRead" + EXT;
	OAuthRequest request = new OAuthRequest(Verb.GET, requestUrl);
	request.addQuerystringParameter("id", Long.toString(messageId));
	service.signRequest(accessToken, request);
	request.addHeader("GData-Version", "3.0");
	Response response = request.send();
	mapper.convertConfirmation(response.getBody());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Job> getBiddedProjects() throws BusinessException {

	List<Job> finalBidList = getBiddProjectsByStatus(BiddedProjectStatusEnum.FORZEN_WAITING_UR_ACTION, JobStatusEnum.WON);
	// Specific situation where this occures is still unknown (No Documentation in the Freelancer API).
	// List<Job> pendingBids = getBiddProjectsByStatus(BiddedProjectStatusEnum.AWAITING_BUYER_ACTION, JobStatusEnum.PENDING);
	List<Job> openBids = getBiddProjectsByStatus(BiddedProjectStatusEnum.OPEN_AND_FROZEN, JobStatusEnum.OPEN);
	List<Job> activeWonBids = getBiddProjectsByStatus(BiddedProjectStatusEnum.CLOSED_WON, JobStatusEnum.ACTIVE);
	List<Job> closedProjects = getBiddProjectsByStatus(BiddedProjectStatusEnum.ALL, JobStatusEnum.CLOSED);

	// Removing from the Open and Frozen bids the ones that are awaiting user action.
	openBids.removeAll(finalBidList);
	// Adding the remaining to the final list.
	finalBidList.addAll(openBids);
	// Removing all the active and the content of finalBidList from all the bids.
	closedProjects.removeAll(activeWonBids);
	closedProjects.removeAll(finalBidList);
	// Adding the remaining bids to the final list.
	finalBidList.addAll(closedProjects);
	return finalBidList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Job> getWonBiddedProjects() throws BusinessException {
	return getBiddProjectsByStatus(BiddedProjectStatusEnum.CLOSED_WON, JobStatusEnum.ACTIVE);
    }

    private List<Job> getBiddProjectsByStatus(BiddedProjectStatusEnum status, JobStatusEnum newStatus) throws BusinessException {
	String requestUrl = BASE_URL + FreelancerMethodCategoryEnum.Freelancer + "/" + "getProjectListForPlacedBids" + EXT;
	OAuthRequest request = new OAuthRequest(Verb.GET, requestUrl);
	request.addQuerystringParameter("status", status.getStringValue());
	service.signRequest(accessToken, request);
	request.addHeader("GData-Version", "3.0");
	Response response = request.send();
	return mapper.convertBiddedProjectsToSystem(response.getBody(), newStatus);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProvider() {
	return "freelancer";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getUserIdFromProvider() throws BusinessException {
	String requestUrl = BASE_URL + FreelancerMethodCategoryEnum.Profile + "/" + "getAccountDetails" + EXT;
	OAuthRequest request = new OAuthRequest(Verb.GET, requestUrl);
	service.signRequest(accessToken, request);
	request.addHeader("GData-Version", "3.0");
	Response response = request.send();
	return mapper.convertAccountDetailsToUserId(response.getBody());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobStatusEnum getMyBidStatusForProject(long projectId) throws BusinessException {
	// TODO Auto-generated method stub
	return null;
    }

}

package gateway;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

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

import crud.DAO;
import exceptions.BusinessException;

public abstract class AbstractApiGateway {

    protected final static String EXT = ".json";
    private String apiSecret;
    private String apiKey;
    protected OAuthService service;
    protected Token accessToken;
    protected Token requestToken;
    protected DAO dao;
    protected long userId;

    public AbstractApiGateway() {
    }

    // Abstract Methods

    public abstract boolean initializeGatewayLocally() throws JsonParseException, JsonMappingException, IOException;

    public abstract String getVerificationURL();

    public abstract Map<String, String> getAccessTokenFromProvider(String verificationCode) throws BusinessException;

    public abstract List<Job> searchJobs(JobSearch jobSearchDto) throws BusinessException;

    public abstract List<Bid> getBidsForProject(long projectId) throws BusinessException;

    public abstract void getJobList();

    public abstract List<ProjectPublicMessage> getPublicMessagesForProject(long projectId) throws BusinessException;

    public abstract void postPublicMessagesOnProject(ProjectPostMessage message) throws BusinessException;

    public abstract List<Notification> getNotifications() throws BusinessException;

    public abstract Project getProjectDetails(long projectId) throws BusinessException;

    public abstract List<Message> getMessages(long projectId) throws BusinessException;

    public abstract void sendMessage(OutgoingMessage msg) throws BusinessException;

    public abstract void placeBid(BidRequest bid) throws BusinessException;

    public abstract void retractBidFromProject(long projectId) throws BusinessException;

    public abstract void acceptBidWon(long projectId, int state) throws BusinessException;

    public abstract int getUnreadMessageCount() throws BusinessException;

    public abstract List<Message> getSentMessages() throws BusinessException;

    public abstract void markMessageAsRead(long messageId) throws BusinessException;

    public abstract List<Job> getBiddedProjects() throws BusinessException;

    public abstract String getProvider();

    public abstract long getUserIdFromProvider() throws BusinessException;

    // Implemented Methods
    /**
     * Because the search of the classes in the plug-in occures through the Class.forName() method the constructor must be a
     * default constructor, hence it is required to use the init method after the initialization of the Gateway.
     * 
     * @param apiKey
     * @param apiSecret
     * @param provider
     */
    public void initGateway(String apiKey, String apiSecret) {
	this.apiKey = apiKey;
	this.apiSecret = apiSecret;
	this.dao = DAO.getInstance();
    }

    public String getApiSecret() {
	return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
	this.apiSecret = apiSecret;
    }

    public String getApiKey() {
	return apiKey;
    }

    public void setApiKey(String apiKey) {
	this.apiKey = apiKey;
    }

    public void setAccessToken(String token, String secret) {
	this.apiSecret = secret;
	this.accessToken = new Token(token, secret);
    }

    public long getUserId() {
	return this.userId;
    }

    public void setUserId(long userId) {
	this.userId = userId;
    }

    protected OAuthRequest createRequest(Verb v, String url, Map<String, String> parameters) {
	OAuthRequest request = new OAuthRequest(v, url);
	Iterator<Entry<String, String>> iter = parameters.entrySet().iterator();
	while (iter.hasNext()) {
	    Map.Entry<String, String> paramPair = (Map.Entry<String, String>) iter.next();
	    request.addQuerystringParameter(paramPair.getKey(), paramPair.getValue());
	}
	return request;
    }

}

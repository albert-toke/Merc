package gateway;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import exceptions.BusinessException;

public class ODeskGateway extends AbstractApiGateway {

    @Override
    public boolean initializeGatewayLocally() throws JsonParseException, JsonMappingException, IOException {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public String getVerificationURL() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Map<String, String> getAccessTokenFromProvider(String verificationCode) throws BusinessException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public List<Job> searchJobs(JobSearch jobSearchDto) throws BusinessException {
	// TODO Auto-generated method stub
	List<Job> jobList = new ArrayList<>();
	return jobList;
    }

    @Override
    public List<Bid> getBidsForProject(long projectId) throws BusinessException {
	// TODO Auto-generated method stub
	List<Bid> bidList = new ArrayList<>();
	return bidList;
    }

    @Override
    public void getJobList() {
	// TODO Auto-generated method stub

    }

    @Override
    public List<ProjectPublicMessage> getPublicMessagesForProject(long projectId) throws BusinessException {
	// TODO Auto-generated method stub
	List<ProjectPublicMessage> messageList = new ArrayList<>();
	return messageList;
    }

    @Override
    public void postPublicMessagesOnProject(ProjectPostMessage message) throws BusinessException {
	// TODO Auto-generated method stub

    }

    @Override
    public List<Notification> getNotifications() throws BusinessException {
	// TODO Auto-generated method stub
	List<Notification> notificationList = new ArrayList<>();
	return notificationList;
    }

    @Override
    public Project getProjectDetails(long projectId) throws BusinessException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public List<Message> getMessages(long projectId) throws BusinessException {
	// TODO Auto-generated method stub
	List<Message> messageList = new ArrayList<Message>();
	return messageList;
    }

    @Override
    public void sendMessage(OutgoingMessage msg) throws BusinessException {
	// TODO Auto-generated method stub

    }

    @Override
    public void placeBid(BidRequest bid) throws BusinessException {
	// TODO Auto-generated method stub

    }

    @Override
    public void retractBidFromProject(long projectId) throws BusinessException {
	// TODO Auto-generated method stub

    }

    @Override
    public void acceptBidWon(long projectId, int state) throws BusinessException {
	// TODO Auto-generated method stub

    }

    @Override
    public int getUnreadMessageCount() throws BusinessException {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public List<Message> getSentMessages() throws BusinessException {
	// TODO Auto-generated method stub
	List<Message> messageList = new ArrayList<Message>();
	return messageList;
    }

    @Override
    public void markMessageAsRead(long messageId) throws BusinessException {
	// TODO Auto-generated method stub

    }

    @Override
    public List<Job> getBiddedProjects() throws BusinessException {
	// TODO Auto-generated method stub
	List<Job> jobList = new ArrayList<Job>();
	return jobList;
    }

    @Override
    public String getProvider() {
	return "odesk";
    }

    @Override
    public long getUserIdFromProvider() throws BusinessException {
	// TODO Auto-generated method stub
	return 0;
    }

}

package mapper;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.wrappers.Attachement;
import common.wrappers.Bid;
import common.wrappers.Job;
import common.wrappers.JobSearch;
import common.wrappers.Message;
import common.wrappers.Notification;
import common.wrappers.Project;
import common.wrappers.ProjectPublicMessage;

import enums.DirectionEnum;
import enums.JobStatusEnum;
import exceptions.BusinessException;

public class FreelancerMapper {

    private static final String ROOT = "json-result";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String PROVIDER = "freelancer";

    private ObjectMapper jsonMapper;

    public FreelancerMapper() {
	jsonMapper = new ObjectMapper();
    }

    // If the JSON mesage is an error message then throws an exception.
    private void verifyErrorMessage(JsonNode root) throws BusinessException {
	JsonNode errors = root.get("errors");
	if (errors != null) {
	    JsonNode error = errors.get("error");
	    String longmsg = error.get("longmsg").asText();
	    int errorCode = error.get("code").asInt();
	    String msg = error.get("msg").asText();
	    throw new BusinessException(errorCode, msg, longmsg);
	}
    }

    public Map<String, String> convertProjectSearchDtoToString(JobSearch jobSearchDto) {
	HashMap<String, String> parameters = new HashMap<String, String>();
	parameters.put("searchkeyword", jobSearchDto.getSearchKeyword());
	parameters.put("count", Integer.toString(jobSearchDto.getCount()));
	parameters.put("searchjobtypecsv", jobSearchDto.getSearchJobTypeCSV());
	parameters.put("isfeatured", "false");
	parameters.put("isnonpublic", "false");
	return parameters;
    }

    /**
     * Converts the JSON response of the searchProject method to a POJO.
     * 
     * @param jsonString
     *            JSON String response.
     * @return A list of Job POJOs.
     * @throws BusinessException
     */
    public List<Job> convertProjectSearchResponseToSystem(String jsonString) throws BusinessException {
	System.out.println(jsonString);
	JsonNode messageNode;
	List<Job> jobResponeList;
	try {
	    messageNode = jsonMapper.readTree(jsonString).get(ROOT);
	    jobResponeList = convertFreelancerProjectToSystem(messageNode);
	} catch (JsonProcessingException e) {
	    throw new BusinessException(e);
	} catch (IOException e) {
	    throw new BusinessException(e);
	}
	return jobResponeList;
    }

    private List<Job> convertFreelancerProjectToSystem(JsonNode projectRespone) {
	ArrayList<Job> jobs = new ArrayList<Job>();
	for (JsonNode freelanceResp : projectRespone.path("items")) {
	    Job job = new Job();
	    job.setAverageBid(freelanceResp.get("averagebid").asDouble());
	    job.setBids(freelanceResp.get("bids").asInt());
	    job.setJobTypeCSV(freelanceResp.get("jobtypecsv").asText());
	    job.setProjectName(freelanceResp.get("projectname").asText());
	    job.setProjectId(freelanceResp.get("projectid").asLong());
	    job.setProjectURL(freelanceResp.get("projecturl").asText());
	    try {
		job.setStartDate(new SimpleDateFormat(DATE_FORMAT).parse(freelanceResp.get("startdate").asText()));
	    } catch (ParseException e) {
		job.setStartDate(null);
		e.printStackTrace();
	    }
	    job.setTimeLeft(freelanceResp.get("timeleft").asText());
	    job.setProvider(PROVIDER);
	    jobs.add(job);
	}
	return jobs;
    }

    /**
     * Converts the JSON response of the getBidsDetails method to a POJO.
     * 
     * @param jsonString
     *            JSON String response.
     * @return A list of Bid POJOs.
     * @throws BusinessException
     */
    public List<Bid> convertBidListToDTO(String jsonString) throws BusinessException {

	List<Bid> bidList = null;
	try {
	    JsonNode messageNode = jsonMapper.readTree(jsonString).get(ROOT);
	    verifyErrorMessage(messageNode);
	    System.out.println(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(messageNode));
	    bidList = convertFreelancerBidDetailsToSystem(messageNode);
	} catch (JsonProcessingException e) {
	    throw new BusinessException(e);
	} catch (IOException e) {
	    throw new BusinessException(e);
	}
	return bidList;
    }

    private List<Bid> convertFreelancerBidDetailsToSystem(JsonNode root) {
	List<Bid> bidList = new ArrayList<Bid>();
	for (JsonNode freelanceBid : root.path("items")) {
	    Bid bid = new Bid();
	    bid.setBidAmount(freelanceBid.get("bid_amount").asDouble());
	    bid.setDescription(freelanceBid.get("descr").asText());
	    bid.setMilestone(freelanceBid.get("milestone").asText());
	    bid.setProvider(freelanceBid.get("provider").asText());
	    bid.setProviderUserId(freelanceBid.get("provider_userid").asLong());
	    bid.setRating(freelanceBid.get("rating").asText());
	    bid.setSubmitTime(freelanceBid.get("submittime").asText());
	    bid.setDuration(freelanceBid.get("period").asInt());
	    bidList.add(bid);

	}
	return bidList;
    }

    /**
     * Converts the JSON response of the getPublicMessages method to a POJO.
     * 
     * @param jsonString
     *            JSON String response.
     * @return A list of Project Public message POJOs.
     * @throws BusinessException
     */
    public List<ProjectPublicMessage> convertProjectPublicMessagesToSystem(String jsonString) throws BusinessException {
	JsonNode root;
	List<ProjectPublicMessage> messageList = null;
	try {
	    root = jsonMapper.readTree(jsonString);
	    System.out.println(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root));
	    verifyErrorMessage(root);
	    messageList = convertFreelancerProjectPublicMessageToSystem(root.get(ROOT));
	} catch (JsonProcessingException e) {
	    throw new BusinessException(e);
	} catch (IOException e) {
	    throw new BusinessException(e);
	}
	return messageList;
    }

    private List<ProjectPublicMessage> convertFreelancerProjectPublicMessageToSystem(JsonNode rootNode) {
	List<ProjectPublicMessage> messages = new ArrayList<ProjectPublicMessage>();
	for (JsonNode freelancerMsg : rootNode.path("items")) {
	    ProjectPublicMessage msg = new ProjectPublicMessage();
	    msg.setUserName(freelancerMsg.get("fromusername").asText());
	    msg.setUserId(freelancerMsg.get("fromuserid").asLong());
	    msg.setText(freelancerMsg.get("text").asText());
	    msg.setDate(convertToDate(freelancerMsg.get("datetime").asText()));
	    msg.setAttachmentUrl(freelancerMsg.get("attachementlink") != null ? freelancerMsg.get("attachementlink").asText()
		    : "");
	    messages.add(msg);
	}
	return messages;
    }

    /**
     * Verifies if the given message contains an error and throws then a BusinessException.
     * 
     * @param jsonString
     *            JSON String response.
     * @throws Exception
     */
    public void convertConfirmation(String jsonString) throws BusinessException {
	JsonNode node = null;
	try {
	    node = jsonMapper.readTree(jsonString);
	    System.out.println(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
	} catch (JsonGenerationException e) {
	    throw new BusinessException(e);
	} catch (JsonMappingException e) {
	    throw new BusinessException(e);
	} catch (IOException e) {
	    throw new BusinessException(e);
	}
	verifyErrorMessage(node);
    }

    /**
     * Converts the JSON response of the getNotification method to a POJO.
     * 
     * @param jsonString
     *            JSON String response.
     * @return A list of Notification POJOs.
     * @throws BusinessException
     */
    public List<Notification> convertNotificationsToSystem(String jsonString) throws BusinessException {
	List<Notification> notifications = null;
	JsonNode messageNode;
	try {
	    messageNode = jsonMapper.readTree(jsonString);
	    System.out.println(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(messageNode));
	    messageNode = messageNode.get(ROOT);
	    notifications = new ArrayList<Notification>();
	    for (JsonNode node : messageNode.path("itmes")) {
		Notification notification = new Notification();
		notification.setContext(node.get("notificationtext").asText());
		notification.setProjectId(node.get("projectid") != null ? node.get("projectid").asLong() : 0);
		notification.setUrl(node.get("url").asText());
		notification.setProvider(PROVIDER);
		notifications.add(notification);
	    }
	} catch (JsonProcessingException e) {
	    throw new BusinessException(e);
	} catch (IOException e) {
	    throw new BusinessException(e);
	}
	return notifications;
    }

    /**
     * Converts the JSON response of the getProjectDetails method to a POJO.
     * 
     * @param jsonString
     *            JSON String response.
     * @return A Project Object.
     * @throws BusinessException
     */
    public Project convertProjectDetailsJsonToSystem(String jsonString) throws BusinessException {
	Project project = new Project();
	try {
	    JsonNode root = jsonMapper.readTree(jsonString).get(ROOT);
	    System.out.println(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root));
	    project.setId(root.get("id").asLong());
	    project.setName(root.get("name").asText());
	    project.setUrl(root.get("url").asText());
	    project.setStartDate(convertToDate(root.get("start_date").asText()));
	    project.setEndDate(convertToDate(root.get("end_date").asText()));
	    JsonNode buyer = root.get("buyer");
	    project.setBuyerId(buyer.get("id").asLong());
	    project.setBuyerUserName(buyer.get("username").asText());
	    project.setBuyerUrl(buyer.get("url").asText());
	    project.setStatus(convertProjectState(root.get("state").asText()));
	    project.setShortDescription(root.get("short_descr").asText());
	    project.setAdditionalOptions(convertAdditionalOptions(root.get("options")));
	    project.setMinBudget(root.get("budget").get("min").asDouble());
	    project.setMaxBudget((root.get("budget").get("max").asDouble()));
	    project.setJobCategory(convertJobCategories(root));
	    project.setBidCount(root.get("bid_stats").get("count").asLong());
	    project.setAverageBidVal(root.get("bid_stats").get("avg").asDouble());

	    if (root.get("seller") != null && root.get("seller").get(0) != null) {
		JsonNode seller = root.get("seller").get(0);
		project.setAssignedFreelancerId(seller.get("id").asLong());
		project.setAssignedFreelancerName(seller.get("username").asText());
		project.setAssignedFreelancerStatus(seller.get("awardStatus").asText());
	    }
	    project.setAttachements(convertAttachements(root));
	} catch (JsonProcessingException e) {
	    throw new BusinessException(e);
	} catch (IOException e) {
	    throw new BusinessException(e);
	}
	return project;
    }

    private Date convertToDate(String source) {
	Date tempDate;
	try {
	    tempDate = new SimpleDateFormat(DATE_FORMAT).parse(source);
	} catch (ParseException e) {
	    tempDate = null;
	    e.printStackTrace();
	}
	return tempDate;
    }

    private Map<String, String> convertAdditionalOptions(JsonNode options) {
	Map<String, String> map = new HashMap<String, String>();
	map.put("featured", options.get("featured").asText());
	map.put("nonpublic", options.get("nonpublic").asText());
	map.put("trial", options.get("trial").asText());
	map.put("fulltime", options.get("fulltime").asText());
	map.put("for_gold_memebers", options.get("for_gold_members").asText());
	map.put("hidden_bids", options.get("hidden_bids").asText());
	return map;
    }

    private JobStatusEnum convertProjectState(String state) {
	JobStatusEnum fullState;
	switch (state) {
	case "A":
	    fullState = JobStatusEnum.OPEN;
	    break;
	case "F":
	    fullState = JobStatusEnum.FROZEN;
	    break;
	case "C":
	    fullState = JobStatusEnum.CLOSED;
	    break;
	case "P":
	    fullState = JobStatusEnum.PENDING;
	    break;
	// There are two remaining states but those cant be reached in the application
	default:
	    fullState = null;
	}
	return fullState;
    }

    private List<Attachement> convertAttachements(JsonNode root) {
	List<Attachement> attachements = new ArrayList<Attachement>();
	for (JsonNode node : root.path("additional_files")) {
	    Attachement attachement = new Attachement();
	    attachement.setName(node.get("name").asText());
	    attachement.setUrl(node.get("full_file_url").asText());
	    attachement.setSubmitDate(convertToDate(node.get("submitdate").asText()));
	    attachement.setMessageId(node.get("id").asLong());
	    attachements.add(attachement);
	}
	return attachements;
    }

    private List<String> convertJobCategories(JsonNode root) {
	List<String> jobs = new ArrayList<String>();
	for (JsonNode node : root.path("jobs")) {
	    jobs.add(node.asText());
	}
	return jobs;
    }

    /**
     * Converts the JSON response of the getInboxMessages method to a POJO.
     * 
     * @param jsonString
     *            JSON String response.
     * @return A list of IncomingMessage POJOs.
     * @throws BusinessException
     */
    public List<Message> convertIncomingMessagesToSystem(String jsonString) throws BusinessException {
	List<Message> messages = null;
	try {
	    JsonNode messageNode = jsonMapper.readTree(jsonString);
	    System.out.println(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(messageNode));
	    verifyErrorMessage(messageNode);
	    JsonNode root = messageNode.get(ROOT);
	    messages = new ArrayList<Message>();
	    for (JsonNode node : root.path("items")) {
		Message msg = new Message();
		msg.setProjectId(node.get("projectid").asLong());
		msg.setUsername(node.get("fromusername").asText());
		msg.setUserId(node.get("fromuserid").asLong());
		msg.setDate(convertToDate(node.get("datetime").asText()));
		msg.setText(node.get("text").asText());
		msg.setDirection(DirectionEnum.INCOMING);
		msg.setProvider("freelancer");
		for (JsonNode subNode : node.path("attachmentlink")) {
		    msg.addAttachment(subNode.asText());
		}
		messages.add(msg);
	    }
	} catch (JsonProcessingException e) {
	    throw new BusinessException(e);
	} catch (IOException e) {
	    throw new BusinessException(e);
	}
	return messages;
    }

    /**
     * Converts the JSON response of the getUnreadCount method to a POJO.
     * 
     * @param jsonString
     *            JSON String response.
     * @return Returns the count of unread Messages.
     * @throws BusinessException
     */
    public int convertUnreadMessageCount(String jsonString) throws BusinessException {
	int count = 0;
	try {
	    JsonNode messageNode = jsonMapper.readTree(jsonString);
	    verifyErrorMessage(messageNode);
	    JsonNode root = messageNode.get(ROOT);
	    count = root.get("unreadcount").asInt();
	} catch (JsonProcessingException e) {
	    throw new BusinessException(e);
	} catch (IOException e) {
	    throw new BusinessException(e);
	}
	return count;
    }

    /**
     * Converts the JSON response of the getSentMessages method to a POJO.
     * 
     * @param jsonString
     *            JSON String response.
     * @return Returns a list of Sent Message POJOs.
     * @throws BusinessException
     */
    public List<Message> convertSentMessagesToSystem(String jsonString) throws BusinessException {
	List<Message> messages = new ArrayList<Message>();
	try {
	    JsonNode root = jsonMapper.readTree(jsonString).get("json-result").get("items");
	    System.out.println(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root));
	    for (JsonNode node : root.path("message")) {
		Message msg = new Message();
		msg.setProvider(PROVIDER);
		msg.setProjectId(node.get("projectid").asLong());
		msg.setUsername(node.get("tousername").asText());
		msg.setUserId(node.get("touserid").asLong());
		msg.setDate(convertToDate(node.get("datetime").asText()));
		msg.setText(node.get("text").asText());
		msg.setDirection(DirectionEnum.OUTGOING);
		for (JsonNode subNode : node.path("attachmentlink")) {
		    msg.addAttachment(subNode.asText());
		}
		messages.add(msg);
	    }
	} catch (JsonProcessingException e) {
	    throw new BusinessException(e);
	} catch (IOException e) {
	    throw new BusinessException(e);
	}

	return messages;
    }

    /**
     * Converts the JSON response of the getProjectListForPlacedBids method to a POJO.
     * 
     * @param jsonString
     *            JSON String response.
     * @return A list of Project POJOs on which there was placed a bid.
     * @throws Exception
     */
    public List<Job> convertBiddedProjectsToSystem(String jsonString, JobStatusEnum status) throws BusinessException {
	List<Job> projects = null;
	try {
	    JsonNode root = jsonMapper.readTree(jsonString).get("json-result");
	    System.out.println(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root));
	    projects = new ArrayList<Job>();
	    for (JsonNode node : root.path("items")) {
		Job job = new Job();
		// project.setAdditionalStatus(node.get("additionalstatus").asText());
		// project.setEndDate(convertToDate(node.get("enddate").asText()));
		// project.setOwnerUserId(node.get("owneruserid").asLong());
		// project.setOwnerUserName(node.get("ownerusername").asText());
		job.setProjectId(node.get("projectid").asLong());
		job.setProjectName(node.get("projectname").asText());
		job.setProjectURL(node.get("projecturl").asText());
		job.setProvider(PROVIDER);
		if (status == JobStatusEnum.OPEN) {
		    if (node.get("status").asText().equals("F")) {
			job.setStatus(JobStatusEnum.FROZEN);
		    } else {
			job.setStatus(JobStatusEnum.OPEN);
		    }
		} else {
		    job.setStatus(status);
		}
		projects.add(job);
	    }
	} catch (JsonProcessingException e) {
	    throw new BusinessException(e);
	} catch (IOException e) {
	    throw new BusinessException(e);
	}
	return projects;
    }

    public JobStatusEnum convertBiddedProjectStatusToSystem(BiddedProjectStatusEnum reqStatus, String respStatus) {
	JobStatusEnum jobStatus;
	switch (reqStatus) {
	case FORZEN_WAITING_UR_ACTION:
	    jobStatus = JobStatusEnum.WON;
	    break;
	case CLOSED_CANCELED:
	    jobStatus = JobStatusEnum.CLOSED;
	    break;
	case CLOSED_WON:
	    jobStatus = JobStatusEnum.ACTIVE;
	    break;
	case CLOSED_LOST:
	    jobStatus = JobStatusEnum.CLOSED_LOST;
	    break;
	case OPEN_AND_FROZEN:
	    if (respStatus.equals("Open")) {
		jobStatus = JobStatusEnum.OPEN;
	    } else {
		jobStatus = JobStatusEnum.FROZEN;
	    }
	default:
	    jobStatus = null;
	    break;

	}
	return jobStatus;
    }

    public long convertAccountDetailsToUserId(String jsonString) throws BusinessException {
	long userId = 0;
	try {
	    JsonNode root = jsonMapper.readTree(jsonString).get("json-result");
	    userId = root.get("userid").asLong();
	} catch (JsonProcessingException e) {
	    throw new BusinessException(e);
	} catch (IOException e) {
	    throw new BusinessException(e);
	}
	return userId;
    }

}

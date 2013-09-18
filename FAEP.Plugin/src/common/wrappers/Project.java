package common.wrappers;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Contains the details of the project.
 * 
 * @author Jindster
 *
 */
public class Project {

	private String provider;
	private long id;
	private String name;
	private String url;
	private Date startDate;
	private Date endDate;
	private String buyerUrl;
	private long buyerId;
	private String buyerUserName;
	private String state;
	private String shortDescription;
	private double minBudget;
	private double maxBudget;
	private List<String> jobCategory;
	private long bidCount;
	private double averageBidVal;
	private Map<String, String> additionalOptions; // isfeaturead, nonpublic,
													// trial, fulltime etc.
	private List<Attachement> attachements;
	private long assignedFreelancerId;
	private String assignedFreelancerName;
	private String assignedFreelancerStatus;

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getBuyerUrl() {
		return buyerUrl;
	}

	public void setBuyerUrl(String buyerUrl) {
		this.buyerUrl = buyerUrl;
	}

	public long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(long buyerId) {
		this.buyerId = buyerId;
	}

	public String getBuyerUserName() {
		return buyerUserName;
	}

	public void setBuyerUserName(String buyerUserName) {
		this.buyerUserName = buyerUserName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public double getMinBudget() {
		return minBudget;
	}

	public void setMinBudget(double minBudget) {
		this.minBudget = minBudget;
	}

	public double getMaxBudget() {
		return maxBudget;
	}

	public void setMaxBudget(double maxBudget) {
		this.maxBudget = maxBudget;
	}

	public long getBidCount() {
		return bidCount;
	}

	public void setBidCount(long bidCount) {
		this.bidCount = bidCount;
	}

	public double getAverageBidVal() {
		return averageBidVal;
	}

	public void setAverageBidVal(double averageBidVal) {
		this.averageBidVal = averageBidVal;
	}

	public Map<String, String> getAdditionalOptions() {
		return additionalOptions;
	}

	public void setAdditionalOptions(Map<String, String> additionalOptions) {
		this.additionalOptions = additionalOptions;
	}

	public List<Attachement> getAttachements() {
		return attachements;
	}

	public void setAttachements(List<Attachement> attachements) {
		this.attachements = attachements;
	}

	public long getAssignedFreelancerId() {
		return assignedFreelancerId;
	}

	public void setAssignedFreelancerId(long assignedFreelancerId) {
		this.assignedFreelancerId = assignedFreelancerId;
	}

	public String getAssignedFreelancerName() {
		return assignedFreelancerName;
	}

	public void setAssignedFreelancerName(String assignedFreelancerName) {
		this.assignedFreelancerName = assignedFreelancerName;
	}

	public String getAssignedFreelancerStatus() {
		return assignedFreelancerStatus;
	}

	public void setAssignedFreelancerStatus(String assignedFreelancerStatus) {
		this.assignedFreelancerStatus = assignedFreelancerStatus;
	}

	public List<String> getJobCategory() {
		return jobCategory;
	}

	public void setJobCategory(List<String> jobCategory) {
		this.jobCategory = jobCategory;
	}

	public String getJobCategoryAsString(String delimiter){
		StringBuilder sb = new StringBuilder();
		if(jobCategory != null && !jobCategory.isEmpty()){
			Iterator<String> iter = jobCategory.iterator();
			while(iter.hasNext()){
				String job = iter.next();
				sb.append(job);
				if(iter.hasNext()){
					sb.append(delimiter);
				}
			}
		}
		return sb.toString();
	}
	
}

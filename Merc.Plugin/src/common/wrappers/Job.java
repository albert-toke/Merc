package common.wrappers;

import java.util.Date;

import constants.and.enums.JobStatusEnum;

public class Job {

    private long projectId;
    private String projectName;
    private int bids;
    private double averageBid;
    private String jobTypeCSV;
    private Date startDate;
    private String timeLeft;
    private String projectURL;
    private String provider;
    private JobStatusEnum status;

    public long getProjectId() {
	return projectId;
    }

    public void setProjectId(long projectId) {
	this.projectId = projectId;
    }

    public String getProjectName() {
	return projectName;
    }

    public void setProjectName(String projectName) {
	this.projectName = projectName;
    }

    public int getBids() {
	return bids;
    }

    public void setBids(int bids) {
	this.bids = bids;
    }

    public double getAverageBid() {
	return averageBid;
    }

    public void setAverageBid(double averageBid) {
	this.averageBid = averageBid;
    }

    public String getJobTypeCSV() {
	return jobTypeCSV;
    }

    public void setJobTypeCSV(String jobTypeCSV) {
	this.jobTypeCSV = jobTypeCSV;
    }

    public Date getStartDate() {
	return startDate;
    }

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    public String getTimeLeft() {
	return timeLeft;
    }

    public void setTimeLeft(String timeLeft) {
	this.timeLeft = timeLeft;
    }

    public String getProjectURL() {
	return projectURL;
    }

    public void setProjectURL(String projectURL) {
	this.projectURL = projectURL;
    }

    public String getProvider() {
	return provider;
    }

    public void setProvider(String provider) {
	this.provider = provider;
    }

    public JobStatusEnum getStatus() {
	return this.status;
    }

    public void setStatus(JobStatusEnum status) {
	this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof Job) {
	    Job job = (Job) obj;
	    if (job.getProjectId() == this.projectId && job.getProvider().equalsIgnoreCase(this.provider)) {
		return true;
	    }
	}
	return false;
    }
}

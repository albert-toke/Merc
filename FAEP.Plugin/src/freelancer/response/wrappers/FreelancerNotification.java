package freelancer.response.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FreelancerNotification {

	@JsonProperty("notificationtext ")
	private String context;
	@JsonProperty("projectid  ")
	private long projectId;
	private String url;

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}

package common.wrappers;

public class ProjectPostMessage {

	private String type;
	private long projectId;
	private String message;
	private String provider;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

}

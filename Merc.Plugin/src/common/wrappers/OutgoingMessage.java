package common.wrappers;

public class OutgoingMessage {

	private long projectId;
	private String messageText;
	private String username;
	private String provider;

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String getUrsername() {
		return username;
	}

	public void setUrsername(String ursername) {
		this.username = ursername;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

}

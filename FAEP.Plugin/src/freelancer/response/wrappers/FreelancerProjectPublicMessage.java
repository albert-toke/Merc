package freelancer.response.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FreelancerProjectPublicMessage {

	@JsonProperty("fromusername")
	private String userName;
	@JsonProperty("fromuserid")
	private long userId;
	@JsonProperty("datetime")
	private String date;
	private String text;
	@JsonProperty("attachmentlink")
	private String attachementUrl;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAttachementUrl() {
		return attachementUrl;
	}

	public void setAttachementUrl(String attachementUrl) {
		this.attachementUrl = attachementUrl;
	}

}

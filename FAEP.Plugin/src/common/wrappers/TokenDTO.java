package common.wrappers;


public class TokenDTO {

	private String provider;
	private String token;
	private String secret;
	private String rawResponse;
	
	public TokenDTO(){
	}
	
	public TokenDTO(String provider, String token, String secret, String rawResponse) {
		super();
		this.provider = provider;
		this.token = token;
		this.secret = secret;
		this.rawResponse = rawResponse;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getRawResponse() {
		return rawResponse;
	}

	public void setRawResponse(String rawResponse) {
		this.rawResponse = rawResponse;
	}
	
	
	
}

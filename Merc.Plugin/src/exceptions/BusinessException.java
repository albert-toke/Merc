package exceptions;

public class BusinessException extends Exception {

	private static final long serialVersionUID = 1L;

	private int errorCode;
	private String longMessage;

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Exception e) {
		super(e);
	}

	public BusinessException(int errorCode, String message, String longMessage) {
		super(message);
		this.errorCode = errorCode;
		this.longMessage = longMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getLongMessage() {
		return longMessage;
	}

}

package constants.and.enums;

public enum JobStatusEnum {

    OPEN("Open"), //
    CLOSED("Closed"), //
    PENDING("Pending"), //
    WON("Won"), //
    ACTIVE("Active"), //
    CLOSED_LOST("Lost"), //
    FROZEN("Frozen");

    private String type;

    private JobStatusEnum(String s) {
	this.type = s;
    }

    public String getStringValue() {
	return type;
    }

}

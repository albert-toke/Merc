package merc.gui.enums;

public enum ActionButtonOptionsEnum {

    ACCEPT("Accept Project"), //
    DECLINE("Decline Project"), //
    WITHDRAW("Withdraw Bid"), //
    POST_MESSAGE("Post Message"), //
    PLACE_BID("Place Bid"), //
    REQUEST_CANCEL("Request Cancel Project");

    private String type;

    private ActionButtonOptionsEnum(String type) {
	this.type = type;
    }

    public String getStringValue() {
	return type;
    }

    public static ActionButtonOptionsEnum getEnumByValue(String value) {
	for (ActionButtonOptionsEnum en : ActionButtonOptionsEnum.values()) {
	    if (en.getStringValue().equals(value)) {
		return en;
	    }
	}
	return null;
    }
}

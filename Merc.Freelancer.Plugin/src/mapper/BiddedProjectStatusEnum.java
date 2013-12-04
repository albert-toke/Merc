package mapper;

public enum BiddedProjectStatusEnum {

    ALL("1"), //
    OPEN_AND_FROZEN("2"), //
    FORZEN_WAITING_UR_ACTION("3"), //
    AWAITING_BUYER_ACTION("4"), //
    CLOSED_WON("5"), //
    CLOSED_LOST("6"), //
    CLOSED_CANCELED("7");

    private String type;

    private BiddedProjectStatusEnum(String s) {
	this.type = s;
    }

    public String getStringValue() {
	return type;
    }
}

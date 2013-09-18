package faep.gui.enums;

public enum SearchOptionsEnum {
    BY_TYPE("by Job Type"), BY_KEYWORD("by Keyword"), PROJECTS_BID_ON("Projects bid on"), PROJECTS_WORKING_ON("Active projects");

    private String type;
    private static final int ENTRY_COUNT = 4;

    private SearchOptionsEnum(String s) {
	this.type = s;
    }

    public String getStringValue() {
	return type;
    }

    public static String[] getAllStringValues() {
	String[] values = new String[ENTRY_COUNT];
	values[0] = BY_TYPE.getStringValue();
	values[1] = BY_KEYWORD.getStringValue();
	values[2] = PROJECTS_BID_ON.getStringValue();
	values[3] = PROJECTS_WORKING_ON.getStringValue();
	return values;
    }
}

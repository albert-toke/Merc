package constants.and.enums;

public enum DirectionEnum {
	OUTGOING("To"),INCOMING("From");
	
	private String direction;
	
	private DirectionEnum(String s){
		this.direction = s;
	}
	
	public String getStringValue(){
		return direction;
	}
}

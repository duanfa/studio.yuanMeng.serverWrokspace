package stdio.kiteDream.module.message.bean;

public enum MessageType {

	NOTICE("notice", 1), CHA_CHING("cha-ching", 2), BROADCAST("broadcast", 3);
	private String name;
	private int value;
	MessageType(String name, int value){
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	public int getValue() {
		return value;
	}
}

package JSONformating.model;

public class JStringValue extends JValue {
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString(){
		return value;
	}
	
	public String export(){
		return "\"" + value + "\"";
	}
}

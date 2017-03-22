package JSONformating.model;

public class JStringValue extends JValue {
	private String strValue;

	public String getValue() {
		return strValue;
	}

	public void setValue(String value) {
		this.strValue = value;
	}
	
	public String toString(){
		return strValue;
	}
	
	public String export(){
		return "\"" + strValue + "\"";
	}
}

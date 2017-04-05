package JSONformating.model;

import org.json.JSONObject;

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
		return strValue;
	}
	
	public boolean sameValue(JValue value){
		return value instanceof JStringValue;// && this.strValue.equals(((JStringValue) value).getValue());
//		return true;
	}
}

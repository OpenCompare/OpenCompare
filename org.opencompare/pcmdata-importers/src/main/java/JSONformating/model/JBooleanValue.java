package JSONformating.model;

public class JBooleanValue extends JValue {
	private Boolean boolValue;

	public Boolean getValue() {
		return boolValue;
	}

	public void setValue(Boolean value) {
		this.boolValue = value;
	}
	
	public String toString(){
		return String.valueOf(boolValue);
	}
	
	public String export(){
		return String.valueOf(boolValue);
	}
	
	public boolean sameValue(JValue value){
		return value instanceof JBooleanValue;
	}
	
	public boolean exactValue(JValue value) {
		return value instanceof JBooleanValue && this.boolValue.equals(((JBooleanValue) value).getValue());
	}
}

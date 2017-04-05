package JSONformating.model;

public class JBooleanValue extends JValue {
	private boolean boolValue;

	public Boolean getValue() {
		return boolValue;
	}

	public void setValue(boolean value) {
		this.boolValue = value;
	}
	
	public String toString(){
		return String.valueOf(boolValue);
	}
	
	public String export(){
		return String.valueOf(boolValue);
	}
	
	public boolean sameValue(JValue value){
		return value instanceof JBooleanValue && this.boolValue == ((JBooleanValue) value).getValue();
//		return true;
	}
}

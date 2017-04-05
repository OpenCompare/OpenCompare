package JSONformating.model;

public class JBooleanValue extends JValue {
	private boolean boolValue;

	public boolean isValue() {
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
}

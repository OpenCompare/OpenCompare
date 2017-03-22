package JSONformating.model;

public class JBooleanValue extends JValue {
	private boolean value;

	public boolean isValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}
	
	public String toString(){
		return String.valueOf(value);
	}
	
	public String export(){
		return String.valueOf(value);
	}
}

package JSONformating.model;

public class JNumberValue extends JValue{
	private float numValue;

	public Float getValue() {
		return numValue;
	}

	public void setValue(float value) {
		this.numValue = value;
	}
	
	public String toString(){
		return String.valueOf(numValue);
	}
	
	public String export(){
		return String.valueOf(numValue);
	}
}

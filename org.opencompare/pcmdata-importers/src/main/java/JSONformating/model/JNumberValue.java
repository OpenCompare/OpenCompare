package JSONformating.model;

public class JNumberValue extends JValue{
	private float value;

	public Float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}
	
	public String toString(){
		return String.valueOf(value);
	}
	
	public String export(){
		return String.valueOf(value);
	}
}

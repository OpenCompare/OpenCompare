package JSONformating.model;

public class JNumberValue extends JValue{
	private Double numValue;

	public Double getValue() {
		return numValue;
	}
	
	public Integer getAsInteger(){
		return numValue.intValue();
	}

	public void setValue(Double value) {
		this.numValue = value;
	}
	
	public String toString(){
		return String.valueOf(numValue);
	}
	
	public Double export(){
		return numValue;
	}
	
	public boolean sameValue(JValue value){
		return value instanceof JNumberValue;
	}
	
	public boolean exactValue(JValue value) {
		return value instanceof JNumberValue && this.numValue.equals(((JNumberValue) value).getValue());
	}
	
	
}

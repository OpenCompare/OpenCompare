package JSONformating.model;

public class JNumberValue extends JValue{
	private float numValue;

	public Float getValue() {
		return numValue;
	}
	
	public Integer getAsInteger(){
		return ((Float) numValue).intValue();
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
	
	public boolean sameValue(JValue value){
//		if(value instanceof JNumberValue && this.numValue != ((JNumberValue) value).getValue()){
//			System.out.println(numValue + " " + ((JNumberValue) value).getValue());
//		}
		return value instanceof JNumberValue;// && this.numValue == ((JNumberValue) value).getValue();
//		return true;
	}
}

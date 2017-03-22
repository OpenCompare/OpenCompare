package JSONformating.model;

import java.util.ArrayList;
import java.util.List;

public class JMultipleValue extends JValue{

	private List<JValue> mulValue = new ArrayList<>();

	public List<JValue> getValue() {
		return mulValue;
	}

	public void setValue(List<JValue> value) {
		this.mulValue = value;
	}

	public String toString(){
		String res = ""+ mulValue.size();
		for(JValue val : mulValue){
			res += ", " + val.toString();
		}
		return res;
	}
	
	public String export(){
		String res = "[";
		for(JValue val : mulValue){
			res += val.export() + ",";
		}
		res = mulValue.isEmpty() ? res : res.substring(0, res.length() - 1);
		res += "]";
		return res;
	}
}

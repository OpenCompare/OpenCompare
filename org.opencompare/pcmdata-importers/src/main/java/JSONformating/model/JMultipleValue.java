package JSONformating.model;

import java.util.ArrayList;
import java.util.List;

public class JMultipleValue extends JValue{

	private List<JValue> value = new ArrayList<>();

	public List<JValue> getValue() {
		return value;
	}

	public void setValue(List<JValue> value) {
		this.value = value;
	}

	public String toString(){
		String res = ""+ value.size();
		for(JValue val : value){
			res += ", " + val.toString();
		}
		return res;
	}
	
	public String export(){
		String res = "[";
		for(JValue val : value){
			res += val.export() + ",";
		}
		res = value.isEmpty() ? res : res.substring(0, res.length() - 1);
		res += "]";
		return res;
	}
}

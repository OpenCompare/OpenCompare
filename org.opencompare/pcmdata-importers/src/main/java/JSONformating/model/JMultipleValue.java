package JSONformating.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonObject;

public class JMultipleValue extends JValue{

	private List<JValue> mulValue = new ArrayList<>();

	public List<JValue> getValue() {
		return mulValue;
	}

	public void setValue(List<JValue> value) throws IOException{
		for(JValue v : value){
			addValue(v);
		}
	}
	

	public void addValue(JValue value) throws IOException{
		if(value instanceof JMultipleValue){
			throw new IOException("Error adding a multiple value in another multiple value");
		}
		mulValue.add(value);
		
	}

	public String toString(){
		String res = ""+ mulValue.size();
		for(JValue val : mulValue){
			res += ", " + val.toString();
		}
		return res;
	}
	
	public JSONArray export(){
		JSONArray array = new JSONArray();
		for (JValue jv : mulValue) {
			array.put(jv.export());
		}
		//JSONArray array = new JSONArray(mulValue);
		return array;
	}
	
	public boolean sameValue(JValue value){
//		if(! (value instanceof JMultipleValue) || (mulValue.size() != ((JMultipleValue) value).getValue().size())){
//			return false;
//		}
//		List<JValue> tempValues = new ArrayList<>(mulValue);
//		for(JValue jval : ((JMultipleValue) value).getValue()){
//			for(JValue thisval : mulValue){
//				if(thisval.sameValue(jval)){
//					if(!tempValues.remove(thisval)){
//						return false;
//					}
//				}
//			}
//		}
//		return tempValues.isEmpty();
		return value instanceof JMultipleValue;
//		return true;
	}
}

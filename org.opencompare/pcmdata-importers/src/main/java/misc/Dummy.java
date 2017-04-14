package misc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.opencompare.api.java.PCMContainer;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import JSONformating.model.JBooleanValue;
import JSONformating.model.JCell;
import JSONformating.model.JMultipleValue;
import JSONformating.model.JNumberValue;
import JSONformating.model.JProduct;
import JSONformating.model.JStringValue;
import JSONformating.model.JValue;
import JSONformating.reader.JSONReader;
import JSONformating.reader.JSONtoPCM;
import JSONformating.model.JSONFormat;
import JSONformating.model.JSONFormatType;

public class Dummy {

	public static void main(String[] args) throws IOException {
//		JCell cell = new JCell();
//		cell.setFeatureID("F0");
//		cell.setId("C0");
//		cell.setProductID("P0");
//		cell.setType(JSONFormatType.MULTIPLE);
//		JStringValue val = new JStringValue();
//		val.setValue("test");
//		JBooleanValue bool = new JBooleanValue();
//		bool.setValue(true);
//		JNumberValue num = new JNumberValue();
//		num.setValue(42.42F);
//		JMultipleValue mulval = new JMultipleValue();
//		mulval.addValue(val);
//		mulval.addValue(bool);
//		mulval.addValue(num);
//		cell.setValue(mulval);
//		JProduct prod = new JProduct();
//		prod.setId("P0");
//		prod.addCell(cell);
//		JSONFormat nf = new JSONFormat();
//		nf.addProduct(prod);
//		System.out.println(nf.export());
		
		PCMContainer pcmC = JSONtoPCM.JSONFormatToPCM(JSONReader.importJSON("off_output/pcms/en_french-blue-veined-cheeses.new.pcm"));
	}
}

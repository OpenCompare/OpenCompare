package JSONformating;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.opencompare.api.java.PCMContainer;

import JSONformating.model.JSONFormat;
import JSONformating.reader.JSONReader;
import JSONformating.reader.JSONtoPCM;
import data_off.PCMUtil;

public class JSONmetamorphTest {
	
	PCMContainer pcmC;
	JSONFormat jf;

	@Test
	public void test() {
		assert(true);
		//fail("Not yet implemented");
	}

	public void importOldFormat(String filename) throws IOException{
		pcmC = PCMUtil.loadPCMContainer(filename);
		jf = PCMtoJSON.mkNewJSONFormatFromPCM(pcmC);
	}
	
	public void importNewFormat(String filename) throws IOException{
		jf = JSONReader.importJSON(filename);
		pcmC = JSONtoPCM.JSONFormatToPCM(jf);
	}
	
	@Deprecated
	public void exportOldFormat(){
		
	}
	
	public void exportNewFormat(){
		
	}
	
	
	public void importFromUnknow(){
		
	}

	@Test
	public void parsable(){
		
	}
	
	@Test
	public void firstPCMequalsToSecondPCM(){
		
	}

	@Test
	public void firstJSONequalsToSecondJSON(){
		
	}

	
}	

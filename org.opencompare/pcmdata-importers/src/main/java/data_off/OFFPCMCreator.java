package data_off;

import java.io.IOException;

public class OFFPCMCreator {
	
	public static void mkPCMFromCSV(String filename) throws IOException{
		
		System.out.println("Converting CSV file "+filename+" to PCM");
		PCMInterpreter.CSVToPCM(filename);
	}

	public static void mkPCMFromCategory(String s) {
		if(s.contains(":")){
			s = s.replace(':', '_');
		}
		try {
			OFFPCMCreator.mkPCMFromCSV("off_output/"+s+".csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void mkNewPCMFromCSV(String filename) throws IOException{
		
		System.out.println("Converting CSV file "+filename+" to new PCM");
		PCMInterpreter.CSVTonewPCM(filename);
	}

	public static void mkNewPCMFromCategory(String s) {
		if(s.contains(":")){
			s = s.replace(':', '_');
		}
		try {
			OFFPCMCreator.mkNewPCMFromCSV("off_output/"+s+".csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

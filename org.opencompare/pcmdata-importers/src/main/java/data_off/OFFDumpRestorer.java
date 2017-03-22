package data_off;

import java.io.IOException;

public class OFFDumpRestorer {
	
	public static boolean isWindows(){
		return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
	}
	
	private static boolean isLinux() {
		return System.getProperty("os.name").toLowerCase().indexOf("linux") >= 0;
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		System.out.println("Restoring...");
		try {
			String dumpDir = "data/dump";
			String[] command;
			if(isWindows()){
				String mongoRestore = "\"C:/Program Files/MongoDB/Server/3.2/bin/mongorestore.exe\" ";
	            command = new String[]{"cmd.exe", "/C", mongoRestore + dumpDir};
			}else if(isLinux()){
				command = new String[]{"/bin/sh", "-c", "mongorestore " + dumpDir};
			}else{
				command = new String[0];
			}
			
            Process p = Runtime.getRuntime().exec(command);
            DisplayStream outStream = new DisplayStream(p.getInputStream());
            DisplayStream errStream = new DisplayStream(p.getErrorStream());

            new Thread(outStream).start();
            new Thread(errStream).start();

            p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		System.out.println("Done restoring.");
	}

}

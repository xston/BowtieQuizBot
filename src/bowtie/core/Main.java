package bowtie.core;

import java.util.TimeZone;

import bowtie.util.Properties;
import bowtie.util.log.Log;

/**
 * @author &#8904
 *
 */
public class Main {
	public static Log log;
	public static void main(String[] args){
		log = new Log(TimeZone.getTimeZone("CET"));
		new Main();
	}
	
	public Main(){
		Properties.getValueOf("world");
		Properties.setValueOf("test", "ret");
		Log.closeAll();
	}
}
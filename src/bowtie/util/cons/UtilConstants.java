package bowtie.util.cons;

import java.io.File;

/**
 * Collection of constants which are used in the util package.
 * @author &#8904
 */
public final class UtilConstants {
	
	/** The path to the default log file. <p>'logs/systemLogs.txt' </p>*/
	public static final String DEFAULT_LOG_PATH;
	
	/** The path to the default properties file. <p>'properties/properties.txt'</p> */
	public static final String DEFAULT_PROPERTY_PATH;
	
	/**
	 * Creating all needed util files if they don't exist yet.
	 */
	static{
		DEFAULT_LOG_PATH = "logs/systemLogs.txt";
		DEFAULT_PROPERTY_PATH = "properties/properties.txt";
		final String[] files = {DEFAULT_LOG_PATH, DEFAULT_PROPERTY_PATH};
		for(String path : files){
			try{
				File file = new File(path);
				file.getParentFile().mkdirs();
				file.createNewFile();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
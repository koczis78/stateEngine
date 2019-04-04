package pl.redeem.jeep.picar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateProperties {
	private static final Logger LOG = LoggerFactory.getLogger(StateProperties.class);
	
	private Properties prop = new Properties();
	
	//singleton
	private static StateProperties instance = null;

	public static StateProperties getStateProperties() {
		if(instance == null) {
			synchronized(StateProperties.class) {
				if(instance==null) {
					instance = new StateProperties();
				}
			}
	      }
	      return instance;
	}

	private StateProperties() {
		LOG.info("Load properties");
		InputStream is = null;
		try {
			is = new FileInputStream("stateEngine.properties");
			prop.load(is);
		
			StringWriter stringWriter = new StringWriter();
			PrintWriter writer = new PrintWriter(stringWriter);
			prop.list(writer);
			
			LOG.info("Loded properties: {}", stringWriter.toString());
			
		} catch (FileNotFoundException e) {
			LOG.error("File stagEngine.properties not found", e);
		} catch (IOException e) {
			LOG.error("Cannot load stagEngine.properties", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					/*ignore*/
				}
			}
		}
	}
	
	public String getProperty(String name) {
		return prop.getProperty(name);
	}
	
	public long getPropertyLong(String name) {
		return Long.parseLong(prop.getProperty(name));
	}
	
	public int getPropertyInt(String name){
		return Integer.parseInt(prop.getProperty(name));
	}
	
	
	
	
	
	
	
	/* TEST only */
	public static void main(String[] args) {
		StateProperties.getStateProperties();
		
		
	}
	

}

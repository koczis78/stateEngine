package pl.redeem.jeep.picar.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.redeem.jeep.picar.StateProperties;

/** @Deprecated
 *
 */
@Deprecated
public class PcHttpConector {
	private static final Logger LOG = LoggerFactory.getLogger(PcHttpConector.class);
	
	private static final String PC_URL = StateProperties.getStateProperties().getProperty("http.pc.url");

	public static final String EVT_SLEEP_PC = "SLEEP_PC";
	public static final String EVT_STEERING_LEFT_UP = "STEERING_LEFT_UP";
	public static final String EVT_STEERING_LEFT_DOWN = "STEERING_LEFT_DOWN";
	public static final String EVT_STEERING_LEFT_CENTER = "STEERING_LEFT_CENTER";
	public static final String EVT_STEERING_RIGHT_UP = "STEERING_RIGHT_UP";
	public static final String EVT_STEERING_RIGHT_DOWN = "STEERING_RIGHT_DOWN";
	public static final String EVT_STEERING_RIGHT_CENTER = "STEERING_RIGHT_CENTER";
	public static final String EVT_DIMMER_1 = "DIMMER_1";
	public static final String EVT_DIMMER_2 = "DIMMER_2";
	public static final String EVT_DIMMER_3 = "DIMMER_3";
	public static final String EVT_DIMMER_4 = "DIMMER_4";
	public static final String EVT_DIMMER_5 = "DIMMER_5";
	public static final String EVT_DIMMER_6 = "DIMMER_6";
	public static final String EVT_DIMMER_7 = "DIMMER_7";
	public static final String EVT_DIMMER_8 = "DIMMER_8";
	public static final String EVT_DIMMER_9 = "DIMMER_9";
	
	public PcHttpConector() {
		LOG.info("INIT pc http conector");	
	}
	
	public void sendEvt(String evt) {
		LOG.info("Send event: {}", evt);
		
		String urlStr = PC_URL + "/index.html?" + evt;
		
		/*try {
			Content resp = Request.Get(urlStr).execute().returnContent();
			
			LOG.debug("send OK");
			LOG.trace("response: {}", resp.asString());
			
		} catch (IOException e) {
			LOG.error("Cannot send", e);
		}*/
		
		//pure java simple impl
		try {
			URL url = new URL(urlStr);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Jeep-State-Engine/1.0");

			int responseCode = con.getResponseCode();
		
			LOG.info("... response code: {}", responseCode);
		} catch (IOException e) {
			LOG.error("Cannot send event", e);
		}
	     
	}
	
	

}

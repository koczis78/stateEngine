package pl.redeem.jeep.picar.http;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.redeem.jeep.picar.JeepState;
import pl.redeem.jeep.picar.StateProperties;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Deprecated
public class ExternalHttpConector {
	private static final Logger LOG = LoggerFactory.getLogger(ExternalHttpConector.class);

	private static final String EXT_URL = StateProperties.getStateProperties().getProperty("http.external.url");

	private Gson gson = new Gson();

	public ExternalHttpConector() {
		LOG.info("INIT external http conector");
	}
	
	public void sendState() {
		//pure java simple impl
		try {
			URL url = new URL(EXT_URL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", "Jeep-State-Engine/1.0");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setConnectTimeout(5000);
			con.setRequestProperty("Host", "burzum.duckdns.org");
			con.connect();

			OutputStreamWriter printout = new OutputStreamWriter(con.getOutputStream());

			gson.toJson(JeepState.getJeepState(), printout);

			printout.flush();
			printout.close();

			int responseCode = con.getResponseCode();

			LOG.info("... response code: {}", responseCode);
		} catch (IOException e) {
			LOG.error("Cannot send event", e);
		}
	     
	}


    public static void main(String[] args) throws IOException {
        ExternalHttpConector ehc = new ExternalHttpConector();

        ehc.sendState();

    }
}

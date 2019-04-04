package pl.redeem.jeep.picar.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.redeem.jeep.picar.JeepState;
import pl.redeem.jeep.picar.StateProperties;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class PcHttpListener {
	
	private static final Logger LOG = LoggerFactory.getLogger(PcHttpListener.class);
	
	//singleton
	private static volatile PcHttpListener instance = null;
	
	public static PcHttpListener getPcHttpListener() {
		if(instance == null) {
			synchronized(PcHttpListener.class) {
				if(instance==null) {
					instance = new PcHttpListener();
				}
			}
	      }
	      return instance;
	}
	
	private PcHttpListener() {
		LOG.info("INIT PC HTTP listener");
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(StateProperties.getStateProperties().getPropertyInt("http.server.port")), 0);
			server.createContext("/", new PcHandler());
	        server.setExecutor(null);
	        server.start();
		} catch (IOException e) {
			LOG.error("Cannot create http server", e);
		}
	}
	
	static class PcHandler implements HttpHandler {
		@Override
        public void handle(HttpExchange t) throws IOException {
			InputStream is = t.getRequestBody();
			String rqBody = IOUtils.toString(is);
			IOUtils.closeQuietly(is);
			
			LOG.debug("request body: {}", rqBody);
			JeepState.getJeepState().respolveAndSetPcState(rqBody);
			
            String response = "{'response':'OK'}";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
		
	}

	
	

	
	//TEST only
	public static void main(String[] args) {	
		PcHttpListener.getPcHttpListener();
		
		while(true) {
			LOG.debug("current state: {}", JeepState.getJeepState().toString());
			
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}

}

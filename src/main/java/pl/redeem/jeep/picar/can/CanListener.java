package pl.redeem.jeep.picar.can;

import de.entropia.can.CanSocket;
import de.entropia.can.CanSocket.CanFrame;
import de.entropia.can.CanSocket.CanId;
import de.entropia.can.CanSocket.CanInterface;
import de.entropia.can.CanSocket.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.redeem.jeep.picar.StateProperties;
import pl.redeem.jeep.picar.procesor.CanProcesor;

import java.io.IOException;

public class CanListener {

	private static final Logger LOG = LoggerFactory.getLogger(CanListener.class);
	
	//singleton
	private static volatile CanListener instance = null;
	
	private CanSocket cs = null;
	private CanProcesor cp = null;

	public static CanListener getCanListener() {
		if(instance == null) {
			synchronized(CanListener.class) {
				if(instance==null) {
					instance = new CanListener();
				}
			}
	      }
	      return instance;
	}
			
	private CanListener() {
		LOG.info("INIT CanListener");
		cp = new CanProcesor();
		
		try {
			
			cs = new CanSocket(Mode.RAW);
			CanInterface iface = new CanInterface(cs, StateProperties.getStateProperties().getProperty("can.interface.name"));
			cs.bind(iface);
			
		} catch (IOException e) {
			LOG.error("Cannot connect can socket", e);
		}
	}
	
	public void logListen() {
		LOG.debug("start logListen");
		while(true) {
			try {
				CanFrame cf = cs.recv();
				
				CanId cid = cf.getCanId();
				
				LOG.debug("Received canId EFF: {}, ERR: {}, SFF: {}", Integer.toHexString(cid.getCanId_EFF()), Integer.toHexString(cid.getCanId_ERR()), Integer.toHexString(cid.getCanId_SFF()));
				
				byte[] fdata = cf.getData();
				
				String sdata = "";
				for(byte b : fdata) {
					sdata += Integer.toHexString(b) + ",";
				}
				
				LOG.debug("recv data: {}", sdata);
				
			} catch (Exception e) {
				LOG.error("Error in receive can frame: {}", e);
			}
			
		}
	}
	
	public void canListen() {
		LOG.debug("start listen");
		
		Runnable lthread = new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					try {
						CanFrame cf = cs.recv();
						CanId cid = cf.getCanId();
						String cidHexString = String.format("%03X", cid.getCanId_EFF());
						LOG.debug("Received canId: {} [EFF: {}, ERR: {}, SFF: {}]", cidHexString, Integer.toHexString(cid.getCanId_EFF()), Integer.toHexString(cid.getCanId_ERR()), Integer.toHexString(cid.getCanId_SFF()));
						
						if(!cp.checkAcessCanId(cidHexString)){
							LOG.trace("unknown canId: {} - DROP", cidHexString);
							continue;
						}
						
						byte[] fdata = cf.getData();
						
						String sdata = "";
						
						int cfsize = fdata.length;
						for(int i = 0;i<8;i++){
							String x = "00";
							if(i<cfsize) {
								x = String.format("%02X", fdata[i]);
							}
							sdata+=x;
						}
										
						LOG.debug("recv data: {}", sdata);
						
						cp.callCANMethod(cidHexString, sdata);
						
					} catch (Exception e) {
						LOG.error("Error in receive can frame: {}", e);
					}
					
				}
			}
		};
		
		LOG.info("Start lisen can thread");
		(new Thread(lthread)).start();
		
	}

	
	
	
	//TEST only
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CanListener cl = CanListener.getCanListener();
		cl.logListen();
	}

}

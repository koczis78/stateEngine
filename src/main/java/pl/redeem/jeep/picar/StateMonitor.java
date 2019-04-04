package pl.redeem.jeep.picar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.redeem.jeep.picar.gpio.GpioPiCarController;
import pl.redeem.jeep.picar.http.ExternalHttpConector;
import pl.redeem.jeep.picar.http.PcHttpConector;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StateMonitor {
	private static final Logger LOG = LoggerFactory.getLogger(StateMonitor.class);
	
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
	
	private JeepState js;
	//private PcHttpConector hc;
	//private ExternalHttpConector ehc;

	
	private int lastSendedDimmerValue = -1;
	
	public StateMonitor() {
		LOG.info("INIT StateMonitor");
	}

	public void startMonitor(){
		LOG.info("start State Monitor");
		js = JeepState.getJeepState();

		/*hc = new PcHttpConector();
		ehc = new ExternalHttpConector();*/

		scheduler.scheduleWithFixedDelay(new MonitorTask(), 100, 200, TimeUnit.MILLISECONDS);

		//scheduler.scheduleWithFixedDelay(new StateHttpSenderTask(), 1, 10, TimeUnit.SECONDS);
	}
	
	class MonitorTask implements Runnable {

	    private MonitorTask() {

		}

		public void run() {
			try {
			      LOG.info(js.toString());
			    
			      /* Wheel stering */
			      if(js.wheelSteringButtonLeftCenter) {
			    	  //hc.sendEvt(PcHttpConector.EVT_STEERING_LEFT_CENTER);

			      } else if(js.wheelSteringButtonLeftDown) {
			    	  //hc.sendEvt(PcHttpConector.EVT_STEERING_LEFT_DOWN);

			      } else if(js.wheelSteringButtonLeftUp) {
			    	  //hc.sendEvt(PcHttpConector.EVT_STEERING_LEFT_UP);

			      } else if(js.wheelSteringButtonRightCenter) {
			    	  //hc.sendEvt(PcHttpConector.EVT_STEERING_RIGHT_CENTER);

			      } else if(js.wheelSteringButtonRightDown) {
			    	  //hc.sendEvt(PcHttpConector.EVT_STEERING_RIGHT_DOWN);

			      } else if(js.wheelSteringButtonRightUp) {
			    	  //hc.sendEvt(PcHttpConector.EVT_STEERING_RIGHT_UP);

			      }
			      
			      /* IGN - on/off PC*/
			      /*if(js.ignRemoved) {

			      } else if(js.ignEngineRunning) {
			      }*/

				  /* PC going to sleep */
				  /*if(js.pcGotoSleep) {
				  }*/
			      
			      /* dimmer */
			      /*int dimmerValue = js.dimmerValue;
			      if(lastSendedDimmerValue!=dimmerValue) {
			    	  switch(js.dimmerValue) {
			    	  	case 1: hc.sendEvt(PcHttpConector.EVT_DIMMER_1);
			    	  			break;
			    	  	case 2: hc.sendEvt(PcHttpConector.EVT_DIMMER_2);
			    	  			break;
			    	  	case 3: hc.sendEvt(PcHttpConector.EVT_DIMMER_3);
			    	  			break;
			    	  	case 4: hc.sendEvt(PcHttpConector.EVT_DIMMER_4);
	    	  					break;
			    	  	case 5: hc.sendEvt(PcHttpConector.EVT_DIMMER_5);
	    	  					break;
			    	  	case 6: hc.sendEvt(PcHttpConector.EVT_DIMMER_6);
	    	  					break;
			    	  	case 7: hc.sendEvt(PcHttpConector.EVT_DIMMER_7);
	    	  					break;
			    	  	case 8: hc.sendEvt(PcHttpConector.EVT_DIMMER_8);
	    	  					break;
			    	  	case 9: hc.sendEvt(PcHttpConector.EVT_DIMMER_9);
	    	  					break;
	    	  			default:
	    	  					LOG.warn("Unknow DIMMER value: {}", dimmerValue);
			    	  }
			    	  
			    	  lastSendedDimmerValue = dimmerValue;
			      }*/
			      
			} catch (Throwable e) {
				LOG.error("catch Throwable on StateMonitor thread", e);
			}

		}
	    
	  }

	/*class StateHttpSenderTask implements Runnable {

		private StateHttpSenderTask() {
			LOG.debug("INIT StateHttpSenderTask");
		}

		public void run() {
			LOG.debug("Send JeepState to external");
			ehc.sendState();
		}

	}*/

}

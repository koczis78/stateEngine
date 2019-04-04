package pl.redeem.jeep.picar.gpio;

import com.pi4j.io.gpio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GpioPiCarController {
	private static final Logger LOG = LoggerFactory.getLogger(GpioPiCarController.class);

	private GpioPinDigitalOutput pinAMP;
	private GpioPinDigitalOutput pinPWSW;
	
	//singleton
	private static volatile GpioPiCarController instance;
	
	public static GpioPiCarController getGpioController() {
		if(instance == null) {
			synchronized(GpioPiCarController.class) {
				if(instance==null) {
					instance = new GpioPiCarController();
				}
			}
	      }
	      return instance;
	}

	private GpioPiCarController() {
		LOG.debug("INIT GPIO controller");
		GpioController gpio = GpioFactory.getInstance();
		
		//AMP
		pinAMP = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_26, "AMP", PinState.LOW);
		pinAMP.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

		//Power SW
		pinPWSW = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "PWSW", PinState.LOW);
		pinPWSW.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
		//Other
	}
	
	public void turnOnAmp(){
		LOG.info("turn ON AMP");
		pinAMP.high();
	}
	
	public void turnOffAmp(){
		LOG.info("turn OFF AMP");
		pinAMP.low();
	}

	public void turnOnPWSW(){
		LOG.info("turn ON PWSW");
		pinPWSW.high();
	}

	public void turnOffPWSW(){
		LOG.debug("turn OFF PWSW");
		pinPWSW.low();
	}

}

package pl.redeem.jeep.picar;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.redeem.jeep.picar.can.CanListener;
import pl.redeem.jeep.picar.gpio.GpioPiCarController;
import pl.redeem.jeep.picar.http.PcHttpListener;

public class StartStateEngine {
	private static final Logger LOG = LoggerFactory.getLogger(StartStateEngine.class);
	
	public static void main(String[] args) {
		LOG.info("Start State Engine");
		
		LOG.info("Can listener...");
		CanListener cl = CanListener.getCanListener();
		cl.canListen();
		LOG.info("...Can listener done");
		
		LOG.info("PC http listener...");
		PcHttpListener phl = PcHttpListener.getPcHttpListener();
		LOG.info("...PC http listener done");
		
		LOG.info("State monitor...");
		StateMonitor sm = new StateMonitor();
		sm.startMonitor();
		LOG.info("...State monitor done");

		/*LOG.info("Turn on PWSW...");
		GpioPiCarController gpio = GpioPiCarController.getGpioController();
		gpio.turnOnPWSW();

		LOG.info("Turn on AMP...");
		gpio.turnOnAmp();*/
		
		LOG.info("Waiting to magic word...");
		while(true) {
			Scanner readUserInput=new Scanner(System.in);
			String magicWord=readUserInput.nextLine();  
			
			System.out.println("magincWord = " + magicWord);
			LOG.info("magincWord = {}", magicWord);
			
			if("666".equalsIgnoreCase(magicWord)) {
				LOG.info("Exit...");	
				System.exit(1);
			}
			
			LOG.info("UNKNOWN magicWord");
		}
		
	}

}

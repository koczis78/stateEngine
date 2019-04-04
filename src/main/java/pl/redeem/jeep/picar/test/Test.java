package pl.redeem.jeep.picar.test;

import pl.redeem.jeep.picar.gpio.GpioPiCarController;
import pl.redeem.jeep.picar.http.PcHttpConector;
import pl.redeem.jeep.picar.procesor.CanProcesor;

public class Test {

	
	public static void main(String[] args) {

		byte[] fdata = {99};

		String sdata = "";
		
		int cfsize = fdata.length;
		for(int i = 0;i<7;i++){
			String x = "00";
			if(i<cfsize) x = String.format("%02X", fdata[i]);
			sdata+=x;
		}
		
		System.out.println("sdata: " + sdata);
		
		
		CanProcesor cp = new CanProcesor();
		
		cp.callCANMethod("3A0", "0200000000000000");
		
		System.out.println("canAccess: " + cp.checkAcessCanId("3A0"));
		
		
		PcHttpConector con = new PcHttpConector();
		//con.sendEvt(PcHttpConector.EVT_STEERING_LEFT_DOWN);
		
		GpioPiCarController gpio = GpioPiCarController.getGpioController();
		

	}

}

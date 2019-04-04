package pl.redeem.jeep.picar.procesor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.redeem.jeep.picar.JeepState;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CanProcesor {
	private static final Logger LOG = LoggerFactory.getLogger(CanProcesor.class);

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private boolean bTimeOut = true;

	private Set<String> accessibleCanIds;
	private Map<String, Method> canMethods = new HashMap<String, Method>();
	private JeepState jeepState;
	
	public boolean checkAcessCanId(String canId) {
		return accessibleCanIds.contains(canId);
	}
	
	public CanProcesor() {
		LOG.info("INIT CAN procesor");
		
		jeepState = JeepState.getJeepState();
		
		HashMap<String, String> hm = new HashMap<String, String>();
		
		for(Method m : this.getClass().getDeclaredMethods()){
			if(m.getName().startsWith("CAN_")) {
				String canId = m.getName().substring(m.getName().indexOf("_")+1, m.getName().lastIndexOf("_"));
				LOG.info("Add canId: {}", canId);
				hm.put(canId, canId);
				LOG.info("Add can method: {}", m.getName());
				canMethods.put(m.getName(), m);
			}
		}
		accessibleCanIds = hm.keySet();
	}
	
	public void callCANMethod(String canId, String canFrame) {
		LOG.debug("Call: canId: {}, canFrame: {}", canId, canFrame);
		
		String mName = "CAN_" + canId + "_" + canFrame;
		LOG.debug("try to call method: {}", mName);
		
		try {
			Method method = canMethods.get(mName);
			if(method!=null) {
				method.invoke(this);
			} else {
				LOG.debug("No method for: {}", mName);
			}
		} catch (ReflectiveOperationException e) {
			LOG.error("Cannnot call method: {}", mName, e);
		}
	}
	
	/* Kluczyk */
	public void CAN_000_0000000000000000(){
		LOG.debug("receive IGN REMOVED");
		jeepState.ignRemoved = true;
		jeepState.ignEngineRunning = false;
		jeepState.ignEngineStart = false;
		jeepState.ignInserted = false;
		jeepState.ignSwitchedOn = false;
	}
	public void CAN_000_0100000000000000(){
		LOG.debug("receive IGN OFF");
		jeepState.ignRemoved = false;
		jeepState.ignEngineRunning = false;
		jeepState.ignEngineStart = false;
		jeepState.ignInserted = true;
		jeepState.ignSwitchedOn = false;
	}
	public void CAN_000_6300000000000000(){
		LOG.debug("receive IGN ON");
		jeepState.ignRemoved = false;
		jeepState.ignEngineRunning = false;
		jeepState.ignEngineStart = false;
		jeepState.ignInserted = false;
		jeepState.ignSwitchedOn = true;
	}
	public void CAN_000_8500000000000000(){
		LOG.debug("receive IGN ENGINE WORKING");
		jeepState.ignRemoved = false;
		jeepState.ignEngineRunning = true;
		jeepState.ignEngineStart = false;
		jeepState.ignInserted = false;
		jeepState.ignSwitchedOn = false;
	}
	public void CAN_000_A900000000000000(){
		LOG.debug("receive IGN ENGINE STARTER");
		jeepState.ignRemoved = false;
		jeepState.ignEngineRunning = false;
		jeepState.ignEngineStart = true;
		jeepState.ignInserted = false;
		jeepState.ignSwitchedOn = false;
	}
	/* END Kluczyk */
	
	/* Klawisze kierownicy */
	public void CAN_3A0_0000000000000000(){
		LOG.debug("receive WHEEL STERING BUTTON - RELEASE");
		if(bTimeOut) {
			jeepState.wheelSteringButtonLeftCenter = false;
			jeepState.wheelSteringButtonRightCenter = false;
		}
		jeepState.wheelSteringButtonLeftDown = false;
		jeepState.wheelSteringButtonLeftUp = false;
		jeepState.wheelSteringButtonRightDown = false;
		jeepState.wheelSteringButtonRightUp = false;
	}
	public void CAN_3A0_0100000000000000(){
		LOG.debug("receive WHEEL STERING BUTTON - LEFT CENTER");
		sheduleBTimer();
		jeepState.wheelSteringButtonLeftCenter = true;
	}
	public void CAN_3A0_0200000000000000(){
		LOG.debug("receive WHEEL STERING BUTTON - LEFT UP");
		jeepState.wheelSteringButtonLeftUp = true;
	}
	public void CAN_3A0_0400000000000000(){
		LOG.debug("receive WHEEL STERING BUTTON - LEFT DOWN");
		jeepState.wheelSteringButtonLeftDown = true;
	}
	public void CAN_3A0_2000000000000000(){
		LOG.debug("receive WHEEL STERING BUTTON - RIGHT CENTER");
		sheduleBTimer();
		jeepState.wheelSteringButtonRightCenter = true ;
	}
	public void CAN_3A0_0800000000000000(){
		LOG.debug("receive WHEEL STERING BUTTON - RIGHT UP");
		jeepState.wheelSteringButtonRightUp = true;
	}
	public void CAN_3A0_1000000000000000(){
		LOG.debug("receive WHEEL STERING BUTTON - RIGHT DOWN");
		jeepState.wheelSteringButtonRightDown = true;
	}	
	/* END Klawisze kierownicy */
	
	/* Hamulec r�czny */
	public void CAN_0D0_8400000000000000(){
		LOG.debug("receive HANDBREAK - ENGAGED");
        jeepState.handBrakeOn = true;
	}
	public void CAN_0D0_0400000000000000(){
		LOG.debug("receive HANDBREAK - DRAINED");
        jeepState.handBrakeOn = false;
	}
	/* END Hamulec r�czny */
	
	/* Drzwi */
	public void CAN_013_A800000000000000(){
		LOG.debug("receive DOOR - ALL DOORS CLOSED");
        jeepState.doorDriverOpen = false;
        jeepState.doorPassengerOpen = false;
	}
	public void CAN_013_A800010000000000(){
		LOG.debug("receive DOOR - DRIVER DOOR OPEN");
        jeepState.doorDriverOpen = true;
	}
	public void CAN_013_A800020000000000(){
		LOG.debug("receive DOOR - PASSENGER DOOR OPEN");
        jeepState.doorPassengerOpen = true;
	}
	public void CAN_013_A800030000000000(){
		LOG.debug("receive DOOR - DRIVER AND PASSENGER DOOR OPEN");
        jeepState.doorDriverOpen = true;
        jeepState.doorPassengerOpen = true;
	}
    /* END Drzwi */

    /* Autoalarm */
	public void CAN_013_0801000000000000(){
		LOG.debug("receive AUTOALARM - ON");
        jeepState.autoalarmOn = true;
	}
	public void CAN_013_0800000000000000(){
		LOG.debug("receive AUTOALARM - OFF");
        jeepState.autoalarmOn = false;
	}
    /* END autoalarm */

	/* Wzmaczniacz */
	public void CAN_3D0_200A0A0A0A0A0000(){
		LOG.debug("receive AMP - TURNED ON");
	}
	public void CAN_3D0_000A0A0A0A0A0000(){
		LOG.debug("receive AMP - TURNED OFF");
	}
	/* END Wzmacniacz */
	
	/* Dimmer */
	public void CAN_1AC_0900000000000000(){
		if(jeepState.dimmerValue!=9) {
			LOG.debug("receive DIMMER - 9");
			jeepState.dimmerValue = 9;
		}
	}
	public void CAN_1AC_0800000000000000(){
		if(jeepState.dimmerValue!=8) {
			LOG.debug("receive DIMMER - 8");
			jeepState.dimmerValue = 8;
		}
	}
	public void CAN_1AC_0700000000000000(){
		if(jeepState.dimmerValue!=7) {
			LOG.debug("receive DIMMER - 7");
			jeepState.dimmerValue = 7;
		}
	}
	public void CAN_1AC_0600000000000000(){
		if(jeepState.dimmerValue!=6) {
			LOG.debug("receive DIMMER - 6");
			jeepState.dimmerValue = 6;
		}
	}
	public void CAN_1AC_0500000000000000(){
		if(jeepState.dimmerValue!=5) {
			LOG.debug("receive DIMMER - 5");
			jeepState.dimmerValue = 5;
		}
	}
	public void CAN_1AC_0400000000000000(){
		if(jeepState.dimmerValue!=4) {
			LOG.debug("receive DIMMER - 4");
			jeepState.dimmerValue = 4;
		}
	}
	public void CAN_1AC_0300000000000000(){
		if(jeepState.dimmerValue!=3) {
			LOG.debug("receive DIMMER - 3");
			jeepState.dimmerValue = 3;
		}
	}
	public void CAN_1AC_0200000000000000(){
		if(jeepState.dimmerValue!=2) {
			LOG.debug("receive DIMMER - 2");
			jeepState.dimmerValue = 2;
		}
	}
	public void CAN_1AC_0100000000000000(){
		if(jeepState.dimmerValue!=1) {
			LOG.debug("receive DIMMER - 1");
			jeepState.dimmerValue = 1;
		}
	}
	/* END Dimmer */
	
	/* Modu� bramy */
	public void CAN_1B3_0000000000000000(){
		LOG.debug("receive GATE - SWITCH OFF");
        jeepState.gateSwitch1On = false;
        jeepState.gateSwitch2On = false;
        jeepState.gateSwitch3On = false;
	}
	public void CAN_1B3_0900000000000000(){
		LOG.debug("receive GATE - SWITCH I");
        jeepState.gateSwitch1On = true;
	}
	public void CAN_1B3_0A00000000000000(){
		LOG.debug("receive GATE - SWITCH II");
        jeepState.gateSwitch2On = true;
	}
	public void CAN_1B3_0B00000000000000(){
		LOG.debug("receive GATE - SWITCH III");
        jeepState.gateSwitch3On = true;
	}
	/* END Modu� bramy */
	
	/* Kierunkowskazy */
	public void CAN_11D_0000000000000000(){
		LOG.debug("receive BLINKER - OFF");
	}
	public void CAN_11D_0800000000000000(){
		LOG.debug("receive BLINKER - LEFT ON");
	}
	public void CAN_11D_1000000000000000(){
		LOG.debug("receive BLINKER - LEFT OFF");
	}
	/* END Kierunkowskazy */

	private void sheduleBTimer() {
		bTimeOut = false;
		scheduler.schedule(new Runnable() {
			@Override
			public void run() {
				bTimeOut = true;
			}
		}, 90, TimeUnit.MILLISECONDS);
	}
	
}

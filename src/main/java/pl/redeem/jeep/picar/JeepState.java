package pl.redeem.jeep.picar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeepState {
	
	private static final Logger LOG = LoggerFactory.getLogger(JeepState.class);
	
	private enum GhostEvents {
		SystemIdle, SystemUnIdle, SystemSuspend, SystemResume, SystemGotoSleep
	}
	
	/* carpc states */
	public boolean pcStateIdle = false;
	public boolean pcStateUnIdle = true;
	public boolean pcStateSuspend = false;
	public boolean pcStateResume = true;
	public boolean pcGotoSleep = false;
	
	/*ign states */
	public boolean ignRemoved = false;
	public boolean ignInserted = false;
	public boolean ignSwitchedOn = false;
	public boolean ignEngineRunning = false;
	public boolean ignEngineStart = false;
	
	/*wheel stering*/
	public boolean wheelSteringButtonLeftCenter = false;
	public boolean wheelSteringButtonLeftUp = false;
	public boolean wheelSteringButtonLeftDown = false;
	public boolean wheelSteringButtonRightCenter = false;
	public boolean wheelSteringButtonRightUp = false;
	public boolean wheelSteringButtonRightDown = false;

	/* doors */
	public boolean doorDriverOpen = false;
	public boolean doorPassengerOpen = false;

	/* autoalarm */
	public boolean autoalarmOn = false;

	/* ręczny */
	public boolean handBrakeOn = false;

	/*dimmer*/
	public int dimmerValue = 9;

	/* gate switch */
	public boolean gateSwitch1On = false;
	public boolean gateSwitch2On = false;
	public boolean gateSwitch3On = false;
	
	//singleton
	private static volatile JeepState instance = null;
	
	public static JeepState getJeepState() {
		if(instance == null) {
			synchronized(JeepState.class) {
				if(instance==null) {
					instance = new JeepState();
				}
			}
	      }
	      return instance;
	}

	public void respolveAndSetPcState(String evt) {
		GhostEvents ge = GhostEvents.valueOf(evt);
		switch(ge) {
			case SystemIdle:
				pcStateIdle = true;
				pcStateUnIdle = false;
				pcStateSuspend = false;
				pcStateResume = true;
				pcGotoSleep = false;
				break;
			case SystemUnIdle:
				pcStateIdle = false;
				pcStateUnIdle = true;
				pcStateSuspend = false;
				pcStateResume = true;
				pcGotoSleep = false;
				break;
			case SystemSuspend:
				pcStateIdle = true;
				pcStateUnIdle = false;
				pcStateSuspend = true;
				pcStateResume = false;
				pcGotoSleep = false;
				break;
			case SystemResume:
				pcStateIdle = false;
				pcStateUnIdle = true;
				pcStateSuspend = false;
				pcStateResume = true;
				pcGotoSleep = false;
				break;
			case SystemGotoSleep:
				pcGotoSleep = true;
		}
	}

	private JeepState() {
		LOG.info("INIT JeepState");
	}

	@Override
	public String toString() {
		return "JeepState{" +
				"pcStateIdle=" + pcStateIdle +
				", pcStateUnIdle=" + pcStateUnIdle +
				", pcStateSuspend=" + pcStateSuspend +
				", pcStateResume=" + pcStateResume +
				", pcGotoSleep=" + pcGotoSleep +
				", ignRemoved=" + ignRemoved +
				", ignInserted=" + ignInserted +
				", ignSwitchedOn=" + ignSwitchedOn +
				", ignEngineRunning=" + ignEngineRunning +
				", ignEngineStart=" + ignEngineStart +
				", wheelSteringButtonLeftCenter=" + wheelSteringButtonLeftCenter +
				", wheelSteringButtonLeftUp=" + wheelSteringButtonLeftUp +
				", wheelSteringButtonLeftDown=" + wheelSteringButtonLeftDown +
				", wheelSteringButtonRightCenter=" + wheelSteringButtonRightCenter +
				", wheelSteringButtonRightUp=" + wheelSteringButtonRightUp +
				", wheelSteringButtonRightDown=" + wheelSteringButtonRightDown +
				", doorDriverOpen=" + doorDriverOpen +
				", doorPassengerOpen=" + doorPassengerOpen +
				", autoalarmOn=" + autoalarmOn +
				", handBrakeOn=" + handBrakeOn +
				", dimmerValue=" + dimmerValue +
				", gateSwitch1On=" + gateSwitch1On +
				", gateSwitch2On=" + gateSwitch2On +
				", gateSwitch3On=" + gateSwitch3On +
				'}';
	}
}

package pl.redeem.jeep.picar.http;

import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import com.pi4j.io.spi.SpiMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SwcPotAdjuster {
    private static final Logger LOG = LoggerFactory.getLogger(SwcPotAdjuster.class);

    public static final int EVT_STEERING_OFF = 128;
    public static final int EVT_STEERING_LEFT_UP = 10;
    public static final int EVT_STEERING_LEFT_DOWN = 20;
    public static final int EVT_STEERING_LEFT_CENTER = 30;
    public static final int EVT_STEERING_RIGHT_UP = 40;
    public static final int EVT_STEERING_RIGHT_DOWN = 50;
    public static final int EVT_STEERING_RIGHT_CENTER = 60;

    private static SpiDevice spi;

    public SwcPotAdjuster() {
        LOG.info("INIT swc pot adjuster");

        try {
            spi = SpiFactory.getInstance(SpiChannel.CS0, 976000, SpiMode.MODE_0);
            setPot(EVT_STEERING_OFF);
        } catch (Exception e) {
            LOG.error("Cannot initialize spi");
        }
    }

    void setPot(int potVal) {
        LOG.debug("SET swc pot value: {}", potVal);

        byte msb = (byte)(potVal >> 8);
        byte lsb = (byte)(potVal & 0xFF);
        byte[] val = new byte[] {msb, lsb};

        try {
            spi.write(val);
        } catch (Exception e) {
            LOG.error("Cannot write to spi", e);
        }
    }



    //TEST
    public static void main(String[] args) {
        SwcPotAdjuster swc = new SwcPotAdjuster();

        System.out.println("send LEFT UP");
        swc.setPot(SwcPotAdjuster.EVT_STEERING_LEFT_UP);

        try {
            Thread.sleep(200);
        } catch (Exception e) {
            System.out.println("sleep exception");
            e.printStackTrace();
        }

        System.out.println("send OFF");
        swc.setPot(SwcPotAdjuster.EVT_STEERING_OFF);

    }
}

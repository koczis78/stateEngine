package pl.redeem.jeep.picar;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JWol {

	private static final Logger LOG = LoggerFactory.getLogger(JWol.class);
	
	public static byte[] wol(String MAC) throws IOException, IllegalArgumentException {
        final String HEX = "0123456789ABCDEF";
        byte bMAC[] = new byte[6];
        int m = 0; // string index
        int i = 0; // MAC byte array index
        int h = 0; // last (high) hex digit
        MAC = MAC.toUpperCase();
        while (m < MAC.length() && i < 2 * 6){
            int n = HEX.indexOf(MAC.charAt(m));
            if (n >= 0){
                if (i % 2 == 0){
                    h = n;
                } else {
                    bMAC[i / 2] = (byte) (h * 16 + n);
                }
                i++;
            }
            m++;
        }
        if (m < MAC.length()){
            throw new IllegalArgumentException("MAC Address must be 12 Hex digits exactly");
        }

        return bMAC;
    }

    public void wol(byte[] MAC) throws IOException {
        if (MAC == null || MAC.length != 6) {
            throw new IllegalArgumentException("MAC array must be present and 6 bytes long");
        }

        // Assemble Magic Packet
        int packetLength = 102;
        byte packetData[] = new byte[packetLength];
        int m = 0;

        // Start off with six 0xFF values
        packetData[m++] = (byte) 255;
        packetData[m++] = (byte) 255;
        packetData[m++] = (byte) 255;
        packetData[m++] = (byte) 255;
        packetData[m++] = (byte) 255;
        packetData[m++] = (byte) 255;

        // Append sixteen copies of MAC address
        for (int i = 0; i < 16 * 6; i++){
            packetData[m] = MAC[m % 6];
            m++;
        }

        DatagramSocket socket = new DatagramSocket(1);
        try {
            InetSocketAddress address = new InetSocketAddress("255.255.255.255", 2304);
            DatagramPacket datagram = new DatagramPacket(packetData, packetLength, address);
            socket.setBroadcast(true);
            socket.send(datagram);
        } finally {
            socket.close();
        }
        LOG.debug("Magic Packet sent.");
    }

    
    
    /**
     * Send/broadcast WoL Magic Packet to host identified by 6-byte MAC.
     * 
     * @param args
     *            MAC address in hex format. Non-hex characters are ignored.
     *            Must end with Hex character. Example:
     *            &quot;00-50-12-34-56-78&quot;
     */
    public static void main(String[] args) throws IOException
    {
        JWol wol = new JWol();
        
        byte[] bMAC = JWol.wol(args[0]);
        wol.wol(bMAC);
        
    }

}

package screenpac.remote;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.DatagramSocket;

public class UDPClient {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            try {
                // get a datagram socket
                DatagramSocket socket = new DatagramSocket();

                // send request
                String str = "Client message: " + i;
                byte[] bOut = str.getBytes();
                InetAddress address = InetAddress.getByName("localhost");
                DatagramPacket packet = new DatagramPacket(bOut, bOut.length, address, 4445);
                socket.send(packet);
                System.out.println("Sent: " + packet);
                // get response
                byte[] buf = new byte[256];
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                // display response
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Quote of the Moment: " + received);

                socket.close();


                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

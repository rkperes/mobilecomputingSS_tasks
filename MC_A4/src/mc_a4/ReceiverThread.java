/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mc_a4;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rafaelkperes
 */
public class ReceiverThread implements Runnable {

    private static final Logger LOGGER = Logger.getLogger("MCA4");

    private final LinkedBlockingDeque<PacketContent> packetsToSend;
    private final HashSet<PacketContent> alreadyReceived;

    public ReceiverThread(LinkedBlockingDeque<PacketContent> packetsToSend) {
        this.packetsToSend = packetsToSend;
        this.alreadyReceived = new HashSet<>();
    }

    @Override
    public void run() {
        LOGGER.log(Level.INFO, "Receiver thread started!");
        try {
            byte[] buf = new byte[1500];
            DatagramSocket sock = new DatagramSocket(5000 + 20);
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            while (true) {
                //LOGGER.log(Level.INFO, "waiting for packet...");
                sock.receive(packet);
                byte[] b = packet.getData();

                PacketContent content = MC_A4.deserializeContent(b);
                //ByteArrayInputStream bis = new ByteArrayInputStream(b);
                //ObjectInput in = new ObjectInputStream(bis);
                //PacketContent content = (PacketContent) in.readObject();
                if (!alreadyReceived.contains(content)) {
                    alreadyReceived.add(content);
                    LOGGER.log(Level.INFO, "received: {0}", content.toString());
                    System.out.println("received: " + new String(content.getContent()));
                    System.out.println("from: " + content.getOriginalsender());

                    if (MC_A4.isMyIP(content.getDestination())) {
                        LOGGER.log(Level.INFO, "PACKET REACHED DESTINATION: {0}", content.toString());
                        System.out.println("I'm the destination!");
                    } else {
                        /* rebroadcast packet! */
                        packetsToSend.put(content); // should it be 'packet' or 'content'?
                        LOGGER.log(Level.INFO, "added to sender queue: {0}", content.toString());
                    }
                }
            }
        } catch (SocketException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException | InterruptedException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

}

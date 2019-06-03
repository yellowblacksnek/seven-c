package ru.snek;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.DatagramChannel;

import static ru.snek.Printer.log;
import static ru.snek.Printer.println;
import static ru.snek.Utils.*;

public class ServerListener extends Thread{
    private Connection con;
    private volatile boolean active;
    private volatile boolean stop = false;

    public ServerListener(Connection con) {
        this.con = con;
        active = false;
    }

    @Override
    public void run() {
        try {
            active = true;
            SocketAddress address = con.getAddress();
            ByteBuffer bb = ByteBuffer.allocate(2048);
            DatagramChannel channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            channel.connect(address);
            while (!stop) {
                while (!active) {
                    log("sleep");
                    Thread.sleep(100);
                }
                bb.clear();
                //log("reading");
                int i = channel.read(bb);
                log(i);
                if(i > 0) {
                    Response res = (Response) objectFromByteArray(bb.array());
                    println(res.getData());
                }
                Thread.sleep(500);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        active = true;
    }

    public void pause() {
        active = false;
    }

    public void kill() { stop = true; }
}

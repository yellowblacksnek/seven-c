package ru.snek;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import static ru.snek.Command.Commands.TEST;
import static ru.snek.Logger.handleException;
import static ru.snek.Printer.log;
import static ru.snek.Utils.*;

public class Connection {
    private DatagramChannel chan;

    //private int bufferSize = 2048;
    private final int maxBufferSize = 65507;
    //private final int defaultTimeout = 5000;

    SocketAddress address;

    Connection(InetAddress addr, int port) throws IOException {
        address = new InetSocketAddress(addr, port);
        chan = DatagramChannel.open();
        chan.connect(address);
    }

    public Response receive() throws IOException {
        //boolean waiterOn = waiter != null;
        ByteBuffer buf = ByteBuffer.allocate(10);
        //chan.socket().setSoTimeout(defaultTimeout);
        buf.clear();
        chan.read(buf);
        //chan.socket().setSoTimeout(200);
        //if (waiterOn) waiter.setStage(3);
        buf.flip();
        int size = buf.asIntBuffer().get();
        if (size == 0) return null;
        int amount = size <= maxBufferSize ? 1 : (size / maxBufferSize + 1);
        buf = ByteBuffer.allocate(amount > 1 ? amount * maxBufferSize : size);
        for (int j = 0; j < size; ) {
            j += chan.read(buf);
            //if (waiterOn) waiter.setPercentage(getPercentage(j, size));
        }
        Response res = (Response) objectFromByteArray(buf.array());
        return res;
    }

    public void send(Command obj) throws Exception {
        //boolean waiterOn = waiter != null;
        byte[] objAsArr;
        objAsArr = objectAsByteArray(obj);
        if (objAsArr == null) throw new Exception("Это странно");
        //if (waiterOn) waiter.setStage(4);
        ByteBuffer bb = ByteBuffer.wrap(objAsArr);
        chan.write(bb);
    }

    public boolean checkConnection() {
        try {
            if (!chan.isOpen()) return false;
            //chan.socket().setSoTimeout(1000);
            send(new Command(TEST));
            Response response = receive();
            //chan.socket().setSoTimeout(defaultTimeout);
            if (response == null) return false;
            if (!response.isNotification() &&
                response.getCommandType() == TEST &&
                response.getStatus() == Response.Status.OK) return true;
            else return false;
        } catch (Exception e) {
            handleException(e);
            return false;
        }
    }

    public DatagramChannel getChan() { return chan; }

    public SocketAddress getAddress() { return address; }

    public void close() {
        try {
            chan.close();
        } catch(IOException e) {  }
    }
}

package com.xiongying;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by xiongying on 17/2/14.
 */
public class TimeClient {

    public static void main(String[] args) {
        int port = 7071;
        Socket socket = null;
        DataInputStream in = null;
        DataOutputStream out = null;
        try {
            SocketAddress address = new InetSocketAddress("127.0.0.1", port);
            socket = new Socket();
            socket.connect(address);
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());

            String sendMsgStr = "2" + "QUERY TIME ORDER" + "3";
            byte[] sendBytes = sendMsgStr.getBytes();
            sendBytes[0] = 0x02;
            sendBytes[sendBytes.length - 1] = 0x03;
            out.write(sendBytes);
            out.flush();
            System.out.println("Send Order 2 server succeed.");

            StringBuilder recvDataStr = new StringBuilder();
            while (true) {
                try {
                    byte rcvByte = in.readByte();
                    if (rcvByte == 2) {
                        recvDataStr = new StringBuilder();
                    } else if (rcvByte == 3) {
                        System.out.println("Now is:" + recvDataStr.toString());
                        recvDataStr = new StringBuilder();
                    } else {
                        recvDataStr.append((char) rcvByte);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out = null;
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket = null;
            }
        }
    }
}

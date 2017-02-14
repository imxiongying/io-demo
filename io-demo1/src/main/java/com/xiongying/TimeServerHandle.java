package com.xiongying;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

/**
 * Created by xiongying on 17/2/14.
 */
public class TimeServerHandle implements Runnable {
    private Socket socket;

    public TimeServerHandle(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        DataInputStream in = null;
        DataOutputStream out = null;
        try {
            in = new DataInputStream(this.socket.getInputStream());
            out = new DataOutputStream(this.socket.getOutputStream());
            StringBuilder recvDataStr = new StringBuilder();
            while (true) {
                try {
                    byte rcvByte = in.readByte();
                    if (rcvByte == 2) {
                        recvDataStr = new StringBuilder();
                    } else if (rcvByte == 3) {
                        System.out.println("The time server receive order:" + recvDataStr.toString());

                        String currentTime = "QUERY TIME ORDER".equals(recvDataStr.toString()) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
                        String sendMsgStr = "2" + currentTime + "3";
                        byte[] sendBytes = sendMsgStr.getBytes();
                        sendBytes[0] = 0x02;
                        sendBytes[sendBytes.length - 1] = 0x03;
                        out.write(sendBytes);
                        out.flush();

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
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                in = null;
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                out = null;
            }
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                this.socket = null;
            }
        }
    }
}

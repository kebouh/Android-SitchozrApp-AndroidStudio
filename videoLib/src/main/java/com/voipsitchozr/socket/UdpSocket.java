package com.voipsitchozr.socket;

import android.util.Log;
import android.view.SurfaceView;

import com.voipsitchozr.options.ConnexionOptions;
import com.voipsitchozr.packet.DatagramPacketWraper;
import com.voipsitchozr.utils.ConcurrentQueue;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import media.VideoReceiver;
import media.VideoSender;

/**
 * Created by kebouh on 18/11/15.
 */
public class UdpSocket  {

    ConcurrentQueue<byte[]>  sendQueue;
    DatagramSocket      socket;
    int                 port;
    InetAddress			inetAddr;
    VideoReceiver       videoReceiver;
    SendThread          sendThread;

    public UdpSocket(ConcurrentQueue<byte[]> sendQueue, int port) {
        this.sendQueue = sendQueue;
        try {
            this.socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.port = port;
        videoReceiver = new VideoReceiver();
    }

    public  void    start(SurfaceView contact) {

        try {
            videoReceiver.setDatagramSocket(new DatagramSocketReceiver(socket));
        } catch (SocketException e) {
            e.printStackTrace();
        }
        videoReceiver.initAndStartVideoReceiver(contact);
        sendThread = new SendThread();
        sendThread.start();
    }

    public	void	initInetAddrAndSocket() throws IOException
    {
        try {
            inetAddr = InetAddress.getByName(ConnexionOptions.SERVER_IP);
            socket = new DatagramSocket(port);
        } catch (UnknownHostException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public class        SendThread extends Thread {

        @Override
        public void run() {
            VideoSender videoSender = VideoSender.getInstance();
            try {
                initInetAddrAndSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted())
            {
                byte[]		data;
               // Log.d("UDP", "loop send");

                if (!sendQueue.isEmpty())
                {
                    data = sendQueue.poll();
                    DatagramPacketWraper packetWraper = videoSender.preparePacket(data);
                    if (packetWraper != null)
                    {
                        byte[] dataPacket = (byte[]) packetWraper.getPacket();
                        DatagramPacket packet = new DatagramPacket(dataPacket, dataPacket.length, inetAddr, port);
                        try {
                            if (socket != null)
                                socket.send(packet);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void onStop()
    {
        if (sendThread != null && sendThread.isAlive())
            sendThread.interrupt();
    }
}

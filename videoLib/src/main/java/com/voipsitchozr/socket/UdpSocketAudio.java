package com.voipsitchozr.socket;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.provider.MediaStore;
import android.view.SurfaceView;

import com.voipsitchozr.options.ConnexionOptions;
import com.voipsitchozr.packet.DatagramPacketWraper;
import com.voipsitchozr.utils.AudioManagerState;
import com.voipsitchozr.utils.ConcurrentQueue;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import media.AudioReceiver;
import media.VideoReceiver;
import media.VideoSender;

/**
 * Created by kebouh on 18/11/15.
 */
public class UdpSocketAudio {

    ConcurrentQueue<byte[]>  sendQueue;
    DatagramSocket      socket;
    int                 port;
    InetAddress			inetAddr;
    SendThread          sendThread;
    Thread              audioReaderThread;
    AudioReceiver audioReceiver;

    public UdpSocketAudio(ConcurrentQueue<byte[]> sendQueue, int port) {
        this.sendQueue = new ConcurrentQueue<byte[]>();
        try {
            System.out.println("port: " + port);
            this.socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        audioReceiver = new AudioReceiver();
        this.port = port;
    }

    public  void    start() {
        audioReceiver.initAndStartAudioReceiver();
        try {
            audioReceiver.setDatagramSocket(new DatagramSocketReceiver(socket, /*AudioManagerState.actualBufferSize*/4096));
        } catch (SocketException e) {
            e.printStackTrace();
        }
        sendThread = new SendThread();
        sendThread.start();
    }

    public	void	initInetAddrAndSocket() throws IOException
    {
        try {
            inetAddr = InetAddress.getByName(ConnexionOptions.SERVER_IP);
            //socket = new DatagramSocket(port);
        } catch (UnknownHostException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public class        SendThread extends Thread {

        @Override
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            //AudioReaderThread();
            try {
                initInetAddrAndSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
            boolean first = true;
            if (first) {
                first = false;
                DatagramPacket packet = new DatagramPacket(ConnexionOptions.ID_SELF.getBytes(), ConnexionOptions.ID_SELF.getBytes().length, inetAddr, port);
                try {
                    System.out.println("udpsendId" + ConnexionOptions.ID_SELF);
                    if (socket != null && packet != null) {
                        System.out.println("udpsendId not null");
                        socket.send(packet);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            AudioRecord arec = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, AudioManagerState.actualBufferSize);

            AcousticEchoCanceler.create(arec.getAudioSessionId());
            arec.startRecording();
            while (!Thread.currentThread().isInterrupted())
            {
                byte[] buffer = new byte[AudioManagerState.actualBufferSize];
                arec.read(buffer, 0, AudioManagerState.actualBufferSize);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inetAddr, port);
                        try {
                            if (socket != null)
                                socket.send(packet);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                buffer = null;
            }
        }
    }

    public void AudioReaderThread()
    {
        audioReaderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isRecording = true;
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                AudioRecord arec = new AudioRecord(MediaRecorder.AudioSource.MIC, 11025, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, AudioManagerState.actualBufferSize);
                byte[] buffer = new byte[AudioManagerState.actualBufferSize];
                arec.startRecording();
                while(isRecording && !Thread.currentThread().isInterrupted()) {
                    //System.out.println("audio: read");
                    arec.read(buffer, 0, AudioManagerState.actualBufferSize);
                    sendQueue.add(buffer);
                }
            }
        });
        audioReaderThread.start();
    }

    public void onStop()
    {
        if (sendThread != null && sendThread.isAlive())
            sendThread.interrupt();
       // if (!audioReaderThread.isInterrupted())
        //audioReaderThread.interrupt();
        if (!audioReceiver.isInterrupted())
            audioReceiver.interrupt();
        audioReceiver.onStop();
        socket.close();
    }
}

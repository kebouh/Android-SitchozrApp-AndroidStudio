package media;

import android.app.Activity;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;

import com.voipsitchozr.main.VoipManager;
import com.voipsitchozr.socket.DatagramSocketReceiver;
import com.voipsitchozr.utils.AudioManagerState;
import com.voipsitchozr.utils.ConcurrentQueue;

import java.io.IOException;

/**
 * Created by kebouh on 11/12/15.
 */
public class AudioReceiver extends Thread {

    ConcurrentQueue<byte[]> queue;
    DatagramSocketReceiver  socket;
    Thread                  play;
   // AudioTrack atrack;

    public AudioReceiver() {
        queue = new ConcurrentQueue<byte[]>();
    }

    public void initAndStartAudioReceiver() {
        //threadPlay();
        //play.start();
        this.start();
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        AudioTrack atrack = new AudioTrack(AudioManager.STREAM_MUSIC, 11025, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, AudioManagerState.actualBufferSize, AudioTrack.MODE_STREAM);
        atrack.setPlaybackRate(11025);
        atrack.play();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                byte[] buffer = socket.receivePacket();
                if (buffer != null)
                atrack.write(buffer, 0, buffer.length);
            } catch (IOException e) {
                e.printStackTrace();
                socket.close();
            }
        }
    }

    public void threadPlay() {
        play = new Thread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                AudioTrack atrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL, 11025, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, AudioManagerState.actualBufferSize, AudioTrack.MODE_STREAM);
                atrack.setPlaybackRate(11025);
                atrack.play();
                while (!Thread.currentThread().isInterrupted()) {
                    byte[] buffer = queue.poll();
                    //System.out.println("audio: play");
                    if (buffer != null) {
                      //  System.out.println("audio: buffer " + buffer.length);
                        atrack.write(buffer, 0, buffer.length);
                    }
                }
            }
        });
    }

    public void onStop() {
      //  if (!play.isInterrupted())
      //  play.interrupt();
    }

    public void setDatagramSocket(DatagramSocketReceiver socket) {
        this.socket = socket;
    }
}

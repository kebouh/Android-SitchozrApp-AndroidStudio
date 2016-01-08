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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

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
        AudioTrack atrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT, 4096/*AudioManagerState.actualBufferSize*/, AudioTrack.MODE_STREAM);
        atrack.setPlaybackRate(44100);
        atrack.play();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                byte[] buffer = socket.receivePacket();
                System.out.println("buffer: " + buffer.length);
                if (buffer != null) {
                    int[] b = byteToInt(buffer);
                    atrack.write(buffer, 0, buffer.length);
                }
            } catch (IOException e) {
                e.printStackTrace();
                socket.close();
            }
        }
    }

    public static short bytesToShort(final short highByte, final short lowByte) {
        final short high = (short) (highByte & 0xff);
        final short low = (short) (lowByte & 0xff);
        return (short)((highByte & 0xFF) | lowByte<<8);
    }

    public int[] byteToInt(byte[] array) {

        int[] shorts = new int[((array.length) /2)];
        int i = 0;
        System.err.println("size array: " + array.length);
        int n = 0;
        while (i+1 < array.length) {
            int val = ((array[i] & 0xff) << 8) | (array[i+1] & 0xff);
            shorts[n] = val;
            //shorts[n] = bytesToShort(array[i], array[i+1]);
            i += 2;
            n++;
        }
        // to turn bytes to shorts as either big endian or little endian.
        //ByteBuffer.wrap(array).order(ByteOrder.BIG_ENDIAN).asShortBuffer().get(shorts);

        return shorts;
    }

    public void onStop() {
      //  if (!play.isInterrupted())
      //  play.interrupt();
    }

    public void setDatagramSocket(DatagramSocketReceiver socket) {
        this.socket = socket;
    }
}

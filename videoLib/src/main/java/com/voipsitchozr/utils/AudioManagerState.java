package com.voipsitchozr.utils;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;

/**
 * Created by kebouh on 11/12/15.
 */
public class AudioManagerState {

    static int oldAudioMode;
    static int oldRingerMode;
    static boolean isSpeakerPhoneOn;
    public static AudioManager audioManager;
    //static public int bufferSize;
    static public int actualBufferSize;

    public static void initAndSaveState(Context context) {
        audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        oldAudioMode = audioManager.getMode();
        oldRingerMode = audioManager.getRingerMode();
        isSpeakerPhoneOn = audioManager.isSpeakerphoneOn();
        //bufferSize = AudioRecord.getMinBufferSize(11025, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);

        int maxBufferSize = 4096; // my value. see what's working best for you.
        int minBufferSize = AudioRecord.getMinBufferSize(44100,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        actualBufferSize = Math.max(minBufferSize, maxBufferSize);
        //audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        //audioManager.setMode(AudioManager.STREAM_MUSIC);
        audioManager.setMode(AudioManager.STREAM_MUSIC);
        audioManager.setSpeakerphoneOn(true);
    }

    public static void restoreState() {
        audioManager.setMode(oldAudioMode);
        audioManager.setRingerMode(oldRingerMode);
        audioManager.setSpeakerphoneOn(isSpeakerPhoneOn);
    }

}

/*
 * Copyright 2013 JNRain
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.jnrain.mobile.ui;

import org.jnrain.didadi.test.DDDTest;

import android.app.Activity;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;


public class AudioDebugActivity extends Activity {
    public AudioRecord audioRecord;
    public int mSamplesRead; // how many samples read
    public int buffersizebytes;
    public int buflen;

    public static final int CHANNEL_CFG = AudioFormat.CHANNEL_IN_MONO;
    public static final int SAMPLE_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    public static final int SAMPLERATE = 44100;

    public static short[] buffer; // +-32767

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.main);
        buffersizebytes = AudioRecord.getMinBufferSize(
                SAMPLERATE,
                CHANNEL_CFG,
                SAMPLE_FORMAT);
        buffer = new short[buffersizebytes];
        buflen = buffersizebytes / 2;
        audioRecord = new AudioRecord(
                android.media.MediaRecorder.AudioSource.MIC,
                SAMPLERATE,
                CHANNEL_CFG,
                SAMPLE_FORMAT,
                buffersizebytes); // constructor
        trigger();
    }

    public void trigger() {
        acquire();
        dump();
    }

    public void acquire() {
        try {
            audioRecord.startRecording();
            mSamplesRead = audioRecord.read(buffer, 0, buffersizebytes);
            audioRecord.stop();
        } catch (Throwable t) {
            Log.e("AudioDebug", "Recording Failed");
        }
    }

    public void dump() {
        TextView tv = new TextView(this);
        setContentView(tv);
        tv.setTextColor(Color.BLACK);
        tv.setText(DDDTest.init() + "\n");
        tv.append("buffersizebytes " + buffersizebytes + "\n");
        for (int i = 0; i < 256; i++) {
            tv.append(" " + buffer[i]);
        }
        tv.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
        trigger();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
            audioRecord.stop();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        audioRecord.release();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionevent) {
        if (motionevent.getAction() == MotionEvent.ACTION_UP) {
            trigger(); // acquire buffer full of samples
        }
        return true;
    }
}

package com.choiintack.easyscreenrecorder;

import android.content.Context;
import android.media.projection.MediaProjection;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.choiintack.easyscreenrecorder.encoder.MediaAudioEncoder;
import com.choiintack.easyscreenrecorder.encoder.MediaEncoder;
import com.choiintack.easyscreenrecorder.encoder.MediaEncoder.MediaEncoderListener;
import com.choiintack.easyscreenrecorder.encoder.MediaMuxerWrapper;
import com.choiintack.easyscreenrecorder.encoder.MediaScreenEncoder;

import java.io.IOException;

/**
 * Created by choiintack on 2017. 7. 18..
 */

public class EasyScreenRecorder {

    private Context mContext;
    private String mPath;
    private MediaProjection mMediaProjection;
    private int mFrameRate = 25;
    private int mBitrate = -1;
    private boolean mRecordAudio = true;

    private MediaMuxerWrapper mMuxer;
    private MediaEncoderListener mListener = new MediaEncoderListener() {
        @Override
        public void onPrepared(MediaEncoder encoder) {
            /* empty callback */
        }

        @Override
        public void onStopped(MediaEncoder encoder) {
            /* empty callback */
        }
    };

    public EasyScreenRecorder(Context context, String path, MediaProjection mediaProjection) {
        this.mContext = context;
        this.mPath = path;
        this.mMediaProjection = mediaProjection;
    }

    public void recordAudio(boolean recordAudio) {
        this.mRecordAudio = recordAudio;
    }

    public void setFrameRate(int frameRate) {
        this.mFrameRate = frameRate;
    }

    public void setBitrate(int bitrate) {
        this.mBitrate = bitrate;
    }

    public void start() {
        try {
            mMuxer = new MediaMuxerWrapper(mPath); // if you record audio only, ".m4a" is also OK.
            new MediaScreenEncoder(mMuxer
                    , mListener
                    , mMediaProjection
                    , getWidth()
                    , getHeight()
                    , getScreenDensity()
                    , mBitrate
                    , mFrameRate);

            if (mRecordAudio) {
                new MediaAudioEncoder(mMuxer, mListener);
            }

            mMuxer.prepare();
            mMuxer.startRecording();
        } catch (final IOException e) {
            Log.e("TAG", "error while starting EasyScreenRecorder:", e);
        }
    }

    public void pause() {
        mMuxer.pauseRecording();
    }

    public void resume() {
        mMuxer.resumeRecording();
    }

    public void stop() {
        mMuxer.stopRecording();
    }

    public String getPath() {
        return mPath;
    }

    private int getScreenDensity() {
        return getDisplayMetrics().densityDpi;
    }

    private int getWidth() {
        return getDisplayMetrics().widthPixels;
    }

    private int getHeight() {
        return getDisplayMetrics().heightPixels;
    }

    private DisplayMetrics getDisplayMetrics() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display =  wm.getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics;
    }


}




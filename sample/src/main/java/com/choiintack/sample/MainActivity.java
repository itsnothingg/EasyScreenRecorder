package com.choiintack.sample;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.choiintack.easyscreenrecorder.EasyScreenRecorder;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EasyScreenRecorder mScreenRecorder;
    MediaProjectionManager mMediaProjectionManager;
    int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new TedPermission(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {}
                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(MainActivity.this, "this app needs permission to perform screen recording", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .check();
    }

    void start(View v) {
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
        v.setVisibility(View.GONE);
        findViewById(R.id.stop).setVisibility(View.VISIBLE);
    }

    void stop(View v) {
        mScreenRecorder.stop();
        v.setVisibility(View.GONE);
        findViewById(R.id.start).setVisibility(View.VISIBLE);
        play(mScreenRecorder.getPath());
    }

    void play(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
        intent.setDataAndType(Uri.parse(path), "video/mp4");
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                return;
            }
            MediaProjection mediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
            mScreenRecorder = new EasyScreenRecorder(this, generatePath(getDateTimeString()), mediaProjection);
            mScreenRecorder.start();
        }
    }

    String generatePath(String fileName) {
        final File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "EasyScreenRecorder");
        dir.mkdirs();
        if (dir.canWrite()) {
            return new File(dir, fileName + ".mp4").toString();
        }
        return null;
    }

    String getDateTimeString() {
        final GregorianCalendar now = new GregorianCalendar();
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US).format(now.getTime());
    }

}













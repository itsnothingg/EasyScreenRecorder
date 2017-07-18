# EasyScreenRecorder
Library that helps you to implement screen recorder(with sound) functionality easily

From the sample :
![Screenshot](screenshot.gif)


## How to use

First Add jitpack.io on yout root `build.gradle`:
```gradle
allprojects {
    repositories {
        maven { url 'http://raw.github.com/saki4510t/libcommon/master/repository/' }
        maven { url 'https://jitpack.io' }
        jcenter()
    }
}
```


Add the following dependency on your app's `build.gradle`:
```gradle
compile 'com.github.itsnothingg:EasyScreenRecorder:1.0.0'
```

First of all, make sure you have these permissions granted in order for this lib to work.
```java
Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO
```


First thing you'll have to do is to grant media projection permission by using the following code
```java
MediaProjectionManager mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
```
this will call permission dialog, and return result on `onActivityResult`

Then on `onActivityResult` initialize EasyScreenRecorder instance
```java
@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                return;
            }
            MediaProjection mediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
            EasyScreenRecorder mScreenRecorder = new EasyScreenRecorder([yourContext], [pathToYourMP4File], mediaProjection);
        }
    }
```

Now you are ready to use these functions
```java
mScreenRecorder.start(); // start recording
mScreenRecorder.pause(); // pause recording
mScreenRecorder.resume(); // resume recording
mScreenRecorder.stop(); // finish and release recording

File videoFile = new File(mScreenRecorder.getPath()); // after recording has stopped, you can play with your file.
```

There are few thing you can customize like:
```java
mScreenRecorder.recordAudio(false); // don't record audio
mScreenRecorder.setFrameRate(30); // set frame rate to 30. (default is 25)
mScreenRecorder.setBitrate(800 * 1000); // change bitrate (default is auto-calculated)
```

please take a look at sample module for more details.


## Notice
copyright of java files under `encoder` package belongs to saki t_saki@serenegiant.com 
most of the sources are from https://github.com/saki4510t/AudioVideoRecordingSample repo. 
it's great repo so please take a look if you are interested in MediaCodec/MediaMuxer. 
















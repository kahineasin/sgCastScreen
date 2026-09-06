package com.sellgirl.castScreen.android.send2;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;
import androidx.core.app.ActivityCompat;
import java.nio.ByteBuffer;

public class ScreenCaptureManager {
    private static final String TAG = "ScreenCapture";
    private static final int REQUEST_CODE = 100;
    private MediaProjection mediaProjection;
    private MediaCodec encoder;
    private VirtualDisplay virtualDisplay;
    private boolean isStreaming = false;
    private FrameReceiver frameReceiver;

    public interface FrameReceiver {
        void onFrame(ByteBuffer buffer, MediaCodec.BufferInfo info);
    }

    public void setFrameReceiver(FrameReceiver receiver) {
        this.frameReceiver = receiver;
    }

    public void requestCapture(Activity activity) {
        MediaProjectionManager manager = (MediaProjectionManager) activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        activity.startActivityForResult(manager.createScreenCaptureIntent(), REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data, Activity activity) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            MediaProjectionManager manager = (MediaProjectionManager) activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            mediaProjection = manager.getMediaProjection(resultCode, data);
            // 注册回调（Android 14+）
            mediaProjection.registerCallback(new MediaProjection.Callback() {
                @Override
                public void onStop() {
                    Log.d(TAG, "MediaProjection stopped");
                    stopStreaming();
                }
            }, null);
            startEncoding(activity);
        }
    }

    private void startEncoding(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        int width = 1280, height = 720, dpi = metrics.densityDpi;

        MediaFormat format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height);
        format.setInteger(MediaFormat.KEY_BIT_RATE, 2_000_000);
        format.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);

        try {
            encoder = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
            encoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            Surface inputSurface = encoder.createInputSurface();
            encoder.start();

            virtualDisplay = mediaProjection.createVirtualDisplay(
                "ScreenCast", width, height, dpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                inputSurface, null, null
            );
            isStreaming = true;
            new Thread(this::drainEncoder).start();
        } catch (Exception e) {
            Log.e(TAG, "Encoder init failed", e);
        }
    }

    private void drainEncoder() {
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        while (isStreaming) {
            int outputIndex = encoder.dequeueOutputBuffer(bufferInfo, 10000);
            if (outputIndex >= 0) {
                ByteBuffer outputBuffer = encoder.getOutputBuffer(outputIndex);
                if (frameReceiver != null) {
                    // 复制数据（因为outputBuffer将在release后失效）
                    ByteBuffer copy = ByteBuffer.allocate(bufferInfo.size);
                    copy.put(outputBuffer);
                    copy.flip();
                    frameReceiver.onFrame(copy, bufferInfo);
                }
                encoder.releaseOutputBuffer(outputIndex, false);
            } else if (outputIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                // 忽略
            } else if (outputIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                // 可以获取格式
            }
        }
    }

    public void stopStreaming() {
        isStreaming = false;
        if (virtualDisplay != null) {
            virtualDisplay.release();
            virtualDisplay = null;
        }
        if (encoder != null) {
            encoder.stop();
            encoder.release();
            encoder = null;
        }
        if (mediaProjection != null) {
            mediaProjection.stop();
            mediaProjection = null;
        }
        Log.d(TAG, "Streaming stopped");
    }
}

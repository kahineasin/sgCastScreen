package com.sellgirl.castScreen.android.send;

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
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

import java.nio.ByteBuffer;

public class ScreenCaptureManager {
    private static final String TAG = "ScreenCapture";
    private static final int REQUEST_CODE = 100;
    private MediaProjection mediaProjection;
    private MediaCodec encoder;
    private VirtualDisplay virtualDisplay;
    private boolean isStreaming = false;
    private OnEncodedFrameListener frameListener;

    public interface OnEncodedFrameListener {
        void onFrame(ByteBuffer buffer, MediaCodec.BufferInfo info);
    }

    public void setFrameListener(OnEncodedFrameListener listener) {
        this.frameListener = listener;
    }

    public void requestCapture(Activity activity) {
        MediaProjectionManager manager = (MediaProjectionManager) activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        activity.startActivityForResult(manager.createScreenCaptureIntent(), REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data, Activity activity) {
//        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            MediaProjectionManager manager = (MediaProjectionManager) activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
//            mediaProjection = manager.getMediaProjection(resultCode, data);
//            startEncoding(activity);
//        }

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            MediaProjectionManager manager = (MediaProjectionManager) activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            mediaProjection = manager.getMediaProjection(resultCode, data);

            // 【关键】在创建 VirtualDisplay 之前，先注册 MediaProjection 回调
            mediaProjection.registerCallback(new MediaProjection.Callback() {
                @Override
                public void onStop() {
                    Log.d(TAG, "MediaProjection stopped by system");
                    // 这里可以清理资源，例如停止编码
                }
            }, null); // 第二个参数为 Handler，null 表示使用主线程

            startEncoding(activity); // 这里会调用 createVirtualDisplay
        }
    }

    private void startEncoding(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        int width = 1280;   // 可根据实际屏幕调整，建议 1280x720
        int height = 720;
        int dpi = metrics.densityDpi;

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
                inputSurface, new VirtualDisplay.Callback() {
                    @Override
                    public void onPaused() {
                        super.onPaused();
                    }

                    @Override
                    public void onResumed() {
                        super.onResumed();
                    }

                    @Override
                    public void onStopped() {
                        super.onStopped();
                    }
                }, new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        SGDataHelper.getLog().print(msg);
                        return true;
                    }
                })
            );

            isStreaming = true;
            // 启动编码数据输出线程
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
                if (frameListener != null) {
                    // 发送帧数据给监听器（TSMuxer）
                    frameListener.onFrame(outputBuffer, bufferInfo);
                }
                encoder.releaseOutputBuffer(outputIndex, false);
            }
        }
    }

    public void stopStreaming() {
        isStreaming = false;
        if (virtualDisplay != null) virtualDisplay.release();
        if (encoder != null) {
            encoder.stop();
            encoder.release();
        }
        if (mediaProjection != null) mediaProjection.stop();
    }
}

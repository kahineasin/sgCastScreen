package com.sellgirl.castScreen.android;

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
import android.view.Surface;
import android.view.WindowManager;
import java.io.OutputStream;
import java.net.Socket;

public class ScreenCaptureManager {
    private static final int REQUEST_CODE_SCREEN_CAPTURE = 100;
    private MediaProjection mediaProjection;
    private MediaCodec encoder;
    private VirtualDisplay virtualDisplay;
    private Socket socket;
    private OutputStream outputStream;
    private boolean isStreaming = false;

    public void requestScreenCapture(Activity activity) {
        MediaProjectionManager manager = (MediaProjectionManager)
            activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        activity.startActivityForResult(manager.createScreenCaptureIntent(), REQUEST_CODE_SCREEN_CAPTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data, Activity activity) {
        if (requestCode == REQUEST_CODE_SCREEN_CAPTURE && resultCode == Activity.RESULT_OK) {
            MediaProjectionManager manager = (MediaProjectionManager)
                activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            mediaProjection = manager.getMediaProjection(resultCode, data);
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
            startSendingData();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void startSendingData() {
        new Thread(() -> {
            try {
                socket = new Socket("电视IP地址", 8080);
                outputStream = socket.getOutputStream();
                MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();

                while (isStreaming) {
                    int outputIndex = encoder.dequeueOutputBuffer(bufferInfo, 10000);
                    if (outputIndex >= 0) {
                        java.nio.ByteBuffer outputBuffer = encoder.getOutputBuffer(outputIndex);
                        byte[] data = new byte[bufferInfo.size];
                        outputBuffer.get(data);
                        // 发送数据（可添加4字节长度头）
                        outputStream.write(data);
                        outputStream.flush();
                        encoder.releaseOutputBuffer(outputIndex, false);
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
    }

    public void stopStreaming() {
        isStreaming = false;
        try { if (outputStream != null) outputStream.close(); } catch (Exception e) {}
        try { if (socket != null) socket.close(); } catch (Exception e) {}
        if (virtualDisplay != null) virtualDisplay.release();
        if (encoder != null) encoder.stop();
        if (mediaProjection != null) mediaProjection.stop();
    }
}

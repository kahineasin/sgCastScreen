package com.sellgirl.castScreen.android.sendimg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import fi.iki.elonen.NanoHTTPD;

public class StreamServer extends NanoHTTPD {
    private  final String TAG="StreamServer";
    private PipedOutputStream pos;
    private PipedInputStream pis;
    private boolean streaming = false;
    private byte[] staticImageData;

    public StreamServer() throws IOException {
        super(8080);
//        pos = new PipedOutputStream();
//        pis = new PipedInputStream(pos);

        generateStaticImage();

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }
    private void generateStaticImage(){
        Bitmap bitmap=Bitmap.createBitmap(1280,720,Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);


        // 渐变背景
        Paint paint = new Paint();
        for (int i = 0; i < 720; i++) {
            float hue = (i / 720f) * 360f;
            int color = Color.HSVToColor(new float[]{hue, 1.0f, 1.0f});
            paint.setColor(color);
            canvas.drawLine(0, i, 1280, i, paint);
        }

        // 画个白色大叉，明显表示这是测试图
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(20);
        canvas.drawLine(100, 100, 1180, 620, paint);
        canvas.drawLine(1180, 100, 100, 620, paint);

        // 画个红色边框
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(30);
        canvas.drawRect(15, 15, 1265, 705, paint);

        // 转成 JPEG 字节数组并缓存
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        staticImageData = baos.toByteArray();
        bitmap.recycle();
        Log.d(TAG, "Static image generated, size: " + staticImageData.length + " bytes");
    }

    public OutputStream getOutputStream() {
        return pos;
    }

//    @Override
//    public Response serve(IHTTPSession session) {
//        String uri = session.getUri();
//        if ("/stream.ts".equals(uri)) {
//            Response response = newFixedLengthResponse(Response.Status.OK, "video/mp2t", pis, -1);
//            response.addHeader("Connection", "keep-alive");
//            response.addHeader("Cache-Control", "no-cache");
//            return response;
//        }
//        return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "Not Found");
//    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Log.d(TAG, "Request: " + uri);

        // 提供静态图片
        if ("/image.jpg".equals(uri)) {
            Response response = newFixedLengthResponse(
                Response.Status.OK,
                "image/jpeg",
//                new BufferedInputStream(staticImageData)
                new ByteArrayInputStream(staticImageData),
                staticImageData.length
            );
            response.addHeader("Content-Length", String.valueOf(staticImageData.length));
            return response;
        }

        // 原来的 TS 流地址保留（先返回空数据，避免报错）
        if ("/stream.ts".equals(uri)) {
            Response response = newFixedLengthResponse(Response.Status.OK, "video/mp2t", pis, -1);
            response.addHeader("Connection", "keep-alive");
            response.addHeader("Cache-Control", "no-cache");
            return response;
        }

        return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "Not Found");
    }

    public void stopServer() {
        try { pos.close(); } catch (Exception e) {}
        try { pis.close(); } catch (Exception e) {}
        stop();
    }
}

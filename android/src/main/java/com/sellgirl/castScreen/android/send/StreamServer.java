package com.sellgirl.castScreen.android.send;

import android.os.Environment;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

import fi.iki.elonen.NanoHTTPD;

//import java.io.File;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class StreamServer extends NanoHTTPD {
    private static final String TAG = "StreamServer";
    private PipedOutputStream pos;
    private PipedInputStream pis;
    private StreamBroadcaster broadcaster;
    private boolean streaming = false;
    public StreamServer() throws IOException {
        super(8080);
        // 创建管道
        pos = new PipedOutputStream();
        pis = new PipedInputStream(pos, 1024 * 1024); // 1MB 缓冲区
        broadcaster = new StreamBroadcaster();
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

    // 供 TSMuxer 写入数据
    @Deprecated
    public PipedOutputStream getOutputStream() {
        return pos;
    }

    public StreamBroadcaster getBroadcaster() {
        return broadcaster;
    }

//    @Override
//    public Response serve(IHTTPSession session) {
//        SGDataHelper.getLog().print("Request URI: "+session.getUri());
//        String uri = session.getUri();
//        if ("/stream.ts".equals(uri)) {
//            Response response = newFixedLengthResponse(Response.Status.OK, "video/mp2t", pis, -1);
//            response.addHeader("Connection", "keep-alive");
//            response.addHeader("Cache-Control", "no-cache");
//            return response;
//        }
//
//        //测试投文件成功
////        // 在 serve 中
////        if ("/stream.ts".equals(uri)) {
////////            File file = new File("/sdcard/test.mp4");
////////            File file = new File("/sdcard/Pictures/gensin/1.mp4");
////
////        //权限问题?
//////            File picsDir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//////            File file = new File(picsDir,"gensin/1.mp4");
////
////            //成功,文件在"此电脑\WP33 Pro\内部共享存储空间\Android\data\com.sellgirl.castScreen\files\Download\1.mp4"
////            File file= Gdx.files.external("Download/1.mp4").file();
////
////            FileInputStream fis = null;
////            try {
////                fis = new FileInputStream(file);
////            } catch (FileNotFoundException e) {
////                //throw new RuntimeException(e);
////                SGDataHelper.getLog().printException(e,TAG);
////            }
////            return newChunkedResponse(Response.Status.OK, "video/mp2t", fis);
////        }
//        return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "Not Found");
//    }
//    @Override
//    public Response serve(IHTTPSession session) {
//        String uri = session.getUri();
//        Log.d(TAG, "Request: " + uri);
//
//        if ("/stream.ts".equals(uri)) {
//            // 从你的输出流（PipedOutputStream）创建 InputStream
//            // 假设你有一个 PipedInputStream 连接到 TSMuxer 的输出
//            // 如果 TSMuxer 直接写入 streamServer.getOutputStream()，那需要改设计
//            // 更稳健：用 PipedInputStream/PipedOutputStream
////            InputStream is = new PipedInputStream() /* 需连接到你写入数据的流 */;
//            Response response = newChunkedResponse(Response.Status.OK, "video/mp2t", pis);
//            response.addHeader("Connection", "keep-alive");
////            response.addHeader("Cache-Control", "no-cache");
//            response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
//            return response;
//        }
//        return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "Not Found");
//    }


@Override
public Response serve(IHTTPSession session) {
    String uri = session.getUri();
    if ("/stream.ts".equals(uri)) {
        try {
            // 为这个请求创建一个全新的管道对
            PipedOutputStream pos = new PipedOutputStream();
            PipedInputStream pis = new PipedInputStream(pos, 1024 * 1024); // 1MB 缓冲

            // 将这个客户端的输出流加入广播器
            broadcaster.addClient(pos);

            // 返回分块响应
            Response response = newChunkedResponse(Response.Status.OK, "video/mp2t", pis);
            response.addHeader("Connection", "keep-alive");
            response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            return response;
        } catch (IOException e) {
            Log.e(TAG, "Failed to create pipe", e);
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain", "Pipe creation failed");
        }
    }
    return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "Not Found");
}
    public void stopServer() {
        try {
            if (pos != null) pos.close();
        } catch (IOException e) { /* ignore */ }
        try {
            if (pis != null) pis.close();
        } catch (IOException e) { /* ignore */ }

        if (broadcaster != null) {
            broadcaster.closeAll();
        }
        stop();
    }
}

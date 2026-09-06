package com.sellgirl.castScreen.android.send2;


import android.util.Log;
import java.io.IOException;
import fi.iki.elonen.NanoHTTPD;

public class StreamServer extends NanoHTTPD {
    private static final String TAG = "StreamServer";
    private final RingBuffer ringBuffer;

    public StreamServer(RingBuffer ringBuffer) throws IOException {
        super(8080);
        this.ringBuffer = ringBuffer;
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        Log.d(TAG, "StreamServer started on port 8080");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        if ("/stream.ts".equals(uri)) {
            RingBufferInputStream inputStream = new RingBufferInputStream(ringBuffer);
            Response response = newChunkedResponse(Response.Status.OK, "video/mp2t", inputStream);
            response.addHeader("Connection", "keep-alive");
            response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            Log.d(TAG, "New client connected");
            return response;
        }
        return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "Not Found");
    }

    public void stopServer() {
        stop();
    }
}

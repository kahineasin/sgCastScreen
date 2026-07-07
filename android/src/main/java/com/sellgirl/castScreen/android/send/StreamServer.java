package com.sellgirl.castScreen.android.send;

import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class StreamServer extends NanoHTTPD {
    private PipedOutputStream pos;
    private PipedInputStream pis;
    private boolean streaming = false;

    public StreamServer() throws IOException {
        super(8080);
        pos = new PipedOutputStream();
        pis = new PipedInputStream(pos);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

    public OutputStream getOutputStream() {
        return pos;
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
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

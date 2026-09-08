package com.sellgirl.castScreen.android.send;

import android.media.MediaCodec;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface ITSMuxer {
    void writeFrame(ByteBuffer buffer, MediaCodec.BufferInfo info) throws IOException;
     void close() throws IOException;
}

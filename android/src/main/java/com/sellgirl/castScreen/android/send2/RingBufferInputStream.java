package com.sellgirl.castScreen.android.send2;


import java.io.IOException;
import java.io.InputStream;

/**
 * 从RingBuffer中读取数据的InputStream，每个实例绑定一个Reader。
 * 非阻塞，若暂无数据则等待（可配置超时）。
 */
public class RingBufferInputStream extends InputStream {
    private RingBuffer.Reader reader;
    private byte[] currentPacket = null;
    private int offset = 0;
    private boolean closed = false;

    public RingBufferInputStream(RingBuffer ringBuffer) {
        this.reader = ringBuffer.newReader();
    }

    @Override
    public int read() throws IOException {
        byte[] b = new byte[1];
        if (read(b, 0, 1) > 0) {
            return b[0] & 0xFF;
        }
        return -1;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (closed) return -1;
        if (len <= 0) return 0;

        // 如果当前包为空或已读完，获取下一个包
        while (currentPacket == null || offset >= currentPacket.length) {
            byte[] packet = reader.read();
            if (packet == null) {
                // 无新数据，等待10ms
                try { Thread.sleep(10); } catch (InterruptedException e) { return -1; }
                continue;
            }
            currentPacket = packet;
            offset = 0;
        }

        int copyLen = Math.min(len, currentPacket.length - offset);
        System.arraycopy(currentPacket, offset, b, off, copyLen);
        offset += copyLen;
        return copyLen;
    }

    @Override
    public void close() throws IOException {
        closed = true;
        reader.close();
    }
}

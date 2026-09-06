package com.sellgirl.castScreen.android.send2;


import android.media.MediaCodec;
import android.util.Log;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 单例TS复用器，将H.264帧封装为MPEG-TS并写入RingBuffer。
 */
public class TSMuxer implements ScreenCaptureManager.FrameReceiver {
    private static final String TAG = "TSMuxer";
    private static final int TS_PACKET_SIZE = 188;
    private static final int PAT_PID = 0x0000;
    private static final int PMT_PID = 0x1000;
    private static final int VIDEO_PID = 0x0100;
    private static final int PCR_PID = VIDEO_PID;

    private RingBuffer ringBuffer;
    private byte[] sps = null;
    private byte[] pps = null;
    private boolean firstFrame = true;
    private int packetCounter = 0;

    // 连续性计数器（每个PID独立）
    private int continuityCounterPAT = 0;
    private int continuityCounterPMT = 0;
    private int continuityCounterVideo = 0;

    private static TSMuxer instance;

    private TSMuxer(RingBuffer ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public static synchronized TSMuxer getInstance(RingBuffer ringBuffer) {
        if (instance == null) {
            instance = new TSMuxer(ringBuffer);
        }
        return instance;
    }

    /**
     * 接收一帧H.264数据（含起始码），复用为TS包并写入RingBuffer
     */
    public void onFrame(ByteBuffer buffer, MediaCodec.BufferInfo info) //throws IOException
    {
        byte[] data = new byte[info.size];
        buffer.get(data);

        // 提取SPS/PPS（如果尚未）
        if (sps == null || pps == null) {
            extractSPSPPS(data);
            if (sps != null && pps != null) {
                Log.d(TAG, "SPS/PPS extracted: sps=" + sps.length + ", pps=" + pps.length);
            }
        }

        if (sps == null || pps == null) {
            return; // 等待关键帧
        }

        boolean isKeyFrame = (info.flags & MediaCodec.BUFFER_FLAG_KEY_FRAME) != 0;

        // 关键帧或第一帧时，先发送SPS+PPS
        if (isKeyFrame || firstFrame) {
            byte[] spsPpsData = new byte[sps.length + pps.length];
            System.arraycopy(sps, 0, spsPpsData, 0, sps.length);
            System.arraycopy(pps, 0, spsPpsData, sps.length, pps.length);
            writePES(spsPpsData, 0, spsPpsData.length, true);
        }

        // 发送视频帧
        writePES(data, 0, data.length, false);

        // 首帧发送PAT/PMT，之后定期发送
        if (firstFrame) {
            writePAT();
            writePMT();
            firstFrame = false;
        }
        if (++packetCounter % 30 == 0) {
            writePAT();
            writePMT();
        }
    }

    private void writePAT() {
        byte[] pat = new byte[TS_PACKET_SIZE];
        pat[0] = 0x47;
        pat[1] = (byte) (0x40 | (PAT_PID >> 8));
        pat[2] = (byte) (PAT_PID & 0xFF);
        pat[3] = (byte) (0x10 | (continuityCounterPAT & 0x0F));
        continuityCounterPAT = (continuityCounterPAT + 1) & 0x0F;

        pat[4] = 0x00;
        int offset = 5;
        pat[offset++] = 0x00; // table_id
        pat[offset++] = (byte) 0xB0;
        pat[offset++] = 0x0D;
        pat[offset++] = 0x00;
        pat[offset++] = 0x01;
        pat[offset++] = (byte) 0xC1;
        pat[offset++] = 0x00;
        pat[offset++] = 0x00;
        pat[offset++] = 0x00;
        pat[offset++] = 0x01;
        pat[offset++] = (byte) (0xE0 | (PMT_PID >> 8));
        pat[offset++] = (byte) (PMT_PID & 0xFF);
        // CRC32 dummy
        pat[offset++] = 0x00;
        pat[offset++] = 0x00;
        pat[offset++] = 0x00;
        pat[offset++] = 0x00;
        ringBuffer.write(pat);
    }

    private void writePMT() {
        byte[] pmt = new byte[TS_PACKET_SIZE];
        pmt[0] = 0x47;
        pmt[1] = (byte) (0x40 | (PMT_PID >> 8));
        pmt[2] = (byte) (PMT_PID & 0xFF);
        pmt[3] = (byte) (0x10 | (continuityCounterPMT & 0x0F));
        continuityCounterPMT = (continuityCounterPMT + 1) & 0x0F;

        pmt[4] = 0x00;
        int offset = 5;
        pmt[offset++] = 0x02;
        pmt[offset++] = (byte) 0xB0;
        pmt[offset++] = 0x12;
        pmt[offset++] = 0x00;
        pmt[offset++] = 0x01;
        pmt[offset++] = (byte) 0xC1;
        pmt[offset++] = 0x00;
        pmt[offset++] = 0x00;
        pmt[offset++] = (byte) (0xE0 | (PCR_PID >> 8));
        pmt[offset++] = (byte) (PCR_PID & 0xFF);
        pmt[offset++] = 0x00;
        pmt[offset++] = 0x00;
        pmt[offset++] = 0x1B; // H.264
        pmt[offset++] = (byte) (0xE0 | (VIDEO_PID >> 8));
        pmt[offset++] = (byte) (VIDEO_PID & 0xFF);
        pmt[offset++] = 0x00;
        pmt[offset++] = 0x00;
        // CRC32 dummy
        pmt[offset++] = 0x00;
        pmt[offset++] = 0x00;
        pmt[offset++] = 0x00;
        pmt[offset++] = 0x00;
        ringBuffer.write(pmt);
    }

    private void writePES(byte[] data, int offset, int length, boolean isConfig) {
        int pesHeaderLen = isConfig ? 9 : 14;
        int pesLen = length + pesHeaderLen;
        byte[] pes = new byte[pesLen];

        pes[0] = 0x00;
        pes[1] = 0x00;
        pes[2] = 0x01;
        pes[3] = (byte) 0xE0;
        int packetLength = length + (isConfig ? 3 : 9);
        pes[4] = (byte) (packetLength >> 8);
        pes[5] = (byte) (packetLength & 0xFF);

        if (isConfig) {
            pes[6] = 0x00;
            pes[7] = 0x00;
            pes[8] = 0x00;
        } else {
            pes[6] = (byte) 0x80;
            pes[7] = (byte) 0x80;
            pes[8] = (byte) 0x05;
            long ptsVal = (System.currentTimeMillis() * 90) % 0x1FFFFFFFFL;
            pes[9]  = (byte) (0x20 | ((ptsVal >> 29) & 0x07));
            pes[10] = (byte) ((ptsVal >> 22) & 0xFF);
            pes[11] = (byte) (0x80 | ((ptsVal >> 14) & 0xFF));
            pes[12] = (byte) ((ptsVal >> 7) & 0xFF);
            pes[13] = (byte) (0x80 | (ptsVal & 0x7F));
        }

        System.arraycopy(data, offset, pes, isConfig ? 9 : 14, length);

        // 将PES打包为TS包（VIDEO_PID）
        int packetIndex = 0;
        boolean isFirst = true;
        while (packetIndex < pes.length) {
            byte[] ts = new byte[TS_PACKET_SIZE];
            ts[0] = 0x47;
            ts[1] = (byte) (0x40 | (VIDEO_PID >> 8));
            ts[2] = (byte) (VIDEO_PID & 0xFF);
            ts[3] = (byte) (0x10 | (isFirst ? 0x20 : 0x00) | (continuityCounterVideo & 0x0F));
            continuityCounterVideo = (continuityCounterVideo + 1) & 0x0F;
            isFirst = false;

            int payloadSize = Math.min(TS_PACKET_SIZE - 4, pes.length - packetIndex);
            System.arraycopy(pes, packetIndex, ts, 4, payloadSize);
            for (int i = 4 + payloadSize; i < TS_PACKET_SIZE; i++) {
                ts[i] = (byte) 0xFF;
            }
            ringBuffer.write(ts);
            packetIndex += payloadSize;
        }
    }

    private void extractSPSPPS(byte[] data) {
        for (int i = 0; i < data.length - 4; i++) {
            if (data[i] == 0 && data[i+1] == 0 && data[i+2] == 0 && data[i+3] == 1) {
                int nalType = data[i+4] & 0x1F;
                if (nalType == 7) {
                    int start = i;
                    int end = findNextStartCode(data, i+4);
                    if (end == -1) end = data.length;
                    sps = new byte[end - start];
                    System.arraycopy(data, start, sps, 0, sps.length);
                } else if (nalType == 8) {
                    int start = i;
                    int end = findNextStartCode(data, i+4);
                    if (end == -1) end = data.length;
                    pps = new byte[end - start];
                    System.arraycopy(data, start, pps, 0, pps.length);
                }
            }
        }
    }

    private int findNextStartCode(byte[] data, int start) {
        for (int i = start; i < data.length - 3; i++) {
            if (data[i] == 0 && data[i+1] == 0 && data[i+2] == 0 && data[i+3] == 1) return i;
            if (data[i] == 0 && data[i+1] == 0 && data[i+2] == 1) return i;
        }
        return -1;
    }
}

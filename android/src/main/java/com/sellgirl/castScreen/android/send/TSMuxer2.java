package com.sellgirl.castScreen.android.send;

import android.media.MediaCodec;
import android.util.Log;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class TSMuxer2 {
    private static final String TAG = "TSMuxer";
    private static final int TS_PACKET_SIZE = 188;
    private static final int PAT_PID = 0x0000;
    private static final int PMT_PID = 0x1000;
    private static final int VIDEO_PID = 0x0100;
    private static final int PCR_PID = VIDEO_PID;

    private OutputStream output;
    private int packetCounter = 0;
    private byte[] sps = null;
    private byte[] pps = null;
    private boolean firstFrame = true;
    private long ptsBase = System.currentTimeMillis() * 90; // 90kHz 基准

    public TSMuxer2(OutputStream output) {
        this.output = output;
    }
    // 在 writeFrame 开头添加计数器
    private int frameCount = 0;
    /**
     * 传入一帧 H.264 数据（必须包含起始码 00 00 00 01 或 00 00 01）
     */
    public void writeFrame(ByteBuffer buffer, MediaCodec.BufferInfo info) throws IOException {
        if(hasErr){return;}
        try {

            frameCount++;
            if (frameCount % 30 == 0) { // 每30帧打印一次
                Log.d(TAG, "Frame " + frameCount + ", size=" + info.size + ", flags=" + info.flags);
            }

            byte[] data = new byte[info.size];
            buffer.get(data);

            // 尝试提取 SPS/PPS（从关键帧中）
            if (sps == null || pps == null) {
                extractSPSPPS(data);
                if (sps != null && pps != null) {
                    Log.d(TAG, "SPS/PPS extracted: sps=" + sps.length + ", pps=" + pps.length);
                }
            }

            // 如果还没有 SPS/PPS，跳过该帧（无法解码）
            if (sps == null || pps == null) {
                // 如果是关键帧但没有 SPS/PPS，可能是帧格式问题，尝试继续
                return;
            }

            // 第一帧先发送 PAT/PMT
            if (firstFrame) {
                writePAT();
                writePMT();
                firstFrame = false;
            }

            // 封装 PES 并输出
            writePES(data, 0, data.length);

            // 每 100 个 TS 包重发 PAT/PMT（保持兼容）
            if (++packetCounter % 100 == 0) {
                writePAT();
                writePMT();
            }
        }catch (Exception e){
            SGDataHelper.getLog().printException(e,TAG);
            hasErr=true;
        }
    }
    private boolean hasErr=false;
    private void writePAT() throws IOException {
        byte[] pat = new byte[TS_PACKET_SIZE];
        pat[0] = 0x47; // sync byte
        pat[1] = (byte) (0x40 | (PAT_PID >> 8));
        pat[2] = (byte) (PAT_PID & 0xFF);
        pat[3] = (byte) 0x10; // payload unit start indicator
        pat[4] = 0x00; // pointer_field
        int offset = 5;
        // table_id = 0x00
        pat[offset++] = 0x00;
        // section_syntax_indicator=1, private_indicator=0, section_length=13
        pat[offset++] = (byte) 0xB0;
        pat[offset++] = 0x0D;
        // transport_stream_id = 1
        pat[offset++] = 0x00;
        pat[offset++] = 0x01;
        // version_number=0, current_next_indicator=1
        pat[offset++] = (byte) 0xC1;
        pat[offset++] = 0x00; // section_number
        pat[offset++] = 0x00; // last_section_number
        // program_number = 1
        pat[offset++] = 0x00;
        pat[offset++] = 0x01;
        // program_map_PID = PMT_PID
        pat[offset++] = (byte) (0xE0 | (PMT_PID >> 8));
        pat[offset++] = (byte) (PMT_PID & 0xFF);
        // CRC32 (dummy)
        pat[offset++] = 0x00;
        pat[offset++] = 0x00;
        pat[offset++] = 0x00;
        pat[offset++] = 0x00;
        output.write(pat);
    }

    private void writePMT() throws IOException {
        byte[] pmt = new byte[TS_PACKET_SIZE];
        pmt[0] = 0x47;
        pmt[1] = (byte) (0x40 | (PMT_PID >> 8));
        pmt[2] = (byte) (PMT_PID & 0xFF);
        pmt[3] = (byte) 0x10;
        pmt[4] = 0x00;
        int offset = 5;
        // table_id = 0x02
        pmt[offset++] = 0x02;
        // section_syntax_indicator=1, private_indicator=0, section_length=18
        pmt[offset++] = (byte) 0xB0;
        pmt[offset++] = 0x12;
        // program_number = 1
        pmt[offset++] = 0x00;
        pmt[offset++] = 0x01;
        // version_number=0, current_next=1
        pmt[offset++] = (byte) 0xC1;
        pmt[offset++] = 0x00;
        pmt[offset++] = 0x00;
        // PCR PID
        pmt[offset++] = (byte) (0xE0 | (PCR_PID >> 8));
        pmt[offset++] = (byte) (PCR_PID & 0xFF);
        // program_info_length = 0
        pmt[offset++] = 0x00;
        pmt[offset++] = 0x00;
        // 流1: H.264 video
        pmt[offset++] = 0x1B; // stream_type H.264
        pmt[offset++] = (byte) (0xE0 | (VIDEO_PID >> 8));
        pmt[offset++] = (byte) (VIDEO_PID & 0xFF);
        // ES_info_length = 0
        pmt[offset++] = 0x00;
        pmt[offset++] = 0x00;
        // CRC32 dummy
        pmt[offset++] = 0x00;
        pmt[offset++] = 0x00;
        pmt[offset++] = 0x00;
        pmt[offset++] = 0x00;
        output.write(pmt);
    }

    private void writePES(byte[] frameData, int offset, int length) throws IOException {
        int pesLen = length + 14; // PES header size (13 bytes + 1 padding? 实际是14)
        byte[] pes = new byte[pesLen];
        // 起始码
        pes[0] = 0x00;
        pes[1] = 0x00;
        pes[2] = 0x01;
        pes[3] = (byte) 0xE0; // stream_id = video
        // PES 包长度 (length + 9)
        int pesPacketLength = length + 9;
        pes[4] = (byte) (pesPacketLength >> 8);
        pes[5] = (byte) (pesPacketLength & 0xFF);
        // 标志位 (PTS/DTS present)
        pes[6] = (byte) 0x80;
        pes[7] = (byte) 0x80;
        pes[8] = (byte) 0x05; // PES header data length = 5 bytes
        // 计算时间戳（简单递增）
        long ptsVal = (System.currentTimeMillis() * 90) % 0x1FFFFFFFFL;
        pes[9]  = (byte) (0x20 | ((ptsVal >> 29) & 0x07));
        pes[10] = (byte) ((ptsVal >> 22) & 0xFF);
        pes[11] = (byte) (0x80 | ((ptsVal >> 14) & 0xFF));
        pes[12] = (byte) ((ptsVal >> 7) & 0xFF);
        pes[13] = (byte) (0x80 | (ptsVal & 0x7F));
        // 拷贝视频数据
        System.arraycopy(frameData, offset, pes, 14, length);

        // 将 PES 包拆分为 TS 包（每个 TS 包 188 字节）
        int packetIndex = 0;
        while (packetIndex < pes.length) {
            byte[] ts = new byte[TS_PACKET_SIZE];
            ts[0] = 0x47;
            ts[1] = (byte) (0x40 | (VIDEO_PID >> 8));
            ts[2] = (byte) (VIDEO_PID & 0xFF);
            ts[3] = (byte) (0x10 | (packetIndex == 0 ? 0x20 : 0x00)); // payload start indicator
            int payloadSize = Math.min(TS_PACKET_SIZE - 4, pes.length - packetIndex);
            System.arraycopy(pes, packetIndex, ts, 4, payloadSize);
            // 剩余填充 0xFF
            for (int i = 4 + payloadSize; i < TS_PACKET_SIZE; i++) {
                ts[i] = (byte) 0xFF;
            }
            output.write(ts);
            packetIndex += payloadSize;
        }
    }

    private void extractSPSPPS(byte[] data) {
        // 查找 NAL 起始码
        for (int i = 0; i < data.length - 4; i++) {
            if (data[i] == 0 && data[i+1] == 0 && data[i+2] == 0 && data[i+3] == 1) {
                int nalType = data[i+4] & 0x1F;
                if (nalType == 7) { // SPS
                    int start = i;
                    int end = findNextStartCode(data, i+4);
                    if (end == -1) end = data.length;
                    sps = new byte[end - start];
                    System.arraycopy(data, start, sps, 0, sps.length);
                } else if (nalType == 8) { // PPS
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

    public void close() throws IOException {
        output.close();
    }
}

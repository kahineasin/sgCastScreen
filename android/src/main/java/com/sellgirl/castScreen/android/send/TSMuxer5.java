package com.sellgirl.castScreen.android.send;

import android.media.MediaCodec;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;

public class TSMuxer5 {
    private static final String TAG = "TSMuxer";
    private static final int TS_PACKET_SIZE = 188;
    private static final int PAT_PID = 0x0000;
    private static final int PMT_PID = 0x0100;
    private static final int VIDEO_PID = 0x0101;
    private static final int PCR_PID = VIDEO_PID;

    private final OutputStream outputStream;
    private byte[] sps;
    private byte[] pps;
    private boolean hasFormat = false;
    private int packetCounter = 0;

    // 连续性计数器
    private int ccPat = 0;
    private int ccPmt = 0;
    private int ccVideo = 0;

    public TSMuxer5(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void writeFrame(ByteBuffer buffer, MediaCodec.BufferInfo info) throws IOException {
        byte[] data = new byte[info.size];
        buffer.get(data);

        // 提取 SPS/PPS
        if (sps == null || pps == null) {
            extractSPSPPS(data);
            if (sps != null && pps != null) {
                Log.d(TAG, "SPS/PPS extracted");
            } else {
                return;
            }
        }

        // 首帧前发送 PAT/PMT
        if (!hasFormat) {
            writePAT();
            writePMT();
            hasFormat = true;
        }

        // 关键帧前发送 SPS+PPS
        boolean isKeyFrame = (info.flags & MediaCodec.BUFFER_FLAG_KEY_FRAME) != 0;
        if (isKeyFrame) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(sps);
            baos.write(pps);
            byte[] configData = baos.toByteArray();
            writePES(configData, 0, configData.length, false); // 无 PTS
        }

        // 发送视频帧
        writePES(data, 0, data.length, true);

        // 定期重发 PAT/PMT
        if (++packetCounter % 50 == 0) {
            writePAT();
            writePMT();
        }
    }

    // ---------- 计算 CRC32 辅助 ----------
    private int calculateCRC32(byte[] data, int offset, int length) {
        CRC32 crc = new CRC32();
        crc.update(data, offset, length);
        return (int) (crc.getValue() ^ 0xFFFFFFFFL);
    }

    // ---------- 生成 PAT ----------
    private void writePAT() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(TS_PACKET_SIZE);
        DataOutputStream dos = new DataOutputStream(baos);

        // TS 头
        dos.writeByte(0x47);
        dos.writeShort((PAT_PID) | 0x4000);
        dos.writeByte(0x10 | (ccPat++ & 0x0F));

        // PSI 段
        dos.writeByte(0x00); // pointer_field
        dos.writeByte(0x00); // table_id
        dos.writeShort(0xB00D); // section_length = 13
        dos.writeShort(0x0001); // transport_stream_id
        dos.writeByte(0xC1);   // version=0, current_next=1
        dos.writeByte(0x00);   // section_number
        dos.writeByte(0x00);   // last_section_number
        dos.writeShort(0x0001); // program_number
        dos.writeShort(PMT_PID | 0xE000); // program_map_PID

        // 计算 CRC32（从 table_id 到 program_map_PID 结束）
        byte[] patData = baos.toByteArray();
        // 此时 patData 包含 TS 头 + PSI 数据，但 CRC 只针对 PSI 段（从 pointer_field 之后到 CRC 之前）
        // 更简单：先写入不含 CRC 的 PSI 段，计算 CRC，再追加
        // 我们重新构造：先构建不含 CRC 的 PSI 段，计算 CRC，然后写入完整包
        ByteArrayOutputStream psiStream = new ByteArrayOutputStream();
        DataOutputStream psiOut = new DataOutputStream(psiStream);
        psiOut.writeByte(0x00); // pointer_field
        psiOut.writeByte(0x00); // table_id
        psiOut.writeShort(0xB00D);
        psiOut.writeShort(0x0001);
        psiOut.writeByte(0xC1);
        psiOut.writeByte(0x00);
        psiOut.writeByte(0x00);
        psiOut.writeShort(0x0001);
        psiOut.writeShort(PMT_PID | 0xE000);
        byte[] psiData = psiStream.toByteArray(); // 长度 = 1+1+2+2+1+1+1+2+2 = 13
        int crc = calculateCRC32(psiData, 0, psiData.length);

        // 重新构建完整的 TS 包
        baos.reset();
        dos = new DataOutputStream(baos);
        dos.writeByte(0x47);
        dos.writeShort((PAT_PID) | 0x4000);
        dos.writeByte(0x10 | (ccPat & 0x0F)); // 注意 cc 已递增，但不影响
        dos.write(psiData);
        dos.writeInt(crc);

        // 填充到 188 字节
        while (baos.size() < TS_PACKET_SIZE) {
            dos.writeByte(0xFF);
        }
        outputStream.write(baos.toByteArray());
    }

    // ---------- 生成 PMT ----------
    private void writePMT() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(TS_PACKET_SIZE);
        DataOutputStream dos = new DataOutputStream(baos);

        // 构造 PSI 段（不含 TS 头）
        ByteArrayOutputStream psiStream = new ByteArrayOutputStream();
        DataOutputStream psiOut = new DataOutputStream(psiStream);
        psiOut.writeByte(0x00); // pointer_field
        psiOut.writeByte(0x02); // table_id
        psiOut.writeShort(0xB01A); // section_length = 18
        psiOut.writeShort(0x0001); // program_number
        psiOut.writeByte(0xC1);
        psiOut.writeByte(0x00);
        psiOut.writeByte(0x00);
        psiOut.writeShort(PCR_PID | 0xE000);
        psiOut.writeShort(0x0000); // program_info_length
        psiOut.writeByte(0x1B);    // stream_type H.264
        psiOut.writeShort(VIDEO_PID | 0xE000);
        psiOut.writeShort(0x0000); // ES_info_length
        byte[] psiData = psiStream.toByteArray(); // 长度 = 1+1+2+2+1+1+1+2+2+1+2+2 = 18
        int crc = calculateCRC32(psiData, 0, psiData.length);

        // 构建 TS 包
        baos.reset();
        dos = new DataOutputStream(baos);
        dos.writeByte(0x47);
        dos.writeShort(PMT_PID | 0x4000);
        dos.writeByte(0x10 | (ccPmt++ & 0x0F));
        dos.write(psiData);
        dos.writeInt(crc);

        while (baos.size() < TS_PACKET_SIZE) {
            dos.writeByte(0xFF);
        }
        outputStream.write(baos.toByteArray());
    }

    // ---------- 生成 PES 并打包 TS ----------
    private void writePES(byte[] data, int offset, int length, boolean withPts) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // PES 头
        dos.writeInt(0x00000001); // 起始码
        dos.writeByte(0xE0);      // stream_id: video

        int ptsSize = withPts ? 5 : 0;
        int pesHeaderLen = 3 + ptsSize;
        int packetLen = length + pesHeaderLen;
        if (packetLen > 0xFFFF) packetLen = 0;

        dos.writeShort(packetLen);
        dos.writeByte(0x80); // PTS/DTS 标志
        dos.writeByte(0x80);
        if (withPts) {
            dos.writeByte(0x05); // PES_header_data_length = 5
            long pts = (System.currentTimeMillis() * 90) % 0x1FFFFFFFFL;
            dos.writeByte((byte) (0x20 | ((pts >> 29) & 0x07)));
            dos.writeByte((byte) ((pts >> 22) & 0xFF));
            dos.writeByte((byte) (0x80 | ((pts >> 14) & 0xFF)));
            dos.writeByte((byte) ((pts >> 7) & 0xFF));
            dos.writeByte((byte) (0x80 | (pts & 0x7F)));
        } else {
            dos.writeByte(0x00); // 无 PTS/DTS
        }

        dos.write(data, offset, length);

        byte[] pesData = baos.toByteArray();
        int pos = 0;
        boolean firstPacket = true;
        while (pos < pesData.length) {
            byte[] ts = new byte[TS_PACKET_SIZE];
            ts[0] = 0x47;
            ts[1] = (byte) (0x40 | (VIDEO_PID >> 8));
            ts[2] = (byte) (VIDEO_PID & 0xFF);
            int afc = 0x10; // payload only
            if (firstPacket) {
                afc |= 0x20; // payload_unit_start_indicator
                firstPacket = false;
            }
            ts[3] = (byte) (afc | (ccVideo++ & 0x0F));

            int payloadSize = Math.min(TS_PACKET_SIZE - 4, pesData.length - pos);
            System.arraycopy(pesData, pos, ts, 4, payloadSize);
            for (int i = 4 + payloadSize; i < TS_PACKET_SIZE; i++) {
                ts[i] = (byte) 0xFF;
            }
            outputStream.write(ts);
            pos += payloadSize;
        }
    }

    // ---------- 提取 SPS/PPS ----------
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

    public void close() throws IOException {
        outputStream.close();
    }
}

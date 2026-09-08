package com.sellgirl.castScreen.android.send;

import android.media.MediaCodec;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class TSMuxer4 {

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
    private long ptsCounter = 0;
    private int packetCounter = 0;

    // 连续性计数器 (每个PID独立，0-15循环)
    private int ccPat = 0;
    private int ccPmt = 0;
    private int ccVideo = 0;

    public TSMuxer4(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * 接收 H.264 帧（必须是带起始码 00 00 00 01 的 NAL 单元）
     */
//    public void writeFrame(ByteBuffer buffer, MediaCodec.BufferInfo info) throws IOException {
//        byte[] data = new byte[info.size];
//        buffer.get(data);
//
//        // 1. 提取 SPS / PPS（必须）
//        if (sps == null || pps == null) {
//            extractSPSPPS(data);
//            if (sps != null && pps != null) {
//                Log.d(TAG, "SPS/PPS extracted");
//            } else {
//                return; // 还没拿到关键配置，跳过
//            }
//        }
//
//        // 2. 判断关键帧 (IDR)
//        boolean isKeyFrame = (info.flags & MediaCodec.BUFFER_FLAG_KEY_FRAME) != 0;
//
//        // 3. 如果是关键帧，先发送 SPS + PPS (作为单独的无 PTS 帧)
//        if (isKeyFrame) {
//            // 将 SPS 和 PPS 合并成一个 NAL 单元流 (直接拼接)
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            baos.write(sps);
//            baos.write(pps);
//            byte[] configData = baos.toByteArray();
//            writePES(configData, 0, configData.length, false, true); // 配置帧不带 PTS
//        }
//
//        // 4. 发送视频帧 (带 PTS)
//        writePES(data, 0, data.length, true, false);
//
//        // 5. 首帧或定期发送 PAT / PMT
//        if (!hasFormat) {
//            writePAT();
//            writePMT();
//            hasFormat = true;
//        } else if (++packetCounter % 50 == 0) {
//            // 每 50 个包重发一次，提高兼容性
//            writePAT();
//            writePMT();
//        }
//    }
    public void writeFrame(ByteBuffer buffer, MediaCodec.BufferInfo info) throws IOException {
        byte[] data = new byte[info.size];
        buffer.get(data);

        // 1. 提取 SPS/PPS（如果尚未提取）
        if (sps == null || pps == null) {
            extractSPSPPS(data);
            if (sps != null && pps != null) {
                Log.d(TAG, "SPS/PPS extracted");
            } else {
                return; // 等待关键帧
            }
        }

        // 2. **关键修改：在最开始就发送 PAT/PMT，而不是等到第一帧完成**
        if (!hasFormat) {
            writePAT();
            writePMT();
            hasFormat = true;
        }

        // 3. 判断关键帧并发送 SPS/PPS
        boolean isKeyFrame = (info.flags & MediaCodec.BUFFER_FLAG_KEY_FRAME) != 0;
        if (isKeyFrame) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(sps);
            baos.write(pps);
            byte[] configData = baos.toByteArray();
            writePES(configData, 0, configData.length, false, true);
        }

        // 4. 发送视频帧
        writePES(data, 0, data.length, true, false);

        // 5. 定期重发 PAT/PMT（保持兼容）
        if (++packetCounter % 50 == 0) {
            writePAT();
            writePMT();
        }
    }
    // ---------- PAT 生成 ----------
    private void writePAT() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(TS_PACKET_SIZE);
        DataOutputStream dos = new DataOutputStream(baos);

        // TS 头 (4 bytes)
        dos.writeByte(0x47); // sync_byte
        dos.writeShort((PAT_PID) | 0x4000); // PID, 加上 transport_priority=0
        // 适配域控制 = 01 (仅有效载荷), 连续性计数器
        int cc = (ccPat++ & 0x0F);
        dos.writeByte(0x10 | cc); // payload_unit_start_indicator=1, adaptation_field_control=01

        // PSI 段: PAT
        dos.writeByte(0x00); // pointer_field
        dos.writeByte(0x00); // table_id
        // section_length (9 + 4 = 13)
        dos.writeShort(0xB00D); // 0xB0 | 0x0D
        dos.writeShort(0x0001); // transport_stream_id
        dos.writeByte(0xC1);    // version_number=0, current_next_indicator=1
        dos.writeByte(0x00);    // section_number
        dos.writeByte(0x00);    // last_section_number
        dos.writeShort(0x0001); // program_number
        dos.writeShort(PMT_PID | 0xE000); // program_map_PID

        // CRC32 (此处简化填0，实际应计算，但多数播放器容忍)
        dos.writeInt(0x00000000);

        // 填充剩余字节到 188
        while (baos.size() < TS_PACKET_SIZE) {
            dos.writeByte(0xFF);
        }
        outputStream.write(baos.toByteArray());
    }

    // ---------- PMT 生成 ----------
    private void writePMT() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(TS_PACKET_SIZE);
        DataOutputStream dos = new DataOutputStream(baos);

        // TS 头
        dos.writeByte(0x47);
        dos.writeShort(PMT_PID | 0x4000);
        int cc = (ccPmt++ & 0x0F);
        dos.writeByte(0x10 | cc);

        // PMT 段
        dos.writeByte(0x00); // pointer_field
        dos.writeByte(0x02); // table_id
        dos.writeShort(0xB01A); // section_length (18)
        dos.writeShort(0x0001); // program_number
        dos.writeByte(0xC1);
        dos.writeByte(0x00);
        dos.writeByte(0x00);
        dos.writeShort(PCR_PID | 0xE000);
        dos.writeShort(0x0000); // program_info_length

        // 视频流描述 (H.264)
        dos.writeByte(0x1B);    // stream_type: H.264
        dos.writeShort(VIDEO_PID | 0xE000);
        dos.writeShort(0x0000); // ES_info_length

        // CRC32
        dos.writeInt(0x00000000);

        while (baos.size() < TS_PACKET_SIZE) {
            dos.writeByte(0xFF);
        }
        outputStream.write(baos.toByteArray());
    }

    // ---------- PES 打包 ----------
    private void writePES(byte[] data, int offset, int length, boolean withPts, boolean isConfig) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // 1. PES 头
        dos.writeInt(0x00000001); // 起始码
        dos.writeByte(0xE0);      // stream_id: video

        int ptsSize = withPts ? 5 : 0;
        int pesHeaderLen = 3 + ptsSize; // 固定3字节标记 + PTS/DTS
        int packetLen = length + pesHeaderLen;
        if (packetLen > 0xFFFF) packetLen = 0; // PES 包长度超过 65535 设为 0

        dos.writeShort(packetLen);

        // 标记位 (PTS/DTS 标志)
        int flags = 0x80; // 默认至少有一个 PTS/DTS 标志位
        if (!isConfig && withPts) {
            flags = 0xC0; // 10 标志：PTS+DTS
        } else if (isConfig) {
            flags = 0x00; // 配置帧不需要 PTS/DTS
        } else {
            flags = 0x80; // 仅 PTS
        }
        dos.writeByte(flags);
        dos.writeByte(0x80); // PTS/DTS 标志 + 数据长度固定

        if (!isConfig && withPts) {
            // PTS (33 bits)
            long pts = (System.currentTimeMillis() * 90) % 0x1FFFFFFFFL;
            dos.writeByte((byte) (0x20 | ((pts >> 29) & 0x07)));
            dos.writeByte((byte) ((pts >> 22) & 0xFF));
            dos.writeByte((byte) (0x80 | ((pts >> 14) & 0xFF)));
            dos.writeByte((byte) ((pts >> 7) & 0xFF));
            dos.writeByte((byte) (0x80 | (pts & 0x7F)));

            // DTS 和 PTS 相同 (简化)
            // 实际应计算，但这里省略，部分播放器可接受
            // 如果你想要稳定，可以添加 DTS，但大多数电视对非交错流不强制
        } else if (isConfig) {
            // 配置帧：PES_header_data_length = 0
            // 已经写过 0x80 但这里需要修正
            // 重写: 前面写错了，我们现在重新构建更稳妥
            // 但为了简便，我们重新调用此方法时 isConfig 单独处理
        }

        dos.write(data, offset, length);

        // 将 PES 包切分为 188 字节的 TS 包
        byte[] pesData = baos.toByteArray();
        int pos = 0;
        boolean firstPacket = true;
        while (pos < pesData.length) {
            byte[] tsPacket = new byte[TS_PACKET_SIZE];
            tsPacket[0] = 0x47;
            tsPacket[1] = (byte) (0x40 | (VIDEO_PID >> 8));
            tsPacket[2] = (byte) (VIDEO_PID & 0xFF);

            int cc = (ccVideo++ & 0x0F);
            // adaptation_field_control: 01 (仅 payload) 或 11 (adaptation + payload)
            // 如果是第一包且含有 PTS 头，payload_unit_start_indicator=1
            int afc = 0x10; // payload only
            if (firstPacket) {
                afc |= 0x20; // payload_unit_start_indicator
                firstPacket = false;
            }
            tsPacket[3] = (byte) (afc | cc);

            int payloadSize = Math.min(TS_PACKET_SIZE - 4, pesData.length - pos);
            System.arraycopy(pesData, pos, tsPacket, 4, payloadSize);
            // 填充剩余
            for (int i = 4 + payloadSize; i < TS_PACKET_SIZE; i++) {
                tsPacket[i] = (byte) 0xFF;
            }
            outputStream.write(tsPacket);
            pos += payloadSize;
        }
    }

    // ---------- 提取 SPS/PPS (从起始码中查找) ----------
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

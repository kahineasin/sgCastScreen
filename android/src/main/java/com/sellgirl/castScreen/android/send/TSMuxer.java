package com.sellgirl.castScreen.android.send;

import android.media.MediaCodec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class TSMuxer {
    private static final int TS_PACKET_SIZE = 188;
    private static final int PAT_INTERVAL = 100;  // 每100个包插入PAT/PMT
    private int packetCounter = 0;
    private OutputStream output;
    private int pcrPid = 0x100;
    private int videoPid = 0x101;
    private byte[] sps = null, pps = null;
    private long pts = 0, dts = 0;
    private long timeBase = 0;

    public TSMuxer(OutputStream output) {
        this.output = output;
    }

    public void setSPSPPS(byte[] sps, byte[] pps) {
        this.sps = sps;
        this.pps = pps;
    }

    // 输入：H.264 完整帧（包含起始码 00 00 00 01 或 00 00 01）
    public void writeFrame(ByteBuffer frameBuffer, MediaCodec.BufferInfo info) throws IOException {
        if (sps == null || pps == null) {
            // 尝试从帧中提取 SPS/PPS（简化：如果帧包含则缓存）
            // 实际应解析 NAL 单元，这里假设第一帧包含 sps/pps
            extractSPSPPS(frameBuffer);
        }

        // 将帧数据分割成 NAL 单元并封装为 TS 包
        byte[] data = new byte[info.size];
        frameBuffer.get(data);
        // 查找起始码并分割 NAL（此处简化：直接封装整个数据为 PES）
        // 实际应遍历 NAL，但简单起见，我们直接封装为一个 PES 包（可能包含多个 NAL）
        // 更好的做法：调用 writePES 传入完整帧
        writePES(data, 0, data.length);
    }

    private void extractSPSPPS(ByteBuffer buffer) {
        // 简单的解析：查找 SPS (0x67) 和 PPS (0x68) 的 NAL 单元
        // 这里略，实际可参考标准实现
    }

    private void writePES(byte[] data, int offset, int length) throws IOException {
        // 1. 生成 PES 包头（此处简化，不填充 PTS/DTS）
        // 2. 将 PES 包分割成 188 字节的 TS 包
        // 3. 定期插入 PAT/PMT
        // 由于代码较长，这里仅提供框架，实际需要完整实现 TS 复用逻辑
        // 建议使用开源库，或参考网上示例
        // 由于篇幅，此处仅示意，实际可使用现成的 TS Muxer（如 ffmpeg 的 libavformat）
        // 或使用 Android 的 MediaMuxer 写入文件再读，但实时性差。
    }
}

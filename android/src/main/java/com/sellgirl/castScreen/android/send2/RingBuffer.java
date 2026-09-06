package com.sellgirl.castScreen.android.send2;


import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 线程安全的环形缓冲区，存储固定大小的TS包（188字节）。
 * 支持多个独立读指针，新读指针从最新数据开始。
 */
public class RingBuffer {
    private static final int TS_PACKET_SIZE = 188;
    private final int capacity;          // 最多存储的TS包数量
    private final byte[][] buffer;
    private volatile long writeSequence = 0;  // 全局写序号（单调递增）
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public RingBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new byte[capacity][TS_PACKET_SIZE];
    }

    /**
     * 写入一个TS包（复制数据）
     */
    public void write(byte[] packet) {
        lock.writeLock().lock();
        try {
            int index = (int) (writeSequence % capacity);
            System.arraycopy(packet, 0, buffer[index], 0, TS_PACKET_SIZE);
            writeSequence++;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 创建一个新的读指针，从当前最新数据开始（可能跳过一些旧包）
     */
    public Reader newReader() {
        return new Reader(writeSequence);
    }

    /**
     * 读指针类
     */
    public class Reader {
        private long sequence;   // 下一个要读取的包序号

        private Reader(long startSeq) {
            this.sequence = startSeq;
        }

        /**
         * 读取下一个TS包，若无新数据则返回 null（非阻塞）
         */
        public byte[] read() {
            lock.readLock().lock();
            try {
                if (sequence >= writeSequence) {
                    return null;  // 无新数据
                }
                // 如果落后太多（超过容量），则跳到最新可读位置（丢弃旧数据）
                long minSeq = writeSequence - capacity;
                if (sequence < minSeq) {
                    sequence = minSeq;
                }
                int index = (int) (sequence % capacity);
                byte[] packet = new byte[TS_PACKET_SIZE];
                System.arraycopy(buffer[index], 0, packet, 0, TS_PACKET_SIZE);
                sequence++;
                return packet;
            } finally {
                lock.readLock().unlock();
            }
        }

        /**
         * 关闭读指针（无需额外操作）
         */
        public void close() {
            // 无资源释放
        }
    }
}

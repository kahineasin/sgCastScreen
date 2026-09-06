package com.sellgirl.castScreen.android.send;

import android.util.Log;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.CopyOnWriteArrayList;

public class StreamBroadcaster {
    private static final String TAG = "StreamBroadcaster";
    private final CopyOnWriteArrayList<OutputStream> clients = new CopyOnWriteArrayList<>();

    public void addClient(OutputStream client) {
        clients.add(client);
        Log.d(TAG, "Client added, total: " + clients.size());
    }

    public void removeClient(OutputStream client) {
        clients.remove(client);
        try { client.close(); } catch (IOException ignored) {}
        Log.d(TAG, "Client removed, remaining: " + clients.size());
    }

    /**
     * 向所有活跃客户端广播数据
     * 如果有客户端写入失败（已断开），自动移除
     */
    public void broadcast(byte[] data, int offset, int len) {
        if (clients.isEmpty()) return;
        for (OutputStream out : clients) {
            try {
                out.write(data, offset, len);
                out.flush();
            } catch (IOException e) {
                // 客户端已断开，从列表中移除
                Log.w(TAG, "Client disconnected, removing");
                removeClient(out);
            }
        }
    }

    public int getClientCount() {
        return clients.size();
    }

    public void closeAll() {
        for (OutputStream out : clients) {
            try { out.close(); } catch (IOException ignored) {}
        }
        clients.clear();
    }
}

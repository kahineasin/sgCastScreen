package com.sellgirl.castScreen.android.send2;


import android.app.Activity;
import android.util.Log;

import com.sellgirl.castScreen.android.DLNADeviceScanner2;

import org.jupnp.UpnpService;
import org.jupnp.controlpoint.ControlPoint;
import org.jupnp.model.action.ActionInvocation;
import org.jupnp.model.message.UpnpResponse;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.Service;
import org.jupnp.model.types.ServiceType;
import org.jupnp.support.model.DIDLContent;
import org.jupnp.support.model.Res;
import org.jupnp.support.model.item.VideoItem;
import org.jupnp.util.MimeType;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class DLNACastManager {
    private static final String TAG = "CastManager";
    private RemoteDevice targetDevice;
    private UpnpService upnpService;
    public ScreenCaptureManager captureManager;
    private StreamServer streamServer;
    private TSMuxer muxer;
    private RingBuffer ringBuffer;
    private boolean inited = false;

    public void init(Activity activity) {
        if (inited) return;
        try {
            // 1. 创建环形缓冲区（容量2000个TS包，约376KB，可缓存约10秒）
            ringBuffer = new RingBuffer(2000);
            // 2. 创建TSMuxer（单例）
            muxer = TSMuxer.getInstance(ringBuffer);
            // 3. 创建StreamServer
            streamServer = new StreamServer(ringBuffer);
            // 4. 创建ScreenCaptureManager，设置帧接收器为TSMuxer
            captureManager = new ScreenCaptureManager();
            captureManager.setFrameReceiver(muxer);
            inited = true;
            Log.d(TAG, "Init completed");
        } catch (IOException e) {
            Log.e(TAG, "Init failed", e);
        }
    }

    public void startCasting(Activity activity) {
        if (!inited) init(activity);
        if (captureManager != null) {
            captureManager.requestCapture(activity);
        }
    }

    public void connectDevice(DLNADeviceScanner2.DLNADevice device, UpnpService upnpService) {
        this.targetDevice = device.getRawDevice();
        this.upnpService = upnpService;
    }

    public void castToDevice(String videoUrl, String title) {
        if (targetDevice == null || upnpService == null) {
            Log.e(TAG, "Device not connected");
            return;
        }
        try {
            // 发送SetAVTransportURI指令
            ServiceType avType = new ServiceType("schemas-upnp-org", "AVTransport", 1);
            org.jupnp.model.meta.Service avService = targetDevice.findService(avType);
            if (avService == null) {
                Log.e(TAG, "AVTransport not found");
                return;
            }
            // 构造DIDL...
            // （此处省略，沿用之前可用的代码）

            // 2. 构建 DIDL-Lite 元数据（使用 VideoItem 的 setter 方式，避免构造函数歧义）
            DIDLContent didl = new DIDLContent();
            VideoItem videoItem = new VideoItem();
            videoItem.setId("0");                     // 实例ID
            videoItem.setParentID("0");               // 父容器ID
            videoItem.setTitle(title);                // 显示标题
            videoItem.setCreator("");                 // 创建者

            // 3. 构造资源描述（Res）
            //    MimeType: video/mp2t (MPEG-TS 流)
            //    value: 视频流的 URL
            MimeType mimeType = new MimeType("video", "mp2t");
            Res res = new Res(mimeType, null, videoUrl); // 第二个参数 size 可为 null
            // 可选：设置分辨率等元数据
            // res.setResolution("1280x720");

            videoItem.addResource(res);
            didl.addItem(videoItem);

            // 4. 发送 SetAVTransportURI 指令
            ActionInvocation setUriInvocation = new ActionInvocation(
                avService.getAction("SetAVTransportURI")
            );
            setUriInvocation.setInput("InstanceID", 0);
            setUriInvocation.setInput("CurrentURI", videoUrl);
            setUriInvocation.setInput("CurrentURIMetaData", didl.toString());

            ControlPoint controlPoint =upnpService.getControlPoint();
            controlPoint.execute(new org.jupnp.controlpoint.ActionCallback(setUriInvocation) {
                @Override
                public void success(ActionInvocation invocation) {
                    Log.d("CastManager", "SetAVTransportURI success");
                    // 成功后自动播放
                    play(avService);
                }

                @Override
                public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                    Log.e("CastManager", "SetAVTransportURI failed: " + defaultMsg);
                }
            });
            Log.d(TAG, "Cast request sent to " + videoUrl);
        } catch (Throwable e) {
            Log.e(TAG, "Cast failed", e);
        }
    }

    // 播放辅助方法
    private void play(Service avTransport) {
        ActionInvocation playInvocation = new ActionInvocation(
            avTransport.getAction("Play")
        );
        playInvocation.setInput("InstanceID", 0);
        playInvocation.setInput("Speed", "1");

        ControlPoint controlPoint =upnpService.getControlPoint();
        controlPoint.execute(new org.jupnp.controlpoint.ActionCallback(playInvocation) {
            @Override
            public void success(ActionInvocation invocation) {
                Log.d("CastManager", "Play success");
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                Log.e("CastManager", "Play failed: " + defaultMsg);
            }
        });
    }

    public void stopCasting() {
        if (captureManager != null) {
            captureManager.stopStreaming();
        }
        if (streamServer != null) {
            streamServer.stopServer();
        }
        if (ringBuffer != null) {
            // 清空缓冲区（可选）
        }
        inited = false;
        Log.d(TAG, "Casting stopped");
    }

    // 获取本机IP（用于生成URL）
    public String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface nif = en.nextElement();
                Enumeration<InetAddress> addr = nif.getInetAddresses();
                while (addr.hasMoreElements()) {
                    InetAddress ip = addr.nextElement();
                    if (!ip.isLoopbackAddress() && ip instanceof Inet4Address) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "127.0.0.1";
    }
}

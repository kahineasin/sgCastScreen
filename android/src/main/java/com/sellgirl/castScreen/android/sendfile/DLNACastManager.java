package com.sellgirl.castScreen.android.sendfile;
import android.app.Activity;
import android.util.Log;

import com.sellgirl.castScreen.android.DLNADeviceScanner2;
import com.sellgirl.castScreen.android.send.ScreenCaptureManager;
//import com.sellgirl.castScreen.android.send.StreamServer;
import com.sellgirl.castScreen.android.send.TSMuxer;
import com.sellgirl.castScreen.android.send.TSMuxer2;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

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
//import org.jupnp.util.MimeType;
//import org.jupnp.support.model.MediaInfo.MimeType;
import org.jupnp.util.MimeType;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;


public class DLNACastManager //implements IDLNADeviceCaster
{
    private static final String TAG = "DLNACastManager";
    private RemoteDevice targetDevice;
    private ControlPoint controlPoint;
    private StreamServer streamServer;
    public ScreenCaptureManager captureManager;
    private TSMuxer2 muxer;

    private boolean inited=false;
//    public void startStreaming(Activity activity, DLNADeviceScanner2.DLNADevice device, UpnpService upnpService) {
//        if(!inited) {
//            inited=true;
//            // 1. 创建 HTTP 服务器
//            try {
//                streamServer = new StreamServer();
//            } catch (IOException e) {
//                e.printStackTrace();
//                inited=false;
//                return;
//            }
//
//            // 2. 初始化 TSMuxer，将其输出连接到 StreamServer
//            muxer = new TSMuxer(streamServer.getOutputStream());
//
//            // 3. 启动屏幕捕获，设置编码回调
//            captureManager = new ScreenCaptureManager();
//            captureManager.setFrameListener((buffer, info) -> {
//                try {
//                    muxer.writeFrame(buffer, info);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//            captureManager.requestCapture(activity);
//        }
//
//        // 4. 连接设备并发送投屏指令
//        connect(device, upnpService);
//        castVideo("http://" + getLocalIpAddress() + ":8080/stream.ts", "Screen Mirroring");
//    }

    public String startStreaming2(Activity activity, DLNADeviceScanner2.DLNADevice device, UpnpService upnpService) {
        if(!inited) {
            inited=true;
            // 1. 创建 HTTP 服务器
            try {
                streamServer = new StreamServer();
            } catch (IOException e) {
                e.printStackTrace();
                inited=false;
                return null;
            }

            // 2. 初始化 TSMuxer，将其输出连接到 StreamServer
            muxer = new TSMuxer2(streamServer.getOutputStream());

            // 3. 启动屏幕捕获，设置编码回调
            captureManager = new ScreenCaptureManager();
            captureManager.setFrameListener((buffer, info) -> {
                try {
                    muxer.writeFrame(buffer, info);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            captureManager.requestCapture(activity);
        }

        // 4. 连接设备并发送投屏指令
        connect(device, upnpService);
        String videoUrl="http://" + getLocalIpAddress() + ":8080/stream.ts";
        try {
            castVideo(videoUrl, "Screen Mirroring");
        }catch (Exception e){
            SGDataHelper.getLog().printException(e,TAG);
        }
        return videoUrl;
    }
    private void connect(DLNADeviceScanner2.DLNADevice device, UpnpService upnpService) {
        this.targetDevice = device.getRawDevice();
        this.controlPoint = upnpService.getControlPoint();
    }

//    public void castVideo(String videoUrl, String title) {
//        if (controlPoint == null || targetDevice == null) return;
//
//        Service avTransport = targetDevice.findService(new ServiceType("urn:schemas-upnp-org:service:AVTransport:1"));
//        if (avTransport == null) return;
//
//        // 构造 DIDL
//        DIDLContent didl = new DIDLContent();
//        VideoItem item = new VideoItem(
//            new org.jupnp.support.model.item.Item.ItemId("0"),
//            "0", title, "",
//            new Res(videoUrl, "video/mp2t")
//        );
//        didl.addItem(item);
//
//        ActionInvocation setUri = new ActionInvocation(avTransport.getAction("SetAVTransportURI"));
//        setUri.setInput("InstanceID", 0);
//        setUri.setInput("CurrentURI", videoUrl);
//        setUri.setInput("CurrentURIMetaData", didl.toString());
//
//        controlPoint.execute(new org.jupnp.controlpoint.ActionCallback(setUri) {
//            @Override
//            public void success(ActionInvocation invocation) {
//                // 播放
//                ActionInvocation play = new ActionInvocation(avTransport.getAction("Play"));
//                play.setInput("InstanceID", 0);
//                play.setInput("Speed", "1");
//                controlPoint.execute(new org.jupnp.controlpoint.ActionCallback(play) {
//                    @Override
//                    public void success(ActionInvocation invocation) {}
//                    @Override
//                    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {}
//                });
//            }
//            @Override
//            public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {}
//        });
//    }

    public void castVideo(String videoUrl, String title) {
        if (controlPoint == null || targetDevice == null) {
            Log.e("CastManager", "Not connected");
            return;
        }

        SGDataHelper.getLog().print("videoUrl:================"+videoUrl);

        // 1. 正确构造 ServiceType（命名空间、类型、版本）
        ServiceType avType = new ServiceType("schemas-upnp-org", "AVTransport", 1);
        Service avTransport = targetDevice.findService(avType);
        if (avTransport == null) {
            Log.e("CastManager", "AVTransport service not found");
            return;
        }

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
            avTransport.getAction("SetAVTransportURI")
        );
        setUriInvocation.setInput("InstanceID", 0);
        setUriInvocation.setInput("CurrentURI", videoUrl);
        setUriInvocation.setInput("CurrentURIMetaData", didl.toString());

        controlPoint.execute(new org.jupnp.controlpoint.ActionCallback(setUriInvocation) {
            @Override
            public void success(ActionInvocation invocation) {
                Log.d("CastManager", "SetAVTransportURI success");
                // 成功后自动播放
                play(avTransport);
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                Log.e("CastManager", "SetAVTransportURI failed: " + defaultMsg);
            }
        });
    }

    // 播放辅助方法
    private void play(Service avTransport) {
        ActionInvocation playInvocation = new ActionInvocation(
            avTransport.getAction("Play")
        );
        playInvocation.setInput("InstanceID", 0);
        playInvocation.setInput("Speed", "1");

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

    // 获取本机 IP 地址（需在 WiFi 网络下）
    private String getLocalIpAddress() {
        try {
            java.util.Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface nif = en.nextElement();
                java.util.Enumeration<InetAddress> addr = nif.getInetAddresses();
                while (addr.hasMoreElements()) {
                    InetAddress ip = addr.nextElement();
                    if (!ip.isLoopbackAddress() && ip instanceof Inet4Address) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {}
        return "127.0.0.1";
    }

    public void stopStreaming() {
        if (captureManager != null) captureManager.stopStreaming();
        if (streamServer != null) streamServer.stopServer();
    }

//    // 用户选择设备后调用
//    @Override
//    private void startCasting2(DeviceIp ip//, UpnpService upnpService
//    ) {
//        UpnpService upnpService=scanner.getUpnpService();
//        DLNADeviceScanner2.DLNADevice device= scanner.getDlnaDevice(ip);
//        castManager = new DLNACastManager();
//        castManager.startStreaming(this, device, upnpService);
//    }
}

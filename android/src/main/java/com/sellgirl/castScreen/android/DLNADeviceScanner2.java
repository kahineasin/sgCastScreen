package com.sellgirl.castScreen.android;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.sellgirl.castScreen.model.DeviceIp;
import com.sellgirl.castScreen.IDLNADeviceScanner;
import com.sellgirl.castScreen.IOnDeviceScanListener;
import com.sellgirl.sgGameHelper.list.Array2;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

import org.jupnp.binding.xml.DeviceDescriptorBinder;
import org.jupnp.binding.xml.UDA10DeviceDescriptorBinderImpl;
import org.jupnp.model.message.header.STAllHeader;
import org.jupnp.model.message.header.UpnpHeader;
import org.jupnp.model.meta.DeviceIdentity;
import org.jupnp.model.meta.RemoteDeviceIdentity;
import org.jupnp.model.types.DeviceType;
import org.jupnp.model.message.header.DeviceTypeHeader;

import org.jupnp.UpnpService;
import org.jupnp.android.AndroidUpnpService;
import org.jupnp.android.AndroidUpnpServiceImpl;
import org.jupnp.model.meta.Device;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.Service;
import org.jupnp.model.types.DeviceType;
import org.jupnp.model.types.ServiceType;
import org.jupnp.model.types.UDN;
import org.jupnp.registry.DefaultRegistryListener;
import org.jupnp.registry.Registry;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DLNADeviceScanner2 implements IDLNADeviceScanner {
    private static final String TAG = "DLNADeviceScanner";
    private AndroidUpnpService upnpService;

//    private final Array2<Device> rawDeviceList = new Array2<>();
    private final CopyOnWriteArrayList<DLNADevice> deviceList = new CopyOnWriteArrayList<>();
    private final Array2<DeviceIp> deviceIps=new Array2<>();
    private OnDeviceScanListener listener;
    private IOnDeviceScanListener listener2;
    private Array2<IOnDeviceScanListener> listeners;
    private Context context;
    private boolean isScanning = false;
//    private final Object lock = new Object();

    // 保存 ServiceConnection 以便解绑
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            upnpService = (AndroidUpnpService) service;
            doSearch();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            upnpService = null;
            if (listener != null) listener.onScanStopped();
        }
    };

    public interface OnDeviceScanListener {
        void onDeviceFound(DLNADevice device);
        void onDeviceLost(DLNADevice device);
        void onScanStarted();
        void onScanStopped();
        void onError(String error);
    }

    public void setListener(OnDeviceScanListener listener) { this.listener = listener; }
    public void setScanListener(IOnDeviceScanListener listener) { this.listener2 = listener; }
    public void addScanListener(IOnDeviceScanListener listener) { if(null==listeners){listeners=new Array2<>();} this.listeners.add(listener); }

    public UpnpService getUpnpService(){
        return upnpService.get();
    }

    public void init(Context context) {
        this.context = context.getApplicationContext();
    }

    public void startScan() {
        if (isScanning) return;
        if (context == null) {
            if (listener != null) listener.onError("Context not set");
            return;
        }
        if (upnpService != null) {
            doSearch();
            return;
        }
        // 绑定服务（异步）
        Intent intent = new Intent(context, AndroidUpnpServiceImpl.class);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }
public void addDevice(RemoteDevice device){

    DLNADevice dlnaDevice = new DLNADevice((RemoteDevice) device);
    if (!deviceList.contains(dlnaDevice) //虽然上面是new, 但这里contains没问题,因为DLNADevice复写了equals方法.
//                    !rawDeviceList.contains(device,true)
    ) {
//                        rawDeviceList.add(device);
        deviceList.add(dlnaDevice);
        DeviceIp ip = new DeviceIp();
        ip.udn = dlnaDevice.getUDN().getIdentifierString();
        URI uri = null;
        try {
            ip.location = dlnaDevice.getLocation();
            uri = new URI(ip.location);
            ip.ip = uri.getHost(); // 返回 "192.168.1.100"
            ip.port = uri.getPort();
        } catch (URISyntaxException e) {
//                            throw new RuntimeException(e);
        }
        if (null == ip.ip) {
            ip.ip = dlnaDevice.getLocation();
        }
        ip.name = dlnaDevice.getFriendlyName();
        deviceIps.add(ip);
        if (listener != null) listener.onDeviceFound(dlnaDevice);
        if (listener2 != null) listener2.onDeviceFound(ip);
        if ( listeners != null) {
            for(IOnDeviceScanListener i:listeners){
                i.onDeviceFound(ip);
            }
        }
    }
}
    private void doSearch() {
//        synchronized(this.lock) {
            if (upnpService == null) {
                if (listener != null) listener.onError("UPnP service not available");
                return;
            }
//        org.eclipse.jetty.server.Server a=new Server();
//        org.eclipse.jetty.server.ServerConnector b=new ServerConnector();
            //org.eclipse.jetty.servlet.ServerConnector b=new ServerConnector();
            ((UpnpService) upnpService.get()).startup();
            Registry registry = upnpService.getRegistry();
            while (null == registry) {
                try {
                    Thread.sleep(1000);
                    registry = upnpService.getRegistry();
                } catch (InterruptedException e) {
                    break;
                }
            }
            if (registry == null) {
                if (listener != null) listener.onError("Registry is null");
                return;
            }
            isScanning = true;
            if (listener != null) listener.onScanStarted();

            registry.addListener(new DefaultRegistryListener() {
                @Override
                public void deviceAdded(Registry registry, Device device) {
                    try {
//                        DLNADevice dlnaDevice = new DLNADevice((RemoteDevice) device);
//                        if (!deviceList.contains(dlnaDevice) //虽然上面是new, 但这里contains没问题,因为DLNADevice复写了equals方法.
////                    !rawDeviceList.contains(device,true)
//                        ) {
//////                        rawDeviceList.add(device);
////                            deviceList.add(dlnaDevice);
////                            DeviceIp ip = new DeviceIp();
////                            ip.udn = dlnaDevice.getUDN().getIdentifierString();
////                            URI uri = null;
////                            try {
////                                ip.location=dlnaDevice.getLocation();
////                                uri = new URI(ip.location);
////                                ip.ip = uri.getHost(); // 返回 "192.168.1.100"
////                                ip.port = uri.getPort();
////                            } catch (URISyntaxException e) {
//////                            throw new RuntimeException(e);
////                            }
////                            if (null == ip.ip) {
////                                ip.ip = dlnaDevice.getLocation();
////                            }
////                            ip.name = dlnaDevice.getFriendlyName();
////                            deviceIps.add(ip);
////                            if (listener != null) listener.onDeviceFound(dlnaDevice);
////                            if (listener2 != null) listener2.onDeviceFound(ip);
//                            addDevice((RemoteDevice) device);
////                            if("客厅极光TV(dlna)".equals(ip.name)){
////                                int a=1;
////                            }
////
//                        // 打印设备名称和服务信息
//                        Log.d(TAG, "Device: " + dlnaDevice.getFriendlyName());
//                        for (Service service : device.getServices()) {
//                            ServiceType type = service.getServiceType();
//                            Log.d(TAG, "  Service: " + type.getNamespace() + ":" + type.getType() + " v" + type.getVersion());
//                        }
//
//                        } else {
//                            int a = 1;
//                            Log.d(TAG, "--------------------contains-------------");
//                        }
                        addDevice((RemoteDevice) device);
                    }catch (Throwable e){
                        SGDataHelper.getLog().printException(e,TAG);
                    }
                }

                @Override
                public void deviceRemoved(Registry registry, Device device) {

////                    rawDeviceList.removeValue(device,true);
//
                    DLNADevice dlnaDevice = new DLNADevice((RemoteDevice) device);
//                    deviceList.remove(dlnaDevice);
                    int idx=0;
//
////                    DLNADevice dlnaDevice=null;
////                    for(DLNADevice i:deviceList){
////                        if(//i.name.equals(device.getDetails().getFriendlyName())
////                            i.equals()
////                        ){
////                            find=deviceIps.get(idx);
////                            deviceIps.removeIndex(idx);
////                            break;
////                        }
////                        idx++;
////                    }
//
                    idx=0;
                    DeviceIp find=null;
                    for(DeviceIp i:deviceIps){
                        if(//i.name.equals(device.getDetails().getFriendlyName())
                            i.udn.equals(dlnaDevice.getUDN().getIdentifierString())
                        ){
                            find=deviceIps.get(idx);
//                            deviceIps.removeIndex(idx);
                            break;
                        }
                        idx++;
                    }
                    if (listener != null) listener.onDeviceLost(dlnaDevice);
                    if ( listener2 != null&&null!=find) {listener2.onDeviceLost(find);}
                    if ( listeners != null&&null!=find) {
                        for(IOnDeviceScanListener i:listeners){
                            i.onDeviceLost(find);
                        }
                    }
                }
            });

            upnpService.getControlPoint().search();

// 只搜索 MediaRenderer 类型的设备
//        DeviceType mediaRendererType = new DeviceType("schemas-upnp-org", "MediaRenderer", 1);
//        upnpService.getControlPoint().search(new DeviceTypeHeader(mediaRendererType));
//        upnpService.getControlPoint().search(new STAllHeader());
//        }
    }
//    public CopyOnWriteArrayList<DLNADevice> getDeviceList(){
//        synchronized(this.lock) {
//            return deviceList;
//        }
//        return
//    }
    public Array2<DeviceIp> getDevice(){
        return deviceIps;
    }
    public DLNADevice getDlnaDevice(DeviceIp ip){
        for(DLNADevice i:deviceList){
            if(ip.udn.equals(i.getUDN().getIdentifierString())){
                return i;
            }
        }
        return null;
    }

    public void stopScan() {
        isScanning = false;
        if (listener != null) listener.onScanStopped();
        if (context != null && upnpService != null) {
            context.unbindService(connection);
        }
        deviceList.clear();
    }

    public String getDeviceIp(DLNADevice device) {
        try {
            URI uri = new URI(device.getLocation());
            return uri.getHost(); // 返回 "192.168.1.100"
        } catch (Exception e) {
            return null;
        }
    }
    // DLNADevice 内部类保持不变 ...
    // 设备数据封装类
    public static class DLNADevice {
        private final RemoteDevice device;
        public DLNADevice(RemoteDevice device) { this.device = device; }
        public String getFriendlyName() { return device.getDetails().getFriendlyName(); }
        public String getModelName() { return device.getDetails().getModelDetails().getModelName(); }
        public String getLocation() { return device.getIdentity().getDescriptorURL().toString(); }
        public UDN getUDN() { return device.getIdentity().getUdn();/*.getUDN(); */}
        public RemoteDevice getRawDevice() { return device; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DLNADevice that = (DLNADevice) o;
            return getUDN().equals(that.getUDN())&&getLocation().equals(that.getLocation());
        }
        @Override
        public int hashCode() { return getUDN().hashCode(); }
    }


//    /**
//     * 从缓存的 location URL 直接加载设备，跳过 SSDP 搜索。
//     * @param location 设备描述文件 URL（如 http://192.168.10.22:39520/description.xml）
//     * @return RemoteDevice 对象，加载失败返回 null
//     */
//    public RemoteDevice loadDeviceFromLocation(String location) {
//        try {
//            DeviceDescriptorBinder binder = new UDA10DeviceDescriptorBinderImpl();
//            // 注意：第二个参数是 System.getProperty("line.separator")，可为 null
//            RemoteDevice device = (RemoteDevice) binder.describe(new URL(location), null);
//            Log.d(TAG, "Loaded device from cache: " + device.getDetails().getFriendlyName());
//            return device;
//        } catch (Exception e) {
//            Log.e(TAG, "Failed to load device from " + location, e);
//            return null;
//        }
//    }


    public RemoteDevice loadDeviceFromLocation(String locationUrl) {
        Document doc=null;
        try {
//            // 用 XML 解析器构建 Document
//            doc = DocumentBuilderFactory.newInstance()
//                .newDocumentBuilder()
//                .parse(new URL(locationUrl).openStream());
//
//            NodeList rootChildren = doc.getChildNodes().item(0).getChildNodes();
//            Node rootChild = rootChildren.item(3);
//            String s1=rootChild.getLocalName();

            // 1. 创建 DocumentBuilder，开启命名空间感知（关键！）
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);  // <--- 必须加这一行
            DocumentBuilder builder = factory.newDocumentBuilder();
             doc = builder.parse(new URL(locationUrl).openStream());

            // 3. 创建一个“空壳”RemoteDevice（关键步骤）
            int maxAgeSecond=1800;
            RemoteDeviceIdentity identity = new RemoteDeviceIdentity(
                new UDN("990a-a184-eded-e14b"),                           // UDN
                maxAgeSecond,                          // maxAgeSeconds，可为null
                new URL(locationUrl),          // descriptorURL
                null,                          // interfaceMacAddress，可为null
                null                           // discoveredOnLocalAddress，可为null
            );
            RemoteDevice tempDevice = new RemoteDevice(identity);

            // 4. 用绑定器填充这个“空壳”设备
            DeviceDescriptorBinder binder = new UDA10DeviceDescriptorBinderImpl();
            RemoteDevice filledDevice = (RemoteDevice) binder.describe(tempDevice, doc);
            return filledDevice;

//            // 绑定器直接解析 Document，传 null 让它自己创建 RemoteDevice
//            DeviceDescriptorBinder binder = new UDA10DeviceDescriptorBinderImpl();
//            //org.jupnp.binding.xml.DescriptorBindingException: Could not parse device DOM //todo
//            return (RemoteDevice) binder.describe(null, doc);
        } catch (Exception e) {
            Log.e(TAG, "Load failed", e);
//            if(null!=doc){SGDataHelper.getLog().print("aaaaaa"+doc.getTextContent());}
            return null;
        }
    }
    /**
     * 手动将设备添加到 Registry，使其可被后续操作使用。
     */
    public void addDeviceToRegistry(RemoteDevice device) {
        if (upnpService != null && upnpService.getRegistry() != null&&null!=device) {
            upnpService.getRegistry().addDevice(device);
            Log.d(TAG, "Device added to registry: " + device.getDetails().getFriendlyName());
        }
        if(null!=device) {
            addDevice(device);
        }
    }
}

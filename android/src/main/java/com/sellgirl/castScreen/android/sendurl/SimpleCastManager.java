package com.sellgirl.castScreen.android.sendurl;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class SimpleCastManager {
    private static final String TAG = "SimpleCast";
    private String avControlUrl;   // 完整的控制 URL，如 http://192.168.10.22:39520/control/AVTransport1
    private String renderingControlUrl; // 可选

    /**
     * 从设备描述 XML 的 location URL 加载控制 URL
     * @param locationUrl 如 http://192.168.10.22:39520/description.xml
     * @return 是否成功加载
     */
    public boolean loadDevice(String locationUrl) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new URL(locationUrl).openStream());

            // 获取 base URL（用于拼接相对路径）
            URL baseUrl = new URL(locationUrl);
            String base = baseUrl.getProtocol() + "://" + baseUrl.getHost() + ":" + baseUrl.getPort();

            // 查找 AVTransport 服务的 controlURL
            NodeList serviceNodes = doc.getElementsByTagName("service");
            for (int i = 0; i < serviceNodes.getLength(); i++) {
                Element service = (Element) serviceNodes.item(i);
                String serviceType = getTagContent(service, "serviceType");
                if (serviceType != null && serviceType.contains("AVTransport")) {
                    String controlUrlPath = getTagContent(service, "controlURL");
                    if (controlUrlPath != null) {
                        // 拼接完整 URL
                        if (controlUrlPath.startsWith("/")) {
                            avControlUrl = base + controlUrlPath;
                        } else {
                            avControlUrl = base + "/" + controlUrlPath;
                        }
                        Log.d(TAG, "AVTransport control URL: " + avControlUrl);
                        return true;
                    }
                }
            }
            Log.e(TAG, "No AVTransport service found in XML");
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Failed to load device", e);
            return false;
        }
    }

    private String getTagContent(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return null;
    }

    /**
     * 发送 SetAVTransportURI 指令
     * @param videoUrl  要播放的视频 URL（如 http://192.168.10.50:8080/stream.ts）
     * @param title     标题（可选）
     * @param didlXml   DIDL-Lite 元数据（可为空字符串）
     * @return 是否成功
     */
    public boolean setAVTransportURI(String videoUrl, String title, String didlXml) {
        if (avControlUrl == null) {
            Log.e(TAG, "Control URL not loaded");
            return false;
        }
        // 如果没有 DIDL，构造一个简单的最小 DIDL（可提高兼容性）
        if (didlXml == null || didlXml.isEmpty()) {
            didlXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<DIDL-Lite xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\" " +
                "xmlns:dc=\"http://purl.org/dc/elements/1.1/\" " +
                "xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\">" +
                "<item id=\"0\" parentID=\"0\" restricted=\"1\">" +
                "<dc:title>" + (title != null ? title : "Stream") + "</dc:title>" +
                "<upnp:class>object.item.videoItem</upnp:class>" +
                "<res protocolInfo=\"http-get:*:video/mp2t:DLNA.ORG_PN=MPEG2_TS\">" + videoUrl + "</res>" +
                "</item></DIDL-Lite>";
        }

        String soapAction = "\"urn:schemas-upnp-org:service:AVTransport:1#SetAVTransportURI\"";

        String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
            "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
            "<s:Body>" +
            "<u:SetAVTransportURI xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">" +
            "<InstanceID>0</InstanceID>" +
            "<CurrentURI>" + xmlEscape(videoUrl) + "</CurrentURI>" +
            "<CurrentURIMetaData>" + xmlEscape(didlXml) + "</CurrentURIMetaData>" +
            "</u:SetAVTransportURI>" +
            "</s:Body></s:Envelope>";

        return sendSoapRequest(soapAction, body);
    }

    /**
     * 发送 Play 指令
     * @return 是否成功
     */
    public boolean play() {
        if (avControlUrl == null) return false;
        String soapAction = "\"urn:schemas-upnp-org:service:AVTransport:1#Play\"";
        String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
            "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
            "<s:Body>" +
            "<u:Play xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">" +
            "<InstanceID>0</InstanceID>" +
            "<Speed>1</Speed>" +
            "</u:Play>" +
            "</s:Body></s:Envelope>";
        return sendSoapRequest(soapAction, body);
    }

    /**
     * 发送 Stop 指令（可选，用于重置状态）
     */
    public boolean stop() {
        if (avControlUrl == null) return false;
        String soapAction = "\"urn:schemas-upnp-org:service:AVTransport:1#Stop\"";
        String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
            "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
            "<s:Body>" +
            "<u:Stop xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">" +
            "<InstanceID>0</InstanceID>" +
            "</u:Stop>" +
            "</s:Body></s:Envelope>";
        return sendSoapRequest(soapAction, body);
    }

    private boolean sendSoapRequest(String soapAction, String bodyXml) {
        try {
            URL url = new URL(avControlUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            conn.setRequestProperty("SOAPACTION", soapAction);
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);

            byte[] postData = bodyXml.getBytes("UTF-8");
            conn.setRequestProperty("Content-Length", String.valueOf(postData.length));

            OutputStream os = conn.getOutputStream();
            os.write(postData);
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            Log.d(TAG, "SOAP response code: " + responseCode);

            // 读取响应（可选）
            BufferedReader reader;
            if (responseCode >= 200 && responseCode < 300) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            conn.disconnect();

            Log.d(TAG, "SOAP response: " + response.toString());

            return responseCode >= 200 && responseCode < 300;
        } catch (Exception e) {
            Log.e(TAG, "SOAP request failed", e);
            return false;
        }
    }

    private String xmlEscape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;");
    }
}

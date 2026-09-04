package com.sellgirl.castScreen;

import com.sellgirl.castScreen.model.DeviceIp;
import com.sellgirl.sgGameHelper.list.Array2;

public interface IDLNADeviceCaster {
    void startCasting(DeviceIp ip);
    void startCastingWeb(DeviceIp ip,String webUrl);
    void startCastingFile(DeviceIp ip,String path);
}

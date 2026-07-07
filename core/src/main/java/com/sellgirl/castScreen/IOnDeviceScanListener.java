package com.sellgirl.castScreen;

import com.sellgirl.castScreen.model.DeviceIp;

public interface IOnDeviceScanListener {
    void onDeviceFound(DeviceIp device);
    void onDeviceLost(DeviceIp device);
    void onScanStarted();
    void onScanStopped();
    void onError(String error);
}

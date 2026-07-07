package com.sellgirl.castScreen;

import com.sellgirl.castScreen.model.DeviceIp;
import com.sellgirl.sgGameHelper.list.Array2;

public interface IDLNADeviceScanner {
     Array2<DeviceIp> getDevice();
    void setScanListener(IOnDeviceScanListener listener);
}

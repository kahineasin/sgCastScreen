package com.sellgirl.castScreen.model;

public class DeviceIp {
    /**
     * unique device name, 發現這個可能重複, 1個設備多個信號的時候
     */
    public String udn;
    public String name;
    public String ip;
    public int port;

    //似乎唯一
    public String location;

    @Override
    public String toString() {
        return "udn:"+udn+" name:"+name+" location:"+location;
    }
}

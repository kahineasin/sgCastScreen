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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceIp that = (DeviceIp) o;
        return udn.equals(that.udn)&&location.equals(that.location);
    }
}

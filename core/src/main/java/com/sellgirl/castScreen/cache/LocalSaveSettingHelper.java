package com.sellgirl.castScreen.cache;

//import com.mygdx.game.share.ISGCloudSave;
import com.badlogic.gdx.utils.Json;
import com.sellgirl.castScreen.Constants;
import com.sellgirl.sgJavaHelper.AES;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/**
 * 本地保存的设置
 */
public class LocalSaveSettingHelper extends LocalSaveSettingHelperBase<LocalDeviceIp>{
    private static  final String tag= LocalSaveSettingHelper.class.getName();

//    public LocalSaveSettingHelper3(ISGCloudSave cloudSave) {
//        super(cloudSave);
//    }

    @Override
    protected String getFileKey() {
        return Constants.GAMEPAD_SETTING;
    }
    // ---------------- GamepadSetting存取相关 ------------------

    protected   String getEncodeStrByGameKey(LocalDeviceIp sasha) {
        if(null==sasha.getIp()){
            return null;
        }
        Json json = new Json();
        // 读取文件，并且解密
        String str=json.toJson(sasha,sasha.getClass());
//        save = json.fromJson(Save.class, Base64Coder.decodeString(file.readString()));

//        StringBuilder sb=new StringBuilder();
//        for(DeviceIp ip:sasha.getIp()){
//            sb.
//        }
//        String str = sasha.getAxisLeftSpace().x1 + "|" + sasha.getAxisLeftSpace().x2
//                + "|" + sasha.getAxisLeftSpace().y1 + "|" + sasha.getAxisLeftSpace().y2
//                + "|" +sasha.getAxisRightSpace().x1 + "|" + sasha.getAxisRightSpace().x2
//                + "|" + sasha.getAxisRightSpace().y1 + "|" + sasha.getAxisRightSpace().y2;

        try {
            return AES.AESEncryptDemo(str, SGDataHelper.decodeBase64(key));
        } catch (Exception e) {
            //hasReadError=true;
            SGDataHelper.getLog().printException(e,tag+".getEncodeStrByGamepadSetting ");
        }
        return null;
    }
    protected LocalDeviceIp initGameKeyByEncodeStr(String s,LocalDeviceIp sasha
    ) throws Exception {

        Json json = new Json();
        LocalDeviceIp tmpsasha = json.fromJson(sasha.getClass(), AES.AESDecryptDemo(s, SGDataHelper.decodeBase64(key)));
        sasha.setIp(tmpsasha.getIp());
        return sasha;
//        String[] s2 = AES.AESDecryptDemo(s, SGDataHelper.decodeBase64(key)).split("[|]");
//        sasha.setAxisLeftSpace(Float.valueOf(s2[0]),Float.valueOf(s2[1]),Float.valueOf(s2[2]),Float.valueOf(s2[3]));
//        sasha.setAxisRightSpace(Float.valueOf(s2[4]),Float.valueOf(s2[5]),Float.valueOf(s2[6]),Float.valueOf(s2[7]));
//        return sasha;
    }

    // ---------------- GamepadSetting存取相关 end ------------------
}

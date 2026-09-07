package com.sellgirl.castScreen.cache;

//import com.mygdx.game.share.ISGCloudSave;
import com.sellgirl.castScreen.Constants;
import com.sellgirl.sgGameHelper.gamepad.SGPS5GamepadSetting;//import com.mygdx.game.share.gamepad.SGPS5GamepadSetting;
import com.sellgirl.sgJavaHelper.AES;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/**
 * 本地保存的设置
 */
public class LocalSaveSettingHelper3 extends LocalSaveSettingHelperBase<SGPS5GamepadSetting>{
    private static  final String tag= LocalSaveSettingHelper3.class.getName();

//    public LocalSaveSettingHelper3(ISGCloudSave cloudSave) {
//        super(cloudSave);
//    }

    @Override
    protected String getFileKey() {
        return Constants.GAMEPAD_SETTING;
    }
    // ---------------- GamepadSetting存取相关 ------------------

    protected   String getEncodeStrByGameKey(SGPS5GamepadSetting sasha) {

        String str = sasha.getAxisLeftSpace().x1 + "|" + sasha.getAxisLeftSpace().x2
                + "|" + sasha.getAxisLeftSpace().y1 + "|" + sasha.getAxisLeftSpace().y2
                + "|" +sasha.getAxisRightSpace().x1 + "|" + sasha.getAxisRightSpace().x2
                + "|" + sasha.getAxisRightSpace().y1 + "|" + sasha.getAxisRightSpace().y2;
        try {
            return AES.AESEncryptDemo(str, SGDataHelper.decodeBase64(key));
        } catch (Exception e) {
            //hasReadError=true;
            SGDataHelper.getLog().printException(e,tag+".getEncodeStrByGamepadSetting ");
        }
        return null;
    }
    protected SGPS5GamepadSetting initGameKeyByEncodeStr(String s,SGPS5GamepadSetting sasha
    ) throws Exception {

        String[] s2 = AES.AESDecryptDemo(s, SGDataHelper.decodeBase64(key)).split("[|]");
        sasha.setAxisLeftSpace(Float.valueOf(s2[0]),Float.valueOf(s2[1]),Float.valueOf(s2[2]),Float.valueOf(s2[3]));
        sasha.setAxisRightSpace(Float.valueOf(s2[4]),Float.valueOf(s2[5]),Float.valueOf(s2[6]),Float.valueOf(s2[7]));
        return sasha;
    }

    // ---------------- GamepadSetting存取相关 end ------------------
}

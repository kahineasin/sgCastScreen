package com.sellgirl.castScreen.cache;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
//import com.mygdx.game.share.ISGCloudSave;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/**
 * 本地保存的设置
 */
public abstract class LocalSaveSettingHelperBase<TGameKey> {
    private static  final String tag= LocalSaveSettingHelperBase.class.getName();
    public static final String key = "a2FoaW5lYXNpbjEyMzQ1Ng==";
//    private final ISGCloudSave cloudSave;
//    public LocalSaveSettingHelperBase(ISGCloudSave cloudSave){
//        this.cloudSave=cloudSave;
//    }
    // ---------------- KnightGameKey存取相关 ------------------
    /**
     * Constants.KNIGHT_KEY_SETTING
     * @return
     */
    protected abstract String getFileKey();
    protected abstract String getEncodeStrByGameKey(TGameKey sasha);
//    protected abstract TGameKey initGameKeyByEncodeStr(String s,Class<?> gamepadType) throws Exception;

    protected abstract TGameKey initGameKeyByEncodeStr(String s,TGameKey gameKey) throws Exception;
    public  void saveGameKey(String gamepadName,TGameKey gameKey) {
//        if(null!=cloudSave){
//            cloudSave.writeSaveToCloud(getFileKey()+"_"+gamepadName,getEncodeStrByGameKey(gameKey));
//            //云存档和本地存档不要混合用，否则就需要比对哪个更新才行
//            return;
//        }
        Preferences preferences = Gdx.app.getPreferences(getFileKey());

        try {
            if(null==gameKey){
                preferences.remove(gamepadName);
            }else{
                preferences.putString(gamepadName, getEncodeStrByGameKey(gameKey));
            }
        } catch (Exception e) {
            SGDataHelper.getLog().printException(e,tag+".saveKnightGameKeyData ");
        }
        preferences.flush();
    }


    public  TGameKey readGameKey(String gamepadName
                                 ,TGameKey gameKey
    ) {
//        if(null!=cloudSave){
//            try {
//                String s = cloudSave.readSaveFromCloud(getFileKey()+"_"+gamepadName);
//                if (null == s || "".equals(s)) {
//                    return null;
//                } else {
//                    return initGameKeyByEncodeStr(s, gameKey);
//                }
//            } catch (Exception e) {
//                SGDataHelper.getLog().printException(e, tag);
//            }
//            return null;
//        }else {
            Preferences preferences = Gdx.app.getPreferences(getFileKey());
            try {
                String s = preferences.getString(gamepadName);
                if (null == s || "".equals(s)) {
                    return null;
                } else {
                    return initGameKeyByEncodeStr(s, gameKey);
                }
            } catch (Exception e) {
                SGDataHelper.getLog().printException(e, tag);
            }
            return null;
//        }
    }
    // ---------------- KnightGameKey存取相关 end ------------------

}

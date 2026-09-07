package com.sellgirl.castScreen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.mygdx.game.demo.piano.IMIDIMusic;
//import com.mygdx.game.sasha.language.Country;
//import com.mygdx.game.share.ISGCloudSave;
import com.sellgirl.sgGameHelper.SGFileDownloader;

/**
 * 为了方便改动主类的位置或者包名
 */
public interface IKnightSasha {
    void setScreen(Screen screen);
    BitmapFont getFont();
    BitmapFont getFont3(int size);
    Batch getBatch();
    boolean isShowTouchpad();
    boolean isRelease();
    boolean isHasKeyboard();
    SGFileDownloader getJarDownloader();
    void setJarDownloader(SGFileDownloader d);
    boolean isSupportCloudSave();
//    ISGCloudSave cloudSave();
//    Country getCountry();
    boolean isSteam();
//    IMIDIMusic getMidiMusic();
//    void setMidiMusic(IMIDIMusic midi);

     void setScanner(IDLNADeviceScanner scanner);
     IDLNADeviceScanner getScanner();
     IDLNADeviceCaster getCaster() ;

     void setCaster(IDLNADeviceCaster caster);
     String getSysIp();
    void setSysIp(String sysIp);
}

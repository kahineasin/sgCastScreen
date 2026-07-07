package com.sellgirl.castScreen;

public class Constants {
    // Visible game world is 5 meters wide
    public static final float VIEWPORT_WIDTH = 5.0f;
    // Visible game world is 5 meters tall
    public static final float VIEWPORT_HEIGHT = 5.0f;
    // GUI Width
    public static final float VIEWPORT_GUI_WIDTH = 800.0f;
    // GUI Height
    public static final float VIEWPORT_GUI_HEIGHT = 480.0f;

    public static final String TEXTURE_ATLAS_OBJECTS = "images/canyonbunny.pack.atlas";

    // Location of image file for level 01
    public static final String LEVEL_01 = "levels/level-01.png";

    // Amount of extra lives at level start
    public static final int LIVES_START = 3;
    public static final float ITEM_FEATHER_POWERUP_DURATION = 9;
    public static final float TIME_DELAY_GAME_OVER = 3;

    public static final String TEXTURE_ATLAS_UI = "images/canyonbunny-ui.pack.atlas";
//    public static final String TEXTURE_ATLAS_LIBGDX_UI = "images/uiskin.atlas";
    public static final String TEXTURE_ATLAS_LIBGDX_UI = "skin/uiskin.atlas";
    // Location of description file for skins
//    public static final String SKIN_LIBGDX_UI = "images/uiskin.json";
    public static final String SKIN_LIBGDX_UI = "skin/uiskin.json";
    public static final String SKIN_CANYONBUNNY_UI = "images/canyonbunny-ui.json";
    //public static final String PREFERENCES = "canyonbunny.preferences";
    /**
     * 游戏设置
     */
    public static final String PREFERENCES = "pre2.opt";
    /**
     * 角色数据
     */
    public static final String CHARACTER_DATA = "pre1.test";

    /**
     * 游戏按键设置
     */
    public static final String KNIGHT_KEY_SETTING = "pre3.opt";
    public static final String RHYTHM_KEY_SETTING = "pre5.opt";
    public static final String GAMEPAD_SETTING = "pre4.opt";

    /**
     * 手柄模拟键盘输入的配置
     */
    public static final String GAMEPAD_TO_KEYBOARD_SETTING = "pre6.opt";


    public static final String EXTERNAL_APK_FILE ="knightsasha/knightsasha.apk";
    public static final String LOCAL_JAR_FILE ="knightsasha.jar";

//    public static final String EXTERNAL_ROOT="knightsasha";//"musicPlayer";
//------------------------------音游目录----------------------------
    /**
     * 系统文件目录internal
     */
    public static final String SYSTEM_FILE_ROOT="sgcastscreen/SystemLocalData/Txt";
    /**
     * 订阅的目录
     */
    public static final String EXTERNAL_SUBSCRIBE_ROOT ="sgcastscreen/subscribe";
    /**
     * 应该配置文件名
     */
    public static final String SYSTEM_SETTING="sgcastscreen";//系统设置
    public static final String EXTERNAL_ROOT="sgcastscreen";//"musicPlayer";
    public static final String EXTERNAL_MP3_ROOT ="sgcastscreen/mp3";

    //-----------------------cs射击设置------------------
    /**
     * 游戏设置
     */
    public static final String CS_SETTING = "cs.opt";
    // Number of carrots to spawn
    public static final int CARROTS_SPAWN_MAX = 100;
    // Spawn radius for carrots
    public static final float CARROTS_SPAWN_RADIUS = 3.5f;
    // Delay after game finished
    public static final float TIME_DELAY_GAME_FINISHED = 6;

}

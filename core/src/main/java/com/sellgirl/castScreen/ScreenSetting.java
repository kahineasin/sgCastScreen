package com.sellgirl.castScreen;

public class ScreenSetting {

	public static final float WORLD_WIDTH = 1800;
	public static final float WORLD_HEIGHT = 1000;
	public static final float WORLD_HEIGHT_METER =3f;// 4米的话人物像素太低;
	public static  int FPS=60;
	/**
	 * 每帧的时间
	 */
	public static final float SPF=1f/60;
//	/**
//	 * SPF*1.3. 慢1.3倍,我都乐观接受
//	 */
//	public static final float SPFM=1.3f/60;
	/**
	 * 像素:米
	 */
    public static float PIXEL_DIVIDE_METER=WORLD_HEIGHT/WORLD_HEIGHT_METER;
    public static final int FONT_SIZE = 24;

}

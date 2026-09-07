package com.sellgirl.castScreen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.sellgirl.castScreen.screen.MainMenuScreen;
import com.sellgirl.castScreen.screen.SignalScreen;
import com.sellgirl.sgGameHelper.SGFileDownloader;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

import java.util.HashSet;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class CastScreen extends Game implements IKnightSasha // extends ApplicationAdapter
{
    private SpriteBatch batch;
//    private Texture image;

    public BitmapFont font;
    public boolean useFontIncremental=true;
    @Override
    public void create() {
//        batch = new SpriteBatch();
//        image = new Texture("libgdx.png");

        //SGProcessHelper.setManager(new SGProcessPcManager());

        batch = new SpriteBatch();
        //img = new Texture("badlogic.jpg");
//        font = MainMenuScreen.getFont2();
        if(useFontIncremental) {
            font = getFont4();
//            font = getFont2();
        }else{
            font = getFont2();
        }
//        font = MainMenuScreen.getFont3();


        initLibGdx();
        initSG();
//        initProj();
//        initCleanProj();
//        initDemoProj();
//        initNiceProj();

//        if(null==AudioManager.instance){
//            AudioManager.instance=new AudioManager(this);
//        }
//        this.setScreen(new SignalScreen( CastScreen.this));
        this.setScreen(new MainMenuScreen(this));
    }

    private  void initLibGdx(){
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        //SGDataHelper.getLog().print("开始运行: "+SGDate.Now());
    }
//    private String settingKey="sgMusicPlayer";

    private void initSG() {

        //SGDataHelper.sgLog=new SGLibGdxLog();
    }

    //--------------------------FONT---------------------------

    private FreeTypeFontGenerator generator;// TTF字体发生器
    private FreeTypeFontGenerator.FreeTypeBitmapFontData fontData;// 负责处理FreeTypeFontGenerator的数据.可以简单地理解成为一个加工好的字符库
         @Deprecated
     private FreeTypeFontGenerator.FreeTypeBitmapFontData fontData2;
    public boolean hasNewCn=false;
    /**
     * 常用，省内存，但不会刷新本次新增的中文字
     * @return
     */
    public BitmapFont getFont2() {
//		if(null!=font) {return font;}
        if(null==generator) {

            /**
             * 以下是进行初始化
             */
            generator = new FreeTypeFontGenerator(Gdx.files.internal("simhei.ttf"));//generator这个东西就算dispose也没用的,内存增加9MB,没办法了
        }

        if(hasNewCn&&null!=fontData){
            fontData.dispose();
            fontData=null;
        }
        if(null==fontData) {//测试发现fontData就算dispose了,还是消耗内存,原因不明
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            FileHandle file = Gdx.files.internal("font_cn.txt");
//			String text = file.readString(SGDataHelper.encoding);
            String text = file.readString("utf8");

            parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS
                +"△○□↑→↓←↖↗↘↙∞"
                + text+readExCn();

            parameter.size=ScreenSetting.FONT_SIZE;//24
            fontData = generator.generateData(parameter);

            if(hasNewCn){hasNewCn=false;}
        }

        BitmapFont font = new BitmapFont(fontData, fontData.regions, false);
        font.setColor(Color.WHITE);

        return font;
    }
    /**
     * harmony有些情况下（比如屏幕右侧向内推时）,会进入Game.dispose方法，
     * 之后旧的generator会导致文字变黑块（可能被系统自动释放了）。
     * 所以game.create时要重新new fontData
     */
    public void disposeFont(){
        if(null!=fontData){
            fontData.dispose();
            fontData=null;
        }
         if(null!=fontData2){
             fontData2.dispose();
             fontData2=null;
         }
    }

    /**
     * 使用gdx原生的动态字体属性parameter.incremental。不用自行维护font.txt了，但性能待测试
     *
     * @deprecated  测试发现，使用incremental=true，当组件文字有变化时，会增加FreeTypeFontGenerator.createGlyph方法的调用次数
     * @return
     */
    @Deprecated
    public  BitmapFont getFont4() {
//		if(null!=font) {return font;}
        if(null==generator) {

            /**
             * 以下是进行初始化
             */
            generator = new FreeTypeFontGenerator(Gdx.files.internal("simhei.ttf"));//generator这个东西就算dispose也没用的,内存增加9MB,没办法了
        }

//        if(hasNewCn&&null!=fontData){
//            fontData.dispose();
//            fontData=null;
//        }
        if(null==fontData2) {//测试发现fontData就算dispose了,还是消耗内存,原因不明
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//            FileHandle file = Gdx.files.internal("font_cn.txt");
////			String text = file.readString(SGDataHelper.encoding);
//            String text = file.readString("utf8");

            parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS
                +"△○□↑→↓←↖↗↘↙∞"
            //    + text+readExCn()
            ;
            parameter.incremental=true;
            parameter.size=ScreenSetting.FONT_SIZE;//24
            fontData2 = generator.generateData(parameter);

//            if(hasNewCn){hasNewCn=false;}
        }

        BitmapFont font = new BitmapFont(fontData2, fontData2.regions, false);
        font.setColor(Color.WHITE);

        return font;
    }
//    public boolean addIfHasNewCn(String s) {
//        if (null == s || s.isEmpty()) {
//            return false;
//        }
//        FileHandle file2 = Gdx.files.external(Constants.EXTERNAL_ROOT + "/font_cn.txt");
//        String origin = "";
//        if (file2.exists()) {
//            origin = file2.readString(SGDataHelper.encoding);
//            StringBuilder result = new StringBuilder();
//            HashSet<Character> set = new HashSet<>();
//            for (char c : origin.toCharArray()) {
//                if (!set.contains(c)) {
//                    set.add(c);
////                    result.append(c);
//                }
//            }
//            for (char c : s.toCharArray()) {
////                if (!set.contains(c)) {
////                    set.add(c);
////                    result.append(c);
////                }
//
//                if ((!set.contains(c))
//                    //&&SGDataHelper.isCnChar(c)
//                    &&(SGDataHelper.isCnChar(c)|| SGDataHelper.isJapaneseChar(c))
//                ) {
//                    set.add(c);
//                    result.append(c);
//                }
//            }
//            if (!result.isEmpty()) {
//                file2.writeString(result.toString(), true, SGDataHelper.encoding);
//                hasNewCn = true;
//                return true;
//            }
//
//        } else {
//            StringBuilder result = new StringBuilder();
//            HashSet<Character> set = new HashSet<>();
//            for (char c : s.toCharArray()) {
//                if ((!set.contains(c))&&SGDataHelper.isCnChar(c)) {
//                    set.add(c);
//                    result.append(c);
//                }
//            }
//            String sResult=result.toString();
//            if (//!result.isEmpty()
//                !SGDataHelper.StringIsNullOrWhiteSpace(sResult)
//            ) {
//                file2.writeString(//result.toString()
//                    sResult
//                    , true, SGDataHelper.encoding);
//                hasNewCn = true;
//                return true;
//            }
//        }
//        return false;
//    }


public static String readExCn(){

    FileHandle file2 = Gdx.files.external(Constants.EXTERNAL_ROOT+"/font_cn.txt");
    String text2="";
    if(file2.exists()){
        text2=file2.readString(SGDataHelper.encoding);
    }
    return text2;
}

    @Override
    public void render() {
//        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
//        batch.begin();
//        batch.draw(image, 140, 210);
//        batch.end();
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
//        image.dispose();

        if(null!=font){
            font.dispose();
            font=null;
        }
//        this.screen=null;
//        MainMenuScreen.hasNewCn=true;
//        MainMenuScreen.disposeFont();
        disposeFont();

        //img.dispose();

//        AudioManager.instance.dispose();
//        AudioManager.instance=null;
    }

//--------------------------样式------------------


    public static TextButton.TextButtonStyle getButtonStyle(Skin skin) {

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);//DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        return textButtonStyle;
    }

    public static Label.LabelStyle getLabelStyle(Skin skin) {

        Label.LabelStyle textButtonStyle = new Label.LabelStyle();
        textButtonStyle.font = skin.getFont("default");
        // textButtonStyle.fontColor = Color.BLACK;
        return textButtonStyle;
    }

    public static TextField.TextFieldStyle getTextFieldStyle(Skin skin) {

        TextField.TextFieldStyle textButtonStyle = new TextField.TextFieldStyle();
        textButtonStyle.font = skin.getFont("default");
        textButtonStyle.fontColor = Color.BLACK;
        textButtonStyle.background = skin.newDrawable("white", Color.YELLOW);
        return textButtonStyle;
    }

    public static Window.WindowStyle getWindowStyle(Skin skin) {

        Window.WindowStyle textButtonStyle = new Window.WindowStyle();
//		textButtonStyle.font = skin.getFont("default");
//		textButtonStyle.fontColor = Color.BLACK;
        textButtonStyle.titleFont = skin.getFont("default");
        // textButtonStyle.titleFontColor = Color.BLACK;
        Drawable tmp= skin.newDrawable("white", Color.GRAY);
//		tmp.setMinWidth(500);
//		tmp.setMinHeight(500);
//		textButtonStyle.background = skin.newDrawable("white", Color.GRAY);
        textButtonStyle.background = tmp;
        //textButtonStyle.titleFontColor=Color.WHITE;
        return textButtonStyle;
    }

    public static TextButton.TextButtonStyle getTextButtonStyle(Skin skin) {

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = skin.getFont("default");
        textButtonStyle.fontColor = Color.BLACK;
        // textButtonStyle.background = skin.newDrawable("white", Color.YELLOW);
        return textButtonStyle;
    }
    public static CheckBox.CheckBoxStyle getCheckBoxStyle(Skin skin) {

        CheckBox.CheckBoxStyle textButtonStyle = new CheckBox.CheckBoxStyle();
        textButtonStyle.font = skin.getFont("default");
        textButtonStyle.fontColor = Color.WHITE;
        int w=10;
        int w2=10;
        Drawable tmp=skin.newDrawable("white", Color.LIGHT_GRAY);
        tmp.setMinWidth(w);
        tmp.setMinHeight(w);
//		textButtonStyle.checkboxOn = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checkboxOn = tmp;
        Drawable tmp2=skin.newDrawable("white", Color.DARK_GRAY);
        tmp2.setMinWidth(w2);
        tmp2.setMinHeight(w2);
//		textButtonStyle.checkboxOff = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.checkboxOff = tmp2;
        // textButtonStyle.background = skin.newDrawable("white", Color.YELLOW);
        return textButtonStyle;
    }
    public static Slider.SliderStyle getSliderStyle(Skin skin) {

        Slider.SliderStyle textButtonStyle = new Slider.SliderStyle();
        Drawable tmp2=skin.newDrawable("white", Color.YELLOW);
        tmp2.setMinHeight(3);
        textButtonStyle.background = tmp2;
        Drawable tmp=skin.newDrawable("white", Color.RED);
//		 int border=4;
//		 tmp.setTopHeight(border);
//		 tmp.setBottomHeight(border);
//		tmp.setLeftWidth(border);
//		tmp.setRightWidth(border);
        tmp.setMinWidth(6);
        tmp.setMinHeight(14);
        textButtonStyle.knob=tmp;
        return textButtonStyle;
    }

    public static List.ListStyle getListStyle(Skin skin) {

        List.ListStyle textButtonStyle = new List.ListStyle();
//		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);//DARK_GRAY);
//		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
//		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
//		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        Drawable tmp2=skin.newDrawable("white", Color.PURPLE);
        tmp2.setMinHeight(3);
        textButtonStyle.selection = tmp2;
        Drawable tmp1=skin.newDrawable("white", Color.BLUE);
        tmp1.setMinHeight(3);
        textButtonStyle.background = tmp1;//非必需,但设置了可以挡住后面
        return textButtonStyle;
    }
    public static ScrollPane.ScrollPaneStyle getScrollStyle(Skin skin) {

        ScrollPane.ScrollPaneStyle textButtonStyle = new ScrollPane.ScrollPaneStyle();
//		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);//DARK_GRAY);
//		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
//		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
//		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
//		textButtonStyle.font = skin.getFont("default");
//		Drawable tmp2=skin.newDrawable("white", Color.YELLOW);
//		tmp2.setMinHeight(3);
//		textButtonStyle.selection = tmp2;
        return textButtonStyle;
    }
    public static SelectBox.SelectBoxStyle getSelectBoxStyle(Skin skin) {

        SelectBox.SelectBoxStyle textButtonStyle = new SelectBox.SelectBoxStyle();
//		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);//DARK_GRAY);
//		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
//		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
//		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        textButtonStyle.listStyle=getListStyle(skin);
        textButtonStyle.scrollStyle=getScrollStyle(skin);
        return textButtonStyle;
    }

    public static ProgressBar.ProgressBarStyle getProgressBarStyle(Skin skin) {

        ProgressBar.ProgressBarStyle textButtonStyle = new ProgressBar.ProgressBarStyle();
        Drawable tmp2=skin.newDrawable("white", Color.YELLOW);
        tmp2.setMinHeight(3);
//		textButtonStyle.background = tmp2;
        Drawable tmp=skin.newDrawable("white", Color.RED);
//		 int border=4;
//		 tmp.setTopHeight(border);
//		 tmp.setBottomHeight(border);
//		tmp.setLeftWidth(border);
//		tmp.setRightWidth(border);
        tmp.setMinWidth(6);
        tmp.setMinHeight(14);
        textButtonStyle.knob=tmp;
        Drawable tmp1=skin.newDrawable("white", Color.BLUE);
        tmp1.setMinHeight(3);
        textButtonStyle.background = tmp1;//非必需,但设置了可以挡住后面
        return textButtonStyle;
    }
//	/**
//	 * 风格为:
//	 * 白色字
//	 * 已启用: 白色
//	 * 未启用: 深色
//	 * @return
//	 */
//	public static Skin getSkin() {
//		// A skin can be loaded via JSON or defined programmatically, either is fine.
//		// Using a skin is optional but strongly
//		// recommended solely for the convenience of getting a texture, region, etc as a
//		// drawable, tinted drawable, etc.
//		Skin skin = new Skin();
//
//		// Generate a 1x1 white texture and store it in the skin named "white".
//		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
//		pixmap.setColor(Color.WHITE);
//		pixmap.fill();
//		skin.add("white", new Texture(pixmap));
//
//
//        skin.add("default", getFont2());
//		return skin;
//	}

    /**
     * 一套的样式，原本没有中文字
     *
     * @param font
     * @return
     */
    public static Skin getSkin2(BitmapFont font) {
        Skin skin = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI), new com.badlogic.gdx.graphics.g2d.TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));

        //注意这里字体改为了黑色，不知道有什么用
//        font.setColor(Color.BLACK);
        //font.setColor(0,0,0,1);
        //为了中文字体
        skin.get(TextButton.TextButtonStyle.class).font=font;
        skin.get(Label.LabelStyle.class).font=font;
        skin.get(SelectBox.SelectBoxStyle.class).font=font;
        return skin;
    }

    private IDLNADeviceScanner scanner;
    public void setScanner(IDLNADeviceScanner scanner){
        this.scanner=scanner;
    }
    public IDLNADeviceScanner getScanner(){
        return scanner;
    }

    private IDLNADeviceCaster caster;
    public IDLNADeviceCaster getCaster() {
        return caster;
    }

    public void setCaster(IDLNADeviceCaster caster) {
        this.caster = caster;
    }

    private String sysIp;
    @Override
    public String getSysIp() {
        return sysIp;
    }

    @Override
    public void setSysIp(String sysIp) {
        this.sysIp=sysIp;
    }

    @Override
    public BitmapFont getFont() {
        return font;
    }

    @Override
    public BitmapFont getFont3(int size) {
        return null;
    }

    @Override
    public Batch getBatch() {
        return batch;
    }

    @Override
    public boolean isShowTouchpad() {
        return false;
    }

    @Override
    public boolean isRelease() {
        return false;
    }

    @Override
    public boolean isHasKeyboard() {
        return false;
    }

    @Override
    public SGFileDownloader getJarDownloader() {
        return null;
    }

    @Override
    public void setJarDownloader(SGFileDownloader d) {

    }

    @Override
    public boolean isSupportCloudSave() {
        return false;
    }

    @Override
    public boolean isSteam() {
        return false;
    }
}

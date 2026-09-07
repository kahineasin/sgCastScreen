package com.sellgirl.castScreen.screen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.touchable;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
//import com.mygdx.game.demo.gamepadStick.GamepadStickMainScreen;
//import com.mygdx.game.demo.piano.GamepadPianoScreen;
//import com.mygdx.game.sasha.AndroidKnightSasha;
//import com.mygdx.game.sasha.GameSetting;
//import com.mygdx.game.sasha.IKnightSasha;
//import com.mygdx.game.sasha.SGCharacter;
//import com.mygdx.game.sasha.SashaData;
//import com.mygdx.game.sasha.SteamKnightSasha;
//import com.mygdx.game.sasha.bulletD3.GameKey;
//import com.mygdx.game.sasha.bulletD3.GameKeyKeyboard;
//import com.mygdx.game.sasha.bulletD3.IKnightSashaGameKey;
//import com.mygdx.game.sasha.language.CN;
//import com.mygdx.game.sasha.language.Country;
//import com.mygdx.game.sasha.language.SGTxt;
//import com.mygdx.game.sasha.language.TXT;
//import com.mygdx.game.sasha.seariver.screen.RhythmMenuScreen;
//import com.mygdx.game.sasha.url.CnUrl;
//import com.mygdx.game.sasha.url.IApiUrl;
//import com.mygdx.game.sasha.util.AudioManager;
//import com.mygdx.game.sasha.util.Constants;
//import com.mygdx.game.sasha.util.GamePreferences;
//import com.mygdx.game.share.ISGCloudSave;
//import com.mygdx.game.share.ScreenSetting;
import com.sellgirl.castScreen.CastScreen;
import com.sellgirl.castScreen.Constants;
import com.sellgirl.castScreen.GameKey;
import com.sellgirl.castScreen.GameKeyKeyboard;
import com.sellgirl.castScreen.IKnightSasha;
import com.sellgirl.castScreen.IKnightSashaGameKey;
import com.sellgirl.castScreen.ScreenSetting;
import com.sellgirl.castScreen.language.TXT;
import com.sellgirl.sgGameHelper.SGConfirmPopups;
import com.sellgirl.sgGameHelper.SGFileDownloader;
import com.sellgirl.sgGameHelper.SGLibGdxHelper;
import com.sellgirl.sgGameHelper.gamepad.AutoConnectGamepad;
import com.sellgirl.sgGameHelper.gamepad.ISGPS5Gamepad;//import com.mygdx.game.share.gamepad.ISGPS5Gamepad;
import com.sellgirl.sgGameHelper.gamepad.SGKeyboardGamepad;//import com.mygdx.game.share.gamepad.SGKeyboardGamepad;
import com.sellgirl.sgGameHelper.gamepad.SGPS5Gamepad;//import com.mygdx.game.share.gamepad.SGPS5Gamepad;
import com.sellgirl.sgGameHelper.tabUi.SGTabUDLRMap;//import com.mygdx.game.share.tabUi.SGTabUDLRMap;
import com.sellgirl.sgGameHelper.tabUi.TabUi;//import com.mygdx.game.share.tabUi.TabUi;
import com.sellgirl.sgJavaHelper.SGAction1;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGEmailSend;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

import java.io.File;
import java.util.HashMap;
import java.util.function.Consumer;

//import com.mygdx.game.share.SGGameHelper;
//import com.mygdx.game.share.SGLibGdxHelper;

public class MainMenuScreen implements Screen {
//	private static final String tag="MainMenuScreen";
	private static  final String tag=MainMenuScreen.class.getName();
//	private final Stage stage;
	private Stage stage;
	private final Table table;
	final IKnightSasha game;
	private final boolean isSteam;
//	private SGWaiter steamWaiter;
//	private String gameVersion="1.0.0";
	// OrthographicCamera camera;
	@Deprecated
	Controller controller;
	private boolean isSonyTvController=false;
//	SGPS5Gamepad sgcontroller;
	ISGPS5Gamepad sgcontroller;
	IKnightSashaGameKey gameKey;
	SGXInputControllerListener controllerListener;
//	private static IApiUrl url = null;
//	SashaData sasha = null;
	private  Skin skin;
	TextButton enterGameBtn;
	TextButton gamePadTestBtn;
	TabUi tabUi;

	private String sysStr;
	private void testReadFile(String path){
		testReadResourceAsStream(path);
		testReadByLibGdx(path);
		testReadResourceBySellgirl(path);
	}
	private void testReadResourceBySellgirl(String path){
		try {
//			idea:ok; java -jar:ok;  exe混淆:ok; exe不混淆:??
			String s=SGDataHelper.readAnylResource(path,10);
			SGDataHelper.getLog().print("testReadResourceBySellgirl :\r\n "+(null!=s?s:"null"));

		}catch (Throwable e){}
	}
	private void testReadResourceAsStream(String path){
		try {
//			idea:no; java -jar:ok;  exe混淆:ok; exe不混淆:no
			SGDataHelper.getLog().print("MainMenuScreen.class.getResourceAsStream ok:"+(null!=MainMenuScreen.class.getResourceAsStream(path)));
//			idea:no; java -jar:ok;  exe混淆:ok; exe不混淆:no
			SGDataHelper.getLog().print("SGDataHelper.class.getResourceAsStream ok:"+(null!=SGDataHelper.class.getResourceAsStream(path)));
//			idea:ok; java -jar:ok;  exe混淆:no; exe不混淆:no
			SGDataHelper.getLog().print("ClassLoader.getSystemResourceAsStream ok:"+(null!=ClassLoader.getSystemResourceAsStream(path)));
//		if(source==null) throw new IOException("Cannot open resource from classpath "+path);
		}catch (Throwable e){}
	}
	private void testReadByLibGdx(String path){
		try {
//			idea:ok; java -jar:ok; exe混淆:ok; exe不混淆:ok
			SGDataHelper.getLog().print("Gdx.files.internal ok:"+(null!=Gdx.files.internal(path).read()));
//			idea:ok; exe混淆:ok; exe不混淆:ok
			SGDataHelper.getLog().print("FileHandle.class.getResourceAsStream('/xx') ok:"+(null!=FileHandle.class.getResourceAsStream("/" + path.replace('\\', '/'))));
//			idea:no; exe混淆:no; exe不混淆:no
			SGDataHelper.getLog().print("new FileInputStream(new File(Gdx.files.getExternalStoragePath(), path)) ok:"+(new File(Gdx.files.getExternalStoragePath(), path)).exists());
//			idea:no; exe混淆:no; exe不混淆:no
			SGDataHelper.getLog().print("new FileInputStream(new File(path)) ok:"+(new File(path)).exists());
//		if(source==null) throw new IOException("Cannot open resource from classpath "+path);
		}catch (Throwable e){
			SGDataHelper.getLog().printException(e,tag+"testReadByLibGdx");
		}
	}

	public MainMenuScreen(final IKnightSasha game) {
//		public MainMenuScreen( SashaGame game) {
		this.game = game;
//		isSteam=game instanceof SteamKnightSasha;
        isSteam=false;
		supportCloudSave=game.isSupportCloudSave();
//		cloudSave=game.cloudSave();

//		sasha = readSashaData();
//
//		if ((null!=game.getCountry()&&Country.CN==game.getCountry())
//				||(null==game.getCountry()&&Country.CN == sasha.getCountry())) {
//			url = new CnUrl();
//
//			if(GameSetting.isGameX()) {
//				Map<String,String> m=CN.get();
//				m.putAll(GameSetting.getTXTX().getCN());
//				TXT.init(m);
//			}else {
//				TXT.init(CN.get());
//			}
//		}else{
//			url = new CnUrl();
//			TXT.init(null);
//		}
//		SGGameHelper.setLanguage(new SGTxt());

//		getSashaVip();

//		double now=SashaData.getDaysBetween(SGDate.Now());

//		if(now>sasha.getLastLogin()) {
//			sasha.setLastLogin(now);
//			saveSashaData(sasha);
//			String deviceName=sasha.getUserId();
//			if("0".equals(sasha.getUserId())){
//				try {
////					deviceName=InetAddress.getLocalHost().getHostName();
//					deviceName=SGDataHelper.getDeviceName();
//
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			SGEmailSend.SendMail(new String[] { SGEmailSend.EMAIL_OWNER_ADDR },
//					"knightSasha_" + deviceName + "_登陆通知", deviceName + "登陆通知,isGameX "+GameSetting.isGameX());
//
//
//		}

		//控制器

//		System.out.println("main screen b"+(++cnt));
		this.controller = SGLibGdxHelper.getGamepad();
//		System.out.println("main screen e"+(cnt));

		if (null != controller) {
			isSonyTvController=checkSonyTvController(controller);
			sgcontroller=new SGPS5Gamepad(controller);
			this.controller.addListener(new SGXInputControllerListener());
//			controllerListener=new SGXInputControllerListener();
//			Controllers.addListener(controllerListener);
		}
		//进入菜单才连接手柄时,就靠这个了
		controllerListener=new SGXInputControllerListener();
		Controllers.addListener(controllerListener);
		if(null!=sgcontroller){
//			ArrayList<SGPS5Gamepad> list=new ArrayList<>();
//			list.add(sgcontroller);
//			autoConnectGamepad= new AutoConnectGamepad(list);
			autoConnectGamepad= new AutoConnectGamepad();
			autoConnectGamepad.addGamepad(sgcontroller);


		}
		if(null==sgcontroller&&game.isHasKeyboard()){
			sgcontroller=new SGKeyboardGamepad();
			gameKey=new GameKeyKeyboard();
			gameKey.setGamepad(sgcontroller);
		}

		if(null!=sgcontroller){

			if(SGKeyboardGamepad.class==sgcontroller.getClass()){
				gameKey=new GameKeyKeyboard();
			}else{
				gameKey=new GameKey();
			}
			gameKey.setGamepad(sgcontroller);
		}

//		if(null==sgcontroller){
//			Controllers.addListener(this.controllerListener);
//		}

		controllerCnt= Controllers.getControllers().size;
		controllerNames=new String[controllerCnt];
		int i=0;
		for (Controller controller : Controllers.getControllers()) {
//			controllerNames[i]=controller.getName()+"("+controller.getClass().getName()+")";//android平台的class就是AndroidController,所以不需要看
			controllerNames[i]=controller.getName();
			i++;
		}

		// 使用伸展视口（StretchViewport）创建舞台
		try {
			stage = new Stage(new StretchViewport(ScreenSetting.WORLD_WIDTH, ScreenSetting.WORLD_HEIGHT));
		}catch (Throwable e){
			SGDataHelper.getLog().print(e);
		}


//      /* 事件初始化 */
//
		// 首先必须注册输入处理器（stage）, 将输入的处理设置给 舞台（Stage 实现了 InputProcessor 接口）
		// 这样舞台才能接收到输入事件, 分发给相应的演员 或 自己处理。
		Gdx.input.setInputProcessor(stage);


//		skin = MainMenuScreen.getSkin();
        skin = CastScreen.getSkin2(game.getFont());
        skin.add("default",game.getFont());

		skin.add("default", MainMenuScreen.getButtonStyle(skin));
		skin.add("default", MainMenuScreen.getLabelStyle(skin));
		skin.add("default", MainMenuScreen.getTextFieldStyle(skin));
		skin.add("default", MainMenuScreen.getWindowStyle(skin));
		skin.add("default", MainMenuScreen.getCheckBoxStyle(skin));
		skin.add("default-horizontal", MainMenuScreen.getSliderStyle(skin));
		skin.add("default-horizontal", MainMenuScreen.getProgressBarStyle(skin));

		//skinLibgdx=skin;
		//skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));
		skinLibgdx = new Skin(Gdx.files.internal("skin/uiskin.json"), new TextureAtlas("skin/uiskin.atlas"));


		table = new Table();
		table.setFillParent(true);

//		TextButton button1=null;
//		if(!isSteam) {
//			 button1 = new TextButton(TXT.g("edit userInfo, or press TRIANGLE"), skin);
////		TextButton button1 = new TextButton("edit userInfo, or press △", skin);
//			button1.addListener(new ClickListener() {
//
//				@Override
//				public void clicked(InputEvent event, float x, float y) {
//
//					game.setScreen(new UserInfoScreen(game, sasha));
//					dispose();
//				}
//			});
//		}
//
////		enterGameBtn = new TextButton(TXT.g("enter the game, or press X"), skin);
//		enterGameBtn = new TextButton(TXT.g("enter the game"), skin);
////		TextButton enterKofGameBtn = new TextButton(TXT.g("enter the KOF game, or press ")+gameKey.getKeyName(IKnightSashaGameKey.KnightKey.X), skin);
//		TextButton enterKofGameBtn = new TextButton(
//				null!=gameKey
//				?(TXT.g("enter the KOF game, or press ")
//					+gameKey.getKeyNamesByMask(gameKey.getAttack()))
//				:TXT.g("enter the KOF game")
//				, skin);
////		TextButton enterD3GameBtn = new TextButton(TXT.g("enter the 3d game, or press L1"), skin);
////		TextButton enterD3GameBtn = new TextButton(TXT.g("enter the 3d cooperative game, or press ")+gameKey.getKeyName(IKnightSashaGameKey.KnightKey.L1), skin);
//		TextButton enterD3GameBtn = new TextButton(
//				null!=gameKey
//				?(TXT.g("enter the 3d cooperative game, or press ")
//					+gameKey.getKeyNamesByMask(gameKey.getDefend()))
//				:TXT.g("enter the 3d cooperative game")
//				,
//				skin);
//		TextButton enterRhythmGameBtn=null;
//		if(!isSteam) {
//			 enterRhythmGameBtn = new TextButton(TXT.g("enter the rhythm game, or press R1"), skin);
//		}

//		TextButton optionBtn = new TextButton(TXT.g("game setting"), skin);
//		TextButton keySettingBtn = new TextButton(TXT.g("key setting"), skin);
        TextButton exitGameBtn = new TextButton(TXT.g("exit game "), skin);
		TextButton castImgBtn = new TextButton(TXT.g("cast URL"), skin);
        TextButton castFileBtn = new TextButton(TXT.g("cast FILE"), skin);
        TextButton castDeviceBtn = new TextButton(TXT.g("cast DEVICE"), skin);
		if(!game.isRelease()) {
			gamePadTestBtn = new TextButton(TXT.g("gamepad test"), skin);
		}
		TextButton leadBtn =null;
		if(isSteam){leadBtn =new TextButton(TXT.g("leaderboard"), skin);}

//		TextButton pianoBtn =null;
//		if(null!=game.getMidiMusic()) {
//			pianoBtn=new TextButton(TXT.g("gamepad piano mini game"), skin);
//		}
//		TextButton csBtn =null;
//		csBtn=new TextButton(TXT.g("cs game"), skin);
//		enterGameBtn.addListener(new ClickListener() {
//
//			@Override
//			public void clicked(InputEvent event, float x, float y) {
//				// System.out.println("-----------goToGuidePage-------------");
////				//gotoGamePage();
//				goToGuidePage();
//			}
//		});
//		enterD3GameBtn.addListener(new ClickListener() {
//
//			@Override
//			public void clicked(InputEvent event, float x, float y) {
//				goToGameD3Page();
//			}
//		});
//
//		enterKofGameBtn.addListener(new ClickListener() {
//
//			@Override
//			public void clicked(InputEvent event, float x, float y) {
//				goToKofCharacterPage();
//			}
//		});
//
//		if(null!=enterRhythmGameBtn) {
//			enterRhythmGameBtn.addListener(new ClickListener() {
//
//				@Override
//				public void clicked(InputEvent event, float x, float y) {
//					goToRhythmPage();
//				}
//			});
//		}
//		optionBtn.addListener(new ClickListener() {
//
//			@Override
//			public void clicked(InputEvent event, float x, float y) {
//				onOptionsClicked();
//			}
//		});
//		keySettingBtn.addListener(new ClickListener() {
//
//			@Override
//			public void clicked(InputEvent event, float x, float y) {
//				goToKeySettingPage();
//			}
//		});

        castImgBtn.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                goToCastImgScreen();
            }
        });
        castFileBtn.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                goToCastFileScreen();
            }
        });

        castDeviceBtn.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                goToCastDeviceScreen();
            }
        });
		exitGameBtn.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				exitGame();
			}
		});

//		if(null!=gamePadTestBtn) {
//			gamePadTestBtn.addListener(new ClickListener() {
//
//				@Override
//				public void clicked(InputEvent event, float x, float y) {
//					goToGamepadTestPage();
//				}
//			});
//		}
//
//		if(null!=leadBtn) {
//			leadBtn.addListener(new ClickListener() {
//
//				@Override
//				public void clicked(InputEvent event, float x, float y) {
//					goToLeadPage();
//				}
//			});
//		}
//
//		if(null!=game.getMidiMusic()) {
//			pianoBtn.addListener(new ClickListener() {
//				@Override
//				public void clicked(InputEvent event, float x, float y) {
//					goToPianoPage();
//				}
//			});
//		}
//
//		csBtn.addListener(new ClickListener() {
//			@Override
//			public void clicked(InputEvent event, float x, float y) {
//				goToCsPage();
//			}
//		});

		int buttonSpace=20;

//		table.add(enterGameBtn).spaceBottom(buttonSpace);
//		table.row();
//		if(null!=button1){
//		table.add(button1).spaceBottom(buttonSpace);
//		table.row();
//		}
//		table.add(enterKofGameBtn).spaceBottom(buttonSpace);
//		table.row();
//		table.add(enterD3GameBtn).spaceBottom(buttonSpace);
//		table.row();
//
//		if(null!=enterRhythmGameBtn) {
//			table.add(enterRhythmGameBtn).spaceBottom(buttonSpace);
//			table.row();
//		}
//		table.add(optionBtn).spaceBottom(buttonSpace);
//		table.row();
//		table.add(keySettingBtn).spaceBottom(buttonSpace);
//		if(null!=gamePadTestBtn) {
//			table.row();
//			table.add(gamePadTestBtn).spaceBottom(buttonSpace);
//		}
//		table.row();
//		if(null!=leadBtn){
//			table.add(leadBtn).spaceBottom(buttonSpace);
//			table.row();
//		}
//
//		if(null!=game.getMidiMusic()) {
//			table.add(pianoBtn).spaceBottom(buttonSpace);
//			table.row();
//		}
//		table.add(csBtn).spaceBottom(buttonSpace);
//		table.row();
		table.add(castImgBtn).spaceBottom(buttonSpace);
		table.row();
        table.add(castFileBtn).spaceBottom(buttonSpace);
        table.row();
        table.add(castDeviceBtn).spaceBottom(buttonSpace);
        table.row();

		table.add(exitGameBtn)//.spaceBottom(buttonSpace)
		;

		stage.addActor(table);
		tabUi=new TabUi();
		tabUi.setItem(table.getChildren());


		Table layerOptionsWindow = buildOptionsWindowLayer();
		stage.addActor(layerOptionsWindow);


//
////		System.out.println("main screen b"+(++cnt));
//		this.controller = SGLibGdxHelper.getGamepad();
////		System.out.println("main screen e"+(cnt));
//
//		if (null != controller) {
//			sgcontroller=new SGPS5Gamepad(controller);
//			this.controller.addListener(new SGXInputControllerListener());
//			controllerListener=new SGXInputControllerListener();
//			Controllers.addListener(controllerListener);
//		}
//		if(null!=sgcontroller){
////			ArrayList<SGPS5Gamepad> list=new ArrayList<>();
////			list.add(sgcontroller);
////			autoConnectGamepad= new AutoConnectGamepad(list);
//			autoConnectGamepad= new AutoConnectGamepad();
//			autoConnectGamepad.addGamepad(sgcontroller);
//
//
//		}
//		if(null==sgcontroller&&game.isHasKeyboard()){
//			sgcontroller=new SGKeyboardGamepad();
//			gameKey=new GameKeyKeyboard();
//			gameKey.setGamepad(sgcontroller);
//		}
//
//		if(null!=sgcontroller){
//
//			if(SGKeyboardGamepad.class==sgcontroller.getClass()){
//				gameKey=new GameKeyKeyboard();
//			}else{
//				gameKey=new GameKey();
//			}
//			gameKey.setGamepad(sgcontroller);
//		}
//
//		controllerCnt= Controllers.getControllers().size;
//		controllerNames=new String[controllerCnt];
//		int i=0;
//		for (Controller controller : Controllers.getControllers()) {
//			controllerNames[i]=controller.getName();
//			i++;
//		}

		if(null==game.getJarDownloader()&&
				(
						//(Application.ApplicationType.Android==Gdx.app.getType()&&!GameSetting.isGameX())
			//			||
            Application.ApplicationType.Desktop==Gdx.app.getType()
				)) {
			getLastVersion(new SGAction1<String>() {
				@Override
				public void go(String s) {
					String version = s;
					if (null != version && 0 < SGDataHelper.compareVersion(version, gameVersion)) {

						 updateJarPopups= new SGConfirmPopups(SGDataHelper.FormatString(TXT.g("found new version {0}, update now?"), version),
								new Consumer<Object>() {
									@Override
									public void accept(Object o) {
//										if (Application.ApplicationType.Android == Gdx.app.getType()) {
											downloadJar(true);
//										}
									}
								},
								skin
						);
						updateJarPopups.show(stage);
//                // 将对话框右下
						updateJarPopups.setPosition(
								(stage.getWidth() - updateJarPopups.getWidth()) *7f/ 8f,
								(stage.getHeight() - updateJarPopups.getHeight()) / 8f);
//			String aa="";
					}
				}
			});
		}

		sysStr= Gdx.app.getType().toString()+" | "+Gdx.app.getVersion();
	}

	private AutoConnectGamepad autoConnectGamepad;
	private final int controllerCnt;
	private final String[] controllerNames;
	// private int cnt=0;


	@SuppressWarnings("unused")
	private void initTestSashaData() {
//		// readUserData();
//		sasha = SashaData.init();
//		// sasha = readSashaData();
//		sasha.setCountry(Country.CN);
//		sasha.setEmail("li@sellgirl.com");
//		sasha.setUserId("li@sellgirl.com");
//		saveSashaData(sasha);
	}

	@Override
	public void show() {
		// System.out.println("listeners count "+enterGameBtn.getListeners().size);

	}
//private final float buttonWait=0;
//	private float buttonWaitCount=0.4f;
	private final float  buttonWait=0.4f;
	private float buttonWaitCount=1f; //这个要设置为所有wait的max值
	@Override
	public void render(float delta) {
		if(null==stage){
			return;
		}


//		if(0<buttonWait){
//			buttonWait-=delta;
		//}
		if (0<buttonWaitCount ) {
			buttonWaitCount -= delta;
		} else if (buttonWaitCount < 0) {
			buttonWaitCount = 0;
		}

		if(!leaving){
			if(null!=winOptions&&winOptions.isTouchable()){
				//弹窗打开时
				if(0>=buttonWaitCount){
					if (optionsWindowTabUi.isEditing()) {
						//tabUi编辑状态
						if (0 >= buttonWaitCount) {
							if (sgcontroller.isROUND()) {
								optionsWindowTabUi.select();
								buttonWaitCount = buttonWait;
							} else {
								if (optionsWindowTabUi.edit(sgcontroller)) {

									buttonWaitCount = buttonWait;
								}
							}
						}
					}else {
						if (null != sgcontroller && (sgcontroller.isUP()||sgcontroller.isStick1Up())) {
							optionsWindowTabUi.up();
							buttonWaitCount = buttonWait;
						} else if (null != sgcontroller && (sgcontroller.isDOWN()||sgcontroller.isStick1Down())) {
							optionsWindowTabUi.down();
							buttonWaitCount = buttonWait;
						} if (null != sgcontroller && (sgcontroller.isLEFT()||sgcontroller.isStick1Left())) {
							optionsWindowTabUi.left();
							buttonWaitCount = buttonWait;
						} else if (null != sgcontroller && (sgcontroller.isRIGHT()||sgcontroller.isStick1Right())) {
							optionsWindowTabUi.right();
							buttonWaitCount = buttonWait;
						} else if (null != sgcontroller && sgcontroller.isCROSS()) {
							optionsWindowTabUi.select();
							buttonWaitCount = buttonWait;
						} else if (null != sgcontroller && sgcontroller.isROUND()) {
							//popups.hide();
							showOptionsWindow(false, true);
						}
					}
				}
			}
			else if(null!=updateJarPopups&&null!= updateJarPopups.getStage()){

				if(0>=buttonWaitCount) {
	//			if (null != sgcontroller && (sgcontroller.isUP()||sgcontroller.isStick1Up())) {
	//				updateJarPopups.up();
	//				buttonWaitCount = buttonWait;
	//			} else if (null != sgcontroller && (sgcontroller.isDOWN()||sgcontroller.isStick1Down())) {
	//				optionsWindowTabUi.down();
	//				buttonWaitCount = buttonWait;
	//			}
					if (null != sgcontroller && (sgcontroller.isLEFT() || sgcontroller.isStick1Left())) {
						updateJarPopups.getTab().left();
						buttonWaitCount = buttonWait;
					} else if (null != sgcontroller && (sgcontroller.isRIGHT() || sgcontroller.isStick1Right())) {
						updateJarPopups.getTab().right();
						buttonWaitCount = buttonWait;
					} else if (null != sgcontroller && sgcontroller.isCROSS()) {
						updateJarPopups.getTab().select();
						buttonWaitCount = buttonWait;
					} else if (null != sgcontroller && sgcontroller.isROUND()) {
						//popups.hide();
						updateJarPopups.hide();
					}
				}
			}
			else {
//				if (null != sgcontroller && sgcontroller.isSQUARE()) {
//	//			goToKofGamePage();
//					goToKofCharacterPage();
//				} else if (null != sgcontroller && sgcontroller.isTRIANGLE()) {
//					goToUserInfoPage();
//				} else if (null != sgcontroller && sgcontroller.isL1()) {
//					goToGameD3Page();
//				} else if (null != sgcontroller && sgcontroller.isR1()) {
//					goToRhythmPage();
//				} else
                    if (null != sgcontroller && (sgcontroller.isUP()||-0.1f>sgcontroller.axisLeftY())
						&& 0 >= buttonWaitCount) {
					tabUi.up();
	//				buttonWait = 0.5f;
					buttonWaitCount=buttonWait;
				} else if (null != sgcontroller && (sgcontroller.isDOWN()||0.1f<sgcontroller.axisLeftY())
						&& 0 >= buttonWaitCount) {
					tabUi.down();
	//				buttonWait = 0.5f;
					buttonWaitCount=buttonWait;
				} else if (null != sgcontroller && sgcontroller.isCROSS() && 0 >= buttonWaitCount//&&null!=tabUi.getCurrentActor()
				) {
	//				((ClickListener)((TextButton)tabUi.getCurrentActor()).getListeners().get(((TextButton)tabUi.getCurrentActor()).getListeners().size-1)).clicked(null,0,0);
	//				((TextButton)tabUi.getCurrentActor()).getClickListener().clicked(new InputEvent(),0,0);
					tabUi.select();
	//				buttonWait = 0.5f;
					buttonWaitCount=buttonWait;
				}
			}

		}else if(leaving){
			if(leavingManager.update()){

//				game.setScreen(leavingScreen);
//				dispose();
				try {
					leavingAction.go(null);
				} catch (Throwable e) {
					SGDataHelper.getLog().printException(e,tag);
				}
				return;
			}else{
				leavingPB.setValue(leavingManager.getProgress()*100);
			}

//			leavingPB.setValue(leavingPercent);
//			leavingPercent+=0.2;
		}

//		if(null!=game.getJarDownloader()&& game.getJarDownloader().downloading){
//			if(null==jarPB){
//				jarPB=new ProgressBar(0,100,1,false,skin);
////				jarPB.setX(ScreenSetting.WORLD_WIDTH/2);
////				jarPB.setY(ScreenSetting.WORLD_HEIGHT/2);
//				jarPB.setX(ScreenSetting.WORLD_WIDTH*3f/4f);
//				jarPB.setY(ScreenSetting.WORLD_HEIGHT/4f);
//				stage.addActor(jarPB);
//			}
//			if(100<=game.getJarDownloader().progress){
//				if(game instanceof AndroidKnightSasha){
//					((AndroidKnightSasha)game).updateApk(Constants.EXTERNAL_APK_FILE);
////					jarDownloader.downloading=false;没有这句的话，安装窗口会不断弹出。其实更好，都下载完了，肯定得装啊
//				}else if(Application.ApplicationType.Desktop== Gdx.app.getType()){// 启动更新脚本并退出当前应用
//					try {
//						new ProcessBuilder("cmd", "/c", "start", "updater.bat").start();
//						Gdx.app.exit();
//					} catch (IOException e) {
//						throw new RuntimeException(e);
//					}
//				}
//			}else{
//				jarPB.setValue(game.getJarDownloader().progress);
//			}
//		}

		//tabUi.select有可能触发跳转，然后stage就null了
		if(null==stage){return;}
		ScreenUtils.clear(0, 0, 0.2f, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		stage.act(Gdx.graphics.getDeltaTime());
		stage.act(delta);
		stage.draw();

		//显示控制器的状态
		float fontX=0;
		float fontY=ScreenSetting.WORLD_HEIGHT-10f;
		float fontRow=26;
//		Batch fontBatch=this.game.getBatch();
		Batch fontBatch=this.stage.getBatch();
		fontBatch.begin();
		game.getFont().draw(fontBatch, sysStr,fontX,fontY);
		fontY-=fontRow;
		game.getFont().draw(fontBatch, "got "+controllerCnt+" controllers:",fontX,fontY);
		fontY-=fontRow;
		for(int i=0;i<controllerCnt&&i<3;i++){
			game.getFont().draw(fontBatch, (i+1)+". "+ controllerNames[i],fontX,fontY);
			fontY-=fontRow;
		}
		if(null!=sgcontroller){
			game.getFont().draw(fontBatch, "current controller is "+sgcontroller.getPadUniqueName(),fontX,fontY);
			fontY-=fontRow;
			String curKey=null;
			if(sgcontroller.isUP()){
				curKey="isUP";
			}else if(sgcontroller.isDOWN()){
				curKey="isDOWN";
			}else if(sgcontroller.isLEFT()){
				curKey="isLEFT";
			}else if(sgcontroller.isRIGHT()){
				curKey="isRIGHT";
			}else if(sgcontroller.isCROSS()){
				curKey="isCROSS";
			}else if(sgcontroller.isROUND()){
				curKey="isROUND";
			}else if(sgcontroller.isSQUARE()){
				curKey="isSQUARE";
			}else if(sgcontroller.isTRIANGLE()){
				curKey="isTRIANGLE";
			}else if(0.1<sgcontroller.axisLeftX()||-0.1>sgcontroller.axisLeftX()){
				curKey="axisLeftX";
			}else if(0.1<sgcontroller.axisLeftY()||-0.1>sgcontroller.axisLeftY()){
				curKey="axisLeftY";
			}
			game.getFont().draw(fontBatch,"current key "+(null==curKey?"is null":curKey),fontX,fontY);
			fontY-=fontRow;

//			game.getFont().draw(fontBatch, "controller("+sgcontroller.getController().getUniqueId()+") is "+(sgcontroller.getController().isConnected()?"":"not ")+"connected",fontX,fontY);
//			fontY-=fontRow;
//			if(null!=sgcontroller.getMsg()){
//				game.getFont().draw(fontBatch, "controller msg: "+sgcontroller.getMsg(),fontX,fontY);
//				fontY-=fontRow;
//			}

			if(null!=controllerListener&&null!=controllerListener.getMsg()){
				game.getFont().draw(fontBatch, "controllerListener("+controllerListener.getUUID()+") msg: "+controllerListener.getMsg(),fontX,fontY);
				fontY-=fontRow;
			}
		}
//		if(GameSetting.isGameX()){
//			game.getFont().draw(fontBatch, "isGameX",fontX,fontY);
//			fontY-=fontRow;
//		}
//		if(isSteam){
//			game.getFont().draw(fontBatch, "steam is"+(((SteamKnightSasha)game).isSteamRunning?"":" not")+" running",fontX,fontY);
//			fontY-=fontRow;
//		}

		game.getFont().draw(fontBatch,"version:"+gameVersion,ScreenSetting.WORLD_WIDTH-300,40);
		fontBatch.end();

//		//steam
//		if(isSteam){
//			((SteamKnightSasha)game).runCallbacks();
//		}
	}



//	private void goToUserInfoPage() {
//		game.setScreen(new UserInfoScreen(game, sasha));
//		dispose();
//
//	}
//
//	private void goToGuidePage() {
//		Guide1Screen g1 = new Guide1Screen(game, sasha);
//		// g1.setLastScreen(this);
//		game.setScreen(g1);
//
//		dispose();
//
//	}
//
//	private void goToGameD3Page() {
//
//
//
//
//		game.setScreen(new com.mygdx.game.sasha.bulletD3.SelectCharacterScreenD3(game, //manager,
//				sasha));
//		dispose();
//
//	}
//
//
//	private  void goToKofCharacterPage() {
//
//		game.setScreen(new com.mygdx.game.sasha.screen.kof.SelectCharacterScreen(game, //manager,
//				sasha));
//		dispose();
//
//	}
//
//	private  void goToRhythmPage() {
//		game.setScreen(new RhythmMenuScreen(game));
//		dispose();
//
//	}
//	private  void goToKeySettingPage() {
//
//		game.setScreen(new com.mygdx.game.sasha.screen.KeySettingScreen(game, //manager,
//				sasha));
//		dispose();
//
//	}
//	private  void goToLeadPage() {
//
//		game.setScreen(new com.mygdx.game.sasha.screen.LeadScreen(game, //manager,
//				sasha));
//		dispose();
//
//	}
//	private  void goToPianoPage() {
//
//		game.setScreen(new GamepadPianoScreen(game));
//		dispose();
//
//	}
	boolean leaving=false;
	ProgressBar leavingPB=null;
	float leavingPercent=0;

	private AssetManager leavingManager;

	private Screen leavingScreen;
	private SGAction1<Object> leavingAction;
//	private GameScreen screen = null;
//	private  void goToCsPage() {
//
//		game.setScreen(new com.mygdx.game.sasha.ecs.cs.CsMenuScreen(game));
//		dispose();
//	}


    private void goToCastImgScreen() {
        game.setScreen(new SignalUrlScreen(game));
        dispose();

    }

    private void goToCastFileScreen() {
        game.setScreen(new SignalFileScreen(game));
        dispose();

    }
    private void goToCastDeviceScreen() {
        game.setScreen(new SignalScreen(game));
        dispose();

    }
	private void exitGame() {

//		screen.dispose();
////		PauseScreen.this.dispose();
		dispose();
		//game.dispose();
		Gdx.app.exit();
	}


//	private  void goToGamepadTestPage() {
//
////		game.setScreen(new com.mygdx.game.sasha.screen.KeySettingScreen(game, //manager,
////				sasha));
//
//		game.setScreen(new GamepadStickMainScreen(game));
//
//		dispose();
//
//	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().setScreenSize(width,height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {

		if(null!=autoConnectGamepad){
			autoConnectGamepad.dispose();
			autoConnectGamepad=null;
		}

		if(null!=stage) {
			stage.dispose();
			stage=null;

		}
		if(null!=skin) {
			skin.dispose();
			skin=null;
		}

		if(null!=skinLibgdx){ skinLibgdx.dispose();skinLibgdx=null;}
		//System.out.println(this.getClass().getSimpleName()+" dispose");
	}

	protected boolean checkSonyTvController(Controller controller){
		return "SONY TV VRC 001".equals(controller.getName());
	}
	// ...Rest of class omitted for succinctness.

	private class SGXInputControllerListener extends ControllerAdapter {

		@Override
		public boolean buttonDown(Controller controller, int buttonIndex) {
//		         if(XBoxKey.CROSS.ordinal()==buttonIndex) {
//			         Gdx.app.log(TAG, "jump: " +buttonIndex);
//		         }

			if (buttonIndex == controller.getMapping().buttonA) {
//			         Gdx.app.log(TAG, "jump: " +buttonIndex);
				// gotoGamePage();
			}

			return false;
		}

		@Override
		public boolean buttonUp(Controller controller, int buttonIndex) {
			return false;
		}

		@Override
		public void connected(Controller controller) {
			//int aa=1;
			msg="connected";
			uuid=controller.getUniqueId();

			if((null==sgcontroller||isSonyTvController)
			&&!checkSonyTvController(controller)
			) {
				isSonyTvController=false;
				sgcontroller = new SGPS5Gamepad(controller);
				if(null==autoConnectGamepad){
					autoConnectGamepad= new AutoConnectGamepad();
				}
				autoConnectGamepad.addGamepad(sgcontroller);
			}
		}

		@Override
		public void disconnected(Controller controller) {
			int aa=1;
			msg="disconnected";
		}
		protected String msg;
		public String getMsg() {
			return msg;
		}
		protected String uuid="";
		public String getUUID() {
			return uuid;
		}
	}

//	private void readUserData() {
//		Preferences preferences = Gdx.app.getPreferences("pre1.test");
//		preferences.putString("sasha", "Kitty");
//		preferences.putBoolean("visible", true);
//		preferences.putInteger("age", 25);
//		preferences.flush();
//
//		String strName1 = preferences.getString("name");
//		boolean isVisible = preferences.getBoolean("visible");
//		int age1 = preferences.getInteger("age");
//	}
	@Deprecated
	public static final String key = "a2FoaW5lYXNpbjEyMzQ1Ng==";
	public static final String key2="MHgxMjM0NTY=";

//	public static String getEncodeStrBySasha(SashaData sasha) {
//
//		String str = sasha.getAgi() + "|" + sasha.getEmail() + "|" + sasha.getKill() + "|" + sasha.getBirth() + "|"
//				+ sasha.getStr() + "|" + sasha.getUserId() + "|" + sasha.getObedient() + "|" + sasha.getLastLogin()
//				+ "|" + sasha.getVip() + "|" + sasha.getVer() + "|" + sasha.getCountry().ordinal() + "|"
//				+ sasha.getCharacter().ordinal() + "|" + sasha.getDef() + "|" + sasha.getHit() + "|"
//				+ sasha.getVipDate() + "|" + sasha.getLastBackup();
//		try {
//			return AES.AESEncryptDemo(str, SGDataHelper.decodeBase64(key));
//		} catch (Exception e) {
//			hasReadError=true;
//			SGDataHelper.getLog().print(tag+".getEncodeStrBySasha "+ e);
//			SGDataHelper.getLog().print(SGDataHelper.getLineInfo(e));
//		}
//		return null;
//	}
	/**
	 * 如果有任何报错, 不应该保存sasha数据, 否则容易由于程序新版本的异常, 复盖了有用的数据. 设置为true
	 *
	 * 测试时为了方便,可以设置为false, 这样便于保存新的数据结构
	 */
	private static final boolean notSaveDataIfError=true;
	private static boolean hasReadError=false;
	private static boolean supportCloudSave;
//	/**
//	 * 支持云存档，如steam
//	 * 给平台复写
//	 */
//	private static ISGCloudSave cloudSave;
//	//	@Deprecated
//	public static void saveSashaData(SashaData sasha) {
//		if(notSaveDataIfError&&hasReadError){
//			return;
//		}
//		String enStr=getEncodeStrBySasha(sasha);
//		if(supportCloudSave){
//			cloudSave.writeSaveToCloud("character_"+sasha.getCharacter().name(),enStr);
//			//云存档和本地存档不要混合用，否则就需要比对哪个更新才行
//			return;
//		}
//		Preferences preferences = Gdx.app.getPreferences("pre1.test");
////		String str = sasha.getAgi() + "|" + sasha.getEmail() + "|" + sasha.getKill() + "|" + sasha.getBirth() + "|"
////				+ sasha.getStr() + "|" + sasha.getUserId() + "|" + sasha.getObedient() + "|" + sasha.getLastLogin()
////				+ "|" + sasha.getVip() + "|" + sasha.getVer() + "|" + sasha.getCountry().ordinal()+ "|" + sasha.getCharacter().ordinal()
////				+ "|" + sasha.getDef()+ "|" + sasha.getHit()+ "|" + sasha.getVipDate()
////				;
////		try {
////			preferences.putString(sasha.getCharacter().name(), AES.AESEncryptDemo(str, SGDataHelper.decodeBase64(key)));
////		} catch (Exception e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		try {
//			preferences.putString(sasha.getCharacter().name(), getEncodeStrBySasha(sasha));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		preferences.flush();
//	}
//
//	private static SashaData initSashaByEncodeStr(String s) throws Exception {
//
//		String[] s2 = AES.AESDecryptDemo(s, SGDataHelper.decodeBase64(key)).split("[|]");
//		SashaData sasha = new SashaData();
//		sasha.setAgi(Double.valueOf(s2[0]));
//		sasha.setBirth(s2[3]);
//		sasha.setKill(Double.valueOf(s2[2]));
//		sasha.setObedient(Double.valueOf(s2[6]));
//		sasha.setStr(Double.valueOf(s2[4]));
//		sasha.setEmail(s2[1]);
//		sasha.setUserId(s2[5]);
//		sasha.setLastLogin(Double.valueOf(s2[7]));
//		sasha.setVip(Boolean.valueOf(s2[8]));
//		sasha.setVer(s2[9]);
////		sasha.setCountry(s2[10]);
//		sasha.setCountry(Country.values()[Integer.valueOf(s2[10])]);
//		sasha.setCharacter(com.mygdx.game.sasha.SGCharacter.values()[Integer.valueOf(s2[11])]);
//		sasha.setDef(Double.valueOf(s2[12]));
//		sasha.setHit(Double.valueOf(s2[13]));
//		sasha.setVipDate(Double.valueOf(s2[14]));
//		sasha.setLastBackup(Double.valueOf(s2[15]));
//		return sasha;
//	}
//
//	public static SashaData readSashaData() {
//		SGCharacter c = SGCharacter.SASHA;
//		if(supportCloudSave){
//			String s3 = cloudSave.readSaveFromCloud("character");
////			SGCharacter c = SGCharacter.SASHA;
//			if (null != s3 && !"".equals(s3)) {
//                try {
//                    c = SGCharacter.valueOf(AES.AESDecryptDemo(s3, SGDataHelper.decodeBase64(key)));
//                } catch (Exception e) {
////                    throw new RuntimeException(e);
//                }
//            }
////			String s = cloudSave.readSaveFromCloud("character_"+c.name());
////			if (null == s || "".equals(s)) {
//////				//没有云存档，就读本地，再想想不对
//////				//云存档和本地存档不要混合用，否则就需要比对哪个更新才行
//////				SashaData sasha = SashaData.init();
//////				return sasha;
////			} else {
////                try {
////                    return initSashaByEncodeStr(s);
////                } catch (Exception e) {
////////                    throw new RuntimeException(e);
//////					return SashaData.init();
////                }
////            }
//		}else {
//			Preferences preferences = Gdx.app.getPreferences("pre1.test");
//			try {
//				String s3 = preferences.getString("character");
////				SGCharacter c = SGCharacter.SASHA;
//				if (null != s3 && !"".equals(s3)) {
//					c = SGCharacter.valueOf(AES.AESDecryptDemo(s3, SGDataHelper.decodeBase64(key)));
//				}
////				String s = preferences.getString(c.name());
////				if (null == s || "".equals(s)) {
////					SashaData sasha = SashaData.init();
////					return sasha;
////				} else {
////
////					return initSashaByEncodeStr(s);
////				}
//			} catch (Exception e) {
//				hasReadError = true;
//				SGDataHelper.getLog().print(tag + ".readSashaData " + e);
//				SGDataHelper.getLog().print(SGDataHelper.getLineInfo(e));
//			}
//		}
//		return changeCharacterData(c,false);
////		SashaData sasha = SashaData.init();
////		return sasha;
//
//	}
//
//	public static SashaData changeCharacterData(SGCharacter c,boolean changeDefault) {
//		SashaData sasha = null;
//		SGCharacter cOld = null;
//		if(supportCloudSave){
////			String s3 = preferences.getString("character");
//			String s3 = cloudSave.readSaveFromCloud("character");
////			//SGCharacter c=SGCharacter.SASHA;
//			if (changeDefault&&null != s3 && !"".equals(s3)) {
//                try {
//                    cOld = SGCharacter.valueOf(AES.AESDecryptDemo(s3, SGDataHelper.decodeBase64(key)));
//                } catch (Exception e) {
////                    throw new RuntimeException(e);
//                }
//                // if(cOld==c) {return null;}
//			}
//			String s = cloudSave.readSaveFromCloud("character_"+c.name());
//			if (null == s || "".equals(s)) {
//				sasha = SashaData.initCharacter(c);
//				// return sasha;
//			} else {
//                try {
//                    sasha = initSashaByEncodeStr(s);
//                } catch (Exception e) {
////                    throw new RuntimeException(e);
//					sasha = SashaData.initCharacter(c);
//                }
//            }
//
//			if (changeDefault&&( null == cOld || cOld != c)) {
//				try {
//					cloudSave.writeSaveToCloud("character",AES.AESEncryptDemo(c.name(), SGDataHelper.decodeBase64(key)));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
////			if (c != sasha.getCharacter()) {
////				sasha.setCharacter(c);
////				saveSashaData(sasha);
////			}
//			return sasha;
//		}else{
//			//读本地
//			Preferences preferences = Gdx.app.getPreferences("pre1.test");
//			if(null==sasha) {
//				try {
//					if(null==cOld) {
//						String s3 = preferences.getString("character");
////			//SGCharacter c=SGCharacter.SASHA;
//						if (null != s3 && !"".equals(s3)) {
//							cOld = SGCharacter.valueOf(AES.AESDecryptDemo(s3, SGDataHelper.decodeBase64(key)));
//							// if(cOld==c) {return null;}
//						}
//					}
//					String s = preferences.getString(c.name());
//					if (null == s || "".equals(s)) {
//						sasha = SashaData.initCharacter(c);
//						// return sasha;
//					} else {
//						sasha = initSashaByEncodeStr(s);
//					}
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//
//			if (null == sasha) {
//				sasha = SashaData.initCharacter(c);
//			}
//			if (null == cOld || cOld != c) {
//				try {
//					preferences.putString("character", AES.AESEncryptDemo(c.name(), SGDataHelper.decodeBase64(key)));
//					preferences.flush();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
////			if (c != sasha.getCharacter()) {
////				sasha.setCharacter(c);
////				saveSashaData(sasha);
////			}
//			return sasha;
//
//		}
//	}
//
//	public static SashaData previewCharacter(SGCharacter c) {
//		return changeCharacterData(c,false);
////		Preferences preferences = Gdx.app.getPreferences("pre1.test");
////		SashaData sasha = null;
////		SGCharacter cOld = null;
////		try {
//////			String s3 = preferences.getString("character");
////////			//SGCharacter c=SGCharacter.SASHA;
//////			if(null!=s3&&!"".equals(s3)) {
//////				cOld=SGCharacter.valueOf(AES.AESDecryptDemo(s3, SGDataHelper.decodeBase64(key)));
//////				//if(cOld==c) {return null;}
//////			}
////			String s = preferences.getString(c.name());
////			if (null == s || "".equals(s)) {
////				sasha = SashaData.initCharacter(c);
////				// return sasha;
////			} else {
////				sasha = initSashaByEncodeStr(s);
////			}
////		} catch (Exception e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////
////		if (null == sasha) {
////			sasha = SashaData.initCharacter(c);
////		}
////		if (null == cOld || cOld != c) {
////			try {
////				preferences.putString("character", AES.AESEncryptDemo(c.name(), SGDataHelper.decodeBase64(key)));
////			} catch (Exception e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////		}
////		return sasha;
//	}

//	private void getSashaVip() {
//		String requestContent = null;
//
//		HttpRequest httpRequest = new HttpRequest(Net.HttpMethods.GET);
//		httpRequest.setUrl(url.getVip());
//		httpRequest.setHeader("Content-Type", "text/plain");
//		httpRequest.setContent(requestContent);
//
//		Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
//
//			public void handleHttpResponse(HttpResponse httpResponse) {
//
//				//final int statusCode = httpResponse.getStatus().getStatusCode();
//
////		            System.out.println("HTTP Request status:" + statusCode);
////		            System.out.println("Content:");
//				try {
//					// String s = AES.AESDecryptDemo(httpResponse.getResultAsString(),
//					// SGDataHelper.decodeBase64(key));
//					String s = AES.AESEncryptDemo(sasha.getUserId(), SGDataHelper.decodeBase64(key));
//					// if (Arrays.asList(s.split("|")).contains(sasha.getUserId())) {
//					if (httpResponse.getResultAsString().indexOf("|" + s + "|") > -1) {
//
//						if (!sasha.getVip()) {
//							sasha.setVip(true);
//							saveSashaData(sasha);
//							System.out.println("远程是vip,本地不是vip");
//							SGEmailSend.SendMail(new String[] { SGEmailSend.EMAIL_OWNER_ADDR },
//									"knightSasha" + sasha.getUserId() + "数据通知", "远程是vip,本地不是vip");
//						}
//					} else if (sasha.getVip()) {
//						System.out.println("远程不是vip,本地是vip");
//						SGEmailSend.SendMail(new String[] { SGEmailSend.EMAIL_OWNER_ADDR },
//								"knightSasha" + sasha.getUserId() + "数据异常通知", "远程不是vip,本地是vip");
//						SGDate d = new SGDate(2020, 1, 1, 0, 0, 0).AddDays(((Double) sasha.getVipDate()).intValue());
//						if (d.AddDays(7).compareTo(SGDate.Now()) < 0) {// 7天之内没有支付开通的检测
//							sasha.setVip(false);
//							MainMenuScreen.saveSashaData(sasha);
//						}
//					}
//					System.out.println();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
//
//			public void failed(Throwable t) {
//				//System.out.println("HTTP request failed!");
//			}
//
//			@Override
//			public void cancelled() {
//				//System.out.println("HTTP request failed!");
//			}
//
//		});
//	}
//
//	public static void getSashaVipAsync(final SashaData sashaRef
//										//, final SGRef<Boolean> needUpdateVip
//										, final SGAction1<Boolean> callBack
//										) {
//		String requestContent = null;
//
//		HttpRequest httpRequest = new HttpRequest(Net.HttpMethods.GET);
//		if(null==url){
//			url = new CnUrl();}
//		httpRequest.setUrl(url.getVip());
//		httpRequest.setHeader("Content-Type", "text/plain");
//		httpRequest.setContent(requestContent);
//
//		Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
//
//			public void handleHttpResponse(HttpResponse httpResponse) {
//
//				//final int statusCode = httpResponse.getStatus().getStatusCode();
//
////		            System.out.println("HTTP Request status:" + statusCode);
////		            System.out.println("Content:");
//				try {
//					// String s = AES.AESDecryptDemo(httpResponse.getResultAsString(),
//					// SGDataHelper.decodeBase64(key));
//					String s = AES.AESEncryptDemo(sashaRef.getUserId(), SGDataHelper.decodeBase64(key));
//					// if (Arrays.asList(s.split("|")).contains(sasha.getUserId())) {
//					if (httpResponse.getResultAsString().indexOf("|" + s + "|") > -1) {
//
//						if (!sashaRef.getVip()) {
//							sashaRef.setVip(true);
//							saveSashaData(sashaRef);
//							//needUpdateVip.SetValue(true);
//							callBack.go(true);
//							//System.out.println("远程是vip,本地不是vip");
//							SGEmailSend.SendMail(new String[] { SGEmailSend.EMAIL_OWNER_ADDR },
//									"knightSasha_" + sashaRef.getUserId() + "数据通知", "远程是vip,本地不是vip");
//						}
//					} else if (sashaRef.getVip()) {
//						//System.out.println("远程不是vip,本地是vip");
//						SGEmailSend.SendMail(new String[] { SGEmailSend.EMAIL_OWNER_ADDR },
//								"knightSasha_" + sashaRef.getUserId() + "数据异常通知", "远程不是vip,本地是vip");
//						SGDate d = new SGDate(2020, 1, 1, 0, 0, 0).AddDays(((Double) sashaRef.getVipDate()).intValue());
//						if (d.AddDays(7).compareTo(SGDate.Now()) < 0) {// 7天之内没有支付开通的检测
//							sashaRef.setVip(false);
//							MainMenuScreen.saveSashaData(sashaRef);
//						}
//					}
//					System.out.println();
//				} catch (Exception e) {
//					SGDataHelper.getLog().printException(e,tag);
//				}
//
//			}
//
//			public void failed(Throwable t) {
//				//System.out.println("HTTP request failed!");
//			}
//
//			@Override
//			public void cancelled() {
//				//System.out.println("HTTP request failed!");
//			}
//
//		});
//	}
//	public static SashaData getSashaBackupData(final SashaData sasha) {
//		// final SashaData[] r= new SashaData[] {};
//		final SGRef<SashaData> r = new SGRef<SashaData>();
//		String requestContent = null;
//
//		HttpRequest httpRequest = new HttpRequest(Net.HttpMethods.GET);
//		httpRequest.setUrl(url.getCharacterBakup().replace("{character}", sasha.getCharacter().name()));
//		httpRequest.setHeader("Content-Type", "text/plain");
//		httpRequest.setContent(requestContent);
//
//		long now = System.currentTimeMillis();
//		final SGRef<Boolean> end = new SGRef<Boolean>(false);
//		Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
//
//			public void handleHttpResponse(HttpResponse httpResponse) {
//
//				// System.out.println("1111111111111111111111");
//				//final int statusCode = httpResponse.getStatus().getStatusCode();
//
////		            System.out.println("HTTP Request status:" + statusCode);
////		            System.out.println("Content:");
//				try {
//					// String s = AES.AESDecryptDemo(httpResponse.getResultAsString(),
//					// SGDataHelper.decodeBase64(key));
//					String s = AES.AESEncryptDemo(sasha.getUserId(), SGDataHelper.decodeBase64(key));
//					// if (Arrays.asList(s.split("|")).contains(sasha.getUserId())) {
//					String s2 = httpResponse.getResultAsString();
//					int idx = s2.indexOf("|" + s);
//					if (idx > -1) {
//						int idx2 = s2.indexOf("|", idx + s.length());
//						if (idx2 > -1) {
//							// r[0]= initSashaByEncodeStr(s2.substring(idx+s.length(), idx2));
//							r.SetValue(initSashaByEncodeStr(s2.substring(idx + s.length() + 1, idx2)));
//						}
//					} else {
////						System.out.println("远程不是vip,本地是vip");
////						PFEmailSend.SendMail(new String[] { PFEmailSend.EMAIL_OWNER_ADDR },
////								"knightSasha" + sasha.getUserId() + "数据异常通知", "远程不是vip,本地是vip");
////						SGDate d=new SGDate(2020,1,1,0,0,0).AddDays(((Double)sasha.getVipDate()).intValue());
////						if(d.AddDays(7).compareTo(SGDate.Now())<0) {//7天之内没有支付开通的检测
////							sasha.setVip("0");
////							MainMenuScreen.saveSashaData(sasha);
////						}
//					}
//					// System.out.println();
//				} catch (Exception e) {
//					SGDataHelper.getLog().printException(e,tag);
//				}
//				end.SetValue(true);
//				// System.out.println("33333333333----"+end.GetValue());
//			}
//
//			public void failed(Throwable t) {
//				//System.out.println("HTTP request failed!");
//				end.SetValue(true);
//			}
//
//			@Override
//			public void cancelled() {
//				//System.out.println("HTTP request failed!");
//				end.SetValue(true);
//			}
//
//		});
//		// System.out.println("222222222222222");
//		while (!end.GetValue() && System.currentTimeMillis() - now < 10000) {
//			try {
//				// System.out.println("44444444444----"+end.GetValue());
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return r.GetValue();
//	}



//	public static String getHttpString(String url) {
//		// final SashaData[] r= new SashaData[] {};
//		final SGRef<String> r = new SGRef<String>();
//		String requestContent = null;
//
//		HttpRequest httpRequest = new HttpRequest(Net.HttpMethods.GET);
////		httpRequest.setUrl(url.getCharacterBakup().replace("{character}", sasha.getCharacter().name()));
//		httpRequest.setUrl(url);
//		httpRequest.setHeader("Content-Type", "text/plain");
//		httpRequest.setContent(requestContent);
//
//		long now = System.currentTimeMillis();
//		final SGRef<Boolean> end = new SGRef<Boolean>(false);
//		Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
//
//			public void handleHttpResponse(HttpResponse httpResponse) {
//
//				try {
//
//					if (HttpStatus.SC_OK != httpResponse.getStatus().getStatusCode()) {
//						end.SetValue(true);
//						return;
//					}
////					String s = AES.AESEncryptDemo(sasha.getUserId(), SGDataHelper.decodeBase64(key));
//					String s2 = httpResponse.getResultAsString();
////					r.SetValue(s2);
////					int idx = s2.indexOf("|" + s);
////					if (idx > -1) {
////						int idx2 = s2.indexOf("|", idx + s.length());
////						if (idx2 > -1) {
////							// r[0]= initSashaByEncodeStr(s2.substring(idx+s.length(), idx2));
////							r.SetValue(initSashaByEncodeStr(s2.substring(idx + s.length() + 1, idx2)));
////						}
////					} else {
////
////					}
//					// System.out.println();
//				} catch (Exception e) {
//					SGDataHelper.getLog().printException(e,tag);
//				}
//				end.SetValue(true);
//				// System.out.println("33333333333----"+end.GetValue());
//			}
//
//			public void failed(Throwable t) {
//				//System.out.println("HTTP request failed!");
//				end.SetValue(true);
//			}
//
//			@Override
//			public void cancelled() {
//				//System.out.println("HTTP request failed!");
//				end.SetValue(true);
//			}
//
//		});
//		// System.out.println("222222222222222");
//		while (!end.GetValue() && System.currentTimeMillis() - now < 10000) {
//			try {
//				// System.out.println("44444444444----"+end.GetValue());
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		return r.GetValue();
//	}

	public void testEmailSend() throws InterruptedException {

		String title = "测试发邮件20230902_1_" + SGDate.Now();

		String[] emails = new String[] { "li@sellgirl.com" };
		SGEmailSend.SendMail(emails, title, title);
		Thread.sleep(2000);
		// System.out.println("测试通过");
	}




	public class MyTextInputListener implements TextInputListener {
		@Override
		public void input(String text) {
			System.out.println(text);
		}

		@Override
		public void canceled() {
		}
	}


	public static TextButtonStyle getButtonStyle(Skin skin) {

		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);//DARK_GRAY);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		return textButtonStyle;
	}

	public static LabelStyle getLabelStyle(Skin skin) {

		LabelStyle textButtonStyle = new LabelStyle();
		textButtonStyle.font = skin.getFont("default");
		// textButtonStyle.fontColor = Color.BLACK;
		return textButtonStyle;
	}

	public static TextFieldStyle getTextFieldStyle(Skin skin) {

		TextFieldStyle textButtonStyle = new TextFieldStyle();
		textButtonStyle.font = skin.getFont("default");
		textButtonStyle.fontColor = Color.BLACK;
		textButtonStyle.background = skin.newDrawable("white", Color.YELLOW);
		return textButtonStyle;
	}

	public static WindowStyle getWindowStyle(Skin skin) {

		WindowStyle textButtonStyle = new WindowStyle();
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

	public static TextButtonStyle getTextButtonStyle(Skin skin) {

		TextButtonStyle textButtonStyle = new TextButtonStyle();
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

	public static ImageButton.ImageButtonStyle getImageButtonStyle(Skin skin) {

		ImageButton.ImageButtonStyle textButtonStyle = new ImageButton.ImageButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);//DARK_GRAY);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		// textButtonStyle.background = skin.newDrawable("white", Color.YELLOW);
		return textButtonStyle;
	}
	/**
	 * 风格为:
	 * 白色字
	 * 已启用: 白色
	 * 未启用: 深色
	 * @return
	 */
	public static Skin getSkin() {
		// A skin can be loaded via JSON or defined programmatically, either is fine.
		// Using a skin is optional but strongly
		// recommended solely for the convenience of getting a texture, region, etc as a
		// drawable, tinted drawable, etc.
		Skin skin = new Skin();

		// Generate a 1x1 white texture and store it in the skin named "white".
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));

//		Pixmap pixmap2 = new Pixmap(5, 5, Format.RGBA8888);
//		pixmap2.setColor(Color.WHITE);
//		pixmap2.fill();
//		skin.add("white5", new Texture(pixmap2));

		// Store the default libGDX font under the name "default".
		// skin.add("default", new BitmapFont());
//		/**
//		 * BitmapFont的初始化。
//		 * 3个参数分别为:fontFile(字体文件)、imageFile(所对应的png文件)、是否翻转
//		 */
		// skin.add("default", new BitmapFont(Gdx.files.internal("sasha_font.fnt"),
		// Gdx.files.internal("sasha_font.png"), false));

		skin.add("default", getFont2());
		return skin;
	}

	/**
	 * 一套的样式，原本没有中文字
	 *
	 * 注意：
	 * 1.如果使用此样式以后，组件没有显示文字(中文字),
	 *   那解决方法就是在代码中把中文字体设置到style.font(可能为子级style)
	 * 测试时建议用此方法
	 * @param font
	 * @return
	 */
	public static Skin getSkin2(BitmapFont font) {
		Skin skin = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));
		//为了中文字体
		font.setColor(Color.BLACK);
		//font.setColor(0,0,0,1);
		skin.get(TextButtonStyle.class).font=font;
		skin.get(LabelStyle.class).font=font;
		skin.get(SelectBox.SelectBoxStyle.class).font=font;
		skin.add("default", font);
//		skin.get(SelectBox.SelectBoxStyle.class).font=font;//
//		skin.get(List.ListStyle.class).font=font;//
		skin.get(SelectBox.SelectBoxStyle.class).listStyle.font=font;
//		skin.add("default", MainMenuScreen.getSelectBoxStyle(skin));//没有这句的话，下拉菜单的字显示不出来--benjamin20250911
		skin.get(CheckBox.CheckBoxStyle.class).font=font;//没这句显示不了中文字
//		skin.add("default", MainMenuScreen.getCheckBoxStyle(skin));//没有这句的话，checkbox的文字显示不出来
//		skin.get(ScrollPane.ScrollPaneStyle.class).font=font;
//		skin.add("default", MainMenuScreen.getScrollStyle(skin));
		skin.add("default", MainMenuScreen.getImageButtonStyle(skin));

		return skin;
	}

	private static FreeTypeFontGenerator generator;// TTF字体发生器
	private static FreeTypeBitmapFontData fontData;// 负责处理FreeTypeFontGenerator的数据.可以简单地理解成为一个加工好的字符库
	public static HashMap<Integer, FreeTypeBitmapFontData> fontDatas=new HashMap<>();
	//private static BitmapFont font;// 要现实的内容
//	//private static SpriteBatch batch;
//
//	public static BitmapFont getFont2() {
//		/**
//		 * 以下是进行初始化
//		 */
//		generator = new FreeTypeFontGenerator(Gdx.files.internal("simhei.ttf"));
//
//		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//		FileHandle file = Gdx.files.internal("font_cn.txt");
//		String text = file.readString();
//
//		parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS
//				+"△○□↑→↓←↖↗↘↙∞"
//				+ text;
//
//		fontData = generator.generateData(parameter);
//
//		font = new BitmapFont(fontData, fontData.regions, false);
//		font.setColor(Color.WHITE);
//		generator.dispose();
//		return font;
//	}

	/**
	 * harmony有些情况下（比如屏幕右侧向内推时）,会进入Game.dispose方法，
	 * 之后旧的generator会导致文字变黑块（可能被系统自动释放了）。
	 * 所以game.create时要重新new fontData
	 */
	public static void disposeFont(){
		if(null!=fontData){
			fontData.dispose();
			fontData=null;
		}
		if(null!=fontDatas&&!fontDatas.isEmpty()){
			for (FreeTypeBitmapFontData m1 : fontDatas.values()) {
				m1.dispose();
			}
			fontDatas.clear();
//			fontDatas=null;//静态类不设置null
		}
	}

	/**
	 * @return
	 */
	public static BitmapFont getFont2() {
//		if(null!=font) {return font;}
		if(null==generator) {

			/**
			 * 以下是进行初始化
			 */
			generator = new FreeTypeFontGenerator(Gdx.files.internal("simhei.ttf"));//generator这个东西就算dispose也没用的,内存增加9MB,没办法了
		}

		if(null==fontData) {//测试发现fontData就算dispose了,还是消耗内存,原因不明
			FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//			FileHandle file = Gdx.files.internal("font_cn.txt");
//			String text = file.readString(SGDataHelper.encoding);
//
//			parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS
//					+"△○□✕↑→↓←↖↗↘↙∞"
//					+ text;
			parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS
					+getCnChar();

			parameter.size=24;
			fontData = generator.generateData(parameter);
		}

		BitmapFont font = new BitmapFont(fontData, fontData.regions, false);
		font.setColor(Color.WHITE);
//		generator.dispose();
//		fontData.dispose();
		return font;
	}

	public static boolean hasNewCn=true;
	public static BitmapFont getFont3(int size) {
//		if(null!=font) {return font;}
		if(null==generator) {

			/**
			 * 以下是进行初始化
			 */
			generator = new FreeTypeFontGenerator(Gdx.files.internal("simhei.ttf"));//generator这个东西就算dispose也没用的,内存增加9MB,没办法了
		}

		if(hasNewCn&&
				null!=fontData){
			fontData.dispose();
			fontData=null;
		}
		if(!fontDatas.containsKey(size)){//null==fontData) {//测试发现fontData就算dispose了,还是消耗内存,原因不明
			FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
			FileHandle file = Gdx.files.internal("font_cn.txt");
//			String text = file.readString(SGDataHelper.encoding);
			String text = file.readString("utf8");

			parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS
					+"△○□↑→↓←↖↗↘↙∞"
					+ text//+MainMenuScreen.readExCn()
			;

			parameter.size=size;//ScreenSetting.FONT_SIZE;//24
			FreeTypeBitmapFontData fontData2 = generator.generateData(parameter);

			fontDatas.put(size,fontData2);
//			if(hasNewCn){hasNewCn=false;}
		}

		BitmapFont font = new BitmapFont(fontDatas.get(size), fontDatas.get(size).regions, false);
		font.setColor(Color.WHITE);

		return font;
	}
	public static String getCnChar(){
		FileHandle file = Gdx.files.internal("font_cn.txt");
		String text = file.readString(SGDataHelper.encoding);
		return "△○□↑→↓←↖↗↘↙∞" //几种交叉符号好像都不在simhei字体中
				+
				text;
	}
//	public static BitmapFont getFont3(AssetManager manager) {
//		/**
//		 * 以下是进行初始化
//		 */
//		//FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("simhei.ttf"));
//		FreeTypeFontGenerator generator=manager.get("simhei.ttf",FreeTypeFontGenerator.class);//AssetManager这样加载可以,但dispose时会报fatal错误
//
//		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//		FileHandle file = Gdx.files.internal("font_cn.txt");
//		String text = file.readString();
//
//		parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS
//				+"△○□↑→↓←↖↗↘↙∞"
//				+ text;
//
//		FreeTypeBitmapFontData fontData = generator.generateData(parameter);
//
//		BitmapFont font = new BitmapFont(fontData, fontData.regions, false);
//		font.setColor(Color.WHITE);
//		generator.dispose();
//		return font;
//	}
//
//	public static BitmapFont getFont4(AssetManager manager) {
////		/**
////		 * 以下是进行初始化
////		 */
////		//FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("simhei.ttf"));
////		//FreeTypeFontGenerator generator=manager.get("simhei.ttf",FreeTypeFontGenerator.class);
////
////		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
////		FileHandle file = Gdx.files.internal("font_cn.txt");
////		String text = file.readString();
////
////		parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS
////				+"△○□↑→↓←↖↗↘↙∞"
////				+ text;
////
////		FreeTypeBitmapFontData fontData = generator.generateData(parameter);
////
////		BitmapFont font = new BitmapFont(fontData, fontData.regions, false);
////		font.setColor(Color.WHITE);
////		generator.dispose();
//		BitmapFont font=manager.get("simhei.ttf",BitmapFont.class);
//		return font;
//	}

	/*------------------游戏设置相关------------------*/
	private  Skin skinLibgdx;
	private Window winOptions;
	private TextButton btnWinOptSave;
	private TextButton btnWinOptCancel;
	private CheckBox chkSound;
	private Slider sldSound;
	private CheckBox chkMusic;
	private CheckBox chkOrthographic;
	private CheckBox chkKofOrthographic;
//	private SelectBox<String> languageCombo;
//	private SelectBox<Country> languageCombo;

	private Slider sldMusic;
//	private SelectBox<CharacterSkin> selCharSkin;
//	private Image imgCharSkin;
	private CheckBox chkShowFpsCounter;
	private CheckBox chkShowFollowPath;
	private CheckBox chkShowTestMenu;
	private CheckBox chkShowFrameBar;


	private final boolean debugEnabled = false;
	private Skin getSkinLibgdx(){
		return null==skinLibgdx?skin:skinLibgdx;
	}
	private String getFontName(){
		return null==skinLibgdx?"default":"default-font";
	}
	private Table buildOptWinAudioSettings() {
		Table tbl = new Table();
		// + Title: "Audio"
		tbl.pad(10, 10, 0, 10);
		//tbl.add(new Label("Audio", skinLibgdx, "default", Color.ORANGE)).colspan(3);
		tbl.add(new Label("Audio", getSkinLibgdx(), getFontName(), Color.ORANGE)).colspan(3);
		tbl.row();
		tbl.columnDefaults(0).padRight(10);
		tbl.columnDefaults(1).padRight(10);
		// + Checkbox, "Sound" label, sound volume slider
		chkSound = new CheckBox("", getSkinLibgdx());
		tbl.add(chkSound);

//		optionsWindowTabUi.addItem(chkSound);
		tabFirstNode=new SGTabUDLRMap.SGTabUDLRNode();
		tabFirstNode.actor=chkSound;
		tmpTabNode=tabFirstNode;

		tbl.add(new Label("Sound", getSkinLibgdx()));
		sldSound = new Slider(0.0f, 1.0f, 0.1f, false, getSkinLibgdx());
		tbl.add(sldSound);

		//optionsWindowTabUi.addItem(sldSound);
//		SGTabUDLRMap.SGTabUDLRNode newNode=new SGTabUDLRMap.SGTabUDLRNode();
//		newNode.actor=sldSound;
//		tmpTabNode.down=newNode;
//		newNode.up=tmpTabNode;
//		tmpTabNode=newNode;
		testAddTabItem(sldSound);
		tabFirstNode.right=tmpTabNode;
		tmpTabNode.left=tabFirstNode;
		 tmpRightNode=tmpTabNode;

		tbl.row();

		// + Checkbox, "Music" label, music volume slider
		chkMusic = new CheckBox("", getSkinLibgdx());
		tbl.add(chkMusic);
		//optionsWindowTabUi.addItem(chkMusic);
		testAddTabItem(chkMusic);
		tabFirstNode.down=tmpTabNode;
		tmpTabNode.up=tabFirstNode;
		 tmpLeftNode=tmpTabNode;

		tbl.add(new Label("Music", getSkinLibgdx()));
		sldMusic = new Slider(0.0f, 1.0f, 0.1f, false, getSkinLibgdx());
		tbl.add(sldMusic);
		//optionsWindowTabUi.addItem(sldMusic);
		testAddTabItem(sldMusic);
		tmpLeftNode.right=tmpTabNode;
		tmpTabNode.left=tmpLeftNode;
		tmpRightNode.down=tmpTabNode;
		tmpTabNode.up=tmpRightNode;
		tmpRightNode=tmpTabNode;

		tbl.row();
		return tbl;
	}
	private void testAddTabItem(Actor actor){
		SGTabUDLRMap.SGTabUDLRNode newNode=new SGTabUDLRMap.SGTabUDLRNode();
		newNode.actor=actor;
		tmpTabNode.down=newNode;
		newNode.up=tmpTabNode;
		tmpTabNode=newNode;
	}
	//我加的 --benjamin
	private Table buildOptWinVideoSettings() {
		Table tbl = new Table();
		// + Title: "Audio"
		tbl.pad(10, 10, 0, 10);
		//tbl.add(new Label("Audio", skinLibgdx, "default", Color.ORANGE)).colspan(3);
		tbl.add(new Label("Video", getSkinLibgdx(), getFontName(), Color.ORANGE)).colspan(3);
		tbl.row();
		tbl.columnDefaults(0).padRight(10);
		tbl.columnDefaults(1).padRight(10);
		// + Checkbox, "Sound" label, sound volume slider
//		chkSound = new CheckBox("", getSkinLibgdx());
		chkOrthographic = new CheckBox("", getSkinLibgdx());
		tbl.add(new Label("Orthographic", getSkinLibgdx()));
		tbl.add(chkOrthographic);
		//optionsWindowTabUi.addItem(chkOrthographic);
		testAddTabItem(chkOrthographic);
//		tmpRightNode.down=tmpTabNode;
//		tmpTabNode.up=tmpRightNode;
		tmpLeftNode.down=tmpTabNode;

//		sldSound = new Slider(0.0f, 1.0f, 0.1f, false, getSkinLibgdx());
//		tbl.add(sldSound);
		tbl.row();
		chkKofOrthographic = new CheckBox("", getSkinLibgdx());
		tbl.add(new Label("Kof Orthographic", getSkinLibgdx()));
		tbl.add(chkKofOrthographic);
		//optionsWindowTabUi.addItem(chkKofOrthographic);
		testAddTabItem(chkKofOrthographic);
//		sldSound = new Slider(0.0f, 1.0f, 0.1f, false, getSkinLibgdx());
//		tbl.add(sldSound);
		tbl.row();

//		languageCombo=new SelectBox(getSkinLibgdx());
//		languageCombo.setItems(Country.values());
//		tbl.add(new Label("language", getSkinLibgdx()));
//		tbl.add(languageCombo);
//		testAddTabItem(languageCombo);
//		tbl.row();
		return tbl;
	}



	private Table buildOptWinDebug() {
		Table tbl = new Table();
		// + Title: "Debug"
		tbl.pad(10, 10, 0, 10);
//		tbl.add(new Label("Debug", skinLibgdx, "default-font", Color.RED)).colspan(3);
		tbl.add(new Label("Debug", getSkinLibgdx(),  getFontName(), Color.RED)).colspan(3);
		tbl.row();
		tbl.columnDefaults(0).padRight(10);
		tbl.columnDefaults(1).padRight(10);
		// + Checkbox, "Show FPS Counter" label
		chkShowFpsCounter = new CheckBox("", getSkinLibgdx());
		tbl.add(new Label("Show FPS Counter", getSkinLibgdx()));
		tbl.add(chkShowFpsCounter);
		//optionsWindowTabUi.addItem(chkShowFpsCounter);
		testAddTabItem(chkShowFpsCounter);
		tbl.row();

		chkShowFollowPath = new CheckBox("", getSkinLibgdx());
		tbl.add(new Label("Show Follow Path", getSkinLibgdx()));
		tbl.add(chkShowFollowPath);
		testAddTabItem(chkShowFollowPath);
		tbl.row();

		chkShowTestMenu = new CheckBox("", getSkinLibgdx());
		tbl.add(new Label("Show Test Menu", getSkinLibgdx()));
		tbl.add(chkShowTestMenu);
		testAddTabItem(chkShowTestMenu);
		tbl.row();

		chkShowFrameBar = new CheckBox("", getSkinLibgdx());
		tbl.add(new Label("Show Frame Bar", getSkinLibgdx()));
		tbl.add(chkShowFrameBar);
		testAddTabItem(chkShowFrameBar);
		tbl.row();
		return tbl;
	}

	private Table buildOptWinButtons() {
		Table tbl = new Table();
		// + Separator
		Label lbl;
		lbl = new Label("", getSkinLibgdx());
		lbl.setColor(0.75f, 0.75f, 0.75f, 1);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = getSkinLibgdx().newDrawable("white");
		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1);
		tbl.row();
		lbl = new Label("", getSkinLibgdx());
		lbl.setColor(0.5f, 0.5f, 0.5f, 1);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = getSkinLibgdx().newDrawable("white");
		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
		tbl.row();
		// + Save Button with event handler
		btnWinOptSave = new TextButton("Save", getSkinLibgdx());
		tbl.add(btnWinOptSave).padRight(30);
//		optionsWindowTabUi.addItem(btnWinOptSave);
		testAddTabItem(btnWinOptSave);
		tmpLeftNode=tmpTabNode;

		btnWinOptSave.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onSaveClicked();
			}
		});
		// + Cancel Button with event handler
		btnWinOptCancel = new TextButton("Cancel", getSkinLibgdx());
		tbl.add(btnWinOptCancel);

//		optionsWindowTabUi.addItem(btnWinOptCancel);
		testAddTabItem(btnWinOptCancel);
		tmpTabNode.down=tabFirstNode;
		tabFirstNode.up=tmpTabNode;
		tmpLeftNode.right=tmpTabNode;
		tmpTabNode.left=tmpLeftNode;


		SGTabUDLRMap udlrMap=new SGTabUDLRMap();
		udlrMap.setFirstNode(tabFirstNode);
		optionsWindowTabUi.setTabMap(udlrMap);
		btnWinOptCancel.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onCancelClicked();
			}
		});
		return tbl;
	}

//	private float buttonWait=0.4f;
	TabUi optionsWindowTabUi;

	SGTabUDLRMap.SGTabUDLRNode tabFirstNode=null;
	SGTabUDLRMap.SGTabUDLRNode tmpTabNode=null;
	//为了便于做左边移动的map
	SGTabUDLRMap.SGTabUDLRNode tmpLeftNode=null;
	SGTabUDLRMap.SGTabUDLRNode tmpRightNode=null;
	private Table buildOptionsWindowLayer() {
		Skin skinTmp=getSkinLibgdx();
		winOptions = new Window("Options",skinTmp);
		optionsWindowTabUi =new TabUi();
		optionsWindowTabUi.setSkin(skinTmp);
		// + Audio Settings: Sound/Music CheckBox and Volume Slider
		winOptions.add(buildOptWinAudioSettings()).row();
		winOptions.add(buildOptWinVideoSettings()).row();
//		// + Character Skin: Selection Box (White, Gray, Brown)
//		winOptions.add(buildOptWinSkinSelection()).row();
		// + Debug: Show FPS Counter
		winOptions.add(buildOptWinDebug()).row();
		// + Separator and Buttons (Save, Cancel)
		winOptions.add(buildOptWinButtons()).pad(10, 0, 10, 0);

		// Make options window slightly transparent
		winOptions.setColor(1, 1, 1, 0.8f);
		// Hide options window by default
//		showOptionsWindow(false, false);//这样会有一瞬间显示出来
		winOptions.getColor().a=0;
		winOptions.setTouchable(Touchable.disabled);

		if (debugEnabled) {
			winOptions.debug();
		}
		// Let TableLayout recalculate widget sizes and positions
		winOptions.pack();
		// Move options window to bottom right corner
//		winOptions.setPosition(Constants.VIEWPORT_GUI_WIDTH - winOptions.getWidth() - 50, 50);
		winOptions.setPosition(ScreenSetting.WORLD_WIDTH - winOptions.getWidth() - 50, 50);
		return winOptions;
	}
	private void onOptionsClicked() {
		loadSettings();
		//showMenuButtons(false);
		showOptionsWindow(true, true);
	}
	private void onSaveClicked() {
		saveSettings();
		onCancelClicked();
//		AudioManager.instance.onSettingsUpdated();
	}

	private void onCancelClicked() {
		//showMenuButtons(true);
		showOptionsWindow(false, true);
//		AudioManager.instance.onSettingsUpdated();
	}
	private void loadSettings() {
//		GamePreferences prefs = GamePreferences.instance;
//		prefs.load();
//		chkSound.setChecked(prefs.sound);
//		sldSound.setValue(prefs.volSound);
//		chkMusic.setChecked(prefs.music);
//		sldMusic.setValue(prefs.volMusic);
//		chkOrthographic.setChecked(prefs.orthographic);
//		chkKofOrthographic.setChecked(prefs.kofOrthographic);
//		languageCombo.setSelected(sasha.getCountry());
////		selCharSkin.setSelectedIndex(prefs.charSkin);
////		onCharSkinSelected(prefs.charSkin);
//		chkShowFpsCounter.setChecked(prefs.showFpsCounter);
//		chkShowFollowPath.setChecked(prefs.showFollowPath);
//		chkShowTestMenu.setChecked(prefs.showTestMenu);
//		chkShowFrameBar.setChecked(prefs.showFrameBar);
	}
	private void saveSettings() {
//		GamePreferences prefs = GamePreferences.instance;
//		prefs.sound = chkSound.isChecked();
//		prefs.volSound = sldSound.getValue();
//		prefs.music = chkMusic.isChecked();
//		prefs.volMusic = sldMusic.getValue();
//		prefs.orthographic = chkOrthographic.isChecked();
//		prefs.kofOrthographic = chkKofOrthographic.isChecked();
////		prefs.charSkin = selCharSkin.getSelectedIndex();
//		prefs.showFpsCounter = chkShowFpsCounter.isChecked();
//		prefs.showFollowPath = chkShowFollowPath.isChecked();
//		prefs.showTestMenu= chkShowTestMenu.isChecked();
//		prefs.showFrameBar= chkShowFrameBar.isChecked();
//		prefs.save();
//
//		if(languageCombo.getSelected()!=sasha.getCountry()) {
//			sasha.setCountry(languageCombo.getSelected());
//			saveSashaData(sasha);
//			game.setScreen(new MainMenuScreen(game));
//			dispose();
//		}
	}
	private void showOptionsWindow(boolean visible, boolean animated) {
		float alphaTo = visible ? 0.8f : 0.0f;
		float duration = animated ? 1.0f : 0.0f;
		Touchable touchEnabled = visible ? Touchable.enabled : Touchable.disabled;
		winOptions.addAction(sequence(
				touchable(touchEnabled),
				alpha(alphaTo, duration)));
	}
	/*------------------游戏设置相关 end------------------*/
	/*------------------apk自动更新------------------*/
//	private SGFileDownloader jarDownloader=null;
	private ProgressBar jarPB=null;
	private final String gameVersion="1.0.1";
	private SGConfirmPopups updateJarPopups=null;
	private void downloadJar(
			boolean forceUpdate
	) {
		if(null!=game.getJarDownloader()){return;}
		game.setJarDownloader(new SGFileDownloader());

//		switch (Gdx.app.getType()){
//			case Android:
//				game.getJarDownloader().download(url.getApkUrl(),Gdx.files.external( Constants.EXTERNAL_APK_FILE),forceUpdate);
//				break;
//			case Desktop:
//				game.getJarDownloader().download(url.getJarUrl(),Gdx.files.local( Constants.LOCAL_JAR_FILE),forceUpdate);
//				break;
//			default:
//				break;
//		}
	}

//	public static void getLastVersion(SGAction1<String> action){
//		SGLibGdxHelper.getHttpStringAsync(url.getGameVersion(),action);
//	}

	public static void getLastVersion(SGAction1<String> action){
//		switch (Gdx.app.getType()){
//			case Android:
//				SGLibGdxHelper.getHttpStringAsync(url.getGameVersion(),action);
//				break;
//			case Desktop:
//				SGLibGdxHelper.getHttpStringAsync(url.getJarVersion(),action);
//				break;
//			default:
//				break;
//		}
	}
	/*------------------apk自动更新 end------------------*/
}

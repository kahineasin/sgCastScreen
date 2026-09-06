package com.sellgirl.castScreen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.sellgirl.castScreen.CastScreen;
import com.sellgirl.castScreen.Constants;
import com.sellgirl.castScreen.IDLNADeviceScanner;
import com.sellgirl.castScreen.IKnightSasha;
import com.sellgirl.castScreen.IOnDeviceScanListener;
import com.sellgirl.castScreen.Language;
import com.sellgirl.castScreen.ScreenSetting;
import com.sellgirl.castScreen.model.DeviceIp;
import com.sellgirl.sgGameHelper.SGGameHelper;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class SignalImgScreen implements Screen// extends ApplicationAdapter
 {
//    private SpriteBatch batch;
//    private Texture image;

    private IKnightSasha game;
     private Stage stage;
     private Table table;
//     final MusicPlayer game;

     //	// OrthographicCamera camera;
//	@Deprecated
//	Controller controller;
//	SGPS5Gamepad sgcontroller;
//	private static IApiUrl url = null;
//	SashaData sasha = null;
     private Skin skin;
     private  boolean test=false;
     private AssetManager manager;
    public SignalImgScreen(IKnightSasha game){
        this.game=game;
        create();
    }
    private int cnt=0;
    private TextField mp4TF;
//    @Override
    public void create() {
//        batch = new SpriteBatch();
//        image = new Texture("libgdx.png");

//		initLibGdx();
//		initSG();
//        initProj();

        // 使用伸展视口（StretchViewport）创建舞台
        stage = new Stage(new StretchViewport(ScreenSetting.WORLD_WIDTH, ScreenSetting.WORLD_HEIGHT));

//      /* 事件初始化 */
//
        // 首先必须注册输入处理器（stage）, 将输入的处理设置给 舞台（Stage 实现了 InputProcessor 接口）
        // 这样舞台才能接收到输入事件, 分发给相应的演员 或 自己处理。
        Gdx.input.setInputProcessor(stage);


//		skin = MainMenuScreen.getSkin();
//        game.font=MainMenuScreen.getFont2();//刷新新的中文字
//        game.font=game.getFont2();
//        game.font.setColor(Color.WHITE);
        skin = CastScreen.getSkin2(game.getFont());

        skin.add("default",game.getFont());
        skin.add("default", CastScreen.getButtonStyle(skin));
        skin.add("default", CastScreen.getLabelStyle(skin));
        skin.add("default", CastScreen.getTextFieldStyle(skin));
        skin.add("default", CastScreen.getWindowStyle(skin));
        skin.add("default", CastScreen.getCheckBoxStyle(skin));
        skin.add("default-horizontal", CastScreen.getSliderStyle(skin));


//        skinLibgdx=skin;
        //skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));
        //skinLibgdx = new Skin(Gdx.files.internal("skin/uiskin.json"), new TextureAtlas("skin/uiskin.atlas"));


        int padding=20;
        table = new Table();
        table.setFillParent(true);
        table.pad(padding);

        Table listTable=new Table();

        int buttonSpace=20;



        Preferences preferences = Gdx.app.getPreferences(Constants.SYSTEM_SETTING);
        String s =  preferences.getString("folder");
        Json json = new Json();
//        MusicFolder folder=json.fromJson(MusicFolder.class,s);
        String[] folder=null;
        if(null!=s&&!s.isEmpty()&&!s.isBlank()){
            folder=s.split(",");
        }
//        Iterator<String> iter = folder.keySet().iterator();
//        while(iter.hasNext()){
        while (null==game.getScanner()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        mp4TF=new TextField(
//            "http://mp3.sellgirl.com/img/web_sasha_1920x1080_02.jpg"
            "http://mp3.sellgirl.com/mp3/v/IGNITE_%E5%AE%8C%E6%95%B4%E7%89%88.mp4"
            ,skin);
        mp4TF.setWidth(ScreenSetting.WORLD_WIDTH*0.5f);

        IDLNADeviceScanner scanner=game.getScanner();
        if(scanner.getDevice().size!=cnt//&& null!=folder
        ) {
            for (DeviceIp key : game.getScanner().getDevice()) {
//            String key=iter.next();
//            final String value = folder.get(key);
                final TextButton btn = new TextButton(key.name, skin);

                btn.addListener(new ClickListener() {

                    @Override
                    public void clicked(InputEvent event, float x, float y) {
//                        goToPlayPage(key);
//                        game.getCaster().startCasting(key);
                        game.getCaster().startCastingWeb(key,mp4TF.getText());
                    }
                });

                listTable.add(btn).spaceBottom(buttonSpace);
                listTable.row();
                //System.out.println(key+" "+value);
            }
        }

//        scanner = new DLNADeviceScanner2();
//        scanner.init(this);
        scanner.setScanListener(new IOnDeviceScanListener() {
            @Override
            public void onDeviceFound(DeviceIp device) {
                Gdx.app.postRunnable(() -> {
//                    Log.d("DLNA", "Found: " + device.getFriendlyName());
                    SGDataHelper.getLog().print("DLNA Found: " + device.name);
                    final TextButton btn = new TextButton( device.name, skin);
                    btn.addListener(new ClickListener() {

                        @Override
                        public void clicked(InputEvent event, float x, float y) {
//                        goToPlayPage(key);
//                            game.getCaster().startCasting(device);
                            game.getCaster().startCastingWeb(device,mp4TF.getText());
                        }
                    });
                    listTable.add(btn).spaceBottom(buttonSpace);
                    listTable.row();
                    listTable.layout();
                });
            }
            @Override public void onDeviceLost(DeviceIp device) {}
            @Override public void onScanStarted() { /*Log.d("DLNA", "Scan started"); */}
            @Override public void onScanStopped() {/* Log.d("DLNA", "Scan stopped");*/ }
            @Override public void onError(String error) {
                /*Log.e("DLNA", "Error: " + error);*/
            }
        });
        game.setScanner(scanner);

        ScrollPane scrollPane=new ScrollPane(listTable);
        manager = new AssetManager();
        Language TXT=new Language(manager);
        SGGameHelper.setLanguage(TXT);
//        Locale locale = new Locale("fr", "CA", "VAR1");
//        Language TXT=new Language(locale);
//        TextButton addFolderBtn=new TextButton(TXT.g("subscribe music"),skin);
//        addFolderBtn.addListener(new ClickListener() {
//
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                goToSubscribePage();
//            }
//        });

        TextButton exitGameBtn = new TextButton(TXT.g("back"), skin);
        exitGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exitGame();
            }
        });
        table.add(mp4TF).width(ScreenSetting.WORLD_WIDTH*0.5f).spaceBottom(buttonSpace);
        table.row();
        table.add(scrollPane).spaceBottom(buttonSpace);
        table.row();
//        table.add(addFolderBtn).spaceBottom(buttonSpace);
//        table.row();
        table.add(exitGameBtn).spaceBottom(buttonSpace);
        table.row();

        stage.addActor(table);
        stage.setDebugAll(test);

//		Table layerOptionsWindow = buildOptionsWindowLayer();
//		stage.addActor(layerOptionsWindow);



////		System.out.println("main screen b"+(++cnt));
//		this.controller = SGLibGdxHelper.getGamepad();
////		System.out.println("main screen e"+(cnt));
//
//		if (null != controller) {
//			sgcontroller=new SGPS5Gamepad(controller);
//			this.controller.addListener(new SGXInputControllerListener());
//		}

//        if(null==game.getJarDownloader()&&
//            (Application.ApplicationType.Android==Gdx.app.getType()
//                ||Application.ApplicationType.Desktop==Gdx.app.getType())
//        ) {
//            getLastVersion(new SGAction1<String>() {
//                @Override
//                public void go(String s) {
//                    String version = s;
//                    if (null != version && 0 < SGDataHelper.compareVersion(version, gameVersion)) {
//
//                        SGConfirmPopups confirmPopups= new SGConfirmPopups(SGDataHelper.FormatString(TXT.g("found new version {0}, update now?"), version),
//                            new Consumer<Object>() {
//                                @Override
//                                public void accept(Object o) {
//                                    downloadJar(true);
//                                }
//                            },
//                            skin
//                        );
//                        confirmPopups.show(stage);
////                // 将对话框右下
//                        confirmPopups.setPosition(
//                            (stage.getWidth() - confirmPopups.getWidth()) *7f/ 8f,
//                            (stage.getHeight() - confirmPopups.getHeight()) / 8f);
//                    }
//                }
//            });
//        }
    }

    @Override
    public void render(float delta) {
//        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
//        batch.begin();
//        batch.draw(image, 140, 210);
//        batch.end();

//        if(null!=game.getJarDownloader()&& game.getJarDownloader().downloading){
//            if(null==jarPB){
//                jarPB=new ProgressBar(0,100,1,false,skin);
//                jarPB.setX(ScreenSetting.WORLD_WIDTH*3f/4f);
//                jarPB.setY(ScreenSetting.WORLD_HEIGHT/4f);
//                stage.addActor(jarPB);
//            }
//            if(100<=game.getJarDownloader().progress){
//                if(game instanceof AndroidMusicPlayer){
//                    ((AndroidMusicPlayer)game).updateApk(Constants.EXTERNAL_APK_FILE);
//////                    dispose();
////                    return;
//                    //在某些系统上，可能有漏掉的权限导致updateApk没反应，下面这行是为了防止死循环
//                    game.getJarDownloader().downloading=false;
//                }else if(Application.ApplicationType.Desktop== Gdx.app.getType()){// 启动更新脚本并退出当前应用
//                    try {
//                        new ProcessBuilder("cmd", "/c", "start", "updater.bat").start();
//                        Gdx.app.exit();
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }else{
//                jarPB.setValue(game.getJarDownloader().progress);
//            }
//        }

        if(null==stage){return;}    //这句也一定不能少
        ScreenUtils.clear(0, 0, 0.2f, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		stage.act(Gdx.graphics.getDeltaTime());
        stage.act(delta);
        stage.draw();

        Batch batch=stage.getBatch();
        batch.begin();
//        game.getFont2()//.font
//            .draw(batch,"version:"+gameVersion,ScreenSetting.WORLD_WIDTH-200,30);
//        game.font//.font
//            .draw(batch,"version:"+gameVersion,ScreenSetting.WORLD_WIDTH-300,40);
        batch.end();
    }

     @Override
     public void show() {
         // System.out.println("listeners count "+enterGameBtn.getListeners().size);

     }
     @Override
     public void resize(int width, int height) {
         // TODO Auto-generated method stub
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
//         batch.dispose();
//         image.dispose();

         if(null!=stage) {
             stage.dispose();
             stage=null;  //这句一定不能少, 吃过亏

         }
         if(null!=skin) {
             skin.dispose();
             skin=null;
         }

//         if(null!=skinLibgdx){ skinLibgdx.dispose();skinLibgdx=null;}


//         manager.dispose();
         //System.out.println(this.getClass().getSimpleName()+" dispose");
     }
//
//     @Override
//    public void dispose() {
//        batch.dispose();
//        image.dispose();
//    }
private void exitGame() {

//		screen.dispose();
////		PauseScreen.this.dispose();
    game.setScreen(new MainMenuScreen(game));
    dispose();
    //game.dispose();
//    Gdx.app.exit();
}
}

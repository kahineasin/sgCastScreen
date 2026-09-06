//package com.sellgirl.castScreen.android.screen;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.Label;
//import com.badlogic.gdx.scenes.scene2d.ui.Skin;
//import com.badlogic.gdx.scenes.scene2d.ui.Table;
//import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
//import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
//import com.sellgirl.castScreen.IKnightSasha;
//import com.sellgirl.castScreen.screen.MainMenuScreen;
//import com.sellgirl.castScreen.android.AndroidLauncher;
//import com.sellgirl.sgJavaHelper.config.SGDataHelper;
//import com.sellgirl.sgJavaHelper.time.Waiter;
//
//public class PermissionScreen implements Screen {
//    private final IKnightSasha game;
//    private Stage stage;
//    private Skin skin;
//    private Table table;
//    private Label titleLabel;
//    private Label descLabel;
//    private TextButton grantButton;
//    private TextButton skipButton;
//    private Waiter waiter=new Waiter(2);
//    public PermissionScreen(IKnightSasha game) {
//        this.game = game;
//    }
//
//    private BitmapFont font;
//    @Override
//    public void show() {
//        stage = new Stage();
//        Gdx.input.setInputProcessor(stage);
//
//        font=game.getFont3(36);
//        // 创建UI（这里简化，实际应该使用合适的Skin）
//        skin =
//        MainMenuScreen.getSkin2(font);
//
//        table = new Table();
//        table.setFillParent(true);
//        stage.addActor(table);
//
//        titleLabel = new Label("需要权限", skin);
////        descLabel = new Label("游戏需要以下权限才能完整运行：\n• 悬浮窗权限 - 显示游戏工具覆盖层\n• 存储权限 - 保存游戏进度", skin);
//        descLabel = new Label(getLabText(), skin);
//
//        grantButton = new TextButton("授予权限", skin);
//        skipButton = new TextButton("稍后再说", skin);
//
//        table.add(titleLabel).padBottom(20).row();
//        table.add(descLabel).width(300).padBottom(40).row();
//        table.add(grantButton).width(200).height(60).padBottom(20).row();
//        table.add(skipButton).width(200).height(60);
//
//        // 按钮事件
//        grantButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                game.onUserRequestedPermissions();
//            }
//        });
//
//        skipButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                // 使用受限模式继续
//                game.onPermissionCheckComplete(false);
//            }
//        });
//
//    }
//
//    public void showPermissionRequest() {
//        // 确保UI状态正确
//        if (grantButton != null) {
//            grantButton.setDisabled(false);
//        }
//    }
//
//    public void showPermissionDenied() {
//        if (titleLabel != null) {
//            titleLabel.setText("权限不足");
//        }
//        if (descLabel != null) {
//            descLabel.setText("部分功能可能无法使用。\n您可以在设置中手动开启权限。");
//        }
//    }
//
//    private boolean b1=false;
//    private boolean b2=true;
//    private boolean b3=false;
//    private String getLabText(){
//        return SGDataHelper.FormatString("游戏需要以下权限才能完整运行：\n• 悬浮窗权限 - 显示游戏工具覆盖层 {0} \n• 存储权限 - 自动更新软件 {1} \n •无障碍 - 模拟 {2}",
//                b1?"有":"无",
//                b2?"有":"无",
//                b3?"有":"无"
//                );
//    }
//    @Override
//    public void render(float delta) {
//        if(waiter.isOK()) {
//            if (game.isOverlayFeatureAvailable() && game.isAccessibilityFeatureAvailable()) {
//                game.setScreen(new MainMenuScreen(game));
//                this.dispose();
//                return;
//            }
//            boolean b = false;
//            if (!b1 && game.isOverlayFeatureAvailable()) {
//                b1 = true;
//                b = true;
//            }
//            if (!b2 && game.isAccessibilityFeatureAvailable()) {
//                b2 = true;
//                b = true;
//            }
//            if(b){
//                descLabel.setText(getLabText());
//            }
//        }
//        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        //已利用AndroidGamepadTool2.onPermissionCheckComplete()来判断权限跳转
//
//        stage.act(delta);
//        stage.draw();
//    }
//
//    @Override
//    public void resize(int width, int height) {
//        stage.getViewport().update(width, height, true);
//    }
//
//    @Override
//    public void pause() {}
//
//    @Override
//    public void resume() {}
//
//    @Override
//    public void hide() {}
//
//    @Override
//    public void dispose() {
//        if (stage != null) stage.dispose();
//        if (skin != null) skin.dispose();
//        if (font != null) font.dispose();
//    }
//}

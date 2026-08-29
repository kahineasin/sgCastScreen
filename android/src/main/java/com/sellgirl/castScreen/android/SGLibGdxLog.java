package com.sellgirl.castScreen.android;

import com.badlogic.gdx.Gdx;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.log.ISGLog;

public class SGLibGdxLog implements ISGLog {
    /**
     * 这里一定别用类名, 以防被混淆了
     */
    private final String tag="knightSasha";
    @Override
    public void writeException(Throwable e, String filePrev) {
        //Gdx.app.error(tag, e.toString());
//        Gdx.app.error(tag, SGDataHelper.getLineInfo(e) );
        Gdx.app.error(tag+filePrev, SGDataHelper.getLineInfo(e) ,e);
    }

    @Override
    public void write(Object e, String filePrev) {
        Gdx.app.log(tag, e.toString());
    }

    @Override
    public void printException(Throwable e,String tag) {
//        if(InvocationTargetException.class==e.getClass()){
//
//            //Gdx.app.debug(tag,SGDataHelper.getLineInfo(e));
//            if(null==tag){
//                Gdx.app.debug(this.tag,SGDataHelper.getErrorFullString(((InvocationTargetException) e).getTargetException()));
//            }else{
//
//                Gdx.app.debug(this.tag+" "+tag,SGDataHelper.getErrorFullString(((InvocationTargetException) e).getTargetException()));
//            }
//        }
        //Gdx.app.debug(tag,SGDataHelper.getLineInfo(e));
        if(null==tag){
            Gdx.app.debug(this.tag,SGDataHelper.getErrorFullString(e));
        }else{

            Gdx.app.debug(this.tag+" "+tag,SGDataHelper.getErrorFullString(e));
        }
    }

    @Override
    public void print(Object e) {
        Gdx.app.debug(tag, e.toString());
    }
}

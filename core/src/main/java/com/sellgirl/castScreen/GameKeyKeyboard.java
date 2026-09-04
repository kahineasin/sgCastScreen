package com.sellgirl.castScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.sellgirl.sgGameHelper.gamepad.ISGPS5Gamepad;//import com.mygdx.game.share.gamepad.ISGPS5Gamepad;

import java.util.HashMap;

/**
 * 游戏按钮, 用于按键设置映射(键盘方式)
 * 此类是方案2, 根据游戏screen上的功能来定义影射
 * 暂时不允许设置L2 R2吧, 以后改也可以的
 * 存在的问题:
 * 1. 由于按键可以组合, 那么轴功能是否能用就很难判断了
 */
public class GameKeyKeyboard implements IKnightSashaGameKey {

    /**
     * 这些值都是binary模式
     */
    private int jump;
    private int dodge;
    private int attack;
    private int heaveAttack;
    private int defend;
    private int l2;
    private int r1;
    private int r2;

    private int gamepadMask;
    public GameKeyKeyboard(){
        init();
    }
    public void init(){
//        jump=XBoxKey.CROSS.getBinary();
//        dodge=XBoxKey.ROUND.getBinary();
//        attack=XBoxKey.SQUARE.getBinary();
//        heaveAttack=XBoxKey.TRIANGLE.getBinary();
//        defend=XBoxKey.L1.getBinary();
////        l2=XBoxKey.L2.getBinary();
//        r1=XBoxKey.R1.getBinary();
////        r2=XBoxKey.R2.getBinary();

//        //Input.Keys 随便就超54
//        //int只能处理32, long也只有64, 所以键盘就不使用多键同按的mask方式了
//        jump= 1 <<Input.Keys.X;
//        dodge= 1 <<Input.Keys.C;
//        attack= 1 <<Input.Keys.Z;
//        heaveAttack=1 << Input.Keys.V;
//        defend= 1 <<Input.Keys.SHIFT_LEFT;
//        r1= 1 <<Input.Keys.S;


        jump= Input.Keys.X;
        dodge= Input.Keys.C;
        attack= Input.Keys.Z;
        heaveAttack= Input.Keys.V;
        defend= Input.Keys.SHIFT_LEFT;
        r1= Input.Keys.S;
//        L2= Input.Keys.D;
//        R2= Input.Keys.F;
//        rightX1= Input.Keys.Q;
//        rightX2= Input.Keys.R;
//        rightY1= Input.Keys.E;
//        rightY2= Input.Keys.W;
//        START= Input.Keys.P;

    }
    public ISGPS5Gamepad getGamepad() {
        return gamepad;
    }

    public void setGamepad(ISGPS5Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    private ISGPS5Gamepad gamepad=null;
    public void update(){
        //键盘很多键, 应该只针对设置过的键做mask运算
//        gamepadMask=gamepad.getQuickBtnKey();
        //        //XBoxKey r=null;
//        int mask=0;
//
//        if(gamepad.isSQUARE()) {
//            //return XBoxKey.SQUARE;
////				mask=XBoxKey.config(mask, XBoxKey.SQUARE, true);
//            mask|= XBoxKey.SQUARE.getBinary();
//        }
//        if(gamepad.isTRIANGLE()) {
//            mask|=XBoxKey.TRIANGLE.getBinary();
//        }
//        if(gamepad.isCROSS()) {
////				return XBoxKey.CROSS;
//            //mask=XBoxKey.config(mask, XBoxKey.CROSS, true);
//            mask|=XBoxKey.CROSS.getBinary();
//        }
//        if(gamepad.isROUND()) {
////				return XBoxKey.ROUND;
//            //XBoxKey.config(mask, XBoxKey.ROUND, true);
//            mask|=XBoxKey.ROUND.getBinary();
//        }
//        if(gamepad.isL1()) {
////				return XBoxKey.L1;
//            //XBoxKey.config(mask, XBoxKey.L1, true);
//            mask|=XBoxKey.L1.getBinary();
//        }
//        if(gamepad.isR1()) {
////				return XBoxKey.R1;
//            //XBoxKey.config(mask, XBoxKey.R1, true);
//            mask|=XBoxKey.R1.getBinary();
//        }
////        if(gamepad.isL2()) {
////            mask|=XBoxKey.L2.getBinary();
////        }
////        if(gamepad.isR2()) {
////            mask|=XBoxKey.R2.getBinary();
////        }
////		}
////        return mask;
//        gamepadMask=mask;
    }
    public boolean isJump(){
        //return SGDataHelper.EnumHasFlag(gamepadMask,jump);
        return Gdx.input.isKeyPressed(jump);
    }
    public boolean isDodge(){

        //return SGDataHelper.EnumHasFlag(gamepadMask,dodge);

        return Gdx.input.isKeyPressed(dodge);
    }
    public boolean isAttack(){

        //return SGDataHelper.EnumHasFlag(gamepadMask,attack);

        return Gdx.input.isKeyPressed(attack);
    }
    public boolean isSkill(){

//        return SGDataHelper.EnumHasFlag(gamepadMask,heaveAttack);

        return Gdx.input.isKeyPressed(heaveAttack);
    }
    public boolean isDefend(){

//        return SGDataHelper.EnumHasFlag(gamepadMask,defend);

        return Gdx.input.isKeyPressed(defend);
    }
    public boolean isKick(){

        //return SGDataHelper.EnumHasFlag(gamepadMask,r1);

        return Gdx.input.isKeyPressed(r1);
    }
//    public float axisL2(ISGPS5Gamepad gamepad) {
//        return gamepad.axisL2();
//    }
//    public float axisR2(ISGPS5Gamepad gamepad) {
//        return gamepad.axisR2();
//    }

    //-------------properties-----------
    public int getJump() {
        return jump;
    }

    public void setJump(int jump) {
        this.jump = jump;
    }

    public int getDodge() {
        return dodge;
    }

    public void setDodge(int dodge) {
        this.dodge = dodge;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getSkill() {
        return heaveAttack;
    }

    public void setSkill(int heaveAttack) {
        this.heaveAttack = heaveAttack;
    }

    public int getDefend() {
        return defend;
    }

    public void setDefend(int defend) {
        this.defend = defend;
    }

    public int getL2() {
        return l2;
    }

    public void setL2(int l2) {
        this.l2 = l2;
    }

    public int getKick() {
        return r1;
    }

    public void setKick(int r1) {
        this.r1 = r1;
    }

    public int getR2() {
        return r2;
    }

    public void setR2(int r2) {
        this.r2 = r2;
    }

//    public HashMap<String,Integer> toMap(){
//        HashMap<String,Integer> r=new HashMap<>();
////        r.put("jump",jump);
////        r.put("dodge",dodge);
////        r.put("attack",attack);
////        r.put("heaveAttack",heaveAttack);
////        r.put("defend",defend);
//////        r.put("l2",l2);
////        r.put("r1",r1);
//////        r.put("r2",);
//        toMap2(r);
//        return r;
//
//    }
    public HashMap<String,Integer> toMap(HashMap<String,Integer> r){

        return GameKey.gameKeyToMap(this,r);
    }
    public void applyMap(HashMap<String,Integer> map){

       GameKey.gameKeyApplyMap(this,map);
    }
    public String getKeyNamesByMask(int mask){
//        ArrayList<Integer> keys=new ArrayList<>();
//        int i=1;
//        int tmpMask=mask;
//        while (0<tmpMask){
//            int tmp=tmpMask/2;
//            if(1==tmpMask-tmp){
//                keys.add(i);
//            }
//            tmpMask=tmp;
//            i*=2;
//        }
//
//        StringBuilder sb=new StringBuilder();
//        boolean isFirst=true;
//        for(Integer key: keys){
//            //if(SGDataHelper.EnumHasFlag(features,i.getBinary())){
//                if(!isFirst){
//                    sb.append("+");
//                }
//                sb.append( Input.Keys.toString(key));
//                isFirst=false;
//            //}
//        }
//        return sb.toString();
////        Input.Keys.toString(1);
////        SGDataHelper.EnumHasFlag()
////        return XBoxKey.getTexts(mask);
        return Input.Keys.toString(mask);
    }
    @Override
    public String getKeyName(KnightKey key) {
        switch (key){
            case A:
                return Input.Keys.toString(jump);
            case B:
                return Input.Keys.toString(dodge);
            case X:
                return Input.Keys.toString(attack);
            case Y:
                return Input.Keys.toString(heaveAttack);
            case L1:
                return Input.Keys.toString(defend);
            case R1:
                return Input.Keys.toString(r1);
            default:
                return key.toString();
        }
    }
}

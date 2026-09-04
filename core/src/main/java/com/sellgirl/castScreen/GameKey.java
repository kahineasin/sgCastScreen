package com.sellgirl.castScreen;

import com.sellgirl.sgGameHelper.gamepad.ISGPS5Gamepad;//import com.mygdx.game.share.gamepad.ISGPS5Gamepad;
import com.sellgirl.sgGameHelper.gamepad.XBoxKey;//import com.mygdx.game.share.XBoxKey;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

import java.util.HashMap;

/**
 * 游戏按钮, 用于按键设置映射
 * 此类是方案2, 根据游戏screen上的功能来定义影射
 * 暂时不允许设置L2 R2吧, 以后改也可以的
 * 存在的问题:
 * 1. 由于按键可以组合, 那么轴功能是否能用就很难判断了
 */
public class GameKey implements IKnightSashaGameKey{

    /**
     * 这些值都是binary模式
     */
    private int jump;
    private int dodge;
    private int attack;
    private int skill;
//    private int kick;
    private int defend;
    private int l2;
    private int kick;
    private int r2;

    private int gamepadMask;
    public GameKey(){
        init();
    }
    public void init(){
        jump=XBoxKey.CROSS.getBinary();
        dodge=XBoxKey.ROUND.getBinary();
        attack=XBoxKey.SQUARE.getBinary();
//        skill =XBoxKey.TRIANGLE.getBinary();
        skill =XBoxKey.R1.getBinary();
        defend=XBoxKey.L1.getBinary();
//        l2=XBoxKey.L2.getBinary();
//        kick =XBoxKey.R1.getBinary();
        kick =XBoxKey.TRIANGLE.getBinary();
//        r2=XBoxKey.R2.getBinary();

    }

    public ISGPS5Gamepad getGamepad() {
        return gamepad;
    }

    public void setGamepad(ISGPS5Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    private ISGPS5Gamepad gamepad=null;
    public void update(){
        gamepadMask=gamepad.getQuickBtnKey();
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
        return SGDataHelper.EnumHasFlag(gamepadMask,jump);
    }
    public boolean isDodge(){
        return SGDataHelper.EnumHasFlag(gamepadMask,dodge);
    }
    public boolean isAttack(){
        return SGDataHelper.EnumHasFlag(gamepadMask,attack);
    }
    public boolean isSkill(){
        return SGDataHelper.EnumHasFlag(gamepadMask, skill);
    }

//    @Override
//    public boolean isKick() {
//        return SGDataHelper.EnumHasFlag(gamepadMask,heaveAttack);
//    }

    public boolean isDefend(){
        return SGDataHelper.EnumHasFlag(gamepadMask,defend);
    }
    public boolean isKick(){
        return SGDataHelper.EnumHasFlag(gamepadMask, kick);
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
        return skill;
    }

    public void setSkill(int heaveAttack) {
        this.skill = heaveAttack;
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
        return kick;
    }

    public void setKick(int r1) {
        this.kick = r1;
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

        return gameKeyToMap(this,r);

    }
    public static HashMap<String,Integer> gameKeyToMap(IKnightSashaGameKey gameKey, HashMap<String,Integer> r){
        //HashMap<String,Integer> r=new HashMap<>();
        r.put("jump",gameKey.getJump());
        r.put("dodge",gameKey.getDodge());
        r.put("attack",gameKey.getAttack());
        r.put("kick",gameKey.getKick());
        r.put("skill",gameKey.getSkill());
        r.put("defend",gameKey.getDefend());
//        r.put("l2",l2);
//        r.put("r2",);
        return r;

    }
    public void applyMap(HashMap<String,Integer> map){

        gameKeyApplyMap(this,map);
    }
    public static void gameKeyApplyMap(IKnightSashaGameKey gameKey,HashMap<String,Integer> map){
        //HashMap<String,Integer> r=new HashMap<>();
        gameKey.setJump(map.get("jump"));
        gameKey.setDodge(map.get("dodge"));
        gameKey.setAttack(map.get("attack"));
        gameKey.setSkill(map.get("skill"));
        gameKey.setDefend(map.get("defend"));
//        r.put("l2",l2);
        gameKey.setKick(map.get("kick"));
//        r.put("r2",);
        //return r;

    }
    public String getKeyNamesByMask(int mask){
        return XBoxKey.getTexts(mask);
    }

    @Override
    public String getKeyName(KnightKey key) {
        switch (key){
            case A:
                return XBoxKey.getTexts(jump);
            case B:
                return XBoxKey.getTexts(dodge);
            case X:
                return XBoxKey.getTexts(attack);
            case Y:
                return XBoxKey.getTexts(kick);
            case L1:
                return XBoxKey.getTexts(defend);
            case R1:
                return XBoxKey.getTexts(skill);
            default:
                return key.toString();
        }
    }
}

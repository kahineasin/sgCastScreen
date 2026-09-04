package com.sellgirl.castScreen;

import com.sellgirl.sgGameHelper.gamepad.ISGPS5Gamepad;//import com.mygdx.game.share.gamepad.ISGPS5Gamepad;

import java.util.HashMap;

/**
 * KnightSasha格斗screen页使用的按键设置
 */
public interface IKnightSashaGameKey {
    /**
     * @deprecated 没必要，直接用gameKey.getR1()之类的返回int
     */
    @Deprecated
    public enum KnightKey{
        A,B,X,Y,L1,R1
    }
    void init();
    void update();
    ISGPS5Gamepad getGamepad();

    void setGamepad(ISGPS5Gamepad gamepad);

    //-------------按键状态-----------
    boolean isJump();
    boolean isDodge();
    boolean isAttack();
    boolean isSkill();
//    boolean isKick();
    boolean isDefend();
    boolean isKick();

    //这方法用于QuickBtn,似乎不通用于 按钮图标

    /**
     * 获得按钮图标或字符
     * 使用方法gameKey.getKeyNamesByMask(gameKey.getJump())
     * @param mask
     * @return
     */
    String getKeyNamesByMask(int mask);
    @Deprecated
    String getKeyName(KnightKey key);

    //-------------properties-----------
    int getJump();

    void setJump(int jump);

    int getDodge();

    void setDodge(int dodge);

    int getAttack();

    void setAttack(int attack);

    int getSkill();

    void setSkill(int heaveAttack);

    int getDefend();

    void setDefend(int defend);

    int getL2();

    void setL2(int l2);

    int getKick();

    void setKick(int r1);

    int getR2();

    void setR2(int r2);


    //-------------Map转换-----------
    HashMap<String,Integer> toMap(HashMap<String,Integer> r);
    void applyMap(HashMap<String,Integer> map);
}

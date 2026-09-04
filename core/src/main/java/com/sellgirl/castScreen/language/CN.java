package com.sellgirl.castScreen.language;

import java.util.HashMap;
import java.util.Map;

/**
 * 英文大小写规范：
 * 按钮上的字， 全小写
 */
public class CN {
	public static Map<String,String> get(){
		Map<String,String> r=new HashMap<String,String>();
		//mainMenu
		r.put("found new version {0}, update now?","有新版本{0},现在更新吗？");
		r.put("leaderboard","排行榜");
		//guide1 screen
		r.put("game support all gamepad, exam PS5 XBOX, key mark use PS5. ",
				"游戏支持所有种类的手柄, 如xbox ps5, 按钮符号统一使用ps5手柄的符号. ");
		r.put("gamepad: leftStick move, □ atk, △ kick, X jump, ○ dodge/def, camera L1+L2 near/R2 far, START for pause",
				"手柄操作方式(推荐): 左摇杆移动  □攻击  X跳  ○躲闪  L1防御  镜头L1+L2近/R2远  菜单键,游戏中暂停");
		r.put(
				"keyboard: directKey move, z atk, x jump, c dodge/def, camera SHIFT+D near/F far, P for pause",
				"键盘操作方式: 方向键移动  z攻击  x跳  c躲闪  shift防御  镜头SHIFT+D近/F远 P暂停");
		r.put("skill: 1. ↓↘→□ ball  2. →↘↓↘→□ up sword  3. ↓↘→↓↘→□ ultimate skill  4. →→跑  5. R2 fly",
				"技能表: 1. ↓↘→□ 气功  2. →↘↓↘→□ 天翔龙闪  3. ↓↘→↓↘→□ 超必杀剑  4. →→跑  5. 飞");
		r.put(
				"movement: 1. two times jump  2. dodge attack  3. Hit Stun  4. def unstum  5. air platform ",
				"行为方式: 1. 二段跳  2. 可以躲闪怪物的攻击  3. 受到攻击会有硬直  4. 防御不会有硬直  5. 黄色矩形为石平台,可以跳上去  6. 可以击杀鬼魂  7. 应该跳过黑色的齿轮");
		r.put("character attr: 1. str(up when kill enemy)  2. agi cause miss(up when ball hit)  3. def(up when def success)",
				"人物属性: 1. str攻击力(杀怪可以提高攻击力)  2. agi敏捷可以使怪物的攻击miss(气功击中怪物提高敏捷)  3. def防御(防御攻击后提高防御力)");
		//SelectCharacterScreen
		r.put("change character, press □","选择角色. 切换角色 □");
		r.put("selected","当前选择");
		//Guide2
		r.put("Game operation demonstration.","游戏操作演示.");
		r.put("dodge","躲闪");
		r.put("defend","防御");
		r.put("jump","跳");
		r.put("QiGong (or R1)","气功 (简化R1)");
		r.put("Shoryuken (or →R1)","天翔龙闪 (简化→R1)");
		r.put("ultimate sword (or R1+△)","必杀剑 (简化R1+△)");
		r.put("ultimate QiGong (or R1+△)","大气功 (简化R1+△)");
		//leaderboard
		r.put("[user name]","[用户名]");
		r.put("[killed]","[击杀数量]");

		r.put("edit userInfo, or press TRIANGLE", "编辑用户信息,或者点 △");
		r.put("enter the game, or press X", "进入游戏,或者点 X");
		r.put("enter the game, or press ", "进入游戏,或者点 ");
		r.put("enter the game(ecs), or press △", "进入游戏(ecs),或者点 △");
		r.put("enter the game(ecs), or press ", "进入游戏(ecs),或者点 ");
		r.put("enter the game", "进入游戏");
		r.put("enter the game (wait {0} seconds)","进入游戏(等{0}秒)");
		r.put("(wait {0} seconds)","(等{0}秒)");
		r.put("enter the 3d game, or press X", "进入3d游戏,或者点 X");
		r.put("enter the 3d game(ecs), or press △", "进入3d游戏(ecs),或者点 △");
		r.put("enter the 3d game(ecs), or press ", "进入3d游戏(ecs),或者点 ");
		r.put("enter the 3d game, or press ", "进入3d游戏,或者点 ");
		r.put("enter the 3d game", "进入3d游戏");
		r.put("or press", "或者点");
		r.put("enter the 2d game, or press □", "进入2d游戏,或者点 □");
		r.put("enter the KOF game, or press □", "进入格斗游戏模式,或者点 □");
		r.put("enter the KOF game, or press ", "进入格斗游戏模式,或者点 ");
		r.put("enter the KOF game","进入格斗游戏模式");
		r.put("enter the 3d game, or press L1", "进入3d游戏模式,或者点 L1");
		r.put("enter the 3d cooperative game, or press L1", "进入3d多人合作模式,或者点 L1");
		r.put("enter the 3d cooperative game, or press ", "进入3d多人合作模式,或者点 ");
		r.put("enter the 3d cooperative game","进入3d多人合作模式");
		r.put("enter the rhythm game, or press R1","进入音乐游戏,或者点 R1");
		r.put("game setting","游戏设置");
		r.put("key setting","按键设置");
		r.put("gamepad test","手柄测试");
		r.put("gamepad to keyboard input","手柄模拟键盘");
		r.put("setting","设置");
        r.put("save setting, press □","保存设置,按 □");
		r.put("save success","保存成功");
		r.put("restore default settings","恢复默认配置");



		r.put("edit email:", "编辑email:");
		r.put("send code", "发送验证码");
		r.put("valid code:","输入验证码:");
		r.put("confirm", "确定");
		r.put("cancel", "取消");
		r.put("return, press ○", "返回, 或者点 ○");
		r.put("return, press ", "返回, 或者点 ");
		r.put("join vip, or press □", "加入vip, 或者点 □");
//		r.put("This is a free game developed by BENJAMIN, and VIP will have a better experience in the game. You may pay 1 yuan or more to permanently activate VIP. The developer promises to permanently update and maintain this game. You can scan the QR code above to make payment, I will active your VIP in one day, thanks.",
//				"这是一款由BENJAMIN开发的免费游戏，VIP将在游戏中获得更好的体验。您可以支付1元或更多的费用永久激活VIP。开发商承诺将永久更新和维护这款游戏。你可以扫描上面的二维码付款，我会在一天内激活你的VIP，谢谢。"
//				);
		r.put("This is a free game developed by BENJAMIN and SASHA. VIP will have a better experience in the game. You may pay 1 yuan or more to permanently activate VIP.",
				"这是一款由 BENJAMIN 和 SASHA 开发的免费游戏，VIP将在游戏中获得更好的体验。您可以支付1元或更多的费用永久激活VIP。"
				);
		r.put("The developer promises to permanently update and maintain this game. I will active your VIP atfer you paid in one day, thanks.",
				"开发者承诺将永久更新和维护这款游戏。我会在你支付后的一天内激活你的VIP，谢谢。"
				);
		r.put("Welcome to contact us, email is sasha@sellgirl.com.",
				"欢迎联系我们，电子邮件是 sasha@sellgirl.com"
				);
		r.put("Purchase Way: ", "支付方式: ");
		r.put("Wechat Pay, press □", "微信支付, 或者点 □");
		r.put("Cancel Pay", "取消支付");
		r.put("Paid", "已支付");
		r.put("wrong valid code", "验证码错误");
		r.put("game continue □", "继续游戏 □");
		r.put("game continue ", "继续游戏 ");
		r.put("back to main menu ○", "回到主菜单 ○");
		r.put("back to main menu ", "回到主菜单 ");
		r.put("exit game △", "退出游戏 △");
		r.put("exit game ", "退出游戏 ");
		r.put("please input email first", "请先填写邮箱");
		r.put("backup or upload character data", "备份或上传角色数据");
		r.put("need become vip first", "需要先加入vip");
		r.put("confirm", "确定");
		r.put("close", "关闭");
		r.put("game guide X", "操作说明 X");
		r.put("game guide ", "操作说明 ");

		r.put("auto setting","自动设置");
		r.put("caculating","计算中");

		r.put("Loading...","加载中...");

		//手柄演奏
		r.put("musical note","音符");
		r.put("black key","黑键");
//		r.put("high pitch:{0} low pitch:{1} #:{2} b:{3}","升音阶:{0} 降音阶:{1} 升半音#:{2} 降半音b:{3}");
		r.put("pitch, C1:{2}+{0} C2:{0} C3:{2} C5:{3} C6:{1} C7:{3}+{1}","音阶, C1:{2}+{0} C2:{0} C3:{2} C5:{3} C6:{1} C7:{3}+{1}");
		r.put("gamepad piano mini game","手柄演奏小游戏");
		r.put("backToMenu(or keep press {key} {s} second)","返回菜单(按住 {key} {s} 秒)");
//		r.put("pay description",
//				"这是一款由BENJAMIN开发的免费游戏，VIP将在游戏中获得更好的体验。您可以支付1元或更多的费用永久激活VIP。开发商承诺将永久更新和维护这款游戏。你可以扫描上面的二维码付款，我会在一天内激活你的VIP，谢谢。"
//				);
		return r;
	}
}

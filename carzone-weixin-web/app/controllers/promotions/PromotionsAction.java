package controllers.promotions;

import java.util.Date;

import cn.org.rapid_framework.page.Page;

import models.promotions.service.PromotionsService;
import models.wx.model.TbCzUser;
import models.wx.vo.query.TbCzUserQuery;
import controllers.Application;
import controllers.WxDataService;

/**
 * 促销模块
 * 
 * @author yanq
 * 
 */
public class PromotionsAction extends Application {
	private static int[] render = { 70, 160, 300, 50, 330, 80, 170, 275, 310,
			250 };

	/**
	 * 用户信息
	 * 
	 * @author: yanq
	 * @version: 2014-1-18 下午5:31:24
	 */
	public static void userInfo() {
		TbCzUser tbCzUser = new TbCzUser();
		tbCzUser.setType(3);
		render(tbCzUser);
	}

	/**
	 * 保存抽奖人信息
	 */
	public static void save(TbCzUser tbCzUser) {
		long userId = -1;
		if (tbCzUser == null) {
			return;
		}
		// 判断用户是否存在
		TbCzUserQuery query = new TbCzUserQuery();
		query.setType(3);
		//微信号和手机号码有一个在系统中存在则不能抽奖
		query.setUserName(tbCzUser.getUserName());
		Page<TbCzUser> page = WxDataService.getWxRmiService().findPage(query);
		if (page.getTotalCount() == 0) {
			query.setUserName(null);
			query.setMobile(tbCzUser.getMobile());
			page = WxDataService.getWxRmiService().findPage(query);
			if (page.getTotalCount() == 0) {
				// 保存抽奖人信息
				tbCzUser.setCreateTime(new Date());
				userId = WxDataService.getWxRmiService().saveOrUpdate(tbCzUser);
			}
		} else {
			userId = page.getResult().get(0).getUserId();
		}
		 lottery(userId);
	}

	/**
	 * 抽奖转盘
	 */
	public static void lottery(long userId) {
		Integer angle = -1;
		String text = null;
		if (userId == -1) {
			render(angle, text);
		}
		TbCzUserQuery query = new TbCzUserQuery();
		query.setUserId(userId);
		TbCzUser user = WxDataService.getWxRmiService().findPage(query)
				.getResult().get(0);
		if (user.getPostion() != null) {
			angle = Integer.parseInt(user.getOtherInfo());
			render(angle, text, userId);
		}
		angle = (int) (Math.random() * 360);
		text = PromotionsService.getAwards(angle);
		if (text == null) {
			angle = render[(int) (Math.random() * 10)];
			text = PromotionsService.getAwards(angle);
		}
		user.setPostion(text);
		user.setOtherInfo(String.valueOf(angle));
		WxDataService.getWxRmiService().saveOrUpdate(user);
		render(angle, text, userId);
	}
	
	public static void result(Long userId){
		TbCzUserQuery query = new TbCzUserQuery();
		query.setUserId(userId);
		Page<TbCzUser>page = WxDataService.getWxRmiService().findPage(query);
		String money =  page.getResult().get(0).getPostion();
		render(money);
	}
}

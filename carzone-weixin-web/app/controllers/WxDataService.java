package controllers;

import java.util.Map;

import models.rmi.UesRmiService;
import models.rmi.WxRmiService;
import models.wx.vo.query.TbCzUserQuery;
import controllers.pojo.LotteryInfo;


public class WxDataService {
	private final static String[] g_awards = {"288","188","88","8","5"};
	private static LotteryInfo lotteryInfo = null;
	public static UesRmiService getUesUserService(){
		return (UesRmiService)play.modules.spring.Spring.getBean("uesRmiService");
	}

	public static WxRmiService getWxRmiService() {
		return (WxRmiService) play.modules.spring.Spring.getBean("wxRmiService");
	}
	
	public static LotteryInfo getLotteryInfo(){
		if (lotteryInfo == null) {
			lotteryInfo =  (LotteryInfo)play.modules.spring.Spring.getBean("lotteryInfo");
			TbCzUserQuery query = new TbCzUserQuery();
			query.setType(3);
			Map<Integer, Integer> map = lotteryInfo.getAwards();
			Integer count = null;
			for (Integer key : map.keySet()) {
				count = map.get(key);
				if (count != -1) {
					query.setPostion(g_awards[key-1]);
					int uesUsers = WxDataService.getWxRmiService().findPage(query).getTotalCount();
					count = count - uesUsers;
					map.put(key, count);
				}
			}
		}
		return lotteryInfo;
	}
}

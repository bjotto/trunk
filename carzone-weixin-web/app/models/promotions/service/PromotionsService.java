package models.promotions.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import common.util.DateUtil;

import controllers.WxDataService;

public class PromotionsService {
	private final static String[] g_awards = { "288", "188", "88", "8", "5" };

	/**
	 * 根据角度判断中奖项目
	 */
	public static String getAwards(int angle) {
		Map<Integer, List<Integer>> allset = WxDataService.getLotteryInfo()
				.getRule();
		Map<Integer, Integer> allAwardsCount = WxDataService.getLotteryInfo()
				.getAwards();
		int current = 0;
		for (Integer key : allset.keySet()) {
			List<Integer> value = allset.get(key);
			Collections.sort(value);
			for (int i = 0; i < value.size(); i = i + 2) {
				if (angle > value.get(i) && angle <= value.get(i + 1)) {
					current = allAwardsCount.get(key);
					if (current > 0) {
						if (isPutAwards(current, key)) {
							current--;
							allAwardsCount.put(key, current);
							return g_awards[key - 1];
						}else {
							return null;
						}
					} else {
						return null;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 出奖规则 2014-1-24 1个88 25 1个88 26 1个88 27 188 28 188 29 288
	 */
	public static boolean isPutAwards(int count, int key) {
		boolean isPut = false;
		int today = DateUtil.getDay();
		switch (key) {
		case 1:
			if ("2014-01-29".contentEquals(DateUtil.toString(new Date(),
					"yyyy-MM-dd"))) {
				isPut = true;
				break;
			}
			if(today>29||today<15){
				isPut = true;
				break;
			}
			break;
		case 2:
			if ("2014-01-27".contentEquals(DateUtil.toString(new Date(),
					"yyyy-MM-dd")) && count > 1) {
				isPut = true;
				break;
			}
			if ("2014-01-28".contentEquals(DateUtil.toString(new Date(),
					"yyyy-MM-dd"))) {
				isPut = true;
				break;
			}
			if (today > 28 || today < 15) {
				isPut = true;
				break;
			}
			break;
		case 3:
			if ("2014-01-24".contentEquals(DateUtil.toString(new Date(),
					"yyyy-MM-dd")) && count > 2) {
				isPut = true;
				break;
			}
			if ("2014-01-25".contentEquals(DateUtil.toString(new Date(),
					"yyyy-MM-dd")) && count > 1) {
				isPut = true;
				break;
			}
			if ("2014-01-26".contentEquals(DateUtil.toString(new Date(),
					"yyyy-MM-dd"))) {
				isPut = true;
				break;
			}
			if (today > 26 || today < 15) {
				isPut = true;
				break;
			}
			break;
		default:
			isPut = true;
			break;
		}
		return isPut;
	}
}

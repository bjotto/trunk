package controllers.pojo;

import java.util.List;
import java.util.Map;

/**
 * @descript: ：奖项设置
 * @author: yanq
 * @version: 2014-1-18 上午9:23:19
 */
public class LotteryInfo {
	private Map<Integer, Integer> awards;//<key, value>key奖项 value数量 0为无数
	private Map<Integer,  List<Integer>>  rule; //key 奖项  value中奖规则(最大角度 和 最小角度)
	
	public Map<Integer, Integer> getAwards() {
		return awards;
	}
	public void setAwards(Map<Integer, Integer> awards) {
		this.awards = awards;
	}
	public Map <Integer,  List<Integer>> getRule() {
		return rule;
	}
	public void setRule( Map <Integer,  List<Integer>> rule) {
		this.rule = rule;

	}

}

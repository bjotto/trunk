/**
 * 
 */
/**
 * @author yanq
 *
 */
package models.news.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.wx.model.TbCarzone315;
import models.wx.model.TbCzAnswer;
import models.wx.model.TbCzCoupon;
import models.wx.model.TbCzQuestion;
import models.wx.model.TbCzQuestionnaire;
import models.wx.model.TbCzResult;
import models.wx.model.TbCzUser;
import models.wx.vo.query.TbCzCouponQuery;
import models.wx.vo.query.TbCzQuestionQuery;
import models.wx.vo.query.TbCzUserQuery;
import net.sf.json.JSONObject;
import cn.org.rapid_framework.page.Page;
import controllers.WxDataService;

public class NewsService {
	// private static String strRandom[] = { "a", "b", "c", "d", "e", "f", "g",
	// "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
	// "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G",
	// "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
	// "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6",
	// "7", "8", "9" };

	private static String strRandom[] = { "0", "1", "2", "3", "4", "5", "6",
			"7", "8", "9" };

	private static Map<String, Integer> preCodeMap = null;
	private static int preLength = 10;
	private static int maxSize = 10000000;

	/**
	 * 
	 * 创建人：颜强 创建时间：2013-11-2 方法描述：生成优惠券编码 参数名称：@param 返回值：@return
	 */
	private static String createPreCode() {
		StringBuffer code = new StringBuffer();
		for (int i = 0; i < preLength; i++) {
			Double dom = Math.random() * strRandom.length;
			code.append(strRandom[dom.intValue()]);
		}
		return code.toString();
	}

	public static String getPreCode() {
		if (preCodeMap == null) {
			preCodeMap = new HashMap<String, Integer>();
			List<TbCzCoupon> allPromos = WxDataService.getWxRmiService()
					.findAllCoupons();
			for (int i = 0; i < allPromos.size(); i++) {
				preCodeMap.put(allPromos.get(i).getCouponCode(), 1);
			}
		}
		String code = createPreCode();
		while (preCodeMap.get(code) != null && maxSize > 0) {
			code = createPreCode();
			maxSize--;
		}
		preCodeMap.put(code, 1);
		return code;
	}

	public static Page<TbCzQuestion> getQuestion(TbCzQuestionQuery query) {
		Page<TbCzQuestion> page = WxDataService.getWxRmiService().findPage(
				query);

		List<TbCzQuestion> list = page.getResult();
		List<String> answers = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getAnswer1() != null)
				answers.add(list.get(i).getAnswer1());
			if (list.get(i).getAnswer2() != null)
				answers.add(list.get(i).getAnswer2());
			if (list.get(i).getAnswer3() != null)
				answers.add(list.get(i).getAnswer3());
			if (list.get(i).getAnswer4() != null)
				answers.add(list.get(i).getAnswer4());
			if (list.get(i).getAnswer5() != null)
				answers.add(list.get(i).getAnswer5());
			if (list.get(i).getAnswer6() != null)
				answers.add(list.get(i).getAnswer6());
			if (list.get(i).getAnswer7() != null)
				answers.add(list.get(i).getAnswer7());
			if (list.get(i).getAnswer8() != null)
				answers.add(list.get(i).getAnswer8());
			if (list.get(i).getOtherAnswer() != null) {
				String[] tmps = list.get(i).getOtherAnswer().split("|");
				for (int j = 0; j < tmps.length; j++) {
					answers.add(tmps[j]);
				}
			}
			list.get(i).setAnswers(answers);
		}
		return page;
	}

	public static Map<String, List<String>> JSON2Map(String jsonMapStr) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		JSONObject jsonMap = JSONObject.fromObject(jsonMapStr);
		Iterator<String> it = jsonMap.keys();

		while (it.hasNext()) {
			String key = it.next();
			map.put(key, (List<String>) jsonMap.get(key));
		}
		return map;
	}

	/**
	 * 
	 * 创建人：颜强 创建时间：2013-12-21 方法描述：保存用户和优惠券信息 参数名称：@param 返回值：@return
	 */
	public static String saveUserAndCoupon(TbCzUser userInfo, String result) {
		Date data = new Date();

		String preCode = null;
		userInfo.setCreateTime(data);
		Long userId = WxDataService.getWxRmiService().saveOrUpdate(userInfo);
		TbCzCoupon tbCzCoupon = new TbCzCoupon();
		tbCzCoupon.setUserId(userId.longValue());
		preCode = getPreCode();
		tbCzCoupon.setCouponCode(preCode);
		tbCzCoupon.setIsValid("1");// 有效
		tbCzCoupon.setIsUser("0");// 没有使用
		tbCzCoupon.setCreatedTime(data);
		WxDataService.getWxRmiService().saveOrUpdate(tbCzCoupon);
		saveQuestionResult(result, userId);
		return preCode;
	}
	
	public static String findPromoCode(TbCzUser userInfo, String result) {
		String promoCode=null;
		TbCzUserQuery query = new TbCzUserQuery();
//		query.setType(userInfo.getType());
		query.setRealName(userInfo.getRealName());
		query.setMobile(userInfo.getMobile());
//		query.setCompany(userInfo.getCompany());
//		query.setOtherInfo(userInfo.getOtherInfo());
		Page<TbCzUser> page =WxDataService.getWxRmiService().findPage(query);
		if (page.getTotalCount() == 0) {
			promoCode = NewsService.saveUserAndCoupon(userInfo, result);
		}else {
			Long userId = page.getResult().get(0).getUserId();
			TbCzCouponQuery query2 = new TbCzCouponQuery();
			query2.setUserId(userId);
			Page<TbCzCoupon>page2 = WxDataService.getWxRmiService().findPage(query2);
			if (page2.getTotalCount() != 0) {
				promoCode = page2.getResult().get(0).getCouponCode();
			}
		}
		return promoCode;
	}

	public static void saveQuestionResult(String result, Long userId) {
		TbCzResult tbCzResult = null;
		Map<String, List<String>> rst = NewsService
				.JSON2Map(result);
		Set key = rst.keySet();
		Date data = new Date();
		for (Iterator iterator = key.iterator(); iterator.hasNext();) {
			tbCzResult = new TbCzResult();
			Object k = iterator.next();
			Long questionId = Long.parseLong(k.toString());
			tbCzResult.setQuestionId(questionId);
			AssignmentAnswers(tbCzResult, rst, k);
			tbCzResult.setCreatedTime(data);
			tbCzResult.setUserId(userId);
			WxDataService.getWxRmiService().saveOrUpdate(tbCzResult);
			tbCzResult = null;
		}
	}

	private static void AssignmentAnswers(TbCzResult tbCzResult,
			Map<String, List<String>> rst, Object k) {
		List<String> answers = rst.get(k);
		for (String answer : answers) {
			if (answer.length() != 1) {
				tbCzResult.setSuggestion(answer);
				continue;
			}
			if (answer.contains("1")) {
				tbCzResult.setAnswer1("1");
				continue;
			}
			if (answer.contains("2")) {
				tbCzResult.setAnswer2("1");
				continue;
			}
			if (answer.contains("3")) {
				tbCzResult.setAnswer3("1");
				continue;
			}
			if (answer.contains("4")) {
				tbCzResult.setAnswer4("1");
				continue;
			}
			if (answer.contains("5")) {
				tbCzResult.setAnswer5("1");
				continue;
			}
			if (answer.contains("6")) {
				tbCzResult.setAnswer6("1");
				continue;
			}
			if (answer.contains("7")) {
				tbCzResult.setAnswer7("1");
				continue;
			}
			if (answer.contains("8")) {
				tbCzResult.setAnswer8("1");
				continue;
			}
		}
	}
	
	public static Map<TbCzQuestionnaire, List<TbCzAnswer>> getQuestionnaire(Page page){
		Map<TbCzQuestionnaire, List<TbCzAnswer>> questionsMap = new LinkedHashMap<TbCzQuestionnaire, List<TbCzAnswer>>();
		List questionAns = page.getResult();
		TbCzQuestionnaire qt = null;
		List<TbCzAnswer> answers = null;
		TbCzAnswer answer = null;
		for (int i = 0; i < questionAns.size(); i++) {
			Map<String, Object> rst = (Map<String, Object>) questionAns.get(i);
			Long questionId = Long.parseLong(rst.get("question_id").toString());
			answers = hasAnswer(questionsMap, questionId);
			if (answers == null) {
				answers = new ArrayList<TbCzAnswer>();
				qt = new TbCzQuestionnaire();
				qt.setQuestionId(questionId);
				qt.setClassify(Integer.parseInt(rst.get("classify").toString()));
				qt.setType(Integer.parseInt(rst.get("type").toString()));
				qt.setQuestion(rst.get("question").toString());
				answer = new TbCzAnswer();
				answer.setQuestionId(questionId);
				answer.setAnswer(rst.get("answer").toString());
				answer.setAnswerId(Long.parseLong(rst.get("answer_id").toString()));
				answers.add(answer);
				questionsMap.put(qt, answers);
			}else {
				answer = new TbCzAnswer();
				answer.setQuestionId(questionId);
				answer.setAnswer(rst.get("answer").toString());
				answer.setAnswerId(Long.parseLong(rst.get("answer_id").toString()));
				answers.add(answer);
//				questionsMap.put(qt, answers);
			}
		}
		return questionsMap;
	}

	private static List<TbCzAnswer> hasAnswer(
			Map<TbCzQuestionnaire, List<TbCzAnswer>> questionsMap,
			Long questionId) {
		List<TbCzAnswer> answers = null;
		Set<TbCzQuestionnaire>key = questionsMap.keySet();
		for (Iterator<TbCzQuestionnaire> it = key.iterator(); it.hasNext();) {
			TbCzQuestionnaire tmp = it.next();
			if (tmp.getQuestionId() == questionId) {
				answers = questionsMap.get(tmp);
				break;
			}
		}
		return answers;
	}
	
	public static void saveOrUpdate(TbCarzone315 tbCarzone315){
		if(tbCarzone315!=null)
		tbCarzone315.setCreatedTime(new Date());
		WxDataService.getWxRmiService().saveOrUpdate(tbCarzone315);
	}
	
}
package models.question.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.org.rapid_framework.page.Page;

import models.news.service.NewsService;
import models.wx.model.TbCzAnswer;
import models.wx.model.TbCzQuestionnaire;
import models.wx.model.TbCzQuestionnaireResult;
import models.wx.model.TbCzUser;
import models.wx.vo.query.TbCzAnswerQuery;
import models.wx.vo.query.TbCzQuestionnaireQuery;


import controllers.WxDataService;
import controllers.util.Commons;

/**
 * 调查问卷
 * 
 * @author yanq
 * 
 */
public class QuestionService {

	/**
	 * 查询题目
	 * */
	public static Page<TbCzQuestionnaire> findQuestionPage(TbCzQuestionnaireQuery query) {
		Page<TbCzQuestionnaire> page = null;
		if (query == null) {
			query = new TbCzQuestionnaireQuery();
		}
		page = WxDataService.getWxRmiService().findPage(query);
		List<TbCzQuestionnaire> list = page.getResult();
		if (list != null && list.size() != 0) {
			TbCzAnswerQuery asquery = new TbCzAnswerQuery();
			TbCzQuestionnaire question = null;
			for (int i = 0; i < list.size(); i++) {
				question = list.get(i);
				switch (question.getType()) {
				case Commons.radio:
				case Commons.mulchoice:
					asquery.setQuestionId(question.getQuestionId());
					List<TbCzAnswer> answers = WxDataService.getWxRmiService()
							.findAnswerByQid(asquery);
					question.setAnswerList(answers);
					break;
				default:
					break;
				}
			}
			asquery = null;
		}
		return page;
	}

	/**
	 * 保存信息
	 */
	public static Long saveOrUpdateUser(TbCzUser tbCzUser){
		tbCzUser.setCreateTime(new Date());
		return WxDataService.getWxRmiService().saveOrUpdate(tbCzUser);
	}
	public static void saveQuestionnaireResult(Long userId, String result){
		TbCzQuestionnaireResult tbCzResult = null;
		Map<String, List<String>> rst = NewsService
				.JSON2Map(result);
		Set key = rst.keySet();
		Date data = new Date();
		for (Iterator iterator = key.iterator(); iterator.hasNext();) {
			Object k = iterator.next();
			Long questionId = Long.parseLong(k.toString());
			List<String> answers = rst.get(k);
			tbCzResult = new TbCzQuestionnaireResult();
			tbCzResult.setQuestionId(questionId);
			tbCzResult.setCreatedTime(data);
			tbCzResult.setUserId(userId);
			for (int i = 1; i < answers.size(); i++) {
				if (answers.get(0).equals("3") ||
						answers.get(0).equals("4")) {
					tbCzResult.setSuggestion(answers.get(i));
				}else {
					tbCzResult.setAnswerId(Long.parseLong(answers.get(i)));
				}
				WxDataService.getWxRmiService().saveOrUpdate(tbCzResult);
			}
		}
	}	
}

package controllers.news;

import java.util.List;

import models.news.service.NewsService;
import models.wx.model.TbCarzone315;
import models.wx.model.TbCzAnswer;
import models.wx.model.TbCzCoupon;
import models.wx.model.TbCzQuestion;
import models.wx.model.TbCzQuestionnaire;
import models.wx.model.TbCzUser;
import models.wx.vo.query.TbCzAnswerQuery;
import models.wx.vo.query.TbCzCouponQuery;
import models.wx.vo.query.TbCzQuestionQuery;
import models.wx.vo.query.TbCzQuestionnaireQuery;
import models.wx.vo.query.TbCzUserQuery;
import cn.org.rapid_framework.page.Page;
import controllers.Application;
import controllers.WxDataService;

public class NewsAction extends Application {
	/**
	 * 调查问卷
	 */
	public static void allquestion() {
		render();
	}

	public static void questions(Integer type) {
		TbCzUser userInfo = new TbCzUser();
		Integer pageNumber = 1;
		if (type == null) {
			type = 100;
		}
		userInfo.setType(type);
		render(pageNumber, userInfo);
	}

	public static void summary(TbCarzone315 carzone){
		if(carzone != null){
			carzone = new TbCarzone315();
		}
		render(carzone);
	}
	
	public static void saveCarzone315(TbCarzone315 carzone){
		if(carzone != null){
			NewsService.saveOrUpdate(carzone);
		}
		render();
	}
	
	public static void questionList(Integer pageNumber, Integer classify) {
		TbCzQuestionQuery query = new TbCzQuestionQuery();
		if (pageNumber == null) {
			pageNumber = 1;
		}
		query.setPageNumber(pageNumber);
		if (classify != null) {
			query.setClassify(classify);
		}
		query.setPageSize(1);
		Page<TbCzQuestion> page = NewsService.getQuestion(query);
		int pageTotal = page.getTotalCount();
		render(page, pageTotal, pageNumber);
	}

	/**
	 * 方法描述：保存优惠信息 参数名称：@param 返回值：@return
	 */
	public static void saveResult(TbCzUser userInfo, String result) {
		String promoCode = null;
		if (userInfo != null) {
			userInfo.setUserLog(request.remoteAddress);
			promoCode = NewsService.findPromoCode(userInfo, result);
			render(promoCode);
		}
	}
	/**
	 * 优惠码查询
	 */
	public static void searchCoupon(){
		render();
	}
	/**
	 * 优惠码查询结果
	 */
	public static void searchResult(TbCzUser userInfo) {
		if (userInfo != null) {
			String promoCode = null;
			TbCzUserQuery query = new TbCzUserQuery();
			query.setMobile(userInfo.getMobile());
			query.setUserName(userInfo.getUserName());
			Page<TbCzUser> page = WxDataService.getWxRmiService().findPage(
					query);
			List<TbCzUser> list = null;
			if (page.getTotalCount() == 0) {
				render(promoCode);
			} else {
				list = page.getResult();
				TbCzUser czUser = null;
				Long userId = null;
				for (int i = 0; i < list.size(); i++) {
					czUser = list.get(i);
					if ((czUser.getOtherInfo() != null&&czUser.getOtherInfo().contentEquals(
							userInfo.getOtherInfo()))
							|| (czUser.getCompany() != null && czUser
									.getCompany().contentEquals(
											userInfo.getOtherInfo()))) {
						userId = czUser.getUserId();
						break;
					}
				}
				if (userId != null) {
					TbCzCouponQuery couponQuery = new TbCzCouponQuery();
					couponQuery.setUserId(userId);
					Page<TbCzCoupon> page2 = WxDataService.getWxRmiService()
							.findPage(couponQuery);
					if (page2.getTotalCount() != 0) {
						promoCode = page2.getResult().get(0).getCouponCode();
					}
				}
				render(promoCode);
			}
		}
	}

	/**
	 * 
	 * 创建人：颜强 创建时间：2013-12-11 方法描述：康众动态 参数名称：@param 返回值：@return
	 */
	public static void carzoneDevelop() {
		render();
	}

	public static void questionEnd() {
		render();
	}

	/**
	 * 新
	 */
	public static void newQuestionnair(int type) {
		TbCzUser userInfo = new TbCzUser();
		Integer pageNumber = 1;
		// if (type == null) {
		// type = 100;
		// }
		type = 1;
		userInfo.setType(type);
		render(pageNumber, userInfo);
	}

	public static void questionnairPage(Integer pageNumber, int classify) {
		Page<TbCzQuestionnaire> qtpage = null;
		Page<TbCzAnswer> anpage = null;
		if (pageNumber == null) {
			pageNumber = 1;
		}
		classify = 3;
		TbCzQuestionnaireQuery query = new TbCzQuestionnaireQuery();
		query.setClassify(classify);
		query.setPageNumber(pageNumber);
		query.setPageSize(1);
		query.setSortColumns("qt.question_id asc");
		qtpage = WxDataService.getWxRmiService().findPage(query);
		int pageTotal = qtpage.getTotalCount();
		if (pageTotal != 0) {
			TbCzAnswerQuery anQuery = new TbCzAnswerQuery();
			anQuery.setQuestionId(qtpage.getResult().get(0).getQuestionId());
			anpage = WxDataService.getWxRmiService().findPage(anQuery);
		}

		// Page qa = WxDataService.getWxRmiService().findAnswerPage(query);

		// Map<TbCzQuestionnaire, List<TbCzAnswer>> map =
		// NewsService.getQuestionnaire(qa);
		render(qtpage, anpage, pageTotal, pageNumber);
	}
	
	/**
	 * 
	 * 创建人：颜强
	 * 创建时间：2013-12-31
	 * 方法描述：抽奖
	 * 参数名称：@param
	 * 返回值：@return
	 */
	public static void lottery(){
		render();
	}
}

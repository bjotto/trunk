package controllers.question;

import models.question.service.QuestionService;
import models.wx.model.TbCzQuestionnaire;
import models.wx.model.TbCzUser;
import models.wx.vo.query.TbCzQuestionnaireQuery;
import models.wx.vo.query.TbCzUserQuery;
import cn.org.rapid_framework.page.Page;
import controllers.Application;
import controllers.WxDataService;

/**
 * 调查问卷
 * 
 * @author yanq
 * 
 */
public class QuestionAction extends Application {

	/**
	 * 调查问卷 classify 1.单车客户调查问卷 2.企业客户调查问卷 3.培训需求调查问卷 4员工生活期望
	 */
	public static void questionnaires(Integer classify) {
		if (classify != null) {
			TbCzUser tbCzUser = new TbCzUser();
			tbCzUser.setType(classify);
			render(tbCzUser);
		}
	}

	/**
	 * 问卷题目
	 */
	public static void question(Integer pageNumber, Integer classify) {
		TbCzQuestionnaireQuery query = new TbCzQuestionnaireQuery();
		if (pageNumber == null) {
			pageNumber = 1;
		}
		if (classify == null) {
			classify = -1;
		}
		query.setPageSize(1);
		query.setPageNumber(pageNumber);
		query.setClassify(classify);
		query.setSortColumns("question_id asc");
		Page<TbCzQuestionnaire> page = QuestionService.findQuestionPage(query);
		int pageTotal = page.getLastPageNumber();
		int pageSize = page.getResult().size();
		int size = page.getPageSize();
		render(pageNumber, pageTotal, page, pageSize, size);
	}

	/**
	 * 方法描述：保存信息 参数名称：@param 返回值：@return
	 */
	public static void saveResult(TbCzUser tbCzUser, String result) {
		if (tbCzUser != null) {
			TbCzUserQuery query = new TbCzUserQuery();
			query.setRealName(tbCzUser.getRealName());
			query.setMobile(tbCzUser.getMobile());
			Page<TbCzUser> page =WxDataService.getWxRmiService().findPage(query);
			Long userId = null;
			if (page.getTotalCount() == 0) {
				userId = QuestionService.saveOrUpdateUser(tbCzUser);
				QuestionService.saveQuestionnaireResult(userId, result);
			}
			render();
		}
		questionnaires(2);
	}
	
	
	/**
	 * 用户反馈
	 */
	public static void enterName(TbCzUser user){
		if (user == null) {
			user = new TbCzUser();
		}
		render(user);
	}
	
	public static void saveUser(TbCzUser user){
		if (user != null) {
			if(user.getOtherInfo() !=null && user.getOtherInfo().length() == 2){
				user.setOtherInfo(null);
			}
			if (QuestionService.saveOrUpdateUser(user) ==null) {
				enterName(user);
			}else {
				success();
			}
		}
	
		
	}
	
	public static void success(){
		render();
	}
}

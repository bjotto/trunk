package controllers.qxxt;

import models.question.service.QuestionService;
import models.wx.model.TbCzUser;
import controllers.Application;

public class QxxtAction extends Application{

	/**
	 * 汽修学堂报名
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

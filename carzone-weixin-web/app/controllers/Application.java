package controllers;

import controllers.util.CarzoneUtil;
import controllers.util.WeixinUtil;
import models.branch.service.BranchService;
import play.mvc.Controller;

public class Application extends Controller {

	public static void index() {
		BranchService.setProvinces();
		render();
	}

}
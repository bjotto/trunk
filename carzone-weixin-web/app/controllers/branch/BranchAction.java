package controllers.branch;

import java.util.List;
import java.util.Map;
import java.util.Set;
import models.branch.service.BranchService;
import models.wx.model.TbCzBranch;
import models.wx.vo.query.TbCzBranchQuery;
import controllers.Application;
import controllers.WxDataService;

public class BranchAction extends Application{
	
	public static void carzoneProfile(){
		render();
	}
	
	/**
	 * 
	 * 创建人：颜强
	 * 创建时间：2013-12-5
	 * 方法描述：查询门店所属省份信息
	 * 参数名称：@param
	 * 返回值：@return
	 */
	public static void listProvinces(){
		Set<Map<String, Object>> provinces = BranchService.getProvinces();
		render(provinces);
	}
	
	public static void getProvinces(){
		Map<Map<String, Object>, Map<String, List<TbCzBranch>>> provinceMaps = BranchService.branchs();
		render( provinceMaps);
	}
	
	public static void getCitys(String province){
		TbCzBranchQuery query = new TbCzBranchQuery();
		query.setProvince(province);
		query.setSortColumns("id");
		List<Map> citys = WxDataService.getWxRmiService().findCitysByProvince(query);
		render(citys);
	}
	
	public static void getBranchs(String city){
		TbCzBranchQuery query = new TbCzBranchQuery();
		query.setCity(city);
		query.setPageSize(50);
		query.setSortColumns("id");
		List<TbCzBranch>branchs = WxDataService.getWxRmiService().findTbCzBranch(query).getResult();
		render(branchs);
	}
}

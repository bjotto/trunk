/**
 * @author yanq
 *
 */
package models.branch.service;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.org.rapid_framework.page.Page;

import models.wx.model.TbCzBranch;
import models.wx.vo.query.TbCzBranchQuery;


import play.cache.Cache;
import controllers.WxDataService;

public class BranchService {
	/**
	 * 
	 * 创建人：颜强 创建时间：2013-12-6 方法描述：设置省级行政区 参数名称：@param 返回值：@return
	 */
	public static void setProvinces() {
		TbCzBranchQuery query = new TbCzBranchQuery();
		query.setSortColumns("id");
		Set<Map<String, Object>> provinces = new LinkedHashSet<Map<String, Object>>(WxDataService
				.getWxRmiService().findProvinces(query));
		if (provinces.size() != 0) {
			play.cache.Cache.set("all_provinces", provinces, "1h");
		}
	}

	/**
	 * 
	 * 创建人：颜强 创建时间：2013-12-6 方法描述：获取省级行政区 参数名称：@param 返回值：@return
	 */
	public static Set<Map<String, Object>> getProvinces() {
		Set<Map<String, Object>> provinces = Cache.get("all_provinces", Set.class);
		if (provinces == null) {
			TbCzBranchQuery query = new TbCzBranchQuery();
			query.setSortColumns("id");
			provinces = new LinkedHashSet<Map<String, Object>>(WxDataService
					.getWxRmiService().findProvinces(query));
		}
		return provinces;
	}

	public static Set<String> getCitys(String province) {
		Set<String> citys = Cache.get("province:" + province, Set.class);
		if (citys == null) {
			citys = new LinkedHashSet<String>();
			TbCzBranchQuery query = new TbCzBranchQuery();
			query.setProvince(province);
			query.setPageSize(100);
			query.setSortColumns("id");
			Page<TbCzBranch> page = WxDataService.getWxRmiService()
					.findTbCzBranch(query);
			List<TbCzBranch> list = page.getResult();
			TbCzBranch branch = null;
			for (int i=0; i<list.size(); i++) {
				branch = list.get(i);
				citys.add(branch.getCity());
			}
			if (citys.size() != 0) {
				Cache.set("province:" + province, citys, "1h");
			}
		}
		return citys;
	}

	public static Page<TbCzBranch> getBranchs(String city) {
		Page<TbCzBranch> page = null;
		page = Cache.get("city:" + city, Page.class);
		if (page == null) {
			TbCzBranchQuery query = new TbCzBranchQuery();
			query.setCity(city);
			query.setPageNumber(20);
			query.setSortColumns("id");
			page = WxDataService.getWxRmiService().findTbCzBranch(query);
			Cache.set("city:" + city, page, "1h");
		}
		return page;
	}
	
	public static Map<String, List<TbCzBranch>> getCityMaps(String province){
		Map<String, List<TbCzBranch>> treeMap = new LinkedHashMap<String, List<TbCzBranch>>();
		Iterator<String> citys = getCitys(province).iterator();
		String city = null;
		while(citys.hasNext()){
			city = citys.next();
			List<TbCzBranch> branchs = getBranchs(city).getResult();
			treeMap.put(city, branchs);
		}
		return treeMap;
	}
	
	public static Map<Map<String,Object>, Map<String, List<TbCzBranch>>> branchs() {
		Set<Map<String, Object>> provinces  = BranchService.getProvinces();
		Map<Map<String,Object>, Map<String, List<TbCzBranch>>> provinceMaps = new LinkedHashMap<Map<String,Object>, Map<String,List<TbCzBranch>>>();
		for(Map<String, Object> province:provinces){
			provinceMaps.put(province, BranchService.getCityMaps(province.get("province").toString()));
		}
		return provinceMaps;
	}
}
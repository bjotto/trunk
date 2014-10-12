package controllers.product;

import controllers.Application;

/**
 * 产品中心
 * @author yanq
 *
 */
public class ProductAction extends Application{
	
	/**
	 * 
	 * 创建人：颜强
	 * 创建时间：2013-12-7
	 * 方法描述：新品上市
	 * 参数名称：@param
	 * 返回值：@return
	 */
	public static void newproduct(){
		render();
	}
	
	/**
	 * 促销信息
	 */
	public static void promotions(){
		render();
	}
	
	/**
	 * 产品
	 */
	public static void carzoneProducts(){
		render();
	}
	
	/**
	 * 品牌
	 */
	public static void carzoneBrands(){
		render();
	}
}
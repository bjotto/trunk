


import controllers.menu.Button;
import controllers.menu.CommonButton;
import controllers.menu.ComplexButton;
import controllers.menu.Menu;
import controllers.util.WeixinMenuUtil;

public class MenuTest {

	public static void main(String[] args) {
		Menu menu = getMenu();
		int result = WeixinMenuUtil.createMenu(menu, "UsyRFDuLQRaKj2W81XFXClHSd99fIFWO7prTpF8PsepSLSbYc9GX72R2ycoZSY8DqbOj9VsV7a085RKB0RS-Q0xGgHgRnWYQYHrK3XubI5oSGbv-CXfQSjZl9MH4YkE0p1WV3tH10YvkNJDTIXdpRw");
		
		if (0 == result)
			System.out.println("创建成功");
		else
			System.out.println("创建失败" + result);
	}

	/**
	 *获取菜单列表
	 * 
	 * @return
	 */
	private static Menu getMenu() {

		CommonButton btn11 = new CommonButton();
		btn11.setName("门店介绍");
		btn11.setType("click");
		btn11.setKey("11");
		
		CommonButton btn12 = new CommonButton();
		btn12.setName("品牌推荐");
		btn12.setType("click");
		btn12.setKey("12");

		CommonButton btn13 = new CommonButton();
		btn13.setName("专家团队");
		btn13.setType("click");
		btn13.setKey("13");
		
		CommonButton btn21 = new CommonButton();
		btn21.setName("新品发布");
		btn21.setType("click");
		btn21.setKey("21");
		
		CommonButton btn22 = new CommonButton();
		btn22.setName("维修与安装");
		btn22.setType("click");
		btn22.setKey("22");
		
		CommonButton btn23 = new CommonButton();
		btn23.setName("康众商城");
		btn23.setType("click");
		btn23.setKey("23");
		
		
		CommonButton btn31 = new CommonButton();
		btn31.setName("优惠促销");
		btn31.setType("click");
		btn31.setKey("30");
		
		CommonButton btn32 = new CommonButton();
		btn32.setName("优惠促销");
		btn32.setType("click");
		btn32.setKey("30");
		
		CommonButton btn33 = new CommonButton();
		btn33.setName("优惠促销");
		btn33.setType("click");
		btn33.setKey("30");
		
		ComplexButton mainBtn1 = new ComplexButton();
		mainBtn1.setName("服务中心");
		mainBtn1.setSub_button(new CommonButton[] { btn11, btn12, btn13});
		
		ComplexButton mainBtn2 = new ComplexButton();
		mainBtn2.setName("产品与应用");
		mainBtn2.setSub_button(new CommonButton[] { btn21, btn22, btn23});

		ComplexButton mainBtn3 = new ComplexButton();
		mainBtn3.setName("会员中心");
		mainBtn3.setSub_button(new CommonButton[] { btn31, btn32, btn33});
		
		Menu menu = new Menu();
		menu.setButton(new Button[] { mainBtn1, mainBtn2, mainBtn3 });
        
		return menu;
	}

}

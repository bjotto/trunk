import java.util.Date;

import models.promotions.service.PromotionsService;

import org.junit.Test;

import play.mvc.Http.Response;
import play.test.FunctionalTest;

import common.util.DateUtil;

public class ApplicationTest extends FunctionalTest {

    @Test
    public void testThatIndexPageWorks() {
        Response response = GET("/");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(play.Play.defaultWebEncoding, response);
        
    }
    @Test
    public void testgetLotteryInfo(){
    	int angle = (int)(Math.random()*360);
		String text = PromotionsService.getAwards(angle);
		System.out.println(angle);
		System.out.println(text);
	}
	@Test
	public void testAfterFiveDay(){
		System.out.println(DateUtil.getDay());
	}
}
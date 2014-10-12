import controllers.pojo.AccessToken;
import controllers.util.WeixinMenuUtil;
import controllers.util.WeixinUtil;


public class TokenTest {
	public static void main(String[] args) {
		AccessToken accessToken = WeixinMenuUtil.getAccessToken("wxede04094b86c47bf", "4f615da0cc3b27c87203629063ffe14b");
		System.out.println(accessToken.getToken());
	}
}

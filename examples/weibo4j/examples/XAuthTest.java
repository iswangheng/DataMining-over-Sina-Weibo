/**
 *
 */
package weibo4j.examples;

import weibo4j.Weibo;
import weibo4j.WeiboException;
import weibo4j.http.AccessToken;

/**
 * @author 
 *
 */
public class XAuthTest {

	/**
	 * 注意调用XAuth需要申请权限，申请可参考http://open.weibo.com/wiki/index.php/XAuth
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
		Weibo weibo = new Weibo();
		try {
			String userId = "";
			String passWord = "";
			AccessToken accessToken = weibo.getXAuthAccessToken(userId, passWord, "client_auth");
			System.out.println("Got access token.");
			System.out.println("Access token: "+ accessToken.getToken());
			System.out.println("Access token secret: "+ accessToken.getTokenSecret());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

}

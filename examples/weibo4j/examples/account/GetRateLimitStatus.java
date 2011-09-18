/**
 * 
 */
package weibo4j.examples.account;

import weibo4j.RateLimitStatus;
import weibo4j.Weibo;
import weibo4j.WeiboException;

/**
 * @author sina
 *
 */
public class GetRateLimitStatus {

	/**
	 * 获取当前用户API访问频率限制
	 * @param args
	 * args[0]和args[1]为通过GetToken.java获取到的accesstoken和accesstoken secret
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
		try {
			Weibo weibo = new Weibo();
			weibo.setToken(args[0], args[1]);
			RateLimitStatus limitStatus = weibo.rateLimitStatus();
			System.out.println(limitStatus.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
}
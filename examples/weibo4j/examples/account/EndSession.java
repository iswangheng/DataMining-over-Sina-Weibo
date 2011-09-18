/**
 * 
 */
package weibo4j.examples.account;

import weibo4j.User;
import weibo4j.Weibo;

/**
 * @author sina
 *
 */
public class EndSession {

	/**
	 * 清除已验证用户的session
	 * @param args
	 * args[0]和args[1]为通过GetToken.java获取到的accesstoken和accesstoken secret
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
        try {
        	Weibo weibo = new Weibo();
        	weibo.setToken(args[0],args[1]);
        	User user = weibo.endSession();
        	System.out.println(user.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

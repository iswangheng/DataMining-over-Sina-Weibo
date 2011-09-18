/**
 * 
 */
package weibo4j.examples.favorites;

import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.WeiboException;

/**
 * @author sina
 *
 */
public class CreateFavorite {

	/**
	 * 添加收藏
	 * @param args
	 * args[0]和args[1]为通过GetToken.java获取到的accesstoken和accesstoken secret
	 * args[2]为将要收藏的statusId
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
		try {
			Weibo weibo = new Weibo();
			weibo.setToken(args[0],args[1]);
			Status status = weibo.createFavorite(Long.parseLong(args[2]));
			System.out.println(status.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
}

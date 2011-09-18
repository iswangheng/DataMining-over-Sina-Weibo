/**
 * 
 */
package weibo4j.examples.favorites;

import java.util.List;

import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.WeiboException;

/**
 * @author sina
 *
 */
public class GetFavorites {

	/**
	 * 获取当前用户的收藏列表 
	 * @param args
	 * args[0]和args[1]为通过GetToken.java获取到的accesstoken和accesstoken secret
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
		try {
			Weibo weibo = new Weibo();
			weibo.setToken(args[0],args[1]);
			int page=1;
			//page做翻页处理
			List<Status> list = weibo.getFavorites(page);
			for(Status status : list) {
				System.out.println(status.toString());
			}
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
}

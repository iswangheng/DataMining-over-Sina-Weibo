package weibo4j.examples.friendships;

import weibo4j.Weibo;
import weibo4j.WeiboException;

/**
 * @author sina
 *
 */
public class ExistsFriendship {

	/**
	 * 是否关注某用户
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
		try {
			//args[2]:自己的id；args[3]:关注对象的id
			Weibo weibo = new Weibo();
			weibo.setToken(args[0],args[1]);
			boolean bool = weibo.existsFriendship(args[2],args[3]);//args[2]:关注用户的id
			System.out.println(bool);
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
}

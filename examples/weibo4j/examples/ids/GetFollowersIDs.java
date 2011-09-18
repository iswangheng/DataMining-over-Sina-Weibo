/**
 * 
 */
package weibo4j.examples.ids;

import weibo4j.Weibo;
import weibo4j.WeiboException;

/**
 * @author sina
 *
 */
public class GetFollowersIDs {

	/**
	 * 获取用户粉丝对象uid列表 
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
		try {
			Weibo weibo = new Weibo();
			weibo.setToken(args[0],args[1]);
			//cursor处理翻页
			int cursor=5;
			//args[2]:关注用户的id
			long[] ids = weibo.getFollowersIDSByUserId(args[2], cursor).getIDs();
			for(long id : ids) {
				System.out.println(id);
			}
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
}

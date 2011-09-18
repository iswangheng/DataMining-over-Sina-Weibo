
package weibo4j.examples.ids;

import weibo4j.Weibo;
import weibo4j.WeiboException;


public class GetFriendsIDs {

	/**
	 * 获取用户关注对象uid列表 
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
		try {
			Weibo weibo = new Weibo();
			weibo.setToken(args[0],args[1]);
			//该接口翻页处理采用cursor参数
			int cursor=5;
            //args[2]:关注用户的id
			long[] ids = weibo.getFriendsIDSByUserId(args[2], cursor).getIDs();
			for(long id : ids) {
				System.out.println(id);
			}
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
}

/**
 * 
 */
package weibo4j.examples.timeline;

import weibo4j.Weibo;

/**
 * @author sina
 *
 */
public class GetUnread {

	/**
	 * 获取当前用户Web未读消息数，包括@我的, 新评论，新私信，新粉丝数。
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
        try {
        	Weibo weibo = new Weibo();
			weibo.setToken(args[0],args[1]);
        	System.out.println(weibo.getUnread().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

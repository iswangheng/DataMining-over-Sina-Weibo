/**
 * 
 */
package weibo4j.examples.friendships;

import weibo4j.Weibo;
import weibo4j.org.json.JSONObject;

/**
 * @author sina
 *
 */
public class ShowFriendships {

	/**
	 * 返回两个用户关系的详细情况 
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
		try {
			//自己与该关注对象的关系
			Weibo weibo = new Weibo();
			weibo.setToken(args[0],args[1]);
			JSONObject object = weibo.showFriendships(args[2]);//args[2]:关注用户的id
			JSONObject source = object.getJSONObject("source");
			JSONObject target = object.getJSONObject("target");
			System.out.println(source.getString("screen_name")+"与"+target.getString("screen_name")+"互为关注");
			//两个用户关系的详细情况 
			object =  weibo.showFriendships(args[3],args[4]);
			source = object.getJSONObject("source");
			target = object.getJSONObject("target");
			System.out.println(source.getString("screen_name")+"与"+target.getString("screen_name")+"互为关注");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

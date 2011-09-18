/**
 * 
 */
package weibo4j.examples.statuses;

import weibo4j.Status;
import weibo4j.Weibo;

/**
 * @author sina
 *
 */
public class Repost {

	/**
	 * 转发一条微博信息
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
        try {
        	Weibo weibo = new Weibo();
			weibo.setToken(args[0],args[1]);
        	String sid = "15667252412";
        	Thread.sleep(1000);
        	//args[2]：添加转发的信息
        	Status status = weibo.repost(sid, "热爱，你的热爱！");
        	System.out.println(status.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

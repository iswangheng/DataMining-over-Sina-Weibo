/**
 *
 */
package weibo4j.examples.timeline;

import java.util.List;

import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import weibo4j.http.AccessToken;
import weibo4j.http.RequestToken;

/**
 * @author sina
 *
 */
public class GetPublicTimeline {

	/**
	 * 获取最新更新的公共微博消息
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
    	try {
			//获取前20条最新更新的公共微博消息
    		Weibo weibo = new Weibo();
			weibo.setToken(args[0],args[1]);
			 List<Status> statuses =weibo.getPublicTimeline();
			for (Status status : statuses) {
	            System.out.println(status.getUser().getName() + ":" +
	                               status.getText() + ":" +
	                               status.getCreatedAt());
	        }
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
}

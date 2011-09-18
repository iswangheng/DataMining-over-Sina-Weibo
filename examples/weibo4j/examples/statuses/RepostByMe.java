/**
 * 
 */
package weibo4j.examples.statuses;

import java.util.List;

import weibo4j.Status;
import weibo4j.Weibo;

/**
 * @author haidong
 *
 */
public class RepostByMe {

	/**
	 * 获取用户最新转发的n条微博消息
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
        try {
        	Weibo weibo = new Weibo();
			weibo.setToken(args[0],args[1]);
        	List <Status> list =  weibo.getrepostbyme(args[2]);
        	for(Status status:list){
        	System.out.println(status.toString());
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
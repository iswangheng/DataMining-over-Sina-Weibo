/**
 * 
 */
package weibo4j.examples.statuses;

import java.util.List;
import weibo4j.Status;
import weibo4j.Weibo;


/**
 * @author sina
 *
 */
public class ForwardStatus {

	/**
	 * 根据微博ID和用户ID跳转到单条微博页面
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
    	try {
    		Weibo weibo = new Weibo();
			weibo.setToken(args[0],args[1]);
			List<Status> list = weibo.getUserTimeline();
			if(list.size() > 0) {
				//args[2]:用户的id
				String url = "http://api.t.sina.com.cn/"+args[2]+"/statuses/"+list.get(0).getId();
				//打开单条微博信息页面
				Runtime.getRuntime().exec("cmd /c start "+url);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

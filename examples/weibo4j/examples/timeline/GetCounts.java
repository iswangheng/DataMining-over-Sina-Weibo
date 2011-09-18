/**
 *
 */
package weibo4j.examples.timeline;

import java.util.List;

import weibo4j.Count;
import weibo4j.Status;
import weibo4j.Weibo;

/**
 * @author sina
 *
 */
public class GetCounts {

	/**
	 * 批量统计微博的评论数，转发数
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
        try {
        	Weibo weibo = new Weibo();
			weibo.setToken(args[0],args[1]);
        	List<Status> statuses = weibo.getUserTimeline();
        	StringBuilder ids = new StringBuilder();
        	for(Status status : statuses) {
        		ids.append(status.getId()).append(',');
        	}
        	ids.deleteCharAt(ids.length() - 1);
        	List<Count> counts = weibo.getCounts(ids.toString());
    		for(Count count : counts) {
    			System.out.println(count.toString());
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

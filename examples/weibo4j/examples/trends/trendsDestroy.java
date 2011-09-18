package weibo4j.examples.trends;

import weibo4j.Weibo;
import weibo4j.WeiboException;

public class trendsDestroy {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
	    String trend_id=args[2];
    	try {
    		Weibo weibo = new Weibo();
		    weibo.setToken(args[0],args[1]);
		    weibo.trendsDestroy(trend_id);
			System.out.println("取消关注话题id" + trend_id);
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

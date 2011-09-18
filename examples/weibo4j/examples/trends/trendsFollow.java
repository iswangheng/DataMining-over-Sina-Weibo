package weibo4j.examples.trends;

import weibo4j.Paging;
import weibo4j.Weibo;
import weibo4j.WeiboException;

public class trendsFollow {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
	    String trend_name=args[2];
    	try {
    		Weibo weibo = new Weibo();
		    weibo.setToken(args[0],args[1]);
		    weibo.trendsFollow(trend_name);
			System.out.println("关注话题" + trend_name);
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

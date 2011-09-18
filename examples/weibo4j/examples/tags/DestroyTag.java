package weibo4j.examples.tags;

import weibo4j.Weibo;


public class DestroyTag {
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
        try {
        	Weibo weibo = new Weibo();
			weibo.setToken(args[0],args[1]);
            boolean destroytag=weibo.destoryTag(args[2]);
             } catch (Exception e) {
			e.printStackTrace();
		}
	}
}


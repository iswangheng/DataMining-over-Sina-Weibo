package weibo4j.examples.tags;

import java.util.List;

import weibo4j.Paging;
import weibo4j.Tag;
import weibo4j.Weibo;

public class GetTags{

	/**
	 * 获取tags；
	 * @param args
	 * args[0]和args[1]为通过GetToken.java获取到的accesstoken和accesstoken secret
	 * args[2]为获取用户的Id
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
		try {
			Weibo weibo = new Weibo();
			weibo.setToken(args[0],args[1]);
			Paging paging = new Paging();
			paging.setCount(20);
			paging.setPage(1);
			List<Tag> gettags=weibo.getTags(args[2], paging);
			for(Tag status : gettags) {
				System.out.println( status.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
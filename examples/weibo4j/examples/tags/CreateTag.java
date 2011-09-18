package weibo4j.examples.tags;

import java.util.List;
import weibo4j.Tag;
import weibo4j.Weibo;

public class CreateTag {
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
		try {
			Weibo weibo = new Weibo();
			weibo.setToken(args[0],args[1]);
			//要创建的一组标签，用半角逗号隔开。
			List<Tag> tag= weibo.createTags(args[2]);
			for(Tag t:tag){
				System.out.println( t.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


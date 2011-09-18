/**
 * 
 */
package weibo4j.examples.statuses;

import weibo4j.Comment;
import weibo4j.Status;
import weibo4j.Weibo;

/**
 * @author sinaWeibo
 *
 */
public class DeleteComment {

	/**
	 * 删除评论,只能删除自己发布的评论
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
		try {
			Weibo weibo = new Weibo();
			weibo.setToken(args[0],args[1]);
			Status status = weibo.updateStatus("test4us");
			Thread.sleep(1000);
			String sid = status.getId()+"";
			System.out.println(sid + " : "+ status.getText()+"  "+status.getCreatedAt());
			Comment comment = weibo.updateComment("comment4u", sid, null);
			System.out.println(comment.getId() + " : " + comment.getText() + "  " + comment.getCreatedAt());
			Thread.sleep(1000);
			weibo.destroyComment(comment.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

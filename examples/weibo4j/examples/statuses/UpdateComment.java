/**
 *
 */
package weibo4j.examples.statuses;

import weibo4j.Comment;
import weibo4j.Status;
import weibo4j.Weibo;

/**
 * @author sina
 *
 */
public class UpdateComment {

	/**
	 * 对一条微博信息进行评论
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
        try {
        	Weibo weibo = new Weibo();
			weibo.setToken(args[0],args[1]);
        	Status status = weibo.updateStatus("test.....");
        	Thread.sleep(1000);
        	String sid = status.getId()+"";
        	Comment comment = weibo.updateComment("1", sid, null);
        	System.out.println(comment.getId() + " : " + comment.getText() + "  " + comment.getCreatedAt());
        	Thread.sleep(1000);
        	comment = weibo.updateComment("2", sid, null);
        	System.out.println(comment.getId() + " : " + comment.getText() + "  " + comment.getCreatedAt());
        	Thread.sleep(1000);
        	comment = weibo.updateComment("3", sid, null);
        	System.out.println(comment.getId() + " : " + comment.getText() + "  " + comment.getCreatedAt());
        	Thread.sleep(1000);
        	comment = weibo.updateComment("4", sid, null);
        	System.out.println(comment.getId() + " : " + comment.getText() + "  " + comment.getCreatedAt());
        	Thread.sleep(1000);
        	comment = weibo.updateComment("5", sid, null);
        	System.out.println(comment.getId() + " : " + comment.getText() + "  " + comment.getCreatedAt());
        	Thread.sleep(1000);
        	comment = weibo.updateComment("6", sid, null);
        	System.out.println(comment.getId() + " : " + comment.getText() + "  " + comment.getCreatedAt());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

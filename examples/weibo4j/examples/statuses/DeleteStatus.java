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
public class DeleteStatus {

	/**
	 * 删除一条微博信息
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
        try {
        	Weibo weibo = new Weibo();
			weibo.setToken(args[0],args[1]);
        	//先发表一篇微博
        	Status status = weibo.updateStatus("测试测试");
        	System.out.println(status.getId() + " : "+ status.getText()+"  "+status.getCreatedAt());
        	//删除刚发表的微博
        	status = weibo.destroyStatus(status.getId());
        	List<Status> list = weibo.getUserTimeline(args[2]);//args[2]:用户id
        	for(Status st : list) {//遍历当前微博信息
        		System.out.println(st.getId() + " : "+ st.getText()+"  "+st.getCreatedAt());
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

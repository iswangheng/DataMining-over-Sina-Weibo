package swarm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import weibo4j.Comment;
import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import weibo4j.http.Response;

public class GetRelationshipThread implements Runnable
{
	public void run()
	{
		try 
		{ 
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
			System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
			Weibo weibo = new Weibo();
			weibo.setToken(Access.accessToken, Access.accessTokenSecret);   
			int cursor = 0;
			long userId = 0;
			long followerId = 0; 
			List<User> userFollowersList;   
			
			do 
			{
				Connection con1 = PublicMethods.getConnection();
				java.sql.Statement stmt = con1.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				ResultSet rset = stmt.executeQuery("select * from users");
				do
				{
					rset.next();
					userId = rset.getLong(1);
					
					//if this user's relationship has not been stored
					if(PublicMethods.hasUserRecordInRelationship(userId) == false)
					{
						Response res = weibo.getFollowersStatusesResponse(userId+"",cursor,200); 
						userFollowersList = User.constructUser(res); 
						if(userFollowersList.size() == 0)
						{
							break;
						}
						for(User userFollower: userFollowersList)
						{
							if(userFollower != null)
							{  
								//System.out.println(user.getName());
								followerId = userFollower.getId(); 
								PublicMethods.InsertRelationshipSql(userId,followerId);		
								//PublicMethods.InsertUserSql(userFollower);		 
							}
							cursor = weibo.getTmdNextCursor(res); 
						} 
					} 
					else
					{
						cursor = -1;
					}
					Thread.sleep(1000);
				} 
				while(cursor != 0);  		
				con1.close();
			} 
			while (true);
		} 
		catch (Exception e1) 
		{ 
			e1.printStackTrace();
		}
	}
}

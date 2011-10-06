package swarm;

import java.sql.Connection;
import java.sql.ResultSet; 
import java.util.List;
 
import weibo4j.User; 
import weibo4j.http.Response;

public class GetRelationshipThread implements Runnable
{
	public void run()
	{
		try 
		{  
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
			    	Connection conRelationship = PublicMethods.getConnection();			
					//if this user's relationship has not been stored
					if(PublicMethods.hasUserRecordInRelationship(userId) == false)
					{
						do
						{			
								Response res = PublicMethods.weibo.getFollowersStatusesResponse(userId+"",cursor,200); 
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
										PublicMethods.InsertRelationshipSql(conRelationship,userId,followerId);		
										//PublicMethods.InsertUserSql(userFollower);		 
									}
									cursor = PublicMethods.weibo.getTmdNextCursor(res); 
									Thread.sleep(2000);
								}
						} 
						while(cursor != 0);  	
					}
					conRelationship.close();
				}
				while(!rset.isLast());
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

package swarm;

import java.sql.Connection;
import java.sql.ResultSet; 
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
 
import weibo4j.User; 
import weibo4j.WeiboException;
import weibo4j.http.Response;

public class GetRelationshipThread implements Runnable
{
	public static boolean getUserRelationship() throws ClassNotFoundException, SQLException, WeiboException, InterruptedException
	{
		boolean isDone = false;
		try 
		{  
			int cursor = -1;
			long userId = 0;
			long friendId = 0; 
			List<User> userFriendsList;    
			System.out.println(" Will connect to the database and get users.......");
			Connection conRelationship = PublicMethods.getConnection();
			java.sql.Statement stmt = conRelationship.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			ResultSet rset = stmt.executeQuery("select id from users where isRelationshipDone = false limit 100"); 
			if(rset.wasNull())
			{
				isDone = true;
			}
			while(rset.next())
			{
				userId = rset.getLong(1);	
				do
				{			
					Response res = PublicMethods.weibo.getFriendsStatusesResponse(userId+"",cursor,200); 
					userFriendsList = User.constructUser(res); 
					if(userFriendsList.size() == 0)
					{
						Thread.sleep(3900);
						break;
					}
					else
					{
						for(User userFriend: userFriendsList)
						{
							if(userFriend != null)
							{  
								System.out.println(userFriend.getName());
								friendId = userFriend.getId(); 
								PublicMethods.InsertRelationshipSql(conRelationship,friendId,userId);		
								//PublicMethods.InsertUserSql(userFollower,conRelationship);		 
							}
						}
						cursor = PublicMethods.weibo.getTmdNextCursor(res); 
					}
					Thread.sleep(3900);
				} 
				while(cursor != 0);  	
				PublicMethods.UpdateUsersRelationship(conRelationship,userId);
				Thread.sleep(1000);
			}  
			conRelationship.close(); 
			//System.out.println("The Relationship Crawling has finished~~~Lalalalalala"); 
		} 
		catch (Exception e1) 
		{ 
			e1.printStackTrace();
		}
		return isDone;
	}
	
	public void run()
	{ 
		try 
		{
			while(true)
			{
				getUserRelationship();
			}
			//System.out.println("The User's Status Crawling has finished~~~Lalalalalala");
		} catch (ClassNotFoundException e) { 
			e.printStackTrace();
		} catch (SQLException e) { 
			e.printStackTrace();
		} catch (WeiboException e) { 
			e.printStackTrace();
		} catch (InterruptedException e) { 
			e.printStackTrace();
		}
	}
}

package swarm;

import java.sql.Connection;
import java.sql.ResultSet; 
import java.util.ArrayList;
import java.util.Collections;
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
			List<Long> userIdList;
			do 
			{
				Connection conRelationship = PublicMethods.getConnection();
				java.sql.Statement stmt = conRelationship.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				ResultSet rset = stmt.executeQuery("select * from users");
				userIdList = new ArrayList<Long>();
				do
				{
					rset.next();
					userId = rset.getLong(1);	
					Long userIdLongValue = new Long(userId);
					userIdList.add(userIdLongValue);
				}
				while(!rset.isLast());
				Collections.shuffle(userIdList);
				for(Long userIdLong:userIdList)
				{			 	
					//if this user's relationship has not been stored
					//OR we can say, 或者说，如果Relationship表中的userId列有该用户（followerId）的记录，说明已经从该用户出发爬过他的followers了，就跳过他。
					if(PublicMethods.hasUserRecordInRelationship(userIdLong.longValue(),conRelationship) == false)
					{
						do
						{			
							Response res = PublicMethods.weibo.getFollowersStatusesResponse(userIdLong.longValue()+"",cursor,200); 
							userFollowersList = User.constructUser(res); 
							if(userFollowersList.size() == 0)
							{
								break;
							}
							else
							{
								for(User userFollower: userFollowersList)
								{
									if(userFollower != null)
									{  
										//System.out.println(user.getName());
										followerId = userFollower.getId(); 
										PublicMethods.InsertRelationshipSql(conRelationship,userIdLong.longValue(),followerId);		
										//PublicMethods.InsertUserSql(userFollower,conRelationship);		 
									}
								}
								cursor = PublicMethods.weibo.getTmdNextCursor(res); 
							}
							Thread.sleep(2700);
						} 
						while(cursor != 0);  	
						Thread.sleep(1000);
					} 
				} 
				conRelationship.close();
			} 
			while (true);
		} 
		catch (Exception e1) 
		{ 
			e1.printStackTrace();
		}
	}
}

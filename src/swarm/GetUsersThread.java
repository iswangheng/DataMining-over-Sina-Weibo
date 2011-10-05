package swarm;
/**
 * @author swarm 
 */ 
/**
* 
delimiter $$

CREATE TABLE `users` (
`id` bigint(20) NOT NULL,   
`screenName` varchar(45) DEFAULT NULL,  
`province` int(11) DEFAULT NULL,   
`city` int(11) DEFAULT NULL,    
`location` varchar(45) DEFAULT NULL,   
`description` mediumtext,    
`url` varchar(45) DEFAULT NULL,    
`profileImageUrl` varchar(45) DEFAULT NULL,   
`userDomain` varchar(45) DEFAULT NULL,   
`gender` varchar(45) DEFAULT NULL,   
`followersCount` int(11) DEFAULT NULL,    
`friendsCount` int(11) DEFAULT NULL,   
`statusesCount` int(11) DEFAULT NULL,    
`createdAt` datetime DEFAULT NULL,   
`verified` tinyint(1) DEFAULT NULL,   
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$
*/
	 
/**
* CREATE TABLE `relationship` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`userId` bigint(20) DEFAULT NULL,
`followerId` bigint(20) DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
*/
import java.sql.Connection;
import java.sql.PreparedStatement; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date; 
import java.util.List;

import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.User; 
import weibo4j.http.Response;

public class GetUsersThread implements Runnable
{  	 	
		
	public static void getMyFriendsFollowers(User user) 
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
			userId = user.getId();
			List<User> userFollowersList; 
			List<User> userAllFollowersList = new ArrayList<User>(); 
	
			//Status userStatus = user.getStatus();  
			PublicMethods.InsertUserSql(user);					//store current user 
			
			do									//get current user's followers and store all those followers~
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
						userAllFollowersList.add(userFollower); 
						//System.out.println(user.getName());
						followerId = userFollower.getId();
						if(PublicMethods.hasRecordInUser(followerId) == false)
						{ 
							PublicMethods.InsertRelationshipSql(userId,followerId);		
							PublicMethods.InsertUserSql(userFollower);		 
						}
					}
				}  
				cursor = weibo.getTmdNextCursor(res); 
				Thread.sleep(1000);
			} 
			while(cursor != 0);
			
	
			//now we are going to store the followers of the followers of current user, and current user means a friend of mine who has lots of followers;
		    /*for(User userInAllFollowersList: userAllFollowersList)
			{
				getFollowers(userInAllFollowersList.getId());				
			} 		
			*/	  
			 
		} 
		catch (Exception e1) 
		{ 
			e1.printStackTrace();
		}
	}
	
	public static void getFollowers(long userId) 
	{
		try 
		{ 
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
			System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
			Weibo weibo = new Weibo();
			weibo.setToken(Access.accessToken, Access.accessTokenSecret);   
			int cursor = 0;
			long followerId = 0; 
			List<User> userFollowersList;   
			do
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
						if(PublicMethods.hasRecordInRelationship(userId,followerId) == false)
						{
							PublicMethods.InsertRelationshipSql(userId,followerId);		
							PublicMethods.InsertUserSql(userFollower);						
							//Status followerStatus = userFollower.getStatus(); 
						} 
					}
				}  
				cursor = weibo.getTmdNextCursor(res); 
				Thread.sleep(1000);
			} 
			while(cursor != 0);  
		} 
		catch (Exception e1) 
		{ 
			e1.printStackTrace();
		}
	}
	
	
	public static void startFromMyself(List<User> myFriList)  
	{  
		for(User myFriend: myFriList)
		{
			getMyFriendsFollowers(myFriend);
		}
	}
	
	
	/**
	 * @author swarm
	 * @return the List of my friends' ID ~~
	 */
	public static List<User> getMyFriends()
	{
		List<User> myFriendsList = new ArrayList<User>();
		try 
		{
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
			System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
			Weibo weibo = new Weibo();
			weibo.setToken(Access.accessToken, Access.accessTokenSecret);
			int cursor;
			cursor = 0;
			try 
			{
				do
				{
					List<User> friendsList=  new ArrayList<User>();
					String currentUserId = "2407207504";
					Response res = weibo.getFriendsStatusesResponse(currentUserId, cursor, 200); 
					friendsList = User.constructUser(res);  
					for(User user: friendsList)
					{
						if(user != null)
						{ 
							myFriendsList.add(user); 
						} 
			    	}
					cursor = weibo.getTmdNextCursor(res);
				}
				while(cursor != 0);
				int friendsNum=0;
				Collections.shuffle(myFriendsList);
				for(User fri: myFriendsList)
				{
					System.out.println("friend screenName: "+fri.getScreenName());
					friendsNum++;
				}
				System.out.println("in total:  "+friendsNum);
			}
			catch (Exception e1) 
			{ 
				e1.printStackTrace(); 
			} 
		} 
		catch (Exception ioe) 
		{
			System.out.println("Failed to read the system input."); 
		}
		return myFriendsList;
	}

	public void run()  
	{ 
		List<User> myFriendsList = new ArrayList<User>();
		myFriendsList = getMyFriends();
		/*
		int friendsNum=0;
		for(User fri: myFriendsList)
		{
			System.out.println("Name: "+fri.getName());
			friendsNum++;
		}
		System.out.println("in total:  "+friendsNum);
		*/
		startFromMyself(myFriendsList);		
	}	
} 

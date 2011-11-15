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
import java.util.ArrayList;
import java.util.Collections; 
import java.util.List;
 
import weibo4j.User; 
import weibo4j.http.Response;

public class GetUsersThread implements Runnable
{  	 	
		
	public static void getMyFriendsFollowers(User user) 
	{
		try 
		{ 
			int cursor = -1;
			int crawleredFollowerNums = 0;
			long userId = 0;
			long followerId = 0; 
			userId = user.getId();
			List<User> userFollowersList; 
			//List<User> userAllFollowersList = new ArrayList<User>(); 

	    	Connection conUsers = PublicMethods.getConnection();	
			//Status userStatus = user.getStatus();  
	    	 
			PublicMethods.InsertUserSql(user,conUsers);					//store current user  
			do									//get current user's followers and store all those followers~
			{
				Response res = PublicMethods.weibo.getFollowersStatusesResponse(userId+"",cursor,200); 
				userFollowersList = User.constructUser(res); 
				if(userFollowersList.size() == 0)
				{
					Thread.sleep(2950);
					break;
				}
				for(User userFollower: userFollowersList)
				{
					if(userFollower != null)
					{
						//userAllFollowersList.add(userFollower); 
						//System.out.println(user.getName());
						
						//如果是僵尸粉，不活跃用户，那么就不要了，要了也没用 						
						if((userFollower.getStatusesCount() < 10) && (userFollower.getFollowersCount() < 5))
						{
							;   // 围脖数<10 and 粉丝数<5，这种用户就不要了
						}
						else
						{
							followerId = userFollower.getId();
							crawleredFollowerNums++;
							PublicMethods.InsertUserSql(userFollower,conUsers);		  
						}
					}
				}  
				cursor = PublicMethods.weibo.getTmdNextCursor(res); 
				if(crawleredFollowerNums > 100000)
				{
					break;
				}
				Thread.sleep(5900);
			} 
			while(cursor != 0); 
			conUsers.close(); 
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
	
	
	/* not useful at least for now*/
	/*
	public static void getFollowers(long userId) 
	{
		try 
		{  
			int cursor = 0;
			long followerId = 0; 
			List<User> userFollowersList;   
	    	Connection conUsers = PublicMethods.getConnection();	
	    	Connection conRelationship = PublicMethods.getConnection();	
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
						if(PublicMethods.hasRecordInRelationship(userId,followerId) == false)
						{
							PublicMethods.InsertRelationshipSql(conRelationship,userId,followerId);		
							PublicMethods.InsertUserSql(userFollower,conUsers);						
							//Status followerStatus = userFollower.getStatus(); 
						} 
					}
				}  
				cursor = PublicMethods.weibo.getTmdNextCursor(res); 
				Thread.sleep(2000);
			} 
			while(cursor != 0);  
			conUsers.close();
			conRelationship.close();
		} 
		catch (Exception e1) 
		{ 
			e1.printStackTrace();
		}
	}
	*/
	
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
			int cursor;
			cursor = -1;
			try 
			{
				do
				{
					List<User> friendsList=  new ArrayList<User>();
					String currentUserId = "2407207504";
					Response res = PublicMethods.weibo.getFriendsStatusesResponse(currentUserId, cursor, 200); 
					friendsList = User.constructUser(res);  
					for(User user: friendsList)
					{
						if(user != null)
						{ 
							myFriendsList.add(user); 
						} 
			    	}
					cursor = PublicMethods.weibo.getTmdNextCursor(res);
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

	/**
	 * @author swarm 
	 */
package swarm;
 /**
  * 
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `screenName` varchar(45) DEFAULT NULL,
  `province` int(11) DEFAULT NULL,
  `city` int(11) DEFAULT NULL,
  `location` varchar(45) DEFAULT NULL,
  `description` mediumtext DEFAULT NULL,
  `url` varchar(45) DEFAULT NULL,
  `profileImageUrl` varchar(45) DEFAULT NULL,
  `userDomain` varchar(45) DEFAULT NULL,
  `gender` varchar(45) DEFAULT NULL,
  `followersCount` int(11) DEFAULT NULL,
  `friendsCount` int(11) DEFAULT NULL,
  `statusesCount` int(11) DEFAULT NULL,
  `favouritesCount` int(11) DEFAULT NULL,
  `createdAt` date DEFAULT NULL,
  `verified` tinyint(1) DEFAULT NULL,
  `geoEnabled` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
  */
		 
/**
 * CREATE TABLE `relationship` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) DEFAULT NULL,
  `followerId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1825 DEFAULT CHARSET=utf8
 */
import java.sql.Connection;
import java.sql.PreparedStatement; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date; 
import java.util.List;
 
import weibo4j.Weibo;
import weibo4j.User; 
import weibo4j.http.Response;
 
public class GetUsers
{  	 	
	public static boolean InsertSql(User user) {
		try 
		{ 
			String insql = "insert ignore into users(id,screenName,province,city" +
					",location,description,url,profileImageUrl" +
					",userDomain,gender,followersCount,friendsCount" +
					",statusesCount,favouritesCount,createdAt,verified,geoEnabled) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 	    	Connection conUsers = PublicMethods.getConnection();	
			PreparedStatement ps = conUsers.prepareStatement(insql);   
			ps.setLong(1, user.getId());
			ps.setString(2, user.getScreenName());
			ps.setInt(3, user.getProvince());
			ps.setInt(4,  user.getCity());
			ps.setString(5, user.getLocation());
			ps.setString(6,  user.getDescription());
			String urlString = "";
			if(user.getURL() != null)
			{
				urlString = user.getURL().toString();
			}
			ps.setString(7,  urlString);
			String profileImageURL = "";
			if(user.getProfileImageURL() != null)
			{
				profileImageURL = user.getProfileImageURL().toString();
			}
			ps.setString(8,  profileImageURL);
			ps.setString(9,  user.getUserDomain());
			ps.setString(10,  user.getGender());
			ps.setInt(11,  user.getFollowersCount());
			ps.setInt(12,  user.getFriendsCount());
			ps.setInt(13,  user.getStatusesCount());
			ps.setInt(14,  user.getFavouritesCount());
			ps.setString(15,  PublicMethods.dateToMySQLDateTimeString(user.getCreatedAt()));   
			ps.setBoolean(16,  user.isVerified());
			ps.setBoolean(17,  user.isGeoEnabled());

			int result = ps.executeUpdate();
			conUsers.close(); 
			if (result > 0)
				return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	 	
		return false;
	}
	
	
	public static boolean InsertRelationshipSql(String userId, String followerId)
	{
		try 
		{ 
			String insql = "insert ignore into relationship(userId,followerId) values(?,?)";
 	    	Connection conRelationship = PublicMethods.getConnection();	
			PreparedStatement ps = conRelationship.prepareStatement(insql);   
			ps.setString(1, userId);
			ps.setString(2, followerId); 
			int result = ps.executeUpdate();
			conRelationship.close(); 
			if (result > 0)
				return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	 	
		return false;
	}
	
	
	public static boolean hasRecordInRelationship(long userId, long followerId) throws ClassNotFoundException, SQLException
	{
		String query = "select count(*) from relationship where  userId = "+userId+" and followerId = "+followerId; 
		Connection con = PublicMethods.getConnection();	
		PreparedStatement ps = con.prepareStatement(query); 
		ResultSet rs = ps.executeQuery();
		rs.next();
		int countNum = rs.getInt(1);
		con.close();
		//System.out.println("CountNum is : "+countNum);
		if(countNum == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	
	public static void getMyFriendsFollowers(User user) 
	{
		try 
		{ 
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
			System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
			Weibo weibo = new Weibo();
			weibo.setToken(Access.accessToken, Access.accessTokenSecret);  
			Thread.currentThread();
			int cursor = 0;
			long userId = 0;
			long followerId = 0; 
			userId = user.getId();
			List<User> userFollowersList; 
			List<User> userAllFollowersList = new ArrayList<User>(); 
			
			InsertSql(user);					//store current user 
			
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
						if(hasRecordInRelationship(userId,followerId) == false)
						{
							InsertRelationshipSql(userId+"",followerId+"");							
						}
						InsertSql(userFollower);
					}
				}  
				cursor = weibo.getTmdNextCursor(res); 
				Thread.sleep(3000);
			} 
			while(cursor != 0);
			

			//now we are going to store the followers of the followers of current user, and current user means a friend of mine who has lots of followers;
			for(User userInAllFollowersList: userAllFollowersList)
			{
				getFollowers(userInAllFollowersList.getId());				
			} 			  
			 
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
			Thread.currentThread();
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
						if(hasRecordInRelationship(userId,followerId) == false)
						{
							InsertRelationshipSql(userId+"",followerId+"");							
						} 
						InsertSql(userFollower);
					}
				}  
				cursor = weibo.getTmdNextCursor(res); 
				Thread.sleep(3000);
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
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{ 
		List<User> myFriendsList = new ArrayList<User>();
		myFriendsList = getMyFriends();
		/* test myFriendsList()
		int friendsNum=0;
		for(String fri: myFriendsList)
		{
			System.out.println("friId: "+fri);
			friendsNum++;
		}
		System.out.println("in total:  "+friendsNum);
		*/
		startFromMyself(myFriendsList);		
	}	
}

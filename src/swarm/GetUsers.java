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
  `description` varchar(45) DEFAULT NULL,
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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import weibo4j.Comment;
import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.User;
import weibo4j.WeiboException;
import weibo4j.http.Response;
 
public class GetUsers
{ 
	public static Connection getConnection() throws SQLException,
	java.lang.ClassNotFoundException 
	{
		Class.forName("com.mysql.jdbc.Driver");
		
		String url = "jdbc:mysql://localhost:3306/weibo";
		String username = "root";
		String password = "root";
		
		Connection conUsers = DriverManager.getConnection(url, username, password);
		return conUsers;
	}
	
	public static String dateToMySQLDateTimeString(Date date) 
	{
		final String[] MONTH = { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
				"Jul", "Aug", "Sep", "Oct", "Nov", "Dec", };

		StringBuffer ret = new StringBuffer();
		String dateToString = date.toString(); // like
												// "Sat Dec 17 15:55:16 CST 2005"
		ret.append(dateToString.substring(24, 24 + 4));// append yyyy
		String sMonth = dateToString.substring(4, 4 + 3);
		for (int i = 0; i < 12; i++) { // append mm
			if (sMonth.equalsIgnoreCase(MONTH[i])) {
				if ((i + 1) < 10)
					ret.append("-0");
				else
					ret.append("-");
				ret.append((i + 1));
				break;
			}
		}

		ret.append("-");
		ret.append(dateToString.substring(8, 8 + 2));
		ret.append(" ");
		ret.append(dateToString.substring(11, 11 + 8));

		return ret.toString();
	}
	
	public static boolean InsertSql(User user) {
		try 
		{ 
			String insql = "insert ignore into users(id,screenName,province,city" +
					",location,description,url,profileImageUrl" +
					",userDomain,gender,followersCount,friendsCount" +
					",statusesCount,favouritesCount,createdAt,verified,geoEnabled) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 	    	Connection conUsers = getConnection();	
			PreparedStatement ps = conUsers.prepareStatement(insql);   
			ps.setLong(1, user.getId());
			ps.setString(2, user.getName());
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
			ps.setString(15, dateToMySQLDateTimeString(user.getCreatedAt()));   
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
 	    	Connection conRelationship = getConnection();	
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

	
	public static void getMyFriendsFollowers(String userId) 
	{
		try 
		{ 
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
			System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
			Weibo weibo = new Weibo();
			weibo.setToken(Access.accessToken, Access.accessTokenSecret);  
			Thread.currentThread();
			int cursor = 0;
			String followerId = ""; 
			List<User> userList; 
			List<User> followerList = new ArrayList<User>(); 
			do
			{
				Response res = weibo.getFollowersStatusesResponse(userId,cursor,200); 
				userList = User.constructUser(res); 
				if(userList.size() == 0)
				{
					break;
				}
				for(User user: userList)
				{
					if(user != null)
					{
						followerList.add(user); 
						//System.out.println(user.getName());
						followerId = user.getId()+"";
						InsertRelationshipSql(userId,followerId);
						InsertSql(user);
					}
				}  
				cursor = weibo.getTmdNextCursor(res); 
				Thread.sleep(5000);
			} 
			while(cursor != 0);
			//System.out.println("  size: "+followerList.size()+" cursor: "+cursor+" friendsNUm: "+friendsNum); 
			for(User user: followerList)
			{
				getFollowers(user.getId()+"");				
			} 
		} 
		catch (Exception e1) 
		{ 
			e1.printStackTrace();
		}
	}
	
	public static void getFollowers(String userId) 
	{
		try 
		{ 
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
			System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
			Weibo weibo = new Weibo();
			weibo.setToken(Access.accessToken, Access.accessTokenSecret);  
			Thread.currentThread();
			int cursor = 0;
			String followerId = ""; 
			List<User> userList;   
			do
			{
				Response res = weibo.getFollowersStatusesResponse(userId,cursor,200); 
				userList = User.constructUser(res); 
				if(userList.size() == 0)
				{
					break;
				}
				for(User user: userList)
				{
					if(user != null)
					{  
						//System.out.println(user.getName());
						followerId = user.getId()+"";
						InsertRelationshipSql(userId,followerId);
						InsertSql(user);
					}
				}  
				cursor = weibo.getTmdNextCursor(res); 
				Thread.sleep(5000);
			} 
			while(cursor != 0);  
		} 
		catch (Exception e1) 
		{ 
			e1.printStackTrace();
		}
	}
	
	
	public static void startFromMyself(List<String> myFriList)  
	{  
		for(String myFriendId: myFriList)
		{
			getMyFriendsFollowers(myFriendId);
		}
	}
	
	
	/**
	 * @author swarm
	 * @return the List of my friends' ID ~~
	 */
	public static List<String> getMyFriends()
	{
		List<String> myFriendsList = new ArrayList<String>();
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
							myFriendsList.add(user.getId()+"");  
						} 
			    	}
					cursor = weibo.getTmdNextCursor(res);
				}
				while(cursor != 0);
				int friendsNum=0;
				for(String fri: myFriendsList)
				{
					System.out.println("friId: "+fri);
					friendsNum++;
				}
				System.out.println("in total:  "+friendsNum);
			}
			catch (Exception e1) 
			{
				// TODO Auto-generated catch block
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
		List<String> myFriendsList = new ArrayList<String>();
		myFriendsList = getMyFriends();
		/*
		 * test myFriendsList()
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

package swarm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import weibo4j.Comment;
import weibo4j.Status;
import weibo4j.User;
import weibo4j.Weibo;

public class PublicMethods
{	
	public static Weibo weibo;
	public static void setWeibo(String consumerKey, String consumerSecret,String accessToken,String accessTokenSecret) 
	{   
		Weibo.CONSUMER_KEY = consumerKey;
		Weibo.CONSUMER_SECRET = consumerSecret;
		System.setProperty("weibo4j.oauth.consumerKey", consumerKey);
    	System.setProperty("weibo4j.oauth.consumerSecret", consumerSecret); 
		weibo = new Weibo();
		weibo.setToken(accessToken, accessTokenSecret); 
	}
	
	public static Connection getConnection() throws SQLException,java.lang.ClassNotFoundException 
	{
		Class.forName("com.mysql.jdbc.Driver");

		String url = "jdbc:mysql://173.231.28.142:3306/weibo"; 
		String username = "weibo";
		String password = "weibo";

		Connection con = DriverManager.getConnection(url, username, password);
		return con;
	}
	
	
	public static boolean InsertUserSql(User user,Connection conUsers) {
		try 
		{ 
			String insql = "insert ignore into users(id,screenName,province,city" +
					",location,description,url,profileImageUrl" +
					",userDomain,gender,followersCount,friendsCount" +
					",statusesCount,createdAt,verified) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
			ps.setString(14,  PublicMethods.dateToMySQLDateTimeString(user.getCreatedAt()));   
			ps.setBoolean(15,  user.isVerified());
	
			int result = ps.executeUpdate();
			if (result > 0)
				return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	 	
		return false;
	}

	public static boolean InsertRelationshipSql(Connection conRelationship,Long userId, Long followerId)
	{
		try 
		{ 
			String insql = "insert into relationship(userId,followerId) values(?,?)";
			PreparedStatement ps = conRelationship.prepareStatement(insql);   
			ps.setLong(1, userId);
			ps.setLong(2, followerId); 
			int result = ps.executeUpdate(); 
			if (result > 0)
				return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	 	
		return false;
	}
	
	
	
	public static boolean hasRecordInUser(long userId, Connection  con) throws ClassNotFoundException, SQLException
	{
		String query = "select count(*) from users where  id = "+userId;  
		PreparedStatement ps = con.prepareStatement(query); 
		ResultSet rs = ps.executeQuery();
		rs.next();
		int countNum = rs.getInt(1); 
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
	
	public static boolean hasRecordInRelationship(long userId, long followerId, Connection con) throws ClassNotFoundException, SQLException
	{
		String query = "select count(*) from relationship where  userId = "+userId+" and followerId = "+followerId;  
		PreparedStatement ps = con.prepareStatement(query); 
		ResultSet rs = ps.executeQuery();
		rs.next();
		int countNum = rs.getInt(1); 
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
	
	public static boolean hasUserRecordInRelationship(long userId,Connection con) throws ClassNotFoundException, SQLException
	{
		String query = "select count(*) from relationship where  userId = "+userId;  
		PreparedStatement ps = con.prepareStatement(query); 
		ResultSet rs = ps.executeQuery();
		rs.next();
		int countNum = rs.getInt(1); 
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
	
	public static String dateToMySQLDateTimeString(Date date)
	{
		final String[] MONTH = { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
				"Jul", "Aug", "Sep", "Oct", "Nov", "Dec", };

		StringBuffer ret = new StringBuffer();
		String dateToString = date.toString(); // like
												// "Sat Dec 17 15:55:16 CST 2005"
		ret.append(dateToString.substring(24, 24 + 4));// append yyyy
		String sMonth = dateToString.substring(4, 4 + 3);
		for (int i = 0; i < 12; i++) 
		{ // append mm
			if (sMonth.equalsIgnoreCase(MONTH[i])) 
			{
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
	 
 
	public static boolean InsertStatusSql(Connection conStatus,Status status, int rtCounts,int commentCounts) 
	{
		try 
		{
			String insql = "insert ignore into status(id,userScreenName,userId,createdAt,text,source,latitude,longitude,original_pic,retweeted_statusId,rtCounts,commentCounts,mid) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement ps = conStatus.prepareStatement(insql);
			ps.setLong(1, status.getId());
			ps.setString(2, status.getUser().getName());
			ps.setLong(3, status.getUser().getId());
			ps.setString(4, PublicMethods.dateToMySQLDateTimeString(status.getCreatedAt()));
			ps.setString(5, status.getText());
			ps.setString(6, status.getSource()); 
			ps.setDouble(7, status.getLatitude());
			ps.setDouble(8, status.getLongitude());
			ps.setString(9, status.getOriginal_pic());
			int retweetStatusId = 0;
			Status retweetStatus = null;
			retweetStatus = status.getRetweeted_status();
			if(retweetStatus != null)
			{
				retweetStatusId = (int) retweetStatus.getId();
			}
			ps.setLong(10, retweetStatusId);
			ps.setInt(11, rtCounts);
			ps.setInt(12, commentCounts);
			ps.setString(13, status.getMid());

			int result = ps.executeUpdate(); 
			if (result > 0)
				return true;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean InsertCommentsSql(Connection conComments,Comment comment, long statusId) 
	{
		try 
		{
			String insql = "insert ignore into comments(id,userScreenName,userId,createdAt,statusId,text,source) values(?,?,?,?,?,?,?)";
			PreparedStatement ps = conComments.prepareStatement(insql);
			ps.setLong(1, comment.getId());
			ps.setString(2, comment.getUser().getScreenName());
			ps.setLong(3, comment.getUser().getId());
			ps.setString(4, PublicMethods.dateToMySQLDateTimeString(comment.getCreatedAt()));
			ps.setLong(5, statusId);
			ps.setString(6, comment.getText());
			ps.setString(7, comment.getSource()); 
			int result = ps.executeUpdate(); 
			if (result > 0)
				return true;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	 
}

package swarm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import weibo4j.Status;
import weibo4j.User;

public class PublicMethods
{
	public static Connection getConnection() throws SQLException,java.lang.ClassNotFoundException 
	{
		Class.forName("com.mysql.jdbc.Driver");

		String url = "jdbc:mysql://localhost:3306/weibo";
		String username = "root";
		String password = "root";

		Connection con = DriverManager.getConnection(url, username, password);
		return con;
	}
	
	
	public static boolean InsertUserSql(User user) {
		try 
		{ 
			String insql = "insert ignore into users(id,screenName,province,city" +
					",location,description,url,profileImageUrl" +
					",userDomain,gender,followersCount,friendsCount" +
					",statusesCount,createdAt,verified) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
			ps.setString(14,  PublicMethods.dateToMySQLDateTimeString(user.getCreatedAt()));   
			ps.setBoolean(15,  user.isVerified());
	
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

	public static boolean InsertRelationshipSql(Long userId, Long followerId)
	{
		try 
		{ 
			String insql = "insert ignore into relationship(userId,followerId) values(?,?)";
		    	Connection conRelationship = PublicMethods.getConnection();	
			PreparedStatement ps = conRelationship.prepareStatement(insql);   
			ps.setLong(1, userId);
			ps.setLong(2, followerId); 
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
	
	
	
	public static boolean hasRecordInUser(long userId) throws ClassNotFoundException, SQLException
	{
		String query = "select count(*) from users where  id = "+userId; 
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
	
	public static boolean hasUserRecordInRelationship(long userId) throws ClassNotFoundException, SQLException
	{
		String query = "select count(*) from relationship where  userId = "+userId; 
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
	
	
	
	/*  wont be useful again....
	public static boolean InsertStatusSql(Status status) 
	{
		try 
		{
			String insql = "insert ignore into status(id,userName,userId,createdAt,text,source,isTruncated,inReplyToStatusId,inReplyToUserId,isFavorited,inReplyToScreenName,latitude,longitude,thumbnail_pic,bmiddle_pic,original_pic,mid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Connection con = PublicMethods.getConnection();
			PreparedStatement ps = con.prepareStatement(insql);
			ps.setLong(1, status.getId());
			ps.setString(2, status.getUser().getName());
			ps.setLong(3, status.getUser().getId());
			ps.setString(4, PublicMethods.dateToMySQLDateTimeString(status
					.getCreatedAt()));
			ps.setString(5, status.getText());
			ps.setString(6, status.getSource());
			ps.setBoolean(7, status.isTruncated());
			ps.setLong(8, status.getInReplyToStatusId());
			ps.setLong(9, status.getInReplyToUserId());
			ps.setBoolean(10, status.isFavorited());
			ps.setString(11, status.getInReplyToScreenName());
			ps.setDouble(12, status.getLatitude());
			ps.setDouble(13, status.getLongitude());
			ps.setString(14, status.getThumbnail_pic());
			ps.setString(15, status.getBmiddle_pic());
			ps.setString(16, status.getOriginal_pic());
			ps.setString(17, status.getMid());

			int result = ps.executeUpdate();
			con.close();
			if (result > 0)
				return true;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean InsertStatusSql(Status status,User user) 
	{
		try 
		{
			String insql = "insert ignore into status(id,userName,userId,createdAt,text,source,isTruncated,inReplyToStatusId,inReplyToUserId,isFavorited,inReplyToScreenName,latitude,longitude,thumbnail_pic,bmiddle_pic,original_pic,mid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Connection con = PublicMethods.getConnection();
			PreparedStatement ps = con.prepareStatement(insql);
			ps.setLong(1, status.getId());
			ps.setString(2, user.getScreenName());
			ps.setLong(3, user.getId());
			ps.setString(4, PublicMethods.dateToMySQLDateTimeString(status
					.getCreatedAt()));
			ps.setString(5, status.getText());
			ps.setString(6, status.getSource());
			ps.setBoolean(7, status.isTruncated());
			ps.setLong(8, status.getInReplyToStatusId());
			ps.setLong(9, status.getInReplyToUserId());
			ps.setBoolean(10, status.isFavorited());
			ps.setString(11, status.getInReplyToScreenName());
			ps.setDouble(12, status.getLatitude());
			ps.setDouble(13, status.getLongitude());
			ps.setString(14, status.getThumbnail_pic());
			ps.setString(15, status.getBmiddle_pic());
			ps.setString(16, status.getOriginal_pic());
			ps.setString(17, status.getMid());

			int result = ps.executeUpdate();
			con.close();
			if (result > 0)
				return true;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	  */
}

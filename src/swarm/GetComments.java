/**
 * 
 */
package swarm;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import weibo4j.Comment;
import weibo4j.Weibo;
import weibo4j.WeiboException;

/**
 * 

CREATE TABLE `comments` (
  `id` bigint(20) NOT NULL,
  `userName` longtext,
  `userId` bigint(20) DEFAULT NULL,
  `createdAt` longtext,
  `statusId` bigint(20) DEFAULT NULL,
  `text` longtext,
  `source` longtext,
  `isTruncated` tinyint(1) DEFAULT NULL,
  `inReplyToStatusId` mediumtext,
  `inReplyToUserId` mediumtext,
  `isFavorited` tinyint(1) DEFAULT NULL,
  `inReplyToScreenName` longtext,
  `latitude` tinyint(1) DEFAULT NULL,
  `longitude` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

 * 
 */


public class GetComments {

	/**
	 * 返回指定微博的最新n条评论
	 * 
	 * @param args
	 * @throws WeiboException
	 */

	public static Connection getConnection() throws SQLException,
			java.lang.ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");

		String url = "jdbc:mysql://localhost:3306/weibo";
		String username = "root";
		String password = "root";

		Connection con = DriverManager.getConnection(url, username, password);
		return con;
	}

	public static String dateToMySQLDateTimeString(Date date) {
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

	public static boolean InsertSql(Comment comment, long statusId) {
		try {
			/*
			
			 */
			String insql = "insert ignore into comments(id,userName,userId,createdAt,statusId,text,source,isTruncated,inReplyToStatusId,inReplyToUserId,isFavorited,inReplyToScreenName,latitude,longitude) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Connection con = getConnection();
			// insql="insert into user(userid,username,password,email) values(user.getId,user.getName,user.getPassword,user.getEmail)";
			PreparedStatement ps = con.prepareStatement(insql);
			// .preparedStatement(insql);
			// PreparedStatement ps=(PreparedStatement)
			// conn.prepareStatement(insql);
			ps.setLong(1, comment.getId());
			ps.setString(2, comment.getUser().getName());
			ps.setLong(3, comment.getUser().getId());
			ps.setString(4, dateToMySQLDateTimeString(comment.getCreatedAt()));
			ps.setLong(5, statusId);
			ps.setString(6, comment.getText());
			ps.setString(7, comment.getSource());
			ps.setBoolean(8, comment.isTruncated());
			ps.setLong(9, comment.getInReplyToStatusId());
			ps.setLong(10, comment.getInReplyToUserId());
			ps.setBoolean(11, comment.isFavorited());
			ps.setString(12, comment.getInReplyToScreenName());
			ps.setDouble(13, comment.getLatitude());
			ps.setDouble(14, comment.getLongitude());
			int result = ps.executeUpdate();
			// ps.executeUpdate();
			if (result > 0)
				return true;
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static void getComments() throws ClassNotFoundException,
			SQLException {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret",
				Weibo.CONSUMER_SECRET);
		Connection con1 = getConnection();
		java.sql.Statement stmt = con1.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rset = stmt.executeQuery("select * from status");
		do {
			try {
				Weibo weibo = new Weibo();
				String accessToken = "8f5c79949a6bf0e99993f38292cf5be3";
				String accessTokenSecret = "5564ed6f9e9a9dc8cbb859d9db60850b";
				weibo.setToken(accessToken, accessTokenSecret);
				rset.next();
				Long statusId = rset.getLong(1);
				List<Comment> comments = weibo.getComments(statusId.toString());
				for (Comment comment : comments) {
					InsertSql(comment, statusId);
				}
			} catch (SQLException ex) {
				System.err.println("SQLException: " + ex.getMessage());
			} catch (WeiboException e) {
				e.printStackTrace();
			}
		} while (!rset.isLast());
		con1.close();
	}

	public static void main(String[] args) throws WeiboException,
			ClassNotFoundException, SQLException {
		getComments();
	}
}

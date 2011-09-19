package swarm;

/**
 *
 */

import java.util.List;
import java.util.Date;

import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import weibo4j.http.AccessToken;
import weibo4j.http.RequestToken;
import java.sql.*;

public class GetPublicTimeline {

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

	public static boolean InsertSql(Status status) {
		try {
			/*
			
			 */
			String insql = "insert ignore into status(id,userName,userId,createdAt,text,source,isTruncated,inReplyToStatusId,inReplyToUserId,isFavorited,inReplyToScreenName,latitude,longitude,thumbnail_pic,bmiddle_pic,original_pic,mid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			// insql="insert into user(userid,username,password,email) values(user.getId,user.getName,user.getPassword,user.getEmail)";
			PreparedStatement ps = getConnection().prepareStatement(insql);
			// .preparedStatement(insql);
			// PreparedStatement ps=(PreparedStatement)
			// conn.prepareStatement(insql);
			ps.setLong(1, status.getId());
			ps.setString(2, status.getUser().getName());
			ps.setLong(3, status.getUser().getId());
			ps.setString(4, dateToMySQLDateTimeString(status.getCreatedAt()));
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
			// ps.executeUpdate();
			if (result > 0)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void getPublicTimeline() {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret",
				Weibo.CONSUMER_SECRET);
		do {
			try {
				Weibo weibo = new Weibo();
				String accessToken = "8f5c79949a6bf0e99993f38292cf5be3";
				String accessTokenSecret = "5564ed6f9e9a9dc8cbb859d9db60850b";
				weibo.setToken(accessToken, accessTokenSecret);
				try {
					Connection con = getConnection();
					Statement sql_statement = con.createStatement();

					List<Status> statuses = weibo.getPublicTimeline();
					for (Status status : statuses) {
						InsertSql(status);
						/*
						 * String statusTest = status.getText().replace("'",
						 * "''"); String sqlStr =
						 * "insert ignore into`text` values(" + status.getId() +
						 * ",'" + status.getUser().getId() + "','" +
						 * status.getUser().getName() + "','" +
						 * dateToMySQLDateTimeString(status .getCreatedAt()) +
						 * "','" + statusTest + "','" + status.getSource() +
						 * "','" + (status.isTruncated() ? 1 : 0) + "','" +
						 * status.getInReplyToStatusId() + "','" +
						 * status.getInReplyToUserId() + "','" +
						 * (status.isFavorited() ? 1 : 0) + "','" +
						 * status.getInReplyToScreenName() + "','" +
						 * status.getLatitude() + "','" + status.getLongitude()
						 * + "','" + status.getThumbnail_pic() + "','" +
						 * status.getBmiddle_pic() + "','" +
						 * status.getOriginal_pic() + "','" + status.getMid() +
						 * "');";
						 */
						// System.out.println("query is : " + sqlStr);

						// sql_statement.execute(sqlStr);
					}
					sql_statement.close();
					con.close();
				} catch (java.lang.ClassNotFoundException e) {
					System.err.print("ClassNotFoundException");
					System.err.println(e.getMessage());
				}
			} catch (SQLException ex) {
				System.err.println("SQLException: " + ex.getMessage());
			} catch (WeiboException e) {
				e.printStackTrace();
			}
		} while (true);

	}

	public static void main(String[] args) {
		getPublicTimeline();
	}
}

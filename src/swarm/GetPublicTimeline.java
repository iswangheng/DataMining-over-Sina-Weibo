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
			String insql = "insert ignore into status(id,createdAt,text,source,isTruncated,inReplyToStatusId,inReplyToUserId,isFavorited,inReplyToScreenName,latitude,longitude,thumbnail_pic,bmiddle_pic,original_pic,mid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			// 涓婇潰鐨勬柟娉曟瘮涓嬮潰鐨勬柟娉曟湁浼樺娍锛屼竴鏂归潰鏄畨鍏ㄦ�锛屽彟涓�柟闈㈡垜蹇樿浜嗏�鈥�
			// insql="insert into user(userid,username,password,email) values(user.getId,user.getName,user.getPassword,user.getEmail)";
			PreparedStatement ps = getConnection().prepareStatement(insql);
			// .preparedStatement(insql);
			// PreparedStatement ps=(PreparedStatement)
			// conn.prepareStatement(insql);		
			ps.setLong(1, status.getId());
			ps.setString(2, dateToMySQLDateTimeString(status.getCreatedAt()));
			ps.setString(3, status.getText());
			ps.setString(4, status.getSource());
			ps.setBoolean(5, status.isTruncated());
			ps.setLong(6, status.getInReplyToStatusId());
			ps.setLong(7, status.getInReplyToUserId());
			ps.setBoolean(8, status.isFavorited());
			ps.setString(9, status.getInReplyToScreenName());
			ps.setDouble(10, status.getLatitude());
			ps.setDouble(11, status.getLongitude());
			ps.setString(12, status.getThumbnail_pic());
			ps.setString(13, status.getBmiddle_pic());
			ps.setString(14, status.getOriginal_pic());
			ps.setString(15, status.getMid());

			int result = ps.executeUpdate();
			// ps.executeUpdate();鏃犳硶鍒ゆ柇鏄惁宸茬粡鎻掑叆
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
		try {
			// 获取前20条最新更新的公共微博消息
			Weibo weibo = new Weibo();
			String accessToken = "8f5c79949a6bf0e99993f38292cf5be3";
			String accessTokenSecret = "5564ed6f9e9a9dc8cbb859d9db60850b";
			weibo.setToken(accessToken, accessTokenSecret);
			try {
				Connection con = getConnection();
				Statement sql_statement = con.createStatement();

				for (int i = 0; i < 1611; i++) {
					List<Status> statuses = weibo.getPublicTimeline();
					for (Status status : statuses) {
						InsertSql(status);
						/*String statusTest = status.getText().replace("'", "''");
						String sqlStr = "insert ignore into`text` values("
								+ status.getId()
								+ ",'"
								+ status.getUser().getId()
								+ "','"
								+ status.getUser().getName()
								+ "','"
								+ dateToMySQLDateTimeString(status
										.getCreatedAt()) + "','" + statusTest
								+ "','" + status.getSource() + "','"
								+ (status.isTruncated() ? 1 : 0) + "','"
								+ status.getInReplyToStatusId() + "','"
								+ status.getInReplyToUserId() + "','"
								+ (status.isFavorited() ? 1 : 0) + "','"
								+ status.getInReplyToScreenName() + "','"
								+ status.getLatitude() + "','"
								+ status.getLongitude() + "','"
								+ status.getThumbnail_pic() + "','"
								+ status.getBmiddle_pic() + "','"
								+ status.getOriginal_pic() + "','"
								+ status.getMid() + "');";*/
						//System.out.println("query is : " + sqlStr);

						//sql_statement.execute(sqlStr);
					}
				}

				System.out.println("Finished!!~~");

				sql_statement.close();
				con.close();

			} catch (java.lang.ClassNotFoundException e) {
				System.err.print("ClassNotFoundException");
				System.err.println(e.getMessage());
			} catch (SQLException ex) {
				System.err.println("SQLException: " + ex.getMessage());
			}

		} catch (WeiboException e) {
			e.printStackTrace();
		}
		
		
	}

	public static void main(String[] args) {
		getPublicTimeline();
	}
}

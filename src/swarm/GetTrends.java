package swarm;
/** SQL:
  CREATE TABLE `trend` (
  `idTrend` int(11) NOT NULL AUTO_INCREMENT,
  `Time` varchar(45) DEFAULT NULL,
  `Content` longtext,
  PRIMARY KEY (`idTrend`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8
 * */ 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import weibo4j.Paging;
import weibo4j.Status;
import weibo4j.Trend;
import weibo4j.Trends;
import weibo4j.Weibo;
import weibo4j.WeiboException;

public class GetTrends {
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

	public static void InsertSql(Trends trends) {
		try {
			String insql = "insert ignore into Trend(Time,Content) values(?,?)";
			Connection con = getConnection();
			PreparedStatement ps = con.prepareStatement(insql);

			for (Trend trend : trends.getTrends()) {
				System.out.println(dateToMySQLDateTimeString(trends.getTrendAt()));
				System.out.println(trend.getName());
				ps.setString(1, dateToMySQLDateTimeString(trends.getTrendAt()));
				ps.setString(2, trend.getName());
				ps.executeUpdate();
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getTrends() {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret",
				Weibo.CONSUMER_SECRET);
		Paging paging = new Paging();
		paging.setCount(20);
		paging.setPage(1);
		do {
			try {
				Weibo weibo = new Weibo();
				weibo.setToken(Access.accessToken, Access.accessTokenSecret);
				List<Trends> trends = weibo.getTrendsHourly(0); // 每小时的
				//List<Trends> trends = weibo.getTrendsDaily(0);  // 每天的
				//List<Trends> trends = weibo.getTrendsWeekly(0); // 每周的
				InsertSql(trends.get(0));
			} catch (WeiboException e) {
				e.printStackTrace();
			}
		} while (false);

	}

	public static void main(String[] args) {
		getTrends();
	}
}

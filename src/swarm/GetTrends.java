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
	
	public static void InsertSql(Trends trends) {
		try {
			String insql = "insert ignore into Trend(Time,Content) values(?,?)";
			Connection con = PublicMethods.getConnection();
			PreparedStatement ps = con.prepareStatement(insql);

			for (Trend trend : trends.getTrends()) {
				System.out.println(PublicMethods.dateToMySQLDateTimeString(trends.getAsOf()));
				System.out.println(trend.getName());
				ps.setString(1, PublicMethods.dateToMySQLDateTimeString(trends.getAsOf()));
				ps.setString(2, trend.getName());
				ps.executeUpdate();
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getTrends() throws InterruptedException {
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
				Thread.currentThread();
				Thread.sleep(1000);
			} catch (WeiboException e) {
				e.printStackTrace();
			}
		} while (false);

	}

	public static void main(String[] args) throws InterruptedException {
		getTrends();
	}
}

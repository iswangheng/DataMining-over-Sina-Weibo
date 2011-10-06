package swarm;

import java.sql.Connection; 
import java.sql.PreparedStatement; 
import java.util.List;

import weibo4j.Paging; 
import weibo4j.Trend;
import weibo4j.Trends; 
import weibo4j.WeiboException;
 
public class GetTrendsThread implements Runnable
{
	int pauseTime;
	
	public GetTrendsThread()
	{
		pauseTime = 1000*60*60;
	}

	public static void InsertSql(Trends trends) 
	{
		try 
		{
			String insql = "insert ignore into trend(time,content) values(?,?)";
			Connection con = PublicMethods.getConnection();
			PreparedStatement ps = con.prepareStatement(insql);

			for (Trend trend : trends.getTrends()) 
			{
				System.out.println(PublicMethods
						.dateToMySQLDateTimeString(trends.getAsOf()));
				System.out.println(trend.getName());
				ps.setString(1, PublicMethods.dateToMySQLDateTimeString(trends
						.getAsOf()));
				ps.setString(2, trend.getName());
				ps.executeUpdate();
			}
			con.close();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
	public  void run()
	{ 
		Paging paging = new Paging();
		paging.setCount(20);
		paging.setPage(1);
		do 
		{
				try 
				{ 
					List<Trends> trends = PublicMethods.weibo.getTrendsHourly(0); // 每小时的
					// List<Trends> trends = weibo.getTrendsDaily(0); // 每天的
					// List<Trends> trends = weibo.getTrendsWeekly(0); // 每周的
					InsertSql(trends.get(0)); 
					Thread.sleep(pauseTime);
				} catch (WeiboException e) {
					e.printStackTrace();
				} catch (InterruptedException e) { 
					e.printStackTrace();
			}
		} while (true);

	}
	
	
}

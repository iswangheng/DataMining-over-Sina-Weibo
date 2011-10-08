package swarm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import weibo4j.Count;
import weibo4j.Paging;
import weibo4j.Status; 
import weibo4j.WeiboException;

public class GetUserStatusThread implements Runnable
{ 
	public static void getUserStatus() throws ClassNotFoundException, SQLException, WeiboException, InterruptedException
	{
		int pageNum = 1;
		Connection conUser = PublicMethods.getConnection();
		java.sql.Statement stmt = conUser.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		ResultSet rset = stmt.executeQuery("select * from users");
		List<Long> userIdList = new ArrayList<Long>();
		long userId = (long)0;
		do
		{
			rset.next();
			userId = rset.getLong(1);	
			Long userIdLongValue = new Long(userId);
			userIdList.add(userIdLongValue);
		}
		while(!rset.isLast());
		Collections.shuffle(userIdList);
		for(Long userIdLong:userIdList)
		{ 
			userId = userIdLong.longValue();		
			Paging pag = new Paging(); 
			pag.setCount(200); 
			pageNum = 1; 
			do
			{
				pag.setPage(pageNum);
				List<Status> statuses = PublicMethods.weibo.getUserTimeline(userId+"",pag);
				if(statuses.isEmpty())
				{
					break;
				}
				else
				{
					StringBuilder ids = new StringBuilder(); 
					int statusNum = 0;
		        	int perRoundNum = 0;
		        	int roundNum = 0;
		        	int roundIndex = 0;	            
		            int statusIndex = 0;
		        	statusNum = statuses.size(); 

					//System.out.println("statuses NUm: "+statusNum);
		        	//这里分两组的考虑是因为取得评论数和转发数目是批量的，另外取得围脖最大一次200，对于批量取得评论数太大，所以分为两组。
		        	//分为两组，第一组100个，第二组100个或者statusNum-100个；除非statusNum<100,也就是最后一次取得微博数不足100个，则只有一组。
		        	if(statusNum < 100)
		        	{
		        		roundNum = 1;
		        		perRoundNum = statusNum;
		        	}
		        	else 
		        	{
		        		roundNum = 2;
		        		perRoundNum = 100;
		        	}		        	
		        	for(int i = 0; i < roundNum; i++)
		        	{
			        	List<Count> counts = null;
			        	ids = new StringBuilder(); 
		        		for(;roundIndex < perRoundNum; roundIndex++)
		        		{
			        		ids.append(statuses.get(roundIndex).getId()).append(',');	
		        		} 			
						//System.out.println("this is the "+i+"th round~~");       
						if(ids.length() != 0)
						{
				        	ids.deleteCharAt(ids.length() - 1); 
				        	counts = PublicMethods.weibo.getCounts(ids.toString());
						}
			            int rtCounts = 0;
			            int commentCounts = 0; 		
			            if(counts.isEmpty() == false)
			            { 
				            for(Count count: counts)
				            {	
					            rtCounts = (int) count.getRt();
					            commentCounts = (int)count.getComments();		
								//System.out.println("has count: "+"rtCounts: "+rtCounts+" commentCounts: "+commentCounts);
					            PublicMethods.InsertStatusSql(conUser, statuses.get(statusIndex), rtCounts, commentCounts); 	 
					            statusIndex++;
				            }
			            }
		        		if(roundNum == 2)
		        		{
		        			perRoundNum = statusNum; 
		        			statusIndex = 100;  //若为有二组的情况，status第二次也要从第二组的第一个开始，而不是第一组的第一个 
		        		}
		        	} 
					pageNum++;
				}
				Thread.sleep(2500);
			}
			while(true); 
			System.out.println("Oops, Empty~~");  
		} 
		conUser.close();
	}
	
	public void run()
	{ 
		try 
		{
			while(true)
			{
				getUserStatus();
			}
		} catch (ClassNotFoundException e) { 
			e.printStackTrace();
		} catch (SQLException e) { 
			e.printStackTrace();
		} catch (WeiboException e) { 
			e.printStackTrace();
		} catch (InterruptedException e) { 
			e.printStackTrace();
		}
	}
}

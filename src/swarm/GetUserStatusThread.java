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
	public static boolean getUserStatus() throws ClassNotFoundException, SQLException, WeiboException, InterruptedException
	{
		int pageNum = 1;
		boolean isDone = true;
		System.out.println("  Will connect to the database and get users.......");
		Connection conUser = PublicMethods.getConnection();
		java.sql.Statement stmt = conUser.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		System.out.println(" Okay the connnection to mysql has been established..we are going to select id from users.....");
		ResultSet rset = stmt.executeQuery("select id,statusesCount from users where isStatusDone = false limit 100;");
		long userId = (long)0;
		int statusesCount = 0;
		int sleepTime = 2700; 
		
		while(rset.next())
		{
			isDone = false;
			userId = rset.getLong(1);	
			statusesCount = rset.getInt(2);
			//System.out.print("userId: "+userId+" statusesCount: "+statusesCount);
			Paging pag = new Paging(); 
			pag.setCount(200); 
			pageNum = 1;  
			if(statusesCount == 0)
			{
				PublicMethods.UpdateUserStatus(conUser, userId);
				continue;
			}
			do
			{
				if(pageNum == 2)
				{
					PublicMethods.UpdateUserStatus(conUser, userId);
					break;
				}
				pag.setPage(pageNum);
				List<Status> statuses = PublicMethods.weibo.getUserTimeline(userId+"",pag);
				if(statuses.isEmpty())
				{
					PublicMethods.UpdateUserStatus(conUser, userId);
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
		        		sleepTime = 4700;
		        	}
		        	else 
		        	{
		        		roundNum = 2;
		        		perRoundNum = 100;
		        		sleepTime = 5700;
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
				        	int rtCounts = 0;
				            int commentCounts = 0; 		
				            if((counts != null) && (counts.isEmpty() == false))
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
						}
			         
		        		if(roundNum == 2)
		        		{
		        			perRoundNum = statusNum; 
		        			statusIndex = 100;  //若为有二组的情况，status第二次也要从第二组的第一个开始，而不是第一组的第一个 
		        		}
		        	} 
					pageNum++;
				}
				Thread.sleep(sleepTime);
			}
			while(true); 
			System.out.println("Oops, Empty~~");  
		}  
		conUser.close();
		return isDone;
	}
	
	public void run()
	{ 
		try 
		{
			while(true)
			{
				if(getUserStatus() == true)
				{
					break;
				}
			}
			System.out.println("The User's Status Crawling has finished~~~Lalalalalala");
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

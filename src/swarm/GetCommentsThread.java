package swarm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import weibo4j.Comment;
import weibo4j.Paging;
import weibo4j.WeiboException;

public class GetCommentsThread implements Runnable
{
	
	public static void getComments() throws ClassNotFoundException, SQLException, InterruptedException
	{
		do 
		{
			Connection conComments = PublicMethods.getConnection();  
			java.sql.Statement stmt = conComments.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			ResultSet rset = stmt.executeQuery("select * from status");
			int pageNum = 1;
			do 
			{
				try 
				{ 
					rset.next();
					Long statusId = rset.getLong(1);
					Paging pag = new Paging(); 
					pag.setCount(100); 
					pageNum = 1;
					do
					{
						pag.setPage(pageNum);
						List<Comment> comments = PublicMethods.weibo.getComments(statusId.toString(),pag);
						//List<Comment> comments = PublicMethods.weibo.getComments("3343531616094195",pag);
						
						pageNum++;
						if(comments.isEmpty())
						{
							break;
						}
						else
						{
							for (Comment comment : comments) 
							{
								PublicMethods.InsertCommentsSql(conComments, comment, statusId);
							} 
						}
						Thread.sleep(2000);
					}
					while(true);
				} catch (SQLException ex) {
					System.err.println("SQLException: " + ex.getMessage());
				} catch (WeiboException e) {
					e.printStackTrace();
				}
			}
			while(false);
			//while (!rset.isLast());
			conComments.close(); 
			Thread.sleep(3600000);
		} while (true);
	}
	
	public void run()
	{
		try 
		{
			getComments();
		} catch (ClassNotFoundException e) { 
			e.printStackTrace();
		} catch (SQLException e) { 
			e.printStackTrace();
		} catch (InterruptedException e) { 
			e.printStackTrace();
		}
	}
}

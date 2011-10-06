package swarm;
/**
 * 
 * @author swarm
 *
 */
import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
 

public class SinaCrawler 
{ 
	
	public static void main(String[] args) throws HttpException, IOException
	{ 
		PublicMethods.setWeibo();
		Thread getUsersThread = new Thread(new GetUsersThread()); 
		Thread getTrendsThread = new Thread(new GetTrendsThread()); 
		Thread getRelationshipThread = new Thread(new GetRelationshipThread()); 
		Thread getUserStatusThread = new Thread(new GetUserStatusThread());
		Thread getCommentsThread = new Thread(new GetCommentsThread());
		
		//getUserStatusThread.start();		
		//getUsersThread.start();
	    //getTrendsThread.start();		
		//getRelationshipThread.start();
		getCommentsThread.start();
		
	}
}

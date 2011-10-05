package swarm;

public class SinaCrawler 
{
	public static void main(String[] args)
	{
		Thread getUsersThread = new Thread(new GetUsersThread()); 
		Thread getTrendsThread = new Thread(new GetTrendsThread()); 
		Thread getRelationshipThread = new Thread(new GetRelationshipThread()); 
		
		getUsersThread.start();
		getTrendsThread.start();		
		getRelationshipThread.start();
	}
}

package swarm;
/**
 * 
 * @author swarm
 *
 */
import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import weibo4j.Weibo;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.awt.Toolkit;
import java.awt.Dimension;


public class SinaCrawler 
{ 
	  public SinaCrawler()
	    {
			JFrame crawlerFrame = new JFrame();
			crawlerFrame.setTitle("A Sina Weibo Crawler!"); 
			 
			Toolkit kit = Toolkit.getDefaultToolkit();    // 定义工具包
			Dimension screenSize = kit.getScreenSize();   // 获取屏幕的尺寸
			int screenWidth = screenSize.width/2;         // 获取屏幕的宽
			int screenHeight = screenSize.height/2;       // 获取屏幕的高
			int height = crawlerFrame.getHeight();
			int width = crawlerFrame.getWidth();
	    
			crawlerFrame.setLocation(screenWidth-width/2-300, screenHeight-height/2-322);
	 
	        setUpUIComponent(crawlerFrame);
			crawlerFrame.setVisible(true);
			crawlerFrame.addWindowListener(
		              new WindowAdapter(){
		                  public void windowClosing(WindowEvent e){
		                     System.exit(0);	
		                  }	
		              }	
		         );
	    }

	    private void setUpUIComponent(final JFrame f)
	    {
	        f.setSize(400, 440);   
	        f.setResizable(false);
	        Container container = f.getContentPane();
	        controlPanel = new JPanel();
			controlPanel.setBackground(new Color(232,232,232));
	        statusPanel = new JPanel();
	        Box baseBox=Box.createVerticalBox();
			controlPanel.setLayout(new BorderLayout());
			controlPanelLabel = new JLabel("                                 This is a simple sina weibo crawler~                      ");
			controlPanelLabel.setBackground(new Color(0,0,0));
			controlPanelLabel.setMaximumSize(new Dimension(400,66));
	        controlPanel.add(controlPanelLabel,BorderLayout.NORTH);
	        statusPanelLabel = new JLabel("  ");
	        
		
			statusPanel.setLayout(new BorderLayout());
			statusPanel.add(statusPanelLabel,BorderLayout.NORTH);
			statusArea = new JTextArea();
			statusArea.setBorder(BorderFactory.createTitledBorder("status of the crawler"));	
			statusArea.setEditable(false);
			
			baseBox.add(controlPanel);			
			baseBox.add(statusPanelLabel);
			baseBox.add(statusArea);
			
			container.add(baseBox);
 
			Box controlBox = Box.createHorizontalBox(); 
			Box controlOneVBox = Box.createVerticalBox();
			Box controlTwoVBox = Box.createVerticalBox();
			String[] account = {" Wang Heng's Account "," Cao Chen's Account"," Longwei Yu's Account"," Zhang Zhaochen's Account"," Ge Zhenghan's Account"};
			String[] crawlerString = {" All Crawlers"," Only User Crawler"," Only Status Crawler"," Only Relationship Crawler"," Only Comments Crawler"," Only Trends Crawler"};
			chooseAccountBox = new JComboBox<String>(account);
			chooseAccountBox.setMaximumSize(new Dimension(320,68));
			chooseAccountBox.setBorder(BorderFactory.createTitledBorder("select which one's account to use "));
			chooseCrawlerBox = new JComboBox<String>(crawlerString);
			chooseCrawlerBox.setMaximumSize(new Dimension(320,68));
			chooseCrawlerBox.setBorder(BorderFactory.createTitledBorder(" select which crawler to start "));

			paddingLabel = new JLabel("         ");

			controlOneVBox.add(chooseAccountBox);
			controlOneVBox.add(paddingLabel);
			paddingLabel = new JLabel("         ");
			controlOneVBox.add(paddingLabel);
			paddingLabel = new JLabel("         ");
			controlOneVBox.add(paddingLabel);
			controlOneVBox.add(chooseCrawlerBox);
			startButton = new JButton("asdfasdf");
			startButton.setMaximumSize(new Dimension(80,30));
			aboutButton = new JButton("About is this fro real");
			aboutButton.setMaximumSize(new Dimension(80,30));   
			exitButton = new JButton("Exit");
			exitButton.setMaximumSize(new Dimension(80,30)); 
			
			startButton.addActionListener(					
					new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							System.exit(0);	
						}
					})	;
			
			aboutButton.addActionListener(					
					new ActionListener()
					{
					         public void actionPerformed(ActionEvent e)
					         {
					        	 JOptionPane.showMessageDialog(f, "Email:   iswangheng@gmail.com", "About this crawler    @author swarm", 1);
					         }
					});
			
			exitButton.addActionListener(					
					new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							System.exit(0);	
						}
					})	;
			
			//controlTwoVBox.add(startButton);
			controlTwoVBox.add(paddingLabel);  
			controlTwoVBox.add(aboutButton);
			paddingLabel = new JLabel("         ");
			controlTwoVBox.add(paddingLabel);
			controlTwoVBox.add(exitButton);
			controlBox.add(controlOneVBox);
			controlBox.add(controlTwoVBox);
			controlPanel.add(controlBox,BorderLayout.CENTER);
			 
	    } 
	    
	    
	    public static void setAccess(int i)
	    {
	    	 switch (i)
	    	 {
		    	 case 1:
		    		 consumerKey = Access.AccessWH.consumerKey;
		    		 consumerSecret = Access.AccessWH.consumerSecret;
		    		 accessToken = Access.AccessWH.accessToken;
		    		 accessTokenSecret = Access.AccessWH.accessTokenSecret;
		    		 break;
		    	 case 2:
		    		 consumerKey = Access.AccessWHT.consumerKey;
		    		 consumerSecret = Access.AccessWHT.consumerSecret;
		    		 accessToken = Access.AccessWHT.accessToken;
		    		 accessTokenSecret = Access.AccessWHT.accessTokenSecret;
		    		 break;
		    	 case 3:
		    		 consumerKey = Access.AccessLWY.consumerKey;
		    		 consumerSecret = Access.AccessLWY.consumerSecret;
		    		 accessToken = Access.AccessLWY.accessToken;
		    		 accessTokenSecret = Access.AccessLWY.accessTokenSecret;
		    		 break;
		    	 case 4:
		    		 consumerKey = Access.AccessZZC.consumerKey;
		    		 consumerSecret = Access.AccessZZC.consumerSecret;
		    		 accessToken = Access.AccessZZC.accessToken;
		    		 accessTokenSecret = Access.AccessZZC.accessTokenSecret;
		    		 break;
		    	 case 5:
		    		 consumerKey = Access.AccessGZH.consumerKey;
		    		 consumerSecret = Access.AccessGZH.consumerSecret;
		    		 accessToken = Access.AccessGZH.accessToken;
		    		 accessTokenSecret = Access.AccessGZH.accessTokenSecret;
		    		 break;
		    	default:
		    		 consumerKey = Access.AccessGZH.consumerKey;
		    		 consumerSecret = Access.AccessGZH.consumerSecret;
		    		 accessToken = Access.AccessGZH.accessToken;
		    		 accessTokenSecret = Access.AccessGZH.accessTokenSecret;
		    		 break;
	    	 }
	    }
	    
		public static void main(String[] args) throws HttpException, IOException
		{ 
			setAccess(4);
			PublicMethods.setWeibo(consumerKey,consumerSecret,accessToken,accessTokenSecret);
			Thread getUsersThread = new Thread(new GetUsersThread()); 
			Thread getTrendsThread = new Thread(new GetTrendsThread()); 
			Thread getRelationshipThread = new Thread(new GetRelationshipThread()); 
			Thread getUserStatusThread = new Thread(new GetUserStatusThread());
			Thread getCommentsThread = new Thread(new GetCommentsThread());
			
			getUserStatusThread.start();		
			//getUsersThread.start();
			//System.out.println("consumer key: "+Weibo.CONSUMER_KEY+" secret: "+Weibo.CONSUMER_SECRET);
			//System.out.println("accessToken: "+accessToken+" accessTokenSecret: "+accessTokenSecret);
			
		    //getTrendsThread.start();		
			//getRelationshipThread.start();
			//getCommentsThread.start();
			// new SinaCrawler();
		}
		
	    private JLabel controlPanelLabel;
	    private JLabel statusPanelLabel;
	    private JPanel controlPanel; 
	    private JPanel statusPanel;
	    
	    private JTextArea statusArea;
	    
	    private JComboBox<String> chooseAccountBox;
	    private JComboBox<String> chooseCrawlerBox;
	    
	    private JButton startButton; 
	    private JButton exitButton;
	    private JButton aboutButton;
	    private JLabel paddingLabel;
	    
	    public static String consumerKey ="";
	    public static String consumerSecret ="";
	    public static String accessToken ="";
	    public static String accessTokenSecret ="";

}

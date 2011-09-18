package weibo4j.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import weibo4j.http.AccessToken;
import weibo4j.http.RequestToken;
import weibo4j.util.BareBonesBrowserLaunch;

public class OauthUpdate {
	public static void main(String[] args) {
		try {
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
			System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
            Weibo weibo = new Weibo();
			RequestToken requestToken = weibo.getOAuthRequestToken();
            
			System.out.println("Got request token.");
			System.out.println("Request token: "+ requestToken.getToken());
			System.out.println("Request token secret: "+ requestToken.getTokenSecret());
			AccessToken accessToken = null;

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			while (null == accessToken) {
				System.out.println("Open the following URL and grant access to your account:");
				System.out.println(requestToken.getAuthorizationURL());
				BareBonesBrowserLaunch.openURL(requestToken.getAuthorizationURL());
				System.out.print("Hit enter when it's done.[Enter]:");

				String pin = br.readLine();
				System.out.println("pin: " + br.toString());
				try{
					accessToken = requestToken.getAccessToken(pin);
				} catch (WeiboException te) {
					if(401 == te.getStatusCode()){
						System.out.println("Unable to get the access token.");
					}else{
						te.printStackTrace();
					}
				}
			}
			System.out.println("Got access token.");
			System.out.println("Access token: "+ accessToken.getToken());
			System.out.println("Access token secret: "+ accessToken.getTokenSecret());

			weibo.setToken(accessToken.getToken(), accessToken.getTokenSecret());

			Status status = weibo.updateStatus("1Q84!");
			System.out.println("Successfully updated the status to ["
					+ status.getText() + "].");

            try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(0);
		} catch (WeiboException te) {
			System.out.println("Failed to get timeline: " + te.getMessage());
			System.exit( -1);
		} catch (IOException ioe) {
			System.out.println("Failed to read the system input.");
			System.exit( -1);
		}
	}
}

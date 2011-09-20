package swarm;

/**
 * CREATE TABLE `status` (
 `id` bigint(20) NOT NULL,
 `userName` longtext,
 `userId` bigint(20) DEFAULT NULL,
 `createdAt` longtext,
 `text` longtext,
 `source` longtext,
 `isTruncated` tinyint(1) DEFAULT NULL,
 `inReplyToStatusId` bigint(20) DEFAULT NULL,
 `inReplyToUserId` bigint(20) DEFAULT NULL,
 `isFavorited` tinyint(1) DEFAULT NULL,
 `inReplyToScreenName` longtext,
 `latitude` double DEFAULT NULL,
 `longitude` double DEFAULT NULL,
 `thumbnail_pic` longtext,
 `bmiddle_pic` longtext,
 `original_pic` longtext,
 `mid` longtext,
 PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8
 *
 */

import java.util.List;
import java.util.Date;
import java.util.Calendar;

import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import weibo4j.http.AccessToken;
import weibo4j.http.RequestToken;
import java.sql.*;

public class GetPublicTimeline { 

	public static void getPublicTimeline() throws InterruptedException {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret",
				Weibo.CONSUMER_SECRET);

		try {
			Weibo weibo = new Weibo();
			weibo.setToken(Access.accessToken, Access.accessTokenSecret);
			do {
				List<Status> statuses = weibo.getPublicTimeline();
				for (Status status : statuses) {
					PublicMethods.InsertStatusSql(status);
				}
				Thread.currentThread();
				Thread.sleep(1000);
			} while (true); // just kidding
		} catch (WeiboException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws InterruptedException {
		getPublicTimeline();
	}
}

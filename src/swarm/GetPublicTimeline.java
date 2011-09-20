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
	public static boolean InsertSql(Status status) {
		try {
			String insql = "insert ignore into status(id,userName,userId,createdAt,text,source,isTruncated,inReplyToStatusId,inReplyToUserId,isFavorited,inReplyToScreenName,latitude,longitude,thumbnail_pic,bmiddle_pic,original_pic,mid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Connection con = PublicMethods.getConnection();
			PreparedStatement ps = con.prepareStatement(insql);
			ps.setLong(1, status.getId());
			ps.setString(2, status.getUser().getName());
			ps.setLong(3, status.getUser().getId());
			ps.setString(4, PublicMethods.dateToMySQLDateTimeString(status
					.getCreatedAt()));
			ps.setString(5, status.getText());
			ps.setString(6, status.getSource());
			ps.setBoolean(7, status.isTruncated());
			ps.setLong(8, status.getInReplyToStatusId());
			ps.setLong(9, status.getInReplyToUserId());
			ps.setBoolean(10, status.isFavorited());
			ps.setString(11, status.getInReplyToScreenName());
			ps.setDouble(12, status.getLatitude());
			ps.setDouble(13, status.getLongitude());
			ps.setString(14, status.getThumbnail_pic());
			ps.setString(15, status.getBmiddle_pic());
			ps.setString(16, status.getOriginal_pic());
			ps.setString(17, status.getMid());

			int result = ps.executeUpdate();
			con.close();
			if (result > 0)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

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
					InsertSql(status);
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

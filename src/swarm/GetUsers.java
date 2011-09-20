package swarm;

/**
 * 
 CREATE TABLE `users` (
 `id` bigint(20) NOT NULL,
 `screenName` varchar(45) DEFAULT NULL,
 `province` int(11) DEFAULT NULL,
 `city` int(11) DEFAULT NULL,
 `location` varchar(45) DEFAULT NULL,
 `description` varchar(45) DEFAULT NULL,
 `url` varchar(45) DEFAULT NULL,
 `profileImageUrl` varchar(45) DEFAULT NULL,
 `userDomain` varchar(45) DEFAULT NULL,
 `gender` varchar(45) DEFAULT NULL,
 `followersCount` int(11) DEFAULT NULL,
 `friendsCount` int(11) DEFAULT NULL,
 `statusesCount` int(11) DEFAULT NULL,
 `favouritesCount` int(11) DEFAULT NULL,
 `createdAt` date DEFAULT NULL,
 `verified` tinyint(1) DEFAULT NULL,
 `geoEnabled` tinyint(1) DEFAULT NULL,
 PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8
 */

/**
 * CREATE TABLE `relationship` (
 `id` bigint(20) NOT NULL AUTO_INCREMENT,
 `userId` bigint(20) DEFAULT NULL,
 `followerId` bigint(20) DEFAULT NULL,
 PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=1825 DEFAULT CHARSET=utf8
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import weibo4j.Comment;
import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.User;
import weibo4j.WeiboException;
import weibo4j.http.Response;

public class GetUsers {
	public static boolean InsertSql(User user) {
		try {
			String insql = "insert ignore into users(id,screenName,province,city"
					+ ",location,description,url,profileImageUrl"
					+ ",userDomain,gender,followersCount,friendsCount"
					+ ",statusesCount,favouritesCount,createdAt,verified,geoEnabled) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Connection conUsers = PublicMethods.getConnection();
			PreparedStatement ps = conUsers.prepareStatement(insql);
			ps.setLong(1, user.getId());
			ps.setString(2, user.getName());
			ps.setInt(3, user.getProvince());
			ps.setInt(4, user.getCity());
			ps.setString(5, user.getLocation());
			ps.setString(6, user.getDescription());
			String urlString = "";
			if (user.getURL() != null) {
				urlString = user.getURL().toString();
			}
			ps.setString(7, urlString);
			String profileImageURL = "";
			if (user.getProfileImageURL() != null) {
				profileImageURL = user.getProfileImageURL().toString();
			}
			ps.setString(8, profileImageURL);
			ps.setString(9, user.getUserDomain());
			ps.setString(10, user.getGender());
			ps.setInt(11, user.getFollowersCount());
			ps.setInt(12, user.getFriendsCount());
			ps.setInt(13, user.getStatusesCount());
			ps.setInt(14, user.getFavouritesCount());
			ps.setString(15, PublicMethods.dateToMySQLDateTimeString(user
					.getCreatedAt()));
			ps.setBoolean(16, user.isVerified());
			ps.setBoolean(17, user.isGeoEnabled());

			int result = ps.executeUpdate();
			conUsers.close();
			if (result > 0)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean InsertRelationshipSql(String userId, String followerId) {
		try {
			String insql = "insert ignore into relationship(userId,followerId) values(?,?)";
			Connection conRelationship = PublicMethods.getConnection();
			PreparedStatement ps = conRelationship.prepareStatement(insql);
			ps.setString(1, userId);
			ps.setString(2, followerId);
			int result = ps.executeUpdate();
			conRelationship.close();
			if (result > 0)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void getUsers(String userId) {
		try {
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
			System.setProperty("weibo4j.oauth.consumerSecret",
					Weibo.CONSUMER_SECRET);
			Weibo weibo = new Weibo();
			weibo.setToken(Access.accessToken, Access.accessTokenSecret);
			int cursor = 0;
			String followerId = "";
			String nextUserId = null;
			List<User> userList;
			int friendsNum = 0;
			do {
				Response res = weibo.getFollowersStatusesResponse(userId,
						cursor, 200);
				userList = User.constructUser(res);
				if (userList.size() == 0) {
					break;
				}
				for (User user : userList) {
					if (user != null) {
						friendsNum++;
						System.out.println(user.getName());
						followerId = user.getId() + "";
						InsertRelationshipSql(userId, followerId);
						InsertSql(user);
					}
				}
				cursor = weibo.getTmdNextCursor(res);
				Thread.currentThread();
				Thread.sleep(1000);
			} while (cursor != 0);
			System.out.println("size: " + userList.size() + " cursor: "
					+ cursor + " friendsNUm: " + friendsNum);
			if (userList.size() != 0) // will get the followers of the followers
										// using recursion
			{
				for (User user : userList) {
					nextUserId = user.getId() + "";
					getUsers(nextUserId);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		System.exit(0);
	}

	public static void startFromDatabase() throws SQLException,
			ClassNotFoundException {
		Connection con1 = PublicMethods.getConnection();
		java.sql.Statement stmt = con1.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rset = stmt.executeQuery("select * from status");
		do {
			try {
				rset.next();
				String userId = rset.getLong(3) + "";
				// System.out.println("userID: "+userId+"  row: "+rset.getRow());
				getUsers(userId);
			} catch (SQLException ex) {
				System.err.println("SQLException: " + ex.getMessage());
			}
		} while (!rset.isLast());
		con1.close();
	}

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {
		startFromDatabase();
	}
}

package swarm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

public class PublicMethods {
	public static Connection getConnection() throws SQLException,
			java.lang.ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");

		String url = "jdbc:mysql://localhost:3306/weibo";
		String username = "root";
		String password = "root";

		Connection con = DriverManager.getConnection(url, username, password);
		return con;
	}

	public static String dateToMySQLDateTimeString(Date date) {
		final String[] MONTH = { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
				"Jul", "Aug", "Sep", "Oct", "Nov", "Dec", };

		StringBuffer ret = new StringBuffer();
		String dateToString = date.toString(); // like
												// "Sat Dec 17 15:55:16 CST 2005"
		ret.append(dateToString.substring(24, 24 + 4));// append yyyy
		String sMonth = dateToString.substring(4, 4 + 3);
		for (int i = 0; i < 12; i++) { // append mm
			if (sMonth.equalsIgnoreCase(MONTH[i])) {
				if ((i + 1) < 10)
					ret.append("-0");
				else
					ret.append("-");
				ret.append((i + 1));
				break;
			}
		}

		ret.append("-");
		ret.append(dateToString.substring(8, 8 + 2));
		ret.append(" ");
		ret.append(dateToString.substring(11, 11 + 8));

		return ret.toString();
	}
}

// Class EmployeeList displays Employees from the table EMP 
// using JDBC drivers of type 4

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

class News {

	public static Connection conn = null;

	public static void initConn() throws SQLException {
		Properties connectionProps = new Properties();
		connectionProps.put("user", "root");

		News.conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/task2", connectionProps);
	}

	public static ResultSet getNoPublished() {
		PreparedStatement stmt = null;

		try {

			// Build an SQL String
			stmt = conn
					.prepareStatement("SELECT * from News WHERE is_published=?");
			stmt.setInt(1, 0);
			ResultSet result = stmt.executeQuery();

			return result;

		} catch (SQLException se) {
			System.out.println("SQLError: " + se.getMessage() + " code: "
					+ se.getErrorCode());

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return null;

	}

	public static void main(String argv[]) throws SQLException {

		ResultSet noPublished = null;
		try {
			News.initConn();
			conn.setAutoCommit(false);

			noPublished = News.getNoPublished();
			if (noPublished == null) {
				System.out.println("Sorry havn't noPublished News");
				System.exit(1);
			}
			Statement stmt = conn.createStatement();

			while (noPublished.next()) {
				int id = noPublished.getInt("id");
				stmt.addBatch("DELETE FROM Files WHERE news_id=" + id);
			}
			stmt.executeBatch();
			conn.commit();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			conn.rollback();
			conn.close();
		}
		noPublished.close();
	}

}

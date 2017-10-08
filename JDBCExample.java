package PizzaApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCExample {
	/**
	 * @author Wim Thiels 
	 * 			The following table contains the default values for
	 *         logging on to the MySQL database. IP address of the database
	 *         server 127.0.0.1 
	 *         Port 3306 
	 *         Username root 
	 *         Password
	 */

	static {
		try {
			/* Type 4 Driver */
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Could not load MySql driver.");
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}

	public static void main(String args[]) {
		String uname = null;
		String psswrd = null;
		Integer choice = 1;
		/* Location of the database */
		String host = "jdbc:mysql://localhost/orderdb";
		/* Sample query */
		String query = "SELECT * FROM customer";

		/* Reading log-in data (username and password) */
		try {
			BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Enter your username on MySql: ");
			uname = br1.readLine();
			BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Enter your password on MySql: ");
			psswrd = br2.readLine();
		} catch (IOException e) {
			System.out.print("Failed to get uname/passwd");
			System.out.println(":" + e.getMessage());
			System.exit(1);
		}

		/* Example of querying a database */
		try {
			/* Connect to MySql database */
			java.sql.Connection conn = DriverManager.getConnection(host, uname, psswrd);
			System.out.println("Connection established...");
			System.out.println();
			/* Create statement */
			java.sql.Statement stmt = conn.createStatement();
			/* Execute the query */
			ResultSet rs = stmt.executeQuery(query);
			/* Output */
			System.out.println("This is an example how you query a DBMS.");
			System.out.println();
			System.out.println(query);
			System.out.println("ID // First Name // Last Name");
			System.out.println("------------------------------");
			while (rs.next()) {
				System.out.print(rs.getString(1));
				System.out.print((" // "));
				System.out.print(rs.getString(2));
				System.out.print((" // "));
				System.out.print(rs.getString(3));
				System.out.print((" // "));
				System.out.println(rs.getString(4));
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception: ");
			System.err.println(e.getMessage());
		}
		
		/* Example of choice options */
		while (choice != 0) {
			/* Ask for user's choice */
			try {
				BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
				System.out.println();
				System.out.println("This is an example of a choice menu.");
				System.out.println();
				System.out.println("Please choose between the following options:");
				System.out.println(" (1) Add registration");
				System.out.println(" (2) Show popular sessions list");
				System.out.println(" (0) Quit");
				System.out.print("Enter your choice: ");
				choice = Integer.parseInt(br1.readLine());
			} catch (NumberFormatException ex) {
				System.err.println("Not a valid number");
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (choice == 1) {
				/* TODO */
			} else if (choice == 2) {
				/* TODO */
			} else {
				/* TODO */
			}
		}
		System.out.println();
		System.out.println("End of Session");
	}
}

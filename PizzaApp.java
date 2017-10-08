package PizzaApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PizzaApp {
	/**
	 * @author Wim Thiels 
	 *         studentnumber : S0049019
	 */
	private static String uname = null;
	private static String psswrd = null;
	private static String host = "jdbc:mysql://localhost/orderdb";
	private static String query;
	private static int orderMax = 0;
	private static java.sql.Connection conn;
	private static java.sql.Statement stmt1;
	private static java.sql.PreparedStatement stmt2;
	private static java.sql.PreparedStatement stmt3;
	private static java.sql.PreparedStatement stmt4;
	private static java.sql.PreparedStatement stmt5;
	private static java.sql.PreparedStatement stmt6;
	private static java.sql.PreparedStatement stmt7;
	private static ResultSet rs1;

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

		Integer choice = 1;

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


		try {
			/* Connect to MySql database */
			conn = DriverManager.getConnection(host, uname, psswrd);
			System.out.println("Connection established...");
			System.out.println();

			while (choice != 0) {
				/* Ask for user's choice */
				try {
					BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
					System.out.println();
					System.out.println("Please choose between the following options:");
					System.out.println(" (1) Order food");
					System.out.println(" (2) Show popular categories");
					System.out.println(" (3) Show popular products");
					System.out.println(" (0) Quit");
					System.out.print("Enter your choice: ");
					choice = Integer.parseInt(br1.readLine());
				} catch (NumberFormatException ex) {
					System.err.println("Not a valid number");
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (choice == 1) {
					orderFood();
				} else if (choice == 2) {
					showPopCateg();
				} else if (choice == 3) {
					showPopProd();
				} else if (choice == 0) {
				} else {
					System.err.println("Wrong input. Choice should be 0, 1, 2 or 3");
				}
			}
			// closing DB-statements
			System.out.println("closing DB connections...");
			if (stmt1 != null)
				stmt1.close();
			if (stmt2 != null)
				stmt2.close();
			if (stmt3 != null)
				stmt3.close();
			if (stmt4 != null)
				stmt4.close();
			if (stmt5 != null)
				stmt5.close();
			if (stmt6 != null)
				stmt6.close();
			if (stmt7 != null)
				stmt7.close();

			conn.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception: ");
			System.err.println(e.getMessage());
		}
		System.out.println();
		System.out.println("End of Session");
	}

	private static void orderFood() throws SQLException {

		// Step 1 : USER-INPUT -> get customerID
		// --------------------------------------
		// I assume that the user always needs to enter the user-id for every order. so the same session can be used to order for multiple people
		int custID = 0;
		ResultSet rs4 = null;
		while (rs4 == null || !rs4.next()) {
			if (rs4 != null)
				System.out.println("Not a correct customerID. Please try again...");
			try {
				BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
				System.out.print("What is your customer ID ? : ");
				custID = Integer.parseInt(br1.readLine());
			} catch (IOException e) {
				System.out.print("Failed to get CustomerID");
				System.out.println(":" + e.getMessage());
				System.exit(1);
			} catch (NumberFormatException e) {
			}
			// check if user id is present on the DB
			if (stmt4 == null)
				stmt4 = conn.prepareStatement("select 1 from customer where customer_id = ?");
			stmt4.setInt(1, custID);
			rs4 = stmt4.executeQuery();
		}
		rs4.close();

		// Step 2 : show product list (ordered)
		// --------------------------------------
		if (stmt1 == null) {
			query = "select * from product order by category, name";
			java.sql.Statement stmt1 = conn.createStatement();
			rs1 = stmt1.executeQuery(query);
		}
		/* Output */
		System.out.println();
		System.out.print(String.format("%1$5s", "ID"));
		System.out.print(("  -   "));
		System.out.print(String.format("%1$20s", "Product Name"));
		System.out.print(("  -   "));
		System.out.println(String.format("%1$8s", "Category"));
		System.out.println("-------------------------------------------------");
		rs1.absolute(0);
		while (rs1.next()) {
			System.out.print(String.format("%1$5s", rs1.getString(1)));
			System.out.print(("  -   "));
			System.out.print(String.format("%1$20s", rs1.getString(2)));
			System.out.print(("  -   "));
			System.out.println(String.format("%1$8s", rs1.getString(3)));
		}

		// Step 3 : USERINPUT -> get productID of the order
		// -------------------------------------------------
		int prodID = 0;
		ResultSet rs5 = null;
		while (rs5 == null || !rs5.next()) {
			if (rs5 != null)
				System.out.println("Not a correct productID. Please try again...");
			try {
				BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
				System.out.print("Which product do you want to order ? ");
				prodID = Integer.parseInt(br1.readLine());
			} catch (IOException e) {
				System.out.print("Failed to get productID");
				System.out.println(":" + e.getMessage());
				System.exit(1);
			} catch (NumberFormatException e) {
			}
			// check if prod id is present on the DB
			if (stmt5 == null)
				stmt5 = conn.prepareStatement("select 1 from product where product_id = ?");
			stmt5.setInt(1, prodID);
			rs5 = stmt5.executeQuery();
		}
		rs5.close();

		// Step 4 : Get ordered product with the highest rating
		// -----------------------------------------------------
		String orderedProdName = " ";
		String restName = " ";
		String restRating = " ";
		if (stmt2 == null)
			stmt2 = conn.prepareStatement("select p.name, r.name, o.rating " 
					+ "from offers o, product p, restaurant r "
					+ "where o.product_id = p.product_id AND o.rest_id = r.rest_id AND o.product_id = ? "
					+ "order by o.rating DESC");
		stmt2.setInt(1, prodID);
		ResultSet rs2 = stmt2.executeQuery();

		if (rs2.next()) {
			orderedProdName = rs2.getString(1);
			restName = rs2.getString(2);
			restRating = rs2.getString(3);
		} else {
			System.out.print("Sorry, that product is currently not offered by any restaurant.");
			return;
		}
		rs2.close();

		// Step 5 : get the last order-id and prepare statement (first time only)
		// ---------------------------------------------------------------------

		if (orderMax == 0) {
			java.sql.Statement stmt = conn.createStatement();
			query = "select MAX(order_id) from orders";
			ResultSet rs3 = stmt.executeQuery(query);

			if (rs3.next()) {        
				orderMax = Integer.parseInt(rs3.getString(1));
			}
			// if no orders found, then orderMax stays 0 = ok
			
			rs3.close();
			stmt.close();
		}

		// Step 6 : insert the order in the DB
		// -----------------------------------
		if (stmt3 == null)
			stmt3 = conn
					.prepareStatement("insert into orders (order_id, customer_id, product_id) VALUES " + "(?,?,?);");
		stmt3.setInt(1, orderMax + 1);
		stmt3.setInt(2, custID);
		stmt3.setInt(3, prodID);

		orderMax += stmt3.executeUpdate(); // will be effectively the same as orderMax++

		/* Output */
		System.out.println();
		System.out.println("Your order will be delivered to the following restaurant : ");
		System.out.print(String.format("%1$20s", "Product Name"));
		System.out.print(("  -   "));
		System.out.print(String.format("%1$20s", "Restaurant Name"));
		System.out.print(("  -   "));
		System.out.println(String.format("%1$8s", "Rating"));
		System.out.println("-----------------------------------------------------------");
		System.out.print(String.format("%1$20s", orderedProdName));
		System.out.print(("  -   "));
		System.out.print(String.format("%1$20s", restName));
		System.out.print(("  -   "));
		System.out.println(String.format("%1$8s", restRating));

	}

	private static void showPopCateg() throws SQLException {
		// prepared statement is preferred just to avoid multiple bindings of the same statement if this option is selected multiple times
		ResultSet rs6 = null;
		if (stmt6 == null) 
			stmt6 = conn.prepareStatement("select a.category, count(*) " 
					+ "from product a, orders b " 
					+ "where a.product_id = b.product_id "
					+ "group by a.category " 
					+ "having count(*) > 4 " 
					+ "order by count(*) DESC, a.category ");
		rs6 = stmt6.executeQuery();
			

		/* Output */
		System.out.println();
		System.out.println("The most popular food categories at the moment are : ");
		System.out.print(String.format("%1$20s", "Product Name"));
		System.out.print(("  -   "));
		System.out.println(String.format("%1$8s", "Count Orders"));
		System.out.println("----------------------------------------------");
		int counter = 0;
		while (rs6.next()) {
			counter++;
			System.out.print(String.format("%1$20s", rs6.getString(1)));
			System.out.print(("  -   "));
			System.out.println(String.format("%1$8s", rs6.getString(2)));
		}
		if (counter == 0)
			System.out.println("Currently no categories with at least 5 orders.");

		rs6.close();

	}

	private static void showPopProd() throws SQLException {
		// prepared statement is preferred just to avoid multiple bindings of the same statement if this option is selected multiple times
		ResultSet rs7 = null;
		if (stmt7 == null) 
			stmt7 = conn.prepareStatement("select a.product_id, a.name, count(*)  " 
					+ "from product a, orders b "
					+ "where a.product_id = b.product_id " 
					+ "group by a.product_id " 
					+ "order by count(*) DESC, a.name "
					+ "limit 5");
		rs7 = stmt7.executeQuery();
			

		/* Output */
		System.out.println();
		System.out.println("The most popular products at the moment are : ");
		System.out.print(String.format("%1$5s", "ID"));
		System.out.print(("  -   "));
		System.out.print(String.format("%1$20s", "Product Name"));
		System.out.print(("  -   "));
		System.out.println(String.format("%1$8s", "Count Orders"));
		System.out.println("----------------------------------------------");
		int counter = 0;
		while (rs7.next()) {
			counter++;
			System.out.print(String.format("%1$5s", rs7.getString(1)));
			System.out.print(("  -   "));
			System.out.print(String.format("%1$20s", rs7.getString(2)));
			System.out.print(("  -   "));
			System.out.println(String.format("%1$8s", rs7.getString(3)));
		}
		if (counter == 0)
			System.out.println("Currently no orders in table.");

		rs7.close();
	}

}

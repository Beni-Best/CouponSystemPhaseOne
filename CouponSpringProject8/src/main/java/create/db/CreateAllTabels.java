package create.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CreateAllTabels {
	public static void main(String[] args) {
		String url = "jdbc:derby://localhost:1527/CouponSystemDB;create = true";

		String sql1 = "CREATE TABLE Company(ID BIGINT not null PRIMARY KEY GENERATED ALWAYS AS IDENTITY  (START WITH 1, INCREMENT BY 1), COMP_NAME VARCHAR(40), PASSWORD VARCHAR(40), EMAIL VARCHAR(40))";
		String sql2 = "CREATE TABLE Company_Coupon(COMP_ID BIGINT, COUPON_ID BIGINT, PRIMARY KEY(COMP_ID, COUPON_ID))";
		String sql3 = "CREATE TABLE Coupon(ID BIGINT not null PRIMARY KEY GENERATED ALWAYS AS IDENTITY  (START WITH 1, INCREMENT BY 1), TITLE VARCHAR(40), START_DATE DATE, END_DATE DATE, AMOUNT INTEGER, TYPE VARCHAR(30), MESSAGE VARCHAR(40), PRICE FLOAT, IMAGE VARCHAR(300))";
		String sql4 = "CREATE TABLE Customer_Coupon(CUST_ID BIGINT, COUPON_ID BIGINT, PRIMARY KEY(CUST_ID, COUPON_ID))";
		String sql5 = "CREATE TABLE Customer(ID BIGINT not null PRIMARY KEY GENERATED ALWAYS AS IDENTITY  (START WITH 1, INCREMENT BY 1), CUST_NAME VARCHAR(40), PASSWORD VARCHAR(40))";
		ArrayList<String> sqlList = new ArrayList<>();
		sqlList.add(sql1);
		sqlList.add(sql2);
		sqlList.add(sql3);
		sqlList.add(sql4);
		sqlList.add(sql5);

		try (Connection con = DriverManager.getConnection(url);) {
			System.out.println("connected");
			Statement stmt = con.createStatement();
			for (String string : sqlList) {
				stmt.executeUpdate(string);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("disconnected");
	}
}

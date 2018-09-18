package test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import coupon.sys.core.connectionPool.ConnectionPool;
import coupon.sys.core.exceptions.CouponSystemException;

public class ViewAllTables {
	public static void main(String[] args) throws CouponSystemException, SQLException {

		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();

		String sql1 = "Select * from Company";
		String sql2 = "Select * from Company_Coupon";
		String sql3 = "Select * from Coupon";
		String sql4 = "Select * from Customer_Coupon";
		String sql5 = "Select * from Customer";

		ArrayList<String> sqlList = new ArrayList<>();
		sqlList.add(sql1);
		sqlList.add(sql2);
		sqlList.add(sql3);
		sqlList.add(sql4);
		sqlList.add(sql5);

		Statement stmt = con.createStatement();
		for (String string : sqlList) {
			ResultSet rs = stmt.executeQuery(string);
			ResultSetMetaData rsMeta = rs.getMetaData();
			System.out.println(rsMeta.getTableName(1));
			for (int i = 1; i <= rsMeta.getColumnCount(); i++) {
				System.out.print(rsMeta.getColumnLabel(i) + " ");
			}
			System.out.println();
			
			
			while (rs.next()) {
				for (int i = 0; i <rs.getMetaData().getColumnCount(); i++) {
					System.out.print(rs.getObject(i+1)+" ");
				}
				System.out.println();
			}
			
			

			System.out.println("--------------------------------------------------------------------------");
		}

	}
}

package coupon.sys.core.dao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import coupon.sys.core.beans.Company;
import coupon.sys.core.beans.Coupon;
import coupon.sys.core.beans.CouponType;
import coupon.sys.core.beans.Customer;
import coupon.sys.core.connectionPool.ConnectionPool;
import coupon.sys.core.exceptions.CouponSystemException;

/**
 * this is a helper class , usually to work with COMPANY_COUPON and CUSTOMER_COUPON (Connections)
 * 
 * @author Beni
 *
 */
public class JoinedTableDaoDb {

	/**
	 * delete coupon from company_coupon and customer_coupon tables.
	 */
	public void deleteCoupon(Coupon coupon) throws CouponSystemException {

		String sql = "DELETE FROM Company_Coupon WHERE Coupon_ID=?";
		String sql2 = "DELETE FROM Customer_Coupon WHERE Coupon_ID=?";
		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();
		try {
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setLong(1, coupon.getId());
			statement.execute();

			PreparedStatement statement2 = con.prepareStatement(sql2);
			statement2.setLong(1, coupon.getId());
			statement2.execute();
		} catch (SQLException e) {
			throw new CouponSystemException("delete Coupon Failed", e);
		} finally {
			cp.returnConnection(con);
		}
	}

	/**
	 * delete specific customer and coupon in customer_coupon table from
	 * database
	 */
	public void deleteCustomer_Coupon(Customer customer, Coupon coupon) throws CouponSystemException {
		String sql = "DELETE FROM Customer_coupon WHERE Customer_ID=? and Coupon_ID=?";
		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();
		try {
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setLong(1, customer.getId());
			statement.setLong(2, coupon.getId());
			statement.execute();
		} catch (SQLException e) {
			throw new CouponSystemException("delete failed!");
		} finally {
			cp.returnConnection(con);
		}
	}
	
	/**
	 * delete customer and all of its coupon purchase history from
	 * Customer_Coupon table
	 */
	
	public void deleteCustomerPurchaseHistory(Customer customer) throws CouponSystemException {

		String sql = "DELETE FROM Customer_Coupon WHERE Customer_ID=?";
		
		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();
		
		try {
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setLong(1, customer.getId());
			statement.execute();
		} catch (SQLException e) {
			throw new CouponSystemException("Delete Customer Purchase History from Customer_Coupon table failed!" ,e);
		} finally {
			cp.returnConnection(con);
		}
	}
	
	/**
	 * add new customer and coupon for customer_coupon table.
	 */
	public void customerPurchaseCoupon(Customer customer, Coupon coupon) throws CouponSystemException {

		String sql = "INSERT INTO Customer_Coupon VALUEs (?,?)";
		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();
		try {
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setLong(1, customer.getId());
			statement.setLong(2, coupon.getId());
			statement.execute();

		} catch (SQLException e) {
			throw new CouponSystemException("customer Purchase Coupon error " , e);
		} finally {
			cp.returnConnection(con);
		}

	}
	
	/**
	 * add new Company and coupon for Company_Coupon table.
	 */
	public void companyCreateCoupon(Company company, Coupon coupon) throws CouponSystemException {

		String sql = "INSERT INTO Company_Coupon VALUEs (?,?)";
		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();
		try {
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setLong(1, company.getId());
			statement.setLong(2, coupon.getId());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new CouponSystemException("creating a new Company_coupon row failed!" ,e);
		} finally {
			cp.returnConnection(con);
		}

	}
	/**
	 * return true if there is a connection between a Company and Coupon
	 * else return false
	 * 
	 * */
	public boolean checkCompanyCouponConnect(Company company,Coupon coupon) throws CouponSystemException {
		Collection<Coupon>coupons = new ArrayList<>();
		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();
		
		String sql = "Select * from COUPON join COMPANY_COUPON on COMPANY_COUPON.COUPON_ID=COUPON.ID where  COMP_ID=? ";
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, company.getId());
			ResultSet rs = pstmt.executeQuery();
			
			//get all coupons of the company
			while (rs.next()) {
				coupons.add(new Coupon(rs.getLong(1), rs.getString(2), rs.getDate(3), rs.getDate(4), rs.getInt(5),
						CouponType.valueOf(rs.getString(6)), rs.getString(7), rs.getDouble(8), rs.getString(9)));
			}
			//final check  
			for (Coupon coup : coupons) {
				if (coup.getId()==coupon.getId()) {
					return true;
				}
			}
			
		} catch (SQLException e) {
			throw new CouponSystemException("chek company coupon connect error ", e);
		}finally {
			cp.returnConnection(con);
		}
		
		return false;
	}
	/**
	 * return true if there is a connection between a customer and Coupon
	 * else return false
	 * */
	public boolean checkCustomerCouponConnect(Customer customer,Coupon coupon) throws CouponSystemException {
		Collection<Coupon>coupons = new ArrayList<>();
		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();
		
		String sql = "Select * from COUPON join CUSTOMER_COUPON on CUSTOMER_COUPON.COUPON_ID=COUPON.ID where  CUST_ID=? ";
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, customer.getId());
			ResultSet rs = pstmt.executeQuery();
			
			//get all coupons of the customer
			while (rs.next()) {
				coupons.add(new Coupon(rs.getLong(1), rs.getString(2), rs.getDate(3), rs.getDate(4), rs.getInt(5),
						CouponType.valueOf(rs.getString(6)), rs.getString(7), rs.getDouble(8), rs.getString(9)));
			}
			//final check  
			for (Coupon coup : coupons) {
				if (coup.getId()==coupon.getId()) {
					return true;
				}
			}
			
		} catch (SQLException e) {
			throw new CouponSystemException("chek company coupon connect error ", e);
		}finally {
			cp.returnConnection(con);
		}
		
		return false;
	}
	
	/**
	 * return all customers from specific company
	 */
	public Collection<Customer> getAllCompaniesCustomers(Company company) throws CouponSystemException {
		Collection<Customer> customers = new ArrayList<>();
		Collection<Coupon> coupons = new ArrayList<>();
		CompanyDaoDb compDao = new CompanyDaoDb();

		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();

		Company tmpComp = new Company();
		tmpComp = compDao.getCompany(company.getName());

		if (tmpComp == null) {
			throw new CouponSystemException("There is no such company");
		}

		try {
			// get all customers that own those coupons
			String sql = "Select * from CUSTOMER where id in(Select CUST_ID from CUSTOMER_COUPON where COUPON_ID = ?)";

			coupons = compDao.getCoupons(tmpComp.getId());
			for (Coupon coupon : coupons) {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setLong(1, coupon.getId());
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					customers.add(new Customer(rs.getLong(1), rs.getString(2), rs.getString(3)));
				}

			}
		} catch (SQLException e) {
			throw new CouponSystemException("Get all customers error", e);
		} finally {
			cp.returnConnection(con);
		}

		return customers;

	}

}

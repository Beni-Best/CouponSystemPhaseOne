package coupon.sys.core.dao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import coupon.sys.core.beans.Company;
import coupon.sys.core.beans.Coupon;
import coupon.sys.core.beans.CouponType;
import coupon.sys.core.beans.Customer;
import coupon.sys.core.connectionPool.ConnectionPool;
import coupon.sys.core.dao.CustomerDao;
import coupon.sys.core.exceptions.CouponSystemException;

public class CustomerDaoDb implements CustomerDao {

	/**
	 * Creates new customer in the database
	 */
	@Override
	public void createCustomer(Customer customer) throws CouponSystemException {

		// get connection from Connection pool
		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();

		String sql = "INSERT INTO CUSTOMER (CUST_NAME, PASSWORD) VALUES (?, ?)";

		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, customer.getName());
			pstmt.setString(2, customer.getPassword());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new CouponSystemException("create customer error", e);
		} finally {
			cp.returnConnection(con);
		}
	}

	/**
	 * removes new customer in the database
	 */
	@Override
	public void removeCustomer(Customer customer) throws CouponSystemException {

		// get connection from Connection pool
		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();

		String sql = "Delete from customer WHERE id = ?";

		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, customer.getId());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw new CouponSystemException("remove customer error", e);
		} finally {
			cp.returnConnection(con);
		}

	}

	/**
	 * Updates customer in the database
	 */
	@Override
	public void updateCustomer(Customer customer) throws CouponSystemException {

		// get connection from Connection pool
		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();

		String sql = "UPDATE customer SET cust_name = ?, password = ? WHERE id = ?";

		try {
			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(1, customer.getName());
			pstmt.setString(2, customer.getPassword());
			pstmt.setLong(3, customer.getId());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw new CouponSystemException("update customer error", e);
		} finally {
			cp.returnConnection(con);
		}

	}

	/**
	 * return customer by id from the database or return null if there is no
	 * such customer
	 */
	@Override
	public Customer getCustomer(long id) throws CouponSystemException {
		Customer customer = new Customer();

		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con1 = cp.getConnection();

		String sql = "Select * from Customer Where id = ?";

		try {

			PreparedStatement pstmt = con1.prepareStatement(sql);
			pstmt.setLong(1, id);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				customer.setId(rs.getLong(1));
				customer.setName((rs.getString(2)));
				customer.setPassword(rs.getString(3));
			} else {
				return null;
			}

			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			throw new CouponSystemException("Get Customer error", e);
		} finally {
			cp.returnConnection(con1);
		}
		return customer;
	}

	/**
	 * return customer by Name from the database or return null if there is no
	 * such customer
	 */
	@Override
	public Customer getCustomer(String custName) throws CouponSystemException {
		Customer customer = new Customer();

		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con1 = cp.getConnection();

		String sql = "Select * from Customer Where CUST_NAME = ?";

		try {

			PreparedStatement pstmt = con1.prepareStatement(sql);
			pstmt.setString(1, custName);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				customer.setId(rs.getLong(1));
				customer.setName((rs.getString(2)));
				customer.setPassword(rs.getString(3));
			} else {
				return null;
			}

			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			throw new CouponSystemException("Get Customer error", e);
		} finally {
			cp.returnConnection(con1);
		}

		return customer;
	}

	/**
	 * return all customer collection from the database
	 */
	@Override
	public Collection<Customer> getAllCustomers() throws CouponSystemException {
		Collection<Customer> customers = new ArrayList<>();

		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con1 = cp.getConnection();

		String sql = "Select * from customer";

		try {

			Statement stmt = con1.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				customers.add(new Customer(rs.getLong(1), rs.getString(2), rs.getString(3)));
			}
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			throw new CouponSystemException("Get all customers error", e);
		} finally {
			cp.returnConnection(con1);
		}

		return customers;
	}

	/**
	 * return all customers of the specific company from the database
	 */
	public Collection<Customer> getAllCustomers(Company company) throws CouponSystemException {
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

	/**
	 * Return all coupons of the specific customer id
	 */
	@Override
	public Collection<Coupon> getCoupons(long custId) throws CouponSystemException {
		Collection<Coupon> coupons = new ArrayList<>();

		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();

		String sql = "Select * from Coupon Where id IN(select COUPON_ID from CUSTOMER_COUPON where CUST_ID =?)";

		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, custId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				coupons.add(new Coupon(rs.getLong(1), rs.getString(2), rs.getDate(3), rs.getDate(4), rs.getInt(5),
						CouponType.valueOf(rs.getString(6)), rs.getString(7), rs.getDouble(8), rs.getString(9)));
			}
		} catch (SQLException e) {
			throw new CouponSystemException("Get coupons error", e);
		} finally {
			cp.returnConnection(con);
		}
		return coupons;
	}

	/**
	 * removes connection between customer and coupon from CUSTOMER_COUPON table
	 */
	public void removeCustomerCoupon(Customer customer) throws CouponSystemException {
		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();

		String sql = "Delete from CUSTOMER_COUPON where CUST_ID =?";
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, customer.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new CouponSystemException("Remove CUSTOMER_COUPON error", e);
		} finally {
			cp.returnConnection(con);
		}

	}

	/**
	 * return true if password and name is correct. 
	 * return false if there is no cust_name with this comp_name or the password is wrong
	 */
	@Override
	public boolean login(String cust_name, String password) throws CouponSystemException {
		Customer customer = new Customer();

		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con1 = cp.getConnection();

		String sql = "Select * from Customer Where CUST_NAME = ?";

		try {

			PreparedStatement pstmt = con1.prepareStatement(sql);
			pstmt.setString(1, cust_name);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				customer.setId(rs.getLong(1));
				customer.setName((rs.getString(2)));
				customer.setPassword(rs.getString(3));
			} else {
				return false;
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			throw new CouponSystemException("login error", e);
		} finally {
			cp.returnConnection(con1);
		}

		if (customer.getPassword().equals(password)) {
			return true;
		} else {
			return false;
		}

	}
}

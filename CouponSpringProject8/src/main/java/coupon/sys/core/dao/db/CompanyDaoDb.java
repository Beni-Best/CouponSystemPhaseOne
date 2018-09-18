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
import coupon.sys.core.connectionPool.ConnectionPool;
import coupon.sys.core.dao.CompanyDao;
import coupon.sys.core.exceptions.CouponSystemException;

public class CompanyDaoDb implements CompanyDao {

	/**
	 * Creates new company in the database
	 */
	@Override
	public void create(Company company) throws CouponSystemException {
		// get connection from pool
		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();
		System.out.println("took 1 connection");
		// create an sql string
		String sql = "INSERT INTO COMPANY (COMP_NAME, PASSWORD ,EMAIL) VALUES (?, ?, ?)";
		try {
			// get a statement from the connection
			PreparedStatement pstmt = con.prepareStatement(sql);
			// execute the sql command

			pstmt.setString(1, company.getName());
			pstmt.setString(2, company.getPassword());
			pstmt.setString(3, company.getEmail());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw new CouponSystemException("create coupon error", e);

		} finally {
			// return connection to pool
			cp.returnConnection(con);
		}

	}

	/***
	 * @author Beni
	 * 
	 *         removes company from the database
	 */
	@Override
	public void removeCompany(Company company) throws CouponSystemException {
		// get connection to the pool
		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con1 = cp.getConnection();

		String sql = "DELETE from company WHERE id=?";
		try {
			PreparedStatement pstmt = con1.prepareStatement(sql);
			// execute
			pstmt.setLong(1, company.getId());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new CouponSystemException("Remove company error", e);
		} finally {
			cp.returnConnection(con1);
		}

	}

	/**
	 * Updates Company in the database
	 */
	@Override
	public void updateCompany(Company company) throws CouponSystemException {

		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con1 = cp.getConnection();

		String sql = "UPDATE company SET comp_name = ?, password = ?, email=? WHERE id = ?";
		try {
			PreparedStatement pstmt = con1.prepareStatement(sql);

			pstmt.setString(1, company.getName());
			pstmt.setString(2, company.getPassword());
			pstmt.setString(3, company.getEmail());
			pstmt.setLong(4, company.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new CouponSystemException("can't update company", e);
		} finally {
			cp.returnConnection(con1);
		}
	}

	/**
	 * return company by id from the database , or return null if there is no
	 * such company
	 */
	@Override
	public Company getCompany(long id) throws CouponSystemException {
		Company company = new Company();

		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con1 = cp.getConnection();

		String sql = "Select * from Company Where id = ?";

		try {

			PreparedStatement pstmt = con1.prepareStatement(sql);
			pstmt.setLong(1, id);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {

				company.setId(rs.getLong(1));
				company.setName(rs.getString(2));
				company.setPassword(rs.getString(3));
				company.setEmail(rs.getString(4));
			} else {
				return null;
			}

			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			throw new CouponSystemException("Get company error", e);
		} finally {
			cp.returnConnection(con1);
		}

		return company;
	}

	/**
	 * return company by name from the database , or return null if there is no
	 * such company
	 */
	@Override
	public Company getCompany(String compName) throws CouponSystemException {
		Company company = new Company();

		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con1 = cp.getConnection();

		String sql = "Select * from Company Where comp_name = ?";

		try {

			PreparedStatement pstmt = con1.prepareStatement(sql);
			pstmt.setString(1, compName);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				company.setId(rs.getLong(1));
				company.setName(rs.getString(2));
				company.setPassword(rs.getString(3));
				company.setEmail(rs.getString(4));
			} else {
				return null;

			}

			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			throw new CouponSystemException("Get company error", e);
		} finally {
			cp.returnConnection(con1);
		}

		return company;
	}

	/**
	 * return all companies collection from the database
	 */
	@Override
	public Collection<Company> getAllCompanies() throws CouponSystemException {
		Collection<Company> companies = new ArrayList<>();

		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con1 = cp.getConnection();

		String sql = "Select * from Company";

		try {

			Statement stmt = con1.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				companies.add(new Company(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4)));
			}
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			throw new CouponSystemException("Get all companies error", e);
		} finally {
			cp.returnConnection(con1);
		}

		return companies;
	}

	/**
	 * Return all coupons of the specific company id
	 */
	@Override
	public Collection<Coupon> getCoupons(long compId) throws CouponSystemException {
		Collection<Coupon> coupons = new ArrayList<>();

		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();

		String sql = "Select * from COUPON Join COMPANY_COUPON on Coupon.ID=COMPANY_COUPON.COUPON_ID Where COMPANY_COUPON.COMP_ID=?";

		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, compId);
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
	 * remove connection between company and coupons in COMPANY_COUPON table
	 */
	public void removeCompanyCoupon(Company company) throws CouponSystemException {
		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();

		String sql = "Delete from COMPANY_COUPON where COMP_ID =?";
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, company.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new CouponSystemException("Remove Company_Coupon error", e);
		} finally {
			cp.returnConnection(con);
		}

	}

	/**
	 * return true if password and name is correct. 
	 * return false if there is no company with this comp_name or the password is wrong
	 */
	@Override
	public boolean login(String comp_name, String password) throws CouponSystemException {
		Company company = new Company();

		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con1 = cp.getConnection();

		String sql = "Select * from COMPANY Where COMP_NAME = ?";

		try {

			PreparedStatement pstmt = con1.prepareStatement(sql);
			pstmt.setString(1, comp_name);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				company.setId(rs.getLong(1));
				company.setName((rs.getString(2)));
				company.setPassword(rs.getString(3));
				company.setEmail(rs.getString(4));
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

		if (company.getPassword().equals(password)) {
			return true;
		} else {
			return false;
		}
	}

}

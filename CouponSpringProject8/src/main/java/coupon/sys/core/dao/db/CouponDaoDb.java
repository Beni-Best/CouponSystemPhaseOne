package coupon.sys.core.dao.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import coupon.sys.core.beans.Coupon;
import coupon.sys.core.beans.CouponType;
import coupon.sys.core.connectionPool.ConnectionPool;
import coupon.sys.core.dao.CouponDao;
import coupon.sys.core.exceptions.CouponSystemException;

public class CouponDaoDb implements CouponDao {

	/**
	 * Create new coupon in the databse
	 */
	@Override
	public void createCoupon(Coupon coupon) throws CouponSystemException {

		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();

		String sql = "INSERT INTO COUPON (TITLE, START_DATE ,END_DATE ,AMOUNT ,TYPE ,MESSAGE ,PRICE ,IMAGE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, coupon.getTitle());
			pstmt.setDate(2, (Date) coupon.getStart_Date());
			pstmt.setDate(3, (Date) coupon.getEnd_Date());
			pstmt.setInt(4, coupon.getAmount());
			pstmt.setString(5, coupon.getType().name());
			pstmt.setString(6, coupon.getMessage());
			pstmt.setDouble(7, coupon.getPrice());
			pstmt.setString(8, coupon.getImage());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new CouponSystemException("Create coupon Error", e);
		} finally {
			cp.returnConnection(con);
		}
	}

	/**
	 * remove coupon from the database
	 */
	@Override
	public void removeCoupon(Coupon coupon) throws CouponSystemException {
		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();

		String sql = "DELETE from coupon WHERE id=?";
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, coupon.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new CouponSystemException("remove coupon error", e);
		} finally {
			cp.returnConnection(con);
		}
	}

	/**
	 * updates coupon in the database
	 */
	@Override
	public void updateCoupon(Coupon coupon) throws CouponSystemException {

		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();

		String sql = "UPDATE coupon SET title = ?, Start_Date = ?, End_Date = ?, amount = ?, type = ?, message = ?, price = ?, image = ? WHERE id = ?";
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(9, coupon.getId());
			pstmt.setString(1, coupon.getTitle());
			pstmt.setDate(2, (Date) coupon.getStart_Date());
			pstmt.setDate(3, (Date) coupon.getEnd_Date());
			pstmt.setInt(4, coupon.getAmount());
			pstmt.setString(5, coupon.getType().name());
			pstmt.setString(6, coupon.getMessage());
			pstmt.setDouble(7, coupon.getPrice());
			pstmt.setString(8, coupon.getImage());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new CouponSystemException("Update coupon error", e);
		} finally {
			cp.returnConnection(con);
		}

	}

	/**
	 * @return coupon by coupon id , return null if there is no coupon with such id
	 */
	@Override
	public Coupon getCoupon(long id) throws CouponSystemException {

		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();
		Coupon coupon = new Coupon();

		String sql = "Select * from Coupon Where id = ?";

		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				coupon.setId(rs.getLong(1));
				coupon.setTitle(rs.getString(2));
				coupon.setStart_Date(rs.getDate(3));
				coupon.setEnd_Date(rs.getDate(4));
				coupon.setAmount(rs.getInt(5));
				coupon.setType(CouponType.valueOf(rs.getString(6)));
				coupon.setMessage(rs.getString(7));
				coupon.setPrice(rs.getDouble(8));
				coupon.setImage(rs.getString(9));
			}else {
				return null;
			}

			pstmt.close();
			rs.close();

		} catch (SQLException e) {
			throw new CouponSystemException("Get Coupon error", e);
		} finally {
			cp.returnConnection(con);
		}

		return coupon;
	}

	/**
	 * @return coupon by couponTitle , return null if there is no coupon with such id
	 */
	@Override
	public Coupon getCoupon(String coupTitle) throws CouponSystemException {
		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();
		Coupon coupon = new Coupon();

		String sql = "Select * from Coupon Where TITLE = ?";

		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, coupTitle);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				coupon.setId(rs.getLong(1));
				coupon.setTitle(rs.getString(2));
				coupon.setStart_Date(rs.getDate(3));
				coupon.setEnd_Date(rs.getDate(4));
				coupon.setAmount(rs.getInt(5));
				coupon.setType(CouponType.valueOf(rs.getString(6)));
				coupon.setMessage(rs.getString(7));
				coupon.setPrice(rs.getDouble(8));
				coupon.setImage(rs.getString(9));
			}else {
				return null;
			}

			pstmt.close();
			rs.close();

		} catch (SQLException e) {
			throw new CouponSystemException("Get Coupon error", e);
		} finally {
			cp.returnConnection(con);
		}

		return coupon;
	}

	/**
	 * return all coupons collection from the database
	 */
	@Override
	public Collection<Coupon> getAllCoupon() throws CouponSystemException {
		Collection<Coupon> coupons = new ArrayList<>();

		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();

		String sql = "Select * from Coupon";

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				coupons.add(new Coupon(rs.getLong(1), rs.getString(2), rs.getDate(3), rs.getDate(4), rs.getInt(5),
						CouponType.valueOf(rs.getString(6)), rs.getString(7), rs.getDouble(8), rs.getString(9)));
			}
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			throw new CouponSystemException("Get all Coupons error", e);
		} finally {
			cp.returnConnection(con);
		}

		return coupons;
	}

	/**
	 * return all coupons collection from the database by type
	 */
	@Override
	public Collection<Coupon> getCouponByType(CouponType type) throws CouponSystemException {
		Collection<Coupon> coupons = new ArrayList<>();

		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();

		String sql = "Select * from Coupon Where type = ?";

		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, type.name());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				coupons.add(new Coupon(rs.getLong(1), rs.getString(2), rs.getDate(3), rs.getDate(4), rs.getInt(5),
						CouponType.valueOf(rs.getString(6)), rs.getString(7), rs.getDouble(8), rs.getString(9)));
			}
			pstmt.close();
			rs.close();
		} catch (SQLException e) {
			throw new CouponSystemException("Get Coupons by type error", e);
		} finally {
			cp.returnConnection(con);
		}

		return coupons;
	}

	/**
	 * remove connection between Company and coupon in COMPANY_COUPON table
	 */
	public void removeCompanyCoupon(Coupon coupon) throws CouponSystemException {
		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();
		
		String sql = "Delete from COMPANY_COUPON where COUPON_ID =?";
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, coupon.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new CouponSystemException("Remove Company_Coupon error", e);
		}finally {
			cp.returnConnection(con);
		}
		
	}
	
	/**
	 * remove connection between customer and coupon in CUSTOMER_COUPON table
	 */
	public void removeCustomerCoupon(Coupon coupon) throws CouponSystemException {
		ConnectionPool cp = ConnectionPool.getInstance();
		Connection con = cp.getConnection();
		
		String sql = "Delete from CUSTOMER_COUPON where COUPON_ID =?";
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, coupon.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new CouponSystemException("Remove CUSTOMER_COUPON error", e);
		}finally {
			cp.returnConnection(con);
		}
		
	}
	
	

}

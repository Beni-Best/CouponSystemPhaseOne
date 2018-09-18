package coupon.sys.core.facade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import coupon.sys.core.beans.Coupon;
import coupon.sys.core.beans.CouponType;
import coupon.sys.core.beans.Customer;
import coupon.sys.core.dao.db.CouponDaoDb;
import coupon.sys.core.dao.db.CustomerDaoDb;
import coupon.sys.core.dao.db.JoinedTableDaoDb;
import coupon.sys.core.exceptions.CouponSystemException;

public class CustomerFacade implements CouponClientFacade {

	private Customer customer;

	CouponDaoDb couponDao = new CouponDaoDb();
	JoinedTableDaoDb joinDao = new JoinedTableDaoDb();
	CustomerDaoDb custDao = new CustomerDaoDb();

	/**
	 * CTOR Needed to prevent Customers to get all purchased coupons that are
	 * not belong to them and so on..
	 * 
	 * @param customer
	 */
	public CustomerFacade(Customer customer) {
		this.customer = customer;
	}

	/**
	 * Add connection between coupon and the customer who invokes this method
	 * 
	 * 
	 * @param coupon
	 * @throws CouponSystemException
	 *             if there is no such coupon title , if Amount of coupons is 0
	 *             or if Coupon's date expired
	 */
	public void purchaseCoupon(Coupon coupon) throws CouponSystemException {

		java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());

		try {
			// get coupon from database by title!
			Coupon tmpCoupon = couponDao.getCoupon(coupon.getTitle());

			if (tmpCoupon == null) {
				throw new CouponSystemException("there is no such coupon title");
			}
			if (tmpCoupon.getAmount() == 0) {
				throw new CouponSystemException("Sorry Amount of coupons is 0");
			}
			if (tmpCoupon.getEnd_Date().compareTo(date) <= 0) {
				throw new CouponSystemException("Sorry Coupon's date expired ");
			}
			if (joinDao.checkCustomerCouponConnect(customer, tmpCoupon) == true) {
				throw new CouponSystemException("Sorry you already purchased this coupon");
			}
			// create connection between customer and coupon
			joinDao.customerPurchaseCoupon(this.customer, tmpCoupon);
			// -1 amount of coupons
			tmpCoupon.setAmount(tmpCoupon.getAmount() - 1);
			couponDao.updateCoupon(tmpCoupon);

		} catch (CouponSystemException e) {
			throw new CouponSystemException("purchaseCoupon error", e);
		}
	}

	/**
	 * Return all the purchased coupons collection of the customer who invokes
	 * this method
	 * 
	 * @return
	 * @throws CouponSystemException
	 */
	public Collection<Coupon> getAllPurchasedCoupons() throws CouponSystemException {

		Collection<Coupon> tmpCoupons = new ArrayList<>();
		Collection<Coupon> coupons = new ArrayList<>();

		tmpCoupons = couponDao.getAllCoupon();
		for (Coupon coupon : tmpCoupons) {
			if (joinDao.checkCustomerCouponConnect(this.customer, coupon)) {
				coupons.add(coupon);
			}
		}
		return coupons;
	}

	/**
	 * Return all the purchased coupons by TYPE collection of the customer who
	 * invokes this method
	 * 
	 * @return
	 * @throws CouponSystemException
	 */
	public Collection<Coupon> getAllPurchasedCouponsByType(CouponType coupType) throws CouponSystemException {

		Collection<Coupon> tmpCoupons = new ArrayList<>();
		Collection<Coupon> coupons = new ArrayList<>();

		tmpCoupons = couponDao.getAllCoupon();
		for (Coupon coupon : tmpCoupons) {
			if (joinDao.checkCustomerCouponConnect(this.customer, coupon)) {
				if (coupon.getType().equals(coupType)) {
					coupons.add(coupon);
				}

			}
		}
		return coupons;

	}

	/**
	 * Return all the purchased coupons by PRICE collection of the customer who
	 * invokes this method
	 * 
	 * @return
	 * @throws CouponSystemException
	 */
	public Collection<Coupon> getAllPurchasedCouponsByPrice(Double price) throws CouponSystemException {

		Collection<Coupon> tmpCoupons = new ArrayList<>();
		Collection<Coupon> coupons = new ArrayList<>();

		tmpCoupons = couponDao.getAllCoupon();
		for (Coupon coupon : tmpCoupons) {
			if (joinDao.checkCustomerCouponConnect(this.customer, coupon)) {
				if (coupon.getPrice() <= price) {
					coupons.add(coupon);
				}

			}
		}
		return coupons;
	}

	/**
	 * Return true if Client's name and password are correct otherwise return false
	 * 
	 */
public boolean login(String userName, String password) throws CouponSystemException {
	
	if (custDao.login(userName, password)) {
		return true;
	}
	return false;
}
}
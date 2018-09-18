package coupon.sys.core.facade;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

import coupon.sys.core.beans.Company;
import coupon.sys.core.beans.Coupon;
import coupon.sys.core.beans.CouponType;
import coupon.sys.core.dao.db.CompanyDaoDb;
import coupon.sys.core.dao.db.CouponDaoDb;
import coupon.sys.core.dao.db.JoinedTableDaoDb;
import coupon.sys.core.exceptions.CouponSystemException;

public class CompanyFacade implements CouponClientFacade {

	private Company company;

	CouponDaoDb coupDao = new CouponDaoDb();
	JoinedTableDaoDb joinDao = new JoinedTableDaoDb();
	CompanyDaoDb compDao = new CompanyDaoDb();

	/**
	 * CTOR Needed to prevent Companies to remove coupons that are not belong to
	 * them and so on..
	 * 
	 * @param company
	 */
	public CompanyFacade(Company company) {
		this.company = company;
	}

	/**
	 * this method create's coupon. Impossible to create coupon if coupon with
	 * the same name already exists
	 * 
	 * 
	 * @param coupon
	 * @throws CouponSystemException
	 */
	public void createCoupon(Coupon coupon) throws CouponSystemException {

		JoinedTableDaoDb joinDao = new JoinedTableDaoDb();

		if (coupDao.getCoupon(coupon.getTitle()) == null) {

			coupDao.createCoupon(coupon);
			joinDao.companyCreateCoupon(this.company, coupDao.getCoupon(coupon.getTitle()));

		} else {
			throw new CouponSystemException("Coupon title already exists in database");
		}

	}

	/**
	 * this method remove's coupon , Checks if coupon belong to the company uses
	 * this method.
	 * 
	 * @param coupon
	 * @throws CouponSystemException
	 */
	public void removeCoupon(Coupon coupon) throws CouponSystemException {

		Coupon tmpCoup = new Coupon();

		tmpCoup = coupDao.getCoupon(coupon.getTitle());
		if (tmpCoup == null) {
			throw new CouponSystemException("There is no such coupon to remove");
		}
		if (joinDao.checkCompanyCouponConnect(company, coupDao.getCoupon(coupon.getTitle())) == false) {
			throw new CouponSystemException("The coupon you wanna remove not belong to your company or not exist");
		}

		joinDao.deleteCoupon(coupDao.getCoupon(coupon.getTitle()));
		coupDao.removeCoupon(coupDao.getCoupon(coupon.getTitle()));

	}

	/**
	 * this method update's coupon's End-date and price , throws exception if
	 * was attempt to update other coupon's parmeters
	 * 
	 * 
	 * @param coupon
	 * @throws CouponSystemException
	 */
	public void updateCoupon(Coupon coupon) throws CouponSystemException {
		Coupon tmpCoup = new Coupon();
		System.out.println("coupon function get is "+coupon);
		tmpCoup = coupDao.getCoupon(coupon.getTitle());
		System.out.println("coupon we get from database is "+tmpCoup);
		if (tmpCoup == null) {
			System.out.println(tmpCoup);
			throw new CouponSystemException("There is no such coupon to update");
		}

		if (joinDao.checkCompanyCouponConnect(company, tmpCoup) == false) {
			throw new CouponSystemException("The coupon you wanna update not belong to your company or not exist");
		}

		// check if not updating something except end-Date and Price
		if (tmpCoup.getTitle().equals(coupon.getTitle()) && tmpCoup.getImage().equals(coupon.getImage())
				&& tmpCoup.getMessage().equals(coupon.getMessage())
				&& tmpCoup.getStart_Date().equals(coupon.getStart_Date()) && tmpCoup.getType().equals(coupon.getType())
				&& tmpCoup.getAmount() == coupon.getAmount()) {
				
			tmpCoup.setEnd_Date(coupon.getEnd_Date());
			tmpCoup.setPrice(coupon.getPrice());
			// update the coupon
			coupDao.updateCoupon(tmpCoup);
		} else {
			throw new CouponSystemException("You are allowed to update only coupon's End-date and price");
		}

	}

	/**
	 * get coupon by ID , check if coupon belong to the company uses this facade
	 * 
	 * @param coupId
	 * @return
	 * @throws CouponSystemException
	 */
	public Coupon getCouponById(long coupId) throws CouponSystemException {
		Coupon tmpCoup = new Coupon();

		tmpCoup = coupDao.getCoupon(coupId);
		if (tmpCoup == null) {
			throw new CouponSystemException("There is no such coupon");
		}
		if (joinDao.checkCompanyCouponConnect(company, tmpCoup) == false) {
			throw new CouponSystemException("The coupon you wanna get not belong to your company or not exist");
		}

		return tmpCoup;
	}

	/**
	 * get all coupons that belong to the company uses this facade
	 * 
	 * @return
	 * @throws CouponSystemException
	 */
	public Collection<Coupon> getAllCoupons() throws CouponSystemException {

		Collection<Coupon> tmpCoupons = new ArrayList<>();
		Collection<Coupon> coupons = new ArrayList<>();

		tmpCoupons = coupDao.getAllCoupon();
		for (Coupon coupon : tmpCoupons) {
			if (joinDao.checkCompanyCouponConnect(this.company, coupon)) {
				coupons.add(coupon);
			}
		}
		return coupons;
	}

	/**
	 * get all coupons by couponType that belong to the company uses this facade
	 * 
	 * @param couponType
	 * @return
	 * @throws CouponSystemException
	 */
	public Collection<Coupon> getAllCouponsByType(CouponType couponType) throws CouponSystemException {

		Collection<Coupon> tmpCoupons = new ArrayList<>();
		Collection<Coupon> coupons = new ArrayList<>();

		tmpCoupons = coupDao.getAllCoupon();
		for (Coupon coupon : tmpCoupons) {
			if (joinDao.checkCompanyCouponConnect(this.company, coupon)) {
				if (coupon.getType().equals(couponType)) {
					coupons.add(coupon);
				}
			}
		}
		return coupons;
	}

	/**
	 * get all coupons by couponPrice that belong to the company uses this
	 * facade
	 * 
	 * @param couponPrice
	 * @return
	 * @throws CouponSystemException
	 */
	public Collection<Coupon> getAllCouponsByPrice(double couponPrice) throws CouponSystemException {

		Collection<Coupon> tmpCoupons = new ArrayList<>();
		Collection<Coupon> coupons = new ArrayList<>();

		tmpCoupons = coupDao.getAllCoupon();
		for (Coupon coupon : tmpCoupons) {
			if (joinDao.checkCompanyCouponConnect(this.company, coupon)) {
				if (coupon.getPrice() <= couponPrice) {
					coupons.add(coupon);
				}

			}
		}
		return coupons;
	}

	/**
	 * get all coupons before end's date that belong to the company uses this
	 * facade
	 * 
	 * @param couponDate
	 * @return
	 * @throws CouponSystemException
	 */
	public Collection<Coupon> getAllCouponsBeforeEndDate(Date couponDate) throws CouponSystemException {

		Collection<Coupon> tmpCoupons = new ArrayList<>();
		Collection<Coupon> coupons = new ArrayList<>();

		tmpCoupons = coupDao.getAllCoupon();
		for (Coupon coupon : tmpCoupons) {
			if (joinDao.checkCompanyCouponConnect(this.company, coupon)) {
				if (coupon.getEnd_Date().compareTo(couponDate) < 0) {
					coupons.add(coupon);
				}

			}
		}
		return coupons;
	}

	/**
	 * Return true if Company's name and password are correct otherwise return false
	 * 
	 */
	public boolean login(String userName, String password) throws CouponSystemException {
		
		if (compDao.login(userName, password)) {
			return true;
		}
		return false;
	}

}

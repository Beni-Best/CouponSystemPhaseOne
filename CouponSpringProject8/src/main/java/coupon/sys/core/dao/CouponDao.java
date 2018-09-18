package coupon.sys.core.dao;

import java.util.Collection;

import coupon.sys.core.beans.Coupon;
import coupon.sys.core.beans.CouponType;
import coupon.sys.core.exceptions.CouponSystemException;

public interface CouponDao {
 public void createCoupon(Coupon coupon) throws CouponSystemException;
 
 public void removeCoupon (Coupon coupon) throws CouponSystemException;
 
 public void updateCoupon (Coupon coupon) throws CouponSystemException;
 
 public Coupon getCoupon(long id)throws CouponSystemException;
 
 public Coupon getCoupon(String coupTitle)throws CouponSystemException;
 
 public Collection<Coupon> getAllCoupon() throws CouponSystemException;
 
 public Collection<Coupon> getCouponByType(CouponType type) throws CouponSystemException;
 
 public void removeCompanyCoupon(Coupon coupon) throws CouponSystemException;
 
 public void removeCustomerCoupon(Coupon coupon) throws CouponSystemException;
}

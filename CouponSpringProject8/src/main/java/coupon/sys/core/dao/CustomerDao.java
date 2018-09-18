package coupon.sys.core.dao;

import java.util.Collection;


import coupon.sys.core.beans.Coupon;
import coupon.sys.core.beans.Customer;
import coupon.sys.core.exceptions.CouponSystemException;

public interface CustomerDao {

	public void createCustomer(Customer customer) throws CouponSystemException;
	
	public void removeCustomer (Customer customer) throws CouponSystemException;
	
	public void updateCustomer (Customer customer)throws CouponSystemException;
	
	public Customer getCustomer (long id) throws CouponSystemException;
	
	public Customer getCustomer (String custName) throws CouponSystemException;
	
	public Collection<Customer> getAllCustomers() throws CouponSystemException;
	
	public Collection<Coupon> getCoupons(long id) throws CouponSystemException;
	
	public void removeCustomerCoupon(Customer customer) throws CouponSystemException;
	
	public boolean login(String cust_name , String password) throws CouponSystemException;
	
	
}

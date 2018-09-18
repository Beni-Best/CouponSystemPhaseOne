package coupon.sys.core.facade;

import java.util.ArrayList;
import java.util.Collection;

import coupon.sys.core.beans.Company;
import coupon.sys.core.beans.Coupon;
import coupon.sys.core.beans.Customer;
import coupon.sys.core.dao.db.CompanyDaoDb;
import coupon.sys.core.dao.db.CouponDaoDb;
import coupon.sys.core.dao.db.CustomerDaoDb;
import coupon.sys.core.exceptions.CouponSystemException;

public class AdminFacade implements CouponClientFacade {

	CompanyDaoDb compDao = new CompanyDaoDb();
	CouponDaoDb coupDao = new CouponDaoDb();
	CustomerDaoDb custDao = new CustomerDaoDb();

	/**
	 * Create new company in the database
	 * 
	 * @param company
	 * @throws CouponSystemException
	 */
	public void createCompany(Company company) throws CouponSystemException {

		if (compDao.getCompany(company.getName()) == null) {
			compDao.create(company);
		} else {
			throw new CouponSystemException("Company name already taken");
		}

	}

	/**
	 * remove company from the system database
	 * 
	 * @param company
	 * @throws CouponSystemException
	 */
	public void removeCompany(Company company) throws CouponSystemException {

		Collection<Coupon> coupons = new ArrayList<>();

		Company tmpComp = new Company();

		tmpComp = compDao.getCompany(company.getName());
		// check if company exists
		if (tmpComp == null) {
			throw new CouponSystemException("there is no such company to remove");
		}

		// get all coupons of the company we want to remove
		coupons.addAll(compDao.getCoupons(tmpComp.getId()));

		// delete COMPANY_COUPON connections
		compDao.removeCompanyCoupon(tmpComp);

		// delete CUSTOMER_COUPON connections
		for (Coupon coupon : coupons) {
			coupDao.removeCustomerCoupon(coupon);
		}

		// delete all coupons of the company we Deleting
		for (Coupon coupon : coupons) {
			coupDao.removeCoupon(coupon);
		}
		// delete company
		compDao.removeCompany(tmpComp);

	}

	/**
	 * Update company, not allowed to update company name.
	 * 
	 * @param company
	 * @throws CouponSystemException
	 */
	public void updateCompany(Company company) throws CouponSystemException {

		Company tmpComp = new Company();

		// get company from database by name
		tmpComp = compDao.getCompany(company.getName());

		// check if there is company with same name to update , if no throw
		// exception
		if (tmpComp == null) {
			throw new CouponSystemException("There is no company with that name to update");
		}

		
		tmpComp.setEmail(company.getEmail());
		tmpComp.setPassword(company.getPassword());
		compDao.updateCompany(tmpComp);

	}

	/**
	 * @return all companies collection in the system
	 * @throws CouponSystemException
	 */
	public Collection<Company> getAllCompanies() throws CouponSystemException {

		Collection<Company> companies = new ArrayList<>();
		try {
			// show on screen & return
			// System.out.println("you get " + compDao.getAllCompanies());
			companies.addAll(compDao.getAllCompanies());
		} catch (Exception e) {
			throw new CouponSystemException("get all companiees error", e);
		}
		return companies;

	}

	/**
	 * 
	 * @param compId
	 * @return company object by id
	 * @throws CouponSystemException
	 */
	public Company getCompany(long compId) throws CouponSystemException {

		Company tmpComp = new Company();

		// get company with by id
		tmpComp = compDao.getCompany(compId);
		// check if we there is a company to show , if no throw exception
		if (tmpComp == null) {
			throw new CouponSystemException("There is no such company");
		}
		// show on screen & return
		return tmpComp;
	}

	/**
	 * Create new customer in the database
	 * 
	 * @param customer
	 * @throws CouponSystemException
	 */
	public void createCustomer(Customer customer) throws CouponSystemException {

		if (custDao.getCustomer(customer.getName()) == null) {
			custDao.createCustomer(customer);
		} else {
			throw new CouponSystemException("The customer name already taken");
		}

	}

	/**
	 * this method remove's customer system , !DELETES all connections between
	 * purchased coupons
	 * 
	 * @param customer
	 * @throws CouponSystemException
	 */
	public void removeCustomer(Customer customer) throws CouponSystemException {


		// check if there is a customer in database.
		if (custDao.getCustomer(customer.getName()) == null) {
			throw new CouponSystemException("There is no customer with this name to remove");
		}

		// Remove Customer_Coupon connection
		custDao.removeCustomerCoupon(custDao.getCustomer(customer.getName()));

		// remove customer
		custDao.removeCustomer(custDao.getCustomer(customer.getName()));

	}

	/**
	 * Updates customer. Not allowed to update customer name
	 * 
	 * @param customer
	 * @throws CouponSystemException
	 */
	public void updateCustomer(Customer customer) throws CouponSystemException {

		
		Customer tmpCust = new Customer();

		// get customer from database with by name
		tmpCust = custDao.getCustomer(customer.getName());
		// check if there is customer with same name to update , if no throw
		// exception
		if (tmpCust == null) {
			throw new CouponSystemException("There is no customer with this name to update");
		}
		

		// update customer
		tmpCust.setPassword(customer.getPassword());
		custDao.updateCustomer(tmpCust);

	}

	/**
	 * 
	 * @return all customers collection in the database
	 * @throws CouponSystemException
	 */
	public Collection<Customer> getAllCustomer() throws CouponSystemException {

		Collection<Customer> customers = new ArrayList<>();
	
		try {
			customers.addAll(custDao.getAllCustomers());
		} catch (CouponSystemException e) {
			throw new CouponSystemException("Get all customers error", e);
		}

		return customers;

	}

	/**
	 * 
	 * @param company
	 * @return all customers collection in the database of the specific company
	 * @throws CouponSystemException
	 */
	public Collection<Customer> getAllCustomer(Company company) throws CouponSystemException {
	
		Collection<Customer> customers = new ArrayList<>();
		customers = custDao.getAllCustomers(company);
		return customers;
	}

	/**
	 * 
	 * @param custId
	 * @return Customer object by id
	 * @throws CouponSystemException
	 */
	public Customer getCustomer(long custId) throws CouponSystemException {
		Customer tmpCust = new Customer();
		tmpCust = custDao.getCustomer(custId);
		if (tmpCust == null) {
			throw new CouponSystemException("There is no such client in DataBase");
		}
		return tmpCust;
	}

	/**
	 * Return true if Admin's name = "admin" and password = "1234". otherwise
	 * throws exception
	 */
	@Override
	public boolean login(String userName, String password) throws CouponSystemException {

		if (userName == "admin") {
			if (password == "1234") {
				return true;
			}
			throw new CouponSystemException("enter '1234' in password section");
		}
		throw new CouponSystemException("enter 'admin' in username section");
	}

}

package coupon.sys.core.system;



import coupon.sys.core.beans.Company;
import coupon.sys.core.beans.Customer;
import coupon.sys.core.connectionPool.ConnectionPool;
import coupon.sys.core.daily.DailyCouponExpirationTask;
import coupon.sys.core.dao.db.CompanyDaoDb;
import coupon.sys.core.dao.db.CustomerDaoDb;
import coupon.sys.core.exceptions.CouponSystemException;
import coupon.sys.core.facade.AdminFacade;
import coupon.sys.core.facade.CompanyFacade;
import coupon.sys.core.facade.CouponClientFacade;
import coupon.sys.core.facade.CustomerFacade;

public class CouponSystem {

	private static DailyCouponExpirationTask runner = new DailyCouponExpirationTask();
	
	private static CouponSystem instance;

	
	/**
	 * 
	 * @return a singleton instance
	 */
	public static CouponSystem getInstance() {
		if (instance == null) {
			instance = new CouponSystem();
		}
		return instance;
	}

	private CouponSystem() {

		runner.startWork();
		
		//		t1.start(); //start thread
	}
	
	private static CustomerDaoDb custDao = new CustomerDaoDb();
	private static CompanyDaoDb comDao = new CompanyDaoDb();
/**
 * 
 * This method return the facade (ADMIN/CUSTOMER/COMPANY) depends on clientType if username and password are correct 
 * 
 * @param username 
 * @param password
 * @param clientType
 * @return
 * @throws CouponSystemException
 */
	public CouponClientFacade login(String username, String password, ClientType clientType)
			throws CouponSystemException {

		CouponClientFacade facade = null;

		if (clientType == (ClientType.ADMIN)) {
			facade = new AdminFacade();
			if (facade.login(username, password) == true) {
				return facade;
			}
		}

		if (clientType == (ClientType.COMPANY)) {
			facade = new CompanyFacade(new Company());
			if (facade.login(username, password)) {
				
				Company comp = comDao.getCompany(username);
				facade = new CompanyFacade(comp);
				return facade;
			}else {
				throw new CouponSystemException("User Name or password is wrong");
			}
		}

		if (clientType == (ClientType.CUSTOMER)) {
			facade = new CustomerFacade(new Customer());
			if (facade.login(username, password)) {
				
				Customer cust = custDao.getCustomer(username);
				facade = new CustomerFacade(cust);
				return facade;
			}else {
				throw new CouponSystemException("User Name or password is wrong");
			}
		}
		
		throw new CouponSystemException("Chose clinet type");

	}
	
	/**
	 * This method will shutDown the system ,will close runner thread , and will close all connections
	 * @throws CouponSystemException
	 */
	public void shutDown () throws CouponSystemException {
		runner.stopTask();
		ConnectionPool cp = ConnectionPool.getInstance();
		cp.closeAllConnections();
	}

}

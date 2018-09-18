package coupon.sys.core.daily;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import coupon.sys.core.beans.Coupon;
import coupon.sys.core.dao.db.CouponDaoDb;
import coupon.sys.core.dao.db.JoinedTableDaoDb;
import coupon.sys.core.exceptions.CouponSystemException;

public class DailyCouponExpirationTask implements Runnable{

	private static DailyCouponExpirationTask runner = new DailyCouponExpirationTask();
	private static Thread t1 = new Thread(runner, "DailyCoupRunner");
	
	/**
	 * this method stars the thread */
	public void startWork() {
		t1.start();
	}
	
	private static volatile Boolean quit = false;
	
	
	@Override
	public void run() {
		
		while (!quit) {
			try {

				java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		        System.out.println(date +" Daily coupon expiration task started ");
				
		       		Collection<Coupon>coupons = new ArrayList<>();
				
				  CouponDaoDb coupDao = new CouponDaoDb();
				  coupons=coupDao.getAllCoupon();
					JoinedTableDaoDb joinDao = new JoinedTableDaoDb();
				for (Coupon coupon : coupons) {
					if (coupon.getEnd_Date().compareTo(date)<0) {
					System.out.println(coupon +" is out of date. System removing this coupon from database");
					joinDao.deleteCoupon(coupon);
					coupDao.removeCoupon(coupon);
				}
				}
				Thread.sleep(1000*60*60*24);

			} catch (InterruptedException e) {
			System.out.println("was interupted");
				break;
			} catch (CouponSystemException e) {
				e.printStackTrace();
			}
		}
		
	}
	/**
	 * this method stops the thread */
	public void stopTask () {
//		t1.interrupt(); // option to interrupt instead of quit
		quit = true;
	}
}

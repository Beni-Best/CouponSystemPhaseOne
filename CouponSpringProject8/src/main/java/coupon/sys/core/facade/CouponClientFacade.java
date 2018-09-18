package coupon.sys.core.facade;

import coupon.sys.core.exceptions.CouponSystemException;

public interface CouponClientFacade {

		public boolean login (String userName, String password)throws CouponSystemException;
}

package coupon.sys.core.beans;

import java.util.Date;

public class Coupon {
private long id;
private String title;
private Date Start_Date;
private Date End_Date;
private int amount;
private CouponType type;
private String message;
private double price;
private String image;


/**
 * empty CTOR
 */
public Coupon() {
	super();
}

/**
 * CTOR with id
 */
public Coupon(long id, String title, Date start_Date, Date end_Date, int amount, CouponType type, String message,
		double price, String image) {
	super();
	this.id = id;
	this.title = title;
	Start_Date = start_Date;
	End_Date = end_Date;
	this.amount = amount;
	this.type = type;
	this.message = message;
	this.price = price;
	this.image = image;
}
/**
 * CTOR without id
 */
public Coupon(String title, Date start_Date, Date end_Date, int amount, CouponType type, String message,
		double price, String image) {
	super();
	this.title = title;
	Start_Date = start_Date;
	End_Date = end_Date;
	this.amount = amount;
	this.type = type;
	this.message = message;
	this.price = price;
	this.image = image;
}

/**
 * get coupon id
 * @return id
 */
public long getId() {
	return id;
}
/**
 * set coupon id
 * @param id
 */
public void setId(long id) {
	this.id = id;
}

/**
 * get coupon title
 * @return title
 */
public String getTitle() {
	return title;
}

/**
 * set coupon title
 * @param title
 */
public void setTitle(String title) {
	this.title = title;
}

/**
 * get coupon start date
 * @return Start_Date
 */
public Date getStart_Date() {
	return Start_Date;
}

/**
 * set coupon start date
 * @param start_Date
 */
public void setStart_Date(Date start_Date) {
	Start_Date = start_Date;
}

/**
 * get coupon end date
 * @return End_Date
 */
public Date getEnd_Date() {
	return End_Date;
}
/**
 * set coupon end date
 * @param end_Date
 */
public void setEnd_Date(Date end_Date) {
	End_Date = end_Date;
}
/**
 * get amount of coupon
 * @return amount
 */
public int getAmount() {
	return amount;
}

/**
 * set amount of coupon
 * @param amount
 */
public void setAmount(int amount) {
	this.amount = amount;
}

/**
 * get coupon type
 * @return type
 */
public CouponType getType() {
	return type;
}
/**
 * set coupon type
 * @param type
 */
public void setType(CouponType type) {
	this.type = type;
}
/**
 * get coupon message
 * @return message
 */
public String getMessage() {
	return message;
}
/**
 * set coupon message
 * @param message
 */
public void setMessage(String message) {
	this.message = message;
}

/**
 * get coupon price
 * @return price
 */
public double getPrice() {
	return price;
}
/**
 * set coupon price
 * @param price
 */
public void setPrice(double price) {
	this.price = price;
}

/**
 * get coupon image
 * @return image
 */
public String getImage() {
	return image;
}

/**
 * set coupon image
 * @param image
 */
public void setImage(String image) {
	this.image = image;
}
@Override
public String toString() {
	return "Coupon [id=" + id + ", title=" + title + ", Start_Date=" + Start_Date + ", End_Date=" + End_Date
			+ ", amount=" + amount + ", type=" + type + ", message=" + message + ", price=" + price + ", image=" + image
			+ "]";
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((End_Date == null) ? 0 : End_Date.hashCode());
	result = prime * result + ((Start_Date == null) ? 0 : Start_Date.hashCode());
	result = prime * result + amount;
	result = prime * result + (int) (id ^ (id >>> 32));
	result = prime * result + ((image == null) ? 0 : image.hashCode());
	result = prime * result + ((message == null) ? 0 : message.hashCode());
	long temp;
	temp = Double.doubleToLongBits(price);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	result = prime * result + ((title == null) ? 0 : title.hashCode());
	result = prime * result + ((type == null) ? 0 : type.hashCode());
	return result;
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Coupon other = (Coupon) obj;
	if (End_Date == null) {
		if (other.End_Date != null)
			return false;
	} else if (!End_Date.equals(other.End_Date))
		return false;
	if (Start_Date == null) {
		if (other.Start_Date != null)
			return false;
	} else if (!Start_Date.equals(other.Start_Date))
		return false;
	if (amount != other.amount)
		return false;
	if (id != other.id)
		return false;
	if (image == null) {
		if (other.image != null)
			return false;
	} else if (!image.equals(other.image))
		return false;
	if (message == null) {
		if (other.message != null)
			return false;
	} else if (!message.equals(other.message))
		return false;
	if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
		return false;
	if (title == null) {
		if (other.title != null)
			return false;
	} else if (!title.equals(other.title))
		return false;
	if (type != other.type)
		return false;
	return true;
}


}


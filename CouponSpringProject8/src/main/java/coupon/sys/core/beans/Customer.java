package coupon.sys.core.beans;

public class Customer {

	private long id;
	private String cust_name;
	private String password;
	
	
	
	public Customer() {
		super();
	}
	public Customer(long id, String cust_name, String password) {
		super();
		this.id = id;
		this.cust_name = cust_name;
		this.password = password;
	}
	public Customer(String cust_name, String password) {
		super();
		this.cust_name = cust_name;
		this.password = password;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return cust_name;
	}
	public void setName(String cust_name) {
		this.cust_name = cust_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "Customer [id=" + id + ", cust_name=" + cust_name + ", password=" + password + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cust_name == null) ? 0 : cust_name.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((password == null) ? 0 : password.hashCode());
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
		Customer other = (Customer) obj;
		if (cust_name == null) {
			if (other.cust_name != null)
				return false;
		} else if (!cust_name.equals(other.cust_name))
			return false;
		if (id != other.id)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}
	
}

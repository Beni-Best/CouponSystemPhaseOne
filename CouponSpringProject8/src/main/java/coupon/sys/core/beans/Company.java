package coupon.sys.core.beans;

public class Company {

	private long id;
	private String comp_name;
	private String password;
	private String email;

	/**
	 * Empty CTOR
	 */
	public Company() {
		
	}
	
	/**
	 * CTOR , without id
	 */
	public Company(String comp_name, String password, String email) {
		super();
		this.comp_name = comp_name;
		this.password = password;
		this.email = email;
	}
	
	/**
	 * CTOR , with id
	 */
	public Company(long id, String comp_name, String password, String email) {
		super();
		this.id = id;
		this.comp_name = comp_name;
		this.password = password;
		this.email = email;
	}

	/**
	 * get company id
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 *  Set company id
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * get name of company
	 * @return comp_name
	 */
	public String getName() {
		return comp_name;
	}

	/**
	 * set company name 
	 * @param name
	 */
	public void setName(String name) {
		this.comp_name = name;
	}

	/**
	 * get company password
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * set company password
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * get company email
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Set company email
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", name=" + comp_name + ", password=" + password + ", email=" + email + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comp_name == null) ? 0 : comp_name.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		Company other = (Company) obj;
		if (comp_name == null) {
			if (other.comp_name != null)
				return false;
		} else if (!comp_name.equals(other.comp_name))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
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

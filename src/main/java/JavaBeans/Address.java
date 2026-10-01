package JavaBeans;

import java.io.Serializable;

public class Address implements Serializable{
	private int id;
	private int addressId;
	private String name;	
	private String postalCode;
	private String address;
	private String email;
	private boolean isMain;
	private boolean isSelf;
	
	public Address() {}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAddressId() {
		return addressId;
	}
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean getIsMain() {
		return isMain;
	}
	public void setIsMain(boolean isMain) {
		this.isMain = isMain;
	}
	public boolean getIsSelf() {
		return isSelf;
	}
	public void setIsSelf(boolean isSelf) {
		this.isSelf = isSelf;
	}
}


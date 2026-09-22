package JavaBeans;

import java.io.Serializable;

public class Order implements Serializable{
	private String name;
	private String postalCode;
	private String address;
	private String email;
	private String selectedPayment;
	private boolean isLogin;
	private boolean isRecipient;
	
	public Order() {}
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
	public String getSelectedPayment() {
		return selectedPayment;
	}
	public void setSelectedPayment(String selectedPayment) {
		this.selectedPayment = selectedPayment;
	}
	public boolean isLogin() {
		return isLogin;
	}
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	public boolean isRecipient() {
		return isRecipient;
	}
	public void setRecipient(boolean isRecipient) {
		this.isRecipient = isRecipient;
	}
}
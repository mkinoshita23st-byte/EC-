package JavaBeans;

import java.io.Serializable;

public class Order implements Serializable {
	private String firstName;
	private String middleName;
	private String lastName;
	private String name;
	private String postalCode;
	private String address;
	private String email;
	private String selectedPayment;
	private boolean isLogin;
	private boolean isRecipient;

	public Order() {
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getSelectedPayment() {
		return selectedPayment;
	}

	public void setSelectedPayment(String selectedPayment) {
		this.selectedPayment = selectedPayment;
	}

	public boolean getIsLogin() {
		return isLogin;
	}

	public void setIsLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public boolean getIsRecipient() {
		return isRecipient;
	}

	public void setIsRecipient(boolean isRecipient) {
		this.isRecipient = isRecipient;
	}

}
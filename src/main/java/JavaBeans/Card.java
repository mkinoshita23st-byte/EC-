package JavaBeans;

import java.io.Serializable;

public class Card implements Serializable{
	private int id;
	private int cardId;
	private String number;
	private String expiry;
	private String holder;
	private boolean isMain;
	
	public Card() {}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCardId() {
		return cardId;
	}
	public void setCardId(int cardId) {
		this.cardId = cardId;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getExpiry() {
		return expiry;
	}
	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}
	public String getHolder() {
		return holder;
	}
	public void setHolder(String holder) {
		this.holder = holder;
	}
	public boolean isMain() {
		return isMain;
	}
	public void setIsMain(boolean isMain) {
		this.isMain = isMain;
	}
}


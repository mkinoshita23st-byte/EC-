package JavaBeans;

import java.io.Serializable;

public class Book implements Serializable{
	private String isbn;
	private String title;
	private int price;
	private String publish;
	private String published;
	private int stock;
	
	private int beforeCartCnt;
	private int cartCnt;

	//初期値が０になるのでコンストで指定不要
	//JavaBeansは引数なしのコンストラクタが必要
	public Book() {}
	
	//ゲッターとセッター
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getPublish() {
		return publish;
	}
	public void setPublish(String publish) {
		this.publish = publish;
	}
	public String getPublished() {
		return published;
	}
	public void setPublished(String published) {
		this.published = published;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public int getBeforeCartCnt() {
		return beforeCartCnt;
	}
	public void setBeforeCartCnt(int beforeCartCnt) {
		this.beforeCartCnt = beforeCartCnt;
	}
	public int getCartCnt() {
		return cartCnt;
	}
	public void setCartCnt(int cartCnt) {
		this.cartCnt = cartCnt;
	}
}


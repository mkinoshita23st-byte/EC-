package Servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/OrderServlet")
public class Order extends HttpServlet {

	private static final long serialVersionUID = 1L;

	//メール用初期化(サーブレットで必要な処理。Tomcatが勝手に使用）
	private String host, port, user, password, charset;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext app = config.getServletContext();
		this.host = app.getInitParameter("smtp.host");
		this.port = app.getInitParameter("smtp.port");
		this.user = app.getInitParameter("smtp.user");
		this.password = app.getInitParameter("smtp.password");
		this.charset = app.getInitParameter("smtp.charset");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String forwardPath = null;
		String action = request.getParameter("action");
		HttpSession session = request.getSession();

		switch (action) {
		case "toOrder" -> {//購入時ログイン済みか確認
			JavaBeans.Order orderInf = (JavaBeans.Order) session.getAttribute("orderInf");
			if (orderInf.getIsLogin()) {
				forwardPath = "order_input";
			} else {
				forwardPath = "login";
				session.setAttribute("fromCart", true);
				session.setAttribute("loginMessage", "商品の購入にはログインが必要です。");
				//loginMessageはlogin.jspでremove
			}
			boolean updateCart = false;
			session.setAttribute("updateCart", updateCart);
		}
		case "orderInput" -> {//
			JavaBeans.Order orderInf = (JavaBeans.Order) session.getAttribute("orderInf");
			if (orderInf.getIsLogin() == true) {
				forwardPath = "order_input";
			} else {
				forwardPath = "login";
				session.setAttribute("loginMessage", "購入画面に進むにはログインしてください");
			}
		}
		case "orderConfirm" -> {
			forwardPath = "order_confirm";
			JavaBeans.Order orderInf = (JavaBeans.Order) session.getAttribute("orderInf");
			JavaBeans.User userInf = (JavaBeans.User) session.getAttribute("userInf");
			
			// 画面から渡されたパラメータを取得
			String addressType = request.getParameter("addressType");
			String paymentType = request.getParameter("paymentType");
			String selectedCardId = request.getParameter("selectedCardId");

			// お届け先情報の保持
			session.setAttribute("addressType", addressType); // 選択種別(self/registered/new)のみ保持
			session.setAttribute("isNewAddress", false);

			if (addressType != null && !addressType.equals("self")) {
				if (addressType.equals("registered")) { // 登録済み住所から選択
					int selectedAddressId = Integer.parseInt(request.getParameter("selectedAddressId"));
					session.setAttribute("selectedAddressId", selectedAddressId);

					List<JavaBeans.Address> addressList = (List<JavaBeans.Address>) session.getAttribute("addressList");
					if (addressList != null) {
						for (JavaBeans.Address add : addressList) {
							if (add.getAddressId() == selectedAddressId) {
								if (!add.getIsSelf()) { // 贈答用
									orderInf.setIsRecipient(true);
									JavaBeans.Recipient recipientInf = new JavaBeans.Recipient();
									recipientInf.setName(add.getName());
									recipientInf.setPostalCode(add.getPostalCode());
									recipientInf.setAddress(add.getAddress());
									recipientInf.setEmail(add.getEmail());
									session.setAttribute("recipientInf", recipientInf);
								} else { // 住所が違う自分用
									orderInf.setPostalCode(add.getPostalCode());
									orderInf.setAddress(add.getAddress());
									orderInf.setEmail(add.getEmail());
								}
							}
						}
					}
				} else if (addressType.equals("new")) { // 新規入力住所
					session.setAttribute("isNewAddress", true);
					
					// JavaBeans.Address に一括保持
					JavaBeans.Address newAddressInf = new JavaBeans.Address();
					newAddressInf.setName(request.getParameter("newRecipientName"));
					newAddressInf.setPostalCode(request.getParameter("newPostalCode"));
					newAddressInf.setAddress(request.getParameter("newAddress"));
					newAddressInf.setEmail(request.getParameter("newEmail"));
					session.setAttribute("newAddressInf", newAddressInf);

					// 贈答用の場合は Recipient にもセット
					if (orderInf.getName() != null && !orderInf.getName().equals(userInf.getName())) {
						orderInf.setIsRecipient(true);
						JavaBeans.Recipient recipientInf = new JavaBeans.Recipient();
						recipientInf.setName(newAddressInf.getName());
						recipientInf.setPostalCode(newAddressInf.getPostalCode());
						recipientInf.setAddress(newAddressInf.getAddress());
						recipientInf.setEmail(newAddressInf.getEmail());
						session.setAttribute("recipientInf", recipientInf);
					}
				}
			}

			// 支払方法の決定と保持
			session.setAttribute("paymentType", paymentType); // 選択種別(credit/cod/convenience)
			String displayPaymentType = "credit".equals(paymentType) ? "クレジットカード"
					: "cod".equals(paymentType) ? "代金引換（代引き）" : "コンビニ決済";
			orderInf.setSelectedPayment(displayPaymentType);

			if ("credit".equals(paymentType) && selectedCardId != null) {
				int cardId = Integer.parseInt(selectedCardId);
				session.setAttribute("selectedCardId", cardId);

				List<JavaBeans.Card> cardList = (List<JavaBeans.Card>) session.getAttribute("cardList");
				if (cardList != null) {
					JavaBeans.Card orderCardInf = new JavaBeans.Card();
					for (JavaBeans.Card card : cardList) {
						if (card.getCardId() == cardId) {
							orderCardInf.setId(card.getId());
							orderCardInf.setCardId(card.getCardId());
							orderCardInf.setNumber(card.getNumber());
							orderCardInf.setExpiry(card.getExpiry());
							orderCardInf.setHolder(card.getHolder());
							session.setAttribute("orderCardInf", orderCardInf);
						}
					}
				}
			}
			session.setAttribute("orderInf", orderInf);
		}
		case "reOrder" -> { // 確認から入力画面へ戻る処理
			forwardPath = "order_input";
			session.setAttribute("isReOrder", true);
		}
		case "orderComplete" -> {
			forwardPath = "order_complete";
			//書籍在庫データベース更新
			List<JavaBeans.Book> bookCartList = (List<JavaBeans.Book>) session.getAttribute("bookCartList");
			DAO.Book bookDao = new DAO.Book();
			for (JavaBeans.Book book : bookCartList) {
				String isbn = book.getIsbn();
				int cartCnt = book.getCartCnt();
				if (cartCnt > 0) {
					bookDao.updateStock(isbn, cartCnt);
				}
			}
			//メール送信
			Servlet.Mail sender = new Servlet.Mail(host, port, user, password, charset);
			JavaBeans.Order orderInf = (JavaBeans.Order) session.getAttribute("orderInf");
			int totalPrice = (Integer) session.getAttribute("totalPrice");
			sender.sendMailForBuyer(bookCartList, orderInf, totalPrice);
			if (orderInf.getIsRecipient() == true) {
				JavaBeans.Recipient recipientInf = (JavaBeans.Recipient) session.getAttribute("recipientInf");
				sender.sendMailForRecipient(bookCartList, orderInf, recipientInf);
			}
			//DBのカート情報を消す
			JavaBeans.User userInf = (JavaBeans.User) session.getAttribute("userInf");
			int userId = userInf.getId();
			DAO.Cart cartDao = new DAO.Cart();
			cartDao.clearCartData(userId);
			//新住所の入力があった場合の処理
			boolean isNewAddress = (boolean) session.getAttribute("isNewAddress");
			if (isNewAddress) {
				session.setAttribute("newAddressMessage", "新しいお届け先をマイページに保存しますか？");
				forwardPath = "add_newaddress";
			}
			//sessionの更新
			session.setAttribute("bookCartList", bookCartList);//カート情報部分初期化				
			session.setAttribute("totalPrice", 0);//初期化
			session.setAttribute("totalQuantity", 0);//初期化
			session.setAttribute("updateCart", false);//初期化
			session.removeAttribute("isReOrder");//削除
		}
		case "saveAddressForm" -> {//購入時の新しいアドレスを登録
			forwardPath = "order_complete";
			JavaBeans.Address newAddressInf = (JavaBeans.Address) session.getAttribute("newAddressInf");
			JavaBeans.User userInf = (JavaBeans.User) session.getAttribute("userInf");
			DAO.Address addressDao = new DAO.Address();
			addressDao.addAddress(newAddressInf, userInf);
			session.removeAttribute("newAddressInf");
		}
		case "topAfterPurchase" -> {//購入完了後トップへ
			forwardPath = "top";
			//bookCartListの内容をデータベース更新後の最新情報にする
			DAO.Book bookDao = new DAO.Book();
			List<JavaBeans.Book> bookCartList = bookDao.getBookData();
			for (JavaBeans.Book book : bookCartList) {
				book.setBeforeCartCnt(0);
				book.setCartCnt(0);
			}
			JavaBeans.Order orderInf = (JavaBeans.Order) session.getAttribute("orderInf");
			orderInf.setSelectedPayment(null);
			orderInf.setIsRecipient(false);
			int totalPrice = 0;
			int totalQuantity = 0;
			//継続情報（Address,Card,Userは継続）
			session.setAttribute("bookCartList", bookCartList);//初期化
			session.setAttribute("orderInf", orderInf);//一部初期化
			session.setAttribute("totalPrice", totalPrice);//初期化
			session.setAttribute("totalQuantity", totalQuantity);//初期化
			session.setAttribute("updateCart", false);//初期化
			//削除
			session.removeAttribute("recipientInf");
			session.removeAttribute("orderCardInf");
		}
		default -> {
			forwardPath = "top";
		}
		};
		//画面遷移のためdoGetへ移動
		response.sendRedirect(request.getContextPath() + "/MainServlet?action=" + forwardPath);

	}
}

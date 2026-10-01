package Servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/BookServlet")
public class Book extends HttpServlet {

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

	//画面遷移
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String forwardPath = null;
		String action = request.getParameter("action");
		HttpSession session = request.getSession();

		if (action == null) {
			forwardPath = "jsp/top.jsp";
			JavaBeans.Order orderInf = new JavaBeans.Order();
			DAO.Book bookDao = new DAO.Book();
			List<JavaBeans.Book> bookCartList = bookDao.getBookData();
			for (JavaBeans.Book book : bookCartList) {
				book.setBeforeCartCnt(0);
				book.setCartCnt(0);
			}
			int totalPrice = 0;
			int totalQuantity = 0;
			//情報セット　画面遷移時にもって行ってもらう
			session.setAttribute("orderInf", orderInf);
			session.setAttribute("bookCartList", bookCartList);
			session.setAttribute("totalPrice", totalPrice);
			session.setAttribute("totalQuantity", totalQuantity);
			session.setAttribute("updateCart", false);
			session.setAttribute("fromCart", false);
		} else {
			forwardPath = switch (action) {
			case "book_list" -> "jsp/book_list.jsp";
			case "cart_view" -> "jsp/cart_view.jsp";
			case "login_error" -> "jsp/login_error.jsp";
			case "login_success" -> "jsp/login_success.jsp";
			case "login" -> "jsp/login.jsp";
			case "logout_complete" -> "jsp/logout_complete.jsp";
			case "logout" -> "jsp/logout.jsp";
			case "mypage_edit_complete" -> "jsp/mypage_edit_complete.jsp";
			case "mypage_edit_confirm" -> "jsp/mypage_edit_confirm.jsp";
			case "mypage_edit" -> "jsp/mypage_edit.jsp";
			case "mypage_view" -> "jsp/mypage_view.jsp";
			case "order_complete" -> "jsp/order_complete.jsp";
			case "order_confirm" -> "jsp/order_confirm.jsp";
			case "order_input" -> "jsp/order_input.jsp";
			case "register_complete" -> "jsp/register_complete.jsp";
			case "register_confirm" -> "jsp/register_confirm.jsp";
			case "register_input" -> "jsp/register_input.jsp";
			default -> "jsp/top.jsp";
			};
		}
		//指定された画面へ情報をもって移動
		RequestDispatcher dispatcher = request.getRequestDispatcher(forwardPath);
		dispatcher.forward(request, response);
	}

	//データベース更新、メール送信など処理
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String forwardPath = null;
		String action = request.getParameter("action");
		HttpSession session = request.getSession();

		switch (action) {
		case "inCart" -> {//一覧でカート冊数更新
			forwardPath = "book_list";
			List<JavaBeans.Book> bookCartList = (List<JavaBeans.Book>) session.getAttribute("bookCartList");
			int totalPrice = 0;
			int totalQuantity = 0;
			for (JavaBeans.Book book : bookCartList) {
				String isbn = request.getParameter(book.getIsbn());
				int inputCnt = (isbn != null && !isbn.isEmpty()) ? Integer.parseInt(isbn) : 0;
				int cartCnt = book.getCartCnt() + inputCnt;
				book.setCartCnt(cartCnt);
				totalPrice += book.getCartCnt() * book.getPrice();
				totalQuantity += book.getCartCnt();
			}
			session.setAttribute("bookCartList", bookCartList);
			session.setAttribute("totalPrice", totalPrice);
			session.setAttribute("totalQuantity", totalQuantity);
		}
		case "updateCart" -> {//カートで冊数更新
			forwardPath = "cart_view";
			List<JavaBeans.Book> bookCartList = (List<JavaBeans.Book>) session.getAttribute("bookCartList");
			int totalPrice = 0;
			int totalQuantity = 0;
			boolean updateCart = true;
			for (JavaBeans.Book book : bookCartList) {
				String isbn = request.getParameter(book.getIsbn());
				int inputCnt = (isbn != null && !isbn.isEmpty()) ? Integer.parseInt(isbn) : 0;
				book.setBeforeCartCnt(book.getCartCnt());//前の冊数を保存
				book.setCartCnt(inputCnt);
				totalPrice += book.getCartCnt() * book.getPrice();
				totalQuantity += book.getCartCnt();
			}
			session.setAttribute("bookCartList", bookCartList);
			session.setAttribute("totalPrice", totalPrice);
			session.setAttribute("totalQuantity", totalQuantity);
			session.setAttribute("updateCart", updateCart);
		}
		case "cartTobookCartList" -> {//カートからリストに行くだけ
			forwardPath = "book_list";
			session.setAttribute("updateCart", false);
		}
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
		case "loginCheck" -> {//ログインできるか、できたらどの画面に遷移するか
			String inputId = request.getParameter("input_id");
			String inputPass = request.getParameter("input_password");
			JavaBeans.Order orderInf = (JavaBeans.Order) session.getAttribute("orderInf");
			if (!orderInf.getIsLogin()) {//ログアウト状態
				DAO.User userDao = new DAO.User();
				JavaBeans.LoginResult result = userDao.checkLogin(inputId, inputPass);
				if (result.getIsSuccess()) {//ログイン可
					boolean fromCart = (boolean) session.getAttribute("fromCart");
					if (fromCart == true) {//購入からログインの場合
						forwardPath = "order_input";
						session.setAttribute("loginSuccessMessage", "ログインに成功しました");
						//loginSuccessMessageはorder_input.jspでremove
						session.setAttribute("fromCart", false);
					} else {//購入以外からのログインの場合
						forwardPath = "login_success";
					}
					//必要なリスト大量作成
					JavaBeans.User userInf = result.getUserInf();
					String userId = String.valueOf((userInf).getId());
					DAO.Address addressDao = new DAO.Address();
					List<JavaBeans.Address> addressList = addressDao.getAddressData(userId);
					DAO.Card cardDao = new DAO.Card();
					List<JavaBeans.Card> cardList = cardDao.getCardData(userId);
					DAO.Order orderDao = new DAO.Order();
					orderInf = (JavaBeans.Order) orderDao.getOrderData(userId);
					orderInf.setIsLogin(result.getIsSuccess());
					//カート情報取得
					int totalQuantity = (Integer) session.getAttribute("totalQuantity");
					List<JavaBeans.Book> bookCartList = (List<JavaBeans.Book>) session.getAttribute("bookCartList");
					DAO.Cart cartDao = new DAO.Cart();
					List<JavaBeans.Cart> cartList = (List<JavaBeans.Cart>) cartDao.getCartData(userId);
					for (JavaBeans.Cart cart : cartList) {
						for (JavaBeans.Book book : bookCartList) {
							if (cart.getIsbn().equals(book.getIsbn())) {
								book.setCartCnt(book.getCartCnt() + cart.getQuantity());
							}
						}
					}
					session.setAttribute("userInf", userInf);//生成
					session.setAttribute("addressList", addressList);//生成
					session.setAttribute("cardList", cardList);//生成
					session.setAttribute("orderInf", orderInf);//情報追加
					session.setAttribute("bookCartList", bookCartList);//情報追加
					//削除
					session.removeAttribute("inputId");
					session.removeAttribute("isReLogin");
				} else {
					forwardPath = "login";
					session.setAttribute("inputId", inputId);
					session.setAttribute("isReLogin", true);
					session.setAttribute("loginMessage", "IDまたはパスワードが違います");
				}
			} else {//ログイン状態の場合
				session.setAttribute("alreadyLoginedMessage", "すでにログイン状態です");
				forwardPath = "book_list";
			}
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

		case "logoutAfterPurchase" -> {
			forwardPath = "logout_complete";
			//情報の初期化
			List<JavaBeans.Book> bookCartList = (List<JavaBeans.Book>) session.getAttribute("bookCartList");
			for (JavaBeans.Book book : bookCartList) {
				book.setBeforeCartCnt(0);
				book.setCartCnt(0);
			}
			JavaBeans.Order orderInf = new JavaBeans.Order();
			int totalPrice = 0;
			int totalQuantity = 0;
			session.setAttribute("bookCartList", bookCartList);//カート情報部分削除
			session.setAttribute("orderInf", orderInf);//初期化
			session.setAttribute("totalPrice", totalPrice);//初期化
			session.setAttribute("totalQuantity", totalQuantity);//初期化
			session.setAttribute("updateCart", false);//初期化
			//削除
			session.removeAttribute("addressList");
			session.removeAttribute("cardList");
			session.removeAttribute("recipientInf");
			session.removeAttribute("userInf");
			session.removeAttribute("orderCardInf");
		}
		case "logout" -> {
			forwardPath = "logout_complete";
			//情報の初期化
			List<JavaBeans.Book> bookCartList = (List<JavaBeans.Book>) session.getAttribute("bookCartList");
			for (JavaBeans.Book book : bookCartList) {
				book.setBeforeCartCnt(0);
				book.setCartCnt(0);
			}
			JavaBeans.Order orderInf = new JavaBeans.Order();
			int totalPrice = 0;
			int totalQuantity = 0;
			int userId = ((JavaBeans.User) session.getAttribute("userInf")).getId();
			DAO.Cart cartDao = new DAO.Cart();
			for (JavaBeans.Book book : bookCartList) {
				if (book.getCartCnt() > 0) {
					cartDao.updateCartData(userId, book.getIsbn(), book.getCartCnt());
				}
			}
			session.setAttribute("bookCartList", bookCartList);//カート情報部分削除
			session.setAttribute("orderInf", orderInf);//初期化
			session.setAttribute("totalPrice", totalPrice);//初期化
			session.setAttribute("totalQuantity", totalQuantity);//初期化
			session.setAttribute("updateCart", false);//初期化
			//削除
			session.removeAttribute("addressList");
			session.removeAttribute("cardList");
			session.removeAttribute("recipientInf");
			session.removeAttribute("userInf");
			session.removeAttribute("orderCardInf");
		}
		case "mypage_edit_complete" -> {
			forwardPath = "mypage_edit_complete";
		}
		case "mypage_edit_confirm" -> {
			forwardPath = "mypage_edit_confirm";
		}
		case "mypage_edit" -> {
			forwardPath = "mypage_edit";
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
			//送付先決定
			String addressType = request.getParameter("addressType");
			session.setAttribute("isNewAddress", false);
			if (addressType != null && !addressType.equals("self")) {//selfはorderInfの情報が変わらないためなにもしない
				if (addressType.equals("registered")) {//選択送付先
					int selectedAddressId = Integer.parseInt(
							request.getParameter("selectedAddressId"));
					List<JavaBeans.Address> addressList = (List<JavaBeans.Address>) session.getAttribute("addressList");
					for (JavaBeans.Address add : addressList) {
						if (add.getAddressId() == selectedAddressId) {
							boolean isSelf = add.getIsSelf();
							if (!isSelf) {//贈答用
								orderInf.setIsRecipient(true);
								JavaBeans.Recipient recipientInf = new JavaBeans.Recipient();
								recipientInf.setName(add.getName());
								recipientInf.setPostalCode(add.getPostalCode());
								recipientInf.setAddress(add.getAddress());
								recipientInf.setEmail(add.getEmail());
								session.setAttribute("recipientInf", recipientInf);
							} else {//住所が違う自分用
								orderInf.setPostalCode(add.getPostalCode());
								orderInf.setAddress(add.getAddress());
								orderInf.setEmail(add.getEmail());
							}
						}
					}
				} else if (addressType.equals("new")) {
					session.setAttribute("isNewAddress", true);
					JavaBeans.Address newAddressInf = new JavaBeans.Address();
					newAddressInf.setName(request.getParameter("newRecipientName"));
					newAddressInf.setPostalCode(request.getParameter("newPostalCode"));
					newAddressInf.setAddress(request.getParameter("newAddress"));
					newAddressInf.setEmail(request.getParameter("newEmail"));
					session.setAttribute("newAddressInf", newAddressInf);
					if (orderInf.getName() != userInf.getAccountName()) {//自分用か贈答用か
						orderInf.setIsRecipient(true);
						JavaBeans.Recipient recipientInf = new JavaBeans.Recipient();
						recipientInf.setName(request.getParameter("newRecipientName"));
						recipientInf.setPostalCode(request.getParameter("newPostalCode"));
						recipientInf.setAddress(request.getParameter("newAddress"));
						recipientInf.setEmail(request.getParameter("newEmail"));
						session.setAttribute("recipientInf", recipientInf);
					}
				}
			}
			//支払方法決定
			String paymentType = request.getParameter("paymentType");
			paymentType = paymentType.equals("credit") ? "クレジットカード"
					: paymentType.equals("cod") ? "代金引換（代引き）" : "コンビニ決済";
			orderInf.setSelectedPayment(paymentType);
			if (paymentType == "クレジットカード") {
				int selectedCardId = Integer.parseInt(
						request.getParameter("selectedCardId"));
				List<JavaBeans.Card> cardList = (List<JavaBeans.Card>) session.getAttribute("cardList");
				JavaBeans.Card orderCardInf = new JavaBeans.Card();
				for (JavaBeans.Card card : cardList) {
					if (card.getCardId() == selectedCardId) {
						orderCardInf.setId(card.getId());
						orderCardInf.setCardId(card.getCardId());
						orderCardInf.setNumber(card.getNumber());
						orderCardInf.setExpiry(card.getExpiry());
						orderCardInf.setHolder(card.getHolder());
						session.setAttribute("orderCardInf", orderCardInf);
					}
				}
			}
			int userId = userInf.getId();
			DAO.Cart cartDao = new DAO.Cart();
			cartDao.clearCartData(userId);
			session.setAttribute("orderInf", orderInf);
		}
		case "modifyOrder" -> {//確認から入力画面へ戻り内容修正
			forwardPath = "order_input";
		}
		case "saveAddressForm" -> {//購入時の新しいアドレスを登録
			forwardPath = "order_complete";
			JavaBeans.Address newAddressInf = (JavaBeans.Address) session.getAttribute("newAddressInf");
			JavaBeans.User userInf = (JavaBeans.User) session.getAttribute("userInf");
			DAO.Address addressDao = new DAO.Address();
			addressDao.addAddress(newAddressInf, userInf);
			session.removeAttribute("newAddressInf");
		}
		case "orderComplete" -> {
			forwardPath = "order_complete";
			request.setCharacterEncoding("UTF-8");
			StringBuilder selectBooks = new StringBuilder();//メールテキスト用意
			//在庫データベース更新・メールテキスト作成
			List<JavaBeans.Book> bookCartList = (List<JavaBeans.Book>) session.getAttribute("bookCartList");
			DAO.Book bookDao = new DAO.Book();
			for (JavaBeans.Book book : bookCartList) {
				String isbn = book.getIsbn();
				int cartCnt = book.getCartCnt();
				if (cartCnt > 0) {
					bookDao.updateStock(isbn, cartCnt);
					selectBooks.append(book.getTitle()).append("　　")
							.append(book.getPrice()).append("　　")
							.append(book.getCartCnt()).append("冊\n");//メールテキスト更新
				}
			}
			//メール送信
			Servlet.Mail sender = new Servlet.Mail(host, port, user, password, charset);
			JavaBeans.Order orderInf = (JavaBeans.Order) session.getAttribute("orderInf");
			sender.sendMailForBuyer(bookCartList, orderInf);
			if (orderInf.getIsRecipient() == true) {
				JavaBeans.Recipient recipientInf = (JavaBeans.Recipient) session.getAttribute("recipientInf");
				sender.sendMailForRecipient(bookCartList, orderInf, recipientInf);
			}
			//情報の初期化
			for (JavaBeans.Book book : bookCartList) {
				book.setBeforeCartCnt(0);
				book.setCartCnt(0);
			}
			boolean isNewAddress = (boolean) session.getAttribute("isNewAddress");
			if (isNewAddress) {
				session.setAttribute("newAddressMessage", "新しいお届け先をマイページに保存しますか？");
			}
			session.setAttribute("bookCartList", bookCartList);//カート情報部分初期化				
			session.setAttribute("totalPrice", 0);//初期化
			session.setAttribute("totalQuantity", 0);//初期化
			session.setAttribute("updateCart", false);//初期化
		}
		case "register_complete" -> {
			forwardPath = "register_complete";
		}
		case "register_confirm" -> {
			forwardPath = "register_confirm";
		}
		case "register_input" -> {
			forwardPath = "register_input";
		}
		default -> {
			forwardPath = "top";
		}
		}
		;
		//画面遷移のためdoGetへ移動
		response.sendRedirect(request.getContextPath() + "/BookServlet?action=" + forwardPath);

	}
}
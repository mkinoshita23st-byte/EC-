package Servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
		@Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			String forwardPath = null;
			String action = request.getParameter("action");
			HttpSession session = request.getSession();
			switch (action) {
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
			case "logoutAfterPurchase" -> {
				forwardPath = "logout_success";
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
				forwardPath = "logout_success";
				//カート情報データベース更新
				List<JavaBeans.Book> bookCartList = (List<JavaBeans.Book>) session.getAttribute("bookCartList");
				for (JavaBeans.Book book : bookCartList) {
					System.out.println( book.getIsbn() + book.getCartCnt());
				}
				JavaBeans.Order orderInf = new JavaBeans.Order();
				int userId = ((JavaBeans.User) session.getAttribute("userInf")).getId();
				DAO.Cart cartDao = new DAO.Cart();
				for (JavaBeans.Book book : bookCartList) {
					if (book.getCartCnt() > 0) {
						cartDao.updateCartData(userId, book.getIsbn(), book.getCartCnt());
					}
				}
				//初期化
				for (JavaBeans.Book book : bookCartList) {
					book.setBeforeCartCnt(0);
					book.setCartCnt(0);
				}
				for (JavaBeans.Book book : bookCartList) {
					System.out.println("isbn:" + book.getIsbn() + " cartCnt:" + book.getCartCnt());
				}
				session.setAttribute("bookCartList", bookCartList);//カート情報部分削除
				session.setAttribute("orderInf", orderInf);//初期化
				session.setAttribute("totalPrice", 0);//初期化
				session.setAttribute("totalQuantity", 0);//初期化
				session.setAttribute("updateCart", false);//初期化
				session.setAttribute("logoutMessage", "");
				//削除
				session.removeAttribute("addressList");
				session.removeAttribute("cardList");
				session.removeAttribute("recipientInf");
				session.removeAttribute("userInf");
				session.removeAttribute("orderCardInf");
			}
			default -> {
				forwardPath = "top";
			}
			}
			;
			//画面遷移のためdoGetへ移動
			response.sendRedirect(request.getContextPath() + "/MainServlet?action=" + forwardPath);

		}
}

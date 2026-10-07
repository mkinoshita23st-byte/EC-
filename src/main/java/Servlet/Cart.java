package Servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/CartServlet")
public class Cart extends HttpServlet {

	private static final long serialVersionUID = 1L;
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
			default -> {
				forwardPath = "top";
			}
			}
			;
			//画面遷移のためdoGetへ移動
			response.sendRedirect(request.getContextPath() + "/MainServlet?action=" + forwardPath);

		}
}

package Servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/MainServlet")
public class Main extends HttpServlet {

	private static final long serialVersionUID = 1L;

	//画面遷移
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String forwardPath = null;
		String action = request.getParameter("action");
		HttpSession session = request.getSession();
		//actionがnullの場合は最低限の情報を取得しトップページに遷移する
		if (action == null) {
			forwardPath = "jsp/top.jsp";
			JavaBeans.Order orderInf = new JavaBeans.Order();
			orderInf.setIsLogin(false);
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
			case "add_newaddress" -> "jsp/add_newaddress.jsp";
			case "book_list" -> "jsp/book_list.jsp";
			case "cart_view" -> "jsp/cart_view.jsp";
			case "login_error" -> "jsp/login_error.jsp";
			case "login_success" -> "jsp/login_success.jsp";
			case "login" -> "jsp/login.jsp";
			case "logout_success" -> "jsp/logout_success.jsp";
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
}

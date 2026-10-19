package Servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/RegisterServlet")
public class Register extends HttpServlet {
		private static final long serialVersionUID = 1L;	
		
		//データベース更新、メール送信など処理
		@Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			String forwardPath = null;
			String action = request.getParameter("action");
			HttpSession session = request.getSession();

			switch (action) {
			case "register_complete" -> {
				forwardPath = "register_complete";
			}
			case "register_confirm" -> {
				forwardPath = "register_confirm";
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


import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AuthFilter implements Filter {
	private String path=null;
	
    @Override
    public void init(FilterConfig config) {
       this.path=config.getInitParameter("path");
    }
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException,IOException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String action = req.getParameter("action");
        // ログインチェック
        if ("orderInput".equals(action)) {
            HttpSession session = req.getSession();
            JavaBeans.Order orderList = (JavaBeans.Order) session.getAttribute("orderList");            
            if (!orderList.isLogin()) {
                // 未ログインの場合,ログイン画面へリダイレクト
                session.setAttribute("loginMessage", "購入画面に進むにはログインしてください");
                res.sendRedirect(req.getContextPath() + "/BookServlet?action=login");
                return;
            }
        }
        // BookServletへ
        chain.doFilter(request, response);
    }


}
package Servlet;
import java.io.IOException;
import java.util.ArrayList;
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
public class Book extends HttpServlet{

		private static final long serialVersionUID=1L;
		
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
		protected void doGet(HttpServletRequest request,HttpServletResponse response)
		throws ServletException,IOException{
			String forwardPath=null;
			String action =request.getParameter("action");
			HttpSession session=request.getSession();
			
			if(action==null) {
				forwardPath="jsp/top.jsp";
				List<JavaBeans.Cart> cartList=new ArrayList<>();
				JavaBeans.Cart cart = new JavaBeans.Cart();
				cartList.add(cart);
				JavaBeans.Order orderInf = new JavaBeans.Order();
				DAO.Book bookDao=new DAO.Book();
				List<JavaBeans.Book> bookList=bookDao.getBookData();
				for (JavaBeans.Book book : bookList) {
					book.setBeforeCartCnt(0);
				    book.setCartCnt(0);
				}
				int totalPrice=0;
				int totalQuantity=0;
				boolean updateCart=false;
				//情報セット　画面遷移時にもって行ってもらう
				session.setAttribute("cartList", cartList);
				session.setAttribute("orderInf", orderInf);
				session.setAttribute("bookList", bookList);
				session.setAttribute("totalPrice", totalPrice);
				session.setAttribute("totalQuantity",totalQuantity);
				session.setAttribute("updateCart", updateCart);
				session.setAttribute("fromCart", false);
				for (JavaBeans.Cart c : cartList) {  
				    System.out.println(c.getCartId() + ", " + c.getId() + ", " + c.getIsbn() + ", " + c.getQuantity());
				}
				System.out.println(orderInf.getName() + ", " + orderInf.getEmail() + ", " + orderInf.isLogin());
				for (JavaBeans.Book b : bookList) {  
				    System.out.println(b.getIsbn() + ", " + b.getTitle() + ", " + b.getPrice() + ", " + b.getStock() + ", " + b.getCartCnt());
				}
			}else {
				forwardPath=switch(action) {
					case "book_list"            ->"jsp/book_list.jsp";
					case "cart_view"            ->"jsp/cart_view.jsp";
					case "login_error"         ->"jsp/login_error.jsp";
					case "login_success"         ->"jsp/login_success.jsp";
					case "login"                ->"jsp/login.jsp";
					case "logout_complete" 	    ->"jsp/logout_complete.jsp";
					case "logout"               ->"jsp/logout.jsp";
					case "mypage_edit_complete" ->"jsp/mypage_edit_complete.jsp";
					case "mypage_edit_confirm"  ->"jsp/mypage_edit_confirm.jsp";
					case "mypage_edit"          ->"jsp/mypage_edit.jsp";
					case "mypage_view"          ->"jsp/mypage_view.jsp";
					case "order_complete"       ->"jsp/order_complete.jsp";
					case "order_confirm"        ->"jsp/order_confirm.jsp";
					case "order_input"        ->"jsp/order_input.jsp";
					case "register_complete"    ->"jsp/register_complete.jsp";
					case "register_confirm"     ->"jsp/register_confirm.jsp";
					case "register_input"       ->"jsp/register_input.jsp";
					default->"jsp/top.jsp";
				};
			}
			//指定された画面へ情報をもって移動
			RequestDispatcher dispatcher=request.getRequestDispatcher(forwardPath);
			dispatcher.forward(request,response);
		}
		
		//データベース更新、メール送信など処理
		@Override
		protected void doPost(HttpServletRequest request,HttpServletResponse response)
		throws ServletException,IOException{
		String forwardPath=null;
		String action =request.getParameter("action");
		HttpSession session=request.getSession();
			
		switch(action) {
			case "inCart"            ->{//カートに入れる
				forwardPath="book_list";
				List<JavaBeans.Book> bookList=(List<JavaBeans.Book>)session.getAttribute("bookList");
				int totalPrice=0;
				int totalQuantity=0;
				for (JavaBeans.Book book : bookList) {  
					String isbn = request.getParameter(book.getIsbn());
					int inputCnt = (isbn!= null && !isbn.isEmpty()) ? Integer.parseInt(isbn) : 0;
				    int cartCnt = book.getCartCnt()+inputCnt;
				    book.setCartCnt(cartCnt);
				    totalPrice+=book.getCartCnt()*book.getPrice();
				    totalQuantity+=book.getCartCnt();
				}
				session.setAttribute("bookList", bookList);
				session.setAttribute("totalPrice", totalPrice);
				session.setAttribute("totalQuantity",totalQuantity);
			}
			case "updateCart"            ->{//カートを更新
				forwardPath="cart_view";
				List<JavaBeans.Book> bookList=(List<JavaBeans.Book>)session.getAttribute("bookList");
				int totalPrice=0;
				int totalQuantity=0;
				boolean updateCart=true;
				for (JavaBeans.Book book : bookList) {       
					String isbn = request.getParameter(book.getIsbn());
					int inputCnt = (isbn!= null && !isbn.isEmpty()) ? Integer.parseInt(isbn) : 0;
				    book.setBeforeCartCnt(book.getCartCnt());//前の冊数を保存
				    book.setCartCnt(inputCnt);
				    totalPrice+=book.getCartCnt()*book.getPrice();
				    totalQuantity+=book.getCartCnt();
				}
				session.setAttribute("bookList", bookList);
				session.setAttribute("totalPrice", totalPrice);
				session.setAttribute("totalQuantity",totalQuantity);
				session.setAttribute("updateCart", updateCart);
			}
			case "cartToBookList"       ->{//カートからリストに行くだけ
		        forwardPath = "book_list";
				boolean updateCart=false;
			    session.setAttribute("updateCart", updateCart);
			}
			case "toOrder"              ->{//購入時ログイン済みか確認
				JavaBeans.Order orderInf = (JavaBeans.Order) session.getAttribute("orderInf");
			    if (orderInf.isLogin()) {
			        forwardPath = "order_input";
			    }else {
			    	forwardPath = "login";
			    	session.setAttribute("fromCart", true);
			    	session.setAttribute("loginMessage", "商品の購入にはログインが必要です。");
			    	//loginMessageはlogin.jspでremove
			    }
			    boolean updateCart=false;
			    session.setAttribute("updateCart", updateCart);
			}
			case "loginCheck"           -> {//ログインできるか、できたらどの画面に遷移するか
			    String inputId = request.getParameter("input_id");
			    String inputPass = request.getParameter("input_password");
				JavaBeans.Order orderInf = (JavaBeans.Order) session.getAttribute("orderInf");
				if(!orderInf.isLogin()) {//ログアウト状態
				    DAO.User userDao = new DAO.User();
			    	JavaBeans.LoginResult result = userDao.checkLogin(inputId, inputPass);
			    	if (result.isSuccess()){//ログイン可
			    		boolean fromCart =(boolean) session.getAttribute("fromCart");
				        if (fromCart==true) {//購入の場合
				            forwardPath = "order_input";
				            session.setAttribute("loginSuccessMessage", "ログインに成功しました");
				          //loginSuccessMessageはorder_input.jspでremove
				            session.setAttribute("fromCart",false);
				    	}else {
				            forwardPath = "login_success"; 
				        }
				    	//必要なリスト大量作成
			    	    JavaBeans.User userInf = result.getUserInf();
			    	    String userId =String.valueOf((userInf).getId());
			    	    DAO.Address addressDao = new DAO.Address();
			    	    List<JavaBeans.Address> addressList=addressDao.getAddressData(userId);
			    	    DAO.Card cardDao = new DAO.Card();
				    	List<JavaBeans.Card> cardList=cardDao.getCardData(userId);
				    	DAO.Order orderDao = new DAO.Order();
				    	orderInf = (JavaBeans.Order) orderDao.getOrderData(userId);
				    	orderInf.setLogin(result.isSuccess());
				    	session.setAttribute("userInf", userInf);//生成
			    	    session.setAttribute("addressList", addressList);//生成
			    	    session.setAttribute("cardList", cardList);//生成
			    	    session.setAttribute("orderInf", orderInf);//情報追加
			    	    //削除
				        session.removeAttribute("inputId");
				        session.removeAttribute("isReLogin");
				    	//確認用あとで消す  
				        System.out.println(userInf);
						for (JavaBeans.Address address : addressList) {  
							System.out.println(address);
						}
						for (JavaBeans.Card card : cardList) {  
							System.out.println(card);
						}
						System.out.println(orderInf);
				    } else {
				    	forwardPath = "login";
				    	session.setAttribute("inputId",inputId);
				    	session.setAttribute("isReLogin",true);
				        session.setAttribute("loginMessage", "IDまたはパスワードが違います");       
				    }
				}else {//ログイン状態の場合
					//alert("ログイン済みです")を出す
					forwardPath = "book_list";
				}
			}
			case "afterBuyed"				->{//購入完了後
				forwardPath="top";
				//BookListの内容をデータベース更新後の最新情報にする
				DAO.Book bookDao=new DAO.Book();
				List<JavaBeans.Book> bookList=bookDao.getBookData();
				for (JavaBeans.Book book : bookList) {
					book.setBeforeCartCnt(0);
				    book.setCartCnt(0);
				}
				List<JavaBeans.Cart> cartList=new ArrayList<>();
				JavaBeans.Cart cart = new JavaBeans.Cart();
				cartList.add(cart);
				JavaBeans.Order orderInf = (JavaBeans.Order) session.getAttribute("orderInf");
				orderInf.setSelectedPayment(null);
				orderInf.setRecipient(false);
				int totalPrice=0;
				int totalQuantity=0;
				boolean updateCart=false;
				//継続情報（Address,Card,Userは継続）
				session.setAttribute("bookList", bookList);//初期化
				session.setAttribute("cartList", cartList);//初期化
				session.setAttribute("orderInf", orderInf);//一部初期化
				session.setAttribute("totalPrice", totalPrice);//初期化
				session.setAttribute("totalQuantity",totalQuantity);//初期化
				session.setAttribute("updateCart", updateCart);//初期化
				//削除
				session.removeAttribute("recipientList");
			}
			case "logout" 	    ->{
				forwardPath="logout_complete";
				//セッション情報の全削除
//				session.invalidate();
				//情報の初期化
				List<JavaBeans.Cart> cartList=new ArrayList<>();
				JavaBeans.Cart cart = new JavaBeans.Cart();
				cartList.add(cart);
				List<JavaBeans.Book> bookList=(List<JavaBeans.Book>)session.getAttribute("bookList");
				for (JavaBeans.Book book : bookList) {
					book.setBeforeCartCnt(0);
				    book.setCartCnt(0);
				}
				JavaBeans.Order orderInf = new JavaBeans.Order();
				int totalPrice=0;
				int totalQuantity=0;
				boolean updateCart=false;
				session.setAttribute("cartList", cartList);//カート空
				session.setAttribute("bookList", bookList);//カート情報部分削除
				session.setAttribute("orderInf", orderInf);//初期化
				session.setAttribute("totalPrice", totalPrice);//初期化
				session.setAttribute("totalQuantity",totalQuantity);//初期化
				session.setAttribute("updateCart", updateCart);//初期化
				//削除
				session.removeAttribute("addressList");
				session.removeAttribute("cardList");
				session.removeAttribute("recipientList");
				session.removeAttribute("userInf");
			}
			case "mypage_edit_complete" ->{
				forwardPath="mypage_edit_complete";
			}
			case "mypage_edit_confirm"  ->{
				forwardPath="mypage_edit_confirm";
			}
			case "mypage_edit"          ->{
				forwardPath="mypage_edit";
			}

			case "orderInput"           ->{//
				JavaBeans.Order orderInf =(JavaBeans.Order) session.getAttribute("orderInf");
				if(orderInf.isLogin()==true) {
					forwardPath="order_input";
				}else {
					forwardPath="login";
					session.setAttribute("loginMessage", "購入画面に進むにはログインしてください");
				}
			}
			case "orderConfirm"    ->{
				forwardPath="order_confirm";
				JavaBeans.Order orderInf =(JavaBeans.Order) session.getAttribute("orderInf");
				JavaBeans.User userInf =(JavaBeans.User)session.getAttribute("userInf");
				//送付先決定
			    String addressType = request.getParameter("addressType");
			    if(addressType != null && !addressType.equals("self")) {//selfはorderInfの情報が変わらないためなにもしない
				    if(addressType.equals("registered")) {//選択送付先
				    	int selectedAddressId= Integer.parseInt(
				    			request.getParameter("selectedAddressId"));
				    	List<JavaBeans.Address> addressList=
				    			(List<JavaBeans.Address>)session.getAttribute("addressList");
				    	for(JavaBeans.Address add:addressList) {
					    	if(add.getAddressId()==selectedAddressId){
					        	boolean isSelf=add.isSelf();
					        	if(!isSelf) {//贈答用
					        		orderInf.setRecipient(true);
					        		JavaBeans.Recipient recipientInf=new JavaBeans.Recipient();
						    		recipientInf.setName(add.getName());
						    		recipientInf.setPostalCode(add.getPostalCode());
						    		recipientInf.setAddress(add.getAddress());
						    		recipientInf.setEmail(add.getEmail());
						        	session.setAttribute("recipientInf", recipientInf);
					        	}else {//住所が違う自分用
							    	orderInf.setPostalCode(add.getPostalCode());
							    	orderInf.setAddress(add.getAddress());
							    	orderInf.setEmail(add.getEmail());
					        	}
				    		}
					    }
				    }else if(addressType.equals("new")){
				    	session.setAttribute("isNewAddress", true);
				    	JavaBeans.Address newAddressInf=new JavaBeans.Address();
				    	newAddressInf.setName(request.getParameter("newRecipientName"));
				    	newAddressInf.setPostalCode(request.getParameter("newPostalCode"));
				    	newAddressInf.setAddress(request.getParameter("newAddress"));
				    	newAddressInf.setEmail(request.getParameter("newEmail"));
				    	session.setAttribute("newAddressInf", newAddressInf);
				        if(orderInf.getName()!=userInf.getAccountName()) {
				        	orderInf.setRecipient(true);
			        		JavaBeans.Recipient recipientInf=new JavaBeans.Recipient();
				    		recipientInf.setName(request.getParameter("newRecipientName"));
				    		recipientInf.setPostalCode(request.getParameter("newPostalCode"));
				    		recipientInf.setAddress(request.getParameter("newAddress"));
				    		recipientInf.setEmail(request.getParameter("newEmail"));
				        	session.setAttribute("recipientInf", recipientInf);
				        }else {
				        	
				        }
				    }
				}
			    //支払方法決定
			    String paymentType= request.getParameter("paymentType");
			    paymentType=paymentType.equals("credit")?"クレジットカード":
			    	paymentType.equals("cod")?"代金引換（代引き）":"コンビニ決済";
			    orderInf.setSelectedPayment(paymentType);			   
			    int selectedCardId= Integer.parseInt(
		    			request.getParameter("selectedCardId"));
		    	List<JavaBeans.Card> cardList=
		    			(List<JavaBeans.Card>)session.getAttribute("cardList");
		    	for(JavaBeans.Card card:cardList) {
		    		if(card.getCardId()==selectedCardId){
		    			session.setAttribute("orderInf", orderInf);
		    		}
			    }
			    
			    session.setAttribute("orderInf", orderInf);
			}
			case "saveAddressForm"			->{//購入時の新しいアドレスを登録
				forwardPath="order_complete";
				JavaBeans.Address newAddressInf =(JavaBeans.Address) session.getAttribute("newAddressInf");
				JavaBeans.User userInf =(JavaBeans.User) session.getAttribute("userInf");
				DAO.Address addressDao=new DAO.Address();
				addressDao.addAddress(newAddressInf,userInf);
				session.removeAttribute("newAddressInf");
			}
			case "orderComplete"			->{
				forwardPath="order_complete";
				JavaBeans.Order orderList =(JavaBeans.Order) session.getAttribute("orderList");
				//在庫データベース更新
				List<JavaBeans.Book> bookList = (List<JavaBeans.Book>) session.getAttribute("bookList");
				DAO.Book bookDao=new DAO.Book();
				for (JavaBeans.Book book : bookList) {
				    String isbn = book.getIsbn();         
				    int cartCnt = book.getCartCnt();
				    if(cartCnt>0) {
				    	bookDao.updateStock(isbn, cartCnt);
				    }
				}
				//メール送信
				Servlet.Mail sender = new Servlet.Mail(host, port, user, password, charset);
				sender.sendMailForBuyer(session);
				if(orderList.isRecipient()==true) {
					sender.sendMailForRecipient(session);
				}
				
				//情報の初期化
				List<JavaBeans.Cart> cartList=new ArrayList<>();
				JavaBeans.Cart cart = new JavaBeans.Cart();
				cartList.add(cart);
				for (JavaBeans.Book book : bookList) {
					book.setBeforeCartCnt(0);
				    book.setCartCnt(0);
				}
				boolean isNewAddress =(boolean)session.getAttribute("isNewAddress");
				if(isNewAddress) {
					session.setAttribute("newAddressMessage", "新しいお届け先をマイページに保存しますか？");
				}
				JavaBeans.Order newOrderInf = new JavaBeans.Order();
				int totalPrice=0;
				int totalQuantity=0;
				boolean updateCart=false;
				session.setAttribute("cartList", cartList);//カート空
				session.setAttribute("bookList", bookList);//カート情報部分削除
				session.setAttribute("orderInf", newOrderInf);//支払方法				session.setAttribute("totalPrice", totalPrice);//初期化
				session.setAttribute("totalQuantity",totalQuantity);//初期化
				session.setAttribute("updateCart", updateCart);//初期化
			}
			case "register_complete"    ->{
				forwardPath="register_complete";
			}
			case "register_confirm"     ->{
				forwardPath="register_confirm";
			}
			case "register_input"       ->{
				forwardPath="register_input";
			}
			default->{
				forwardPath="top";
			}
		};
		//画面遷移のためdoGetへ移動
		response.sendRedirect(request.getContextPath() + "/BookServlet?action=" + forwardPath);
//					
//				else if(action.equals("login")){
//					forwardPath="jsp/login.jsp";
//					DAO.Address addressDao=new DAO.Address();
//					List<JavaBeans.Address> addressList=addressDao.getAddressData();
//					DAO.Card cardDao=new DAO.Card();
//					List<JavaBeans.Card> cardList=cardDao.getCardData();
//					DAO.Recipient recipientDao=new DAO.Recipient();
//					List<JavaBeans.Recipient> recipientList=recipientDao.getRecipientData();
//					DAO.User userDao=new DAO.User();
//					List<JavaBeans.User> userList=userDao.getUserData();
//					session.setAttribute("addressList", addressList);
//					session.setAttribute("cardList", cardList);
//					session.setAttribute("recipientList", recipientList);
//					session.setAttribute("userList", userList);
//					for (JavaBeans.Address address : addressList) {  
//						System.out.println(address.getPostalCode());
//					}
//				}
//				else if(action.equals("login_complete")){
//					forwardPath="jsp/login.jsp";
//					DAO.Address addressDao=new DAO.Address();
//					List<JavaBeans.Address> addressList=addressDao.getAddressData();
//					DAO.Card cardDao=new DAO.Card();
//					List<JavaBeans.Card> cardList=cardDao.getCardData();
//					DAO.Recipient recipientDao=new DAO.Recipient();
//					List<JavaBeans.Recipient> recipientList=recipientDao.getRecipientData();
//					DAO.User userDao=new DAO.User();
//					List<JavaBeans.User> userList=userDao.getUserData();
//				}
//				else if(action.equals("updateCart")) {//カート内でカートを更新
//					forwardPath="jsp/cart.jsp";
//					List<JavaBeans.Book> BookList=(List<JavaBeans.Book>)session.getAttribute("BookList");
//					//カートの冊数更新・前の冊数保存+合計金額・総冊数更新
//					int totalPrice=0;
//					int totalQuantity=0;
//					boolean updateCart=true;
//					for (JavaBeans.Book book : BookList) {       
//						String isbn = request.getParameter(book.getIsbn());
//						int inputCnt = (isbn!= null && !isbn.isEmpty()) ? Integer.parseInt(isbn) : 0;
//					    book.setBeforeCartCnt(book.getCartCnt());//前の冊数を保存
//					    book.setCartCnt(inputCnt);//変更したカート内冊数に更新
//					    totalPrice+=book.getCartCnt()*book.getPrice();
//					    totalQuantity+=book.getCartCnt();
//					}
//					session.setAttribute("BookList", BookList);
//					session.setAttribute("totalPrice", totalPrice);
//					session.setAttribute("totalQuantity",totalQuantity);
//					session.setAttribute("updateCart", updateCart);
//				}//WEB-INF内にあるため、ブラウザから直接URL入力やa href でアクセスができない
//				else if(action.equals("top")) {//前の情報を保持したまま画面遷移
//					forwardPath="jsp/top.jsp";
//				}
//				else if(action.equals("backCatalog")) {//前の情報を保持したまま画面遷移
//					forwardPath="jsp/book_catalog.jsp";
//					boolean updateCart=false;
//					//カート更新前冊数を削除（updateCartでのみ使用）
//					List<JavaBeans.Book> BookList=(List<JavaBeans.Book>)session.getAttribute("BookList");
//					for (JavaBeans.Book book : BookList) {       
//						book.setBeforeCartCnt(0);
//					}
//					session.setAttribute("BookList", BookList);	
//					session.setAttribute("updateCart", updateCart);
//				}
//				else if(action.equals("backCart")) {//前の情報を保持したまま画面遷移
//					forwardPath="jsp/cart.jsp";
//				}
//				else if(action.equals("toCheckout")) {//カートから購入画面へ
//					forwardPath="jsp/checkout.jsp";
//					List<JavaBeans.Book> BookList = (List<JavaBeans.Book>) session.getAttribute("BookList");
//					//合計金額・総冊数を計算+カート更新前冊数を削除（updateCartでのみ使用）
//					int totalPrice=0;
//					int totalQuantity=0;
//					boolean updateCart=false;
//					for (JavaBeans.Book book : BookList) {  
//					    totalPrice+=book.getCartCnt()*book.getPrice();
//					    totalQuantity+=book.getCartCnt();
//					    book.setBeforeCartCnt(0);
//					}	
//					session.setAttribute("BookList", BookList);
//					session.setAttribute("totalPrice", totalPrice);
//					session.setAttribute("totalQuantity",totalQuantity);
//					session.setAttribute("updateCart", updateCart);
//				}
//				else if(action.equals("thankyou")) {//前の情報を保持したまま画面遷移
//					forwardPath="jsp/thankyou.jsp";
//					//カート情報（BookList）や合計金額だけを消して、ユーザーのログイン状態などは残したいならOK
//					//今までの情報をリセット（在庫数変更後の新しいテーブルデータを取得）
//					DAO.Book bookDao = new DAO.Book();
//				    List<JavaBeans.Book> BookList = bookDao.getBookData();
//					//最初なので全てに０をセットする
//					for (JavaBeans.Book book : BookList) {
//						book.setBeforeCartCnt(0);
//					    book.setCartCnt(0);
//					}
//					int totalPrice=0;//合計金額
//					int totalQuantity=0;//総冊数
//					boolean updateCart=false;
//					//情報セット　画面遷移時にもって行ってもらう
//					session.setAttribute("BookList", BookList);
//					session.setAttribute("totalPrice", totalPrice);
//					session.setAttribute("totalQuantity",totalQuantity);
//					session.setAttribute("updateCart", updateCart);
//				}
//				
//				
//				
//				
//				
//				
//	
//				request.setCharacterEncoding("UTF-8");
//
//				//セッションから情報取得
//				HttpSession session=request.getSession();
//				List<JavaBeans.Book> BookList = (List<JavaBeans.Book>) session.getAttribute("BookList");
//				int totalPrice = (int) session.getAttribute("totalPrice");
//				
//				//在庫データベース更新+メール送信用情報取得
//				DAO.Book bookDao=new DAO.Book();//データベース用意
//				StringBuilder selectBooks=new StringBuilder();//メールテキスト用意
//				//データ１行ずつ変更
//				for (JavaBeans.Book book : BookList) {
//				    String isbn = book.getIsbn();         
//				    int cartCnt = book.getCartCnt();
//				    //購入分だけ対象
//				    if(cartCnt>0) {
//				    	bookDao.updateStock(isbn, cartCnt);//在庫データ更新
//				    	selectBooks.append(book.getTitle()).append("　　")
//				    		.append(book.getPrice()).append("　　")
//				    		.append(book.getCartCnt()).append("冊\n");//メールテキスト更新
//				    }
//				}
//				
//				// メール添付用PDF生成
//				PDF pdfCreator = new PDF();
//				String invoicePath = pdfCreator.createInvoice(session);
//				String shippingSlipPath = pdfCreator.createShippingSlip(session);
//
//				//メール送信
//				Mail mail = new Mail(host, port, user, password, charset);
//				mail.sendMailForBuyer(session, invoicePath);
//				mail.sendMailForRecipient(session, shippingSlipPath);
//				
//				String path="/BookController?action=thankyou";
//				//thankyouページへ飛ぶためにdoGetへ移動
//				response.sendRedirect(request.getContextPath() + path);
//				session.removeAttribute("updateCart");
//			}
	}
}
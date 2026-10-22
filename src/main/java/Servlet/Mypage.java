package Servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/MypageServlet")
public class Mypage extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String forwardPath = null;
		String action = request.getParameter("action");
		HttpSession session = request.getSession();

		switch (action) {
		case "mypage_edit_complete" -> {
			forwardPath = "mypage_edit_complete";
		}
		case "confirmMypage" -> {
			forwardPath = "mypage_edit";
			//				if(基本情報に変更があったら) {
			//					String newAccountName = request.getParameter("accountName");
			//					 updateMyPage(int id,int userId,String accountName,String address,String email,String password)
			//				}
			//				if(住所の変更があったら) {
			//				 updateAddress(List<JavaBeans.Address> addressList,int changeId);
			//				}
			//				if(カードの変更があったら) {
			//					addCard(int id, String number, String expiry, String holder, Boolean isMain);
			//				}	
			//				String newAccountName = request.getParameter("accountName");
			//	            String newEmail = request.getParameter("email");
			//	            String newPostalCode = request.getParameter("postalCode");
			//	            String newAddress = request.getParameter("address");
			//	            boolean isUserChanged = !equals(currentUser.getAccountName(), newAccountName)
			//	                                 || !equals(currentUser.getEmail(), newEmail)
			//	                                 || !equals(currentUser.getPostalCode(), newPostalCode)
			//	                                 || !equals(currentUser.getAddress(), newAddress);
			//	            if (isUserChanged) {
			//	                updateMyPage(currentUser.getId(), currentUser.getUserId(), 
			//	                             newAccountName, newAddress, newEmail, currentUser.getPassword());
			//	            String mainAddressIndexStr = request.getParameter("mainAddressIndex");
			//	            int mainAddressIndex = (mainAddressIndexStr != null) ? Integer.parseInt(mainAddressIndexStr) : -1;
			//	            
			//	            List<AddressBean> updatedAddressList = new ArrayList<>();
			//	            boolean isAddressChanged = false;
			//
			//	            if (currentAddressList != null) {
			//	                for (int i = 0; i < currentAddressList.size(); i++) {
			//	                    AddressBean oldAddr = currentAddressList.get(i);
			//	                    
			//	                    int addressId = Integer.parseInt(request.getParameter("addressId_" + i));
			//	                    String name = request.getParameter("addressName_" + i);
			//	                    String postalCode = request.getParameter("addressPostalCode_" + i);
			//	                    String detail = request.getParameter("addressDetail_" + i);
			//	                    String email = request.getParameter("addressEmail_" + i);
			//	                    boolean isMain = (i == mainAddressIndex);
			//
			//	                    // 変更があるかチェック
			//	                    if (!equals(oldAddr.getName(), name) 
			//	                     || !equals(oldAddr.getPostalCode(), postalCode)
			//	                     || !equals(oldAddr.getAddress(), detail)
			//	                     || !equals(oldAddr.getEmail(), email)
			//	                     || oldAddr.getIsMain() != isMain) {
			//	                        
			//	                        isAddressChanged = true;
			//	                    }
			//
			//	                    // 更新用リストオブジェクトを作成して詰め直す
			//	                    AddressBean newAddr = new AddressBean(addressId, name, postalCode, detail, email, isMain);
			//	                    updatedAddressList.add(newAddr);
			//	                }
			//	            }
			//
			//	            if (isAddressChanged) {
			//	                // 変更があった場合のDAO呼び出し
			//	                // changeId にはメインに変更されたIDなどを渡すか、仕様に合わせて設定
			//	                int changeId = (mainAddressIndex != -1) ? updatedAddressList.get(mainAddressIndex).getId() : 0;
			//	                updateAddress(updatedAddressList, changeId);
			//	            }
			//
			//	            // ==========================================
			//	            // 3. クレジットカード情報の変更チェック & 更新
			//	            // ==========================================
			//	            String mainCardIndexStr = request.getParameter("mainCardIndex");
			//	            int mainCardIndex = (mainCardIndexStr != null) ? Integer.parseInt(mainCardIndexStr) : -1;
			//
			//	            if (currentCardList != null) {
			//	                for (int i = 0; i < currentCardList.size(); i++) {
			//	                    CardBean oldCard = currentCardList.get(i);
			//
			//	                    int cardId = Integer.parseInt(request.getParameter("cardId_" + i));
			//	                    String number = request.getParameter("cardNumber_" + i);
			//	                    String holder = request.getParameter("cardHolder_" + i);
			//	                    String expiry = request.getParameter("cardExpiry_" + i);
			//	                    boolean isMain = (i == mainCardIndex);
			//
			//	                    // 変更があるかチェック
			//	                    if (!equals(oldCard.getNumber(), number)
			//	                     || !equals(oldCard.getHolder(), holder)
			//	                     || !equals(oldCard.getExpiry(), expiry)
			//	                     || oldCard.isMain() != isMain) {
			//	                        
			//	                        // カード情報に変更があった場合（個別に更新または登録処理を実行）
			//	                        addCard(cardId, number, expiry, holder, isMain);
			//	                    }
			//	                }
			//	            }
		}
		case "addNewAddress" -> {
			forwardPath = "mypage_view";

			// 新住所情報取得
			JavaBeans.Address newAddressInf = new JavaBeans.Address();
			newAddressInf.setLastName(request.getParameter("newRecipientLastName").trim());
			newAddressInf.setFirstName(request.getParameter("newRecipientFirstName").trim());
			String middleNameOption = request.getParameter("middleNameOption");
			//ミドルネームの有無でフルネームを決定する
			if (middleNameOption.equals("exists")) {
				String newMiddleName = (request.getParameter("newRecipientMiddleName")).trim();
				newAddressInf.setMiddleName(newMiddleName);
				if (newMiddleName != null && !newMiddleName.isEmpty()) {
					newAddressInf.setName(newAddressInf.getFirstName() + "・"
							+ newAddressInf.getMiddleName() + "・" + newAddressInf.getLastName());
				} else {
					newAddressInf.setName(newAddressInf.getLastName() + " " + newAddressInf.getFirstName());
				}
			} else {//middleNameOption.equals("none")
				newAddressInf.setName(newAddressInf.getLastName() + " " + newAddressInf.getFirstName());
			}
			newAddressInf.setPostalCode(request.getParameter("newPostalCode"));
			newAddressInf.setAddress(request.getParameter("newAddress"));
			newAddressInf.setEmail(request.getParameter("newEmail"));
			//DBに追加、新しいアドレスリストを作成
			JavaBeans.User userInf = (JavaBeans.User) session.getAttribute("userInf");
			DAO.Address addressDao = new DAO.Address();
			addressDao.addAddress(newAddressInf, userInf);
			List<JavaBeans.Address> addressList = addressDao.getAddressData(userInf.getId());
			session.setAttribute("addressList", addressList);
		}
		case "addNewCard" -> {
			forwardPath = "mypage_view";
			
			//新カード情報取得
			JavaBeans.Card newCardInf = new JavaBeans.Card();
			newCardInf.setNumber(request.getParameter("newNumber"));
			newCardInf.setExpiry(request.getParameter("newExpiry"));
			newCardInf.setHolder(request.getParameter("newHolder"));
			Boolean newIsMain=Boolean.valueOf(request.getParameter("newIsMain"));
			newCardInf.setIsMain(newIsMain);
			JavaBeans.User userInf = (JavaBeans.User) session.getAttribute("userInf");
			DAO.Card cardDao = new DAO.Card();
			cardDao.addCard(newCardInf,userInf.getId());
			List<JavaBeans.Card> cardList = cardDao.getCardData(userInf.getId());
			session.setAttribute("cardList",cardList);
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

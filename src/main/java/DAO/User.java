package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import JavaBeans.LoginResult;

public class User {
	//個人情報取得
	public LoginResult checkLogin(String inputId, String inputPass){
		JavaBeans.User userInf = null; 
		boolean isSuccess = false;
		try {
			Context context=new InitialContext();
			DataSource ds = (DataSource) context.lookup(
			"java:comp/env/jdbc/abcdpool");
			try (Connection db = ds.getConnection()){
				PreparedStatement ps = db.prepareStatement(
						"SELECT * FROM usr_mypage " 
						+"WHERE user_id = ? AND password = ?;");
				ps.setString(1, inputId);
				ps.setString(2, inputPass);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						userInf = new JavaBeans.User();
						userInf.setId(rs.getInt("id"));
						userInf.setUserId(rs.getString("user_id")); 
						userInf.setFirstName(rs.getString("first_name"));
						userInf.setMiddleName(rs.getString("middle_name"));
						userInf.setLastName(rs.getString("last_name"));
						if(userInf.getMiddleName()==null || userInf.getMiddleName().isEmpty()) {
							userInf.setName(userInf.getLastName()+" "+userInf.getFirstName());
						}else {
							userInf.setName(userInf.getFirstName()+"・"+userInf.getMiddleName()+"・"+userInf.getLastName());
						}
						userInf.setPostalCode(rs.getString("postal_code"));
						userInf.setAddress(rs.getString("address"));
						userInf.setEmail(rs.getString("email"));
						userInf.setPassword(rs.getString("password"));
					}
				}
			} // catch略
			if (userInf != null) {
				isSuccess = true;
			}
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		}
		return new LoginResult(isSuccess, userInf);
	}
	//マイページ更新
	public void updateMyPage(int id,int userId,String firstName,String middleName,String lastName,
			String address,String email,String password) {
		try {
			Context context=new InitialContext();
			DataSource ds=(DataSource)context.lookup(
					"java:comp/env/jdbc/abcdpool");
			try (Connection db=ds.getConnection()){
				PreparedStatement ps=db.prepareStatement(
					"UPDATE usr_mypage SET user_id=?,first_name=?,middle_name=?,last_name=?,"
					+ "address=?,email=?,password=? WHERE id = ?;");
	            // 各項目（?）に更新情報をセット
	            ps.setInt(1,userId);
	            ps.setString(2,firstName);
	            ps.setString(3,middleName);
	            ps.setString(4,lastName);
	            ps.setString(5,address);
	            ps.setString(6,email);
	            ps.setString(7,password);
	            ps.setInt(8,id);
	            ps.executeUpdate();
			}//catch略
		}catch(NamingException | SQLException e){
			e.printStackTrace();
		}
	}
}


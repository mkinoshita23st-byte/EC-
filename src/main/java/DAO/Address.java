package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Address {
	//宛先情報取得
	public List<JavaBeans.Address>getAddressData(String id){
		List<JavaBeans.Address> list=new ArrayList<>();
		try {
			Context context=new InitialContext();
			DataSource ds=(DataSource)context.lookup(
					"java:comp/env/jdbc/abcdpool");
				try (Connection db=ds.getConnection()){
					PreparedStatement ps=db.prepareStatement(
							"SELECT * FROM usr_addresses WHERE id = ? AND is_main = 0");
					ps.setString(1,id);
					try (ResultSet rs = ps.executeQuery()) {
	                    while (rs.next()) {
							JavaBeans.Address address=new JavaBeans.Address();
							address.setId(rs.getInt("id"));
							address.setAddressId(rs.getInt("address_id"));
							address.setName(rs.getString("name"));
							address.setPostalCode(rs.getString("postal_code"));
							address.setAddress(rs.getString("address"));
							address.setEmail(rs.getString("email"));
							address.setMain(rs.getBoolean("is_main"));
							address.setSelf(rs.getBoolean("is_self"));
							list.add(address);
	                    }
					}//catch略
				}//catch略
		}catch(NamingException | SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	//宛先更新(mainの変更アリだと元mainをfalseにする処理が必要(呼び出し先に任せる？）
	public void updateAddress(List<JavaBeans.Address> addressList,int changeId) {

		try {
			Context context=new InitialContext();
			DataSource ds=(DataSource)context.lookup(
					"java:comp/env/jdbc/abcdpool");
			try (Connection db=ds.getConnection()){
				PreparedStatement ps=db.prepareStatement(
						"UPDATE usr_addresses SET address_id=?,name=?,"
						+ "postal_code=?,address=?,email=?,is_main=?,is_self=?"
						+ " WHERE id = ?;");
				for(JavaBeans.Address addr:addressList) {
					if(changeId==addr.getId()) {
			            ps.setInt(1,addr.getAddressId());
			            ps.setString(2,addr.getName());
			            ps.setString(3,addr.getPostalCode());
			            ps.setString(4,addr.getAddress());
			            ps.setString(5,addr.getEmail());
			            ps.setBoolean(6,addr.isMain());
			            ps.setBoolean(7,addr.isSelf());
			            ps.setInt(8,addr.getId());
			            ps.executeUpdate();
					}
				}
			}//catch略
		}catch(NamingException | SQLException e){
			e.printStackTrace();
		}
	}
	//新住所追加
	public void addAddress(JavaBeans.Address newAddressInf,JavaBeans.User userInf) {
		int id=userInf.getId();
//		int addressIdは自動割り振り
		String name=newAddressInf.getName();
		String postalCode=newAddressInf.getPostalCode();
		String address=newAddressInf.getAddress();
		String email=newAddressInf.getEmail();
		Boolean isMain=false;
		Boolean isSelf;
		if(name==userInf.getAccountName()) {
			isSelf=true;
		}else {
			isSelf=false;
		}
		
		try {
			Context context=new InitialContext();
			DataSource ds=(DataSource)context.lookup(
					"java:comp/env/jdbc/abcdpool");
			try (Connection db=ds.getConnection()){
				PreparedStatement ps=db.prepareStatement(
						"INSERT INTO usr_addresses (id, name, postal_code, "
						+ "address, email, is_main, is_self) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?);");
	            // 各項目（?）に更新情報をセット
	            ps.setInt(1,id);
	            ps.setString(2,name);
	            ps.setString(3,postalCode);
	            ps.setString(4,address);
	            ps.setString(5,email);
	            ps.setBoolean(6,isMain);
	            ps.setBoolean(7,isSelf);
	            ps.executeUpdate();
			}//catch略
		}catch(NamingException | SQLException e){
			e.printStackTrace();
		}
	}
}


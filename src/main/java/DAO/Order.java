package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Order {
	public JavaBeans.Order getOrderData(String id){
		JavaBeans.Order orderInf=null;
		try {
			Context context=new InitialContext();
			DataSource ds=(DataSource)context.lookup(
					"java:comp/env/jdbc/abcdpool");
				try (Connection db=ds.getConnection()){
					PreparedStatement ps=db.prepareStatement(
							"SELECT * FROM usr_mypage WHERE id = ?;");
					ps.setString(1,id);
					try (ResultSet rs = ps.executeQuery()) {
	                    if (rs.next()) {
							orderInf=new JavaBeans.Order();
							orderInf.setFirstName(rs.getString("first_name"));
							orderInf.setMiddleName(rs.getString("middle_name"));
							orderInf.setLastName(rs.getString("last_name"));
							if(orderInf.getMiddleName()==null || orderInf.getMiddleName().isEmpty()) {
								orderInf.setName(orderInf.getLastName()+" "+orderInf.getFirstName());
							}else {
								orderInf.setName(orderInf.getFirstName()+"・"+orderInf.getMiddleName()+"・"+orderInf.getLastName());
							}
							orderInf.setPostalCode(rs.getString("postal_code"));
							orderInf.setAddress(rs.getString("address"));
							orderInf.setEmail(rs.getString("email"));
	                    }
					}//catch略
				}//catch略
		}catch(NamingException | SQLException e) {
			e.printStackTrace();
		}
		return orderInf;
	}
}

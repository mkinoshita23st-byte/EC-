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

public class Book {
	//書籍情報取得
	public List<JavaBeans.Book>getBookData(){
		List<JavaBeans.Book> list=new ArrayList<>();
		try {
			Context context=new InitialContext();
			DataSource ds=(DataSource)context.lookup(
					"java:comp/env/jdbc/abcdpool");
				try (Connection db=ds.getConnection()){
					PreparedStatement ps=db.prepareStatement(
							"SELECT * FROM getbook;");
					ResultSet rs=ps.executeQuery();
					while(rs.next()) {
						JavaBeans.Book book=new JavaBeans.Book();
						book.setIsbn(rs.getString("isbn"));
						book.setTitle(rs.getString("title"));
						book.setPrice(rs.getInt("price"));
						book.setPublish(rs.getString("publish"));
						book.setPublished(rs.getString("published"));
						book.setStock(rs.getInt("stock"));
						list.add(book);
					}
				}
		}catch(NamingException | SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	//書籍在庫数更新
	public void updateStock(String isbn,int purchaseCnt) {
		try {
			Context context=new InitialContext();
			DataSource ds=(DataSource)context.lookup(
					"java:comp/env/jdbc/abcdpool");
			try (Connection db=ds.getConnection()){
				PreparedStatement ps=db.prepareStatement(
						"UPDATE getbook SET stock = stock - ? "
						+ "WHERE isbn = ?;");
	            // ?1:購入数をセット
	            ps.setInt(1,purchaseCnt);
	            // ?2:ISBN（書籍ID）をセット
	            ps.setString(2,isbn);
	            ps.executeUpdate();
			}//catch略
		}catch(NamingException | SQLException e){
			e.printStackTrace();
		}
	}
}


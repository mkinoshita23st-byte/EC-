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

public class Cart {
	// ユーザーIDに紐づくカート情報一覧の取得
	public List<JavaBeans.Cart> getCartData(String id) {
		List<JavaBeans.Cart> list = new ArrayList<>();
		try {
			Context context = new InitialContext();
			DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/abcdpool");
			try (Connection db = ds.getConnection()) {
				PreparedStatement ps = db.prepareStatement(
						"SELECT * FROM usr_cart WHERE id = ?;");
				ps.setString(1, id);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						JavaBeans.Cart cart = new JavaBeans.Cart();
						cart.setCartId(rs.getInt("cart_id"));
						cart.setId(rs.getInt("id"));
						cart.setIsbn(rs.getString("isbn"));
						cart.setQuantity(rs.getInt("quantity"));
						list.add(cart);
					}
				}
			}
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	// カート情報の上書き・追加・削除（都度更新)	
	public void updateCart(int userId, String isbn, int quantity) {
	    try {
	        Context context = new InitialContext();
	        DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/abcdpool");
	        try (Connection db = ds.getConnection()) {
	            try (PreparedStatement ps = db.prepareStatement(
	            		"SELECT cart_id, quantity FROM usr_cart WHERE id = ? AND isbn = ?;")) {
	                ps.setInt(1, userId);
	                ps.setString(2, isbn);
	                try (ResultSet rs = ps.executeQuery()) {
	                    if (rs.next()) {
	                    	int cartId = rs.getInt("cart_id");
	                        if (quantity > 0) {
	                            //上書き
	                            try (PreparedStatement psUpdate = db.prepareStatement(
	                                    "UPDATE usr_cart SET quantity = ? WHERE cart_id = ?;")) {
	                                psUpdate.setInt(1, quantity);
	                                psUpdate.setInt(2, cartId);
	                                psUpdate.executeUpdate();
	                            }
	                        } else {
	                            // 削除
	                            try (PreparedStatement psDelete = db.prepareStatement(
	                                    "DELETE FROM usr_cart WHERE cart_id = ?;")) {
	                                psDelete.setInt(1, cartId);
	                                psDelete.executeUpdate();
	                            }
	                        }
	                    } else {
	                        // 新規追加
	                        if (quantity > 0) {
	                            try (PreparedStatement psInsert = db.prepareStatement(
	                                    "INSERT INTO usr_cart (id, isbn, quantity) VALUES (?, ?, ?);")) {
	                                psInsert.setInt(1, userId);
	                                psInsert.setString(2, isbn);
	                                psInsert.setInt(3, quantity);
	                                psInsert.executeUpdate();
	                            }
	                        }
	                    }
	                }
	            }
	        }
	    } catch (NamingException | SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	// 購入完了後に全削除
	public void clearCart(String id) {
	    try {
	        Context context = new InitialContext();
	        DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/abcdpool");
	        try (Connection db = ds.getConnection()) {
	            PreparedStatement ps = db.prepareStatement(
	                    "DELETE FROM usr_cart WHERE id = ?;");
	            ps.setString(1, id);
	            ps.executeUpdate();
	        }
	    } catch (NamingException | SQLException e) {
	        e.printStackTrace();
	    }
	}
}

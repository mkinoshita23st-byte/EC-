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

//読み取り・更新・追加
public class Card {
	//カード情報取得
	public List<JavaBeans.Card>getCardData(int id){
		List<JavaBeans.Card> list=new ArrayList<>();
		try {
			Context context=new InitialContext();
			DataSource ds=(DataSource)context.lookup(
					"java:comp/env/jdbc/abcdpool");
				try (Connection db=ds.getConnection()){
					PreparedStatement ps=db.prepareStatement(
							"SELECT * FROM usr_cards WHERE id=?;");
	                ps.setInt(1, id);
					ResultSet rs=ps.executeQuery();
					while(rs.next()) {
						JavaBeans.Card card=new JavaBeans.Card();
						card.setId(rs.getInt("id"));
						card.setCardId(rs.getInt("card_id"));
						card.setNumber(rs.getString("number"));
						card.setExpiry(rs.getString("expiry"));
						card.setHolder(rs.getString("holder"));
						card.setIsMain(rs.getBoolean("is_main"));
						list.add(card);
					}
				}//catch略
		}catch(NamingException | SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	//カード更新
	public void updateCard(JavaBeans.Card cardInf,int id ) {
		try {
			Context context=new InitialContext();
			DataSource ds=(DataSource)context.lookup(
					"java:comp/env/jdbc/abcdpool");
			try (Connection db=ds.getConnection()){
				if(cardInf.getIsMain()) {
					PreparedStatement psIsMain=db.prepareStatement(
							"UPDATE usr_cards SET is_main = false WHERE id = ?");
							psIsMain.setInt(1, id);
							psIsMain.executeUpdate();
				}
				PreparedStatement ps=db.prepareStatement(
						"UPDATE usr_cards SET card_id=?,number=?,"
						+ "expiry=?,holder=?,is_main=?"
						+ " WHERE id = ?;");
	            ps.setInt(1,cardInf.getCardId());
	            ps.setString(2,cardInf.getNumber());
	            ps.setString(3,cardInf.getExpiry());
	            ps.setString(4,cardInf.getHolder());
	            ps.setBoolean(5,cardInf.getIsMain());
	            ps.setInt(6,id);
	            ps.executeUpdate();
			}//catch略
		}catch(NamingException | SQLException e){
			e.printStackTrace();
		}
      }

    // カード新規追加
    public void addCard(JavaBeans.Card cardInf,int id) {
    	try {
			Context context=new InitialContext();
			DataSource ds=(DataSource)context.lookup(
					"java:comp/env/jdbc/abcdpool");
			try (Connection db=ds.getConnection()){
				if(cardInf.getIsMain()) {
					PreparedStatement psIsMain=db.prepareStatement(
							"UPDATE usr_cards SET is_main = false WHERE id = ?");
							psIsMain.setInt(1, id);
							psIsMain.executeUpdate();
				}
				PreparedStatement ps=db.prepareStatement(
						"INSERT INTO usr_cards (id, number, expiry, holder, is_main) "
						+ "VALUES (?, ?, ?, ?, ?);");
	            ps.setString(1,cardInf.getNumber());
	            ps.setString(2,cardInf.getExpiry());
	            ps.setString(3,cardInf.getHolder());
	            ps.setBoolean(4,cardInf.getIsMain());
	            ps.setInt(5,id);
	            ps.executeUpdate();
			}//catch略
		}catch(NamingException | SQLException e){
			e.printStackTrace();
		}
    }
}



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
	public List<JavaBeans.Card>getCardData(String id){
		List<JavaBeans.Card> list=new ArrayList<>();
		try {
			Context context=new InitialContext();
			DataSource ds=(DataSource)context.lookup(
					"java:comp/env/jdbc/abcdpool");
				try (Connection db=ds.getConnection()){
					PreparedStatement ps=db.prepareStatement(
							"SELECT * FROM usr_cards WHERE id=?;");
					int Id=Integer.parseInt(id);
	                ps.setInt(1, Id);
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
	public void updateCard(int id,int cardId,String number,
			String expiry,String holder,Boolean isMain) {//sessionのcardListが必要？
		try {
			Context context=new InitialContext();
			DataSource ds=(DataSource)context.lookup(
					"java:comp/env/jdbc/abcdpool");
			try (Connection db=ds.getConnection()){
				if(isMain) {
					PreparedStatement psIsMain=db.prepareStatement(
							"UPDATE usr_cards SET is_main = false WHERE id = ?");
							psIsMain.setInt(1, id);
							psIsMain.executeUpdate();
				}
				PreparedStatement ps=db.prepareStatement(
						"UPDATE usr_cards SET card_id=?,number=?,"
						+ "expiry=?,holder=?,is_main=?"
						+ " WHERE id = ?;");
	            ps.setInt(1,cardId);
	            ps.setString(2,number);
	            ps.setString(3,expiry);
	            ps.setString(4,holder);
	            ps.setBoolean(5,isMain);
	            ps.setInt(6,id);
	            ps.executeUpdate();
			}//catch略
		}catch(NamingException | SQLException e){
			e.printStackTrace();
		}
      }

    // カード新規追加
    public void addCard(int id, String number, String expiry, String holder, Boolean isMain) {
    	try {
			Context context=new InitialContext();
			DataSource ds=(DataSource)context.lookup(
					"java:comp/env/jdbc/abcdpool");
			try (Connection db=ds.getConnection()){
				if(isMain) {
					PreparedStatement psIsMain=db.prepareStatement(
							"UPDATE usr_cards SET is_main = false WHERE id = ?");
							psIsMain.setInt(1, id);
							psIsMain.executeUpdate();
				}
				PreparedStatement ps=db.prepareStatement(
						"INSERT INTO usr_cards (id, number, expiry, holder, is_main) "
						+ "VALUES (?, ?, ?, ?, ?);");
	            ps.setString(1,number);
	            ps.setString(2,expiry);
	            ps.setString(3,holder);
	            ps.setBoolean(4,isMain);
	            ps.setInt(5,id);
	            ps.executeUpdate();
			}//catch略
		}catch(NamingException | SQLException e){
			e.printStackTrace();
		}
    }
}



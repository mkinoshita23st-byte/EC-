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
			String expiry,String holder,Boolean isMain) {
		try {
			Context context=new InitialContext();
			DataSource ds=(DataSource)context.lookup(
					"java:comp/env/jdbc/abcdpool");
			try (Connection db=ds.getConnection()){
				PreparedStatement ps=db.prepareStatement(
						"UPDATE usr_cards SET number=?,"
						+ "expiry=?,holder=?,is_main=?"
						+ " WHERE id = ?;");
	            ps.setInt(1,cardId);
	            ps.setString(2,number);
	            ps.setString(3,expiry);
	            ps.setString(4,holder);
	            ps.setBoolean(5,isMain);
	            ps.setInt(6,id);
	            if(isMain) {
	            	ps=db.prepareStatement("UPDATE usr_cards SET is_main = false WHERE id = ?;");
	            }
	            ps.executeUpdate();
			}//catch略
		}catch(NamingException | SQLException e){
			e.printStackTrace();
		}
	}
      
            try {
                // 更新対象がメインカードに指定された場合、対象ユーザーの全カードを先にfalseへリセット
                if (Boolean.TRUE.equals(isMain)) {
                    try (PreparedStatement psReset = db.prepareStatement(resetMainSql)) {
                        psReset.setInt(1, id);
                        psReset.executeUpdate();
                    }
                }


    // カード新規追加（isMainがtrueの場合、同ユーザーの他カードを自動的にfalseへ更新）
    public void addCard(int id, String number, String expiry, String holder, Boolean isMain) {
        String resetMainSql = "UPDATE usr_cards SET is_main = false WHERE id = ?;";
        String insertSql = "INSERT INTO usr_cards (id, number, expiry, holder, is_main) VALUES (?, ?, ?, ?, ?);";

        try (Connection db = getConnection()) {
            db.setAutoCommit(false); // トランザクション開始

            try {
                // 新規カードがメインに指定された場合、対象ユーザーの既存カードをすべてfalseにリセット
                if (Boolean.TRUE.equals(isMain)) {
                    try (PreparedStatement psReset = db.prepareStatement(resetMainSql)) {
                        psReset.setInt(1, id);
                        psReset.executeUpdate();
                    }
                }

                // 新しいカード情報を登録
                try (PreparedStatement ps = db.prepareStatement(insertSql)) {
                    ps.setInt(1, id);
                    ps.setString(2, number);
                    ps.setString(3, expiry);
                    ps.setString(4, holder);
                    ps.setBoolean(5, isMain);
                    ps.executeUpdate();
                }

                db.commit(); // トランザクション完了
            } catch (SQLException e) {
                db.rollback(); // エラー時はロールバック
                throw e;
            }
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
        }
    }
}



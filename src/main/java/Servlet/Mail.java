package Servlet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import jakarta.servlet.http.HttpSession;

public class Mail {
    private String host;
    private String port;
    private String user;
    private String password;
    private String charset;

    public Mail(String host, String port, String user, String password, String charset) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.charset = charset;
    }
    
    public void sendMailForBuyer(HttpSession httpSession) {
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", this.host);
        props.setProperty("mail.smtp.port", this.port);
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.connectiontimeout", "60000");
        props.setProperty("mail.smtp.timeout", "60000");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {
        	//セッションからの情報
        	List<JavaBeans.Book> bookList=(List<JavaBeans.Book>)httpSession.getAttribute("bookList");
        	JavaBeans.Order orderList = (JavaBeans.Order) httpSession.getAttribute("orderList");
            String userEmail = orderList.getEmail();
            String userName = orderList.getName();
            StringBuilder selectBooks=new StringBuilder();//メールテキスト用意
			for (JavaBeans.Book book : bookList) {       
			    int cartCnt = book.getCartCnt();
			    //購入分だけ対象
			    if(cartCnt>0) {
			    	selectBooks.append(book.getTitle()).append("　　")
			    		.append(book.getPrice()).append("　　")
			    		.append(book.getCartCnt()).append("冊\n");//メールテキスト更新
			    }
			}
        	//注文日の取得
            LocalDate today = LocalDate.now();
            LocalDate shippingDate = today.plusDays(2);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年M月d日");
            String shippingDateStr = shippingDate.format(formatter);
            //送信メールの設定
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("no-reply@fukuokabooks.com", "福岡書店", charset));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail, userName, charset));
            message.setSubject("注文完了及び請求書のご案内", charset);
            //請求書取得
            Servlet.PDF pdf=new Servlet.PDF();
            String pdfPath = pdf.createInvoice(httpSession);
            //マルチパート作成
            MimeMultipart multipart = new MimeMultipart();

            //テキストセット
            MimeBodyPart textPart = new MimeBodyPart();
            String mailText = "注文完了のお知らせ\n"
                    + "購入者様\n\n"
                    + "この度は、福岡書店をご利用いただき誠にありがとうございます。\n\n"
                    + "<注文内容>\n"
                    + selectBooks
                    + "-------------------------------------------------------------\n"
                    + "                              合計金額 円\n\n"
                    + "発送予定日は " + shippingDateStr + " です。\n"
                    + "添付ファイルにて請求書を送付しておりますのでご確認ください。\n";
            textPart.setText(mailText, charset);
            multipart.addBodyPart(textPart);

            //添付物セット
            MimeBodyPart temp = new MimeBodyPart();
            DataHandler handler = new DataHandler(new FileDataSource(pdfPath));
            temp.setDataHandler(handler);
            temp.setFileName(MimeUtility.encodeText(handler.getName()));
            multipart.addBodyPart(temp);
            
            // メッセージにマルチパートをセットして送信
            message.setContent(multipart);
            Transport.send(message);

            // 送信完了後に一時ファイルを削除する場合
//            if (attachmentPath != null) {
//                new File(attachmentPath).delete();
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void sendMailForRecipient(HttpSession httpSession) {
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", this.host);
        props.setProperty("mail.smtp.port", this.port);
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.connectiontimeout", "60000");
        props.setProperty("mail.smtp.timeout", "60000");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {
        	//セッションからの情報
        	List<JavaBeans.Book> bookList=(List<JavaBeans.Book>)httpSession.getAttribute("bookList");
        	JavaBeans.Recipient recipientList = (JavaBeans.Recipient) httpSession.getAttribute("recipientList");
            String recipientEmail = recipientList.getEmail();
            String recipientName = recipientList.getName();
            StringBuilder selectBooks=new StringBuilder();//メールテキスト用意
			for (JavaBeans.Book book : bookList) {       
			    int cartCnt = book.getCartCnt();
			    //購入分だけ対象
			    if(cartCnt>0) {
			    	selectBooks.append(book.getTitle()).append("　　")
			    		.append(book.getPrice()).append("　　")
			    		.append(book.getCartCnt()).append("冊\n");//メールテキスト更新
			    }
			}
        	//注文日の取得
            LocalDate today = LocalDate.now();
            LocalDate shippingDate = today.plusDays(2);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年M月d日");
            String shippingDateStr = shippingDate.format(formatter);
            //送信メールの設定
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("no-reply@fukuokabooks.com", "福岡書店", charset));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail, recipientName, charset));
            message.setSubject("注文完了及び請求書のご案内", charset);
            //送り状取得
            Servlet.PDF pdf=new Servlet.PDF();
            String pdfPath = pdf.createShippingSlip(httpSession);
            //メール内容（添付物含む）を作成
            MimeMultipart multipart = new MimeMultipart();
            
            //テキストセット
            MimeBodyPart text = new MimeBodyPart();
            String mailText = "注文完了のお知らせ\n"
                    + "受取人様\n\n"
                    + "この度は、福岡書店をご利用いただき誠にありがとうございます。\n\n"
                    + "<注文内容>\n"
                    + selectBooks
                    + "-------------------------------------------------------------\n"
                    + "                              合計金額円\n\n"
                    + "発送予定日は " + shippingDateStr + " です。\n"
                    + "添付ファイルにて請求書を送付しておりますのでご確認ください。\n";
            text.setText(mailText, charset);
            multipart.addBodyPart(text);
            
            //添付物セット
            MimeBodyPart temp = new MimeBodyPart();
            DataHandler handler = new DataHandler(new FileDataSource(pdfPath));
            temp.setDataHandler(handler);
            temp.setFileName(MimeUtility.encodeText(handler.getName()));
            multipart.addBodyPart(temp);

            // メッセージにセットして送信
            message.setContent(multipart);
            Transport.send(message);
            
            // 送信完了後に一時ファイルを削除する場合
//          if (attachmentPath != null) {
//              new File(attachmentPath).delete();
//          }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

































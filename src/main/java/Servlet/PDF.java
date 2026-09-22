package Servlet;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;

import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

//C:\Users\izumi\AppData\Local\Temp
public class PDF {
	//請求書作成
    public String createInvoice(HttpSession session) 
    		throws ServletException, IOException {
        // セッション情報取得
    	JavaBeans.Order orderList = (JavaBeans.Order) session.getAttribute("orderList");
        String name = orderList.getName();
        String postalCode = orderList.getPostalCode();
        String address = orderList.getAddress();
        
        // 注文日取得
        LocalDate today = LocalDate.now();
        DateTimeFormatter todayFormatter = DateTimeFormatter.ofPattern("yyyy年M月d日");
        String todayStr = today.format(todayFormatter);

        //PDFファイル保存先設定
        //Windowsが用意する一時保存用フォルダのパスを取得(C:\Users\ユーザー名\AppData\Local\Temp)
        //OSに応じたファイルパスの区切り文字を取得(Windows\バックスラッシュ)
        //重複しないファイル名を作成
        String tempDir = System.getProperty("java.io.tmpdir");
        String pdfPath = tempDir + File.separator + "invoice_" + System.currentTimeMillis() + ".pdf";
        
        // PDFドキュメント生成
        PdfDocument pdf = new PdfDocument(new PdfWriter(pdfPath));
        // メタ情報設定
        PdfDocumentInfo info = pdf.getDocumentInfo();
        info.setTitle("請求書")
            .setSubject(name + "様")
            .setAuthor("福岡書店")
            .setCreator("iText")
            .setKeywords("iText,請求書,福岡書店");
        
        // Document作成
        Document doc = new Document(pdf);
        PdfFont font = PdfFontFactory.createFont("HeiseiKakuGo-W5", "UniJIS-UCS2-H");
        doc.setFont(font);

        //請求書ヘッダーテーブル生成
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{55f, 45f}));
        headerTable.setWidth(UnitValue.createPercentValue(100));
        //QRコード作成
        String url = "oka-kunren.ac.jp/schools/1";
        BarcodeQRCode qrCode = new BarcodeQRCode(url);
        Image qrcode = new Image(qrCode.createFormXObject(pdf));
        qrcode.setWidth(60f);
        qrcode.setHeight(60f);
        //左セル作成（購入者情報）
        Cell leftCell = new Cell();
        leftCell.setBorder(Border.NO_BORDER);
        leftCell.add(new Paragraph(name + " 様").setFontSize(14).setBold().setMarginBottom(5f));
        leftCell.add(new Paragraph(postalCode).setFontSize(10).setMultipliedLeading(1.2f));
        leftCell.add(new Paragraph(address).setFontSize(10).setMultipliedLeading(1.2f));
        headerTable.addCell(leftCell);
        //右セル作成（書店情報）
        Cell rightCell = new Cell();
        rightCell.setBorder(Border.NO_BORDER);
        	//テーブル生成
        Table sellerTable = new Table(UnitValue.createPercentArray(new float[]{35f, 65f}));
        sellerTable.setWidth(UnitValue.createPercentValue(100));
        Cell qrCell = new Cell().add(qrcode).setBorder(Border.NO_BORDER);
        Cell shopInfoCell = new Cell().add(new Paragraph("福岡書店\n福岡市東区千早4丁目24番1号１１１１１１１１１１１１１１１１１")
                .setFontSize(10)
                .setMultipliedLeading(1.2f))//行間
                .setBorder(Border.NO_BORDER);
        sellerTable.addCell(qrCell);
        sellerTable.addCell(shopInfoCell);
        rightCell.add(sellerTable);
        headerTable.addCell(rightCell);

        //[商品明細]テーブル生成
        Table detailTable = new Table(new float[] { 40f, 220f, 50f, 70f, 70f });
        detailTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
        detailTable.addHeaderCell(new Cell().add(new Paragraph("番号")));
        detailTable.addHeaderCell(new Cell().add(new Paragraph("商品名")));
        detailTable.addHeaderCell(new Cell().add(new Paragraph("数量")));
        detailTable.addHeaderCell(new Cell().add(new Paragraph("単価")));
        detailTable.addHeaderCell(new Cell().add(new Paragraph("小計")));

        detailTable.addCell(new Cell().add(new Paragraph(String.valueOf(1))));
        detailTable.addCell(new Cell().add(new Paragraph("書籍名")));
        detailTable.addCell(new Cell().add(new Paragraph(String.valueOf(4))));
        detailTable.addCell(new Cell().add(new Paragraph(String.valueOf(2590))));
        detailTable.addCell(new Cell().add(new Paragraph(String.valueOf(10360))));
        for (int i = 0; i < 3; i++) {
        	detailTable.addCell(new Cell().setBorder(Border.NO_BORDER));
        }
        detailTable.addCell(new Cell().add(new Paragraph("合計")));
        detailTable.addCell(new Cell().add(new Paragraph(String.valueOf(10360))));
        
        //バーコード生成
        Barcode128 barCode = new Barcode128(pdf);
        String barcodeNumber = "1234567890";
        barCode.setCode(barcodeNumber);
        Image barcode = new Image(barCode.createFormXObject(pdf));
        barcode.setWidth(180f);
        barcode.setHeight(50f);
        barcode.setMarginTop(25f);

        //請求書内容
        doc.add(new Paragraph("請求書")
                .setFontSize(22)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10f))
        .add(new Paragraph("注文日：" + todayStr)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginBottom(20f))
        .add(headerTable)
        .add(new Paragraph("[商品明細]")
                .setFontSize(12)
                .setBold()
                .setMarginBottom(5f))
        .add(detailTable)
        .add(barcode);
        doc.close();
        
        return pdfPath;
    }
    
 // 送り状作成
    public String createShippingSlip(HttpSession session) 
    		throws ServletException, IOException {
        // セッション情報取得
    	JavaBeans.Order orderList = (JavaBeans.Order) session.getAttribute("orderList");
    	JavaBeans.Order recipientList = (JavaBeans.Order) session.getAttribute("recipientList");
        String userName = orderList.getName();
        String recipientName = recipientList.getName();
        String postalCode = recipientList.getPostalCode();
        String address = recipientList.getAddress();
    	
        //PDFファイル保存先設定
        String tempDir = System.getProperty("java.io.tmpdir");
        String pdfPath = tempDir + File.separator + "shippingslip_" + System.currentTimeMillis() + ".pdf";
        
        //PDFドキュメント生成
        PdfDocument pdf = new PdfDocument(new PdfWriter(pdfPath));
        //メタ情報設定
        PdfDocumentInfo info = pdf.getDocumentInfo();
        info.setTitle("送り状")
            .setSubject(recipientName + "様")
            .setAuthor("福岡書店")
            .setCreator("iText")
            .setKeywords("iText,送り状,福岡書店");
        
     // Document作成
        Document doc = new Document(pdf);
        PdfFont font = PdfFontFactory.createFont("HeiseiKakuGo-W5", "UniJIS-UCS2-H");
        doc.setFont(font);

      //送り状ヘッダーテーブル作成
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{55f, 45f}));
        headerTable.setWidth(UnitValue.createPercentValue(100));
        //QRコード作成
        String url = "oka-kunren.ac.jp/schools/1";
        BarcodeQRCode qrCode = new BarcodeQRCode(url);
        Image qrcode = new Image(qrCode.createFormXObject(pdf));
        qrcode.setWidth(60f);
        qrcode.setHeight(60f);
        //左セル作成（受取人情報）
        Cell leftCell = new Cell();
        leftCell.setBorder(Border.NO_BORDER);
        leftCell.add(new Paragraph(recipientName + " 様").setFontSize(14).setBold().setMarginBottom(5f));
        leftCell.add(new Paragraph(postalCode).setFontSize(10).setMultipliedLeading(1.2f));
        leftCell.add(new Paragraph(address).setFontSize(10).setMultipliedLeading(1.2f));
        headerTable.addCell(leftCell);
        //右セル作成（QRコード情報）
        Cell rightCell = new Cell();
        rightCell.setBorder(Border.NO_BORDER);
        //ヘッダーテーブル内テーブル
        Table sellerTable = new Table(UnitValue.createPercentArray(new float[]{35f, 65f}));
        sellerTable.setWidth(UnitValue.createPercentValue(100));
        Cell qrCell = new Cell().add(qrcode).setBorder(Border.NO_BORDER);
        Cell shopInfoCell = new Cell().add(new Paragraph("福岡書店\n福岡市東区千早4丁目24番1号")
                .setFontSize(10)
                .setMultipliedLeading(1.2f))
                .setBorder(Border.NO_BORDER);
        sellerTable.addCell(qrCell);
        sellerTable.addCell(shopInfoCell);
        rightCell.add(sellerTable);
        headerTable.addCell(rightCell);
        
        //商品明細テーブル作成
        Table detailTable = new Table(new float[] { 50f, 250f, 70f });
        detailTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
        detailTable.addHeaderCell(new Cell().add(new Paragraph("番号")));
        detailTable.addHeaderCell(new Cell().add(new Paragraph("商品名")));
        detailTable.addHeaderCell(new Cell().add(new Paragraph("数量")));

        detailTable.addCell(new Cell().add(new Paragraph(String.valueOf(1))));
        detailTable.addCell(new Cell().add(new Paragraph("書籍書籍")));
        detailTable.addCell(new Cell().add(new Paragraph(String.valueOf(5))));
        
        //バーコード生成
        Barcode128 barCode = new Barcode128(pdf);
        String barcodeNumber = "1234567890";
        barCode.setCode(barcodeNumber);
        Image barcode = new Image(barCode.createFormXObject(pdf));
        barcode.setWidth(180f);
        barcode.setHeight(50f);
        barcode.setMarginTop(20f);
        
        //送り状内容
        doc.add(new Paragraph("（送り状）")
                .setFontSize(20)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER))    
        .add(headerTable)
        .add(new Paragraph("この度は、福岡書店をご利用いただき誠にありがとうございます。\n"
                + "本商品は、" + userName + "様より、"
                + "ご指定の住所にお届けするご依頼をいただいたものです。"
                + "以下の商品をお届けいたしますので、ご査収の程よろしくお願いいたします。\n")
                .setFontSize(11)
                .setMarginTop(10f))
        .add(new Paragraph("[商品明細]")
                .setFontSize(13)
                .setBold()
                .setMarginTop(15f)
                .setMarginBottom(5f))
        .add(detailTable)
        .add(barcode);
        doc.close();

        return pdfPath;
    }
}
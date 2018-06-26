package com.example.administrator.julong.util;

import com.example.administrator.julong.entity.OuterEntity;
import com.example.administrator.julong.entity.OuterentryEntity;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/10/12 0012.
 */

public class CreateOuterpdf {
    Document document = new Document();// 建立一个Document对象

    private static Font headfont ;// 设置字体大小
    private static Font keyfont;// 设置字体大小

    public CreateOuterpdf(File file) {
        document.setPageSize(PageSize.A4);// 设置页面大小
        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int maxWidth = 520;

    public PdfPCell createCell(float f, String value, Font font, int align){
        PdfPCell cell = new PdfPCell();
        cell.setFixedHeight(f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setPhrase(new Phrase(value,font));

        return cell;
    }

    public PdfPCell createCell(float f,String value,Font font){
        PdfPCell cell = new PdfPCell();
        cell.setFixedHeight(f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPhrase(new Phrase(value,font));
        return cell;
    }

    public PdfPCell createCell(float f,String value,Font font,int align,int colspan){
        PdfPCell cell = new PdfPCell();
        cell.setFixedHeight(f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value,font));
        return cell;
    }

    public PdfPCell createCell(float f,String value,Font font,int align,int colspan,boolean boderFlag){
        PdfPCell cell = new PdfPCell();
        cell.setFixedHeight(f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value,font));
        cell.setPadding(3.0f);

        if(!boderFlag){
            cell.setBorder(0);
            cell.setPaddingTop(15.0f);
            cell.setPaddingBottom(8.0f);
        }
        return cell;
    }

    public PdfPTable createTable(int colNumber){
        PdfPTable table = new PdfPTable(colNumber);
        try{
            table.setTotalWidth(maxWidth);
            table.setLockedWidth(true);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setBorder(1);
        }catch(Exception e){
            e.printStackTrace();
        }
        return table;
    }

    public void generateYCMPDF(OuterEntity outer, List<OuterentryEntity> entrylist) throws Exception{
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
        headfont = new Font(bfChinese, 18, Font.BOLD);
        keyfont = new Font(bfChinese, 12, Font.NORMAL);// 设置字体大小
        PdfPTable table = createTable(11);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        Date date = new Date();
        SimpleDateFormat bartDateFormat = new SimpleDateFormat
                ("YYYY-MM-dd");


        Paragraph docTitle = new Paragraph("出库单明细", headfont);
        docTitle.setAlignment(Element.ALIGN_CENTER);
        docTitle.setSpacingBefore(20);
        document.add(docTitle);
        int size = 25;

        PdfPCell baseCell = new PdfPCell(new Paragraph("单号", keyfont));
        baseCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        baseCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        baseCell.setColspan(2);
        baseCell.setFixedHeight(size);
        table.addCell(baseCell);

        System.out.println(outer.getFbillno());
        System.out.println(keyfont.getSize());
        PdfPCell Cell1 = new PdfPCell(new Paragraph(outer.getFbillno(), keyfont));
        Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        Cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Cell1.setColspan(9);
        Cell1.setFixedHeight(size);
        table.addCell(Cell1);



        PdfPCell Cell2 = new PdfPCell(new Paragraph( "日期", keyfont));
        Cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        Cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Cell2.setColspan(2);
        Cell2.setFixedHeight(size);
        table.addCell(Cell2);


        PdfPCell Cell3 = new PdfPCell(new Paragraph(outer.getFdate(), keyfont));
        Cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        Cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Cell3.setColspan(9);
        Cell3.setFixedHeight(size);
        table.addCell(Cell3);

        PdfPCell Cell4 = new PdfPCell(new Paragraph( "客户", keyfont));
        Cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        Cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Cell4.setColspan(2);
        Cell4.setFixedHeight(size);
        table.addCell(Cell4);

        PdfPCell Cell5 = new PdfPCell(new Paragraph(outer.getFname(), keyfont));
        Cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
        Cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Cell5.setColspan(9);
        Cell5.setFixedHeight(size);
        table.addCell(Cell5);

        PdfPCell Cell6 = new PdfPCell(new Paragraph( "仓库", keyfont));
        Cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
        Cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Cell6.setColspan(2);
        Cell6.setFixedHeight(size);
        table.addCell(Cell6);

        PdfPCell Cell7 = new PdfPCell(new Paragraph(outer.getFstock(), keyfont));
        Cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
        Cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Cell7.setColspan(9);
        Cell7.setFixedHeight(size);
        table.addCell(Cell7);

        PdfPCell Cell10 = new PdfPCell(new Paragraph( "品名", keyfont));
        Cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
        Cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Cell10.setColspan(2);
        Cell10.setFixedHeight(size);
        table.addCell(Cell10);

        PdfPCell Cell11 = new PdfPCell(new Paragraph( "规格", keyfont));
        Cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
        Cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Cell11.setFixedHeight(size);
        table.addCell(Cell11);

        PdfPCell Cell12 = new PdfPCell(new Paragraph( "数量", keyfont));
        Cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
        Cell12.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Cell12.setFixedHeight(size);
        table.addCell(Cell12);


        PdfPCell Cell17 = new PdfPCell(new Paragraph( "备注", keyfont));
        Cell17.setHorizontalAlignment(Element.ALIGN_CENTER);
        Cell17.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Cell17.setColspan(7);
        Cell17.setFixedHeight(size);
        table.addCell(Cell17);


        if(entrylist.size()>=1)
        {
            for(int i=0;i<entrylist.size();i++){

                PdfPCell Cell = new PdfPCell(new Paragraph( entrylist.get(i).getFname(), keyfont));
                Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                Cell.setColspan(2);
                Cell.setFixedHeight(size);
                table.addCell(Cell);

                Cell = new PdfPCell(new Paragraph(entrylist.get(i).getFmodel(), keyfont));
                Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                Cell.setFixedHeight(size);
                table.addCell(Cell);

                Cell = new PdfPCell(new Paragraph(String.valueOf(entrylist.get(i).getFqty()), keyfont));
                Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                Cell.setFixedHeight(size);
                table.addCell(Cell);

                Cell = new PdfPCell(new Paragraph(String.valueOf(entrylist.get(i).getFnote()), keyfont));
                Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                Cell.setColspan(7);
                Cell.setFixedHeight(size);
                table.addCell(Cell);
            }
        }
        document.add(table);
        document.newPage();
        document.close();
    }
}

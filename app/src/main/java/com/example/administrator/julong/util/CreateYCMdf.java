package com.example.administrator.julong.util;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import com.example.administrator.julong.entity.BiaotiEntity;
import com.example.administrator.julong.entity.BiaotouEntity;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

 
public class CreateYCMdf{ 
    Document document = new Document();// 建立一个Document对象
     
    private static Font headfont ;// 设置字体大小 
    private static Font keyfont;// 设置字体大小 
    private static Font keyfont2;// 设置字体大小
    private static Font thirdfont;// 设置字体大小
    private static Font  headfont1;
    private static Font  headfont2;
    private static Font keyfont11;
    private static Font keyfonts;
    private static Font keyfontc;

//    static{ 
//        BaseFont bfChinese; 
//        try { 
//            //bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED); 
//            bfChinese = BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,1",BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//            headfont = new Font(bfChinese, 16, Font.BOLD);// 设置字体大小 
////            headfont1 = new Font(bfChinese, 11, Font.NORMAL);// 设置字体大小
//            keyfont = new Font(bfChinese, 6, Font.NORMAL);// 设置字体大小 
//            thirdfont = new Font(bfChinese, 8, Font.BOLD);// 设置字体大小
//            keyfont11 = new Font(bfChinese, 10, Font.BOLD);
//        } catch (Exception e) {          
//            e.printStackTrace(); 
//        }  
//    } 
     
     
    public CreateYCMdf(File file) {
//         document.setPageSize(PageSize.A4);// 设置页面大小 
         document.setPageSize(new RectangleReadOnly(842F,595F));
         try { 
            PdfWriter.getInstance(document,new FileOutputStream(file)); 
            document.open();  
        } catch (Exception e) { 
            e.printStackTrace(); 
        }  
         
         
    } 
    int maxWidth = 520; 
     
     
     public PdfPCell createCell(float f,String value,Font font,int align){ 
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
//     public PdfPTable createTable(float[] widths){
//            PdfPTable table = new PdfPTable(widths);
//            try{
//                table.setTotalWidth(maxWidth);
//                table.setLockedWidth(true);
//                table.setHorizontalAlignment(Element.ALIGN_CENTER);
//                table.getDefaultCell().setBorder(1);
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//            return table;
//        }
     
//     public PdfPTable createBlankTable(){
//         PdfPTable table = new PdfPTable(1);
//         table.getDefaultCell().setBorder(0);
//         table.addCell(createCell(30.0f,"", keyfont));
//         table.setSpacingAfter(20.0f);
//         table.setSpacingBefore(20.0f);
//         return table;
//     }
      
     public void generateYCMPDF(BiaotouEntity biaotou, List<BiaotiEntity> entrylist) throws Exception{
         BaseFont bfChinese = //BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,1",BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
         BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//         BaseFont bfHei = BaseFont.createFont("c:/WINDOWS/fonts/SIMHEI.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
         headfont = new Font(bfChinese, 20, Font.BOLD);
         keyfont = new Font(bfChinese, 12, Font.NORMAL);// 设置字体大小
         keyfont2 = new Font(bfChinese, 16, Font.NORMAL);// 设置字体大小
         headfont1 = new Font(bfChinese, 10, Font.NORMAL);
         headfont2 = new Font(bfChinese, 6, Font.NORMAL);
         thirdfont = new Font(bfChinese, 14, Font.BOLD);
         keyfont11 = new Font(bfChinese, 12, Font.BOLD);
         keyfont11.setColor(BaseColor.RED);
         keyfonts = new Font(bfChinese, 12, Font.BOLD);
         keyfontc = new Font(bfChinese, 16, Font.BOLD);

         //单据名称
         Paragraph docTitle = new Paragraph(String.valueOf("东方巨龙国际贸易部采购合同"), headfont);
         docTitle.setAlignment(Element.ALIGN_CENTER);
         document.add(docTitle);
         
         //加入空行
         Paragraph blankRow1 = new Paragraph(15f, " ", keyfont);
         document.add(blankRow1);
         //固定部分
         
         //第一行
         PdfPTable table1 = createTable(2);
         table1.setTotalWidth(700);
         int width1[] = {40,35};
         table1.setWidths(width1);
         PdfPCell cell31 = new PdfPCell(new Paragraph(String.valueOf("办公室地址：中国义乌宾王拉链街6区36号三楼"),thirdfont));
         PdfPCell cell32 = new PdfPCell(new Paragraph(String.valueOf("仓库地址：廿三里思源路3号"),thirdfont));
         cell31.setBorder(0);
         cell32.setBorder(0);
         table1.addCell(cell31);
         table1.addCell(cell32);
         document.add(table1);
         
         
       //第二行
         PdfPTable table2 = createTable(2);
         table2.setTotalWidth(700);
         int width2[] = {15,35};
         table2.setWidths(width2);
         PdfPCell cell33 = new PdfPCell(new Paragraph(String.valueOf("送货电话：0579-85571032"),thirdfont));
         PdfPCell cell34 = new PdfPCell(new Paragraph(String.valueOf("采购手机：18260581536（江）   15151382939（盛）   18657922331（杨）"),thirdfont));
         cell33.setBorder(0);
         cell34.setBorder(0);
         table2.addCell(cell33);
         table2.addCell(cell34);
         document.add(table2);
         
         
         //第三行
         PdfPTable table3 = createTable(3);
         table3.setTotalWidth(700);
         int width3[] = {40,35,30};
         table3.setWidths(width3);
         PdfPCell cell35 = new PdfPCell(new Paragraph(String.valueOf("QQ：2260993827"),thirdfont));
         PdfPCell cell36 = new PdfPCell(new Paragraph(String.valueOf("E-mail:Julong@126.com"),thirdfont));
         PdfPCell cell37 = new PdfPCell(new Paragraph(String.valueOf("监督电话：13901461763（钱）"),thirdfont));
         cell35.setBorder(0);
         cell36.setBorder(0);
         cell37.setBorder(0);
         table3.addCell(cell35);
         table3.addCell(cell36);
         table3.addCell(cell37);
         document.add(table3);

         
       //加入空行
         Paragraph blankRow2 = new Paragraph(6f, " ", keyfont);
         document.add(blankRow2);
         
         //表头 第一行
         PdfPTable table4 = createTable(7);
         table4.setTotalWidth(700);
         int width4[] = {8,20,8,20,8,20,20};
         table4.setWidths(width4);
         PdfPCell cell38 = new PdfPCell(new Paragraph("客户唛头",keyfont));
         PdfPCell cell38s = new PdfPCell(new Paragraph(biaotou.getMt(),keyfonts));
         PdfPCell cell39 = new PdfPCell(new Paragraph("供 应 商",keyfont));
         PdfPCell cell39s = new PdfPCell(new Paragraph(biaotou.getGys(),keyfonts));
         PdfPCell cell40 = new PdfPCell(new Paragraph("店面地址",keyfont));
         PdfPCell cell40s = new PdfPCell(new Paragraph(biaotou.getDz(),keyfonts));
         PdfPCell cell40p = new PdfPCell(new Paragraph("",keyfont));
         cell38.setBorder(0);
         cell38s.setBorder(2);
         cell38s.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell38s.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell39.setBorder(0);
         cell39s.setBorder(2);
         cell39s.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell39s.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell40.setBorder(0);
         cell40s.setBorder(2);
         cell40s.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell40s.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell40p.setBorder(0);
         table4.addCell(cell38);
         table4.addCell(cell38s);
         table4.addCell(cell39);
         table4.addCell(cell39s);
         table4.addCell(cell40);
         table4.addCell(cell40s);
         table4.addCell(cell40p);
         document.add(table4);
         
         
         //表头 第二行
         PdfPTable table5 = createTable(7);
         table5.setTotalWidth(700);
         int width5[] = {8,20,8,20,8,20,20};
         table5.setWidths(width5);
         PdfPCell cell41 = new PdfPCell(new Paragraph("跟 单 员",keyfont));
         PdfPCell cell41s = new PdfPCell(new Paragraph(biaotou.getGd(),keyfonts));
         PdfPCell cell42 = new PdfPCell(new Paragraph("手    机",keyfont));
         PdfPCell cell42s = new PdfPCell(new Paragraph(biaotou.getSj(),keyfonts));
         PdfPCell cell43 = new PdfPCell(new Paragraph("电    话",keyfont));
         PdfPCell cell43s = new PdfPCell(new Paragraph(biaotou.getDh(),keyfonts));
         PdfPCell cell43p = new PdfPCell(new Paragraph("",keyfont));
         cell41.setBorder(0);
         cell41s.setBorder(2);
         cell41s.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell41s.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell42.setBorder(0);
         cell42s.setBorder(2);
         cell42s.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell42s.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell43.setBorder(0);
         cell43s.setBorder(2);
         cell43s.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell43s.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell43p.setBorder(0);
         table5.addCell(cell41);
         table5.addCell(cell41s);
         table5.addCell(cell42);
         table5.addCell(cell42s);
         table5.addCell(cell43);
         table5.addCell(cell43s);
         table5.addCell(cell43p);
         document.add(table5);
         
         
         //表头 第三行
         PdfPTable table6 = createTable(7);
         table6.setTotalWidth(700);
         int width6[] = {8,20,8,20,32,4,12};
         table6.setWidths(width6);
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
         PdfPCell cell44 = new PdfPCell(new Paragraph("签单日期",keyfont));
         PdfPCell cell44s = new PdfPCell(new Paragraph(sdf.format(Date.parse(biaotou.getFDate())),keyfonts));
         PdfPCell cell45 = new PdfPCell(new Paragraph("交货日期",keyfont));
         PdfPCell cell45s = new PdfPCell(new Paragraph(sdf.format(Date.parse(biaotou.getFdate1())),keyfonts));
         PdfPCell cell4s = new PdfPCell(new Paragraph("",keyfont));
         PdfPCell cell4sp = new PdfPCell(new Paragraph("No:",keyfonts));
         PdfPCell cell46 = new PdfPCell(new Paragraph(biaotou.getFBillNo(),keyfont11));
         cell44.setBorder(0);
         cell44s.setBorder(2);
         cell44s.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell44s.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell45.setBorder(0);
         cell45s.setBorder(2);
         cell45s.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell45s.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell4s.setBorder(0);
         cell4sp.setBorder(0);
         cell46.setBorder(0);
         table6.addCell(cell44);
         table6.addCell(cell44s);
         table6.addCell(cell45);
         table6.addCell(cell45s);
         table6.addCell(cell4s);
         table6.addCell(cell4sp);
         table6.addCell(cell46);
         document.add(table6);
         
         
         //表体
        PdfPTable table = createTable(28);
        table.setTotalWidth(800);
        table.setSpacingBefore(10);
//        Date date = new Date();
//        SimpleDateFormat bartDateFormat = new SimpleDateFormat("YYYY-MM-dd"); 
  
        int size = 20;
        
        //第一列
         PdfPCell Cell10 = new PdfPCell(new Paragraph( "品名", keyfont));
        Cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
        Cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);
         Cell10.setColspan(5);
        Cell10.setFixedHeight(size);
        table.addCell(Cell10);

         PdfPCell Cell11 = new PdfPCell(new Paragraph( "规格", keyfont));
        Cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
        Cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Cell11.setColspan(2);
        Cell11.setFixedHeight(size);
        table.addCell(Cell11);

         PdfPCell Cell12s = new PdfPCell(new Paragraph( "单位", keyfont));
         Cell12s.setHorizontalAlignment(Element.ALIGN_CENTER);
         Cell12s.setVerticalAlignment(Element.ALIGN_MIDDLE);
         Cell12s.setFixedHeight(size);
         table.addCell(Cell12s);

         PdfPCell Cell12 = new PdfPCell(new Paragraph( "件数", keyfont));
        Cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
        Cell12.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Cell12.setFixedHeight(size);
        table.addCell(Cell12);

         PdfPCell Cell13 = new PdfPCell(new Paragraph( "箱数", keyfont));
        Cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
        Cell13.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Cell13.setFixedHeight(size);
        table.addCell(Cell13);

         PdfPCell Cell14 = new PdfPCell(new Paragraph( "总数量", keyfont));
         Cell14.setHorizontalAlignment(Element.ALIGN_CENTER);
         Cell14.setVerticalAlignment(Element.ALIGN_MIDDLE);
         Cell14.setColspan(2);
         Cell14.setFixedHeight(size);
         table.addCell(Cell14);

         PdfPCell Cell15 = new PdfPCell(new Paragraph( "单价", keyfont));
         Cell15.setHorizontalAlignment(Element.ALIGN_CENTER);
         Cell15.setVerticalAlignment(Element.ALIGN_MIDDLE);
         Cell15.setColspan(2);
         Cell15.setFixedHeight(size);
         table.addCell(Cell15);
         
         PdfPCell Cell15s = new PdfPCell(new Paragraph( "总价", keyfont));
         Cell15s.setHorizontalAlignment(Element.ALIGN_CENTER);
         Cell15s.setVerticalAlignment(Element.ALIGN_MIDDLE);
         Cell15s.setColspan(2);
         Cell15s.setFixedHeight(size);
         table.addCell(Cell15s);

         PdfPCell Cell16 = new PdfPCell(new Paragraph( "体积", keyfont));
         Cell16.setHorizontalAlignment(Element.ALIGN_CENTER);
         Cell16.setVerticalAlignment(Element.ALIGN_MIDDLE);
         Cell16.setFixedHeight(size);
         table.addCell(Cell16);

         PdfPCell Cell16s = new PdfPCell(new Paragraph( "材质", keyfont));
         Cell16s.setHorizontalAlignment(Element.ALIGN_CENTER);
         Cell16s.setVerticalAlignment(Element.ALIGN_MIDDLE);
         Cell16s.setColspan(2);
         Cell16s.setFixedHeight(size);
         table.addCell(Cell16s);

         PdfPCell Cell16p = new PdfPCell(new Paragraph( "单重", keyfont));
         Cell16p.setHorizontalAlignment(Element.ALIGN_CENTER);
         Cell16p.setVerticalAlignment(Element.ALIGN_MIDDLE);
         Cell16p.setColspan(2);
         Cell16p.setFixedHeight(size);
         table.addCell(Cell16p);

         PdfPCell Cell17 = new PdfPCell(new Paragraph( "编号", keyfont));
         Cell17.setHorizontalAlignment(Element.ALIGN_CENTER);
         Cell17.setVerticalAlignment(Element.ALIGN_MIDDLE);
         Cell17.setColspan(3);
         Cell17.setFixedHeight(size);
         table.addCell(Cell17);
         
         
         PdfPCell Cell18 = new PdfPCell(new Paragraph( "备注", keyfont));
         Cell18.setHorizontalAlignment(Element.ALIGN_CENTER);
         Cell18.setVerticalAlignment(Element.ALIGN_MIDDLE);
         Cell18.setColspan(4);
         Cell18.setFixedHeight(size);
         table.addCell(Cell18);


         PdfPTable tablebt = createTable(28);
         tablebt.setTotalWidth(800);


         for(int i=0;i<12;i++){

        	 size = 24;
             DecimalFormat df = new DecimalFormat("#0.00");
             DecimalFormat df1 = new DecimalFormat("#");
             DecimalFormat df2 = new DecimalFormat("#0.000");

             PdfPCell Cell = new PdfPCell(new Paragraph(entrylist.get(i).getFName(), keyfont));
             Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             Cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
             Cell.setColspan(5);
             Cell.setFixedHeight(size);
             tablebt.addCell(Cell);

             PdfPCell Cell1 = new PdfPCell(new Paragraph(entrylist.get(i).getFmodel(), keyfont));
             Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
             Cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
             Cell1.setColspan(2);
             Cell1.setFixedHeight(size);
             tablebt.addCell(Cell1);

             PdfPCell Cell1s = new PdfPCell(new Paragraph(entrylist.get(i).getFunit(), keyfont));
             Cell1s.setHorizontalAlignment(Element.ALIGN_CENTER);
             Cell1s.setVerticalAlignment(Element.ALIGN_MIDDLE);
             Cell1s.setFixedHeight(size);
             tablebt.addCell(Cell1s);

             PdfPCell Cell2;
             if(entrylist.get(i).getJs()==0){
                 Cell2 = new PdfPCell(new Paragraph("", keyfont));
             }else {
                 Cell2 = new PdfPCell(new Paragraph(String.valueOf(entrylist.get(i).getJs()), keyfont));
             }
             Cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
             Cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
             Cell2.setFixedHeight(size);
             tablebt.addCell(Cell2);


             PdfPCell Cell3;
             if(entrylist.get(i).getXs()==0){
                 Cell3 = new PdfPCell(new Paragraph("", keyfont));
             }else{
                 Cell3 = new PdfPCell(new Paragraph(String.valueOf(entrylist.get(i).getXs()), keyfont));
             }
             Cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
             Cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
             Cell3.setFixedHeight(size);
             tablebt.addCell(Cell3);


             PdfPCell Cell4;
             if(entrylist.get(i).getJs()==0){
                 Cell4 = new PdfPCell(new Paragraph("", keyfont));
             }else{
                 double total = entrylist.get(i).getJs()*entrylist.get(i).getXs();
                 Cell4 = new PdfPCell(new Paragraph(String.valueOf(df1.format(total)), keyfont));
             }
             Cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
             Cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
             Cell4.setColspan(2);
             Cell4.setFixedHeight(size);
             tablebt.addCell(Cell4);


             PdfPCell Cell5;
             if(entrylist.get(i).getFAuxPrice()==null){
                 Cell5 = new PdfPCell(new Paragraph("", keyfont));
             }else{
                 Cell5 = new PdfPCell(new Paragraph(df2.format(Double.parseDouble(entrylist.get(i).getFAuxPrice())), keyfont));
             }
             Cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
             Cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
             Cell5.setColspan(2);
             Cell5.setFixedHeight(size);
             tablebt.addCell(Cell5);


             PdfPCell Cell5s;
             if(entrylist.get(i).getFAuxPrice()==null){
                 Cell5s = new PdfPCell(new Paragraph("", keyfont));
             }else {
                 Cell5s = new PdfPCell(new Paragraph(df.format(Double.parseDouble(entrylist.get(i).getFAmount())), keyfont));
             }
             Cell5s.setHorizontalAlignment(Element.ALIGN_CENTER);
             Cell5s.setVerticalAlignment(Element.ALIGN_MIDDLE);
             Cell5s.setColspan(2);
             Cell5s.setFixedHeight(size);
             tablebt.addCell(Cell5s);

             PdfPCell Cell6;
             double tj = entrylist.get(i).getTj();
             if(String.valueOf(tj).equals("0.0")){
               Cell6   = new PdfPCell(new Paragraph("", keyfont));
             }else {
                 Cell6 = new PdfPCell(new Paragraph(String.valueOf(df.format(tj)), keyfont));
             }
             Cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
             Cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
             Cell6.setFixedHeight(size);
             tablebt.addCell(Cell6);

             PdfPCell Cell6s = new PdfPCell(new Paragraph(entrylist.get(i).getFmaterial(), keyfont));
             Cell6s.setHorizontalAlignment(Element.ALIGN_CENTER);
             Cell6s.setVerticalAlignment(Element.ALIGN_MIDDLE);
             Cell6s.setColspan(2);
             Cell6s.setFixedHeight(size);
             tablebt.addCell(Cell6s);

             PdfPCell Cell6p;
             double sw = entrylist.get(i).getFsingleweight();
             if(String.valueOf(tj).equals("0.0")){
                 Cell6p   = new PdfPCell(new Paragraph("", keyfont));
             }else {
                 Cell6p = new PdfPCell(new Paragraph(String.valueOf(df.format(sw)), keyfont));
             }
             Cell6p.setHorizontalAlignment(Element.ALIGN_CENTER);
             Cell6p.setVerticalAlignment(Element.ALIGN_MIDDLE);
             Cell6p.setColspan(2);
             Cell6p.setFixedHeight(size);
             tablebt.addCell(Cell6p);

             PdfPCell Cell7 = new PdfPCell(new Paragraph(entrylist.get(i).getBianhao(), keyfont));
             Cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
             Cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
             Cell7.setColspan(3);
             Cell7.setFixedHeight(size);
             tablebt.addCell(Cell7);

             if(i==0) {
                 PdfPCell Cell8 = new PdfPCell(new Paragraph(entrylist.get(0).getFnote(), keyfont));
//                 Cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
//                 Cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
                 Cell8.setColspan(4);
                 Cell8.setRowspan(12);
                 Cell8.setFixedHeight(size);
                 tablebt.addCell(Cell8);
             }
           }



//         PdfPCell Cells = new PdfPCell(new Paragraph("一存根（白）", keyfont));
//         PdfPCell Cells1 = new PdfPCell(new Paragraph("二客户（红）", keyfont));
//         PdfPCell Cells2 = new PdfPCell(new Paragraph("三仓库（蓝）", keyfont));
//         Cells.setBorder(0);
//         Cells1.setBorder(0);
//         Cells2.setBorder(0);
//         table.addCell(Cells);
//         table.addCell(Cells1);
//         table.addCell(Cells2);
         
         
         document.add(table);
         document.add(tablebt);

         
         PdfPTable table7 = createTable(3);
         table7.setTotalWidth(700);
         int width7[] = {35,30,15};
         table7.setWidths(width7);
         PdfPCell cell47 = new PdfPCell(new Paragraph("双方确认：本人已阅读并同意订单背面的“注意事项”",headfont1));
         PdfPCell cell48 = new PdfPCell(new Paragraph("付款方式：中国农业银行转账（汇票）",headfont1));
         PdfPCell cell49 = new PdfPCell(new Paragraph("定金:________________",headfont1));
         cell47.setBorder(0);
         cell48.setBorder(0);
         cell49.setBorder(0);
         table7.addCell(cell47);
         table7.addCell(cell48);
         table7.addCell(cell49);
         table7.setSpacingBefore(8);
         document.add(table7);
         
         
         
         PdfPTable table8 = createTable(4);
         table8.setTotalWidth(700);
         table8.setSpacingBefore(8);
         int width8[] = {10,30,30,35};
         table8.setWidths(width8);
         PdfPCell cell5s = new PdfPCell(new Paragraph("",keyfont));
         PdfPCell cell50 = new PdfPCell(new Paragraph("采购签名_________________",keyfont));
         PdfPCell cell51 = new PdfPCell(new Paragraph("供应商签名__________________",keyfont));
         PdfPCell cell52 = new PdfPCell(new Paragraph("交货后_______天付款",keyfont));
         cell50.setBorder(0);
         cell51.setBorder(0);
         cell52.setBorder(0);
         cell5s.setBorder(0);
         table8.addCell(cell5s);
         table8.addCell(cell50);
         table8.addCell(cell51);
         table8.addCell(cell52);
         document.add(table8);

//         PdfPTable c = createTable(4);
//         c.setTotalWidth(200);
//         PdfPCell cs = new PdfPCell(new Paragraph("",keyfont));
//         cs.setColspan(4);
//         cs.setBorderWidth(2);
//         cs.setBorder(14);
//         cs.setFixedHeight(20);
//         String[] mtc2 = new String[]{"编    号:","品    名:","规    格:","内装数:","箱    规:","毛    重:"};
//         int wid1 = 50;
//         int wid2 = 30;
//         switch(biaotou.getMt()){
//             case "C":
//                 PdfPCell c1 = new PdfPCell(new Paragraph("C",keyfontc));
//                 c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//                 c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                 c1.setColspan(4);
//                 c1.setFixedHeight(wid1);
//                 c1.setBorderWidth(2);
//                 c1.setBorder(13);
//                 c.addCell(c1);
//                 String[] mtc = new String[]{"条形码:","品   名:","规   格:","内装数:","箱   规:","毛   重:"};
//                 for(int i=0;i<mtc.length;i++){
//                     c1 = new PdfPCell(new Paragraph(mtc[i],keyfont2));
//                     c1.setColspan(2);
//                     c1.setFixedHeight(wid2);
//                     c1.setBorderWidth(2);
//                     c1.setBorder(4);
//                     c.addCell(c1);
//
//                     c1 = new PdfPCell(new Paragraph("",keyfont2));
//                     c1.setColspan(2);
//                     c1.setFixedHeight(wid2);
//                     c1.setBorderWidth(2);
//                     c1.setBorder(8);
//                     c.addCell(c1);
//                 }
//
//                 c.addCell(cs);
//                 document.add(c);
//                 break;
//             case "M":
//
//                 break;
//             case "I":
//                 PdfPCell c3 = new PdfPCell(new Paragraph("I",keyfontc));
//                 c3.setHorizontalAlignment(Element.ALIGN_CENTER);
//                 c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                 c3.setColspan(4);
//                 c3.setFixedHeight(wid1);
//                 c3.setBorderWidth(2);
//                 c3.setBorder(13);
//                 c.addCell(c3);
//                 for(int i=0;i<mtc2.length;i++){
//                     c3 = new PdfPCell(new Paragraph(mtc2[i],keyfont2));
//                     c3.setColspan(2);
//                     c3.setFixedHeight(wid2);
//                     c3.setBorderWidth(2);
//                     c3.setBorder(4);
//                     c.addCell(c3);
//
//                     c3 = new PdfPCell(new Paragraph("",keyfont2));
//                     c3.setColspan(2);
//                     c3.setFixedHeight(wid2);
//                     c3.setBorderWidth(2);
//                     c3.setBorder(8);
//                     c.addCell(c3);
//                 }
//
//                 c.addCell(cs);
//                 document.add(c);
//                 break;
//             case "Q":
//                 PdfPCell c4 = new PdfPCell(new Paragraph("Q",keyfontc));
//                 c4.setHorizontalAlignment(Element.ALIGN_CENTER);
//                 c4.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                 c4.setColspan(4);
//                 c4.setFixedHeight(wid1);
//                 c4.setBorderWidth(2);
//                 c4.setBorder(13);
//                 c.addCell(c4);
//                 for(int i=0;i<mtc2.length;i++){
//                     c4 = new PdfPCell(new Paragraph(mtc2[i],keyfont2));
//                     c4.setColspan(2);
//                     c4.setFixedHeight(wid2);
//                     c4.setBorderWidth(2);
//                     c4.setBorder(4);
//                     c.addCell(c4);
//
//                     c4 = new PdfPCell(new Paragraph("",keyfont2));
//                     c4.setColspan(2);
//                     c4.setFixedHeight(wid2);
//                     c4.setBorderWidth(2);
//                     c4.setBorder(8);
//                     c.addCell(c4);
//                 }
//
//                 c.addCell(cs);
//                 document.add(c);
//                 break;
//             case "G":
//                 PdfPCell c5 = new PdfPCell(new Paragraph("G",keyfontc));
//                 c5.setHorizontalAlignment(Element.ALIGN_CENTER);
//                 c5.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                 c5.setColspan(4);
//                 c5.setFixedHeight(wid1);
//                 c5.setBorderWidth(2);
//                 c5.setBorder(13);
//                 c.addCell(c5);
//                 for(int i=0;i<mtc2.length;i++){
//                     c5 = new PdfPCell(new Paragraph(mtc2[i],keyfont2));
//                     c5.setColspan(2);
//                     c5.setFixedHeight(wid2);
//                     c5.setBorderWidth(2);
//                     c5.setBorder(4);
//                     c.addCell(c5);
//
//                     c5 = new PdfPCell(new Paragraph("",keyfont2));
//                     c5.setColspan(2);
//                     c5.setFixedHeight(wid2);
//                     c5.setBorderWidth(2);
//                     c5.setBorder(8);
//                     c.addCell(c5);
//                 }
//
//                 c.addCell(cs);
//                 document.add(c);
//                 break;
//             case "MX":
//                 PdfPCell c6 = new PdfPCell(new Paragraph("G",keyfontc));
//                 c6.setHorizontalAlignment(Element.ALIGN_CENTER);
//                 c6.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                 c6.setColspan(4);
//                 c6.setFixedHeight(wid1);
//                 c6.setBorderWidth(2);
//                 c6.setBorder(13);
//                 c.addCell(c6);
//                 String[] mtc3 = new String[]{"CODIGO:","品   名:","规   格:","内装数:","箱   规:","毛   重:"};
//                 for(int i=0;i<mtc3.length;i++){
//                     c6 = new PdfPCell(new Paragraph(mtc3[i],keyfont2));
//                     c6.setColspan(2);
//                     c6.setFixedHeight(wid2);
//                     c6.setBorderWidth(2);
//                     c6.setBorder(4);
//                     c.addCell(c6);
//
//                     c6 = new PdfPCell(new Paragraph("",keyfont2));
//                     c6.setColspan(2);
//                     c6.setFixedHeight(wid2);
//                     c6.setBorderWidth(2);
//                     c6.setBorder(8);
//                     c.addCell(c6);
//                 }
//
//                 c.addCell(cs);
//                 document.add(c);
//                 break;
//         }

        document.newPage();
        document.close();
     } 
      
//     public static void main(String[] args) throws Exception {
//         System.out.println("begin");
//         File file = new File("d:\\text1111.pdf");
//         file.createNewFile();
//         BiaotouEntity tou = new BiaotouEntity();
//         tou.setFDate("123");
//         tou.setGys("丁飞燕");
//         tou.setMt("麦大头");
//         tou.setFBillNo("POORD000018");
//         tou.setGd("lalala");
//         tou.setDz("上海市长宁区");
//         tou.setSj("454545451");
//         tou.setDh("14215115");
//         List<BiaotiEntity> entrylist = new ArrayList<>(12);
//         BiaotiEntity entity = new BiaotiEntity();
//         entity.setFmodel("钢筋");
//         entity.setTj(200);
//         entity.setFAmount("123");
//         entity.setFName("茶水杯");
//         entity.setFnote("阿姬SD卡圣诞节");
//         entity.setFAuxPrice("123");
//         entity.setFAuxQty("123");
//         entrylist.add(entity);
//         entrylist.add(new BiaotiEntity());
//         entrylist.add(new BiaotiEntity());
//         entrylist.add(new BiaotiEntity());
//         entrylist.add(new BiaotiEntity());
//         entrylist.add(new BiaotiEntity());
//         entrylist.add(new BiaotiEntity());
//         entrylist.add(new BiaotiEntity());
//         entrylist.add(new BiaotiEntity());
//         entrylist.add(new BiaotiEntity());
//         entrylist.add(new BiaotiEntity());
//         entrylist.add(new BiaotiEntity());
//        new CreateYCMdf(file).generateYCMPDF(tou,entrylist);
//        System.out.println("end");
//    }
     
     
} 
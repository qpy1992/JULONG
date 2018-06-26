package com.example.administrator.julong.util;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.example.administrator.julong.entity.BiaotiEntity;
import com.example.administrator.julong.entity.BiaotouEntity;
import com.example.administrator.julong.entity.OuterEntity;
import com.example.administrator.julong.entity.OuterentryEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * Created by Administrator on 2017/11/1 0001.
 */

public class ExcelUtil {
    public static void writeExcel(Context context, BiaotouEntity biaotou, List<BiaotiEntity> list, File file){
        try {
            WritableWorkbook wwb;
            wwb = Workbook.createWorkbook(file);
            WritableSheet sheet = wwb.createSheet("采购订单明细", 0);
            String[] title = { "单号", "日期", "供应商", "跟单员", "唛头", "品名", "规格", "数量", "箱数", "体积", "单价", "金额", "备注"};
            Label label;
            for (int i = 0; i < title.length; i++) {
                // Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
                // 在Label对象的子对象中指明单元格的位置和内容
                label = new Label(i, 0, title[i], getHeader());
                // 将定义好的单元格添加到工作表中
                sheet.addCell(label);
            }

            //写入表头
            Label orderno = new Label(0, 1, biaotou.getFBillNo());
            Label fdate = new Label(1, 1, biaotou.getFDate());
            Label fsupplier = new Label(2,1,biaotou.getGys());
            Label fgd = new Label(3, 1, biaotou.getGd());
            Label fmt = new Label(4, 1, biaotou.getMt());

            sheet.addCell(orderno);
            sheet.addCell(fdate);
            sheet.addCell(fsupplier);
            sheet.addCell(fgd);
            sheet.addCell(fmt);

            //写入表体
            for (int i = 0; i < list.size(); i++) {
                BiaotiEntity biaoti = list.get(i);

                Label fname = new Label(5, i + 1, biaoti.getFName());
                Label fmodel = new Label(6, i + 1, biaoti.getFmodel());
                Label fnumber = new Label(7, i + 1, biaoti.getFAuxQty());
                Label fxs = new Label(8, i + 1, biaoti.getXs()+"");
                Label ftj = new Label(9, i + 1, biaoti.getTj()+"");
                Label fdj = new Label(10, i + 1, biaoti.getFAuxPrice());
                Label famount = new Label(11, i + 1, biaoti.getFAmount());
                Label fnote = new Label(12, i + 1, biaoti.getFnote());

                sheet.addCell(fname);
                sheet.addCell(fmodel);
                sheet.addCell(fnumber);
                sheet.addCell(fxs);
                sheet.addCell(ftj);
                sheet.addCell(fdj);
                sheet.addCell(famount);
                sheet.addCell(fnote);
            }

            wwb.write();

            wwb.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void writeExcelck(Context context, OuterEntity outer, List<OuterentryEntity> list, File file){
        try {
            WritableWorkbook wwb;
            wwb = Workbook.createWorkbook(file);
            WritableSheet sheet = wwb.createSheet("出库单明细", 0);
            String[] title = { "单号", "日期", "客户", "仓库", "品名", "规格", "数量", "备注"};
            Label label;
            for (int i = 0; i < title.length; i++) {
                // Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
                // 在Label对象的子对象中指明单元格的位置和内容
                label = new Label(i, 0, title[i], getHeader());
                // 将定义好的单元格添加到工作表中
                sheet.addCell(label);
            }

            //写入表头
            Label orderno = new Label(0, 1, outer.getFbillno());
            Label fdate = new Label(1, 1, outer.getFdate());
            Label fsupplier = new Label(2,1,outer.getFname());
            Label fgd = new Label(3, 1, outer.getFstock());

            sheet.addCell(orderno);
            sheet.addCell(fdate);
            sheet.addCell(fsupplier);
            sheet.addCell(fgd);

            //写入表体
            for (int i = 0; i < list.size(); i++) {
                OuterentryEntity outerentry = list.get(i);

                Label fname = new Label(4, i + 1, outerentry.getFname());
                Label fmodel = new Label(5, i + 1, outerentry.getFmodel());
                Label fnumber = new Label(6, i + 1, outerentry.getFqty());
                Label fxs = new Label(7, i + 1, outerentry.getFnote());

                sheet.addCell(fname);
                sheet.addCell(fmodel);
                sheet.addCell(fnumber);
                sheet.addCell(fxs);
            }

            wwb.write();

            wwb.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }




    public static WritableCellFormat getHeader() {
        WritableFont font = new WritableFont(WritableFont.TIMES, 10,
                WritableFont.BOLD);// 定义字体
        try {
            font.setColour(Colour.BLUE);// 蓝色字体
        } catch (WriteException e1) {
            e1.printStackTrace();
        }
        WritableCellFormat format = new WritableCellFormat(font);
        try {
            format.setAlignment(jxl.format.Alignment.CENTRE);// 左右居中
            format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 上下居中
            format.setBorder(Border.ALL, BorderLineStyle.THIN,
                    Colour.BLACK);// 黑色边框
            format.setBackground(Colour.YELLOW);// 黄色背景
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }
}

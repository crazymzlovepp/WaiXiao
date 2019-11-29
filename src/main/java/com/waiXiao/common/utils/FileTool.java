package com.waiXiao.common.utils;



import java.io.*;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.regex.Pattern;

import com.waiXiao.common.Page;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;

/*  本类主要是 下载那些已经访问过的文件*/
public class FileTool {

    private static String dirPath;


    /**
     * getMethod.getResponseHeader("Content-Type").getValue()
     * 根据 URL 和网页类型生成需要保存的网页的文件名，去除 URL 中的非文件名字符
     */
    private static String getFileNameByUrl(String url, String contentType) {
        //去除 http://
        url = url.substring(7);
        //text/html 类型
        if (contentType.indexOf("html") != -1) {
            url = url.replaceAll("[\\?/:*|<>\"]", "_") + "movieManagePage.html";
            return url;
        }
        //如 application/pdf 类型
        else {
            return url.replaceAll("[\\?/:*|<>\"]", "_") + "." +
                    contentType.substring(contentType.lastIndexOf("/") + 1);
        }
    }

    /*
    *  生成目录
    * */
    private static void mkdir() {
        if (dirPath == null) {
            dirPath = Class.class.getClass().getResource("/").getPath() + "temp\\";
        }
        File fileDir = new File(dirPath);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
    }

    /**
     * 保存网页字节数组到本地文件，filePath 为要保存的文件的相对地址
     */

    public static void saveToLocal(Page page) {
        mkdir();
        String fileName =  getFileNameByUrl(page.getUrl(), page.getContentType()) ;
        String filePath = dirPath + fileName ;
        byte[] data = page.getContent();
        try {
            //Files.lines(Paths.get("D:\\jd.txt"), StandardCharsets.UTF_8).forEach(System.out::println);
            DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(filePath)));
            for (int i = 0; i < data.length; i++) {
                out.write(data[i]);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static File inputStreamToFile(InputStream inputStream,File file) throws Exception{
        if(inputStream != null){
            OutputStream os = new FileOutputStream(file);
            int byteReaders = 0;
            byte[] byteBuff = new byte[8192];
            while((byteReaders = inputStream.read(byteBuff,0,8192)) != -1){
                os.write(byteBuff,0,byteReaders);
            }
            os.close();
            inputStream.close();
        }
        return file;
    }
    /* *
    * @author :     mym
    * @date Date :  2019/11/29 13:07
    * @version :    V1.0
    * @describe :   获取单元格数据值
    * @param :      
    * @return :     
    */
    public static String getCellValueToString(XSSFCell cell) {
        Object obj = null;
        int cellType = cell.getCellType();
        switch (cellType) {
            case XSSFCell.CELL_TYPE_BLANK:
                obj = "";
                break;
            case XSSFCell.CELL_TYPE_BOOLEAN:
                obj = cell.getBooleanCellValue();
                break;
            case XSSFCell.CELL_TYPE_ERROR:
                obj = "Bad value!";
                break;
            case XSSFCell.CELL_TYPE_NUMERIC:
                obj = getValueOfNumericCell(cell);
                break;
            case XSSFCell.CELL_TYPE_FORMULA:
                try {
                    obj = getValueOfNumericCell(cell);
                } catch (IllegalStateException e) {
                    try {
                        obj = cell.getRichStringCellValue().toString();
                    } catch (IllegalStateException e2) {
                        obj = cell.getErrorCellString();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                obj = cell.getRichStringCellValue().getString();
        }
        return obj.toString();
    }


    // 获取数字类型的cell值
    private static Object getValueOfNumericCell(XSSFCell cell) {
        Boolean isDate = DateUtil.isCellDateFormatted(cell);
        Double d = cell.getNumericCellValue();
        Object o = null;
        if (isDate) {
            o = DateFormat.getDateTimeInstance()
                    .format(cell.getDateCellValue());
        } else {
            o = getRealStringValueOfDouble(d);
        }
        return o;
    }

    // 处理科学计数法与普通计数法的字符串显示，尽最大努力保持精度
    private static String getRealStringValueOfDouble(Double d) {
        String doubleStr = d.toString();
        boolean b = doubleStr.contains("E");
        int indexOfPoint = doubleStr.indexOf('.');
        if (b) {
            int indexOfE = doubleStr.indexOf('E');
            // 小数部分
            BigInteger xs = new BigInteger(doubleStr.substring(indexOfPoint
                    + BigInteger.ONE.intValue(), indexOfE));
            // 指数
            int pow = Integer.valueOf(doubleStr.substring(indexOfE
                    + BigInteger.ONE.intValue()));
            int xsLen = xs.toByteArray().length;
            int scale = xsLen - pow > 0 ? xsLen - pow : 0;
            doubleStr = String.format("%." + scale + "f", d);
        } else {
            java.util.regex.Pattern p = Pattern.compile(".0$");
            java.util.regex.Matcher m = p.matcher(doubleStr);
            if (m.find()) {
                doubleStr = doubleStr.replace(".0", "");
            }
        }
        return doubleStr;
    }
}

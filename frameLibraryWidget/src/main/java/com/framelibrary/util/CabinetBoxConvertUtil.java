package com.framelibrary.util;

/**
 * @author 范明
 * @version 1.0
 * @date 创建时间：2016年10月10日 下午11:21:20
 * @description 转换工具类，把副柜号和格口号转换成实际看见的位置，例如：0103 转换为：A02
 * @return
 */
public class CabinetBoxConvertUtil {

    /**
     * 默认一个副柜的箱子数
     */
    private static final int DEFAULT_COLUME_BOX_COUNT = 24;

    /**
     * @param columeAndBox   副柜号和格口号的拼接：0102
     * @param columeBoxCount 当前副柜的格口数
     * @return 转换的结果 A02
     */
    public static String convertShowCabinetColumeBox(String columeAndBox, int columeBoxCount) {
        if (columeBoxCount <= 0) {  // 如果格口数没传，默认为24
            columeBoxCount = DEFAULT_COLUME_BOX_COUNT;
        }
        return convertShowColumeBox(columeAndBox, columeBoxCount);
    }

    /**
     * 转换副柜方法
     *
     * @param cabinetCoulmeNo 副柜号
     * @return 转换后的结果，副柜名
     */
    public static String convertDefaultColume(String cabinetCoulmeNo) {
        String tempResult = "";
        if ("01".equals(cabinetCoulmeNo)) {
            tempResult += "AB";
        } else if ("02".equals(cabinetCoulmeNo)) {
            tempResult += "CD";
        } else if ("03".equals(cabinetCoulmeNo)) {
            tempResult += "EF";
        } else if ("04".equals(cabinetCoulmeNo)) {
            tempResult += "GH";
        } else if ("05".equals(cabinetCoulmeNo)) {
            tempResult += "IJ";
        } else if ("06".equals(cabinetCoulmeNo)) {
            tempResult += "KL";
        } else if ("07".equals(cabinetCoulmeNo)) {
            tempResult += "MN";
        } else if ("08".equals(cabinetCoulmeNo)) {
            tempResult += "OP";
        } else if ("09".equals(cabinetCoulmeNo)) {
            tempResult += "QR";
        } else if ("10".equals(cabinetCoulmeNo)) {
            tempResult += "ST";
        } else if ("11".equals(cabinetCoulmeNo)) {
            tempResult += "UV";
        } else if ("12".equals(cabinetCoulmeNo)) {
            tempResult += "WX";
        } else if ("13".equals(cabinetCoulmeNo)) {
            tempResult += "YZ";
        } else if ("00".equals(cabinetCoulmeNo)) {
            tempResult += "主柜";
        } else {
            tempResult += cabinetCoulmeNo;
        }
        return tempResult;
    }

    /**
     * 转换副柜号+格口号
     *
     * @param columeAndBox   副柜号+格口号
     * @param columeBoxCount 当前副柜的所有格口总数
     * @return 副柜名+格口名
     */
    private static String convertShowColumeBox(String columeAndBox, int columeBoxCount) {
        String tempColume = columeAndBox.substring(0, 2);
        String tempBox = columeAndBox.substring(2, 4);
        String tempResult = convertDefaultColume(tempColume);
        int cabinetBox = Integer.parseInt(tempBox);
        if (tempResult.equals(tempColume) || tempResult.equals("主柜")) {
            tempResult = tempResult + tempBox;
            return tempResult;
        } else {
            if (cabinetBox > columeBoxCount / 2) {
                tempResult = tempResult.substring(1, 2);
                cabinetBox = cabinetBox - columeBoxCount / 2;
            } else {
                tempResult = tempResult.substring(0, 1);
            }
            tempResult = tempResult + convertString(String.valueOf(cabinetBox));
            return tempResult;
        }

    }

    /**
     * 字符串转换，如果字符串长度只有1位，则在字符串前加0
     *
     * @param content
     * @return 长度为2的字符串
     */
    public static String convertString(String content) {
        if (content.length() < 2) {
            return "0" + content;
        }
        return content;
    }


    /**
     * 根据列名获取列号
     *
     * @param columnName
     * @return
     */
    public static String getCabinetCoulmeNo(String cabinetCoulmeName) {
        String tempResult = "";
        if ("AB".equals(cabinetCoulmeName)) {
            tempResult += "01";
        } else if ("CD".equals(cabinetCoulmeName)) {
            tempResult += "02";
        } else if ("EF".equals(cabinetCoulmeName)) {
            tempResult += "03";
        } else if ("GH".equals(cabinetCoulmeName)) {
            tempResult += "04";
        } else if ("IJ".equals(cabinetCoulmeName)) {
            tempResult += "05";
        } else if ("KL".equals(cabinetCoulmeName)) {
            tempResult += "06";
        } else if ("MN".equals(cabinetCoulmeName)) {
            tempResult += "07";
        } else if ("OP".equals(cabinetCoulmeName)) {
            tempResult += "08";
        } else if ("QR".equals(cabinetCoulmeName)) {
            tempResult += "09";
        } else if ("ST".equals(cabinetCoulmeName)) {
            tempResult += "10";
        } else if ("UV".equals(cabinetCoulmeName)) {
            tempResult += "11";
        } else if ("WX".equals(cabinetCoulmeName)) {
            tempResult += "12";
        } else if ("YZ".equals(cabinetCoulmeName)) {
            tempResult += "13";
        } else {
            tempResult += cabinetCoulmeName;
        }
        return tempResult;
    }

    /**
     * 根据副柜名获取列号   "B" 转换成01
     *
     * @param columnAndBox 副柜名+箱子名 "B07"
     * @param boxCount     一个副柜箱子的个数
     * @return 列号+需要加的格口数 ,例如A01==0100 01副柜号 00 表示转换成实际箱子号需要加的数 B01 == 01(boxCount/2)
     */
    private static String getColumnNumber(String columnAndBox, int boxCount) {
        int count = 0;
        String column = columnAndBox.substring(0, 1);
        if (column.equals("A") || column.equals("B")) {
            if (column.equals("B")) {
                count += boxCount / 2;
            }
            column = "01";
        } else if (column.equals("C") || column.equals("D")) {
            if (column.equals("D")) {
                count += boxCount / 2;
            }
            column = "02";
        } else if (column.equals("E") || column.equals("F")) {
            if (column.equals("F")) {
                count += boxCount / 2;
            }
            column = "03";
        } else if (column.equals("G") || column.equals("H")) {
            if (column.equals("H")) {
                count += boxCount / 2;
            }
            column = "04";
        } else if (column.equals("I") || column.equals("J")) {
            if (column.equals("J")) {
                count += boxCount / 2;
            }
            column = "05";
        } else if (column.equals("K") || column.equals("L")) {
            if (column.equals("L")) {
                count += boxCount / 2;
            }
            column = "06";
        } else if (column.equals("M") || column.equals("N")) {
            if (column.equals("N")) {
                count += boxCount / 2;
            }
            column = "07";
        } else if (column.equals("O") || column.equals("P")) {
            if (column.equals("P")) {
                count += boxCount / 2;
            }
            column = "08";
        } else if (column.equals("Q") || column.equals("R")) {
            if (column.equals("R")) {
                count += boxCount / 2;
            }
            column = "09";
        } else if (column.equals("S") || column.equals("T")) {
            if (column.equals("T")) {
                count += boxCount / 2;
            }
            column = "10";
        } else if (column.equals("U") || column.equals("V")) {
            if (column.equals("V")) {
                count += boxCount / 2;
            }
            column = "11";
        } else if (column.equals("W") || column.equals("X")) {
            if (column.equals("X")) {
                count += boxCount / 2;
            }
            column = "12";
        } else if (column.equals("Y") || column.equals("Z")) {
            if (column.equals("Z")) {
                count += boxCount / 2;
            }
            column = "13";
        }
        column += count;
        return column;
    }

    /**
     * 反转 将N07转成0719
     *
     * @param columnAndBox N07
     * @param boxCount     24
     * @return 0719
     */
    private static String reversalConvert(String columnAndBox, int boxCount) {
        String result = getColumnNumber(columnAndBox, boxCount);
        int count = Integer.parseInt(columnAndBox.substring(1, 3)) + Integer.parseInt(result.substring(2));
        return result.substring(0, 2) + count;
    }


    public static void main(String[] args) {
        System.out.println(reversalConvert("J06", 22));
    }
}

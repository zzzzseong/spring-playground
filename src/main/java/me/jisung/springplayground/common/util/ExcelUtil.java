package me.jisung.springplayground.common.util;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.Color;
import me.jisung.springplayground.common.annotation.ExcelColumn;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

    private static final int MAX_ROW_COUNT = 1_000_000;
    private static final int MIN_ROW_COUNT = 1;

    private ExcelUtil() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    /**
     * 리스트에 담긴 데이터를 POI Workbook 객체로 변환해 반환하는 메서드
     * @param list  변환 대상 리스트 - 제네릭 클래스에 @ExcelColumn 어노테이션 필요
     * @param clazz 리스트 제네릭 클래스 객체
     */
    public static void listToExcel(List<?> list, Class<?> clazz, HttpServletResponse response, String filename, String sheetName) {
        if(list.size() <= MIN_ROW_COUNT || list.size() >= MAX_ROW_COUNT) {
            throw new IllegalArgumentException("MAX_ROW_COUNT: " + MAX_ROW_COUNT + ", MIN_ROW_COUNT: " + MIN_ROW_COUNT);
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        Row header = sheet.createRow(0);
        header.setHeight((short) 500);

        List<Field> fieldList = getFieldList(clazz);

        // ADD HEADER ROW TO EXCEL
        XSSFCellStyle headerStyle = (XSSFCellStyle) workbook.createCellStyle();
        setHeaderCellStyle(headerStyle, new XSSFColor(new Color(20, 171, 177), new DefaultIndexedColorMap()));
        for (int i = 0; i < fieldList.size(); i++) {
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(fieldList.get(i).getAnnotation(ExcelColumn.class).headerName());
            headerCell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 5000);
        }

        // ADD BODY ROW TO EXCEL
        try {
            int rowCnt = 1;
            XSSFCellStyle bodyStyle = (XSSFCellStyle) workbook.createCellStyle();
            setBodyCellStyle(bodyStyle);
            for (int i = 0; i < list.size(); i++) {
                Row row = sheet.createRow(rowCnt++);
                Object element = list.get(i);

                for (int j = 0; j < fieldList.size(); j++) {
                    Cell bodyCell = row.createCell(j);
                    bodyCell.setCellStyle(bodyStyle);
                    Field field = fieldList.get(j);
                    field.setAccessible(true);
                    Object value = field.get(element);

                    if (value instanceof Number) {
                        Number numberValue = (Number) value;
                        bodyCell.setCellValue(numberValue.doubleValue());
                        continue;
                    }

                    bodyCell.setCellValue(value == null ? "" : value.toString());
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        response.setHeader("Content-Filename", filename);

        try {
            ServletOutputStream out = response.getOutputStream();
            workbook.write(out);
            workbook.close();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void setHeaderCellStyle(XSSFCellStyle cellStyle, XSSFColor color) {
        cellStyle.setFillForegroundColor(color);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
    }

    private static void setBodyCellStyle(CellStyle cellStyle) {
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    }

    private static List<Field> getFieldList(Class<?> clazz) {
        List<Field> fieldList = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ExcelColumn.class))
                .toList();

        if(fieldList.isEmpty()) throw new IllegalArgumentException("[" + clazz.getName() + "] Can't find field with @ExcelColumn annotation");

        return fieldList;
    }
}

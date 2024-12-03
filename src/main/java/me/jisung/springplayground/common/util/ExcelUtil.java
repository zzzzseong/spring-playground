package me.jisung.springplayground.common.util;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.annotation.ExcelColumn;
import me.jisung.springplayground.common.exception.Api5xxErrorCode;
import me.jisung.springplayground.common.exception.ApiException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j(topic = "ExcelUtil")
public class ExcelUtil {

    private static final int MAX_ROW_SIZE = 1_000_000;

    private static final Color HEADER_COLOR = new Color(20, 171, 177);
    private static final short HEADER_HEIGHT = 500;
    private static final int HEADER_WIDTH = 5000;

    private ExcelUtil() {}

    /**
     * 리스트에 담긴 데이터를 POI Workbook 객체로 변환해 반환하는 메서드
     * @param list  변환 대상 리스트 - 제네릭 클래스에 @ExcelColumn 어노테이션 필요
     * @param clazz 리스트 제네릭 클래스 객체
     * @param response HttpServletResponse 객체
     * @param filename 다운로드 받을 파일 이름
     * @param sheetName 엑셀 시트 이름
     */
    public static void listToExcel(List<?> list, Class<?> clazz, HttpServletResponse response, String filename, String sheetName) {
        if(list.size() >= MAX_ROW_SIZE) {
            log.error("row size is too big. current row size: {}", list.size());
            log.error("adjusting size automatically. max row size: {}", MAX_ROW_SIZE);
            list = list.subList(0, MAX_ROW_SIZE);
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        Row header = sheet.createRow(0);
        header.setHeight(HEADER_HEIGHT);

        List<Field> fieldList = getFieldList(clazz);

        // add header row
        XSSFCellStyle headerStyle = (XSSFCellStyle) workbook.createCellStyle();
        setHeaderCellStyle(headerStyle, new XSSFColor(HEADER_COLOR, new DefaultIndexedColorMap()));
        for (int i = 0; i < fieldList.size(); i++) {
            Cell headerCell = header.createCell(i);

            headerCell.setCellValue(fieldList.get(i).getAnnotation(ExcelColumn.class).headerName());
            headerCell.setCellStyle(headerStyle);

            sheet.setColumnWidth(i, HEADER_WIDTH);
        }

        // add body rows
        try {
            int rowCnt = 1;
            XSSFCellStyle bodyStyle = (XSSFCellStyle) workbook.createCellStyle();
            setBodyCellStyle(bodyStyle);

            for (Object element : list) {
                Row row = sheet.createRow(rowCnt++);

                for (int j = 0; j < fieldList.size(); j++) {
                    Cell bodyCell = row.createCell(j);
                    bodyCell.setCellStyle(bodyStyle);
                    Object value = fieldList.get(j).get(element);

                    if (value instanceof Number numberValue) {
                        bodyCell.setCellValue(numberValue.doubleValue());
                        continue;
                    }

                    bodyCell.setCellValue(String.valueOf(value));
                }
            }

            response.setContentType("ms-vnd/excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);

            ServletOutputStream out = response.getOutputStream();
            workbook.write(out);
            workbook.close();
            out.close();
        } catch (IllegalAccessException | IOException e) {
            throw new ApiException(e, Api5xxErrorCode.SERVICE_UNAVAILABLE);
        }
    }

    public static String makeFilename(String subject) {
        SimpleDateFormat now = new SimpleDateFormat(DateUtil.DATE_TIME_FORMAT);
        return subject + "-" + now.format(new Date()) + ".xlsx";
    }

    private static List<Field> getFieldList(Class<?> clazz) {
        List<Field> fieldList = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ExcelColumn.class))
                .toList();

        /* 엑셀 추출 대상 클래스에서 @ExcelColumn을 찾을 수 없는 경우 */
        if(fieldList.isEmpty()) {
            Api5xxErrorCode errorCode = Api5xxErrorCode.SERVICE_UNAVAILABLE;
            throw ApiException.builder()
                .httpStatus(errorCode.getHttpStatus())
                .code(errorCode.getCode())
                .message("[" + clazz.getName() + "] can't find field with @ExcelColumn annotation")
                .build();
        }

        for (Field field : fieldList) field.setAccessible(true);

        return fieldList;
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
}

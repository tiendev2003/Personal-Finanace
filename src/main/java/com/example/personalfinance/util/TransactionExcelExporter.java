package com.example.personalfinance.util;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import com.example.personalfinance.entity.Transaction;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TransactionExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private final List<Transaction> transactionList;

    public TransactionExcelExporter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
        this.workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Transactions");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16); // Fixed typo: setFontHeight
        style.setFont(font);

        createCell(row, 0, "Transaction ID", style);
        createCell(row, 1, "Date", style);
        createCell(row, 2, "Time", style);
        createCell(row, 3, "Amount", style);
        createCell(row, 4, "Category", style);
        createCell(row, 5, "Type", style);
        createCell(row, 6, "Description", style);
        createCell(row, 7, "Account", style);
        createCell(row, 8, "Payment Type", style);
    }

    private void createCell(Row row, int column, Object value, CellStyle style) {
        sheet.autoSizeColumn(column);
        Cell cell = row.createCell(column);

        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue(value != null ? value.toString() : "");
        }

        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (Transaction transaction : transactionList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            Instant instant = Instant.ofEpochMilli(transaction.getDateTime());
            LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
            String formattedDate = dateTime.format(dateFormatter);
            String formattedTime = dateTime.format(timeFormatter);

            createCell(row, columnCount++, transaction.getId(), style);
            createCell(row, columnCount++, formattedDate, style);
            createCell(row, columnCount++, formattedTime, style);
            createCell(row, columnCount++, transaction.getAmount(), style);
            createCell(row, columnCount++, transaction.getCategory().getName(), style);
            createCell(row, columnCount++, transaction.getCategory().getType(), style);
            createCell(row, columnCount++, transaction.getDescription(), style);
            createCell(row, columnCount++, transaction.getAccount().getName(), style);
            createCell(row, columnCount++, transaction.getPaymentType(), style);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
        } finally {
            workbook.close(); // Ensures proper cleanup
        }
    }
}

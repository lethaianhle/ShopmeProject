package com.shopme.admin.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shopme.common.entity.User;

public class UserExcelExporter extends AbtractExporter{
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	
	public UserExcelExporter() {
		workbook = new XSSFWorkbook();
	}
	
	private void writeHeaderLine() {
		sheet = workbook.createSheet("Users");
		XSSFRow row = sheet.createRow(0);
		
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		cellStyle.setFont(font);
		
		createCell(row, 0, "User ID", cellStyle);
		createCell(row, 1, "E-mail", cellStyle);
		createCell(row, 2, "First Name", cellStyle);
		createCell(row, 3, "Last Name", cellStyle);
		createCell(row, 4, "Roles", cellStyle);
		createCell(row, 5, "Enabled", cellStyle);
	}
	
	private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle cellStyle) {
		XSSFCell cell = row.createCell(columnIndex);
		cell.setCellStyle(cellStyle);
		sheet.autoSizeColumn(columnIndex);
		
		if(value instanceof Integer) {
			cell.setCellValue((int)value);
		} else if(value instanceof String) {
			cell.setCellValue(value.toString());
		} else if(value instanceof Boolean) {
			cell.setCellValue((boolean)value);
		}
		
	}
	
	public void export(List<User> listUser, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/octet-stream", ".xlsx");
		writeHeaderLine();
		writeDataLines(listUser);
		
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		
		workbook.close();
		outputStream.close();
		
	}

	private void writeDataLines(List<User> listUser) {
		int rowIndex = 1;
		for (User user : listUser) {
			XSSFRow row = sheet.createRow(rowIndex);
			XSSFCellStyle cellStyle = workbook.createCellStyle();
			XSSFFont font = workbook.createFont();
			font.setFontHeight(14);
			cellStyle.setFont(font);
			
			createCell(row, 0, user.getId(), cellStyle);
			createCell(row, 1, user.getEmail(), cellStyle);
			createCell(row, 2, user.getFirstName(), cellStyle);
			createCell(row, 3, user.getLastName(), cellStyle);
			createCell(row, 4, user.getRoles().toString(), cellStyle);
			createCell(row, 5, user.isEnabled(), cellStyle);
			
			rowIndex++;
		}
	}
}

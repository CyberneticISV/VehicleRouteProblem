package com.cybernetic.home;

import com.cybernetic.example.CostMatrixVRPTW;
import com.cybernetic.mapquest.MapQuestClient;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;

import static com.cybernetic.mapquest.constants.RouteResponseParameters.DISTANCE;

@Controller
public class DemoController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DemoController.class);

	/**
	 * Size of a byte buffer to read/write file
	 */
	private static final int BUFFER_SIZE = 4096;

	/**
	 * Path of the file to be downloaded, relative to application's directory
	 */
	private String filePath = "/resources/css/bootstrap.min.css";

	private CostMatrixVRPTW  costMatrixVRPTW = new CostMatrixVRPTW();

	@RequestMapping(value = "/calculate", method = RequestMethod.GET)
	public void index(@RequestParam("vehicleNumber") int vehicleNumber, Principal principal,
						HttpServletRequest request,
						HttpServletResponse response) throws IOException {
		/*costMatrixVRPTW.calculate(vehicleNumber);*/

		downoalFile(request, response, filePath);
/*
		LOGGER.info(new MapQuestClient().getRoute().findValue(DISTANCE).toString());*/
	}

	private static void downoalFile(HttpServletRequest request, HttpServletResponse response, String filePath) throws IOException {
/*		ServletContext context = request.getServletContext();
		String appPath = context.getRealPath("");
		System.out.println("appPath = " + appPath);

		// construct the complete absolute path of the file
		String fullPath = appPath + filePath;
		File downloadFile = new File(fullPath);
		FileInputStream inputStream = new FileInputStream(downloadFile);

		// get MIME type of the file
		String mimeType = context.getMimeType(fullPath);*/
/*		if (mimeType == null) {
			// set to binary type if MIME mapping not found
			mimeType = "application/octet-stream";
		}
		System.out.println("MIME type: " + mimeType);*/

/*		File downloadFile = new File(fullPath);*/
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=DemoSolution.xls");
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("FirstSheet");

		HSSFRow rowhead = sheet.createRow((short)0);
		rowhead.createCell(0).setCellValue("No.");
		rowhead.createCell(1).setCellValue("Name");
		rowhead.createCell(2).setCellValue("Address");
		rowhead.createCell(3).setCellValue("Email");

		HSSFRow row = sheet.createRow((short)1);
		row.createCell(0).setCellValue("1");
		row.createCell(1).setCellValue("Sankumarsingh");
		row.createCell(2).setCellValue("India");
		row.createCell(3).setCellValue("sankumarsingh@gmail.com");

		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		workbook.write(outByteStream);

		byte[] outArray = outByteStream.toByteArray();
		response.setContentLength(outArray.length);
		response.setHeader("Expires:", "0");
		OutputStream outStream = response.getOutputStream();
		outStream.write(outArray);
		outStream.flush();
/*
		// set content attributes for the response
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());

		// set headers for the response
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"",
				downloadFile.getName());
		response.setHeader(headerKey, headerValue);

		// get output stream of the response
		OutputStream outStream;
		outStream = response.getOutputStream();

		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = -1;

		// write bytes read from the input stream into the output stream
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}

		inputStream.close();
		outStream.close();*/
	}
}

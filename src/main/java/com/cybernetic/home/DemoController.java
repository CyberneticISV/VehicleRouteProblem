package com.cybernetic.home;

import com.cybernetic.example.CostMatrixVRPTW;
import com.cybernetic.example.SolutionPrintHelper;
import com.cybernetic.mapquest.MapQuestClient;
import jsprit.core.problem.VehicleRoutingProblem;
import jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import jsprit.core.reporting.SolutionPrinter;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;
import java.util.Map;

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

    private CostMatrixVRPTW costMatrixVRPTW = new CostMatrixVRPTW();

    @RequestMapping(value = "/calculate", method = RequestMethod.GET)
    public void index(@RequestParam("vehicleNumber") int vehicleNumber, HttpServletResponse response) {
        Map<String, Object> result = costMatrixVRPTW.calculate(vehicleNumber);
        HSSFWorkbook wb = generateWorkbook((VehicleRoutingProblem) result.get("problem"),
                (VehicleRoutingProblemSolution) result.get("solution"));
        downloadFile(response, wb);
    }

    private static void downloadFile(HttpServletResponse response, HSSFWorkbook wb) {
        ByteArrayOutputStream excelOutput = new ByteArrayOutputStream();
        byte[] byteRpt;

        try {
            wb.write(excelOutput);
            byteRpt = excelOutput.toByteArray();

            response.setHeader("Content-Disposition", "attachment;filename=VehicleRoutSolution.xls");
            response.setContentType("application/vnd.ms-excel");
            OutputStream out = response.getOutputStream();
            response.setContentLength(byteRpt.length);
            response.setBufferSize(byteRpt.length);

            out.write(byteRpt, 0, byteRpt.length);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static HSSFWorkbook generateWorkbook(VehicleRoutingProblem problem, VehicleRoutingProblemSolution solution) {
        HSSFWorkbook wb = new HSSFWorkbook();
       // turn off gridlines
/*        sheet.setDisplayGridlines(false);
        sheet.setPrintGridlines(false);
        sheet.setFitToPage(true);*/
/*        sheet.setHorizontallyCenter(true);*/
/*        HSSFPrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(true);*/

        //the following three statements are required only for HSSF
/*        sheet.setAutobreaks(true);
        printSetup.setFitHeight((short) 1);
        printSetup.setFitWidth((short) 1);*/


        /*START CREATING PROBLEM SHEET*/
        HSSFSheet problemSheet = wb.createSheet("Problem");
        /*HEADER*/
        HSSFRow headerRow = problemSheet.createRow(0);
        headerRow.setHeightInPoints(16f);
        HSSFCell cellBrand = headerRow.createCell(0);
        cellBrand.setCellValue(new HSSFRichTextString("indicator"));
        HSSFCell cellVehicleDescription = headerRow.createCell(1);
        cellVehicleDescription.setCellValue(new HSSFRichTextString("value"));

        /*CONTENT*/
        HSSFRow noJobsRow = problemSheet.createRow(1);
        noJobsRow.createCell(0).setCellValue(new HSSFRichTextString("noJobs"));
        noJobsRow.createCell(1).setCellValue(problem.getJobs().values().size());

        SolutionPrintHelper.Jobs jobs = SolutionPrintHelper.getNuOfJobs(problem);
        HSSFRow noServicesRow = problemSheet.createRow(2);
        noServicesRow.createCell(0).setCellValue(new HSSFRichTextString("noServices"));
        noServicesRow.createCell(1).setCellValue(jobs.nServices);

        HSSFRow noShipmentsRow = problemSheet.createRow(3);
        noShipmentsRow.createCell(0).setCellValue(new HSSFRichTextString("noShipments"));
        noShipmentsRow.createCell(1).setCellValue(jobs.nShipments);

        HSSFRow fleetsizeRow = problemSheet.createRow(4);
        fleetsizeRow.createCell(0).setCellValue(new HSSFRichTextString("fleetsize"));
        fleetsizeRow.createCell(1).setCellValue(new HSSFRichTextString(problem.getFleetSize().toString()));
        /*END CREATING PROBLEM SHEET*/

/*        HSSFCell cellRange = headerRow.createCell(2);
        cellRange.setCellValue("Measured range");
        HSSFCell cellDistance = headerRow.createCell(3);
        cellDistance.setCellValue("Distance");
        HSSFCell cellUsage = headerRow.createCell(4);
        cellUsage.setCellValue("Average usage");
        HSSFCell cellCost = headerRow.createCell(5);
        cellCost.setCellValue("Price (excl.)");*/
        //content




/*
        cell = row.createCell(1);
        cell.setCellValue(2);
        cell = row.createCell(2);
        cell.setCellValue(1);
        cell = row.createCell(3);
        cell.setCellValue(54475);
        cell = row.createCell(4);
        cell.setCellValue(54);
        cell = row.createCell(5);
        cell.setCellValue(12345);*/
        return wb;
    }
}

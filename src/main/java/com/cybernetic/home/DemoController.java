package com.cybernetic.home;

import com.cybernetic.example.CostMatrixVRPTW;
import com.cybernetic.example.SolutionPrintHelper;
import jsprit.core.problem.VehicleRoutingProblem;
import jsprit.core.problem.job.Job;
import jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import jsprit.core.problem.solution.route.VehicleRoute;
import jsprit.core.problem.solution.route.activity.TourActivity;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

@Controller
public class DemoController {

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
        buildProblemSheet(problem, wb);
        buildSolutionSummarySheet(solution, wb);
        buildSolutionSheetVerbose(wb, problem, solution);
        buildUnassignedJobsSheet(wb, solution);
        return wb;
    }

    private static void buildProblemSheet(VehicleRoutingProblem problem, HSSFWorkbook wb) {
        HSSFSheet problemSheet = wb.createSheet("Problem");
        /*HEADER*/
        HSSFRow headerRow = problemSheet.createRow(0);
        headerRow.setHeightInPoints(16f);
        headerRow.createCell(0)
                .setCellValue(new HSSFRichTextString("indicator"));
        headerRow.createCell(1)
                .setCellValue(new HSSFRichTextString("value"));

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
    }

    private static void buildSolutionSummarySheet(VehicleRoutingProblemSolution solution, HSSFWorkbook wb) {
        HSSFSheet solutionSummarySheet = wb.createSheet("Solution");
        /*HEADER*/
        HSSFRow headerRow = solutionSummarySheet.createRow(0);
        headerRow.setHeightInPoints(16f);
        headerRow.createCell(0)
                .setCellValue(new HSSFRichTextString("indicator"));
        headerRow.createCell(1)
                .setCellValue(new HSSFRichTextString("value"));

        /*CONTENT*/
        HSSFRow costsRow = solutionSummarySheet.createRow(1);
        costsRow.createCell(0).setCellValue(new HSSFRichTextString("costs"));
        costsRow.createCell(1).setCellValue(solution.getCost());

        HSSFRow noVehiclesRow = solutionSummarySheet.createRow(2);
        noVehiclesRow.createCell(0).setCellValue(new HSSFRichTextString("noVehicles"));
        noVehiclesRow.createCell(1).setCellValue(solution.getRoutes().size());

        HSSFRow unassgndJobsRow = solutionSummarySheet.createRow(3);
        unassgndJobsRow.createCell(0).setCellValue(new HSSFRichTextString("unassgndJobs"));
        unassgndJobsRow.createCell(1).setCellValue(solution.getUnassignedJobs().size());
    }

    public static void buildSolutionSheetVerbose(HSSFWorkbook wb, VehicleRoutingProblem problem,
                                                 VehicleRoutingProblemSolution solution) {
        HSSFSheet solutionSheet = wb.createSheet("Detailed solution ");
        /*HEADER*/
        int rowNum = 0;
        HSSFRow headerRow = solutionSheet.createRow(rowNum);
        headerRow.setHeightInPoints(16f);
        headerRow.createCell(0)
                .setCellValue(new HSSFRichTextString("route"));
        headerRow.createCell(1)
                .setCellValue(new HSSFRichTextString("vehicle"));
        headerRow.createCell(2)
                .setCellValue(new HSSFRichTextString("activity"));
        headerRow.createCell(3)
                .setCellValue(new HSSFRichTextString("job"));
        headerRow.createCell(4)
                .setCellValue(new HSSFRichTextString("arrTime"));
        headerRow.createCell(5)
                .setCellValue(new HSSFRichTextString("endTime"));
        headerRow.createCell(6)
                .setCellValue(new HSSFRichTextString("costs"));

        /*CONTENT*/
        int routeNum = 1;
        for(Iterator unassignedJobAlgin = solution.getRoutes().iterator(); unassignedJobAlgin.hasNext(); ++routeNum) {

            VehicleRoute route = (VehicleRoute)unassignedJobAlgin.next();
            double j = 0.0D;
            HSSFRow firstRow = solutionSheet.createRow(++rowNum);
            firstRow.createCell(0)
                    .setCellValue(routeNum);
            firstRow.createCell(1)
                    .setCellValue(new HSSFRichTextString(SolutionPrintHelper.getVehicleString(route)));
            firstRow.createCell(2)
                    .setCellValue(new HSSFRichTextString(route.getStart().getName()));
            firstRow.createCell(3)
                    .setCellValue(new HSSFRichTextString("-"));
            firstRow.createCell(4)
                    .setCellValue(new HSSFRichTextString("undef"));
            firstRow.createCell(5)
                    .setCellValue(Math.round(route.getStart().getEndTime()));
            firstRow.createCell(6)
                    .setCellValue(Math.round(j));

            Object prevAct = route.getStart();

            TourActivity act;
            for(Iterator c = route.getActivities().iterator(); c.hasNext(); prevAct = act) {
                act = (TourActivity)c.next();
                String jobId;
                if(act instanceof TourActivity.JobActivity) {
                    jobId = ((TourActivity.JobActivity)act).getJob().getId();
                } else {
                    jobId = "-";
                }

                double c1 = problem.getTransportCosts().getTransportCost(((TourActivity) prevAct).getLocation(), act.getLocation(), ((TourActivity) prevAct).getEndTime(), route.getDriver(), route.getVehicle());
                c1 += problem.getActivityCosts().getActivityCost(act, act.getArrTime(), route.getDriver(), route.getVehicle());
                j += c1;
                HSSFRow nextRow = solutionSheet.createRow(++rowNum);
                nextRow.createCell(0)
                        .setCellValue(routeNum);
                nextRow.createCell(1)
                        .setCellValue(new HSSFRichTextString(SolutionPrintHelper.getVehicleString(route)));
                nextRow.createCell(2)
                        .setCellValue(new HSSFRichTextString(act.getName()));
                nextRow.createCell(3)
                        .setCellValue(new HSSFRichTextString(jobId));
                nextRow.createCell(4)
                        .setCellValue(Math.round(act.getArrTime()));
                nextRow.createCell(5)
                        .setCellValue(Math.round(act.getEndTime()));
                nextRow.createCell(6)
                        .setCellValue(Math.round(j));
            }

            double var18 = problem.getTransportCosts().getTransportCost(((TourActivity)prevAct).getLocation(), route.getEnd().getLocation(), ((TourActivity)prevAct).getEndTime(), route.getDriver(), route.getVehicle());
            var18 += problem.getActivityCosts().getActivityCost(route.getEnd(), route.getEnd().getArrTime(), route.getDriver(), route.getVehicle());
            j += var18;

            HSSFRow lastRow = solutionSheet.createRow(++rowNum);
            lastRow.createCell(0)
                    .setCellValue(routeNum);
            lastRow.createCell(1)
                    .setCellValue(new HSSFRichTextString(SolutionPrintHelper.getVehicleString(route)));
            lastRow.createCell(2)
                    .setCellValue(new HSSFRichTextString(route.getEnd().getName()));
            lastRow.createCell(3)
                    .setCellValue(new HSSFRichTextString("-"));
            lastRow.createCell(4)
                    .setCellValue(Math.round(route.getEnd().getArrTime()));
            lastRow.createCell(5)
                    .setCellValue(new HSSFRichTextString("undef"));
            lastRow.createCell(6)
                    .setCellValue(Math.round(j));
            ++rowNum;
        }
    }

    private static void buildUnassignedJobsSheet(HSSFWorkbook wb, VehicleRoutingProblemSolution solution) {
        HSSFSheet problemSheet = wb.createSheet("Unassigned Jobs");
        /*HEADER*/
        HSSFRow headerRow = problemSheet.createRow(0);
        headerRow.setHeightInPoints(16f);
        HSSFCell unassignedJobs = headerRow.createCell(0);
        unassignedJobs.setCellValue(new HSSFRichTextString("Unassigned jobs"));

        /*CONTENT*/
        if(!solution.getUnassignedJobs().isEmpty()) {
            Iterator var16 = solution.getUnassignedJobs().iterator();
            int rowNum = 1;
            while(var16.hasNext()) {
                HSSFRow tempRow = problemSheet.createRow(rowNum++);
                Job var17 = (Job)var16.next();
                tempRow.createCell(0).setCellValue(new HSSFRichTextString(var17.getId()));
            }
        }
    }
}

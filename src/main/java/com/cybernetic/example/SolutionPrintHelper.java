package com.cybernetic.example;

import java.io.PrintWriter;
import java.util.Iterator;
import jsprit.core.problem.VehicleRoutingProblem;
import jsprit.core.problem.job.Job;
import jsprit.core.problem.job.Service;
import jsprit.core.problem.job.Shipment;
import jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import jsprit.core.problem.solution.route.VehicleRoute;
import jsprit.core.problem.solution.route.activity.TourActivity;
import jsprit.core.problem.solution.route.activity.TourActivity.JobActivity;

public class SolutionPrintHelper {
    private static final PrintWriter SYSTEM_OUT_AS_PRINT_WRITER;

    public SolutionPrintHelper() {
    }

    public static void print(VehicleRoutingProblemSolution solution) {
        print(SYSTEM_OUT_AS_PRINT_WRITER, solution);
        SYSTEM_OUT_AS_PRINT_WRITER.flush();
    }

    public static void print(PrintWriter out, VehicleRoutingProblemSolution solution) {
        out.println("[costs=" + solution.getCost() + "]");
        out.println("[#vehicles=" + solution.getRoutes().size() + "]");
    }

    public static void print(VehicleRoutingProblem problem, VehicleRoutingProblemSolution solution, SolutionPrintHelper.Print print) {
        print(SYSTEM_OUT_AS_PRINT_WRITER, problem, solution, print);
        SYSTEM_OUT_AS_PRINT_WRITER.flush();
    }

    public static void print(PrintWriter out, VehicleRoutingProblem problem, VehicleRoutingProblemSolution solution, SolutionPrintHelper.Print print) {
        String leftAlign = "| %-13s | %-8s | %n";
        out.format("+--------------------------+%n");
        out.printf("| problem                  |%n");
        out.format("+---------------+----------+%n");
        out.printf("| indicator     | value    |%n");
        out.format("+---------------+----------+%n");
        out.format(leftAlign, "noJobs", problem.getJobs().values().size());
        SolutionPrintHelper.Jobs jobs = getNuOfJobs(problem);
        out.format(leftAlign, "noServices", jobs.nServices);
        out.format(leftAlign, "noShipments", jobs.nShipments);
        out.format(leftAlign, "fleetsize", problem.getFleetSize().toString());
        out.format("+--------------------------+%n");
        String leftAlignSolution = "| %-13s | %-40s | %n";
        out.format("+----------------------------------------------------------+%n");
        out.printf("| solution                                                 |%n");
        out.format("+---------------+------------------------------------------+%n");
        out.printf("| indicator     | value                                    |%n");
        out.format("+---------------+------------------------------------------+%n");
        out.format(leftAlignSolution, "costs", solution.getCost());
        out.format(leftAlignSolution, "noVehicles", solution.getRoutes().size());
        out.format(leftAlignSolution, "unassgndJobs", solution.getUnassignedJobs().size());
        out.format("+----------------------------------------------------------+%n");
        if(print.equals(SolutionPrintHelper.Print.VERBOSE)) {
            printVerbose(out, problem, solution);
        }

    }

    public static void printVerbose(VehicleRoutingProblem problem, VehicleRoutingProblemSolution solution) {
        printVerbose(SYSTEM_OUT_AS_PRINT_WRITER, problem, solution);
        SYSTEM_OUT_AS_PRINT_WRITER.flush();
    }

    public static void printVerbose(PrintWriter out, VehicleRoutingProblem problem, VehicleRoutingProblemSolution solution) {
        String leftAlgin = "| %-7s | %-20s | %-21s | %-15s | %-15s | %-15s | %-15s |%n";
        out.format("+--------------------------------------------------------------------------------------------------------------------------------+%n");
        out.printf("| detailed solution                                                                                                              |%n");
        out.format("+---------+----------------------+-----------------------+-----------------+-----------------+-----------------+-----------------+%n");
        out.printf("| route   | vehicle              | activity              | job             | arrTime         | endTime         | costs           |%n");
        int routeNu = 1;

        for(Iterator unassignedJobAlgin = solution.getRoutes().iterator(); unassignedJobAlgin.hasNext(); ++routeNu) {
            VehicleRoute route = (VehicleRoute)unassignedJobAlgin.next();
            out.format("+---------+----------------------+-----------------------+-----------------+-----------------+-----------------+-----------------+%n");
            double j = 0.0D;
            out.format(leftAlgin, routeNu, getVehicleString(route), route.getStart().getName(), "-", "undef", Math.round(route.getStart().getEndTime()), Math.round(j));
            Object prevAct = route.getStart();

            TourActivity act;
            for(Iterator c = route.getActivities().iterator(); c.hasNext(); prevAct = act) {
                act = (TourActivity)c.next();
                String jobId;
                if(act instanceof JobActivity) {
                    jobId = ((JobActivity)act).getJob().getId();
                } else {
                    jobId = "-";
                }

                double c1 = problem.getTransportCosts().getTransportCost(((TourActivity)prevAct).getLocation(), act.getLocation(), ((TourActivity)prevAct).getEndTime(), route.getDriver(), route.getVehicle());
                c1 += problem.getActivityCosts().getActivityCost(act, act.getArrTime(), route.getDriver(), route.getVehicle());
                j += c1;
                out.format(leftAlgin, routeNu, getVehicleString(route), act.getName(), jobId, Math.round(act.getArrTime()), Math.round(act.getEndTime()), Math.round(j));
            }

            double var18 = problem.getTransportCosts().getTransportCost(((TourActivity)prevAct).getLocation(), route.getEnd().getLocation(), ((TourActivity)prevAct).getEndTime(), route.getDriver(), route.getVehicle());
            var18 += problem.getActivityCosts().getActivityCost(route.getEnd(), route.getEnd().getArrTime(), route.getDriver(), route.getVehicle());
            j += var18;
            out.format(leftAlgin, routeNu, getVehicleString(route), route.getEnd().getName(), "-", Math.round(route.getEnd().getArrTime()), "undef", Math.round(j));
        }

        out.format("+--------------------------------------------------------------------------------------------------------------------------------+%n");
        if(!solution.getUnassignedJobs().isEmpty()) {
            out.format("+----------------+%n");
            out.format("| unassignedJobs |%n");
            out.format("+----------------+%n");
            String var15 = "| %-14s |%n";
            Iterator var16 = solution.getUnassignedJobs().iterator();

            while(var16.hasNext()) {
                Job var17 = (Job)var16.next();
                out.format(var15, var17.getId());
            }

            out.format("+----------------+%n");
        }

    }

    public static String getVehicleString(VehicleRoute route) {
        return route.getVehicle().getId();
    }

    public static SolutionPrintHelper.Jobs getNuOfJobs(VehicleRoutingProblem problem) {
        int nShipments = 0;
        int nServices = 0;
        Iterator var3 = problem.getJobs().values().iterator();

        while(var3.hasNext()) {
            Job j = (Job)var3.next();
            if(j instanceof Shipment) {
                ++nShipments;
            }

            if(j instanceof Service) {
                ++nServices;
            }
        }

        return new SolutionPrintHelper.Jobs(nServices, nShipments);
    }

    static {
        SYSTEM_OUT_AS_PRINT_WRITER = new PrintWriter(System.out);
    }

    public static class Jobs {
        public int nServices;
        public int nShipments;

        public Jobs(int nServices, int nShipments) {
            this.nServices = nServices;
            this.nShipments = nShipments;
        }
    }

    public enum Print {
        CONCISE,
        VERBOSE;

        Print() {
        }
    }
}

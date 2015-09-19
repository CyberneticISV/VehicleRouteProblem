package com.cybernetic.example;

import jsprit.core.problem.VehicleRoutingProblem;
import jsprit.core.problem.job.Job;
import jsprit.core.problem.job.Service;
import jsprit.core.problem.job.Shipment;
import jsprit.core.problem.solution.route.VehicleRoute;

import java.util.Iterator;

public class SolutionPrintHelper {

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

    public static class Jobs {
        public int nServices;
        public int nShipments;

        public Jobs(int nServices, int nShipments) {
            this.nServices = nServices;
            this.nShipments = nShipments;
        }
    }
}

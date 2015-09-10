package com.cybernetic.example;

/*******************************************************************************
 * Copyright (C) 2014  Stefan Schroeder
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

import jsprit.core.algorithm.VehicleRoutingAlgorithm;
import jsprit.core.algorithm.io.VehicleRoutingAlgorithms;
import jsprit.core.problem.Location;
import jsprit.core.problem.VehicleRoutingProblem;
import jsprit.core.problem.cost.VehicleRoutingTransportCosts;
import jsprit.core.problem.job.Service;
import jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import jsprit.core.problem.solution.route.activity.TimeWindow;
import jsprit.core.problem.vehicle.VehicleImpl;
import jsprit.core.problem.vehicle.VehicleType;
import jsprit.core.problem.vehicle.VehicleTypeImpl;
import jsprit.core.reporting.SolutionPrinter;
import jsprit.core.util.Solutions;
import jsprit.core.util.VehicleRoutingTransportCostsMatrix;

import java.util.Collection;

public class CostMatrixVRPTW {

    public static void calculate(int vehicleNumber) {
        /*
		 * some preparation - create output folder
		 */
        Examples.createOutputFolder();
/*
        int vehicleNumber = 10;*/

        //describe vehicle type
        VehicleType type = VehicleTypeImpl.Builder.newInstance("type")
                .addCapacityDimension(0, 15)
                .setCostPerDistance(1)
                .build();

        //describe depo location
        Location depo = Location.newInstance("0");

        VehicleImpl[] vehicles = new VehicleImpl[vehicleNumber];

        for (int i = 0; i <= vehicleNumber - 1; i++) {
            vehicles[i] = VehicleImpl.Builder.newInstance("vehicle" + Integer.toString(i))
                    .setStartLocation(depo)
                    .setType(type)
                    .setReturnToDepot(false)
                    .build();
        }

        int serviceNumber = 3;
        Service[] services = new Service[serviceNumber];

        for (int i = 0; i <= serviceNumber - 1; i++) {
            services[i] = Service.Builder.newInstance(Integer.toString(i)).addSizeDimension(0, 1).setServiceTime(3).setTimeWindow(TimeWindow.newInstance(5, 20)).setLocation(Location.newInstance(Integer.toString(i + 1))).build();

        }


		/*Service s1 = Service.Builder.newInstance("1").addSizeDimension(0, 1).setLocation(Location.newInstance("1")).build();
		Service s2 = Service.Builder.newInstance("2").addSizeDimension(0, 1).setLocation(Location.newInstance("2")).build();
		Service s3 = Service.Builder.newInstance("3").addSizeDimension(0, 1).setLocation(Location.newInstance("3")).build();
		*/

		/*
		 * Assume the following symmetric distance-matrix
		 * from,to,distance
		 * 0,1,10.0
		 * 0,2,20.0
		 * 0,3,5.0
		 * 1,2,4.0
		 * 1,3,1.0
		 * 2,3,2.0
		 *
		 * and this time-matrix
		 * 0,1,5.0
		 * 0,2,10.0
		 * 0,3,2.5
		 * 1,2,2.0
		 * 1,3,0.5
		 * 2,3,1.0
		 */
        //define a matrix-builder building a symmetric matrix
        VehicleRoutingTransportCostsMatrix.Builder costMatrixBuilder = VehicleRoutingTransportCostsMatrix.Builder.newInstance(true);
        costMatrixBuilder.addTransportDistance("0", "1", 10.0);
        costMatrixBuilder.addTransportDistance("0", "2", 20.0);
        costMatrixBuilder.addTransportDistance("0", "3", 5.0);
        costMatrixBuilder.addTransportDistance("1", "2", 4.0);
        costMatrixBuilder.addTransportDistance("1", "3", 1.0);
        costMatrixBuilder.addTransportDistance("2", "3", 2.0);

        costMatrixBuilder.addTransportTime("0", "1", 10.0);
        costMatrixBuilder.addTransportTime("0", "2", 20.0);
        costMatrixBuilder.addTransportTime("0", "3", 5.0);
        costMatrixBuilder.addTransportTime("1", "2", 4.0);
        costMatrixBuilder.addTransportTime("1", "3", 1.0);
        costMatrixBuilder.addTransportTime("2", "3", 2.0);

        VehicleRoutingTransportCosts costMatrix = costMatrixBuilder.build();

        VehicleRoutingProblem.Builder vrp = VehicleRoutingProblem.Builder.newInstance();
        vrp.setFleetSize(VehicleRoutingProblem.FleetSize.FINITE).setRoutingCost(costMatrix);

        for (int i = 0; i <= vehicleNumber - 1; i++) {
            vrp.addVehicle(vehicles[i]);
        }

        for (int i = 0; i <= serviceNumber - 1; i++) {
            vrp.addJob(services[i]);
        }

        VehicleRoutingProblem problem = vrp.build();
        VehicleRoutingAlgorithm vra = VehicleRoutingAlgorithms.readAndCreateAlgorithm(problem, "input/fastAlgo.xml");

        Collection<VehicleRoutingProblemSolution> solutions = vra.searchSolutions();
        VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);
        //SolutionPrinter.print(Solutions.bestOf(solutions));
        SolutionPrinter.print(problem, bestSolution, SolutionPrinter.Print.VERBOSE);
        //new Plotter(vrp, Solutions.bestOf(solutions)).plot("output/yo.png", "po");

    }

}


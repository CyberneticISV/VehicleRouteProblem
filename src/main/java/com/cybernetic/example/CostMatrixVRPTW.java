package com.cybernetic.example;

import com.cybernetic.example.dao.ClientDAO;
import com.cybernetic.example.dao.OrderDAO;
import com.cybernetic.example.entities.Client;
import com.cybernetic.example.entities.Order;
import com.cybernetic.mapquest.MapQuestClient;
import com.cybernetic.mapquest.constants.RouteResponseParameters;
import com.fasterxml.jackson.databind.JsonNode;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CostMatrixVRPTW {

    private static final int MIN_CAPACITY_DIMENSION = 0;
    private static final int MAX_CAPACITY_DIMENSION = 15;
    private static final int DEFAULT_COST_PER_DISTANCE = 1;

    //describe depo location
    private static final Location DEPO = Location.newInstance("0");

    //describe default vehicle type
    private static final VehicleType DEFAULT_VEHICLE_TYPE = VehicleTypeImpl.Builder.newInstance("default")
            .addCapacityDimension(MIN_CAPACITY_DIMENSION, MAX_CAPACITY_DIMENSION)
            .setCostPerDistance(DEFAULT_COST_PER_DISTANCE)
            .build();
    private static double averageServiceTime = 3.0;

    private OrderDAO orderDAO = new OrderDAO();
    private ClientDAO clientDAO = new ClientDAO();
    private MapQuestClient mapQuestClient = new MapQuestClient();

    public void calculate(int vehicleNumber) {
        /*
         * some preparation - create output folder
		 */
        //TODO:Remake to create folder in right place
        Examples.createOutputFolder();

        List<VehicleImpl> vehicles = prepareVehicles(vehicleNumber);

        List<Service> services = new ArrayList<>();
        Map<String, Order> orders = orderDAO.getAllOrders();
        for (Map.Entry<String, Order> entry : orders.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue());
            Order order = entry.getValue();
            addNormalizedOrder(services, order);
        }

        Map<String, Client> clients = clientDAO.getAllClients();

        VehicleRoutingTransportCosts costMatrix = getVehicleRoutingTransportCosts(clients, mapQuestClient);

        VehicleRoutingProblem.Builder vrp = VehicleRoutingProblem.Builder.newInstance();
        vrp.setFleetSize(VehicleRoutingProblem.FleetSize.FINITE)
                .setRoutingCost(costMatrix);

        vrp.addAllVehicles(vehicles);
        vrp.addAllJobs(services);

        VehicleRoutingProblem problem = vrp.build();
        VehicleRoutingAlgorithm vra = VehicleRoutingAlgorithms.readAndCreateAlgorithm(problem, "input/fastAlgo.xml");

        Collection<VehicleRoutingProblemSolution> solutions = vra.searchSolutions();
        VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);
        //SolutionPrinter.print(Solutions.bestOf(solutions));
        SolutionPrinter.print(problem, bestSolution, SolutionPrinter.Print.VERBOSE);
        //new Plotter(vrp, Solutions.bestOf(solutions)).plot("output/yo.png", "po");
    }

    /**
     * Builds non-static time and distance cost matrix based on preset locations (clients)
     *
     * @param clients the Map with clients
     * @param mapQuestClient the Map with clients
     * @return  VehicleRoutingTransportCosts object with time and distance cost matrix
     * */
    private static VehicleRoutingTransportCosts getVehicleRoutingTransportCosts(Map<String, Client> clients,
                                                                                MapQuestClient mapQuestClient) {
        VehicleRoutingTransportCostsMatrix.Builder costMatrixBuilder =
                VehicleRoutingTransportCostsMatrix.Builder.newInstance(true);
        for (Map.Entry<String, Client> entry : clients.entrySet()) {
            for (Map.Entry<String, Client> subEntry : clients.entrySet()) {
                if (!entry.getKey().equals(subEntry.getKey())) {
                    String from = entry.getValue().getLatitude() + "," + entry.getValue().getLongitude();
                    String to = subEntry.getValue().getLatitude() + "," + subEntry.getValue().getLongitude();
                    JsonNode result = mapQuestClient.getRoute(from, to);
                    costMatrixBuilder.addTransportDistance(entry.getKey(), subEntry.getKey(), result.findValue(RouteResponseParameters.DISTANCE).doubleValue());
                    costMatrixBuilder.addTransportTime(entry.getKey(), subEntry.getKey(), result.findValue(RouteResponseParameters.TIME).doubleValue() / 60);
                }
            }
        }
        return costMatrixBuilder.build();
    }

    private void addNormalizedOrder(List<Service> services, Order order) {
        Location location = Location.newInstance(order.getLocationId());
        if (order.getDimensionValue() < MAX_CAPACITY_DIMENSION) {
            services.add(Service.Builder.newInstance("service" + order.getId())
                    .addSizeDimension(0, order.getDimensionValue())
                    .setServiceTime(averageServiceTime)
                    .setTimeWindow(TimeWindow.newInstance(order.getStartTime(), order.getEndTime()))
                    .setLocation(location).build());
        } else {
            int i = 0;
            int dimensionValue = order.getDimensionValue();
            while (dimensionValue > MAX_CAPACITY_DIMENSION) {
                services.add(Service.Builder.newInstance("service_" + order.getId() + "_" + ++i)
                        .addSizeDimension(0, MAX_CAPACITY_DIMENSION)
                        .setServiceTime(averageServiceTime)
                        .setTimeWindow(TimeWindow.newInstance(order.getStartTime(), order.getEndTime()))
                        .setLocation(location).build());
                dimensionValue -= MAX_CAPACITY_DIMENSION;
            }
        }
    }

    private static List<VehicleImpl> prepareVehicles(int vehicleNumber) {
        List<VehicleImpl> vehicles = new ArrayList<>(vehicleNumber);

        for (int i = 0; i <= vehicleNumber - 1; i++) {
            vehicles.add(VehicleImpl.Builder.newInstance("vehicle" + Integer.toString(i))
                    .setStartLocation(DEPO)
                    .setType(DEFAULT_VEHICLE_TYPE)
                    .setReturnToDepot(false)
                    .build());
        }
        return vehicles;
    }
}


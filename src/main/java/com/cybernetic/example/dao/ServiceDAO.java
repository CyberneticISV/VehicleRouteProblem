package com.cybernetic.example.dao;

import jsprit.core.problem.Location;
import jsprit.core.problem.job.Service;
import jsprit.core.problem.solution.route.activity.TimeWindow;

import java.util.*;

public class ServiceDAO {
    private static Map<String, List<Service>> serviceMap = new HashMap<String, List<Service>>();
    static {
        serviceMap.put("3.170417,101.697239",
                Arrays.asList(Service.Builder.newInstance("3.170417,101.697239")
                .addSizeDimension(0, 1)
                .setServiceTime(3)
                .setTimeWindow(TimeWindow.newInstance(5, 20))
                .setLocation(Location.newInstance("3.170417,101.697239"))
                .build()));
    }
    public ServiceDAO() {

    }
}

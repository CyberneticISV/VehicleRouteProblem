package com.cybernetic.example.dao;

import com.cybernetic.example.entities.Order;

import java.util.*;

public class OrderDAO {
    private static Map<String, Order> ordersMap = new HashMap<>();
    static {
        int locationId = 0;
        int orderId = 0;
        ordersMap.put(Integer.toString(++orderId), new Order(Integer.toString(orderId), Integer.toString(++locationId), 1, 30, 60));
        ordersMap.put(Integer.toString(++orderId), new Order(Integer.toString(orderId), Integer.toString(++locationId), 1, 60, 90));
        ordersMap.put(Integer.toString(++orderId), new Order(Integer.toString(orderId), Integer.toString(++locationId), 1, 90, 120));
        ordersMap.put(Integer.toString(++orderId), new Order(Integer.toString(orderId), Integer.toString(++locationId), 1, 30, 60));
        ordersMap.put(Integer.toString(++orderId), new Order(Integer.toString(orderId), Integer.toString(++locationId), 1, 60, 90));
        ordersMap.put(Integer.toString(++orderId), new Order(Integer.toString(orderId), Integer.toString(++locationId), 1, 60, 90));
        ordersMap.put(Integer.toString(++orderId), new Order(Integer.toString(orderId), Integer.toString(++locationId), 1, 60, 90));
        ordersMap.put(Integer.toString(++orderId), new Order(Integer.toString(orderId), Integer.toString(++locationId), 1, 30, 60));
        ordersMap.put(Integer.toString(++orderId), new Order(Integer.toString(orderId), Integer.toString(++locationId), 1, 90, 120));
        ordersMap.put(Integer.toString(++orderId), new Order(Integer.toString(orderId), Integer.toString(++locationId), 1, 60, 90));
        ordersMap.put(Integer.toString(++orderId), new Order(Integer.toString(orderId), Integer.toString(++locationId), 1, 30, 60));
        ordersMap.put(Integer.toString(++orderId), new Order(Integer.toString(orderId), Integer.toString(++locationId), 1, 60, 90));
        ordersMap.put(Integer.toString(++orderId), new Order(Integer.toString(orderId), Integer.toString(++locationId), 1, 60, 90));
        ordersMap.put(Integer.toString(++orderId), new Order(Integer.toString(orderId), Integer.toString(++locationId), 1, 90, 120));
        ordersMap.put(Integer.toString(++orderId), new Order(Integer.toString(orderId), Integer.toString(++locationId), 1, 60, 90));
        ordersMap.put(Integer.toString(++orderId), new Order(Integer.toString(orderId), Integer.toString(++locationId), 1, 90, 120));
        ordersMap.put(Integer.toString(++orderId), new Order(Integer.toString(orderId), Integer.toString(++locationId), 1, 30, 60));
        ordersMap.put(Integer.toString(++orderId), new Order(Integer.toString(orderId), Integer.toString(++locationId), 1, 90, 120));
        ordersMap.put(Integer.toString(++orderId), new Order(Integer.toString(orderId), Integer.toString(++locationId), 1, 30, 60));
    }
    public OrderDAO() {
    }

    public Order getOrderById(String id) {
        return ordersMap.get(id);
    }

    public Map<String, Order> getAllOrders() {
        return ordersMap;
    }
}

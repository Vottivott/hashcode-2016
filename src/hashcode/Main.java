package hashcode;

import io.shimmen.simpleascii.*;

import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Main {

    static List<SimulationDrone> drones;
    static List<Warehouse> warehouses;
    static Warehouse w0;
    static int numTurns;
    static List<Order> orders;

    //         id       weight
    static Map<Integer, Integer> productWeights = new HashMap<>();

    public static void main(String[] args) throws IOException {

        AsciiReader reader = new AsciiReader("busy_day.in");
        reader.performRead();

        List<Integer> header = reader.nextLine().defaultSectionSplit().assertSectionCount(5).getSectionsAsInts(Radix.Decimal);
        int rows = header.get(0);
        int columns = header.get(1);
        int numDrones = header.get(2);
        numTurns = header.get(3);
        int maxPayload = header.get(4);
        Drone.setMaxPayload(maxPayload);

        int productCount = reader.nextLine().defaultSectionSplit().assertSectionCount(1).getSectionAtIndexAsInt(0, Radix.Decimal);
        List<Integer> productWeightsList = reader.nextLine().defaultSectionSplit().assertSectionCount(productCount).getSectionsAsInts(Radix.Decimal);
        for (int i = 0; i < productWeightsList.size(); i++) {
            productWeights.put(i, productWeightsList.get(i));
        }

        warehouses = new ArrayList<>();
        int numWarehouses = reader.nextLine().defaultSectionSplit().assertSectionCount(1).getSectionAtIndexAsInt(0, Radix.Decimal);
        for (int warehouseIndex = 0; warehouseIndex < numWarehouses; warehouseIndex++) {
            List<Integer> position = reader.nextLine().defaultSectionSplit().assertSectionCount(2).getSectionsAsInts(Radix.Decimal);
            int r = position.get(0);
            int c = position.get(1);
            List<Integer> productsList = reader.nextLine().defaultSectionSplit().assertSectionCount(productCount).getSectionsAsInts(Radix.Decimal);
            warehouses.add(new Warehouse(warehouseIndex, new Point(r, c), productsList));
        }

        orders = new ArrayList<>();
        int numOrders = reader.nextLine().defaultSectionSplit().assertSectionCount(1).getSectionAtIndexAsInt(0, Radix.Decimal);
        for (int orderIndex = 0; orderIndex < numOrders; orderIndex++) {
            List<Integer> position = reader.nextLine().defaultSectionSplit().assertSectionCount(2).getSectionsAsInts(Radix.Decimal);
            int r = position.get(0);
            int c = position.get(1);
            int numItems = reader.nextLine().defaultSectionSplit().assertSectionCount(1).getSectionAtIndexAsInt(0, Radix.Decimal);
            List<Integer> orderList = reader.nextLine().defaultSectionSplit().assertSectionCount(numItems).getSectionsAsInts(Radix.Decimal);
            orders.add(new Order(new Point(r, c), orderList));
        }

        drones = new ArrayList<>();
        for (int droneIndex = 0; droneIndex < numDrones; droneIndex++) {
            drones.add(new SimulationDrone(droneIndex, warehouses.get(0)));
        }



        Map<Point, Integer> ordersToDestinations = new HashMap<>();
        Map<Point, Integer> productsToDestinations = new HashMap<>();
        for (Order order: orders) {
            if (!ordersToDestinations.containsKey(order.getTarget())) {
                ordersToDestinations.put(order.getTarget(), 0);
                productsToDestinations.put(order.getTarget(), 0);
            }
            Integer orderCount = ordersToDestinations.get(order.getTarget());
            ordersToDestinations.put(order.getTarget(), orderCount + 1);

            //Integer productsCount = productsToDestinations.get(order.getTarget());
            //ordersToDestinations.put(order.getTarget(), orderCount + order.getProducts().size());
        }

        w0 = warehouses.get(0);

        // Start sim
        Simulation sim = new Simulation();
        sim.start();


        //
        // Perform strategy: default
        //
        /*
        {
            // i = 0

         w0 = warehouses.get(0);

            for (Drone drone: drones) {

                // Filter impossible orders (in terms of stock)
                List<Order> possibleOrders = filterImpossibleOrders(w0, orders);

                // Filter impossible orders (in terms of weight)
                possibleOrders = filterImpossibleOrdersWeight(orders);

                assert possibleOrders.size() > 0: "Handle this if it becomes a problem!";

                // Sort by distance from w0
                Collections.sort(possibleOrders, (a, b) -> turnsRequired(w0.getPosition(), a.getTarget()) - turnsRequired(w0.getPosition(), b.getTarget()));

                // Get first in list...
                Order first = possibleOrders.get(0);

                // Assign drone to order
                for (Integer productId: first.getProducts().keySet()) {

                    //commands.add(new LoadCommand(drone, w0, productId, first.getProducts().get(productId)));
                }


                //Collections.sort(orders, (o1, o2) -> o1.getNumTotalItems() - o2.getNumTotalItems());
                //System.out.println(turnsRequired(w0.getPosition(), orders.get(orders.size()-1).getTarget()));
            }

        }
*/




        AsciiWriter writer = new AsciiWriter("test.out");
    }

    public static int turnsRequired(Point a, Point b) {
        int dc = a.getC() - b.getC();
        int dr = a.getR() - b.getR();
        return (int)Math.ceil(Math.sqrt(dc*dc + dr*dr));
    }

    public static List<Order> filterImpossibleOrdersWeight(List<Order> orders) {
        return orders.stream().filter((order) -> order.getTotalWeight() <= Drone.getMaximumLoad()).collect(Collectors.toList());
    }

    public static List<Order> filterImpossibleOrders(Warehouse w, List<Order> orders) {
        List<Order> filteredOrders = new ArrayList<>();
        for (Order order : orders) {
            boolean possible = true;
            for (Integer product : order.getProducts().keySet()) {
                if (w.getProducts().containsKey(product)) {
                    int wAmount = w.getProducts().get(product);
                    int oAmount = order.getProducts().get(product);
                    if (oAmount > wAmount) {
                        possible = false;
                        break;
                    }
                } else {
                    possible = false;
                    break;
                }
            }
            if (possible) {
                filteredOrders.add(order);
            }
        }
        return filteredOrders;
    }
}

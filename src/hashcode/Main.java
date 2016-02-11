package hashcode;

import io.shimmen.simpleascii.*;

import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {

        AsciiReader reader = new AsciiReader("busy_day.in");
        reader.performRead();

        List<Integer> header = reader.nextLine().defaultSectionSplit().assertSectionCount(5).getSectionsAsInts(Radix.Decimal);
        int rows = header.get(0);
        int columns = header.get(1);
        int numDrones = header.get(2);
        int numTurns = header.get(3);
        int maxPayload = header.get(4);
        Drone.setMaxPayload(maxPayload);

        int productCount = reader.nextLine().defaultSectionSplit().assertSectionCount(1).getSectionAtIndexAsInt(0, Radix.Decimal);
        //  id       weight
        Map<Integer, Integer> productWeights = new HashMap<>();
        List<Integer> productWeightsList = reader.nextLine().defaultSectionSplit().assertSectionCount(productCount).getSectionsAsInts(Radix.Decimal);
        for (int i = 0; i < productWeightsList.size(); i++) {
            productWeights.put(i, productWeightsList.get(i));
        }

        List<Warehouse> warehouses = new ArrayList<>();
        int numWarehouses = reader.nextLine().defaultSectionSplit().assertSectionCount(1).getSectionAtIndexAsInt(0, Radix.Decimal);
        for (int warehouseIndex = 0; warehouseIndex < numWarehouses; warehouseIndex++) {
            List<Integer> position = reader.nextLine().defaultSectionSplit().assertSectionCount(2).getSectionsAsInts(Radix.Decimal);
            int r = position.get(0);
            int c = position.get(1);
            List<Integer> productsList = reader.nextLine().defaultSectionSplit().assertSectionCount(productCount).getSectionsAsInts(Radix.Decimal);
            warehouses.add(new Warehouse(new Point(r, c), productsList));
        }

        List<Order> orders = new ArrayList<>();
        int numOrders = reader.nextLine().defaultSectionSplit().assertSectionCount(1).getSectionAtIndexAsInt(0, Radix.Decimal);
        for (int orderIndex = 0; orderIndex < numOrders; orderIndex++) {
            List<Integer> position = reader.nextLine().defaultSectionSplit().assertSectionCount(2).getSectionsAsInts(Radix.Decimal);
            int r = position.get(0);
            int c = position.get(1);
            int numItems = reader.nextLine().defaultSectionSplit().assertSectionCount(1).getSectionAtIndexAsInt(0, Radix.Decimal);
            List<Integer> orderList = reader.nextLine().defaultSectionSplit().assertSectionCount(numItems).getSectionsAsInts(Radix.Decimal);
            orders.add(new Order(new Point(r, c), orderList));
        }

        List<Drone> drones = new ArrayList<>();
        for (int droneIndex = 0; droneIndex < numDrones; droneIndex++) {
            drones.add(new Drone(warehouses.get(0)));
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


        {
//            System.out.println(orders.get(orders.size()-1).getNumTotalItems());
            Warehouse W0 = warehouses.get(0);
            //W0.getProducts()
            System.out.println("!");
            List<Order> possibleOrders = filterImpossibleOrders(W0, orders);
            //Collections.sort(orders, (o1, o2) -> o1.getNumTotalItems() - o2.getNumTotalItems());
            Collections.sort(orders, (a, b) -> turnsRequired(W0.getPosition(), a.getTarget()) - turnsRequired(W0.getPosition(), b.getTarget()));
            System.out.println(turnsRequired(W0.getPosition(), orders.get(orders.size()-1).getTarget()));
        }





        AsciiWriter writer = new AsciiWriter("test.out");
    }

    public static int turnsRequired(Point a, Point b) {
        int dc = a.getC() - b.getC();
        int dr = a.getR() - b.getR();
        return (int)Math.ceil(Math.sqrt(dc*dc + dr*dr));
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

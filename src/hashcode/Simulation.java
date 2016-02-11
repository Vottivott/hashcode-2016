package hashcode;

import java.util.*;
import java.util.stream.Collectors;

class SimulationDrone {

    int droneIndex;

    Point start, end, deliveryPoint;
    int stepsTaken = 0;
    int stepsRequired = Main.turnsRequired(start, end);

    //          id       count
    private Map<Integer, Integer> inventory = new HashMap<>();

    public SimulationDrone(int droneIndex, Warehouse origin) {
        this.droneIndex = droneIndex;
        this.start = origin.getPosition();
    }

    public void loadProduct(int id, int count) {
        int currentCount = inventory.get(id);
        inventory.put(id, currentCount + count);
    }

    public void step() {
        stepsTaken += 1;
        if (stepsTaken >= stepsRequired) {

            start = end;

            // Unload


            // Ask for new instruction
            Simulation.awaitingInstructions.add(this);
        }
    }

    public int getDroneIndex() {
        return droneIndex;
    }

    public void setFinalDestination(Point p) {
        this.deliveryPoint = p;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public Point getFinalDestination() {
        return deliveryPoint;
    }
}

public class Simulation {

    static List<Command> commands = new ArrayList<>();

    static List<SimulationDrone> awaitingInstructions = new ArrayList<>();
    List<SimulationDrone> drones = new ArrayList<>(Main.drones);

    public static int radius = 50;

    public void start() {

        // All drones awaiting instr..
        awaitingInstructions = new ArrayList<>(drones);

        List<Order> circledOrders = Main.orders.stream()
                .filter((order -> Main.turnsRequired(Main.w0.getPosition(), order.getTarget()) < radius))
                .collect(Collectors.toList());

        // TODO: Possibly sort...


        for (int i = 0; i < Main.numTurns; i++) {

            // Give drones instructions
            for (SimulationDrone drone: awaitingInstructions) {

                if (!isInWarehouse(drone)) {
                    // Send to closest warehouse
                    Warehouse closest = Collections.min(Main.warehouses, (a, b) ->
                            Main.turnsRequired(drone.start, a.getPosition()) - Main.turnsRequired(drone.start, b.getPosition()));

                    List<Order> possibleOrders = filterImpossibleOrders(closest, Main.orders);


                    Order closestOrder = Collections.min(Main.orders, (a, b) ->
                            Main.turnsRequired(closest.getPosition(), a.getTarget()) - Main.turnsRequired(closest.getPosition(), b.getTarget()));

                    int totalWeight = 0;
                    for (Integer product : closestOrder.getProducts().keySet()) {
                        if (closest.getProducts().containsKey(product)) {
                            while(totalWeight < Drone.getMaximumLoad() && closest.getProducts().get(product) > 0) {
                                totalWeight += Main.productWeights.get(product);
                                drone.loadProduct(product, 1);
                                drone.setFinalDestination(closestOrder.getTarget());
                                drone.setEnd(closest.getPosition());
                                closest.getProducts().put(product, closest.getProducts().get(product) - 1);
                                commands.add(new LoadCommand(drone, closest, product, 1));
                            }
                        }
                    }

                }

                else /* if in warehouse */ {
                    drone.setEnd(drone.getFinalDestination());
                    commands.add(new UnloadCommand(drone, ))
                }

            }

            // Step drones
            for (SimulationDrone drone: drones) {
                drone.step();
            }

        }

    }

    private boolean isInWarehouse(SimulationDrone drone) {
        for (Warehouse w: Main.warehouses) {
            if (w.getPosition().equals(drone.start)) {
                return true;
            }
        }
        return false;
    }

    public List<Order> filterImpossibleOrders(Warehouse w, List<Order> orders) {
        List<Order> filteredOrders = new ArrayList<>();
        for (Order order : orders) {
            for (Integer product : order.getProducts().keySet()) {
                if (w.getProducts().containsKey(product)) {
                    filteredOrders.add(order);
                }
            }
        }
        return filteredOrders;
    }

}

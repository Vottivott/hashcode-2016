package hashcode;

public class UnloadCommand implements Command {

    Drone drone;
    Warehouse warehouse;
    int productId, count;

    public UnloadCommand(Drone drone, Warehouse warehouse, int productId, int count) {
        this.drone = drone;
        this.warehouse = warehouse;
        this.productId = productId;
        this.count = count;
    }

    @Override
    public String toString() {
        return drone.getDroneIndex() + "U" + warehouse.getIndex() + "" + productId + "" + count;
    }
}

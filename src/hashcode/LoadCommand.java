package hashcode;

public class LoadCommand implements Command {

    Drone drone;
    Warehouse warehouse;
    int productId, count;

    public LoadCommand(Drone drone, Warehouse warehouse, int productId, int count) {
        this.drone = drone;
        this.warehouse = warehouse;
        this.productId = productId;
        this.count = count;
    }

    @Override
    public String toString() {
        return drone.getDroneIndex() + "L" + warehouse.getIndex() + "" + productId + "" + count;
    }
}

package hashcode;

public class UnloadCommand implements Command {

    Drone drone;
    Point desination;
    int productId, count;

    public UnloadCommand(Drone drone, Point desination, int productId, int count) {
        this.drone = drone;
        this.desination = desination;
        this.productId = productId;
        this.count = count;
    }

    @Override
    public String toString() {
        return drone.getDroneIndex() + "U" + desination.getIndex() + "" + productId + "" + count;
    }
}

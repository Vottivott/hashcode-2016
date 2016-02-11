package hashcode;

import java.util.HashMap;
import java.util.Map;

public class Drone {

    public static int MAXIMUM_LOAD;


    Point position;

    //          id       count
    private Map<Integer, Integer> inventory = new HashMap<>();

    public Drone(Warehouse origin) {
        this.position = origin.getPosition();
    }

    public void loadProduct(int id, int count) {
        int currentCount = inventory.get(id);
        inventory.put(id, currentCount + count);
    }

    public void unloadProduct(int id, int count) {
        int currentCount = inventory.get(id);
        assert currentCount >= count;
        inventory.put(id, currentCount - count);
    }




    public static void setMaxPayload(int maximumLoad) {
        MAXIMUM_LOAD = maximumLoad;
    }

    public static int getMaximumLoad() {
        return MAXIMUM_LOAD;
    }
}

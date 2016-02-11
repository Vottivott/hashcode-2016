package hashcode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order {

    private Point target;

    //          id       count
    private Map<Integer, Integer> products = new HashMap<>();


    public Order(Point target, List<Integer> orderList) {
        this.target = target;

        for (int item: orderList) {
            if(!products.containsKey(item)) {
                products.put(item, 0);
            }

            Integer currentCount = products.get(item);
            products.put(item, currentCount + 1);
        }
    }

    public Point getTarget() {
        return target;
    }

    public Map<Integer, Integer> getProducts() {
        return products;
    }

    public int getNumTotalItems() {
        int count = 0;
        for (Integer productId: products.keySet()) {
            count += products.get(productId);
        }
        return count;
    }
}

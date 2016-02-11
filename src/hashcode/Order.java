package hashcode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order {

    private Point target;

    //          id       count
    private Map<Integer, Integer> products = new HashMap<>();


    public Order(Point target, List<Integer> productList) {
        this.target = target;

        for (int productId = 0; productId < productList.size(); productId++) {
            products.put(productId, productList.get(productId));
        }
    }

    public Point getTarget() {
        return target;
    }

    public Map<Integer, Integer> getProducts() {
        return products;
    }
}

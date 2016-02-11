package hashcode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Warehouse {

    private Point position;

    //          id       count
    private Map<Integer, Integer> products = new HashMap<>();


    public Warehouse(Point position, List<Integer> productList) {
        this.position = position;

        for (int productId = 0; productId < productList.size(); productId++) {
            if(!products.containsKey(productId)) {
                products.put(productId, 0);
            }

            Integer currentCount = productList.get(productId);
            products.put(productId, currentCount + 1);
        }
    }

    public Point getPosition() {
        return position;
    }

    public Map<Integer, Integer> getProducts() {
        return products;
    }
}

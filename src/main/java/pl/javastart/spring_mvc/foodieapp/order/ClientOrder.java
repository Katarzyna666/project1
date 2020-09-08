package pl.javastart.spring_mvc.foodieapp.order;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import pl.javastart.spring_mvc.foodieapp.item.Item;

@Component
@SessionScope
public class ClientOrder {
    private Order order;

    public ClientOrder() {
        clear();
    }

    Order getOrder() {
        return order;
    }

    void clear() {
        order = new Order();
        order.setStatus(Status.NEW);
    }

    void add(Item item) {
        order.getItems().add(item);
    }
}
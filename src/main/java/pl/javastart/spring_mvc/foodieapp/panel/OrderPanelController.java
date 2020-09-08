package pl.javastart.spring_mvc.foodieapp.panel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.javastart.spring_mvc.foodieapp.item.Item;
import pl.javastart.spring_mvc.foodieapp.order.Order;
import pl.javastart.spring_mvc.foodieapp.order.OrderRepository;
import pl.javastart.spring_mvc.foodieapp.order.Status;

import java.util.List;
import java.util.Optional;

@Controller
public class OrderPanelController {
    private OrderRepository orderRepository;

    @Autowired
    public OrderPanelController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/panel/zamowienia")
    public String getOrders(@RequestParam(required = false) Status status, Model model) {
        List<Order> orders;
        if (status == null) {
            orders = orderRepository.findAll();
        } else
            orders = orderRepository.findByStatus(status);
        model.addAttribute("orders", orders);
        return "panel/orders";
    }

    @GetMapping("/panel/zamowienie/{id}")
    public String singleOrder(@PathVariable Long id, Model model) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(order1 -> singleOrderPanel(order1, model))
                .orElse("redirect:/");
    }

    @PostMapping("/panel/zamowienie/{id}")
    public String changeOrder(@PathVariable Long id, Model model) {
        Optional<Order> order = orderRepository.findById(id);
        order.ifPresent(order1 -> {
            order1.setStatus(Status.nextStatus(order.get().getStatus()));
            orderRepository.save(order1);
        });
        return order.map(order1 -> singleOrderPanel(order1, model)).
                orElse("redirect:/");
    }

    public String singleOrderPanel(Order order, Model model) {
        model.addAttribute("order", order);
        model.addAttribute("sum", order.getItems().stream().mapToDouble(Item::getPrice).sum());
        return "panel/order";
    }
}
package pl.javastart.spring_mvc.foodieapp.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.javastart.spring_mvc.foodieapp.common.Message;
import pl.javastart.spring_mvc.foodieapp.item.Item;
import pl.javastart.spring_mvc.foodieapp.item.ItemRepository;

import java.util.Optional;

@Controller
public class OrderController {

    private OrderRepository orderRepository;
    private ClientOrder clientOrder;
    private ItemRepository itemRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository, ClientOrder clientOrder, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.clientOrder = clientOrder;
        this.itemRepository = itemRepository;
    }

    @GetMapping("/zamowienie/dodaj")
    public String addItemToOrder(@RequestParam Long itemId, Model model) {
        Optional<Item> orderedItem = itemRepository.findById(itemId);
        orderedItem.ifPresent(item -> clientOrder.add(item));
        if (orderedItem.isPresent()) {
            model.addAttribute("message", new Message("Dodano do zamówienia", orderedItem.get().getName()));
        } else {
            model.addAttribute("message", new Message("Nie dodano do zamówienia, ", "pobrano błędne id " + itemId));
        }
        return "message";
    }

    @GetMapping("/zamowienie")
    public String getCurrentOrder(Model model) {
        Order order = clientOrder.getOrder();
        model.addAttribute("order", order);
        model.addAttribute("sum", order.getItems().stream().mapToDouble(Item::getPrice).sum());
        return "order";
    }

    @PostMapping("/zamowienie/realizuj")
    public String proceedOrder(@RequestParam String address, @RequestParam String telephone, Model model) {
        Order order = clientOrder.getOrder();
        order.setAddress(address);
        order.setTelephone(telephone);
        orderRepository.save(order);
        clientOrder.clear();
        model.addAttribute("message", new Message("Dziękujemy", "Zamówienie przekazano do realizacji"));
        return "message";
    }
}
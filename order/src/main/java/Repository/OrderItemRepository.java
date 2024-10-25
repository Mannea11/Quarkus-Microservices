package Repository;

import Model.Order;
import Model.OrderItem;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public class OrderItemRepository implements PanacheRepository<OrderItem> {
}

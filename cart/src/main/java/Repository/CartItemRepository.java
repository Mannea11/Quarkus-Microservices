package Repository;

import Model.CartItem;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CartItemRepository implements PanacheRepository<CartItem> {
}

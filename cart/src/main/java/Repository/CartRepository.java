package Repository;

import Model.Cart;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class CartRepository implements PanacheRepository<Cart> {

    public Optional<Cart> findByUsername(String username) {
        return find("username", username).firstResultOptional();
    }
}

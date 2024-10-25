package Service;

import Config.RESTClient;
import DTO.CartDTO;
import DTO.CartItemDTO;
import Model.Order;
import Model.OrderItem;
import Repository.OrderRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class OrderService {

    @Inject
    JsonWebToken jwt;

    @Inject
    OrderRepository orderRepository;

    @RestClient
    RESTClient restClient;
    @Transactional
    public Response createOrderFromCart(String token) {
        CartDTO cart = restClient.getFullCart(token);
        if (cart != null) {
            return convertCartToOrder(cart, token);
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Kunde inte hämta varukorgen").build();
        }
    }

    private Response convertCartToOrder(CartDTO cart, String token) {
        Order order = new Order();
        order.setUsername(extractUsernameFromJwt(token));
        order.setTotalPrice(cart.getTotalPrice());

        List<OrderItem> orderItems = convertCartItemsToOrderItems(cart.getCartItems(), order);
        order.setItems(orderItems);
        orderRepository.persist(order);

        return Response.ok("Order skapad från varukorgen: " + order).build();
    }

    private List<OrderItem> convertCartItemsToOrderItems(List<CartItemDTO> cartItems, Order order) {
        if (cartItems == null) {
            return new ArrayList<>();
        }
        return cartItems.stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setProductName(cartItem.getProductName());
            orderItem.setPrice(cartItem.getPrice());
            return orderItem;
        }).collect(Collectors.toList());
    }

    private String extractUsernameFromJwt(String token) {
        token = token.trim().replace("Bearer ", "");
        String username = jwt.getName();
        return username;
    }
}

package Service;


import Config.RESTClient;
import DTO.CartDTO;
import DTO.CartItemDTO;
import DTO.ProductDTO;
import Model.Cart;
import Model.CartItem;
import Repository.CartItemRepository;
import Repository.CartRepository;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class CartService {

    @Inject
    @RestClient
    RESTClient restClient;

    @Inject
    JWTParser jwtParser;

    @Inject
    CartItemRepository cartItemRepository;
    @Inject
    CartRepository cartRepository;

    public void addItemToCart(CartItemDTO cartItemDTO, String token) {
        String username = extractUsernameFromJwt(token);
        ProductDTO product = restClient.getProductById(cartItemDTO.getProductId());
        CartItem cartItem = new CartItem();
        cartItem.setProductId(cartItemDTO.getProductId());
        cartItem.setProductName(product.getName());
        cartItem.setPrice(product.getPrice());

        Cart cart = getOrCreateCart(username);
        cartItem.setCart(cart);

        cartItemRepository.persist(cartItem);
        updateTotalPrice(cart);
    }

    public String extractUsernameFromJwt(String token) {
        token = token.trim().replace("Bearer ", "");
        try {
            JsonWebToken jwt = jwtParser.parse(token);
            return jwt.getName();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Cart getOrCreateCart(String username) {
        Optional<Cart> optionalCart = cartRepository.findByUsername(username);
        return optionalCart.orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUsername(username);
            newCart.setCartItems(new ArrayList<>());
            cartRepository.persist(newCart);
            return newCart;
        });
    }
@Transactional
    public void deleteCart(String token) {
        String username = extractUsernameFromJwt(token);
        Optional<Cart> optionalCart = cartRepository.findByUsername(username);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            cartRepository.delete(cart);
        } else {
            System.out.println("varukorg ej hittad");
        }
    }
    private void updateTotalPrice(Cart cart) {
        List<CartItem> cartItems = cart.getCartItems();
        double totalPrice = 0.0;
        for (CartItem item : cartItems) {
            totalPrice += item.getPrice();
        }
        cart.setTotalPrice(totalPrice);
        cartRepository.persist(cart);
    }

    public List<String> getAllCartItems(String token) {
        String username = extractUsernameFromJwt(token);
        Optional<Cart> optionalCart = cartRepository.findByUsername(username);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            List<String> cartItemsInfo = new ArrayList<>();
            for (CartItem item : cart.getCartItems()) {
                String itemInfo = item.getProductName() + " - " + item.getPrice() + " kr";
                cartItemsInfo.add(itemInfo);
            }
            return cartItemsInfo;
        } else {
            System.out.println("Varukorgen kunde inte hittas för användaren: " + username);
            return Collections.emptyList();
        }
    }

    public CartDTO getFullCart(String token) {
        String username = extractUsernameFromJwt(token);
        Optional<Cart> optionalCart = cartRepository.findByUsername(username);
        if (optionalCart.isPresent()) {
            return convertToCartDTO(optionalCart.get());
        } else {
            return null;
        }
    }
    private CartDTO convertToCartDTO(Cart cart) {
        List<CartItemDTO> cartItemDTOs = cart.getCartItems().stream().map(this::convertToCartItemDTO).collect(Collectors.toList());
        return new CartDTO(cart.getCartId(),cart.getUsername(), cart.getTotalPrice(), cartItemDTOs);
    }

    private CartItemDTO convertToCartItemDTO(CartItem item) {
        return new CartItemDTO(item.getProductId(), item.getProductName(), item.getPrice());
    }
}



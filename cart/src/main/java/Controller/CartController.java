package Controller;

import DTO.CartDTO;
import DTO.CartItemDTO;
import Service.CartService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/cart")
public class CartController {

    @Inject
    JsonWebToken jwt;

    @Inject
    CartService cartService;


    @DELETE
    @Path("/deletecart")
    public Response deleteCart(@HeaderParam("Authorization") String token) {
        cartService.deleteCart(token);
        return Response.ok("Varukorgen är raderad").build();
    }

    @GET
    @Path("/cartitems")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCartItems(@HeaderParam("Authorization") String token) {
        List<String> cartItemsInfo = cartService.getAllCartItems(token);
        return Response.ok(cartItemsInfo).build();
    }

    @GET
    @Path("/fullcart")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFullCart(@HeaderParam("Authorization") String token) {
        CartDTO cartDTO = cartService.getFullCart(token);
        if (cartDTO != null) {
            return Response.ok(cartDTO).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}



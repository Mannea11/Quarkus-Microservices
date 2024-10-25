package Controller;

import Service.OrderService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/order")
@Singleton
public class OrderController {

    @Inject
    OrderService orderService;

    @POST
    @Path("/create")
    public Response createOrderFromCart(@HeaderParam("Authorization") String token) {
        try {
            return orderService.createOrderFromCart(token);
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ett fel inträffade: " + e.getMessage())
                    .build();
        }
    }
}

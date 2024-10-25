package Config;

import DTO.CartDTO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


@RegisterRestClient
public interface RESTClient {

    @GET
    @Path("/cart/fullcart")
    CartDTO getFullCart(@HeaderParam("Authorization") String token);
}




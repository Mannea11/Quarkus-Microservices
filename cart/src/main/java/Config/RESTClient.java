package Config;

import DTO.ProductDTO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
public interface RESTClient {

    @GET
    @Path("/product/id/{id}")
    ProductDTO getProductById(@PathParam("id") int productId);
}
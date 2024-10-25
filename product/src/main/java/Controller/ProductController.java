package Controller;

import Model.Product;
import Repository.ProductRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@Path("/product")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {

    @Inject
    ProductRepository productRepository;

    @GET
    @Path("/all")
    public Iterable<Product> getAllProducts() {
        return productRepository.findAll().list();
    }

    @GET
    @Path("/id/{id}")
    public Response getById(@PathParam("id") Long id) {
        Optional<Product> product = Optional.ofNullable(productRepository.findById(id));
        if (product.isPresent()) {
            return Response.ok(product.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}

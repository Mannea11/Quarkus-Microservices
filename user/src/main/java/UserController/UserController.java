package UserController;

import Config.JWTUtil;
import Model.User;
import Repository.UserRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.security.InvalidAlgorithmParameterException;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    JWTUtil jwtUtil;
    @Inject
    UserRepository userRepository;

    @POST
    @Path("/register")
    @Transactional
    public Response register(User user) {
        userRepository.persist(user);
        return Response.ok(user).build();
    }

    @POST
    @Path("/login")
    public Response login(User user) throws InvalidAlgorithmParameterException {

            String username = user.getUsername();
            String password = user.getPassword();

            User authenticatedUser = userRepository.findByUserNameAndPassword(username, password);
            String token = jwtUtil.generateToken(authenticatedUser);
            return Response.ok(token).build();
    }
}


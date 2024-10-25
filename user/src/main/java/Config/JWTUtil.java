package Config;
import Model.User;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;


@ApplicationScoped
public class JWTUtil {


        public String generateToken(User user) {
            long duration = Duration.ofHours(1).getSeconds();
            return Jwt.claims()
                    .subject(user.getUsername())
                    .issuer("QuarkusMicro")
                    .expiresAt(System.currentTimeMillis() / 1000 + duration)
                    .sign();
        }
    }





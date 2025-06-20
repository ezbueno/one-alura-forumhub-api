package developer.ezandro.forumhubapi.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import developer.ezandro.forumhubapi.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateJwtToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            return JWT.create()
                    .withIssuer("ForumHub API")
                    .withSubject(user.getEmail())
                    .withExpiresAt(this.expirationDate())
                    .sign(algorithm);
        } catch (
                JWTCreationException exception) {
            throw new JWTCreationException("Error generating JWT token!", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            return JWT.require(algorithm)
                    .withIssuer("ForumHub API")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (
                JWTVerificationException exception) {
            throw new JWTVerificationException("Invalid or expired JWT token!", exception);
        }
    }

    private Instant expirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
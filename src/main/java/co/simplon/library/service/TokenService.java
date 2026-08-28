package co.simplon.library.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final JwtEncoder encoder;

    public TokenService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    public String generateToken(Authentication auth) {
        // crée l'en-tête avec l'algorithme de signature RSA
        JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256).build();

        String scope = auth.getAuthorities().stream()
                .map((authority) -> authority.getAuthority())
                .collect(Collectors.joining(" ")); // ex: "ROLE_USER ROLE_ADMIN"

        Instant now = Instant.now();
        JwtClaimsSet payload = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(auth.getName())
                .claim("scope", scope)
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(header, payload)).getTokenValue();
    }
}
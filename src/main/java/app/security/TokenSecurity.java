package app.security;

import app.enums.Role;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import app.dtos.UserDTO;
import com.nimbusds.jwt.SignedJWT;
//import dk.bugelhartmann.ITokenSecurity;
import app.security.interfaces.ITokenSecurity;
import dk.bugelhartmann.TokenCreationException;
import dk.bugelhartmann.TokenVerificationException;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
public class TokenSecurity implements ITokenSecurity {

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO getUserWithRolesFromToken(String token) throws ParseException {
        // Return a user with Set of roles as strings
        SignedJWT jwt = SignedJWT.parse(token);
        String roles = jwt.getJWTClaimsSet().getClaim("roles").toString();
        String username = jwt.getJWTClaimsSet().getClaim("email").toString();

        Set<Role> rolesSet = Arrays
                .stream(roles.split(","))
                .map(r -> Role.valueOf(r))
                .collect(Collectors.toSet());
        return new UserDTO(username, rolesSet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean tokenIsValid(String token, String secret) throws ParseException, TokenVerificationException {
        boolean verified = false;
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            verified = jwt.verify(new MACVerifier(secret));
        } catch (JOSEException e) {
            throw new TokenVerificationException("Could not verify token", e.getCause());
        }
        if (verified)
            return true;
        else
            return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean tokenNotExpired(String token) throws ParseException{
        if (timeToExpire(token) > 0)
            return true;
        else
            return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int timeToExpire(String token) throws ParseException{
        SignedJWT jwt = SignedJWT.parse(token);
        return (int) (jwt.getJWTClaimsSet().getExpirationTime().getTime() - new Date().getTime());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String createToken(UserDTO user, String ISSUER, int TOKEN_EXPIRE_TIME, String SECRET_KEY) {
        // https://codecurated.com/blog/introduction-to-jwt-jws-jwe-jwa-jwk/
        try {
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getEmail())
                    .issuer(ISSUER)
                    .claim("email", user.getEmail())
                    .claim("roles", user.getRoles().stream()
                            .map(r -> r.name())
                            .reduce((s1, s2) -> s1 + "," + s2).get())
                    .expirationTime(new Date(new Date().getTime() + TOKEN_EXPIRE_TIME))
                    .build();
            Payload payload = new Payload(claimsSet.toJSONObject());

            JWSSigner signer = new MACSigner(SECRET_KEY);
            JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
            JWSObject jwsObject = new JWSObject(jwsHeader, payload);
            jwsObject.sign(signer);
            return jwsObject.serialize();

        } catch (JOSEException e) {
            throw new RuntimeException("Could not create token", e);
        }
    }
}
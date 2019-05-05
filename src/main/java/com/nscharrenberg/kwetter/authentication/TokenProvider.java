package com.nscharrenberg.kwetter.authentication;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import com.nscharrenberg.kwetter.responses.HttpStatusCodes;
import com.nscharrenberg.kwetter.responses.ObjectResponse;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.Key;
import java.util.Date;

import static java.lang.Thread.currentThread;

public class TokenProvider {
    private static String SECRET_KEY = "/privateKey.pem";
    public static String ISSUER = "Kwetter";
    public static int TIME_TO_LIVE = 800000;
    public static String AUTHENTICATION_SCHEME = "Bearer";

    public static ObjectResponse<String> generate(String id, String issuer, String subject, long ttlMillis) {
        try {
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);


            byte[] apiKeySecretBytes = new byte[0];
            apiKeySecretBytes = DatatypeConverter.parseBase64Binary(getSecretKey(SECRET_KEY));
            Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

            JwtBuilder builder = Jwts
                    .builder()
                    .setId(id)
                    .setIssuedAt(now)
                    .setSubject(subject)
                    .setIssuer(issuer)
                    .signWith(signatureAlgorithm, signingKey);

            //if it has been specified, let's add the expiration
            if (ttlMillis >= 0) {
                long expMillis = nowMillis + ttlMillis;
                Date exp = new Date(expMillis);
                builder.setExpiration(exp);
            }

            return new ObjectResponse<>(HttpStatusCodes.OK, "Token created", builder.compact());
        } catch (Exception e) {
            return new ObjectResponse<>(HttpStatusCodes.INTERNAL_SERVER_ERROR, "Something went wrong");
        }
    }

    private static String getSecretKey(String resource) throws IOException {
        byte[] byteBuffer = new byte[16384];
        int length = currentThread().getContextClassLoader().getResource(resource).openStream().read(byteBuffer);

        String key = new String(byteBuffer, 0, length)
                .replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)----", "")
                .replaceAll("\r\n", "")
                .replaceAll("\n", "")
                .trim();

        return key;
    }

    public static ObjectResponse<Claims> decode(String bearer) {
        try {
            if(bearer.startsWith(String.format("%s ", AUTHENTICATION_SCHEME))) {
                String token  = bearer.substring(TokenProvider.AUTHENTICATION_SCHEME.length()).trim();

                return new ObjectResponse<>(HttpStatusCodes.OK, "Authorized", Jwts.parser()
                        .setSigningKey(DatatypeConverter.parseBase64Binary(getSecretKey(SECRET_KEY)))
                        .parseClaimsJws(token)
                        .getBody());
            } else {
                return new ObjectResponse<>(HttpStatusCodes.UNAUTHORIZED, "You are not authorized");
            }
        } catch (Exception e) {
            return new ObjectResponse<>(HttpStatusCodes.UNAUTHORIZED, "You are not authorized");
        }

    }
}

package com.jwtdemo1.demo.jwtUtil;

import java.io.Serializable;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Encoders;
//import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;
@Component
public class JwtUtil implements Serializable{
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 844033501250937392L;
	public static final long JWT_TOKEN_VALIDITY = 5*60*60;
	private String sectetKey = "MyPintos111"; 
//	//creates a spec-compliant secure-random key:
//	SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); //or HS384 or HS512
//
//	//If you wanted to store the generated key as a String, you could presumably Base64 encode it:
//
//	String base64Key = Encoders.BASE64.encode(key.getEncoded());
	
	
	
	//retrieve user name from jwt token
	public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
// retrieve expiration date from the JWT token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(sectetKey)
                .parseClaimsJws(token)
                .getBody();
    }
    
    
 // check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    
    
 // generate token for user
    public String generateToken(UserDetails userDetails) {
    	Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims,userDetails.getUsername());
    }

    //generate token for user
    private String doGenerateToken(Map<String, Object> claims,String subject) {
    	// while creating the token -
    	// 1. Define claims of the token, like Issuer, Expiration, Subject, and the ID
    	// 2. Sign the JWT using the HS512 algorithm and secret key.
    	// 3. According to JWS Compact
    	// Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    	// compaction of the JWT to a URL-safe string
    	
//        Claims claims = Jwts.claims().setSubject(subject);
//        claims.put("scopes", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                //.setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY*1000))
                .signWith(SignatureAlgorithm.HS256, sectetKey)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (
              username.equals(userDetails.getUsername())
                    && !isTokenExpired(token));
    }



}

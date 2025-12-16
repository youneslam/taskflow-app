package com.dev.taskboard.User.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Clé secrète Base64 pour signer et vérifier les tokens
    private static final String SECRET_KEY = "Uv6/oHQZRzBmy0EeMPkIbKD8eRE6xCUdtDCSOqklwcY=";


    /** Extrait le username (subject) depuis un token */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /** Extrait une claim spécifique depuis un token */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /** Génère un token simple pour un utilisateur */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /** Vérifie si un token est valide pour un utilisateur donné */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /** Vérifie si le token est expiré */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    /** Génère un token avec des claims personnalisées */
    private String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)                          // Ajouter les claims personnalisées
                .setSubject(userDetails.getUsername())       // Username
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Date d’émission
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // Expiration
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Signature HS256
                .compact();
    }

    /** Extrait la date d’expiration du token */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /** Extrait toutes les claims depuis un token */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()                  // Parser pour lire le token
                .verifyWith(getSigningKey()) // Vérifie la signature
                .build()
                .parseSignedClaims(token)    // Parse le JWT signé
                .getPayload();               // Retourne les claims
    }

    /** Retourne la clé secrète pour signer et vérifier le token */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

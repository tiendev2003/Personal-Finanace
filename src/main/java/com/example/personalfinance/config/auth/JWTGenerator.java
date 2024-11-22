package com.example.personalfinance.config.auth;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTGenerator {

	private SecretKey key = Keys.hmacShaKeyFor("trancongtien123123123123123123123213".getBytes());

	public String generateToken(Authentication authentication) {

		String token = Jwts.builder()

				.issuedAt(new Date())
				.expiration(new Date(new Date().getTime() + 86400000))
				.signWith(key)
				.claim("email", authentication.getName())
				.compact();
		System.out.println("New token :");
		System.out.println(token);
		return token;
	}

	public String getUsernameFromJWT(String token) {
		Claims claims = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		String email = String.valueOf(claims.get("email"));

		return email;
	}

	public String getEmailFromJwtToken(String token) {
		Claims claims = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		String email = String.valueOf(claims.get("email"));

		return email;
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser()
					.verifyWith(key)
					.build()
					.parseSignedClaims(token)
					.getPayload();
			return true;
		} catch (Exception ex) {
			throw new AuthenticationCredentialsNotFoundException("JWT was exprired or incorrect",
					ex.fillInStackTrace());
		}
	}

	public String getTokenFromHeader(String token) {
		if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
			return token.substring(7);
		}
		return null;
	}
}

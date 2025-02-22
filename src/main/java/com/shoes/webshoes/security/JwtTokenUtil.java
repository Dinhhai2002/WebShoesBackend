package com.shoes.webshoes.security;

import java.io.Serializable;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -2550185165626007488L;

	public static final long JWT_TOKEN_VALIDITY = 15 * 60 * 60;

	@Autowired
	ApplicationProperties applicationProperties;

	// lấy ra username từ jwt token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	// lấy expiration date từ jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	// trích xuất tất cả các Claims từ token JWT bằng cách sử dụng giải thuật HS512
	// và secret key.
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(applicationProperties.getJwtSecret()).parseClaimsJws(token).getBody();
	}

	// kiểm tra token có hết hạn hay chưa
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	// tạo ra token cho user
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new Hashtable<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}

	// thực hiện việc tạo token
	private String doGenerateToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, applicationProperties.getJwtSecret()).compact();
	}

	// validate token
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}

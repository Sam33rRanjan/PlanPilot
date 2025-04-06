package incture.planPilot.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import incture.planPilot.entity.User;
import incture.planPilot.service.user.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	@Autowired
	private UserService userService;
	
	private String staticKey = "413F4428472B4B6250655368566D5970337336763979244226452948404D6351";
	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
	
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}
	
	private String generateToken(Map<String, Object> claims, UserDetails userDetails) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	private Key getSigningKey() {
		byte[] keyBytes=Decoders.BASE64.decode(staticKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public boolean isTokenValid(String token, UserDetails userDetails) {
		try {
			String username = extractUsername(token);
			return username.equals(userDetails.getUsername()) && !isTokenExpired(token);			
		} catch (Exception e) {
			logger.error("Error validating token: " + e.getMessage());
			return false;
		}
	}
	
	public String extractUsername(String token) {
		try {
			return extractClaims(token, Claims::getSubject);			
		} catch (Exception e) {
			logger.error("Error extracting username: " + e.getMessage());
			return null;
		}
	}
	
	public boolean isTokenExpired(String token) {
		try {
			return extractExpiration(token).before(new Date());			
		} catch (Exception e) {
			logger.error("Error checking token expiration: " + e.getMessage());
			return true;
		}
	}
	
	public Date extractExpiration(String token) {
		try {
			return extractClaims(token, Claims::getExpiration);			
		} catch (Exception e) {
			logger.error("Error extracting expiration date: " + e.getMessage());
			return null;
		}
	}
	
	private <T> T extractClaims(String token, Function<Claims,T> claimsResolver) {
		try {
			Claims claims = extractAllClaims(token);
			return claimsResolver.apply(claims);			
		} catch (Exception e) {
			logger.error("Error extracting claims: " + e.getMessage());
			return null;
		}
	}
	
	private Claims extractAllClaims(String token) {
		try {
			return Jwts.parserBuilder()
					.setSigningKey(getSigningKey())
					.build()
					.parseClaimsJws(token)
					.getBody();						
		} catch (Exception e) {
			logger.error("Error extracting all claims from token: " + e.getMessage());
			return null;
		}
	}
	
	public User getLoggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String email = userDetails.getUsername();
			User user = userService.getUserByEmail(email).get();
			return user;
		}
		return null;
	}

}

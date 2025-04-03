package incture.planPilot.util;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

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
	private String secretKey = "";
	
	public JwtUtil() {
		KeyGenerator keyGen;
		try {
			keyGen = KeyGenerator.getInstance("HmacSHA256");
			SecretKey sk = keyGen.generateKey();
			secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Error generating secret key");
		}
		System.out.println("Secret Key for signingKey: " + secretKey);
	}
	
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
	
	public boolean isTokenValid(String token, UserDetails userDetails) throws Exception {
		String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}
	
	public String extractUsername(String token) throws Exception {
		return extractClaims(token, Claims::getSubject);
	}
	
	public boolean isTokenExpired(String token) throws Exception {
		return extractExpiration(token).before(new Date());
	}
	
	public Date extractExpiration(String token) throws Exception {
		return extractClaims(token, Claims::getExpiration);
	}
	
	private <T> T extractClaims(String token, Function<Claims,T> claimsResolver) throws Exception {
		Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) throws Exception {
		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();			
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

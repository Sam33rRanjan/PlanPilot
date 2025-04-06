package incture.planPilot.config;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import incture.planPilot.service.jwt.JwtService;
import incture.planPilot.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private JwtService jwtService;
	
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) 
			throws ServletException, IOException {
		logger.debug("Starting JWT authentication filter");
		final String authorizationHeader = request.getHeader("Authorization");
		final String jwtString;
		final String userEmail;
		
		if(StringUtils.isEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
			logger.debug("Authorization header is missing or does not start with Bearer");
			filterChain.doFilter(request, response);
			return;
		}
		logger.debug("Extracting JWT from request header");
		jwtString = authorizationHeader.substring(7);
		try {
			logger.debug("Extracting user email from JWT");
			userEmail = jwtUtil.extractUsername(jwtString);
			if(StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
				logger.debug("Loading user details for email: {}", userEmail);
				UserDetails userDetails = jwtService.userDetailsService().loadUserByUsername(userEmail);
				if(jwtUtil.isTokenValid(jwtString, userDetails)) {
					logger.debug("JWT is valid, setting authentication in security context");
					SecurityContext context = SecurityContextHolder.createEmptyContext();
					logger.debug("Creating authentication token for user: {}", userEmail);
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					logger.debug("Setting authentication details");
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					logger.debug("Setting authentication in security context");
					context.setAuthentication(authToken);
					logger.debug("Setting security context holder");
					SecurityContextHolder.setContext(context);
				}
			}
			logger.debug("Continuing filter chain");
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			logger.error("Error occurred during JWT authentication ", e);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		}
	}

}

package incture.planPilot.controller.auth;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import incture.planPilot.dao.UserRepository;
import incture.planPilot.dto.LoginRequest;
import incture.planPilot.dto.LoginResponse;
import incture.planPilot.dto.SignupRequest;
import incture.planPilot.dto.UserDto;
import incture.planPilot.entity.User;
import incture.planPilot.service.auth.AuthService;
import incture.planPilot.service.jwt.JwtService;
import incture.planPilot.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@PostMapping("/signup")
	public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
		if(authService.hasUserWithEmail(signupRequest.getEmail())) {
			logger.error("Singup failed: User with email already exists");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User with email already exists");
		}
		if(authService.hasUserWithUsername(signupRequest.getUsername())) {
			logger.error("Singup failed: User with username already exists");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User with username already exists");
		}
		logger.info("Creating new user with email: " + signupRequest.getEmail());
		UserDto createdUser = authService.signupUser(signupRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		logger.info("Logging in user with email: " + loginRequest.getEmail());
		logger.debug("Authenticating user with email: " + loginRequest.getEmail());
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		logger.debug("User authenticated successfully");
		logger.debug("Loading user details for email: " + loginRequest.getEmail());
		final UserDetails userDetails = jwtService.userDetailsService().loadUserByUsername(loginRequest.getEmail());
		logger.debug("User details loaded successfully");
		logger.debug("Generating JWT token for user with email: " + loginRequest.getEmail());
		final String jwt = jwtUtil.generateToken(userDetails);
		logger.debug("JWT token generated successfully");
		LoginResponse loginResponse = new LoginResponse();
		Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());
		if(user.isPresent()) {
			loginResponse.setJwt(jwt);
			loginResponse.setUserId(user.get().getId());
			loginResponse.setUserRole(user.get().getUserRole());			
		}
		logger.info("User logged in successfully with email: " + loginRequest.getEmail());
		return ResponseEntity.ok(loginResponse);
	}
	
}

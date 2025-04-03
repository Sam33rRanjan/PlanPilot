package incture.planPilot.controller.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
	
	@PostMapping("/signup")
	public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
		if(authService.hasUserWithEmail(signupRequest.getEmail())) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User with email already exists");
		}
		if(authService.hasUserWithUsername(signupRequest.getUsername())) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User with username already exists");
		}
		UserDto createdUser = authService.signupUser(signupRequest);
		if(createdUser == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User could not be created");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
	}
	
	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginRequest loginRequest) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Incorrect username or password");
		}
		final UserDetails userDetails = jwtService.userDetailsService().loadUserByUsername(loginRequest.getEmail());
		Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());
		final String jwt = jwtUtil.generateToken(userDetails);
		LoginResponse authenticationResponse = new LoginResponse();
		if(user.isPresent()) {
			authenticationResponse.setJwt(jwt);
			authenticationResponse.setUserId(user.get().getId());
			authenticationResponse.setUserRole(user.get().getUserRole());			
		}
		return authenticationResponse;
	}
	
}

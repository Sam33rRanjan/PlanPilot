package incture.planPilot.service.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import incture.planPilot.dao.UserRepository;

@Service
public class JwtServiceImplementation implements JwtService {
	
	@Autowired
	private UserRepository userRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(JwtServiceImplementation.class);
	
	@Override
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				try {
					logger.info("Loading user by username: " + username);
					return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
				} catch (Exception e) {
					logger.error("Error loading user by username: " + e.getMessage());
					return null;
				}
			}
		};
	}

}

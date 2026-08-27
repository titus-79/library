package co.simplon.library.service;

import co.simplon.library.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService  implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepositoryInjected) {
        this.userRepository = userRepositoryInjected;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username " + username));
    }

    public boolean existsByUsername(String username) {
        return this.userRepository.findByUsername(username).isPresent();
    }
}

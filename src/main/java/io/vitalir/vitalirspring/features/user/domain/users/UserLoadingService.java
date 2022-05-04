package io.vitalir.vitalirspring.features.user.domain.users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserLoadingService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserLoadingService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userOptional = userRepository.getUserByEmail(username);
        if (userOptional.isEmpty()) {
            log.debug("Email " + username + " was not found");
            throw new UsernameNotFoundException("Email or password  was not found");
        }
        var user = userOptional.get();
        return new User(
                user.getEmail(),
                user.getPassword(),
                user.getRole().getAuthorities()
        );
    }
}

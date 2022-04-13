package io.vitalir.vitalirspring.features.user;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userOptional = userRepository.getUserByEmail(username);
        if (userOptional.isEmpty()) {
            return null;
        }
        var user = userOptional.get();
        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}

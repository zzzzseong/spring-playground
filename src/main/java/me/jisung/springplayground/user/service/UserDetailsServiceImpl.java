package me.jisung.springplayground.user.service;

import lombok.RequiredArgsConstructor;
import me.jisung.springplayground.user.entity.UserDetailsImpl;
import me.jisung.springplayground.user.entity.UserEntity;
import me.jisung.springplayground.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with -> email : " + email)
        );
        return new UserDetailsImpl(user);
    }
}

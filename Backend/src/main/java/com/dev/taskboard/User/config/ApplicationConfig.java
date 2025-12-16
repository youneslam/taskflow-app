package com.dev.taskboard.User.config;

import com.dev.taskboard.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

@Configuration
@RequiredArgsConstructor

public class ApplicationConfig {
    private final UserRepository userRepository;
    @Bean
    UserDetailsService userDetailsService(){
        return username -> userRepository.findByEmail(username).orElseThrow(() ->new UsernameNotFoundException("User not found"));
    }
}

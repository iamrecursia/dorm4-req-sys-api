package com.kozitskiy.dorm4.sys.security.service;

import com.kozitskiy.dorm4.sys.dto.AuthRequestDto;
import com.kozitskiy.dorm4.sys.dto.AuthResponseDto;
import com.kozitskiy.dorm4.sys.entities.User;
import com.kozitskiy.dorm4.sys.repositories.UserRepository;
import com.kozitskiy.dorm4.sys.security.utils.JwtTokenProvider;
import com.kozitskiy.dorm4.sys.service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserRepository userRepository;

    public AuthResponseDto authenticate(AuthRequestDto authRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDto.username(),
                        authRequestDto.password()
                )
        );

        User user = userService.findUserByUsername(authRequestDto.username());
        String token = jwtTokenProvider.generateToken(authentication);

        user.setToken(token);
        userRepository.save(user);


        return AuthResponseDto.builder()
                .token(token)
                .build();
    }
}

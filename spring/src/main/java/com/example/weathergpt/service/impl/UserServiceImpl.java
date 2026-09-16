package com.example.weathergpt.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.weathergpt.domain.dto.AuthResponseDto;
import com.example.weathergpt.domain.dto.LoginUserDto;
import com.example.weathergpt.domain.dto.UserRequestDto;
import com.example.weathergpt.domain.dto.UserResponseDto;
import com.example.weathergpt.domain.entity.RoleEnum;
import com.example.weathergpt.domain.entity.User;
import com.example.weathergpt.domain.entity.Chat;
import com.example.weathergpt.exception.EmailAlreadyExistsException;
import com.example.weathergpt.exception.InvalidCredentialsException;
import com.example.weathergpt.exception.UserNotFoundException;
import com.example.weathergpt.mapper.WeatherMapper;
import com.example.weathergpt.repository.UserRepository;
import com.example.weathergpt.service.JwtService;
import com.example.weathergpt.service.UserService;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final WeatherMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public UserResponseDto createUser(
            UserRequestDto request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(
                    "Email already registered"
            );
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .homeLocation(request.homeLocation())
                .workLocation(request.workLocation())
                .role(request.role())
                .password(
                        passwordEncoder.encode(
                                request.password()
                        )
                )
                .createdAt(LocalDateTime.now())
                .build();

        Chat chat = Chat.builder().user(user).build();
        user.setChat(chat);
        userRepository.save(user);

        return mapper.toUserResponseDto(user);
    }

    @Override
    public void updatePassword(UUID userId, String newPassword) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        userRepository.delete(user);
    }

    @Override
    public AuthResponseDto authenticateUser(
            LoginUserDto loginUserDto) {

        User user = userRepository
                .findByEmail(loginUserDto.email())
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid email or password"
                        )
                );

        if (user.getPassword() == null ||
                !passwordEncoder.matches(
                        loginUserDto.password(),
                        user.getPassword()
                )) {

            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        String token =
                jwtService.generateToken(user);

        return new AuthResponseDto(
                token,
                mapper.toUserResponseDto(user)
        );
    }

    @Override
    public UserResponseDto getUserProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        return mapper.toUserResponseDto(user);
    }
}

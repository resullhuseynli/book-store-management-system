package com.store.book.controller;

import com.store.book.dao.dto.AuthDtoRequest;
import com.store.book.dao.dto.AuthDtoResponse;
import com.store.book.dao.dto.UserDtoUpdate;
import com.store.book.dao.entity.UserEntity;
import com.store.book.security.CustomUserDetailsService;
import com.store.book.security.JwtService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Validated
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthDtoResponse> login(@Valid @RequestBody AuthDtoRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
        UserEntity user = userDetailsService.loadUserByUsername(request.getUsername());
        String jwt = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthDtoResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody AuthDtoRequest request) {
        customUserDetailsService.registerNewUser(request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update")
    public ResponseEntity<Void> update(
            @Valid @RequestBody UserDtoUpdate update,

            @Parameter(
                    description = "Language preference (e.g., en, az)",
                    name = "Accept-Language",
                    in = ParameterIn.HEADER,
                    example = "az"
            )
            @RequestHeader(name = "Accept-Language", required = false, defaultValue = "en") String language
    ) {
        Locale locale = Locale.forLanguageTag(language);
        customUserDetailsService.updateUser(update);
        return ResponseEntity.noContent().build();
    }

}

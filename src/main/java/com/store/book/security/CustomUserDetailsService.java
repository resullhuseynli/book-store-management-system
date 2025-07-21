package com.store.book.security;

import com.store.book.dao.CartRepository;
import com.store.book.dao.UserEntityRepository;
import com.store.book.dao.dto.AuthDtoRequest;
import com.store.book.dao.dto.UserDtoUpdate;
import com.store.book.dao.entity.Cart;
import com.store.book.dao.entity.UserEntity;
import com.store.book.enums.Role;
import com.store.book.enums.Status;
import com.store.book.exception.exceptions.UserAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserEntityRepository userRepository;
    private final CartRepository cartRepository;

    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void registerNewUser(AuthDtoRequest request) {
        if (userRepository.findByUserName(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistException("User exists");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(passwordEncoder().encode(request.getPassword()));
        userEntity.setUserName(request.getUsername());
        userEntity.setRole(Role.USER);
        userEntity.setMoney(BigDecimal.ZERO);
        userRepository.save(userEntity);
        Cart cart = new Cart();
        cart.setUser(userEntity);
        cart.setStatus(Status.ACTIVE);
        cartRepository.save(cart);
    }

    public void updateUser(UserDtoUpdate update) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = loadUserByUsername(username);
        userEntity.setFirstName(update.getFirstName());
        userEntity.setLastName(update.getLastName());
        userEntity.setEmail(update.getEmail());
        userRepository.save(userEntity);
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


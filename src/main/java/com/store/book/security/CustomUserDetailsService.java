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
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserEntityRepository userRepository;
    private final CartRepository cartRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MessageSource messageSource;

    private final ConcurrentMap<String, CompletableFuture<String>> responseMap = new ConcurrentHashMap<>();

    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        final Locale locale = LocaleContextHolder.getLocale();
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        messageSource.getMessage("NotFoundErrorMessage", null, locale)));
    }

    public void registerNewUser(AuthDtoRequest request) {
        final Locale locale = LocaleContextHolder.getLocale();
        if (userRepository.findByUserName(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistException(
                    messageSource.getMessage("UserAlreadyExistsErrorMessage", null, locale));
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

    public String sendPaymentRequest(Long userId, BigDecimal amount) throws Exception {
        final Locale locale = LocaleContextHolder.getLocale();
        String requestId = UUID.randomUUID().toString();
        String message = userId + ":" + requestId + ":" + amount;

        CompletableFuture<String> future = new CompletableFuture<>();
        responseMap.put(requestId, future);

        kafkaTemplate.send("payment-request", message);

        try {
            String response = future.get(10, TimeUnit.SECONDS);
            String[] parts = response.split(":");
            if (parts[1].equals("SUCCESS")) {
                increaseMoney(amount);
                return "Payment Success";
            } else {
                return "Payment Failed";
            }
        } catch (TimeoutException timeoutException) {
            throw new TimeoutException(
                    messageSource.getMessage("TimeOutMessage", null, locale));
        } finally {
            responseMap.remove(requestId);
        }
    }

    @KafkaListener(topics = "payment-response", groupId = "book-service")
    public void handleResponse(String message) {
        String[] parts = message.split(":");
        String userId = parts[0];
        String requestId = parts[1];
        String status = parts[2];

        CompletableFuture<String> future = responseMap.get(requestId);
        if (future != null) {
            future.complete(userId + ":" + status);
        }
    }

    private void increaseMoney(BigDecimal amount) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = loadUserByUsername(username);
        userEntity.setMoney(userEntity.getMoney().add(amount));
        userRepository.save(userEntity);
    }
}


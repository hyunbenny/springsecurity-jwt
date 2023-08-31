package com.hyunbenny.springsecurityjwt.service;

import com.hyunbenny.springsecurityjwt.controller.request.JoinRequest;
import com.hyunbenny.springsecurityjwt.domain.UserAccount;
import com.hyunbenny.springsecurityjwt.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccount join(JoinRequest request) {
        UserAccount userEntity = UserAccount.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles("ROLE_USER")
                .build();

        return userAccountRepository.save(userEntity);
    }

    public UserAccount findUser(String username) {
        return userAccountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다."));
    }
}

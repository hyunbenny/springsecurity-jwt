package com.hyunbenny.springsecurityjwt.auth;

import com.hyunbenny.springsecurityjwt.domain.UserAccount;
import com.hyunbenny.springsecurityjwt.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * SecurityConfig에서 formLogin().disable()했기 때문에 예전에 했던 것처럼
 * /login 요청이 자동으로 동작하지 않는다.
 *
 * 직접 필터에 PrincipalDetailsService를 넣어서 동작하게 해줘야 한다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserAccount userAccountEntity = userAccountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다. username: " + username));

        return new PrincipalDetails(userAccountEntity);
    }
}

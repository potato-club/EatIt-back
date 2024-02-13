package com.gamza.ItEat.service.jwt;


import com.gamza.ItEat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return new UserDetailsImpl(userRepository.findByEmail(userName)
                .orElseThrow(() -> new UsernameNotFoundException("회원이 존재하지 않습니다.")));
    }
}

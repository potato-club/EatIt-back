package com.gamza.ItEat.service.jwt;


import com.gamza.ItEat.entity.UserEntity;
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
        UserEntity user = userRepository.findByEmail(userName);

        if (user == null) {
            throw new UsernameNotFoundException(userName + "는 존재하지 않는 사용자입니다.");
        }

//        return new UserDetailsImpl(userRepository.findByEmail(userName));

        return new UserDetailsImpl(user);
    }
}

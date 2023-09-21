package com.bloom.pium.service.impl;

import com.bloom.pium.data.entity.UserInfo;
import com.bloom.pium.data.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user = userInfoRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + ":사용자를 찾을 수 없습니다 " );
        }

        // 사용자 정보를 기반으로 Spring Security의 User 객체를 생성하여 반환 => loadUserByUsername() 메서드 구현 가능
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles("ROLE_USER")  // 사용자의 역할을 여기에 추가
                .build();

    }
}

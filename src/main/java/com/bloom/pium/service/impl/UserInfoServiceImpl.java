package com.bloom.pium.service.impl;

import com.bloom.pium.config.security.JwtTokenProvider;
import com.bloom.pium.config.security.SecurityConfig;
import com.bloom.pium.data.dto.UserInfoDto;
import com.bloom.pium.data.dto.UserinfoResponseDto;
import com.bloom.pium.data.entity.UserInfo;
import com.bloom.pium.data.repository.UserInfoRepository;
import com.bloom.pium.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.bloom.pium.data.UserRoleEnum.ROLE_USER;


@Service
public class UserInfoServiceImpl  implements UserInfoService {

    private UserInfoRepository userInfoRepository;
    private JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public  UserInfoServiceImpl(UserInfoRepository userInfoRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder){
        this.userInfoRepository = userInfoRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isUsernameUnique(String username) {
        UserInfo existingUser = userInfoRepository.findByUsername(username);
        return existingUser == null;
    }

    @Override
    public UserInfoDto join(UserInfoDto userInfoDto) {
        UserInfo user = new UserInfo();
        user.setUsername(userInfoDto.getUsername());
        user.setPassword(passwordEncoder.encode(userInfoDto.getPassword())); // passwordEncoder
        user.setName(userInfoDto.getName());
        user.setPhone(userInfoDto.getPhone());
        user.setGender(userInfoDto.getGender());
        user.setStatus("일반");
        user.setRoles(userInfoDto.getRoles());
        user.setRoles(Collections.singletonList(ROLE_USER)); // 리스트로 해서 사용자0, 관리자1인 리스트 인덱스 값 저장

        userInfoRepository.save(user);

        return userInfoDto;
    }

    @Override
    public UserinfoResponseDto findUsername(String username) {
        UserInfo findUser = userInfoRepository.findByUsername(username);
        UserinfoResponseDto user = new UserinfoResponseDto();
        user.setUsername(findUser.getUsername());
        user.setPassword(findUser.getPassword());
        user.setRoles(findUser.getRoles());
        user.setToken(jwtTokenProvider.createToken(user.getUsername(), user.getRoles()));
        return user;


    }

}

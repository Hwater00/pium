package com.bloom.pium.data.repository;

import com.bloom.pium.data.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {

    UserInfo findByUsername(String username);
}

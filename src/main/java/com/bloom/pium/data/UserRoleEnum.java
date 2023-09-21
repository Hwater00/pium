package com.bloom.pium.data;

import lombok.Getter;

@Getter
public enum UserRoleEnum {
    ROLE_USER("일반사용자"), ROLE_ADMIN("관리자");

    private final String roleName;

    UserRoleEnum(String roleName) {
        this.roleName = roleName;
    }

}

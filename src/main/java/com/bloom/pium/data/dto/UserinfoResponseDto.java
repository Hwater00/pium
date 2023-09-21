package com.bloom.pium.data.dto;

import com.bloom.pium.data.UserRoleEnum;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserinfoResponseDto {
    private String username;
    private String password;
    private String name;
    private String phone;
    private String gender;
    private List<UserRoleEnum> roles = new ArrayList<>();

    private String token;

}

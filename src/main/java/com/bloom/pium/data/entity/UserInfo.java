package com.bloom.pium.data.entity;


import com.bloom.pium.data.UserRoleEnum;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString(callSuper = true)
@Entity
@Table(name = "userinfo")
public class UserInfo implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @Pattern(regexp = "01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4}$)")
    private String phone;
    @Column(nullable = false)
    private String gender;
    @Column(nullable = false)
    private String status = "일반";

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private List<UserRoleEnum> roles = new ArrayList<>(); // 권한


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName()))// UserRoleEnum 값을 문자열로 변환하여 사용.collect(Collectors.toList());
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


//    // 발신한 메시지 목록
//    @OneToMany(mappedBy = "sender")
//    private List<Message> sentMessages;
//
//    // 수신한 메시지 목록
//    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
//    private List<Message> receivedMessages;

}



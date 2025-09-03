package com.bytearchive.dto;


import lombok.*;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements UserDetails{

    private String id;
    private String email;
    private String password;
    private String name;
    private String nickname;

    @ToString.Exclude
    private FileDTO image;

    @Override
    public String toString() {
        return "UserDTO(id=" + id +
                ", email=" + email +
                ", name=" + name +
                ", nickname=" + nickname + ")";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public String getUsername() {
        return this.id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}

package org.example.project_base_test.security.principle;


import lombok.*;
import org.example.project_base_test.model.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserPrinciple implements UserDetails {
    private User user ;
    private Collection<? extends GrantedAuthority> authorities ;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

}

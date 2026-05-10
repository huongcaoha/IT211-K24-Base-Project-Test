package org.example.project_base_test.security.principle;

import org.example.project_base_test.model.entity.Permission;
import org.example.project_base_test.model.entity.Role;
import org.example.project_base_test.model.entity.User;
import org.example.project_base_test.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    private final UserRepository userRepository;

    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Cacheable(cacheNames = "users",key = "#email")
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " not found!");
        }
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
            if (role.getPermissions() != null) {
                for (Permission permission : role.getPermissions()) {
                    grantedAuthorities.add(new SimpleGrantedAuthority(permission.getPermissionName()));
                }
            }
        }
        return UserPrinciple
                .builder()
                .user(user)
                .authorities(grantedAuthorities)
                .build();
    }
}

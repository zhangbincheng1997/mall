package cn.jnu.security.jwt;

import cn.jnu.mbg.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class JwtUserDetails implements UserDetails {

    private User user;

    private List<String> roleList;

    private List<String> permissionList;

    public JwtUserDetails(User user, List<String> roleList, List<String> permissionList) {
        this.user = user;
        this.roleList = roleList;
        this.permissionList = permissionList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> collection = new ArrayList<>();
        if (roleList != null) {
            collection.addAll(roleList);
        }
        if (permissionList != null) {
            collection.addAll(permissionList);
        }
        return collection.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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
}

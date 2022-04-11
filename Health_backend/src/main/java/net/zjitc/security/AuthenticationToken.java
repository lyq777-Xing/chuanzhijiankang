package net.zjitc.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class AuthenticationToken {
    private String token;
    private Collection<? extends GrantedAuthority> authorities;
}

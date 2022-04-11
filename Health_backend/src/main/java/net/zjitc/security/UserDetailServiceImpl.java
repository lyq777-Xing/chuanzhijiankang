package net.zjitc.security;

import net.zjitc.entity.User;
import net.zjitc.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("用户名或密码不正确");
        }

        return new AccounUser(user.getId(), user.getUsername(), user.getPassword(),getUserAuthority(user.getId()));
    }

    /**
     * 获取用户权限信息（角色，菜单权限）
     * @param userId
     * @return
     */
    public List<GrantedAuthority> getUserAuthority(Integer userId){
        String authority = userService.getAuthorityInfo(userId);

        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}

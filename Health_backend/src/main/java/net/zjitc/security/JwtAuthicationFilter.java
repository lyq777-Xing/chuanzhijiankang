package net.zjitc.security;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import net.zjitc.entity.User;
import net.zjitc.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwt拦截器
 */
public class JwtAuthicationFilter extends BasicAuthenticationFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Reference
    @org.springframework.data.annotation.Reference
    private UserService userService;

    public JwtAuthicationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(jwtUtils.getHeader());
        if(StrUtil.isBlankOrUndefined(header)){
            chain.doFilter(request,response);
            return;
        }

        Claims claimByToken = jwtUtils.getClaimByToken(header);
        if(claimByToken == null){
            throw new JwtException("token 异常");
        }

        if(jwtUtils.isTokenExpired(claimByToken)){
            throw new JwtException("token已过期");
        }

        String username = claimByToken.getSubject();

//        获取userid
        User byUsername = userService.findByUsername(username);
        int id = byUsername.getId();

//        获取用户的权限信息
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, userDetailService.getUserAuthority(id));
        SecurityContextHolder.getContext().setAuthentication(token);

        chain.doFilter(request,response);
    }
}

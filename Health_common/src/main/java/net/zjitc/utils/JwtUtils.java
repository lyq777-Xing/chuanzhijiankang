package net.zjitc.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
@ConfigurationProperties(prefix = "markerhub.jwt")
public class JwtUtils {

    private long exprie;

    private String secret;

    private String header;

    /**
     * 生成jwt
     */
    public String generateToken(String username){
        Date date = new Date();
        Date date1 = new Date(date.getTime() + 1000 + exprie);

        return  Jwts.builder()
                    .setHeaderParam("typ","JWT")
    //                将username放入
                    .setSubject(username)
    //                创建时间
                    .setIssuedAt(date)
    //                过期时间(7days)
                    .setExpiration(date1)
    //                密钥
                    .signWith(SignatureAlgorithm.HS512,secret)
                    .compact();
    }

    /**
     * 解析jwt
     */
    public Claims getClaimByToken(String jwt){
//        密钥可能被纂改 这样会产生异常 需要捕捉 否则无法进行下面的过滤器
        try{
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt)
                    .getBody();
        }catch (Exception e){
            return null;
        }
    }


    /**
     * 判断jwt是否过期的方法
     */
    public boolean isTokenExpired(Claims claims){
//        判断过期时间是否在当前时间之后
        return claims.getExpiration().before(new Date());
    }
}

package net.zjitc.controller;

import com.alibaba.fastjson.JSON;
import net.zjitc.common.MessageConstant;
import net.zjitc.common.Result;
import net.zjitc.constant.RedisMessageConstant;
import net.zjitc.entity.LoginByPhone;
import net.zjitc.entity.Member;
import net.zjitc.service.MemberService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import java.util.Date;

@CrossOrigin
@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    /**
     * 通过手机验证码登录
     * @param loginByPhone
     * @return
     */
    @PostMapping("/login")
    public Result Login(@RequestBody LoginByPhone loginByPhone){
        try{
            String phoneNumber = loginByPhone.getPhoneNumber();
            String cat = loginByPhone.getCode();
//          从redis中获取缓存验证码
            String code = jedisPool.getResource().get(phoneNumber + RedisMessageConstant.SENDTYPE_LOGIN);
            Member member1 = new Member();
            if(cat.equals(code) && code != null && cat != null){
                //验证码输入正确
                //通过电话判断当前用户是否为会员
                Member serviceByPhone = memberService.findByPhone(phoneNumber);
                if(serviceByPhone == null){
//                表明该用户目前不是会员 需要注册成为会员
                    Member member = new Member();
                    member.setPhoneNumber(phoneNumber);
                    member.setRegTime(new Date());
                    member1 = memberService.addMember(member);
                }
            }else {
//            表明验证码输入错误
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            //登录成功
            //写入Cookie，跟踪用户
            Cookie cookie = new Cookie("login_member_telephone",phoneNumber);
            //路径
            cookie.setPath("/");
            //保存会员信息到Redis中
            String json = JSON.toJSON(member1).toString();
            jedisPool.getResource().setex(phoneNumber,60*30,json);
            return new Result(true,MessageConstant.LOGIN_SUCCESS,member1);

        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"登录失败");
        }

    }
}

package net.zjitc.controller;

import net.zjitc.common.MessageConstant;
import net.zjitc.common.Result;
import net.zjitc.constant.RedisMessageConstant;
import net.zjitc.utils.SendSmsUtils;
import net.zjitc.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@CrossOrigin
@RestController
@RequestMapping("/validateCode")
public class CalidateCodeController{

    @Autowired
    private JedisPool jedisPool;

    /**
     * 用户在线体检预约发送验证码
     * @param phone
     * @return
     */
    @GetMapping("/sendOderCode")
    public Result SendOderCode(String phone){
        Integer validateCode = 0;
        try {
//          随机生成4位数字验证码
            validateCode = ValidateCodeUtils.generateValidateCode(4);
//          给用户发送验证码
            String string = validateCode.toString();
            String[] code = {string};
            String[] phones = {phone};
            SendSmsUtils.sendShortMessage("1354314",phones,code);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
//        将验证码保存到redis
        jedisPool.getResource().setex(phone+ RedisMessageConstant.SENDTYPE_ORDER,300, String.valueOf(validateCode));
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    /**
     * 获取登录验证码
     * @param phone
     * @return
     */
    @GetMapping("/sendLoginCode")
    public Result SendLoginCode(String phone){
        Integer validateCode = 0;
        try {
//          随机生成4位数字验证码
            validateCode = ValidateCodeUtils.generateValidateCode(4);
//          给用户发送验证码
            String string = validateCode.toString();
            String[] code = {string};
            String[] phones = {phone};
            SendSmsUtils.sendShortMessage("1353292",phones,code);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的验证码为：" + validateCode);
//        将验证码保存到redis
        jedisPool.getResource().setex(phone+ RedisMessageConstant.SENDTYPE_LOGIN,300, String.valueOf(validateCode));
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);

    }
}

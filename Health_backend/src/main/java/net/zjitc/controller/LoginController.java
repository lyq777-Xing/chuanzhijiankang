package net.zjitc.controller;

import net.zjitc.common.Result;
import net.zjitc.entity.User;
import net.zjitc.service.UserService;
import org.springframework.data.annotation.Reference;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class LoginController {

    @Reference
    private UserService userService;

    @PostMapping("/login")
    public Result findByUsername(String username, String password){
        User users = new User();
        users.setUsername(username);
        users.setPassword(password);
        User byUsername = userService.findByUsername(username);
        if(byUsername!=null){
            return new Result(true,"登录成功",byUsername);
        }else {
            return new Result(false,"登录失败");

        }
    }
}

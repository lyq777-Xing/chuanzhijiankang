package net.zjitc.controller;

import net.zjitc.common.ResponseResult;
import net.zjitc.entity.Users;
import net.zjitc.service.Userservice;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin
@RestController
public class UserController {
//    @Reference
//    private static Userservice userservice;
//
//    @GetMapping("/user")
//    public ResponseResult findAll(){
//        Users all = userservice.findAll();
//        ResponseResult<Object> result = new ResponseResult<>();
//        result.Success("aaa",all);
//        return result;
//    }
}

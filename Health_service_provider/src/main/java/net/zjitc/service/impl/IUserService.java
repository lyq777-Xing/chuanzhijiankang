package net.zjitc.service.impl;

import net.zjitc.entity.Users;
import net.zjitc.mapper.UserMapper;
import net.zjitc.service.Userservice;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class IUserService implements Userservice {

    @Autowired
    private UserMapper userMapper;

    @Override
    public String test(String name) {
        return "hello"+name;
    }

    @Override
    public Users findAll() {
        Users users = userMapper.selectById(500);
        return users;
    }
}

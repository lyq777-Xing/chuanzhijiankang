package net.zjitc.service.impl;

import net.zjitc.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.Reference;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = {IUserServiceTest.class})
@ContextConfiguration
@RunWith(SpringRunner.class)
class IUserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void getAuthorityInfo() {
        String authorityInfo = userService.getAuthorityInfo(1);
        System.out.println(authorityInfo);
    }
}

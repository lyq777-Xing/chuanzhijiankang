package net.zjitc.utils;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SendSmsUtilsTest.class})
public  class SendSmsUtilsTest {
    @Test
    public void test1(){
        String[] strings = new String[]{"18957765450"};
        String[] strings1 = new String[]{"1234"};
        SendSmsUtils.sendShortMessage("1353292",strings,strings1);
    }
}

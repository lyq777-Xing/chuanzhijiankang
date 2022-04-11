import net.zjitc.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.Reference;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {IUserServiceTest.class})
@ContextConfiguration
@RunWith(SpringRunner.class)
public class IUserServiceTest {

    @Reference
    private UserService userService;

    @Test
    public void getAuthorityInfo() {
        String authorityInfo = userService.getAuthorityInfo(1);
        System.out.println(authorityInfo);
    }
}

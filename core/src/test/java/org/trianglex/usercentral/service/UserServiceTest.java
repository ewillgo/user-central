package org.trianglex.usercentral.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.trianglex.common.util.ToolUtils;
import org.trianglex.usercentral.CoreApplication;
import org.trianglex.usercentral.api.domain.User;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    private UserService userService;

    @Test
    public void getUserById() {
        String userId = ToolUtils.getUUID();
        User user = userService.getUserByUserId(userId, "*");
        logger.info(user.toString());
    }

    @Test
    public void getUserByUsername() {
        String username = "test@gmail.com";
        User user = userService.getUserByUsername(username, "*");
        logger.info(user.toString());
    }
}

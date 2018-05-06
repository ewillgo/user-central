package org.trianglex.usercentral.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.trianglex.common.controller.AbstractMockController;
import org.trianglex.common.util.JavaBeanUtils;
import org.trianglex.usercentral.CoreApplication;
import org.trianglex.usercentral.dto.LoginRequest;
import org.trianglex.usercentral.dto.RegisterRequest;

import static org.trianglex.usercentral.constant.UrlConstant.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserCentralControllerTest extends AbstractMockController<UserCentralController> {

    private static final Logger logger = LoggerFactory.getLogger(UserCentralControllerTest.class);

    @Test
    public void addUserTest() throws Exception {
        RegisterRequest userDTO = new RegisterRequest();
        userDTO.setUsername("test@gmail.com");
        userDTO.setPassword(DigestUtils.md5Hex("123456"));
        userDTO.setNickname("窗外的麻雀，在电线杆上多嘴。");
        userDTO.setGender(0);

        MvcResult mvcResult = mockPost(C_API_USER + M_API_USER_GET_REGISTER, JavaBeanUtils.beanToMap(userDTO));
        logger.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void loginTest() throws Exception {
        LoginRequest loginDTO = new LoginRequest();
        loginDTO.setUsername("test@gmail.com");
        loginDTO.setPassword(DigestUtils.md5Hex("123456"));

        MvcResult mvcResult = mockPost(C_API_USER + M_API_USER_GET_LOGIN, JavaBeanUtils.beanToMap(loginDTO));
        logger.info(mvcResult.getResponse().getContentAsString());
    }
}

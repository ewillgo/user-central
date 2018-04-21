package org.trianglex.usercentral.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.trianglex.common.dto.Result;
import org.trianglex.common.support.ConstPair;
import org.trianglex.common.util.PasswordUtils;
import org.trianglex.common.util.RegexUtils;
import org.trianglex.usercentral.domain.User;
import org.trianglex.usercentral.dto.LoginDTO;
import org.trianglex.usercentral.dto.RegisterDTO;
import org.trianglex.usercentral.service.UserService;
import org.trianglex.usercentral.session.SessionUser;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static org.trianglex.usercentral.constant.UrlConstant.*;
import static org.trianglex.usercentral.constant.UserConstant.*;

@RestController
@RequestMapping(C_USER)
public class UserCentralController {

    @Autowired
    private UserService userService;

    @PostMapping(M_USER_POST_REGISTER)
    public Result<Integer> register(@Valid @ModelAttribute("userDTO") RegisterDTO registerDTO, HttpSession session) {
        Result<Integer> result = new Result<>();

        User user = registerDTO.toPO(new User());
        user.setSalt(PasswordUtils.salt256());
        user.setPassword(PasswordUtils.password(user.getPassword(), user.getSalt()));

        if (RegexUtils.isMatch(user.getUsername(), RegexUtils.EMAIL)) {
            user.setEmail(user.getUsername());
        }

        boolean ret = userService.addUser(user);
        ConstPair constPair = ret ? USER_REGISTER_SUCCESS : USER_REGISTER_FAIL;

        if (ret) {
            refreshSession(user, session);
        }

        result.setData(user.getId());
        result.setStatus(constPair.getStatus());
        result.setMessage(constPair.getMessage());
        return result;
    }

    @PostMapping(M_USER_POST_LOGIN)
    public Result login(@Valid @ModelAttribute("loginDTO") LoginDTO loginDTO, HttpSession session) {
        Result result = new Result();

        User user = userService.getUserByUsername(loginDTO.getUsername(), "salt");
        if (user == null) {
            result.setStatus(USER_NOT_EXISTS.getStatus());
            result.setMessage(USER_NOT_EXISTS.getMessage());
            return result;
        }

        user = userService.getUserByUsernameAndPassword(
                loginDTO.getUsername(), PasswordUtils.password(loginDTO.getPassword(), user.getSalt()), "*");

        if (user == null) {
            result.setStatus(USER_NOT_EXISTS.getStatus());
            result.setMessage(USER_NOT_EXISTS.getMessage());
            return result;
        }

        refreshSession(user, session);

        result.setStatus(USER_LOGIN_SUCCESS.getStatus());
        result.setMessage(USER_LOGIN_SUCCESS.getMessage());
        return result;
    }

    @GetMapping(M_USER_GET_LOGOUT)
    public Result logout(HttpSession session) {
        session.invalidate();
        return null;
    }

    private void refreshSession(User user, HttpSession session) {
        SessionUser sessionUser = new SessionUser();
        BeanUtils.copyProperties(user, sessionUser);
        session.setAttribute(SESSION_KEY, sessionUser);
    }
}

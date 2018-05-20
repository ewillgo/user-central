package org.trianglex.usercentral.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trianglex.common.dto.Result;
import org.trianglex.usercentral.client.domain.User;
import org.trianglex.usercentral.client.dto.UpdateUserRequest;
import org.trianglex.usercentral.client.dto.UserPaginateRequest;
import org.trianglex.usercentral.client.dto.UserRequest;
import org.trianglex.usercentral.service.UserService;

import javax.validation.Valid;

import static org.trianglex.usercentral.client.constant.UrlConstant.*;

@RestController
@RequestMapping(C_USER)
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(C_USER_POST_FETCH_USER_BY_PAGINAGE)
    public Result<User> getUserByPaginate(@Valid @ModelAttribute UserPaginateRequest userPaginateRequest) {
        return new Result<>();
    }

    @PostMapping(C_USER_POST_FETCH_USER_BY_USER_ID)
    public Result<User> getUserByUserId(@Valid @ModelAttribute UserRequest userRequest) {
        return new Result<>();
    }

    @PostMapping(C_USER_POST_UPDATE_USER_BY_USER_ID)
    public Result updateUserByUserId(@Valid @ModelAttribute UpdateUserRequest updateUserRequest) {
        return new Result();
    }
}

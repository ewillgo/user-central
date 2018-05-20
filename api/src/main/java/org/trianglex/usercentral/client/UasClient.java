package org.trianglex.usercentral.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.trianglex.common.dto.Result;
import org.trianglex.usercentral.client.dto.*;

import javax.validation.Valid;

import static org.trianglex.usercentral.client.constant.ClientConstant.SERVICE_NAME;
import static org.trianglex.usercentral.client.constant.UrlConstant.*;

@FeignClient(SERVICE_NAME)
public interface UasClient {

    /**
     * 用户注册
     */
    @ResponseBody
    @PostMapping(M_API_USER_POST_REGISTER)
    Result<UasRegisterResponse> register(@Valid @RequestBody UasRegisterRequest uasRegisterRequest);

    /**
     * 用户登录
     */
    @ResponseBody
    @PostMapping(value = M_API_USER_POST_LOGIN)
    Result<UasLoginResponse> login(@Valid @RequestBody UasLoginRequest uasLoginRequest);

    /**
     * 用户登出
     */
    @ResponseBody
    @PostMapping(M_API_USER_POST_LOGOUT)
    Result logout(@Valid @RequestBody UasLogoutRequest uasLogoutRequest);

    /**
     * 获取远程会话信息
     */
    @ResponseBody
    @PostMapping(M_API_USER_POST_GET_REMOTE_SESSION)
    Result<RemoteSessionResponse> getRemoteSession(@Valid @RequestBody RemoteSessionRequest remoteSessionRequest);
}

package org.trianglex.usercentral.constant;

/**
 * 控制器映射格式：
 * C_模块名称
 *
 * 执行方法映射格式：
 * M_模块名称_请求方式_方法名
 */
public interface UrlConstant {

    String C_USER = "/api/user";
    String M_USER_POST_REGISTER = "/register";
    String M_USER_POST_LOGIN = "/login";
    String M_USER_GET_LOGOUT = "/logout";
    String M_USER_GET_VALIDATE_TICKET = "/validateTicket";
    String M_USER_POST_REFRESH = "/refreshSession";

}

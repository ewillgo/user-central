package org.trianglex.usercentral.api.constant;

/**
 * 控制器映射格式：
 * C_模块名称
 *
 * 执行方法映射格式：
 * M_模块名称_请求方式_方法名
 */
public interface UrlConstant {

    String C_API_USER = "/api/user";
    String M_API_USER_POST_REGISTER = C_API_USER + "/register";
    String M_API_USER_POST_LOGIN = C_API_USER + "/login";
    String M_API_USER_POST_LOGOUT = C_API_USER + "/logout";
    String M_API_USER_POST_GET_REMOTE_SESSION = C_API_USER + "/getRemoteSession";

    String C_USER = "/user";
    String C_USER_POST_FETCH_USER_BY_PAGINAGE = "/getUserByPaginate";
    String C_USER_POST_FETCH_USER_BY_USER_ID = "/getUserByUserId";
    String C_USER_POST_UPDATE_USER_BY_USER_ID = "/updateUserByUserId";

}

package org.trianglex.usercentral.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trianglex.common.database.DataSource;
import org.trianglex.usercentral.api.domain.User;
import org.trianglex.usercentral.dao.UserDAO;

import static org.trianglex.usercentral.constant.DataSourceNames.USERCENTRAL;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    /**
     * 添加用户
     *
     * @param user 用户实体
     * @return
     */
    @DataSource(USERCENTRAL)
    public boolean addUser(User user) {
        return userDAO.addUser(user) > 0;
    }

    /**
     * 根据用户名和密码获取用户信息
     *
     * @param username 账号
     * @param password 密码
     * @param fields   查询字段
     * @return
     */
    @DataSource(USERCENTRAL)
    public User getUserByUsernameAndPassword(String username, String password, String fields) {
        return userDAO.getUserByUsernameAndPassword(username, password, fields);
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId 用户ID
     * @param fields 查询字段
     * @return
     */
    @DataSource(USERCENTRAL)
    public User getUserByUserId(String userId, String fields) {
        return userDAO.getUserByUserId(userId, fields);
    }

    /**
     * 根据用户名获取用户数据
     *
     * @param username 账号
     * @param fields   查询字段
     * @return
     */
    @DataSource(USERCENTRAL)
    public User getUserByUsername(String username, String fields) {
        return userDAO.getUserByUsername(username, fields);
    }

    /**
     * 判断账号是否已存在
     *
     * @param username 账号
     * @return
     */
    @DataSource(USERCENTRAL)
    public boolean isUsernameExists(String username) {
        return userDAO.isUserExists("username = '" + username + "'") > 0;
    }

    /**
     * 判断昵称是否已存在
     *
     * @param nickname 昵称
     * @return
     */
    @DataSource(USERCENTRAL)
    public boolean isNicknameExists(String nickname) {
        return userDAO.isUserExists("nickname = '" + nickname + "'") > 0;
    }

    /**
     * 判断手机号码是否已存在
     *
     * @param phone 联系电话
     * @return
     */
    @DataSource(USERCENTRAL)
    public boolean isPhoneExists(String phone) {
        return userDAO.isUserExists("phone = '" + phone + "'") > 0;
    }
}

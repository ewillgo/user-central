package org.trianglex.usercentral.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trianglex.common.database.DataSource;
import org.trianglex.usercentral.client.domain.UserPrivilege;
import org.trianglex.usercentral.dao.UserPrivilegeDAO;

import java.util.List;

import static org.trianglex.usercentral.constant.DataSourceNames.USERCENTRAL;

@Service
public class UserPrivilegeService {

    @Autowired
    private UserPrivilegeDAO userPrivilegeDAO;

    /**
     * 获取账号权限信息
     *
     * @param userId 用户ID
     * @return
     */
    @DataSource(USERCENTRAL)
    public List<UserPrivilege> getUserPrivileges(String userId) {
        return userPrivilegeDAO.getUserPrivileges(userId);
    }
}

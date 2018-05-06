package org.trianglex.usercentral.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trianglex.common.database.DataSource;
import org.trianglex.usercentral.dao.ApplicationDAO;
import org.trianglex.usercentral.domain.Application;

import static org.trianglex.usercentral.constant.DataSourceNames.USERCENTRAL;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationDAO applicationDAO;

    /**
     * 根据账号获取接入应用程序信息
     *
     * @param appKey 账号
     * @param fields 需要查询的字段
     * @return
     */
    @DataSource(USERCENTRAL)
    public Application getApplicationByAppKey(String appKey, String fields) {
        return applicationDAO.getApplicationByAppKey(appKey, fields);
    }
}

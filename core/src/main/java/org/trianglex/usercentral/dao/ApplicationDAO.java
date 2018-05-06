package org.trianglex.usercentral.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.trianglex.usercentral.domain.Application;

@Mapper
public interface ApplicationDAO {

    Application getApplicationByAppKey(@Param("appKey") String appKey, @Param("fields") String fields);
}

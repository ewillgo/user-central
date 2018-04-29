package org.trianglex.usercentral.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.trianglex.usercentral.domain.UserPrivilege;

import java.util.List;

@Mapper
public interface UserPrivilegeDAO {

    List<UserPrivilege> getUserPrivileges(@Param("userId") String userId);
}

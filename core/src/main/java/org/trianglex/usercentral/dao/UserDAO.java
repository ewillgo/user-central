package org.trianglex.usercentral.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.trianglex.usercentral.domain.User;

@Mapper
public interface UserDAO {

    int addUser(User user);

    User getUserByUsernameAndPassword(@Param("username") String username,
                                      @Param("password") String password,
                                      @Param("fields") String fields);

    User getUserById(@Param("userId") int userId, @Param("fields") String fields);

    User getUserByUsername(@Param("username") String username, @Param("fields") String fields);

    int isUserExists(@Param("where") String where);
}

package org.trianglex.usercentral.api.core;

import org.trianglex.usercentral.api.domain.User;
import org.trianglex.usercentral.api.domain.UserPrivilege;

import java.io.Serializable;
import java.util.List;

public class UasSession implements Serializable {

    private static final long serialVersionUID = -1398320661646042419L;
    private User user;
    private List<UserPrivilege> userPrivilegeList;

    public UasSession() {

    }

    public UasSession(User user, List<UserPrivilege> userPrivilegeList) {
        this.user = user;
        this.userPrivilegeList = userPrivilegeList;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<UserPrivilege> getUserPrivilegeList() {
        return userPrivilegeList;
    }

    public void setUserPrivilegeList(List<UserPrivilege> userPrivilegeList) {
        this.userPrivilegeList = userPrivilegeList;
    }
}

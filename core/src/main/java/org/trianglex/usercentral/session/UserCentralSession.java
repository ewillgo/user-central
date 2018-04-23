package org.trianglex.usercentral.session;

import org.springframework.beans.BeanUtils;
import org.trianglex.usercentral.domain.Privilege;
import org.trianglex.usercentral.domain.User;

import java.io.Serializable;

public class UserCentralSession implements Serializable {

    private static final long serialVersionUID = -5893500963169820890L;
    private SessionUser user = new SessionUser();
    private SessionUserPrivilege privilege = new SessionUserPrivilege();
    private long timestamp;

    public UserCentralSession() {

    }

    public UserCentralSession(User user) {
        BeanUtils.copyProperties(user, this.user);
    }

    public UserCentralSession(User user, Privilege privilege) {
        BeanUtils.copyProperties(user, this.user);
        BeanUtils.copyProperties(privilege, this.privilege);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public SessionUser getUser() {
        return user;
    }

    public void setUser(SessionUser user) {
        this.user = user;
    }

    public SessionUserPrivilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(SessionUserPrivilege privilege) {
        this.privilege = privilege;
    }

}

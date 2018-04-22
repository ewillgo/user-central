package org.trianglex.usercentral.session;

import java.io.Serializable;

public class SessionUserPrivilege implements Serializable {

    private static final long serialVersionUID = -6027220940503667167L;

    private String privilegeId;

    public SessionUserPrivilege() {

    }

    public String getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(String privilegeId) {
        this.privilegeId = privilegeId;
    }
}

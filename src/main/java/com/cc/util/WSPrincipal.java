package com.cc.util;

import java.security.Principal;

/**
 * @author:xiasc
 * @Date:2018/11/1
 * @Time:18:04
 **/
public class WSPrincipal implements Principal {
    private String username;
    private String uuid;

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public String getName() {
        return username;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

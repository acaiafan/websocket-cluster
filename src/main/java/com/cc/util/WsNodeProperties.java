package com.cc.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author:xiasc
 * @Date:2018/11/9
 * @Time:15:14
 **/
@Component
@ConfigurationProperties(prefix = "ws.node")
@PropertySource("classpath:application.yml")
public class WsNodeProperties {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

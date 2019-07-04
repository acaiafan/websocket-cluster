package com.cc.util.websocket;

import com.cc.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import java.security.Principal;
import java.util.concurrent.TimeUnit;

/**
 * @author:xiasc
 * @Date:2018/11/12
 * @Time:16:26
 **/
@Component
@Slf4j
public class AuthWebSocketHandlerDecoratorFactory implements WebSocketHandlerDecoratorFactory {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${ws.node.name}")
    private String node;

    @Override
    public WebSocketHandler decorate(WebSocketHandler webSocketHandler) {
        return new WebSocketHandlerDecorator(webSocketHandler) {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                Principal principal = session.getPrincipal();
                if(principal != null){
                    String username = principal.getName();
                    if(StringUtils.isNotEmpty(username)){
                        stringRedisTemplate.opsForValue().set(Constants.WS_REDIS_USERNAME_PREFIX+username,
                                node);
                        //自动超时
                        stringRedisTemplate.expire(Constants.WS_REDIS_USERNAME_PREFIX+username,6,TimeUnit.HOURS);
                    }
                }
                super.afterConnectionEstablished(session);
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                Principal principal = session.getPrincipal();
                if(principal != null){
                    String username = principal.getName();
                    stringRedisTemplate.delete(Constants.WS_REDIS_USERNAME_PREFIX+username);
                }
                super.afterConnectionClosed(session, closeStatus);
            }
        };
    }


}

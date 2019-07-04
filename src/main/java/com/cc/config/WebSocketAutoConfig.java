package com.cc.config;

import com.alibaba.fastjson.JSONObject;
import com.cc.exception.WebsocketException;
import com.cc.util.Constants;
import com.cc.util.WSPrincipal;
import com.cc.util.websocket.AuthWebSocketHandlerDecoratorFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

/**
 * @author:xiasc
 * @Date:2018/10/25
 * @Time:14:15
 **/
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketAutoConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private AuthWebSocketHandlerDecoratorFactory authWebSocketHandlerDecoratorFactory;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("ws.node.name")
    private String node;

    @Value("${token.check}")
    private String tokenCheckUrl;

    Logger logger = LoggerFactory.getLogger(WebSocketAutoConfig.class);
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/bullet")         //开启/bullet端点
                .addInterceptors(new HandshakeInterceptor() {
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
                        logger.info("connecting to {}",node);
                        ServletServerHttpRequest req = (ServletServerHttpRequest)serverHttpRequest;
                        String token = req.getServletRequest().getParameter("access_token");
                        String username = parseToken(token);
                        if(isConnected(username)){
                            throw new WebsocketException(99,"已存在用户连接");
                        }
                        String uuid = UUID.randomUUID().toString();
                        if(StringUtils.isNotEmpty(username)){
                            Principal principal = new WSPrincipal();
                            ((WSPrincipal) principal).setUsername(username+"_"+uuid);
                            map.put("user",principal);
                            return true;

                        }
                        return false;
                    }

                    @Override
                    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

                    }
                })
                .setHandshakeHandler(new DefaultHandshakeHandler(){
                    @Override
                    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                        return (WSPrincipal)attributes.get("user");
                    }
                })
                .setAllowedOrigins("*","null")         //允许跨域访问
                .withSockJS();
        registry.addEndpoint("/bullet2").setAllowedOrigins("*","null").withSockJS();
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue","/toAll");
    }

    private String parseToken(String token){
        Principal principal = null;
//        String url = "http://localhost:8791/es/index";
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.add("Authorization","Bearer "+token);

        HttpEntity entity = new HttpEntity(null,headers);
        ResponseEntity<JSONObject> obj  = client.postForEntity(tokenCheckUrl,entity,JSONObject.class);
        if(obj.getStatusCode()== HttpStatus.OK){
            if(obj.getBody() != null){
             return obj.getBody().get("name").toString();
            }
        }
        return null;
    }


    private boolean isConnected(String username){
        if(StringUtils.isNotEmpty(username)){
            String node = (String)stringRedisTemplate.opsForValue().get(Constants.WS_REDIS_USERNAME_PREFIX+username);
            if(StringUtils.isNotEmpty(node)){
                return true;
            }
        }
        return false;
    }


    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.addDecoratorFactory(authWebSocketHandlerDecoratorFactory);
    }
}

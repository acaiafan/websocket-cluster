package com.cc.util;

/**
 * @author:xiasc
 * @Date:2018/11/12
 * @Time:14:50
 **/
public class Constants {
    public static String WS_REDIS_USERNAME_PREFIX = "websocket_username_";

    public static String WS_REDIS_SESSION_PREFIX = "websocket_session_";

    //mq推送top通知全体用户发送消息地址
    public static String WS_TOPIC_DESTINATION = "wstopic.topic";

    //WS连接广播地址
    public static String WS_TOPIC_ADDR = "/toAll/bullet";

    //WS连接点对点地址
    public static String WS_POINT_ADDR = "/queue/notifications";
}

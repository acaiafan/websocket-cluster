package com.cc.exception;

import lombok.Data;

/**
 * @author:xiasc
 * @Date:2018/11/12
 * @Time:16:53
 **/
@Data
public class WebsocketException extends RuntimeException {
    private Integer code;

    public WebsocketException(Integer code, String message){
        super(message);
        this.code = code;
    }

}

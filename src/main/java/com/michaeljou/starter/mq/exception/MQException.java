package com.michaeljou.starter.mq.exception;

/**
 * Created by lenovo on 2017/8/22.
 */
public class MQException extends RuntimeException{

    public MQException() {
    }

    public MQException(String message) {
        super(message);
    }
}

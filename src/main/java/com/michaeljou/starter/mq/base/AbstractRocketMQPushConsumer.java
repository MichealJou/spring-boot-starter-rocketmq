package com.michaeljou.starter.mq.base;


import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.common.message.MessageExt;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;


/**
 * @param <T> ocketMQ的消费者(Push模式)处理消息的接口
 * @author michaeljou
 */
@Slf4j
public abstract class AbstractRocketMQPushConsumer<T> {

    public AbstractRocketMQPushConsumer() {
    }


    /**
     * 继承这个方法处理消息
     *
     * @param message 消息范型
     * @return
     */
    public abstract boolean process(T message);

    /**
     * 原生dealMessage方法，可以重写此方法自定义序列化和返回消费成功的相关逻辑
     *
     * @param list                       消息列表
     * @param consumeConcurrentlyContext 上下文
     * @return
     */
    public ConsumeConcurrentlyStatus dealMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        for (MessageExt messageExt : list) {
            if (messageExt.getReconsumeTimes() != 0) {
                log.info("re-consume times: {}", messageExt.getReconsumeTimes());
            }
            log.info("receive msgId: {}, tags : {}", messageExt.getMsgId(), messageExt.getTags());
            T t = parseMessage(messageExt);
            if (null != t && !process(t)) {
                log.warn("consume fail , ask for re-consume , msgId: {}", messageExt.getMsgId());
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    /**
     * 反序列化解析消息
     *
     * @param message 消息体
     * @return
     */
    private T parseMessage(MessageExt message) {
        if (message == null || message.getBody() == null) {
            return null;
        }
        final Type type = this.getMessageType();
        if (type instanceof Class) {
            Object data = JSON.parseObject(new String(message.getBody()), type);
            return (T) data;
        } else {
            log.warn("Parse msg error. {}", message);
            return null;
        }
    }

    /**
     * 解析消息类型
     *
     * @return
     */
    private Type getMessageType() {
        Type superType = this.getClass().getGenericSuperclass();
        if (superType instanceof ParameterizedType) {
            return ((ParameterizedType) superType).getActualTypeArguments()[0];
        } else {

            return Object.class;

        }

    }
}

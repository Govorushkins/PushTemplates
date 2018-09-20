package com.testing.backendless_messaging_test.entities;

/**
 * Created by who on 4/15/15.
 */
public class PushLogger {

    private String deliveryTime;
    private String threadName;
    private String messageId;

    public PushLogger() {
        //
    }

    public PushLogger(Long deliveryTime, String threadName, String messageId)
    {
        this.deliveryTime = deliveryTime.toString();
        this.threadName = threadName;
        this.messageId = messageId;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}

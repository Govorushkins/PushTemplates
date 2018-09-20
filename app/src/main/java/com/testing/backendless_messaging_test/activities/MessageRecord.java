package com.testing.backendless_messaging_test.activities;


import android.content.ContentValues;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MessageRecord {
    public static final String MESSAGE_RECORD_TABLE_NAME = MessageRecord.class.getSimpleName();
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ACTION = "action";
    public static final String COLUMN_TEMPLATE_NAME = "templateName";
    public static final String COLUMN_MESSAGE_ID = "messageId";
    public static final String COLUMN_MESSAGE_TEXT = "messageText";
    public static final String COLUMN_USER_REPLY = "userReply";
    public static final String COLUMN_DATE_TIME = "dateTime";
    public static final List<String> columns = Arrays.asList(COLUMN_ID, COLUMN_ACTION, COLUMN_TEMPLATE_NAME, COLUMN_MESSAGE_ID, COLUMN_MESSAGE_TEXT, COLUMN_USER_REPLY, COLUMN_DATE_TIME);

    private Integer id;
    private String action;
    private String templateName;
    private Integer messageId;
    private String messageText;
    private String userReply;
    private Date dateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getUserReply() {
        return userReply;
    }

    public void setUserReply(String userReply) {
        this.userReply = userReply;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public void setDateTime(long milliseconds) {
        this.dateTime = new Date(milliseconds);
    }

    public ContentValues convertToContentValues() {
        ContentValues values = new ContentValues();
        values.put(MessageRecord.COLUMN_ID, id);
        values.put(MessageRecord.COLUMN_ACTION, action);
        values.put(MessageRecord.COLUMN_TEMPLATE_NAME, templateName);
        values.put(MessageRecord.COLUMN_MESSAGE_ID, messageId);
        values.put(MessageRecord.COLUMN_MESSAGE_TEXT, messageText);
        values.put(MessageRecord.COLUMN_USER_REPLY, userReply);
        values.put(MessageRecord.COLUMN_DATE_TIME, dateTime.getTime());
        return values;
    }
}

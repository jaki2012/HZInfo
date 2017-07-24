package com.hnac.hzinfo.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hnac.hzinfo.common.persistence.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author lijiechu
 * @create on 17/7/20
 * @description
 */
public class NoticeRecord {
    // 公告的唯一索引
    private int index;
    // 公告发布人ID
    private String sender;
    // 公告标题
    private String title;
    // 公告内容
    private String content;
    // 发布时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendTime;
    // 内容索引
    private String annexFileIndex;
    // 附件索引
    private String contentFileIndex;


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getSendTime() {
        return sendTime;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getAnnexFileIndex() {
        return annexFileIndex;
    }

    public void setAnnexFileIndex(String annexFileIndex) {
        this.annexFileIndex = annexFileIndex;
    }

    public String getContentFileIndex() {
        return contentFileIndex;
    }

    public void setContentFileIndex(String contentFileIndex) {
        this.contentFileIndex = contentFileIndex;
    }

}

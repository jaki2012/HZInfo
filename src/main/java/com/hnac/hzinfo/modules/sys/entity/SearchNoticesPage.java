package com.hnac.hzinfo.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author lijiechu
 * @create on 17/8/7
 * @description 构造搜索公告条件的实体类
 */
public class SearchNoticesPage {
    // 起始索引 通常为0
    private int start;
    // 页面大小
    private int length;
    // 第几页
    private int page;
    // 根据哪一列来排序
    private String orderColumn;
    // 排序的方向 desc降序 asc升序
    private String orderType;

    private String titleCondition;

    private String contentCondition;

    private String senderCondition;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date minSendTimeCondition;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date maxSendTimeCondition;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getOrderColumn() {
        return orderColumn;
    }

    public void setOrderColumn(String orderColumn) {
        this.orderColumn = orderColumn;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getTitleCondition() {
        return titleCondition;
    }

    public void setTitleCondition(String titleCondition) {
        this.titleCondition = titleCondition;
    }

    public String getContentCondition() {
        return contentCondition;
    }

    public void setContentCondition(String contenteCondition) {
        this.contentCondition = contenteCondition;
    }

    public String getSenderCondition() {
        return senderCondition;
    }

    public void setSenderCondition(String senderCondition) {
        this.senderCondition = senderCondition;
    }

    public Date getMinSendTimeCondition() {
        return minSendTimeCondition;
    }

    public void setMinSendTimeCondition(Date minSendTimeCondition) {
        this.minSendTimeCondition = minSendTimeCondition;
    }

    public Date getMaxSendTimeCondition() {
        return maxSendTimeCondition;
    }

    public void setMaxSendTimeCondition(Date maxSendTimeCondition) {
        this.maxSendTimeCondition = maxSendTimeCondition;
    }
}

package com.hnac.hzinfo.modules.sys.entity;

import java.util.List;

/**
 * @author lijiechu
 * @create on 17/7/25
 * @description noticesIndexes包装类 需要作为外部类才能被springmvc自动转化
 */
public class NoticesIndexesWrapper {
    private List<Integer> noticesIndexes;

    public NoticesIndexesWrapper() {
    }

    public List<Integer> getNoticesIndexes() {
        return noticesIndexes;
    }

    public void setNoticesIndexes(List<Integer> noticesIndexes) {
        this.noticesIndexes = noticesIndexes;
    }
}

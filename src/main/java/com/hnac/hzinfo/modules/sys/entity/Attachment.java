package com.hnac.hzinfo.modules.sys.entity;

/**
 * @author lijiechu
 * @create on 17/8/3
 * @description 附件类
 */
public class Attachment {
    private int attachmentID;

    private String savePath;

    public int getAttachmentID() {
        return attachmentID;
    }

    public void setAttachmentID(int attachmentID) {
        this.attachmentID = attachmentID;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }
}

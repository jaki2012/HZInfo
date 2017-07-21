package com.hnac.hzinfo.modules.sys.entity;

/**
 * @author lijiechu
 * @create on 17/7/20
 * @description
 */
public class Annex {
    // 公告附件文件唯一标识号
    private int fileID;
    // 文件在服务器所存储的路径
    private String savePath;
    // 文件对应的md5校验码
    private String fileMd5;

    public Annex(String savePath, String fileMd5) {
        this.savePath = savePath;
        this.fileMd5 = fileMd5;
    }

    public int getFileID() {
        return fileID;
    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }
}

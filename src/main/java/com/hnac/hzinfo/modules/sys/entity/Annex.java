package com.hnac.hzinfo.modules.sys.entity;

/**
 * @author lijiechu
 * @create on 17/7/20
 * @description 上传文件操作,同时服务于公告编辑器中的媒体文件和下载附件
 */
public class Annex {
    // 公告附件文件唯一标识号
    private int fileID;
    // 文件在服务器所存储的路径
    private String savePath;
    // 文件对应的md5校验码
    private String fileMd5;
    // 文件对应的类型,用于HTTP请求相应
    private String fileType;
    // 文件的存储名字,同样要求唯一
    private String fileName;
    // 文件的大小 单位为kb
    private long fileSize;
    // 文件从属的公告id
    private int noticeID;

    public Annex() {
    }

    public Annex(int fileID, String savePath, String fileMd5, String fileType, String fileName, int fileSize) {
        this.fileID = fileID;
        this.savePath = savePath;
        this.fileMd5 = fileMd5;
        this.fileType = fileType;
        this.fileName = fileName;
        this.fileSize = fileSize;
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

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getNoticeID() {
        return noticeID;
    }

    public void setNoticeID(int noticeID) {
        this.noticeID = noticeID;
    }
}

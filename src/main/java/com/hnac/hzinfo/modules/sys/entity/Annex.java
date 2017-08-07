package com.hnac.hzinfo.modules.sys.entity;

/**
 * @author lijiechu
 * @create on 17/8/3
 * @description 公告附件类
 */
public class Annex {
    // 附件的索引
    private int annexID;

    // 附件原来的名字 主要为了更好的用户交互设计
    // 避免用户直接下载以一堆字符串命名的文件
    private String originalName;

    // 文件的保存目录
    private String savePath;

    // 文件的md5值 用于检验
    private String fileMd5;

    public int getAnnexID() {
        return annexID;
    }

    public void setAnnexID(int annexID) {
        this.annexID = annexID;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
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

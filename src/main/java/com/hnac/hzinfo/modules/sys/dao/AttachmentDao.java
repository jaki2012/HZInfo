package com.hnac.hzinfo.modules.sys.dao;

import com.hnac.hzinfo.common.persistence.CrudDao;
import com.hnac.hzinfo.common.persistence.annotation.MyBatisDao;
import com.hnac.hzinfo.modules.sys.entity.Annex;
import com.hnac.hzinfo.modules.sys.entity.Attachment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lijiechu
 * @create on 17/8/3
 * @description 操作百度Ueditor上传文件的dao类
 */
@MyBatisDao
public interface AttachmentDao extends CrudDao<Attachment>{
    String getAttachmentPathByID(int attachmentID);

    int deleteUselessImages(int noticeIndex, @Param("imagesID") List<Integer> imagesID);

    List<Attachment> findUselessImages(int noticeIndex, @Param("imagesID") List<Integer> imagesID);

    int deleteAttachmentsByNoticeIndex(int noticeIndex);

}

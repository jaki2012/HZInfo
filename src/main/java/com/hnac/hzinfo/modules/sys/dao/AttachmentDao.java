package com.hnac.hzinfo.modules.sys.dao;

import com.hnac.hzinfo.common.persistence.CrudDao;
import com.hnac.hzinfo.common.persistence.annotation.MyBatisDao;
import com.hnac.hzinfo.modules.sys.entity.Attachment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lijiechu
 * @create on 17/8/3
 * @description
 */
@MyBatisDao
public interface AttachmentDao extends CrudDao<Attachment>{
    int deleteAttachmentByID(int attachmentID);

    Attachment getAttachmentByID(int attachmentID);

    List<Attachment> getAttachmentsNameByIDs(@Param("attachmentsID")List<Integer> attachmentIDs);

    int deleteAttachmentsByIDs(@Param("attachmentsID")List<Integer> attachmentIDs);
}

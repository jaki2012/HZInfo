package com.hnac.hzinfo.modules.sys.service;

import com.alibaba.fastjson.JSONObject;
import com.hnac.hzinfo.modules.sys.entity.Attachment;
import com.hnac.hzinfo.modules.sys.entity.NoticeRecord;
import com.hnac.hzinfo.modules.sys.entity.SearchNoticesPage;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lijiechu
 * @create on 17/7/20
 * @description
 */
public interface NoticeRecordService {

    /**
     * 新增公告接口
     * @param noticeRecord
     * @return 1代表新增成功, 0代表插入失败
     */
    int add(NoticeRecord noticeRecord);

    /**
     * 更新公告接口
     * @param noticeRecord
     * @return 受影响的行数, 1代表新增成功, 0代表插入失败
     */
    int update(NoticeRecord noticeRecord, String deleteAttachments);

    /**
     * 上传附件
     * @param fields 附件索引列表
     * @param filePath 文件的上传路径
     * @param fileMd5 文件md5码
     * @return 0代表新增成功, 1代表服务器空间不足, 2代表插入失败
     */
    int uploadAnnex(List<String> fields, MultipartFile file, String filePath, String fileMd5);

    /**
     * 返回数据库中所有的公告
     * 在数据量比较小的情况下把分页交给前台做
     * @return
     */
    List<NoticeRecord> getAllNotices();

    /**
     * 带分页的查询
     * @param searchNoticesPage 搜索条件实体类
     * @return
     */
    JSONObject getAllNoticesByPage(SearchNoticesPage searchNoticesPage);

    /**
     * 根据index索引查找对应的公告
     * @param noticeID 公告的索引
     * @return 若成功查找到则返回相应的公告,否则返回Null
     */
    NoticeRecord findNoticeByIndex(int noticeID);

    /**
     * 删除<list>中包含的索引对应的公告
     * @param indexes 要删除的公告列表
     * @return 受影响的行数
     */
    int deleteNoticesByIndexes(List<Integer> indexes);

    /**
     * 把ueditor富文本编辑器的图片上传服务器中,可根据需求存数据库表
     * @param image
     * @return ueditor要求返回的格式
     */
    Map<String, Object> handleUeditorImageUpload(MultipartFile image);

    byte[] getUeditorImage(int imageID);

    int uploadAttachment(MultipartFile file);

    int deleteAttachment(int attachmentID);

    List<Attachment> getAttachmentsNameByIDs(List<Integer> attachmentIDs);

    void downloadAttachment(int attachmentID, HttpServletResponse response);
}

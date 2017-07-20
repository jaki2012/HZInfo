package com.hnac.hzinfo.modules.sys.service;

import com.hnac.hzinfo.modules.sys.entity.NoticeRecord;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
     * 上传附件
     * @param fields 附件索引列表
     * @param filePath 文件的上传路径
     * @param fileMd5 文件md5码
     * @return 0代表新增成功, 1代表服务器空间不足, 2代表插入失败
     */
    int uploadAnnex(List<String> fields, MultipartFile file, String filePath, String fileMd5);
}

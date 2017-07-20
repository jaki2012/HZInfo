package com.hnac.hzinfo.modules.sys.service.Impl;

import com.hnac.hzinfo.common.service.BaseService;
import com.hnac.hzinfo.modules.sys.dao.NoticeRecordDao;
import com.hnac.hzinfo.modules.sys.entity.NoticeRecord;
import com.hnac.hzinfo.modules.sys.service.NoticeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author lijiechu
 * @create on 17/7/20
 * @description 不能继承BaseService,因为BaseService中有(Transactional=readonly)
 */
@Service
public class NoticeRecordServiceImpl implements NoticeRecordService {

    @Autowired
    NoticeRecordDao noticeRecordDao;

    @Override
    public int add(NoticeRecord noticeRecord) {
        noticeRecord.setSendTime(new Date());
        noticeRecord.setAnnexFileIndex("A");
        noticeRecord.setContentFileIndex("B");
        noticeRecordDao.insert(noticeRecord);
        return 0;
    }

    @Override
    public int uploadAnnex(List<String> fields, MultipartFile file, String filePath, String fileMd5) {
        //判断文件非空
        if(!file.isEmpty()){
            try {
                //文件保存路径
                String savePath = filePath + "/" + file.getOriginalFilename();
                file.transferTo(new File(savePath));
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return 0;
    }
}

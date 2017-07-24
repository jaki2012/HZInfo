package com.hnac.hzinfo.modules.sys.service.Impl;

import com.hnac.hzinfo.modules.sys.dao.AnnexDao;
import com.hnac.hzinfo.modules.sys.dao.NoticeRecordDao;
import com.hnac.hzinfo.modules.sys.entity.Annex;
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
 * @description 不能继承BaseService,因为BaseService中有(Transactional-readonly=true)
 */
@Service
public class NoticeRecordServiceImpl implements NoticeRecordService {

    @Autowired
    NoticeRecordDao noticeRecordDao;

    @Autowired
    AnnexDao annexDao;

    @Override
    public int add(NoticeRecord noticeRecord) {
        noticeRecord.setSendTime(new Date());
        noticeRecord.setAnnexFileIndex("A");
        noticeRecord.setContentFileIndex("B");
        // 返回的是更新的条数,或者说是受影响的条数
        noticeRecordDao.insert(noticeRecord);
        // System.out.println("echo: == " + noticeRecord.getIndex() );
        return 0;
    }

    @Override
    public int uploadAnnex(List<String> fields, MultipartFile file, String filePath, String fileMd5) {
        // 判断公告是否已存在
        // 判断文件是否非空
        if(!file.isEmpty()){
            try {
                // 文件保存路径
                String savePath = filePath + "/" + file.getOriginalFilename();
                file.transferTo(new File(savePath));
                // 数据库更新
                Annex newAnnex = new Annex(savePath,fileMd5);
                annexDao.insert(newAnnex);
                // 添加索引
                fields.add(String.valueOf(newAnnex.getFileID()));
            } catch(FileNotFoundException e) {
                e.printStackTrace();
                return 2;
            } catch (IOException e){
                e.printStackTrace();
                return 2;
            }
        }
        return 0;
    }

    @Override
    public List<NoticeRecord> getAllNotices() {
        return noticeRecordDao.findAll();
    }

    @Override
    public NoticeRecord findNoticeByIndex(int noticeID) {
        return noticeRecordDao.findByIndex(noticeID);
    }
}

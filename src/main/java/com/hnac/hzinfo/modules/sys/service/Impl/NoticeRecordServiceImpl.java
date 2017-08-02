package com.hnac.hzinfo.modules.sys.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hnac.hzinfo.common.utils.FileUtils;
import com.hnac.hzinfo.modules.sys.dao.AnnexDao;
import com.hnac.hzinfo.modules.sys.dao.NoticeRecordDao;
import com.hnac.hzinfo.modules.sys.entity.Annex;
import com.hnac.hzinfo.modules.sys.entity.NoticeRecord;
import com.hnac.hzinfo.modules.sys.service.NoticeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lijiechu
 * @create on 17/7/20
 * @description 不能继承BaseService,因为BaseService中有(Transactional-readonly=true)
 */
@Service
public class NoticeRecordServiceImpl implements NoticeRecordService {

    // 临时存储的ueditorImageCache
    private List<Integer> ueditorImageCache = new ArrayList<>();

    @Autowired
    private ServletContext servletContext;

    private int tag = 1;

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

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
        // 无参数时的正则表达式
        // String pattern = "(/ueditor/temp/imageupload/)(\\S)*(\\.)(\\w)*";
        // url带参数时的断言表达式
        String pattern = "(/ueditor/temp/imageupload/)(\\S)*(\\.)(\\S)*(?=\\\")";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(noticeRecord.getContent());
        int count = 0;

        while(m.find()) {
            count++;
            System.out.println(m.group(0));
        }
        return 0;
    }

    @Override
    public int update(NoticeRecord noticeRecord) {
        //更新时间
        noticeRecord.setSendTime(new Date());
        return noticeRecordDao.update(noticeRecord);
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
                Annex newAnnex = new Annex();
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
    public JSONObject getAllNoticesByPage(int start, int length, int page, int column, String dir, String titleCondition, String contentCondition
            ,String senderCondition, Date minSendTimeCondition, Date maxSendTimeCondition) {
        JSONObject result = new JSONObject();
        int totalNoticesSize = this.getAllNotices().size();
        int filteredNoticesSize = noticeRecordDao.findAllFiltered(titleCondition, contentCondition, senderCondition, minSendTimeCondition, maxSendTimeCondition).size();
        result.put("recordsTotal", totalNoticesSize);
        result.put("recordsFiltered", filteredNoticesSize);
        String columnName = "";
        switch(column) {
            case 2 : {
                columnName = "title";
                break;
            }
            case 3 : {
                columnName = "sender";
                break;
            }
            case 4 : {
                columnName = "sendTime";
                break;
            }
            default: {
                columnName = "sendTime";
                break;
            }
        }
        List<NoticeRecord> filteredNotices = noticeRecordDao.findAllByPage(start, length, page, columnName, dir, titleCondition, contentCondition, senderCondition, minSendTimeCondition, maxSendTimeCondition);
        String allNoticesStr = JSON.toJSONString(filteredNotices, SerializerFeature.WriteDateUseDateFormat);
        JSONArray noticesJsonArray = JSONArray.parseArray(allNoticesStr);
        result.put("data", noticesJsonArray);
        return result;

    }

    @Override
    public NoticeRecord findNoticeByIndex(int noticeID) {
        return noticeRecordDao.findByIndex(noticeID);
    }

    @Override
    public int deleteNoticesByIndexes(List<Integer> indexes) {
        return noticeRecordDao.deleteByIndexes(indexes);
    }

    @Override
    public Map<String,Object> handleUeditorImageUpload(String uuid, MultipartFile image) {
//        Annex annex = new Annex();
        // 获取文件的名字
        String imageName = image.getOriginalFilename();
        // 获取转换后的uuid文件名
        String uuidFileName = FileUtils.getUUIDFileName(imageName);
        // 获取文件的临时保存目录
        String subSavePath = FileUtils.getSubSavePath();
        String savePath = servletContext.getRealPath("/") + "ueditor/temp/imageupload"+ subSavePath;

//        annex.setFileName(imageName);
//        annex.setFileSize(image.getSize());
//        annex.setFileType(image.getContentType());
//        annex.setSavePath(savePath + "/"+ uuidFileName);

        try {
            File targetFile = new File(savePath, uuidFileName);
            //创建文件夹
            if(!targetFile.exists()){
                targetFile.mkdirs();
            }
            image.transferTo(targetFile);
//            annex.setFileMd5(FileUtils.getMd5ByFile(targetFile));
//            int fileID = annexDao.insert(annex);
//            this.ueditorImageCache.add(fileID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, java.lang.Object> m = new HashMap<String, java.lang.Object>();
        m.put("original",image.getOriginalFilename());
        m.put("name", image.getOriginalFilename());
//        m.put("url", "/sys/notice/ueditorimage?imageid="+annex.getFileID());
        //
        StringBuilder urlParams = new StringBuilder();
        urlParams.append("?fileName=").append(imageName)
                .append("&fileType=").append(image.getContentType())
                .append("&fileSize=").append(image.getSize());
        m.put("url", "/ueditor/temp/imageupload" + subSavePath + "/" + uuidFileName + urlParams);
        m.put("type",image.getContentType());
        m.put("size",image.getSize());
        m.put("state","SUCCESS");
        return m;
    }

    @Override
    public byte[] getUeditorImage(int imageID) {
        String imgPath = annexDao.getAnnexPathByID(imageID);
        File image = new File(imgPath);
        //  测试这个文件路径是否存在（也就是这个文件是否存在）
        if (!image.exists()) {
            return null;
        } else {
            try {
                // FileUtils.readFileToByteArray(File file)把一个文件转换成字节数组返回
                return(FileUtils.readFileToByteArray(image));
            } catch (IOException e){
                e.printStackTrace();
                return null;
            }
        }
    }
}

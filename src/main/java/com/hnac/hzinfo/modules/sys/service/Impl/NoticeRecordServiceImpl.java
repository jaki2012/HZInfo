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
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private ServletContext servletContext;

    @Autowired
    NoticeRecordDao noticeRecordDao;

    @Autowired
    AnnexDao annexDao;

    @Override
    @Transactional
    public int add(NoticeRecord noticeRecord) {
        noticeRecord.setSendTime(new Date());
        noticeRecord.setAnnexFileIndex("A");
        noticeRecord.setContentFileIndex("B");
        // 返回的是更新的条数,或者说是受影响的条数
        noticeRecordDao.insert(noticeRecord);
        // 无参数时的正则表达式
        // String pattern = "(/ueditor/temp/imageupload/)(\\S)*(\\.)(\\w)*";
        // url带参数时的断言表达式
        String pattern = "/(ueditor/temp/imageupload/\\S*)\\?fileName=([\\S\\s]*?)\\&fileType=(\\S*)\\&fileSize=(\\S*)(?=\\\")";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(noticeRecord.getContent());
        int count = 0;
        StringBuffer toReplace = new StringBuffer();
        // m.groupCount = 4
        while(m.find()) {
            Annex annex = new Annex();
            annex.setFileName(m.group(2));
            annex.setFileType(m.group(3));
            annex.setFileSize(Integer.parseInt(m.group(4)));
            annex.setNoticeID(noticeRecord.getIndex());
            try {
                File tempFile = new File(servletContext.getRealPath("/") + m.group(1));
                annex.setFileMd5(FileUtils.getMd5ByFile(tempFile));
                String subSavePath = FileUtils.getSubSavePath();
                String savePath = "/Users/lijiechu/Documents/HZInfoTemp" + subSavePath;
                FileUtils.moveToOtherFolder(servletContext.getRealPath("/") +m.group(1),savePath);
                annex.setSavePath(savePath + File.separator + tempFile.getName());
                annexDao.insert(annex);
                // 如下是string的方法 非matcher方法
                // m.group(0).replace(".*", "/sys/notice/ueditorimage?imageid="+ annex.getFileID());
                m.appendReplacement(toReplace, "/sys/notice/ueditorimage?imageid="+ annex.getFileID());
                System.out.println(m.group(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        m.appendTail(toReplace);
        noticeRecord.setContent(toReplace.toString());
        // 调用以下逻辑将产生多余及错误的逻辑,应直接dao层操作
        // this.update(noticeRecord);
        noticeRecordDao.update(noticeRecord);
        return 0;
    }

    @Override
    @Transactional
    public int update(NoticeRecord noticeRecord) {
        //更新时间
        noticeRecord.setSendTime(new Date());
        ArrayList<Integer> stillExistedImages = new ArrayList<>();
        // 匹配已经在数据库存档的图片链接正则表达式
        String pattern = "/sys/notice/ueditorimage\\?imageid\\=(\\d*)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(noticeRecord.getContent());
        while(m.find()){
            stillExistedImages.add(Integer.parseInt(m.group(1)));
        }

        // 匹配新增加的图片
        pattern = "/(ueditor/temp/imageupload/\\S*)\\?fileName=([\\S\\s]*?)\\&fileType=(\\S*)\\&fileSize=(\\S*)(?=\\\")";
        r = Pattern.compile(pattern);
        //m不能再复用
        Matcher m2 = r.matcher(noticeRecord.getContent());

        StringBuffer toReplace = new StringBuffer();
        // m2.groupCount = 4
        while(m2.find()) {
            Annex annex = new Annex();
            annex.setFileName(m2.group(2));
            annex.setFileType(m2.group(3));
            annex.setFileSize(Integer.parseInt(m2.group(4)));
            annex.setNoticeID(noticeRecord.getIndex());
            try {
                File tempFile = new File(servletContext.getRealPath("/") + m2.group(1));
                annex.setFileMd5(FileUtils.getMd5ByFile(tempFile));
                String subSavePath = FileUtils.getSubSavePath();
                String savePath = "/Users/lijiechu/Documents/HZInfoTemp" + subSavePath;
                FileUtils.moveToOtherFolder(servletContext.getRealPath("/") +m2.group(1),savePath);
                annex.setSavePath(savePath + File.separator + tempFile.getName());
                annexDao.insert(annex);
                // 如下是string的方法 非matcher方法
                // m2.group(0).replace(".*", "/sys/notice/ueditorimage?imageid="+ annex.getFileID());
                m2.appendReplacement(toReplace, "/sys/notice/ueditorimage?imageid="+ annex.getFileID());
                // 这句很关键 避免将新增的图片也删除了
                stillExistedImages.add(annex.getFileID());
                System.out.println(m2.group(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        m2.appendTail(toReplace);
        // 不要忘记把正则替换后的内容覆盖进去
        noticeRecord.setContent(toReplace.toString());
        CleanUselessImages cleanUselessImages = new CleanUselessImages(noticeRecord.getIndex(), stillExistedImages);
        Thread cleanThread = new Thread(cleanUselessImages);
        cleanThread.start();

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
    @Transactional
    public int deleteNoticesByIndexes(List<Integer> indexes) {
        for(Integer i : indexes) {
            annexDao.deleteAnnicesByNoticeIndex(i);
        }
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


        try {
            File targetFile = new File(savePath, uuidFileName);
            //创建文件夹
            if(!targetFile.exists()){
                targetFile.mkdirs();
            }
            image.transferTo(targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, java.lang.Object> m = new HashMap<String, java.lang.Object>();
        m.put("original",image.getOriginalFilename());
        m.put("name", image.getOriginalFilename());
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

    class CleanUselessImages implements Runnable {
        private int noticeID;
        private List<Integer> imagesID;

        public CleanUselessImages(int noticeID, List<Integer> imagesID) {
            this.noticeID = noticeID;
            this.imagesID = imagesID;
        }

        @Override
        public void run() {
            System.out.println("Clean thread starts..");
            if(imagesID.size() > 0) {
                List<Annex> annices = annexDao.findUselessImages(noticeID, imagesID);
                for(Annex annex : annices){
                    try{
                        FileUtils.deleteFile(annex.getSavePath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                annexDao.deleteUselessImages(noticeID, imagesID);
            }
            System.out.println("Clean thread ends..");
        }
    }
}

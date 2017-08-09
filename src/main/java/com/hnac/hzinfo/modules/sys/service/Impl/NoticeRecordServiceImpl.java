package com.hnac.hzinfo.modules.sys.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hnac.hzinfo.common.config.Global;
import com.hnac.hzinfo.common.utils.FileUtils;
import com.hnac.hzinfo.common.utils.StringUtils;
import com.hnac.hzinfo.modules.sys.dao.AnnexDao;
import com.hnac.hzinfo.modules.sys.dao.AttachmentDao;
import com.hnac.hzinfo.modules.sys.dao.NoticeRecordDao;
import com.hnac.hzinfo.modules.sys.entity.Annex;
import com.hnac.hzinfo.modules.sys.entity.Attachment;
import com.hnac.hzinfo.modules.sys.entity.NoticeRecord;
import com.hnac.hzinfo.modules.sys.entity.SearchNoticesPage;
import com.hnac.hzinfo.modules.sys.service.NoticeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lijiechu
 * @create on 17/7/20
 * @description 不能继承BaseService,因为BaseService中有(Transactional-readonly=true)
 */
@Service
@Lazy(false)
@PropertySource("classpath:hzinfo.properties")
public class NoticeRecordServiceImpl implements NoticeRecordService {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    NoticeRecordDao noticeRecordDao;

    @Autowired
    AnnexDao annexDao;

    @Autowired
    AttachmentDao attachmentDao;

    @Value("${uploadPath}")
    private String uploadPath;
    @Value("${uploadTempPath}")
    private String uploadTempPath;

    @Override
    @Transactional
    public int add(NoticeRecord noticeRecord) {
        noticeRecord.setSendTime(new Date());
        noticeRecord.setContentFileIndex("B");
        // 返回的是更新的条数,或者说是受影响的条数
        noticeRecordDao.insert(noticeRecord);
        // 无参数时的正则表达式
        // String pattern = "(/ueditor/temp/imageupload/)(\\S)*(\\.)(\\w)*";
        // url带参数时的断言表达式
        String pattern = "/(" + uploadTempPath + "/\\S*)\\?fileName=([\\S\\s]*?)\\&fileType=(\\S*)\\&fileSize=(\\S*)(?=\\\")";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(noticeRecord.getContent());
        StringBuffer toReplace = new StringBuffer();
        // m.groupCount = 4
        while(m.find()) {
            Attachment attachment = new Attachment();
            attachment.setFileName(m.group(2));
            attachment.setFileType(m.group(3));
            attachment.setFileSize(Integer.parseInt(m.group(4)));
            attachment.setNoticeID(noticeRecord.getIndex());
            try {
                File tempFile = new File(servletContext.getRealPath("/") + m.group(1));
                attachment.setFileMd5(FileUtils.getMd5ByFile(tempFile));
                String subSavePath = FileUtils.getSubSavePath();
                String savePath = uploadPath + subSavePath;
                FileUtils.moveToOtherFolder(servletContext.getRealPath("/") +m.group(1),savePath);
                attachment.setSavePath(savePath + File.separator + tempFile.getName());
                attachmentDao.insert(attachment);
                m.appendReplacement(toReplace, "/sys/notice/ueditorimage?imageid="+ attachment.getFileID());
                System.out.println(m.group(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        m.appendTail(toReplace);
        noticeRecord.setContent(toReplace.toString());
        noticeRecordDao.update(noticeRecord);
        return 0;
    }

    @Override
    @Transactional
    public int update(NoticeRecord noticeRecord, String deleteAnnexes) {

        if(null != deleteAnnexes && null != noticeRecord.getAnnexFileIndex()) {
            String[] deleteAnnexesStr = deleteAnnexes.split(",");
            String[] existedAnnexesStr = noticeRecord.getAnnexFileIndex().split(",");
            List<String> delAnnexes = Arrays.asList(deleteAnnexesStr);
            List<String> exiAnnexes = Arrays.asList(existedAnnexesStr);
            // 转换为ArrayList的原因在于 ArraysList$List类并没有对应的add和remove方法
            // 此处我们通过求集合的差集 更新公告的附件索引
            List<String> delAnnexesA = new ArrayList<>(delAnnexes);
            List<String> exiAnnexesA = new ArrayList<>(exiAnnexes);

            if (delAnnexes.size() > 0) {
                List<Integer> delAnnexesIDs = new ArrayList<>();
                for (String delAnnex : delAnnexes) {
                    delAnnexesIDs.add(Integer.parseInt(delAnnex));
                }
                // 启动线程清理不需要的文件和数据库索引
                Thread cleanThread = new Thread(new CleanUselessAnnexes(delAnnexesIDs));
                cleanThread.start();
            }

            // 暂时忽略排序信息
            exiAnnexesA.removeAll(delAnnexesA);
            String[] exiAnnexesArr = exiAnnexesA.toArray(new String[]{});
            noticeRecord.setAnnexFileIndex(StringUtils.join(exiAnnexesArr, ","));
        }

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
        pattern = "/(" + uploadTempPath + "/\\S*)\\?fileName=([\\S\\s]*?)\\&fileType=(\\S*)\\&fileSize=(\\S*)(?=\\\")";
        r = Pattern.compile(pattern);
        //m不能再复用
        Matcher m2 = r.matcher(noticeRecord.getContent());

        StringBuffer toReplace = new StringBuffer();
        // m2.groupCount = 4
        while(m2.find()) {
            Attachment attachment = new Attachment();
            attachment.setFileName(m2.group(2));
            attachment.setFileType(m2.group(3));
            attachment.setFileSize(Integer.parseInt(m2.group(4)));
            attachment.setNoticeID(noticeRecord.getIndex());
            try {
                File tempFile = new File(servletContext.getRealPath("/") + m2.group(1));
                attachment.setFileMd5(FileUtils.getMd5ByFile(tempFile));
                String subSavePath = FileUtils.getSubSavePath();
                String savePath = uploadPath + subSavePath;
                FileUtils.moveToOtherFolder(servletContext.getRealPath("/") +m2.group(1),savePath);
                attachment.setSavePath(savePath + File.separator + tempFile.getName());
                attachmentDao.insert(attachment);
                // 如下是string的方法 非matcher方法
                // m2.group(0).replace(".*", "/sys/notice/ueditorimage?imageid="+ attachment.getFileID());
                m2.appendReplacement(toReplace, "/sys/notice/ueditorimage?imageid="+ attachment.getFileID());
                // 这句很关键 避免将新增的图片也删除了
                stillExistedImages.add(attachment.getFileID());
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
    public JSONObject uploadAnnex(MultipartFile file, int[] fileIds,  String filePath, String fileMd5) {
        int resultCode = 0;
        int[] newFileIds = null;
        // 判断文件是否非空
        if(!file.isEmpty()){
            try {
                // 文件保存路径
                String savePath = filePath + "/" + file.getOriginalFilename();
                // 此处应当调用文件上传模块接口, 暂时用转存本地服务器与数据库代替
                file.transferTo(new File(savePath));
                Annex newAnnex = new Annex();
                newAnnex.setFileMd5(fileMd5);
                newAnnex.setOriginalName(file.getOriginalFilename());
                newAnnex.setSavePath(filePath);
                annexDao.insert(newAnnex);
                // 插入后获取自动更新的主键
                int newAnnexID = newAnnex.getAnnexID();
                int originalFileIdsLength = fileIds.length;
                newFileIds = new int[originalFileIdsLength + 1];
                System.arraycopy(fileIds,0,newFileIds,0,originalFileIdsLength);
                newFileIds[originalFileIdsLength] = newAnnexID;
            } catch(FileNotFoundException e) {
                e.printStackTrace();
                resultCode = 2;
            } catch (IOException e){
                e.printStackTrace();
                resultCode = 2;
            }
        }
        JSONObject result = new JSONObject();
        // 返回加上了新上传文件索引的索引列表
        result.put("fileIds",newFileIds);
        // 上传操作结果码 可能从调用文件模块接口返回
        result.put("resultCode",resultCode);
        return result;
    }

    @Override
    @Transactional
    public JSONObject deleteAnnex(long messageId, int[] fileIds, int fileId) {
        JSONObject deleteResult = new JSONObject();
        boolean result;
        int[] newFileIds = new int[fileIds.length-1];
        int index = 0;
        result = annexDao.deleteAnnexByID(fileId) == 1 ? true : false;
        for(int i: fileIds ){
            if(fileId != fileIds[i]){
                newFileIds[index++] = fileIds[i];
            }
        }
        deleteResult.put("newFileIds", newFileIds);
        deleteResult.put("result", result);
        return deleteResult;
    }

    @Override
    public List<NoticeRecord> getAllNotices() {
        return noticeRecordDao.findAll();
    }

    @Override
    public JSONObject getAllNoticesByPage(SearchNoticesPage searchNoticesPage) {
        JSONObject result = new JSONObject();
        int totalNoticesSize = this.getAllNotices().size();
        int filteredNoticesSize = noticeRecordDao.findAllFiltered(searchNoticesPage).size();
        result.put("recordsTotal", totalNoticesSize);
        result.put("recordsFiltered", filteredNoticesSize);
        List<NoticeRecord> filteredNotices = noticeRecordDao.findAllByPage(searchNoticesPage);
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
        ExecutorService pool  = Executors.newFixedThreadPool(5);
        for(Integer i : indexes) {
            // 传一个size为0的空ArrayList进入这个方法相当于删除所有的无用图片
            CleanUselessImages cleanUselessImages = new CleanUselessImages(i, new ArrayList<Integer>());
            pool.execute(cleanUselessImages);
            NoticeRecord noticeRecord = noticeRecordDao.findByIndex(i);
            // 数据库设计缺陷
            if(null != noticeRecord.getAnnexFileIndex() && noticeRecord.getAnnexFileIndex().length()!=0) {
                String[] annexFileIndexesStr = noticeRecord.getAnnexFileIndex().split(",");
                List<Integer> annexFileIndexes = new ArrayList<>();
                for (int j = 0; j < annexFileIndexesStr.length; j++) {
                    if(annexFileIndexesStr[j].length() == 0) continue;
                    annexFileIndexes.add(Integer.parseInt(annexFileIndexesStr[j]));
                }
                CleanUselessAnnexes cleanUselessAttachments = new CleanUselessAnnexes(annexFileIndexes);
                pool.execute(cleanUselessAttachments);
            }
        }
        // 关闭一个线程池
        pool.shutdown();
        return noticeRecordDao.deleteByIndexes(indexes);
    }

    @Override
    public Map<String,Object> handleUeditorImageUpload(MultipartFile image) {
//        Attachment annex = new Attachment();
        // 获取文件的名字
        String imageName = image.getOriginalFilename();
        // 获取转换后的uuid文件名
        String uuidFileName = FileUtils.getUUIDFileName(imageName);
        // 获取文件的临时保存目录
        String subSavePath = FileUtils.getSubSavePath();
        String savePath = servletContext.getRealPath("/") + uploadTempPath + subSavePath;

        // 将MultipartFile保存到指定目录
        FileUtils.saveMultipartFileToPath(image, savePath, uuidFileName);

        Map<String, java.lang.Object> m = new HashMap<String, java.lang.Object>();
        // 如下为百度Ueditor要求的回调格式
        m.put("original",image.getOriginalFilename());
        m.put("name", image.getOriginalFilename());
        StringBuilder urlParams = new StringBuilder();
        urlParams.append("?fileName=").append(imageName)
                .append("&fileType=").append(image.getContentType())
                .append("&fileSize=").append(image.getSize());
        m.put("url", File.separator + uploadTempPath + subSavePath + "/" + uuidFileName + urlParams);
        m.put("type",image.getContentType());
        m.put("size",image.getSize());
        m.put("state","SUCCESS");
        return m;
    }

    @Override
    public byte[] getUeditorImage(int imageID) {
        String imgPath = attachmentDao.getAttachmentPathByID(imageID);
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

    @Override
    public int uploadAnnex(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String uuidFileName = FileUtils.getUUIDFileName(fileName);
        // 获取文件的临时保存目录
        String subSavePath = FileUtils.getSubSavePath();
        String savePath = uploadPath + subSavePath;

        // 将MultipartFile保存到指定目录
        File targetFile = FileUtils.saveMultipartFileToPath(file, savePath, uuidFileName);

        Annex annex = new Annex();
        annex.setOriginalName(fileName);
        try {
            annex.setFileMd5(FileUtils.getMd5ByFile(targetFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        annex.setSavePath(savePath + File.separator + uuidFileName);
        annexDao.insert(annex);
        return annex.getAnnexID();
    }

    @Override
    public int deleteAnnex(int annexID) {
        Annex annexToDelete = annexDao.getAnnexByID(annexID);
        FileUtils.deleteFile(annexToDelete.getSavePath());
        return annexDao.deleteAnnexByID(annexID);
    }

    @Override
    public int deleteAnnexes(List<Integer> annexesIDs) {
        CleanUselessAnnexes cleanUselessAnnexes = new CleanUselessAnnexes(annexesIDs);
        Thread thread = new Thread(cleanUselessAnnexes);
        thread.start();
        return 0;
    }

    @Override
    public List<Annex> getAnnexesByIDs(List<Integer> annexesIDs) {
        return annexDao.getAnnexesByIDs(annexesIDs);
    }

    @Override
    public void downloadAnnex(int annexID, HttpServletResponse response) {
        Annex annex = annexDao.getAnnexByID(annexID);
        if(null == annex) {
            return;
        }
        File file = new File(annex.getSavePath());
        OutputStream out = null;
        if(null == file || !file.exists()){
            return;
        }
        try {
            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            // 解决中文乱码问题
            response.setHeader("Content-Disposition", "annex; filename=" + new String(annex.getOriginalName().getBytes("gbk"),"iso-8859-1"));
            out = response.getOutputStream();
            out.write(FileUtils.readFileToByteArray(file));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null != out) {
                try {
                    out.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Scheduled(cron = "${autocleancircle}")
    public void autoCleanNoticeRecords(){
        System.out.println("searching");
        int expireDays = Integer.parseInt(Global.getConfig("noticerecordexpiredays"));
        // 设置为 0 则不自动清理
        if(0 == expireDays) {
            return;
        }
        List<Integer> expiredNotices = noticeRecordDao.findExpiredNoticeIDs(expireDays);
        // 如果数组为空 则构造的sql语句为 Delete from t_notice_record 没有where条件 此时不能执行删除方法
        // 必须加这个参数 否则将删除所有公告
        if(!expiredNotices.isEmpty()) {
            this.deleteNoticesByIndexes(expiredNotices);
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
            System.out.println("Clean images thread starts..");
            if(null != imagesID) {
                List<Attachment> attachments = attachmentDao.findUselessImages(noticeID, imagesID);
                for(Attachment attachment : attachments){
                    try{
                        FileUtils.deleteFile(attachment.getSavePath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                attachmentDao.deleteUselessImages(noticeID, imagesID);
            }
            System.out.println("Clean images thread ends..");
        }
    }

    class CleanUselessAnnexes implements Runnable {
        private List<Integer> annexesID;

        public CleanUselessAnnexes(List<Integer> annexesID) {
            this.annexesID = annexesID;
        }

        @Override
        public void run() {
            System.out.println("Clean annexes thread starts..");
            List<Annex> annexes = annexDao.getAnnexesByIDs(annexesID);
            if(null != annexes) {
                for(Annex annex : annexes) {
                    try {
                        FileUtils.deleteFile(annex.getSavePath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            annexDao.deleteAnnexesByIDs(annexesID);
        }
    }
}

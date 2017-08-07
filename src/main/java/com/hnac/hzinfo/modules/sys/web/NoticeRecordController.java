package com.hnac.hzinfo.modules.sys.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hnac.hzinfo.modules.sys.entity.NoticeRecord;
import com.hnac.hzinfo.modules.sys.entity.NoticesIndexesWrapper;
import com.hnac.hzinfo.modules.sys.entity.SearchNoticesPage;
import com.hnac.hzinfo.modules.sys.service.NoticeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @author lijiechu
 * @create on 17/7/20
 * @description
 */
@RestController
@RequestMapping("/sys/notice")
public class NoticeRecordController {

    @Autowired
    NoticeRecordService noticeRecordService;

    @RequestMapping(value="/showWelcomeMsg", method = RequestMethod.GET)
    public String showWelcomeMsg(){
        return "Hello, Notice_Record_Module start to rock!";
    }

    @RequestMapping(value="/add", method = RequestMethod.POST)
    public int add(@RequestBody NoticeRecord noticeRecord){
        return noticeRecordService.add(noticeRecord);
    }

    @RequestMapping(value= "/update", method = RequestMethod.PUT)
    public int update(@RequestParam(value = "deleteAttachments",required = false)String deleteAttachments, @RequestBody NoticeRecord noticeRecord){
        return noticeRecordService.update(noticeRecord, deleteAttachments);
    }

    @RequestMapping(value = "/allNotices", method = RequestMethod.GET)
    public JSONObject getAllNotices(){
        // 将查询到的公告list转化为JSON 数组
        // jsonformat注解失效的原因在于, fastjson在转string的时候又一次把date转换成为了long型的date
        String allNoticesStr = JSON.toJSONString(noticeRecordService.getAllNotices(), SerializerFeature.WriteDateUseDateFormat);
        JSONArray noticesJsonArray = JSONArray.parseArray(allNoticesStr);
        // datatables要求将数据包在aaData字段中
        JSONObject resultJSON = new JSONObject();
        resultJSON.put("aaData", noticesJsonArray);
        return resultJSON;
    }

    @RequestMapping(value = "/allNoticesWithPage", method = RequestMethod.POST)
    public JSONObject getAllNoticesByPage(@RequestBody SearchNoticesPage searchNoticesPage){
        return noticeRecordService.getAllNoticesByPage(searchNoticesPage);
    }

    @RequestMapping(value = "/notice/{noticeID}", method = RequestMethod.GET)
    public JSONObject getNotice(@PathVariable("noticeID") int noticeID){
        // 为了日期格式化
        String noticeJsonStr = JSONObject.toJSONString(noticeRecordService.findNoticeByIndex(noticeID), SerializerFeature.WriteDateUseDateFormat);
        return JSON.parseObject(noticeJsonStr);
    }

    @RequestMapping(value = "/notice", method = RequestMethod.DELETE)
    public int deleteNoticesByIndexes(@RequestBody NoticesIndexesWrapper noticesIndexesWrapper){
        System.out.println("indexes" + noticesIndexesWrapper.getNoticesIndexes().size());
        return noticeRecordService.deleteNoticesByIndexes(noticesIndexesWrapper.getNoticesIndexes());
    }

    @RequestMapping(value="/uploadAnnex", method = RequestMethod.POST)
    public int uploadAnnex(@RequestParam("noticeIndex") int noticeIndex, @RequestParam("file") MultipartFile file){
        String filePath = "/Users/lijiechu/Documents/HZInfoTemp";
        return noticeRecordService.uploadAnnex(null, file, filePath,"0");
    }

    // 百度Ueditor上传图片
    @RequestMapping(value = "/ueditorimage", method = RequestMethod.POST)
    public Map<String, Object> handleUeditorImageUpload(@RequestParam(value = "upfile",required = false)MultipartFile upfile, HttpServletRequest request, HttpServletResponse response) {
        return noticeRecordService.handleUeditorImageUpload(upfile);
    }
    // 百度Ueditor获取图片回显
    @RequestMapping(value = "ueditorimage", method = RequestMethod.GET)
    public void readUeditorImage(@RequestParam("imageid") int imageid,HttpServletResponse response) {
        response.setContentType("image/*");
        try {
            response.getOutputStream().write(noticeRecordService.getUeditorImage(imageid));
            //java在使用流时,都会有一个缓冲区,按一种它认为比较高效的方法来发数据:
            //把要发的数据先放到缓冲区,缓冲区放满以后再一次性发过去,而不是分开一次一次地发.
            //而flush()表示强制将缓冲区中的数据发送出去,不必等到缓冲区满.
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "test")
    public String receivePageCloseMsg(){
        return "I've received your message";
    }

    @RequestMapping(value = "/annex", method = RequestMethod.POST)
    public JSONObject uploadAttachment(MultipartFile uploadAnnex) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error", "");
        int annexID = noticeRecordService.uploadAnnex(uploadAnnex);
        JSONObject initialPreviewConfig = new JSONObject();
        initialPreviewConfig.put("url", "/sys/notice/annex/delete");
        initialPreviewConfig.put("key",  annexID);
        JSONObject extraInfo = new JSONObject();
        extraInfo.put("annexID", annexID);
        JSONArray initialPreviewConfigArr = new JSONArray();
        initialPreviewConfig.put("extra", extraInfo);
        initialPreviewConfigArr.add(initialPreviewConfig);
        jsonObject.put("initialPreviewConfig", initialPreviewConfigArr);
        jsonObject.put("initialPreview", new JSONArray());
        return  jsonObject;
    }

    @RequestMapping(value = "/annexes", method = RequestMethod.GET)
    public JSONArray getAnnexesIDs(@RequestParam("annexFileIndex") String annexFileIndex){
        String[] annexesStrIDs = annexFileIndex.split(",");
        List<Integer> annexesIDs = new ArrayList<>();
        for(String annexStrID:annexesStrIDs){
            annexesIDs.add(Integer.parseInt(annexStrID));
        }

        String allAttachmentsStr = JSON.toJSONString( noticeRecordService.getAnnexesByIDs(annexesIDs));
        JSONArray attachmentsJsonArray = JSONArray.parseArray(allAttachmentsStr);
        return  attachmentsJsonArray;
    }

    @RequestMapping(value = "/annex", method = RequestMethod.GET)
    public void downloadAnnexByID(@RequestParam("annexID") int annexID, HttpServletRequest request, HttpServletResponse response){
        noticeRecordService.downloadAnnex(annexID,response);
    }

    @RequestMapping(value = "/annex", method = RequestMethod.DELETE)
    public int deleteAnnex(int annexID) {
        return noticeRecordService.deleteAnnex(annexID);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void  messageNotReadable(HttpMessageNotReadableException exception, HttpServletResponse response){
         System.out.println(exception.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DataAccessException.class)
    public void  requestNotFound(DataAccessException exception, HttpServletResponse response){
        System.out.println(exception.getMessage());
    }

}

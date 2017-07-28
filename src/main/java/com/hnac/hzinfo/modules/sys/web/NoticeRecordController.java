package com.hnac.hzinfo.modules.sys.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hnac.hzinfo.modules.sys.entity.NoticeRecord;
import com.hnac.hzinfo.modules.sys.entity.NoticesIndexesWrapper;
import com.hnac.hzinfo.modules.sys.service.NoticeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.SysexMessage;
import java.util.ArrayList;
import java.util.List;

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
    public int update(@RequestBody NoticeRecord noticeRecord){
        return noticeRecordService.update(noticeRecord);
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

    @RequestMapping(value = "/allNoticesWithPage", method = RequestMethod.GET)
    public JSONObject getAllNoticesByPage(@RequestParam int start, @RequestParam int length, @RequestParam int page,
    @RequestParam("orderColumn") int column, @RequestParam("orderType") String dir){
        return noticeRecordService.getAllNoticesByPage(start, length, page, column, dir);
    }

    @RequestMapping(value = "/notice/{noticeID}", method = RequestMethod.GET)
    public JSONObject getNotice(@PathVariable("noticeID") int noticeID){
        // 为了日期格式化
        String noticeJsonStr = JSONObject.toJSONString(noticeRecordService.findNoticeByIndex(noticeID), SerializerFeature.WriteDateUseDateFormat);
        return JSON.parseObject(noticeJsonStr);
        //return (JSONObject) JSONObject.toJSON(noticeRecordService.findNoticeByIndex(noticeID));
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

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void  messageNotReadable(HttpMessageNotReadableException exception, HttpServletResponse response){
        //LOGGER.error("请求参数不匹配。", exception);
         System.out.println(exception.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DataAccessException.class)
    public void  messageNotReadable2(DataAccessException exception, HttpServletResponse response){
        //LOGGER.error("请求参数不匹配。", exception);
        System.out.println(exception.getMessage());
    }

}

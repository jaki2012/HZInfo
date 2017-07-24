package com.hnac.hzinfo.modules.sys.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hnac.hzinfo.modules.sys.entity.NoticeRecord;
import com.hnac.hzinfo.modules.sys.service.NoticeRecordService;
import org.codehaus.jackson.map.JsonDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

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
        System.out.println("iamin");
        return noticeRecordService.add(noticeRecord);
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

    @RequestMapping(value = "/notice/{noticeID}", method = RequestMethod.GET)
    public JSONObject getNotice(@PathVariable("noticeID") int noticeID){
        // ParserConfig mapping = new ParserConfig();
        String noticeJsonStr = JSONObject.toJSONString(noticeRecordService.findNoticeByIndex(noticeID), SerializerFeature.WriteDateUseDateFormat);
        return JSON.parseObject(noticeJsonStr);
        //return (JSONObject) JSONObject.toJSON(noticeRecordService.findNoticeByIndex(noticeID));
    }

    @RequestMapping(value="/add1", method = RequestMethod.POST)
    public int add1(@RequestParam("haha") int i){
        return i;
    }

    @RequestMapping(value="/uploadAnnex", method = RequestMethod.POST)
    public int uploadAnnex(@RequestParam("noticeIndex") int noticeIndex, @RequestParam("file") MultipartFile file){
        String filePath = "/Users/lijiechu/Documents/HZInfoTemp";
        return noticeRecordService.uploadAnnex(null, file, filePath,"0");
    }
}

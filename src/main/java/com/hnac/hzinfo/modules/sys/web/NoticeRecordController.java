package com.hnac.hzinfo.modules.sys.web;

import com.hnac.hzinfo.modules.sys.entity.NoticeRecord;
import com.hnac.hzinfo.modules.sys.service.NoticeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

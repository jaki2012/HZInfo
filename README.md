# HZInfo

Before using, you should add webapp/WEB-INF/lib/* into JAR libraries

t_notice_record and t_notice_record_file related sql
----

```
CREATE TABLE `t_notice_record` (
  `index` int(4) NOT NULL AUTO_INCREMENT,
  `sender` varchar(50) NOT NULL,
  `title` varchar(100) NOT NULL,
  `content` varchar(4000) DEFAULT NULL,
  `sendTime` datetime NOT NULL,
  `annexFileIndex` varchar(20) DEFAULT NULL,
  `contentFileIndex` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`index`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

// 公告包含的附件
CREATE TABLE `t_notice_record_annex` (
  `annexID` int(11) NOT NULL AUTO_INCREMENT,
  `originalName` varchar(255) DEFAULT '',
  `savePath` varchar(255) NOT NULL,
  `fileMd5` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`attachmentID`)
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8;

//公告中带的百度Ueditor图片\视频\等上传文件
CREATE TABLE `t_ueditor_attachment` (
  `fileID` int(4) NOT NULL AUTO_INCREMENT,
  `savePath` varchar(255) NOT NULL DEFAULT '',
  `fileMd5` varchar(50) NOT NULL DEFAULT '',
  `fileType` varchar(50) NOT NULL DEFAULT '',
  `fileName` varchar(200) NOT NULL DEFAULT '',
  `fileSize` int(11) NOT NULL COMMENT 'Bytes',
  `noticeID` int(4) DEFAULT NULL,
  PRIMARY KEY (`fileID`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;


```

We cannot specify the length for ```datetime```, and the max length for ```nvarchar``` is 4000.
In mysql, the ```nvarchar``` will automatically transfer to ```varchar```.

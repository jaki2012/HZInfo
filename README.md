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

CREATE TABLE `t_notice_record_file` (
  `fileID` int(4) NOT NULL AUTO_INCREMENT,
  `savePath` varchar(255) NOT NULL DEFAULT '',
  `fileMd5` varchar(50) NOT NULL DEFAULT '',
  `fileType` varchar(50) NOT NULL DEFAULT '',
  `fileName` varchar(200) NOT NULL DEFAULT '',
  `fileSize` int(11) NOT NULL COMMENT 'KB',
  `fileUrl` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`fileID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

We cannot specify the length for ```datetime```, and the max length for ```nvarchar``` is 4000.
In mysql, the ```nvarchar``` will automatically transfer to ```varchar```.

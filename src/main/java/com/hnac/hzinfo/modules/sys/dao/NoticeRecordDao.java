package com.hnac.hzinfo.modules.sys.dao;

import com.hnac.hzinfo.common.persistence.CrudDao;
import com.hnac.hzinfo.common.persistence.annotation.MyBatisDao;
import com.hnac.hzinfo.modules.sys.entity.NoticeRecord;

import java.util.List;

/**
 * @author lijiechu
 * @create on 17/7/20
 * @description
 */
@MyBatisDao
public interface NoticeRecordDao extends CrudDao<NoticeRecord> {
    NoticeRecord findByIndex(int index);

    List<NoticeRecord> findAll();

    int deleteByIndexes(List<Integer> indexes);
}


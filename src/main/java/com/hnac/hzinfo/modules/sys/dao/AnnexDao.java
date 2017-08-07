package com.hnac.hzinfo.modules.sys.dao;

import com.hnac.hzinfo.common.persistence.CrudDao;
import com.hnac.hzinfo.common.persistence.annotation.MyBatisDao;
import com.hnac.hzinfo.modules.sys.entity.Annex;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lijiechu
 * @create on 17/7/20
 * @description 操作数据表t_notice_record_annex
 */
@MyBatisDao
public interface AnnexDao extends CrudDao<Annex> {

    int deleteAnnexByID(int annexID);

    Annex getAnnexByID(int annexID);

    List<Annex> getAnnexesByIDs(@Param("annexesIDs")List<Integer> annexesIDs);

    int deleteAnnexesByIDs(@Param("annexesIDs")List<Integer> annexesIDs);


}

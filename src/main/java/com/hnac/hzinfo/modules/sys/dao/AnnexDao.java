package com.hnac.hzinfo.modules.sys.dao;

import com.hnac.hzinfo.common.persistence.CrudDao;
import com.hnac.hzinfo.common.persistence.annotation.MyBatisDao;
import com.hnac.hzinfo.modules.sys.entity.Annex;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lijiechu
 * @create on 17/7/20
 * @description
 */
@MyBatisDao
public interface AnnexDao extends CrudDao<Annex> {
    String getAnnexPathByID(int annexID);

    int deleteUselessImages(int noticeIndex, @Param("imagesID") List<Integer> imagesID);

    List<Annex> findUselessImages(int noticeIndex, @Param("imagesID") List<Integer> imagesID);

    int deleteAnnicesByNoticeIndex(int noticeIndex);
}

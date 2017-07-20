package com.hnac.hzinfo.modules.hz3000data.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.hnac.hzinfo.modules.hz3000data.entity.Data;
import com.hnac.hzinfo.modules.hz3000data.entity.YcPoint;
import com.hnac.hzinfo.modules.hz3000data.utils.RealDataUtil;

import ReadData.ReadDataTypes;
import ReadData.YcRValue;
import ReadData.YkWValue;
import ReadData.YxRValue;

/**
 * 数据处理服务层
 * 
 * @author ypj
 *
 */
@Service
public class RealDataService {
	public List<Data> getYcRealData() {

		return null;
	}

	/**
	 * 根据数据组id，获得带数据组信息的实时数据
	 * 
	 * @author ypj
	 * @param groupId
	 * @return
	 */
	public List<Data> getYcRealDataWithGroupInfo(String groupId) {
		List<Data> dataList = new ArrayList<Data>();
		List<YcPoint> pointList = RealDataUtil.getYcPointByGroupId(groupId);
		long[] ids = new long[pointList.size()];
		for (int i = 0; i < pointList.size(); i++) {
			ids[i] = Long.parseLong(pointList.get(i).getId());
		}
		// if (!ReadData.Instance.ReadData_Init(RealDataUtil.getDllPath(),
		// RealDataUtil.getReadDllName(),
		// RealDataUtil.getWriteDllName())) {
		// return null;
		// }
		List<YcRValue> ycValueList = new ArrayList<YcRValue>();
		ReadDataTypes.EReadDataRet readDataRet = ReadAnyDatasID(ids, ycValueList, null, null);
		// ReadData.Instance.ReadAnyDatasID(ids, ycValueList, null, null);
		if (ReadDataTypes.EReadDataRet.rdFail == readDataRet) {
			return null;
		}
		for (int j = 0; j < ycValueList.size(); j++) {
			Data data = new Data();
			YcPoint point = pointList.get(j);
			YcRValue ycRValue = ycValueList.get(j);
			data.setPoint(point);
			data.setValue(ycRValue.Value);
			data.setQ(ycRValue.q);
			data.setTime(ycRValue.Time);
			dataList.add(data);
		}
		return dataList;
	}

	/**
	 * 根据数据组id得到不带数据组信息的遥测数据值
	 * 
	 * @author ypj
	 * @param groupId
	 * @return
	 */
	public List<Data> getYcRealDataWithoutGroupInfo(String groupId) {
		List<YcPoint> pointList = RealDataUtil.getYcPointByGroupId(groupId);
		long[] ids = new long[pointList.size()];
		for (int i = 0; i < pointList.size(); i++) {
			ids[i] = Long.parseLong(pointList.get(i).getId());
		}
		return getRealData(ids, RealDataUtil.IS_YC);
	}

	/**
	 * 根据id序列得到遥测值
	 * 
	 * @author ypj
	 * @param ids
	 * @return
	 */
	public List<Data> getYcRealData(long[] ids) {
		return getRealData(ids, RealDataUtil.IS_YC);
	}

	/**
	 * 根据id序列得到遥信数据
	 * 
	 * @author ypj
	 * @param ids
	 * @return
	 */
	public List<Data> getYxRealData(long[] ids) {
		return getRealData(ids, RealDataUtil.IS_YX);
	}

	/**
	 * 根据id序列和数据类型得到遥信或者遥测值
	 * 
	 * @author ypj
	 * @param ids
	 * @param type
	 * @return
	 */
	public List<Data> getRealData(long[] ids, int type) {
		// if (!ReadData.Instance.ReadData_Init(RealDataUtil.getDllPath(),
		// RealDataUtil.getReadDllName(),
		// RealDataUtil.getWriteDllName())) {
		// return null;
		// }
		List<Data> dataList = new ArrayList<Data>();
		if (type == RealDataUtil.IS_YC) {
			List<YcRValue> ycValueList = new ArrayList<YcRValue>();
			ReadDataTypes.EReadDataRet readDataRet = ReadAnyDatasID(ids, ycValueList, null, null);
			// ReadData.Instance.ReadAnyDatasID(ids, ycValueList, null, null);
			if (ReadDataTypes.EReadDataRet.rdFail == readDataRet) {
				return null;
			}
			for (int j = 0; j < ycValueList.size(); j++) {
				Data data = new Data();
				YcRValue ycRValue = ycValueList.get(j);
				data.setValue(ycRValue.Value);
				data.setQ(ycRValue.q);
				data.setTime(ycRValue.Time);
				dataList.add(data);
			}
		} else if (type == RealDataUtil.IS_YX) {
			List<YxRValue> yxRValueList = new ArrayList<YxRValue>();
			ReadDataTypes.EReadDataRet readDataRet = ReadAnyDatasID(null, null, ids, yxRValueList);
			// ReadData.Instance.ReadAnyDatasID(null, null, ids, yxRValueList);
			if (ReadDataTypes.EReadDataRet.rdFail == readDataRet) {
				return null;
			}
			for (int j = 0; j < yxRValueList.size(); j++) {
				Data data = new Data();
				YxRValue yxRValue = yxRValueList.get(j);
				data.setValue(yxRValue.Value);
				data.setQ(yxRValue.q);
				data.setTime(yxRValue.Time);
				dataList.add(data);
			}
		}
		return dataList;
	}

	/****************************************************************************************************************/
	public boolean ReadData_Init() {
		return true;
	}

	public ReadDataTypes.EReadDataRet ReadAnyDatasID(long[] YcID, List<YcRValue> YcV, long[] YxID, List<YxRValue> YxV) {
		Random random = new Random();
		if (YcV != null) {
			for (YcRValue yc : YcV) {
				yc.LastValidValue = random.nextDouble();
				yc.q = 0;
				yc.Time = new Date();
				yc.OrignalValue = random.nextDouble();
			}
		}
		if (YxV != null) {
			for (YxRValue yx : YxV) {
				yx.Value = (byte) random.nextInt(2);
				yx.q = 0;
				yx.Time = new Date();
				yx.YxType = ReadDataTypes.EYxTpye.yxOneBit;
			}
		}
		return ReadDataTypes.EReadDataRet.rdSuccess;
	}
	
	public ReadDataTypes.EYkRetStatus SendOrder(YkWValue YkV){
		return ReadDataTypes.EYkRetStatus.yrsSuccess;
	}

}

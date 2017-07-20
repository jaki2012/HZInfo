package com.hnac.hzinfo.modules.hz3000data.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.hnac.hzinfo.common.config.Global;
import com.hnac.hzinfo.common.utils.DateUtils;
import com.hnac.hzinfo.modules.hz3000data.dao.HisDataDao;
import com.hnac.hzinfo.modules.hz3000data.dao.HisDataDao.EnGroupByDateTime;
import com.hnac.hzinfo.modules.hz3000data.dao.HisDataDao.StatisticsType;
import com.hnac.hzinfo.modules.hz3000data.entity.HisDataSoe;
import com.hnac.hzinfo.modules.hz3000data.entity.HisDataYc;

public class HisDataUtil {
	public static String connectString = "";

	public static String getConnectString() {
		Global.getInstance();
		HisDataUtil.connectString = Global.getConfig("hz3000.db");
		return HisDataUtil.connectString;
	}

	public static List<HisDataYc> getYc(String conn, String ycid, String beginTime, String endTime) {
		HisDataDao dao = new HisDataDao();
		dao.SetConnStr(conn);
		return dao.GetYc(ycid, beginTime, endTime);
	}
	
	public static List<HisDataSoe> getSoe(String conn, String beginTime, String endTime, String soeIds, String typeIds) {
		HisDataDao dao = new HisDataDao();
		dao.SetConnStr(conn);
		return dao.GetSoe( beginTime, endTime, soeIds, typeIds);
	}

	public static List<List<String>> QueryRingData(String conn, String ycid, Date start, Date end,
			StatisticsType statisticstype, EnGroupByDateTime ringQueryType) {
		HisDataDao dao = new HisDataDao();
		dao.SetConnStr(conn);
		return dao.QueryRingData(ycid, start, end, statisticstype, ringQueryType);
	}

	public static List<List<String>> QueryDiffData(String conn, String ycid, Date start, Date end, int comparetime,
			StatisticsType statisticstype, EnGroupByDateTime ringQueryType) {
		HisDataDao dao = new HisDataDao();
		dao.SetConnStr(conn);
		return dao.QueryDiffData(ycid, start, end, comparetime, statisticstype, ringQueryType);
	}
	
	public static List<List<String>> QueryRingDataWithOffset(String conn, String ycid, Date start, Date end,
			StatisticsType statisticstype, EnGroupByDateTime ringQueryType, int offsetSecond) {
		HisDataDao dao = new HisDataDao();
		dao.SetConnStr(conn);
		return dao.QueryRingDataWithOffset(ycid, start, end, statisticstype, ringQueryType, offsetSecond);
	}

	public static List<List<String>> QueryDiffDataWithOffset(String conn, String ycid, Date start, Date end, int comparetime,
			StatisticsType statisticstype, EnGroupByDateTime ringQueryType, int offsetSecond) {
		HisDataDao dao = new HisDataDao();
		dao.SetConnStr(conn);
		return dao.QueryDiffDataWithOffset(ycid, start, end, comparetime, statisticstype, ringQueryType, offsetSecond);
	}
	

	/**
	 * 根据参数得到报表数据的查询开始时间
	 * 
	 * @author ypj
	 * @param date
	 * @param startTime
	 * @param type
	 * @return
	 */
	public static Date getSingleStartTime(String date, int startTime, EnGroupByDateTime type) {
		String startString = startTime >= 10 ? "" + startTime : "0" + startTime;
		switch (type) {
		case Hour:
			date = date.substring(0, 11) + startString + ":00:00";
			return DateUtils.parseDate(date);
		case Day:
			date = date.substring(0, 8) + startString + " 00:00:00";
			return DateUtils.parseDate(date);
		case Month:
			date = date.substring(0, 5) + startString + "-01 00:00:00";
			return DateUtils.parseDate(date);
		case Year:
			date = startTime + "-01-01 00:00:00";
			return DateUtils.parseDate(date);
		}
		return new Date();
	}

	/**
	 * 根据参数得到报表数据的开始时间
	 * 
	 * @author ypj
	 * @param date
	 * @param startTime
	 * @param timeUnit
	 * @return
	 */
	public static Date getSingleStartTime(String date, int startTime, String timeUnit) {
		String startString = startTime >= 10 ? "" + startTime : "0" + startTime;
		switch (timeUnit.toLowerCase()) {
		case "hourdata":
			date = date.substring(0, 11) + startString + ":00:00";
			return DateUtils.parseDate(date);
		case "daydata":
			date = date.substring(0, 8) + startString + " 00:00:00";
			return DateUtils.parseDate(date);
		case "monthdata":
			date = date.substring(0, 5) + startString + "-01 00:00:00";
			return DateUtils.parseDate(date);
		case "yeardata":
			date = startTime + "-01-01 00:00:00";
			return DateUtils.parseDate(date);
		}
		return new Date();
	}

	/**
	 * 根据字符内容返回查询数据库的统计方式
	 * 
	 * @author ypj
	 * @param dataQueryType
	 * @return
	 */
	public static StatisticsType getStatisticsType(String dataQueryType) {
		switch (dataQueryType) {
		case "IntVal":
			return StatisticsType.IntVal;
		case "MaxVal":
			return StatisticsType.MaxVal;
		case "MinVal":
			return StatisticsType.MinVal;
		case "AvgVal":
			return StatisticsType.AvgVal;
		case "SumVal":
			return StatisticsType.SumVal;
		case "ChangeVal":
			return StatisticsType.ChangeVal;
		default:
			return StatisticsType.IntVal;

		}
	}

	/**
	 * 根据字符内容返回查询数据库的统计方式
	 * 
	 * @author ypj
	 * @param dataQueryType
	 * @return
	 */
	public static int getIntStatisticsType(String dataQueryType) {
		switch (dataQueryType) {
		case "IntVal":
			return 0;
		case "MaxVal":
			return 1;
		case "MinVal":
			return 2;
		case "AvgVal":
			return 3;
		case "SumVal":
			return 4;
		case "ChangeVal":
			return 5;
		default:
			return 0;

		}
	}

	/**
	 * 根据字符内容返回查询数据库的时间单位
	 * 
	 * @author ypj
	 * @param dateTimeType
	 * @return
	 */
	public static EnGroupByDateTime getEnGroupByDateTime(String dateTimeType) {
		switch (dateTimeType) {
		case "HourData":
			return EnGroupByDateTime.Hour;
		case "DayData":
			return EnGroupByDateTime.Day;
		case "MonthData":
			return EnGroupByDateTime.Month;
		case "YearData":
			return EnGroupByDateTime.Year;
		default:
			return EnGroupByDateTime.Hour;
		}
	}

	/**
	 * 根据时间类型返回环比查询时的类型
	 * 
	 * @author ypj
	 * @param dateTimeType
	 * @return
	 */
	public static int getRingQueryType(String dateTimeType) {
		switch (dateTimeType) {
		case "HourData":
			return 0;
		case "DayData":
			return 1;
		case "MonthData":
			return 2;
		case "YearData":
			return 3;
		default:
			return 0;
		}
	}

	/**
	 * 得到开始时间
	 * 
	 * @author ypj
	 * @param date
	 * @param timeUnit
	 * @return
	 */
	public static Date getStartTime(String date, String timeUnit) {
		switch (timeUnit.toLowerCase()) {
		case "hourdata":
			date = date.substring(0, 11) + "00:00:00";
			return DateUtils.parseDate(date);
		case "daydata":
			date = date.substring(0, 8) + "01 00:00:00";
			return DateUtils.parseDate(date);
		case "monthdata":
			date = date.substring(0, 5) + "01-01 00:00:00";
			return DateUtils.parseDate(date);
		case "yeardata":
			date = date.substring(0, 5) + "01-01 00:00:00";
			return DateUtils.parseDate(date);
		}
		return new Date();
	}

	/**
	 * 得到截止时间
	 * 
	 * @author ypj
	 * @param date
	 * @param timeUnit
	 * @return
	 */
	public static Date getEndTime(String date, String timeUnit) {
		switch (timeUnit.toLowerCase()) {
		case "hourdata":
			date = date.substring(0, 11) + "23:59:59";
			return DateUtils.parseDate(date);
		case "daydata":
			Date currentDate = DateUtils.parseDate(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(currentDate);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String lastDay = format.format(calendar.getTime());
			date = lastDay + " 23:59:59";
			return DateUtils.parseDate(date);
		case "monthdata":
			date = date.substring(0, 5) + "12-31 23:59:59";
			return DateUtils.parseDate(date);
		case "yeardata":
			date = date.substring(0, 5) + "12-31 23:59:59";
			return DateUtils.parseDate(date);
		}
		return new Date();
	}
}

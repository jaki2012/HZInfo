package com.hnac.hzinfo.modules.hz3000data.dao;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.hnac.hzinfo.common.utils.DBConnectUtils;
import com.hnac.hzinfo.modules.hz3000data.entity.HisDataSoe;
import com.hnac.hzinfo.modules.hz3000data.entity.HisDataYc;

public class HisDataDao {
	public enum EnGroupByDateTime {
		Hour, Day, Month, Year
	}

	public enum StatisticsType {
		IntVal, MaxVal, MinVal, AvgVal, SumVal, ChangeVal
	}

	private String m_Conn;

	public HisDataDao() {
		super();
	}

	public HisDataDao(String conn) {
		super();
		this.m_Conn = conn;
	}

	public void SetConnStr(String conn) {
		m_Conn = conn;
	}

	public List<HisDataYc> GetYc(String id, String beginTime, String endTime) {
		String tablename = GetYcTableName(id);
		if (tablename != null) {
			String thenConn = "select * from " + tablename + " where datatime >= '" + beginTime + "' and datatime <= '"
					+ endTime + "' order by datatime";
			List<HisDataYc> ret = new ArrayList<HisDataYc>();
			DBConnectUtils.Query(m_Conn, thenConn, HisDataYc.class, ret);
			return ret;
		} else
			return new ArrayList<HisDataYc>();
	}

	public List<HisDataSoe> GetSoe(String beginTime, String endTime, String soeIds, String typeIds) {
		String tablename = "t_soe";
		String thenConn = "select * from " + tablename + " where datatime >= '" + beginTime + "' and datatime <= '"
				+ endTime + "' ";
		if (soeIds != null && !soeIds.equals("")){
			thenConn += " and hzrealid in (" + soeIds + ") ";
		}
		if (soeIds != null && !typeIds.equals("")){
			thenConn += " and soetype in (" + typeIds + ") ";
		}
		thenConn += "order by datatime desc";
		List<HisDataSoe> ret = new ArrayList<HisDataSoe>();
		DBConnectUtils.Query(m_Conn, thenConn, HisDataSoe.class, ret);
		return ret;
	}

	/**
	 * 获取某个id以后的记录
	 * 
	 * @author ypj
	 * @param id
	 * @return
	 */
	public List<HisDataSoe> GetSoe(long id,int index) {
		String thenConn = "select * from t_soe where id > '" + id + "' order by id desc limit 0 , " + index;
		List<HisDataSoe> ret = new ArrayList<HisDataSoe>();
		DBConnectUtils.Query(m_Conn, thenConn, HisDataSoe.class, ret);
		return ret;
	}

	/**
	 * 获取前index个记录
	 * 
	 * @author ypj
	 * @param index
	 * @return
	 */
	public List<HisDataSoe> GetSoe(int index) {
		if (index < 1) {
			return null;
		}
		String thenConn = "select * from t_soe order by id desc limit 0 , " + index;
		List<HisDataSoe> ret = new ArrayList<HisDataSoe>();
		DBConnectUtils.Query(m_Conn, thenConn, HisDataSoe.class, ret);
		return ret;
	}

	/*
	 * 获取环比数据
	 */
	public List<List<String>> QueryRingData(String ycid, Date start, Date end, StatisticsType statisticstype,
			EnGroupByDateTime ringQueryType) {
		String tablestr = GetYcTableName(ycid);
		if (tablestr != null) {
			String sqltext = GetRingData(tablestr, start, end, statisticstype, ringQueryType);
			return DBConnectUtils.QueryString(m_Conn, sqltext, null);
		} else
			return null;
	}

	/*
	 * 获取同比数据
	 */
	public List<List<String>> QueryDiffData(String ycid, Date start, Date end, int comparetime,
			StatisticsType statisticstype, EnGroupByDateTime ringQueryType) {
		String tablestr = GetYcTableName(ycid);
		if (tablestr != null) {
			String sqltext = GetDiffData(tablestr, start, end, comparetime, statisticstype, ringQueryType);
			return DBConnectUtils.QueryString(m_Conn, sqltext, null);
		} else
			return null;
	}

	/*
	 * 获取单个处理数据
	 */
	public List<List<String>> QuerySingleData(String ycid, Date start, StatisticsType statisticstype,
			EnGroupByDateTime ringQueryType) {
		String tablestr = GetYcTableName(ycid);
		if (tablestr != null) {
			String sqltext = GetSingleData(tablestr, start, statisticstype, ringQueryType);
			return DBConnectUtils.QueryString(m_Conn, sqltext, null);
		} else
			return null;
	}

	/*
	 * 获取环比数据
	 */
	public List<List<String>> QueryRingDataWithOffset(String ycid, Date start, Date end, StatisticsType statisticstype,
			EnGroupByDateTime ringQueryType, int offsetSecond) {
		String tablestr = GetYcTableName(ycid);
		if (tablestr != null) {
			String sqltext = GetRingDataWithOffset(tablestr, start, end, statisticstype, ringQueryType, offsetSecond);
			return DBConnectUtils.QueryString(m_Conn, sqltext, null);
		} else
			return null;
	}

	/*
	 * 获取同比数据
	 */
	public List<List<String>> QueryDiffDataWithOffset(String ycid, Date start, Date end, int comparetime,
			StatisticsType statisticstype, EnGroupByDateTime ringQueryType, int offsetSecond) {
		String tablestr = GetYcTableName(ycid);
		if (tablestr != null) {
			String sqltext = GetDiffDataWithOffset(tablestr, start, end, comparetime, statisticstype, ringQueryType, offsetSecond);
			return DBConnectUtils.QueryString(m_Conn, sqltext, null);
		} else
			return null;
	}

	/*
	 * 获取单个处理数据
	 */
	public List<List<String>> QuerySingleDataWithOffset(String ycid, Date start, StatisticsType statisticstype,
			EnGroupByDateTime ringQueryType, int offsetSecond) {
		String tablestr = GetYcTableName(ycid);
		if (tablestr != null) {
			String sqltext = GetSingleDataWithOffset(tablestr, start, statisticstype, ringQueryType, offsetSecond);
			return DBConnectUtils.QueryString(m_Conn, sqltext, null);
		} else
			return null;
	}
	
	/*
	 * 从给出的遥测rid获得表名
	 */
	public String GetYcTableName(String id) {
		List<List<String>> tabnameid = DBConnectUtils.QueryString(m_Conn,
				"select tableno from contactyc where ycid = " + id, null);
		if (tabnameid.size() > 0) {
			String tablename = "tyc_" + tabnameid.get(0).get(0);
			return tablename;
		} else
			return null;
	}

	private String GetRingData(String tablestr, Date start, Date end, StatisticsType staisticstype,
			EnGroupByDateTime ringQueryType) {
		String sqltext = "";
		String datetimegroupstr = GetGroupStrDateTime(ringQueryType);
		String comparedatetimes = GetRingCompareArrayByDateTime(start, end, ringQueryType);
		String stasticsstr = GetStastiscStr(staisticstype);

		sqltext += "select " + datetimegroupstr + " as datatime";
		sqltext += "," + stasticsstr;
		sqltext += " from " + tablestr;
		sqltext += " where " + datetimegroupstr + " in";
		sqltext += " " + comparedatetimes;
		sqltext += " " + "group by " + datetimegroupstr;
		sqltext += " " + "order by datatime";
		return sqltext;
	}
	
	private String GetRingDataWithOffset(String tablestr, Date start, Date end, StatisticsType staisticstype,
			EnGroupByDateTime ringQueryType, int offsetSecond) {
		String sqltext = "";
		String datetimegroupstr = GetGroupStrDateTimeEx(ringQueryType, offsetSecond);
		String comparedatetimes = GetRingCompareArrayByDateTime(start, end, ringQueryType);
		String stasticsstr = GetStastiscStr(staisticstype);

		sqltext += "select " + datetimegroupstr + " as datatime";
		sqltext += "," + stasticsstr;
		sqltext += " from " + tablestr;
		sqltext += " where " + datetimegroupstr + " in";
		sqltext += " " + comparedatetimes;
		sqltext += " " + "group by " + datetimegroupstr;
		sqltext += " " + "order by datatime";
		return sqltext;
	}

	private String GetDiffData(String tablestr, Date start, Date end, int comparetime, StatisticsType staisticstype,
			EnGroupByDateTime ringQueryType) {
		String sqltext = "";
		String datetimegroupstr = GetGroupStrDateTime(ringQueryType);
		String comparedatetimes = GetDiffCompareArrayByDateTime(start, end, comparetime, ringQueryType);
		String stasticsstr = GetStastiscStr(staisticstype);

		sqltext += "select " + datetimegroupstr + " as datatime";
		sqltext += "," + stasticsstr;
		sqltext += " from " + tablestr;
		sqltext += " where " + datetimegroupstr + " in";
		sqltext += " " + comparedatetimes;
		sqltext += " " + "group by " + datetimegroupstr;
		sqltext += " " + "order by datatime";
		return sqltext;
	}
	
	private String GetDiffDataWithOffset(String tablestr, Date start, Date end, int comparetime, StatisticsType staisticstype,
			EnGroupByDateTime ringQueryType, int offsetSecond) {
		String sqltext = "";
		String datetimegroupstr = GetGroupStrDateTimeEx(ringQueryType, offsetSecond);
		String comparedatetimes = GetDiffCompareArrayByDateTime(start, end, comparetime, ringQueryType);
		String stasticsstr = GetStastiscStr(staisticstype);

		sqltext += "select " + datetimegroupstr + " as datatime";
		sqltext += "," + stasticsstr;
		sqltext += " from " + tablestr;
		sqltext += " where " + datetimegroupstr + " in";
		sqltext += " " + comparedatetimes;
		sqltext += " " + "group by " + datetimegroupstr;
		sqltext += " " + "order by datatime";
		return sqltext;
	}

	private String GetSingleData(String tablestr, Date start, StatisticsType staisticstype,
			EnGroupByDateTime ringQueryType) {
		String sqltext = "";
		String datetimegroupstr = GetGroupStrDateTime(ringQueryType);
		String comparedatetimes = GetSingleCompareArrayByDateTime(start, ringQueryType);
		String stasticsstr = GetStastiscStr(staisticstype);

		sqltext += "select " + datetimegroupstr + " as datatime";
		sqltext += "," + stasticsstr;
		sqltext += " from " + tablestr;
		sqltext += " where " + datetimegroupstr + " in";
		sqltext += " " + comparedatetimes;
		sqltext += " " + "group by " + datetimegroupstr;
		sqltext += " " + "order by datatime";
		return sqltext;
	}
	
	private String GetSingleDataWithOffset(String tablestr, Date start, StatisticsType staisticstype,
			EnGroupByDateTime ringQueryType, int offsetSecond) {
		String sqltext = "";
		String datetimegroupstr = GetGroupStrDateTimeEx(ringQueryType, offsetSecond);
		String comparedatetimes = GetSingleCompareArrayByDateTime(start, ringQueryType);
		String stasticsstr = GetStastiscStr(staisticstype);

		sqltext += "select " + datetimegroupstr + " as datatime";
		sqltext += "," + stasticsstr;
		sqltext += " from " + tablestr;
		sqltext += " where " + datetimegroupstr + " in";
		sqltext += " " + comparedatetimes;
		sqltext += " " + "group by " + datetimegroupstr;
		sqltext += " " + "order by datatime";
		return sqltext;
	}

	private String GetStastiscStr(StatisticsType staisticstype) {
		String sqltext = "";
		switch (staisticstype) {
		case IntVal:
			sqltext += "val";
			break;
		case MaxVal:
			sqltext += "MAX(val) as maxval";
			break;
		case MinVal:
			sqltext += "MIN(val) as minval";
			break;
		case AvgVal:
			sqltext += "AVG(val) as avgval";
			break;
		case SumVal:
			sqltext += "SUM(val) as sumval";
			break;
		case ChangeVal:
			sqltext += "MAX(val) - MIN(val) as changeval";
			break;
		default:
			break;
		}

		return sqltext;
	}

	private String GetRingCompareArrayByDateTime(Date start, Date end, EnGroupByDateTime ringQueryType) {
		String sqltext = "";
		Calendar startCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		startCalendar.setTime(start);
		endCalendar.setTime(end);
		switch (ringQueryType) {
		case Hour: {
			List<Date> result = new ArrayList<Date>();
			startCalendar.set(Calendar.HOUR_OF_DAY, 0);
			startCalendar.set(Calendar.MINUTE, 0);
			startCalendar.set(Calendar.SECOND, 0);
			startCalendar.set(Calendar.MILLISECOND, 0);
			endCalendar.set(Calendar.HOUR_OF_DAY, 23);
			endCalendar.set(Calendar.MINUTE, 59);
			endCalendar.set(Calendar.SECOND, 59);
			endCalendar.set(Calendar.MILLISECOND, 999);
			for (; startCalendar.compareTo(endCalendar) < 0; startCalendar.add(Calendar.HOUR_OF_DAY, 1)) {
				result.add(startCalendar.getTime());
			}
			for (int i = 0; i < result.size(); i++) {
				if (i == 0) {
					sqltext += "(";
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
				sqltext += "'" + sdf.format(result.get(i)) + "',";
				if (i == result.size() - 1) {
					sqltext = sqltext.substring(0, sqltext.length() - 1);
					sqltext += ")";
				}
			}
		}
			break;
		case Day: {
			List<Date> result = new ArrayList<Date>();
			startCalendar.set(Calendar.DAY_OF_MONTH, 1);
			startCalendar.set(Calendar.HOUR_OF_DAY, 0);
			startCalendar.set(Calendar.MINUTE, 0);
			startCalendar.set(Calendar.SECOND, 0);
			startCalendar.set(Calendar.MILLISECOND, 0);
			endCalendar.set(Calendar.DAY_OF_MONTH, 1);
			endCalendar.set(Calendar.HOUR_OF_DAY, 0);
			endCalendar.set(Calendar.MINUTE, 0);
			endCalendar.set(Calendar.SECOND, 0);
			endCalendar.set(Calendar.MILLISECOND, 0);
			endCalendar.add(Calendar.MONTH, 1);
			for (; startCalendar.compareTo(endCalendar) < 0; startCalendar.add(Calendar.DAY_OF_YEAR, 1)) {
				result.add(startCalendar.getTime());
			}
			for (int i = 0; i < result.size(); i++) {
				if (i == 0) {
					sqltext += "(";
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				sqltext += "'" + sdf.format(result.get(i)) + "',";
				if (i == result.size() - 1) {
					sqltext = sqltext.substring(0, sqltext.length() - 1);
					sqltext += ")";
				}
			}
		}
			break;
		case Month: {
			List<Date> result = new ArrayList<Date>();
			startCalendar.set(Calendar.DAY_OF_MONTH, 1);
			startCalendar.set(Calendar.HOUR_OF_DAY, 0);
			startCalendar.set(Calendar.MINUTE, 0);
			startCalendar.set(Calendar.SECOND, 0);
			startCalendar.set(Calendar.MILLISECOND, 0);
			endCalendar.set(Calendar.MONTH, 11);
			endCalendar.set(Calendar.DAY_OF_MONTH, 1);
			endCalendar.set(Calendar.HOUR_OF_DAY, 0);
			endCalendar.set(Calendar.MINUTE, 0);
			endCalendar.set(Calendar.SECOND, 0);
			endCalendar.set(Calendar.MILLISECOND, 0);
			endCalendar.add(Calendar.MONTH, 1);
			for (; startCalendar.compareTo(endCalendar) < 0; startCalendar.add(Calendar.MONTH, 1)) {
				result.add(startCalendar.getTime());
			}
			for (int i = 0; i < result.size(); i++) {
				if (i == 0) {
					sqltext += "(";
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				sqltext += "'" + sdf.format(result.get(i)) + "',";
				if (i == result.size() - 1) {
					sqltext = sqltext.substring(0, sqltext.length() - 1);
					sqltext += ")";
				}
			}
		}
			break;
		case Year:
			break;
		default:
			break;
		}
		return sqltext;
	}

	private String GetDiffCompareArrayByDateTime(Date start, Date end, int comparetime,
			EnGroupByDateTime ringQueryType) {
		String sqltext = "";
		Calendar startCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		startCalendar.setTime(start);
		endCalendar.setTime(end);
		switch (ringQueryType) {
		case Hour: {
			List<Date> result = new ArrayList<Date>();
			startCalendar.set(Calendar.HOUR_OF_DAY, comparetime);
			startCalendar.set(Calendar.MINUTE, 0);
			startCalendar.set(Calendar.SECOND, 0);
			startCalendar.set(Calendar.MILLISECOND, 0);
			endCalendar.set(Calendar.HOUR_OF_DAY, 23);
			endCalendar.set(Calendar.MINUTE, 59);
			endCalendar.set(Calendar.SECOND, 59);
			endCalendar.set(Calendar.MILLISECOND, 999);
			for (; startCalendar.compareTo(endCalendar) < 0; startCalendar.add(Calendar.DAY_OF_YEAR, 1)) {
				result.add(startCalendar.getTime());
			}
			for (int i = 0; i < result.size(); i++) {
				if (i == 0) {
					sqltext += "(";
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
				sqltext += "'" + sdf.format(result.get(i)) + "',";
				if (i == result.size() - 1) {
					sqltext = sqltext.substring(0, sqltext.length() - 1);
					sqltext += ")";
				}
			}
		}
			break;
		case Day: {
			List<Date> result = new ArrayList<Date>();
			startCalendar.set(Calendar.DAY_OF_MONTH, 1);
			startCalendar.set(Calendar.HOUR_OF_DAY, 0);
			startCalendar.set(Calendar.MINUTE, 0);
			startCalendar.set(Calendar.SECOND, 0);
			startCalendar.set(Calendar.MILLISECOND, 0);
			endCalendar.set(Calendar.DAY_OF_MONTH, 1);
			endCalendar.set(Calendar.HOUR_OF_DAY, 23);
			endCalendar.set(Calendar.MINUTE, 59);
			endCalendar.set(Calendar.SECOND, 59);
			endCalendar.set(Calendar.MILLISECOND, 999);
			for (; startCalendar.compareTo(endCalendar) < 0; startCalendar.add(Calendar.MONTH, 1)) {
				result.add(startCalendar.getTime());
			}
			for (int i = 0; i < result.size(); i++) {
				if (i == 0) {
					sqltext += "(";
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-");
				sqltext += "'" + sdf.format(result.get(i)) + new DecimalFormat("00").format(comparetime) + "',";
				if (i == result.size() - 1) {
					sqltext = sqltext.substring(0, sqltext.length() - 1);
					sqltext += ")";
				}
			}
		}
			break;
		case Month: {
			List<Date> result = new ArrayList<Date>();
			startCalendar.set(Calendar.MONTH, comparetime - 1);
			startCalendar.set(Calendar.DAY_OF_MONTH, 1);
			startCalendar.set(Calendar.HOUR_OF_DAY, 0);
			startCalendar.set(Calendar.MINUTE, 0);
			startCalendar.set(Calendar.SECOND, 0);
			startCalendar.set(Calendar.MILLISECOND, 0);
			endCalendar.set(Calendar.MONTH, 11);
			endCalendar.set(Calendar.DAY_OF_MONTH, 1);
			endCalendar.set(Calendar.HOUR_OF_DAY, 0);
			endCalendar.set(Calendar.MINUTE, 0);
			endCalendar.set(Calendar.SECOND, 0);
			endCalendar.set(Calendar.MILLISECOND, 0);
			endCalendar.add(Calendar.MONTH, 1);
			for (; startCalendar.compareTo(endCalendar) < 0; startCalendar.add(Calendar.YEAR, 1)) {
				result.add(startCalendar.getTime());
			}
			for (int i = 0; i < result.size(); i++) {
				if (i == 0) {
					sqltext += "(";
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				sqltext += "'" + sdf.format(result.get(i)) + "',";
				if (i == result.size() - 1) {
					sqltext = sqltext.substring(0, sqltext.length() - 1);
					sqltext += ")";
				}
			}
		}
			break;
		case Year:
			break;
		default:
			break;
		}
		return sqltext;
	}

	private String GetSingleCompareArrayByDateTime(Date start, EnGroupByDateTime ringQueryType) {
		String sqltext = "";
		switch (ringQueryType) {
		case Hour: {
			List<Date> result = new ArrayList<Date>();
			result.add(start);
			for (int i = 0; i < result.size(); i++) {
				if (i == 0) {
					sqltext += "(";
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
				sqltext += "'" + sdf.format(result.get(i)) + "',";
				if (i == result.size() - 1) {
					sqltext = sqltext.substring(0, sqltext.length() - 1);
					sqltext += ")";
				}
			}
		}
			break;
		case Day: {
			List<Date> result = new ArrayList<Date>();
			result.add(start);
			for (int i = 0; i < result.size(); i++) {
				if (i == 0) {
					sqltext += "(";
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				sqltext += "'" + sdf.format(result.get(i)) + "',";
				if (i == result.size() - 1) {
					sqltext = sqltext.substring(0, sqltext.length() - 1);
					sqltext += ")";
				}
			}
		}
			break;
		case Month: {
			List<Date> result = new ArrayList<Date>();
			result.add(start);
			for (int i = 0; i < result.size(); i++) {
				if (i == 0) {
					sqltext += "(";
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				sqltext += "'" + sdf.format(result.get(i)) + "',";
				if (i == result.size() - 1) {
					sqltext = sqltext.substring(0, sqltext.length() - 1);
					sqltext += ")";
				}
			}
		}
			break;
		case Year:
			break;
		default:
			break;
		}
		return sqltext;
	}

	/*
	 * 0:Hour,1:Day,2:Month,3:Year
	 */
	private String GetGroupStrDateTime(EnGroupByDateTime ringQueryType) {
		String str = "";
		switch (ringQueryType) {
		case Hour:
			str = "DATE_FORMAT(datatime,'%Y-%m-%d %H')";
			break;
		case Day:
			str = "DATE_FORMAT(datatime,'%Y-%m-%d')";
			break;
		case Month:
			str = "DATE_FORMAT(datatime,'%Y-%m')";
			break;
		case Year:
			str = "DATE_FORMAT(datatime,'%Y')";
			break;
		default:
			break;
		}
		return str;
	}
	
	/*
	 * 带上了偏差
	 * 0:Hour,1:Day,2:Month,3:Year
	 */
	private String GetGroupStrDateTimeEx(EnGroupByDateTime ringQueryType, int diffSecond) {
		String str = "";	//如果要带上统一偏差，参考：DATE_FORMAT(addtime(datatime, -5),'%Y-%m-%d %H:%i:%S')
		switch (ringQueryType) {
		case Hour:
			str = "DATE_FORMAT(addtime(datatime, " + diffSecond + "),'%Y-%m-%d %H')";
			break;
		case Day:
			str = "DATE_FORMAT((addtime(datatime, " + diffSecond + "),'%Y-%m-%d')";
			break;
		case Month:
			str = "DATE_FORMAT((addtime(datatime, " + diffSecond + "),'%Y-%m')";
			break;
		case Year:
			str = "DATE_FORMAT((addtime(datatime, " + diffSecond + "),'%Y')";
			break;
		default:
			break;
		}
		return str;
	}
}

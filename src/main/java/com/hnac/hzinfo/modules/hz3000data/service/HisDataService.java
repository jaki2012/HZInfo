package com.hnac.hzinfo.modules.hz3000data.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.hnac.hzinfo.AMServer.base.UserInfo;
import com.hnac.hzinfo.common.service.BaseService;
import com.hnac.hzinfo.modules.hz3000data.dao.HisDataDao;
import com.hnac.hzinfo.modules.hz3000data.dao.HisDataDao.EnGroupByDateTime;
import com.hnac.hzinfo.modules.hz3000data.dao.HisDataDao.StatisticsType;
import com.hnac.hzinfo.modules.hz3000data.entity.BasePoint;
import com.hnac.hzinfo.modules.hz3000data.entity.Cell;
import com.hnac.hzinfo.modules.hz3000data.entity.DataGroup;
import com.hnac.hzinfo.modules.hz3000data.entity.HisDataSoe;
import com.hnac.hzinfo.modules.hz3000data.entity.HisDataYc;
import com.hnac.hzinfo.modules.hz3000data.entity.NullPoint;
import com.hnac.hzinfo.modules.hz3000data.entity.Sheet;
import com.hnac.hzinfo.modules.hz3000data.entity.SoePoint;
import com.hnac.hzinfo.modules.hz3000data.entity.WorkBook;
import com.hnac.hzinfo.modules.hz3000data.entity.YcPoint;
import com.hnac.hzinfo.modules.hz3000data.entity.YkPoint;
import com.hnac.hzinfo.modules.hz3000data.entity.YxPoint;
import com.hnac.hzinfo.modules.hz3000data.utils.HisDataUtil;
import com.hnac.hzinfo.modules.hz3000data.utils.RealDataUtil;
import com.hnac.hzinfo.modules.sys.utils.UserUtils;

@Service
public class HisDataService extends BaseService implements InitializingBean {

	public List<HisDataYc> getYc(String conn, String ycid, Date beginTime, Date endTime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return HisDataUtil.getYc(conn, ycid, formatter.format(beginTime), formatter.format(endTime));
	}

	private static String[][] mSoeType = { { "", "" }, { "分", "合" }, { "复归", "动作" }, { "闭锁", "允许" }, { "正常", "过低" },
			{ "报警复归", "报警" }, { "断线恢复", "断线" }, { "恢复正常", "异常" }, { "复归", "错误" }, { "光隔短路", "光隔开路" }, { "短路", "开路" },
			{ "异常恢复", "异常" }, { "失压恢复", "失压" }, { "定值正常", "定值错误" }, { "闭锁复归", "闭锁" }, { "复归", "输入" }, { "输入复归", "输入" },
			{ "失败", "动作" } };
	private static String[] mSoeKind = { "默认", "系统", "报警", "事故", "操作", "遥测越限", "遥信变位", "注册信息", "信息提示", "设备巡检", "遥信变位",
			"遥测越限", "遥测越限", "操作记录", "操作记录", "备用6", "备用7", "备用8", "未定义" };
	private static String[] mSelKind = {"0","1","2","3","4","5,11,12","6,10","7","8","9","13,14","15,16,17","18"};
	
	public static void parseSoe(HisDataSoe soe){
		try {
			BasePoint bp = null;
			// 系统事件且ID为0，则是通讯中断/恢复事件，不读厂信息
			if (soe.getSoeType() == 1 || soe.getSoeType() == 14) {
				bp = new NullPoint();
			} else
				bp = RealDataUtil.getDataPointDef(soe.getHzRealid());
			if (bp != null) {
				soe.factory = bp.getFactoryName();
				soe.group = bp.getGroupName();
				soe.pointname = bp.getName();
				soe.soetypename = mSoeKind[soe.getSoeType()];
				if (bp.getDataType() == RealDataUtil.IS_SOE) {
					SoePoint sp = (SoePoint) bp;
					parseSoeOptionvals(soe, sp);
					if (!Strings.isNullOrEmpty(sp.getAlarmType())) {
						int index = Integer.parseInt(sp.getAlarmType());
						soe.soestatusname = mSoeType[index][soe.get_Status()];
					}
				}
				else if (bp.getDataType() == RealDataUtil.IS_YX) {
					soe.soestatusname = mSoeType[1][soe.get_Status()];
				}
			} else {
				soe.factory = "";
				soe.group = "";
				soe.pointname = "";
				soe.soestatusname = "";
				soe.soetypename = "";
			}
			// RDS遥测越限(skback2上限，skback3上上限)
			if (soe.getSoeType() == 11) {
				if (soe.get_Status() == 0)
					soe.soestatusname = "超下限";
				else
					soe.soestatusname = "超上限";
			} else if (soe.getSoeType() == 12) {
				if (soe.get_Status() == 0)
					soe.soestatusname = "超下下限";
				else
					soe.soestatusname = "超上上限";
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void parseSoeOptionvals(HisDataSoe soe, SoePoint sp){
		if (soe.getOptionVals() == null || "NULL".equals(soe.getOptionVals()))
			return;
		else{
			String[] vals = soe.getOptionVals().split("\\|");
			String[] acts;
			String[] units;
			if (sp.getActionNameList() == null)
				acts = new String[0];
			else
				acts = sp.getActionNameList().split("▓");
			if (sp.getActionUnitList() == null)
				units = new String[0];
			else
				units = sp.getActionUnitList().split("▓");
			String ret = "";
			for (int i = 0; i < vals.length; i++){
				String tact = null;
				String tval = null;
				if (acts.length > i)
					tact = acts[i];
				if (vals.length > i)
					tval = vals[i];
				ret += tact + ":" + tval;
				if (units.length > i)
					ret += units[i];
				if (i != vals.length - 1)
					ret += "\n";
			}
			soe.setOptionNums(vals.length);
			soe.setOptionVals(ret);
		}
	}
	
	public List<HisDataSoe> getSoe(String conn, Date beginTime, Date endTime, String facId, String groupId, String typeId) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//id转查询字符集
		String soeIds = "";
		List<DataGroup> groups = new ArrayList<DataGroup>();
		if (!"-1".equals(groupId))
		{
			DataGroup tar = RealDataUtil.getGroupByIdEx(groupId);
			if (tar != null)
				groups.add(tar);
			else 
				return new ArrayList<HisDataSoe>();
			soeIds = "-1";
		}
		else if (!"-1".equals(facId)){
			groups = RealDataUtil.getGroupByFactoryIdEx(facId);
			if (groups == null || groups.size() == 0)
				return new ArrayList<HisDataSoe>();
			soeIds = "-1";
		}
		//拼id字符串
		for (int i = 0; i < groups.size(); i++){
			DataGroup tar = groups.get(i);
			List<YkPoint> yk = tar.getTelecontrolPointList();
			List<YcPoint> yc = tar.getTelemetryPointList();
			List<YxPoint> yx = tar.getTelesignalPointList();
			List<SoePoint> soe = tar.getEventPointList();
			if (yk != null){
				for (int j = 0; j < yk.size() ; j++){
					soeIds += "," + yk.get(j).getId();
				}
			}
			if (yx != null){
				for (int j = 0; j < yx.size() ; j++){
					soeIds += "," + yx.get(j).getId();
				}
			}
			if (yc != null){
				for (int j = 0; j < yc.size() ; j++){
					soeIds += "," + yc.get(j).getId();
				}
			}
			if (soe != null){
				for (int j = 0; j < soe.size() ; j++){
					soeIds += "," + soe.get(j).getId();
				}
			}
		}
		//拼类型字符串
		String typeIds = "";
		if (!"-1".equals(typeId)){
			int index = Integer.parseInt(typeId);
			typeIds = mSelKind[index];
		}
		//开始查询
		List<HisDataSoe> soes = HisDataUtil.getSoe(conn, formatter.format(beginTime), formatter.format(endTime), soeIds, typeIds);
		for (int i = 0; i < soes.size(); i++) {
			HisDataSoe soe = soes.get(i);
			parseSoe(soe);
		}
		return soes;
	}

	/**
	 * 读取id值以后的报警数据
	 * 
	 * @author ypj
	 * @param conn
	 * @param id
	 * @return
	 * @throws DocumentException
	 */
	public List<HisDataSoe> getSoe(String conn, long id, int index) throws DocumentException {
		HisDataDao dao = new HisDataDao(conn);
		List<HisDataSoe> soes = dao.GetSoe(id, index);
		for (int i = 0; i < soes.size(); i++) {
			HisDataSoe soe = soes.get(i);
			parseSoe(soe);
		}
		return soes;
	}

	/**
	 * 活动前index个记录
	 * 
	 * @author ypj
	 * @param conn
	 * @param idIdex
	 * @return
	 * @throws DocumentException
	 */
	public List<HisDataSoe> getSoe(String conn, int idIdex) throws DocumentException {
		HisDataDao dao = new HisDataDao(conn);
		List<HisDataSoe> soes = dao.GetSoe(idIdex);
		for (int i = 0; i < soes.size(); i++) {
			HisDataSoe soe = soes.get(i);
			parseSoe(soe);
		}
		return soes;
	}

	public List<List<String>> QueryRingData(String conn, String ycid, Date start, Date end, int statisticstype,
			int ringQueryType) {
		StatisticsType st = StatisticsType.IntVal;
		switch (statisticstype) {
		case 0:
			st = StatisticsType.IntVal;
			break;
		case 1:
			st = StatisticsType.MaxVal;
			break;
		case 2:
			st = StatisticsType.MinVal;
			break;
		case 3:
			st = StatisticsType.AvgVal;
			break;
		case 4:
			st = StatisticsType.SumVal;
			break;
		case 5:
			st = StatisticsType.ChangeVal;
			break;
		default:
			break;
		}
		EnGroupByDateTime rqt = EnGroupByDateTime.Hour;
		switch (ringQueryType) {
		case 0:
			rqt = EnGroupByDateTime.Hour;
			break;
		case 1:
			rqt = EnGroupByDateTime.Day;
			break;
		case 2:
			rqt = EnGroupByDateTime.Month;
			break;
		case 3:
			rqt = EnGroupByDateTime.Year;
			break;
		default:
			break;
		}
		return HisDataUtil.QueryRingData(conn, ycid, start, end, st, rqt);
	}

	public List<List<String>> QueryDiffData(String conn, String ycid, Date start, Date end, int comparetime,
			int statisticstype, int ringQueryType) {
		StatisticsType st = StatisticsType.IntVal;
		switch (statisticstype) {
		case 0:
			st = StatisticsType.IntVal;
			break;
		case 1:
			st = StatisticsType.MaxVal;
			break;
		case 2:
			st = StatisticsType.MinVal;
			break;
		case 3:
			st = StatisticsType.AvgVal;
			break;
		case 4:
			st = StatisticsType.SumVal;
			break;
		case 5:
			st = StatisticsType.ChangeVal;
			break;
		default:
			break;
		}
		EnGroupByDateTime rqt = EnGroupByDateTime.Hour;
		switch (ringQueryType) {
		case 0:
			rqt = EnGroupByDateTime.Hour;
			break;
		case 1:
			rqt = EnGroupByDateTime.Day;
			break;
		case 2:
			rqt = EnGroupByDateTime.Month;
			break;
		case 3:
			rqt = EnGroupByDateTime.Year;
			break;
		default:
			break;
		}
		return HisDataUtil.QueryDiffData(conn, ycid, start, end, comparetime, st, rqt);
	}

	public List<List<String>> QueryRingDataWithOffset(String conn, String ycid, Date start, Date end, int statisticstype,
			int ringQueryType, int offsetSecond) {
		StatisticsType st = StatisticsType.IntVal;
		switch (statisticstype) {
		case 0:
			st = StatisticsType.IntVal;
			break;
		case 1:
			st = StatisticsType.MaxVal;
			break;
		case 2:
			st = StatisticsType.MinVal;
			break;
		case 3:
			st = StatisticsType.AvgVal;
			break;
		case 4:
			st = StatisticsType.SumVal;
			break;
		case 5:
			st = StatisticsType.ChangeVal;
			break;
		default:
			break;
		}
		EnGroupByDateTime rqt = EnGroupByDateTime.Hour;
		switch (ringQueryType) {
		case 0:
			rqt = EnGroupByDateTime.Hour;
			break;
		case 1:
			rqt = EnGroupByDateTime.Day;
			break;
		case 2:
			rqt = EnGroupByDateTime.Month;
			break;
		case 3:
			rqt = EnGroupByDateTime.Year;
			break;
		default:
			break;
		}
		return HisDataUtil.QueryRingDataWithOffset(conn, ycid, start, end, st, rqt, offsetSecond);
	}

	public List<List<String>> QueryDiffDataWithOffset(String conn, String ycid, Date start, Date end, int comparetime,
			int statisticstype, int ringQueryType, int offsetSecond) {
		StatisticsType st = StatisticsType.IntVal;
		switch (statisticstype) {
		case 0:
			st = StatisticsType.IntVal;
			break;
		case 1:
			st = StatisticsType.MaxVal;
			break;
		case 2:
			st = StatisticsType.MinVal;
			break;
		case 3:
			st = StatisticsType.AvgVal;
			break;
		case 4:
			st = StatisticsType.SumVal;
			break;
		case 5:
			st = StatisticsType.ChangeVal;
			break;
		default:
			break;
		}
		EnGroupByDateTime rqt = EnGroupByDateTime.Hour;
		switch (ringQueryType) {
		case 0:
			rqt = EnGroupByDateTime.Hour;
			break;
		case 1:
			rqt = EnGroupByDateTime.Day;
			break;
		case 2:
			rqt = EnGroupByDateTime.Month;
			break;
		case 3:
			rqt = EnGroupByDateTime.Year;
			break;
		default:
			break;
		}
		return HisDataUtil.QueryDiffDataWithOffset(conn, ycid, start, end, comparetime, st, rqt, offsetSecond);
	}
	/**
	 * 返回单个点特定时间内的值
	 * 
	 * @author ypj
	 * @param ycid
	 * @param start
	 * @param statisticstype
	 * @param ringQueryType
	 * @return
	 */
	public List<List<String>> QuerySingleData(String conn, String ycid, Date start, StatisticsType statisticstype,
			EnGroupByDateTime ringQueryType) {

		return new HisDataDao(conn).QuerySingleData(ycid, start, statisticstype, ringQueryType);
	}
	
	/**
	 * 返回单个点特定时间内的值
	 * 
	 * @author ypj
	 * @param ycid
	 * @param start
	 * @param statisticstype
	 * @param ringQueryType
	 * @return
	 */
	public List<List<String>> QuerySingleDataWithOffset(String conn, String ycid, Date start, StatisticsType statisticstype,
			EnGroupByDateTime ringQueryType, int offsetSecond) {

		return new HisDataDao(conn).QuerySingleDataWithOffset(ycid, start, statisticstype, ringQueryType, offsetSecond);
	}

	/**
	 * 报表的响应
	 * 
	 * @author ypj
	 * @param request
	 * @param response
	 * @param sheet
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateSheetResponse(HttpServletRequest request, HttpServletResponse response, WorkBook workBook) {
		List<Sheet> sheets = workBook.getSheets();
		Map[] resultMaps = new Map[sheets.size()];
		HttpSession session = request.getSession();
		String reportPerson = ((UserInfo) session.getAttribute(UserUtils.CURRENT_USERINFO)).UserAccount;

		for (int i = 0; i < sheets.size(); i++) {
			Sheet sheet = sheets.get(i);
			resultMaps[i] = new HashMap<String, Object>();
			resultMaps[i].put("reportPerson", reportPerson);
			resultMaps[i].putAll(getDataFromNormalSheetEx(sheet, workBook.getDatetime()));
		}
		// --------------
		String resultString = JSON.toJSONString(resultMaps);
		resultString = "{\"status\":\"200\",\"errorMsg\":\"success\",\"result\":" + resultString + "}";
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer;
		try {
			writer = response.getWriter();
			writer.print(resultString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 普通方式下单个sheet的数据填充
	 * 
	 * @author ypj
	 * @param sheet
	 * @param dateTime
	 * @return
	 */
	public Map<String, Object> getDataFromNormalSheet(Sheet sheet, String dateTime) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Cell> cells = sheet.getCells();
		for (Cell cell : cells) {
			System.out.println("getting data ,real id : " + cell.getRealid());
			Date startDate = HisDataUtil.getSingleStartTime(dateTime, cell.getStarttime(), cell.getDateTimeType());
			List<List<String>> queryResult = this.QuerySingleData(HisDataUtil.getConnectString(), cell.getRealid(),
					startDate, HisDataUtil.getStatisticsType(cell.getDataquerytype()),
					HisDataUtil.getEnGroupByDateTime(cell.getDateTimeType()));
			if (queryResult != null && !queryResult.isEmpty()) {
				result.put(cell.getCellid(), queryResult.get(0).get(1));
			} else {
				result.put(cell.getCellid(), 0);
			}
		}
		return result;
	}

	/**
	 * 普通方式下单个sheet的数据填充,采用优化方案
	 * 
	 * @author hx
	 * @param sheet
	 * @param dateTime
	 * @return
	 */
	public Map<String, Object> getDataFromNormalSheetEx(Sheet sheet, String dateTime) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Cell> cells = sheet.getCells();
		//以下是批量查询过程
		Map<String, List<Cell>> mapcells = sortCellsEx(cells); 
		for (Map.Entry<String, List<Cell>> entry : mapcells.entrySet()) {  
			if (checkSortedCells(entry.getValue()) == false)
				continue;
			getSortedValue(entry.getValue(), dateTime);
		} 
		//以下是剩余查询过程
		for (Cell cell : cells) {
			if (cell.DBSorted == true){
				result.put(cell.getCellid(), cell.DBValue);
			}
			else{
				System.out.println("getting data ,real id : " + cell.getRealid());
				Date startDate = HisDataUtil.getSingleStartTime(dateTime, cell.getStarttime(), cell.getDateTimeType());
				List<List<String>> queryResult = this.QuerySingleData(HisDataUtil.getConnectString(), cell.getRealid(),
						startDate, HisDataUtil.getStatisticsType(cell.getDataquerytype()),
						HisDataUtil.getEnGroupByDateTime(cell.getDateTimeType()));
				if (queryResult != null && !queryResult.isEmpty()) {
					result.put(cell.getCellid(), queryResult.get(0).get(1));
				} else {
					result.put(cell.getCellid(), 0);
				}
			}
		}
		return result;
	}
	
	/**
	 * 按"id,持续时间,偏差时间,数据类型,时间间隔类型"的方式整理cell
	 * 
	 * @author hx
	 * @return
	 */
	private Map<String, List<Cell>> sortCellsEx(List<Cell> cells){
		Map<String, List<Cell>> mapcell = new HashMap<String, List<Cell>>();
		for (Cell cell : cells) {
			String tkey = getCellKey(cell);
			if (mapcell.containsKey(tkey)){
				mapcell.get(tkey).add(cell);
			}
			else{
				mapcell.put(tkey, new ArrayList<Cell>());
				mapcell.get(tkey).add(cell);
			}			
		}
		return mapcell;
	}
	
	/**
	 * 判断是否采用优化整体查询方式，即批量查询有效数据应占50%以上
	 * 
	 * @author hx
	 * @return
	 */
	private boolean checkSortedCells(List<Cell> cells){
		if (cells.size() <= 1)
			return false;
		if (cells.get(0).getDuration() != 1)
			return false;
		int mincount = cells.get(0).getStarttime();
		int maxcount = cells.get(0).getStarttime() + cells.get(0).getDuration();
		int numcount = cells.get(0).getDuration();
		for (int i = 1; i < cells.size(); i++)
		{
			if (cells.get(i).getStarttime() < mincount)
				mincount = cells.get(i).getStarttime();
			if (cells.get(i).getStarttime() + cells.get(i).getDuration() > maxcount)
				maxcount = cells.get(i).getStarttime() + cells.get(i).getDuration();
			numcount += cells.get(i).getDuration();
		}
		if (numcount * 2 < (maxcount - mincount))
			return false;
		else
			return true;
	}
	
	/**
	 * 通过批量查询，获取Cell的值
	 * 
	 * @author hx
	 * @return
	 */
	private void getSortedValue(List<Cell> cells, String dateTime){
		int mincount = cells.get(0).getStarttime();
		int maxcount = cells.get(0).getStarttime() + cells.get(0).getDuration();
		for (int i = 1; i < cells.size(); i++)
		{
			if (cells.get(i).getStarttime() < mincount)
				mincount = cells.get(i).getStarttime();
			if (cells.get(i).getStarttime() + cells.get(i).getDuration() > maxcount)
				maxcount = cells.get(i).getStarttime() + cells.get(i).getDuration();
		}
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		try {
			Date date = sdf.parse(dateTime);
			Calendar calendarStart = Calendar.getInstance();
			Calendar calendarEnd = Calendar.getInstance();
			calendarStart.setTime(date);
			calendarEnd.setTime(date);
			
			if ("HourData".equals(cells.get(0).getDateTimeType())){
				calendarStart.set(Calendar.HOUR_OF_DAY, mincount);
				calendarEnd.set(Calendar.HOUR_OF_DAY, maxcount - 1);
				List<List<String>> queryResult = this.QueryRingData(
						HisDataUtil.getConnectString(),
						cells.get(0).getRealid(),
						calendarStart.getTime(),
						calendarEnd.getTime(),
						HisDataUtil.getIntStatisticsType(cells.get(0).getDataquerytype()),
						0);
				if (queryResult != null){
					for (int i = 0; i < cells.size(); i++)
					{
						double val = getResultValueHour(date, cells.get(i), queryResult);
						cells.get(i).DBValue = val;
						cells.get(i).DBSorted = true;
					}
				}
			}
			else if ("DayData".equals(cells.get(0).getDateTimeType())){
				calendarStart.set(Calendar.DAY_OF_MONTH, mincount);
				calendarEnd.set(Calendar.DAY_OF_MONTH, maxcount - 1);
				List<List<String>> queryResult = this.QueryRingData(
						HisDataUtil.getConnectString(),
						cells.get(0).getRealid(),
						calendarStart.getTime(),
						calendarEnd.getTime(),
						HisDataUtil.getIntStatisticsType(cells.get(0).getDataquerytype()),
						1);
				if (queryResult != null){
					for (int i = 0; i < cells.size(); i++)
					{
						double val = getResultValueDay(date, cells.get(i), queryResult);
						cells.get(i).DBValue = val;
						cells.get(i).DBSorted = true;
					}
				}
			}
			else if ("MonthData".equals(cells.get(0).getDateTimeType())){
				calendarStart.set(Calendar.DAY_OF_MONTH, 1);
				calendarStart.set(Calendar.MONTH, mincount - 1);	//月份calendar从0起，输入从1起，要再多减1
				calendarEnd.set(Calendar.DAY_OF_MONTH, 1);
				calendarEnd.set(Calendar.MONTH, maxcount - 2);
				List<List<String>> queryResult = this.QueryRingData(
						HisDataUtil.getConnectString(),
						cells.get(0).getRealid(),
						calendarStart.getTime(),
						calendarEnd.getTime(),
						HisDataUtil.getIntStatisticsType(cells.get(0).getDataquerytype()),
						2);
				if (queryResult != null){
					for (int i = 0; i < cells.size(); i++)
					{
						double val = getResultValueMonth(date, cells.get(i), queryResult);
						cells.get(i).DBValue = val;
						cells.get(i).DBSorted = true;
					}
				}
			}
			else if ("YearData".equals(cells.get(0).getDateTimeType())){
				//此处应不可能
			}
			else{
				//此处应不可能
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private double getResultValueHour(Date baseTime, Cell cell, List<List<String>> queryResult){
		//先获取目标时间
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		SimpleDateFormat sdf2 = new SimpleDateFormat( "yyyy-MM-dd HH" );
		Calendar calendarStart = Calendar.getInstance();
		calendarStart.setTime(baseTime);
		calendarStart.set(Calendar.HOUR_OF_DAY, cell.getStarttime());
		double total = 0;
		for(int i = 0; i <= cell.getDuration() - 1; i++){
			String time = sdf2.format(calendarStart.getTime());
			total += getTimeValue(time, queryResult);
			calendarStart.add(Calendar.HOUR_OF_DAY, 1);
		}
		return total;
	}
	
	private double getResultValueDay(Date baseTime, Cell cell, List<List<String>> queryResult){
		//先获取目标时间
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		SimpleDateFormat sdf2 = new SimpleDateFormat( "yyyy-MM-dd" );
		Calendar calendarStart = Calendar.getInstance();
		calendarStart.setTime(baseTime);
		calendarStart.set(Calendar.DAY_OF_MONTH, cell.getStarttime());
		double total = 0;
		for(int i = 0; i <= cell.getDuration() - 1; i++){
			String time = sdf2.format(calendarStart.getTime());
			total += getTimeValue(time, queryResult);
			calendarStart.add(Calendar.DAY_OF_MONTH, 1);
		}
		return total;
	}
	
	private double getResultValueMonth(Date baseTime, Cell cell, List<List<String>> queryResult){
		//先获取目标时间
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		SimpleDateFormat sdf2 = new SimpleDateFormat( "yyyy-MM" );
		Calendar calendarStart = Calendar.getInstance();
		calendarStart.setTime(baseTime);
		calendarStart.set(Calendar.DAY_OF_MONTH, 1);	//为了避免1月31日+1个月可能带来的错误，将日期设为1日
		calendarStart.set(Calendar.MONTH, cell.getStarttime() - 1);
		double total = 0;
		for(int i = 0; i <= cell.getDuration() - 1; i++){
			String time = sdf2.format(calendarStart.getTime());
			total += getTimeValue(time, queryResult);
			calendarStart.add(Calendar.MONTH, 1);
		}
		return total;
	}
	
	private double getTimeValue(String time, List<List<String>> queryResult){
		for (int i = 0; i < queryResult.size(); i++){
			if (time.equals(queryResult.get(i).get(0))){
				return Double.parseDouble(queryResult.get(i).get(1));
			}
		}
		return 0;
	}
	
	private String getCellKey(Cell cell){
		return "" + cell.getRealid() + "," + cell.getDiffmin() + "," + cell.getDataquerytype() + "," + cell.getDateTimeType();
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {

	}
}

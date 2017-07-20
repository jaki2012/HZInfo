package com.hnac.hzinfo.modules.hz3000data.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hnac.hzinfo.AMServer.base.UserInfo;
import com.hnac.hzinfo.common.security.RSAUtils;
import com.hnac.hzinfo.common.utils.CacheUtils;
import com.hnac.hzinfo.common.utils.DataGridModel;
import com.hnac.hzinfo.common.utils.DateUtils;
import com.hnac.hzinfo.common.utils.StringUtils;
import com.hnac.hzinfo.common.web.BaseController;
import com.hnac.hzinfo.modules.hz3000data.entity.BasePoint;
import com.hnac.hzinfo.modules.hz3000data.entity.Data;
import com.hnac.hzinfo.modules.hz3000data.entity.DataGroup;
import com.hnac.hzinfo.modules.hz3000data.entity.DataResultSet;
import com.hnac.hzinfo.modules.hz3000data.entity.Factory;
import com.hnac.hzinfo.modules.hz3000data.entity.HisDataSoe;
import com.hnac.hzinfo.modules.hz3000data.entity.WorkBook;
import com.hnac.hzinfo.modules.hz3000data.entity.YcPoint;
import com.hnac.hzinfo.modules.hz3000data.service.HisDataService;
import com.hnac.hzinfo.modules.hz3000data.service.RealDataService;
import com.hnac.hzinfo.modules.hz3000data.utils.HisDataUtil;
import com.hnac.hzinfo.modules.hz3000data.utils.RealDataUtil;
import com.hnac.hzinfo.modules.sys.utils.UserUtils;

import ReadData.ReadDataTypes;
import ReadData.ReadDataTypes.EYkCommand;
import ReadData.YcRValue;
import ReadData.YkWValue;
import ReadData.YxRValue;

/**
 * 数据控制层
 * 
 * @author ypj
 *
 */
@Controller
@RequestMapping(value = "hz3000data/realdata")
public class RealDataControl extends BaseController {

	public static String KEYPAIR = "keyPair";// 存放密钥的session参数名
	public static String OPERATETIME = "operateTime";// 存放时间戳的session参数名
	public static long CHECKTIMEOUT = 3600000;
	public static long ALARMINTERV = 300000;
	@Autowired
	private RealDataService realDataService;
	@Autowired
	private HisDataService hisDataService;
	@Value("${soe.show.number}")
	private int soeNumer;

	/**
	 * 跳转到实时数据页面
	 * 
	 * @author ypj
	 * @return
	 */
	@RequestMapping("/yc")
	public String realTimeData() {
		return "/modules/hz3000data/realtelemetry";
	}

	/**
	 * 根据文件名和类型名跳转到html5文件页面的具体位置
	 * 
	 * @author ypj
	 * @param fileName
	 * @param type
	 * @return
	 */
	@RequestMapping("/file")
	public String directToFile() {
		String type = (String) CacheUtils.get(RealDataUtil.DATA_CACHE, "html_file_type");
		String fileName = (String) CacheUtils.get(RealDataUtil.DATA_CACHE, "html_file_name");
		int dotPosition = fileName.lastIndexOf(".");
		if (dotPosition > 0) {
			fileName = fileName.substring(0, dotPosition);
		}
		String realPath = RealDataUtil.getDynamicFileMappingPath();
		return realPath + type + "/" + fileName;
	}

	@RequestMapping("/bengzhan")
	public String getBengzhan() {
		return "/modules/hz3000data/bengzhan";
	}

	@RequestMapping("/daba")
	public String getDaba() {
		return "/modules/hz3000data/daba";
	}

	@RequestMapping("/dianzhan")
	public String getDianzhan() {
		return "/modules/hz3000data/dianzhan";
	}

	@RequestMapping("/report")
	public String getReport() {
		return "/modules/hz3000data/report";
	}

	@RequestMapping("/shuizi")
	public String getShuizi() {
		return "/modules/hz3000data/shuizi";
	}

	@RequestMapping("/visiblecontrol")
	public String getVisiblecontrol() {
		return "/modules/hz3000data/visiblecontrol";
	}

	@RequestMapping("/shuiqing")
	public String getShuiqing() {
		return "/modules/hz3000data/shuiqing";
	}

	/*------------------------------------------------------------------------------------------------------------------*/
	/**
	 * 返回所有工厂、站点
	 * 
	 * @author ypj
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getfactories")
	public List<Factory> getFactories() {
		return RealDataUtil.getFactories();
	}

	/**
	 * 由厂、站id获得数据组
	 * 
	 * @author ypj
	 * @param factoryId
	 *            厂、站id
	 * @return 组序列
	 */
	@ResponseBody
	@RequestMapping("/getgroupbyfactoryid")
	public List<DataGroup> getGroupByFactoryid(String factoryId) {
		return RealDataUtil.getGroupByFactoryId(factoryId);
	}

	/**
	 * 由数据组id获得遥测列表
	 * 
	 * @author ypj
	 * @param factoryId
	 *            厂、站id
	 * @return 组序列
	 */
	@ResponseBody
	@RequestMapping("/gettelemetrybygroupid")
	public List<YcPoint> getTelemetryByGroupid(String groupId) {
		List<YcPoint> list = null;
		try {
			list = RealDataUtil.getYcPointByGroupId(groupId);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return list;
	}

	/**
	 * 根据组id获得实时遥测数据
	 * 
	 * @author ypj
	 * @param groupId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/gettelemetryrealdatabygroupid")
	public DataGridModel<Data> getTelemetryRealTimeDataByGroupId(String groupId) {
		DataGridModel<Data> dataModel = new DataGridModel<Data>();
		List<Data> dataList = realDataService.getYcRealDataWithGroupInfo(groupId);
		dataModel.setRows(dataList);
		dataModel.setTotal(dataList.size());
		return dataModel;
	}

	/**
	 * 获得第一条告警数据
	 * 
	 * @author ypj
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/firstSoeAlarmSender")
	public List<HisDataSoe> getFirstSoeRValue() {

		String conn = HisDataUtil.getConnectString();
		List<HisDataSoe> datas = null;
		try {
			datas = hisDataService.getSoe(conn, (int) 1);
		} catch (DocumentException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return datas;
	}

	/**
	 * 获得id后最新告警数据
	 * 
	 * @author ypj
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/soeAlarmSender")
	public List<HisDataSoe> getSoeRValueAftreId(long id) {
		String conn = HisDataUtil.getConnectString();
		List<HisDataSoe> datas = null;
		try {
			datas = hisDataService.getSoe(conn, id, soeNumer);
		} catch (DocumentException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return datas;
	}

	/**
	 * @author ypj
	 * @param request
	 * @param type
	 *            具体的数据值存于数据库的href字段中
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getFileMenuList")
	public List<String> getFileMenuList(HttpServletRequest request, String type) {
		String path = request.getSession().getServletContext().getRealPath("/");
		path = path + RealDataUtil.getDynamicFilePath() + type.trim();
		return RealDataUtil.getFileMenuList(path);
	}

	/**
	 * 接收参数
	 * 
	 * @author ypj
	 * @param key
	 * @param value
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/setFileDirectParameter")
	public int setFileDirectParameter(HttpServletRequest request) {
		String type = request.getParameter("type");
		String fileName = request.getParameter("fileName");
		fileName = StringUtils.urlEncode(fileName);
		CacheUtils.put(RealDataUtil.DATA_CACHE, "html_file_type", type);
		CacheUtils.put(RealDataUtil.DATA_CACHE, "html_file_name", fileName);
		return 1;
	}

	/**
	 * 返回hotdot数据
	 * 
	 * @author ypj
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getRealData")
	public DataResultSet getRealData(HttpServletRequest request, HttpServletResponse response) {
		/* 获取数据 */
		String[] ids;
		String[] realIds = request.getParameterValues("realId");
		if (realIds != null) {
			ids = realIds;
		} else {
			@SuppressWarnings("rawtypes")
			Enumeration enumeration = request.getParameterNames();
			if (enumeration.hasMoreElements()) {
				String idString = (String) enumeration.nextElement();
				idString = idString.substring(idString.indexOf("[") + 1, idString.indexOf("]"));
				idString = idString.replaceAll("\"", "");
				ids = idString.split(",");
			} else {
				return null;
			}
		}
		DataResultSet result = new DataResultSet();
		Map<String, Data> dataMap = new HashMap<String, Data>();
		List<Long> ycList = new ArrayList<Long>();
		List<Long> yxList = new ArrayList<Long>();
		List<Long> noneList = new ArrayList<Long>();
		/* 按类别划分数据 */
		for (int i = 0; i < ids.length; i++) {
			try {
				ids[i] = ids[i].trim();
				int type = RealDataUtil.getDataType(ids[i]);
				if (RealDataUtil.IS_YC == type) {
					ycList.add(Long.parseLong(ids[i]));
				} else if (RealDataUtil.IS_YX == type) {
					yxList.add(Long.parseLong(ids[i]));
				} else {
					noneList.add(Long.parseLong(ids[i]));
				}
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		/* 按id序列获得数据 */
		List<Data> ycDataList, yxDataList;
		if (ycList.size() > 0) {
			long[] ycArray = new long[ycList.size()];
			for (int i = 0; i < ycList.size(); i++) {
				ycArray[i] = ycList.get(i);
			}
			ycDataList = realDataService.getYcRealData(ycArray);
			for (int j = 0; j < ycList.size(); j++) {
				dataMap.put("" + ycArray[j], ycDataList.get(j));
			}
		}
		if (yxList.size() > 0) {
			long[] yxArray = new long[yxList.size()];
			for (int i = 0; i < yxList.size(); i++) {
				yxArray[i] = yxList.get(i);
			}
			yxDataList = realDataService.getYxRealData(yxArray);
			for (int j = 0; j < yxList.size(); j++) {
				dataMap.put("" + yxArray[j], yxDataList.get(j));
			}
		}
		if (noneList.size() > 0) {
			for (int m = 0; m < noneList.size(); m++) {
				Data data = new Data();
				data.setQ(1);
				data.setValue(0);
				dataMap.put("" + noneList.get(m), data);
			}
		}
		result.setStatus("200");
		result.setErrorMsg("success");
		result.setResult(dataMap);
		return result;
	}

	/**
	 * 遥控登录验证
	 * 
	 * @author ypj
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping("/yklogin")
	public void ykLogin(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		/* 获得操作类型 */
		String action = request.getParameter("action");
		String data = new String();
		if (action == null) {
			@SuppressWarnings("rawtypes")
			Enumeration enumeration = request.getParameterNames();
			if (enumeration.hasMoreElements()) {
				String parameterString = (String) enumeration.nextElement();
				YkAction ykAction = JSON.parseObject(parameterString, YkAction.class);
				action = ykAction.getAction();
				data = ykAction.getData();
			}
			switch (action) {
			case "checklogin":
				checklogin(request, response);
				break;
			case "normalyklogin":
				normalyklogin(request, response, data);
				break;
			case "wufang":
				break;
			case "normalyk":
				normalyk(request, response, data);
				break;
			default:
			}
		}

	}

	/**
	 * 检测是否已经登录返回登录状态和一个rsa公钥和时间戳
	 * 
	 * @author ypj
	 * @param request
	 * @param response
	 */
	private void checklogin(HttpServletRequest request, HttpServletResponse response) {
		try {
			/* 生成密钥 */
			Map<String, Object> keyMap = RSAUtils.getKeys();
			RSAPublicKey rsaPublicKey = (RSAPublicKey) keyMap.get(RSAUtils.PUBLIC_KEY);
			/* session中放入密钥和时间戳 */
			Date date = new Date();
			HttpSession session = request.getSession();
			session.setAttribute(RealDataControl.KEYPAIR, keyMap);
			session.setAttribute(RealDataControl.OPERATETIME, date);
			/* 拼接返回json值 */
			UserInfo userInfo = (UserInfo) session.getAttribute(UserUtils.CURRENT_USERINFO);
			if (userInfo != null && userInfo.UserAccount != null) {
				writeLoginResponseContent(response, "200", "已登录!", rsaPublicKey.getPublicExponent().toString(16),
						rsaPublicKey.getModulus().toString(16), date);
			} else {
				writeLoginResponseContent(response, "201", "未登录!", rsaPublicKey.getPublicExponent().toString(16),
						rsaPublicKey.getModulus().toString(16), date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 普通登录返回登录验证结果
	 * 
	 * @author ypj
	 * @param request
	 * @param response
	 * @param data
	 */
	private void normalyklogin(HttpServletRequest request, HttpServletResponse response, String data) {
		HttpSession session = request.getSession();
		@SuppressWarnings("unchecked")
		Map<String, Object> keyMap = (Map<String, Object>) session.getAttribute(RealDataControl.KEYPAIR);
		Date date = (Date) session.getAttribute(RealDataControl.OPERATETIME);
		if (keyMap == null || date == null) {
			try {
				loginResponse(session, response, "211", "验证失败!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (new Date().getTime() - date.getTime() > RealDataControl.CHECKTIMEOUT) {
			try {
				loginResponse(session, response, "211", "时间过期!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				RSAPrivateKey privateKey = (RSAPrivateKey) keyMap.get(RSAUtils.PRIVATE_KEY);
				String receiveString = RSAUtils.decryptByPrivateKey(data, privateKey);
				String[] receiveArray = receiveString.split(",");
				String id = receiveArray[0].trim();
//				String password = receiveArray[1].trim();
				long timeMill = Long.parseLong(receiveArray[2].trim());

//				AMSClient client = AMSClient.getInstance(UserUtils.getAMSIp());
//				UserInfoHolder userInfoHolder = new UserInfoHolder();				
//				client.GetUser(id, userInfoHolder);
				
				UserInfo userInfo = new UserInfo();
				userInfo.UserAccount = id;
				userInfo.UserName = "名称";
//				userInfoHolder.value;
				if (/*!id.equals(userInfo.UserAccount) || client.ValidatePwdEx(id, password) < 0
						||*/ timeMill != date.getTime()) {
					loginResponse(session, response, "211", "获取用户信息失败");
				} else {
					session.setAttribute(UserUtils.CURRENT_USERINFO, userInfo);
					loginResponse(session, response, "200", "登录成功");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 响应普通遥控操作
	 * 
	 * @author ypj
	 * @param request
	 * @param response
	 * @param data
	 */
	private void normalyk(HttpServletRequest request, HttpServletResponse response, String data) {
		HttpSession session = request.getSession();
		@SuppressWarnings("unchecked")
		Map<String, Object> keyMap = (Map<String, Object>) session.getAttribute(RealDataControl.KEYPAIR);
		RSAPrivateKey privateKey = (RSAPrivateKey) keyMap.get(RSAUtils.PRIVATE_KEY);
		try {
			String receiveString = RSAUtils.decryptByPrivateKey(data, privateKey);

			String dataString = receiveString.substring(receiveString.indexOf("{") + 1, receiveString.indexOf("}"))
					.replaceAll("[\"]", "");
			String[] dataList = dataString.split(":");
			String controlTypeString = null, hotidString = null, extraParamString = null;

			for (int i = 0; i < dataList.length - 1; i++) {
				String title = dataList[i];
				String pramString = dataList[i + 1];
				int titelPosition = title.lastIndexOf(",");
				title = titelPosition < 0 ? title.replaceAll("\"", "").trim()
						: title.substring(titelPosition + 1).replaceAll("\"", "").trim();
				switch (title.toLowerCase()) {
				case "controltype":
					int controlPosition = pramString.lastIndexOf(",");
					controlTypeString = controlPosition < 0 ? pramString.replaceAll("\"", "").trim()
							: pramString.substring(0, controlPosition).replaceAll("\"", "").trim();
					break;
				case "hotid":
					int idDataPosition = pramString.lastIndexOf(",");
					hotidString = idDataPosition < 0 ? pramString.replaceAll("\"", "").trim()
							: pramString.substring(0, idDataPosition).replaceAll("\"", "").trim();
					break;
				case "extraparam":
					if (pramString.indexOf("[") > 0 && pramString.indexOf("]") > 0) {
						extraParamString = pramString.substring(pramString.indexOf("[") + 1, pramString.indexOf("]"))
								.replaceAll("\"", "");
					} else {
						extraParamString = pramString.replaceAll("\"", "");
					}

					break;
				}
			}
			if (controlTypeString == null || hotidString == null) {
				loginResponse(session, response, "222", "数据错误");
			} else {
				YkWValue ykwValue = new YkWValue();
				// 添加监测点id
				long hotid = Long.parseLong(hotidString);
				ykwValue.ID = hotid;
				// 由额外参数则添加额外参数
				if (extraParamString != null) {
					String[] extraParamList = extraParamString.split(",");
					double[] extraParam = new double[extraParamList.length];
					for (int i = 0; i < extraParamList.length; i++) {
						extraParam[i] = Double.parseDouble(extraParamList[i].trim());
					}

					ykwValue.dblParam = extraParam;
				}
				// 根据控制类型进行参数配置
				switch (controlTypeString) {
				case "1":
					ykwValue.YkCommand = EYkCommand.ykCommon;
					break;
				case "2":
					ykwValue.YkCommand = EYkCommand.ykCBreaker;
					break;
				case "3":
					ykwValue.YkCommand = EYkCommand.ykAdjust;
					break;
				case "6":
				case "8":
					double[] dblpara68 = new double[1];
					dblpara68[0] = 0;
					ykwValue.dblParam = dblpara68;
					ykwValue.YkCommand = EYkCommand.ykCommon;
					break;
				case "7":
				case "9":
					double[] dblpara79 = new double[1];
					dblpara79[0] = 1;
					ykwValue.dblParam = dblpara79;
					ykwValue.YkCommand = EYkCommand.ykCommon;
					break;
				}
				ykwValue.Operator = ((UserInfo) session.getAttribute(UserUtils.CURRENT_USERINFO)).UserAccount;
				ykwValue.OpTime = new Date();

				boolean ret = true;
				// ReadData.Instance.ReadData_Init(RealDataUtil.getDllPath(),
				// RealDataUtil.getReadDllName(),
				// RealDataUtil.getWriteDllName());
				if (ret && ReadDataTypes.EYkRetStatus.yrsSuccess == realDataService.SendOrder(
						ykwValue)/* ReadData.Instance.SendOrder(ykwValue) */) {
					loginResponse(session, response, "200", "操作成功");
				} else {
					loginResponse(session, response, "221", "发送遥控失败");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 拼接登录返回的json数据
	 * 
	 * @author ypj
	 * @param status
	 * @param errorMsg
	 * @param e
	 * @param n
	 * @param date
	 * @return
	 */
	private String getLoginResponseContent(String status, String errorMsg, String e, String n, Date date) {
		return "{\"status\":\"" + status + "\",\"errorMsg\":\"" + errorMsg + "\",\"result\":{\"key\":{\"e\":\"" + e
				+ "\",\"n\":\"" + n + "\"},\"time\":\"" + date.getTime() + "\"}}";
	}

	/**
	 * 写入response内容
	 * 
	 * @author ypj
	 * @param response
	 * @param status
	 * @param errorMsg
	 * @param e
	 * @param n
	 * @param date
	 * @throws IOException
	 */
	private void writeLoginResponseContent(HttpServletResponse response, String status, String errorMsg, String e,
			String n, Date date) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.print(getLoginResponseContent(status, errorMsg, e, n, date));
	}

	/**
	 * 遥控登录响应
	 * 
	 * @author ypj
	 * @param session
	 * @param response
	 * @param status
	 * @param errorMsg
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	private void loginResponse(HttpSession session, HttpServletResponse response, String status, String errorMsg)
			throws NoSuchAlgorithmException, IOException {
		Map<String, Object> keyMap = RSAUtils.getKeys();
		RSAPublicKey rsaPublicKey = (RSAPublicKey) keyMap.get(RSAUtils.PUBLIC_KEY);
		Date date = new Date();
		session.setAttribute(RealDataControl.KEYPAIR, keyMap);
		session.setAttribute(RealDataControl.OPERATETIME, date);
		writeLoginResponseContent(response, status, errorMsg, rsaPublicKey.getPublicExponent().toString(16),
				rsaPublicKey.getModulus().toString(16), date);
	}

	/**
	 * 报表页面数据填充
	 * 
	 * @author ypj
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping("/updateSheet")
	public void updateSheet(HttpServletRequest request, HttpServletResponse response) {
		String dataString = null;
		Enumeration<?> enumeration = request.getParameterNames();
		if (enumeration.hasMoreElements()) {
			dataString = (String) enumeration.nextElement();
		}
		WorkBook workBook = (WorkBook) JSON.parseObject(dataString, WorkBook.class);
		Date date = DateUtils.parseDate(workBook.getDatetime());
		workBook.setDatetime(DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss"));
		hisDataService.updateSheetResponse(request, response, workBook);
	}

	@SuppressWarnings("finally")
	@ResponseBody
	@RequestMapping("/paramName")
	public RealIdInfoResponse paramName(HttpServletRequest request, HttpServletResponse response) {
		String dataString = null;
		Enumeration<?> enumeration = request.getParameterNames();
		if (enumeration.hasMoreElements()) {
			dataString = (String) enumeration.nextElement();
		}
		BaseRequest realId = (BaseRequest) JSON.parseObject(dataString, BaseRequest.class);

		RealIdInfoResponse result = new RealIdInfoResponse();
		try {
			BasePoint point = RealDataUtil.getDataPointDef(realId.getRealId());
			result.setStatus("200");
			result.setErrorMsg("success");
			result.setResult(point.getFactoryName() + "-" + point.getGroupName() + "-" + point.getName());

		} catch (DocumentException e) {
			result.setStatus("201");
			result.setErrorMsg("读取错误");
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			return result;
		}
	}

	/**
	 * rds操作
	 * 
	 * @author ypj
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping("/rds")
	public DataResponse rds(HttpServletRequest request, HttpServletResponse response) {
		String dataString = null;
		Enumeration<?> enumeration = request.getParameterNames();
		if (enumeration.hasMoreElements()) {
			dataString = (String) enumeration.nextElement();
		}
		RdsRequest rdsRequest = (RdsRequest) JSON.parseObject(dataString, RdsRequest.class);
		switch (rdsRequest.getAction().toLowerCase()) {
		case "get":
			return getRds(rdsRequest);
		case "set":
			return setRds(rdsRequest);
		default:
			return null;
		}
	}

	/**
	 * 返回监护员列表
	 * 
	 * @author ypj
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getMonitorList")
	public List<UserInfo> getMonitorList(HttpSession session) {
		List<UserInfo> userList = new ArrayList<UserInfo>();
//				AMSClient.getInstance(UserUtils.getAMSIp())
//				.loadUsers(null, null, UserUtils.getMonitorId(), 0, Integer.MAX_VALUE).getRows();
		return userList;
	}

	/**
	 * rds数组操作
	 * 
	 * @author ypj
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping("/rdsArray")
	public RdsGetArrayResponse rdsArray(HttpServletRequest request, HttpServletResponse response) {
		String dataString = null;
		Enumeration<?> enumeration = request.getParameterNames();
		if (enumeration.hasMoreElements()) {
			dataString = (String) enumeration.nextElement();
		}
		Map<String, RdsRequest> dataMap = new HashMap<String, RdsRequest>();
		dataMap = (Map<String, RdsRequest>) JSON.parseObject(dataString,
				new TypeReference<HashMap<String, RdsRequest>>() {
				});
		boolean ret = true;
		// ReadData.Instance.ReadData_Init(RealDataUtil.getDllPath(),
		// RealDataUtil.getReadDllName(),
		// RealDataUtil.getWriteDllName());
		RdsGetArrayResponse rdsGetArrayResponse = new RdsGetArrayResponse();
		if (ret) {
			/* 获得需要查询的点的id */
			List<String> ycName = new ArrayList<String>();
			List<String> yxName = new ArrayList<String>();
			List<BasePoint> ycList = new ArrayList<BasePoint>();
			List<BasePoint> yxList = new ArrayList<BasePoint>();

			for (Entry<String, RdsRequest> entry : dataMap.entrySet()) {
				RdsRequest rdsRequest = entry.getValue();
				BasePoint point = RealDataUtil.getDataPoint(rdsRequest.getChannel(), rdsRequest.getModule(),
						rdsRequest.getAddress(), rdsRequest.getType());
				if (point.getDataType() == RealDataUtil.IS_YC) {
					ycList.add(point);
					ycName.add(entry.getKey());
					continue;
				}
				if (point.getDataType() == RealDataUtil.IS_YX) {
					yxList.add(point);
					yxName.add(entry.getKey());
					continue;
				}
			}
			/* id组装成id序列 */
			long[] ycIds = new long[ycList.size()];
			long[] yxIds = new long[yxList.size()];
			for (int i = 0; i < ycList.size(); i++) {
				ycIds[i] = Long.parseLong(ycList.get(i).getId());
			}
			for (int j = 0; j < yxList.size(); j++) {
				yxIds[j] = Long.parseLong(yxList.get(j).getId());
			}
			List<YcRValue> YcV = new ArrayList<YcRValue>();
			List<YxRValue> YxV = new ArrayList<YxRValue>();
			Map result = new HashMap();
			// ReadData.Instance.ReadAnyDatasID(ycIds, YcV, yxIds, YxV);
			realDataService.ReadAnyDatasID(ycIds, YcV, yxIds, YxV);
			for (int m = 0; m < YcV.size(); m++) {
				result.put(ycName.get(m), YcV.get(m).Value);
			}
			for (int n = 0; n < YxV.size(); n++) {
				result.put(yxName.get(n), YxV.get(n).Value);
			}
			rdsGetArrayResponse.setStatus("200");
			rdsGetArrayResponse.setErrorMsg("success");
			rdsGetArrayResponse.setResult(result);
			return rdsGetArrayResponse;
		}
		rdsGetArrayResponse.setStatus("202");
		rdsGetArrayResponse.setErrorMsg("获取数据失败!");
		return rdsGetArrayResponse;
	}

	/**
	 * 返回rds请求结果
	 * 
	 * @param rdsRequest
	 * @return
	 */
	private RdsGetResponse getRds(RdsRequest rdsRequest) {
		BasePoint point = RealDataUtil.getDataPoint(rdsRequest.getChannel(), rdsRequest.getModule(),
				rdsRequest.getAddress(), rdsRequest.getType());
		RdsGetResponse resResPonse = new RdsGetResponse();
		boolean ret = true;
		// ReadData.Instance.ReadData_Init(RealDataUtil.getDllPath(),
		// RealDataUtil.getReadDllName(),
		// RealDataUtil.getWriteDllName());
		if (ret) {
			switch (rdsRequest.getType()) {
			case 0:
				long[] YcID = { Long.parseLong(point.getId()) };
				List<YcRValue> YcV = new ArrayList<YcRValue>();
				ReadDataTypes.EReadDataRet readDataRet = realDataService.ReadAnyDatasID(YcID, YcV, null, null);
				// ReadData.Instance.ReadAnyDatasID(YcID, YcV, null, null);
				if (ReadDataTypes.EReadDataRet.rdSuccess == readDataRet) {
					resResPonse.setStatus("200");
					resResPonse.setErrorMsg("success");
					resResPonse.setResult(YcV.get(0).Value);
					return resResPonse;
				}
				break;
			case 1:
				long[] YxID = { Long.parseLong(point.getId()) };
				List<YxRValue> YxV = new ArrayList<YxRValue>();
				ReadDataTypes.EReadDataRet readyxDataRet = realDataService.ReadAnyDatasID(null, null, YxID, YxV);
				// ReadData.Instance.ReadAnyDatasID(null, null, YxID, YxV);
				if (ReadDataTypes.EReadDataRet.rdSuccess == readyxDataRet) {
					resResPonse.setStatus("200");
					resResPonse.setErrorMsg("success");
					resResPonse.setResult(YxV.get(0).Value);
					return resResPonse;
				}
				break;
			}
		}
		resResPonse.setStatus("201");
		resResPonse.setErrorMsg("failed");
		return resResPonse;
	}

	/**
	 * 返回rds请求结果
	 * 
	 * @param rdsRequest
	 * @return
	 */
	private RdsGetResponse setRds(RdsRequest rdsRequest) {
		RdsGetResponse resResPonse = new RdsGetResponse();
		boolean ret = true;
		// ReadData.Instance.ReadData_Init(RealDataUtil.getDllPath(),
		// RealDataUtil.getReadDllName(),
		// RealDataUtil.getWriteDllName());
		if (ret) {
			switch (rdsRequest.getType()) {
			case 0:
				boolean readDataRet = true;
//				ReadData.Instance.WriteOneYc(rdsRequest.getChannel(), rdsRequest.getModule(),
//						rdsRequest.getAddress(), rdsRequest.getValue(), rdsRequest.getQ(), new Date());
				if (readDataRet) {
					resResPonse.setStatus("200");
					resResPonse.setErrorMsg("success");
					resResPonse.setResult(1);
					return resResPonse;
				}
				break;
			case 1:
				boolean readyxDataRet = true;
//				ReadData.Instance.WriteOneYc(rdsRequest.getChannel(), rdsRequest.getModule(),
//						rdsRequest.getAddress(), rdsRequest.getValue(), rdsRequest.getQ(), new Date());
				if (readyxDataRet) {
					resResPonse.setStatus("200");
					resResPonse.setErrorMsg("success");
					resResPonse.setResult(1);
					return resResPonse;
				}
				break;
			}
		}
		resResPonse.setStatus("201");
		resResPonse.setErrorMsg("failed");
		return resResPonse;
	}

}

class BaseRequest {
	private String realId;

	/**
	 * @return the realId
	 */
	public String getRealId() {
		return realId;
	}

	/**
	 * @param realId
	 *            the realId to set
	 */
	public void setRealId(String realId) {
		this.realId = realId;
	}

}

/**
 * 读写rds数据类
 * 
 * @author ypj 2016年12月28日
 */
class RdsRequest {
	private String action;
	private long channel;
	private int module;
	private int address;
	private int type;
	private double value;
	private int q;

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the channel
	 */
	public long getChannel() {
		return channel;
	}

	/**
	 * @param channel
	 *            the channel to set
	 */
	public void setChannel(long channel) {
		this.channel = channel;
	}

	/**
	 * @return the module
	 */
	public int getModule() {
		return module;
	}

	/**
	 * @param module
	 *            the module to set
	 */
	public void setModule(int module) {
		this.module = module;
	}

	/**
	 * @return the address
	 */
	public int getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(int address) {
		this.address = address;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * @return the q
	 */
	public int getQ() {
		return q;
	}

	/**
	 * @param q
	 *            the q to set
	 */
	public void setQ(int q) {
		this.q = q;
	}

}

class RdsGetResponse extends DataResponse {
	private double result;

	/**
	 * @return the result
	 */
	public double getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(double result) {
		this.result = result;
	}

}

/**
 * 返回realId详细信息的类
 * 
 * @author ypj 2017年2月15日
 */
class RealIdInfoResponse extends DataResponse {
	private String result;

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
}

class RdsGetArrayResponse extends DataResponse {
	private Map<String, Double> result;

	/**
	 * @return the result
	 */
	public Map<String, Double> getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(Map<String, Double> result) {
		this.result = result;
	}

}

class DataResponse {
	private String status;
	private String errorMsg;

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the errorMsg
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

	/**
	 * @param errorMsg
	 *            the errorMsg to set
	 */
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}

class YkAction {
	private String action;
	private String data;

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	class Result {
		private String key;
		private String time;

		/**
		 * @return the key
		 */
		public String getKey() {
			return key;
		}

		/**
		 * @param key
		 *            the key to set
		 */
		public void setKey(String key) {
			this.key = key;
		}

		/**
		 * @return the time
		 */
		public String getTime() {
			return time;
		}

		/**
		 * @param time
		 *            the time to set
		 */
		public void setTime(String time) {
			this.time = time;
		}
	}
}

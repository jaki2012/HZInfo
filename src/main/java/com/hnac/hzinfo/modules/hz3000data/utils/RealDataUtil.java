package com.hnac.hzinfo.modules.hz3000data.utils;

import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hnac.hzinfo.common.config.Global;
import com.hnac.hzinfo.common.utils.SystemPath;
import com.hnac.hzinfo.modules.hz3000data.entity.BasePoint;
import com.hnac.hzinfo.modules.hz3000data.entity.DataGroup;
import com.hnac.hzinfo.modules.hz3000data.entity.Factory;
import com.hnac.hzinfo.modules.hz3000data.entity.NullPoint;
import com.hnac.hzinfo.modules.hz3000data.entity.SoePoint;
import com.hnac.hzinfo.modules.hz3000data.entity.YcPoint;
import com.hnac.hzinfo.modules.hz3000data.entity.YkPoint;
import com.hnac.hzinfo.modules.hz3000data.entity.YxPoint;

/**
 * 数据处理类
 * 
 * @author ypj
 *
 */
public class RealDataUtil {

	public static final String DATA_CACHE = "dataCache";
	public static final String ALL_FACTORIES_RECURSIVE = "all_factories_recursive";
	public static final String ALL_FACTORIES = "all_factories";
	public static final String FACTORY_ID = "factory_id_";
	public static final String GROUP_ID = "group_id_";
	public static final String POINT_ID = "point_id_";
	public static final String TELEMETRYPOINT_ID = "telemetry_point_id_";

	private static String DYNAMIC_FILE_PATH = null;
	private static String DYNAMIC_FILE_MAPPING_PATH = null;
	private static String DLL_PATH = null;
	private static String XML_SETTING_PATH = null;
	private static String READ_DLL_NAME = null;
	private static String WRITE_DLL_NAME = null;

	public static final int IS_NULL = 0;
	public static final int IS_YC = 1;
	public static final int IS_YX = 2;
	public static final int IS_YK = 3;
	public static final int IS_SOE = 4;

	public static List<Factory> datas = null;

	/**
	 * 得到场组点信息的配置文件的存放路径
	 * 
	 * @author ypj
	 * @return
	 */
	public static String getSettingPath() {
		if (XML_SETTING_PATH == null) {
			if (SystemPath.isLinux()) {
				XML_SETTING_PATH = Global.getConfig("hz3000.runtime.setting.linux");
			} else {
				XML_SETTING_PATH = Global.getConfig("hz3000.runtime.setting.windows");
			}

		}
		return XML_SETTING_PATH;
	}

	/**
	 * 得到hz3000的dll存储路径
	 * 
	 * @author ypj
	 * @return
	 */
	public static String getDllPath() {
		if (DLL_PATH == null) {
			if (SystemPath.isLinux()) {
				DLL_PATH = Global.getConfig("hz3000.dll.path.linux");
			} else {
				DLL_PATH = Global.getConfig("hz3000.dll.path.windows");
			}
		}
		return DLL_PATH;
	}

	/**
	 * 获取读数据模块名字
	 * 
	 * @author ypj
	 * @return
	 */
	public static String getReadDllName() {
		if (READ_DLL_NAME == null) {
			if (SystemPath.isLinux()) {
				READ_DLL_NAME = Global.getConfig("hz3000.read.dll.name.linux");
			} else {
				READ_DLL_NAME = Global.getConfig("hz3000.read.dll.name.windows");
			}
		}
		return READ_DLL_NAME;
	}

	/**
	 * 获取写数据模块名称
	 * 
	 * @author ypj
	 * @return
	 */
	public static String getWriteDllName() {
		if (WRITE_DLL_NAME == null) {
			if (SystemPath.isLinux()) {
				WRITE_DLL_NAME = Global.getConfig("hz3000.write.dll.name.linux");
			} else {
				WRITE_DLL_NAME = Global.getConfig("hz3000.write.dll.name.windows");
			}
		}
		return WRITE_DLL_NAME;
	}

	/**
	 * 得到动态页面文件存放位置
	 * 
	 * @author ypj
	 * @return
	 */
	public static String getDynamicFilePath() {
		if (DYNAMIC_FILE_PATH == null) {
			DYNAMIC_FILE_PATH = Global.getConfig("dynamic.file.path");
		}
		return DYNAMIC_FILE_PATH;
	}

	/**
	 * 得到动态页面文件寻址位置
	 * 
	 * @author ypj
	 * @return
	 */
	public static String getDynamicFileMappingPath() {
		if (DYNAMIC_FILE_MAPPING_PATH == null) {
			DYNAMIC_FILE_MAPPING_PATH = Global.getConfig("dynamic.file.mapping.path");
		}
		return DYNAMIC_FILE_MAPPING_PATH;
	}

	/**
	 * 按路径获得Document对象，低效率方法，不应多次调用
	 * 
	 * @author ypj
	 * @return
	 * @throws DocumentException
	 */
	private static Document getDocument() throws DocumentException {
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(new File(RealDataUtil.getSettingPath()));
		return document;
	}

	private static List<Factory> getDatas() {
		if (datas != null)
			return datas;
		else {
			try {
				datas = parseXMLSetting();
				return datas;
			} catch (Exception e) {
				Logger logger = LoggerFactory.getLogger(RealDataUtil.class);
				e.printStackTrace();
				logger.error(e.getMessage());
				datas = new ArrayList<Factory>();
				return datas;
			}
		}
	}

	/**
	 * 把工厂节点转化为一个工厂类
	 * 
	 * @author ypj
	 * @param factoryElement
	 * @return
	 */
	private static Factory generateFactory(Element factoryElement) {
		Factory factory = new Factory();
		for (@SuppressWarnings("unchecked")
		Iterator<Attribute> factoryAttributes = factoryElement.attributeIterator(); factoryAttributes.hasNext();) {
			Attribute factoryAttribute = factoryAttributes.next();
			switch (factoryAttribute.getName()) {
			case "StationNum":
				factory.setStationNum(factoryAttribute.getValue());
				break;
			case "Name":
				factory.setName(factoryAttribute.getValue());
				break;
			case "ID":
				factory.setId(factoryAttribute.getValue());
				break;
			case "TheGroupID":
				factory.setTheGroupId(factoryAttribute.getValue());
				break;
			case "TheGroupNum":
				factory.setStationNum(factoryAttribute.getValue());
				break;
			case "CHN":
				factory.setChn(factoryAttribute.getValue());
				break;
			case "ENG":
				factory.setEng(factoryAttribute.getValue());
				break;
			default:
				break;
			}
		}
		return factory;
	}

	/**
	 * 组信息节点转化为组类型
	 * 
	 * @author ypj
	 * @param groupElement
	 * @return
	 */
	private static DataGroup generateGroup(Element groupElement) {
		DataGroup dataGroup = new DataGroup();
		/* 解析数据组属性 */
		for (@SuppressWarnings("unchecked")
		Iterator<Attribute> groupAttributes = groupElement.attributeIterator(); groupAttributes.hasNext();) {
			Attribute groupAttribute = groupAttributes.next();
			switch (groupAttribute.getName()) {
			case "ID":
				dataGroup.setId(groupAttribute.getValue());
				break;
			case "Name":
				dataGroup.setName(groupAttribute.getValue());
				break;
			case "UnLock":
				if ("true".equals(groupAttribute.getValue().toLowerCase())) {
					dataGroup.setUnlock(true);
				} else {
					dataGroup.setUnlock(false);
				}
				break;
			case "CHN":
				dataGroup.setChn(groupAttribute.getValue());
				break;
			case "ENG":
				dataGroup.setEng(groupAttribute.getValue());
				break;
			default:
				break;
			}
		}
		return dataGroup;
	}

	/**
	 * 按节点生成遥测类
	 * 
	 * @author ypj
	 * @param ycPointElement
	 * @return
	 */
	private static YcPoint generateYcPoint(Element ycPointElement) {
		YcPoint telemetryPoint = new YcPoint();
		telemetryPoint.setDataType(IS_YC);
		for (@SuppressWarnings("unchecked")
		Iterator<Attribute> ycAttributes = ycPointElement.attributeIterator(); ycAttributes.hasNext();) {
			Attribute ycAttribute = ycAttributes.next();
			switch (ycAttribute.getName()) {
			case "ID":
				telemetryPoint.setId(ycAttribute.getValue());
				break;
			case "Name":
				telemetryPoint.setName(ycAttribute.getValue());
				break;
			case "CHN":
				telemetryPoint.setChn(ycAttribute.getValue());
				break;
			case "ENG":
				telemetryPoint.setEng(ycAttribute.getValue());
				break;
			case "Channel":
				telemetryPoint.setChannelIndex(ycAttribute.getValue());
				break;
			case "Module":
				telemetryPoint.setModuleIndex(ycAttribute.getValue());
				break;
			case "Dot":
				telemetryPoint.setAddressIndex(ycAttribute.getValue());
				break;
			default:
				break;
			}
		}
		return telemetryPoint;
	}

	/**
	 * 由节点得到遥信类
	 * 
	 * @author ypj
	 * @param yxPointElement
	 * @return
	 */
	private static YxPoint generateYxPoint(Element yxPointElement) {
		YxPoint yxPoint = new YxPoint();
		yxPoint.setDataType(IS_YX);
		for (@SuppressWarnings("unchecked")
		Iterator<Attribute> yxAttributes = yxPointElement.attributeIterator(); yxAttributes.hasNext();) {
			Attribute yxAttribute = yxAttributes.next();
			switch (yxAttribute.getName()) {
			case "ID":
				yxPoint.setId(yxAttribute.getValue());
				break;
			case "Name":
				yxPoint.setName(yxAttribute.getValue());
				break;
			case "YXAlarmType":
				yxPoint.setAlarmType(yxAttribute.getValue());
				break;
			case "Channel":
				yxPoint.setChannelIndex(yxAttribute.getValue());
				break;
			case "Module":
				yxPoint.setModuleIndex(yxAttribute.getValue());
				break;
			case "Dot":
				yxPoint.setAddressIndex(yxAttribute.getValue());
				break;
			default:
				break;
			}
		}
		return yxPoint;
	}

	/**
	 * 根据节点获得遥控点信息
	 * 
	 * @author ypj
	 * @param ykPointElement
	 * @return
	 */
	private static YkPoint generateYkPoint(Element ykPointElement) {
		YkPoint ykPoint = new YkPoint();
		ykPoint.setDataType(IS_YK);
		for (@SuppressWarnings("unchecked")
		Iterator<Attribute> ykAttributes = ykPointElement.attributeIterator(); ykAttributes.hasNext();) {
			Attribute ykAttribute = ykAttributes.next();
			switch (ykAttribute.getName()) {
			case "ID":
				ykPoint.setId(ykAttribute.getValue());
				break;
			case "Name":
				ykPoint.setName(ykAttribute.getValue());
				break;
			case "YKExplain":
				ykPoint.setExplain(ykAttribute.getValue());
				break;
			case "Channel":
				ykPoint.setChannelIndex(ykAttribute.getValue());
				break;
			case "Module":
				ykPoint.setModuleIndex(ykAttribute.getValue());
				break;
			case "Dot":
				ykPoint.setAddressIndex(ykAttribute.getValue());
				break;
			default:
				break;
			}
		}
		return ykPoint;
	}

	/**
	 * 根据节点获得实时警报类
	 * 
	 * @author ypj
	 * @param soePointElement
	 * @return
	 */
	private static SoePoint generateSoePoint(Element soePointElement) {
		SoePoint soePoint = new SoePoint();
		soePoint.setDataType(IS_SOE);
		for (@SuppressWarnings("unchecked")
		Iterator<Attribute> soeAttributes = soePointElement.attributeIterator(); soeAttributes.hasNext();) {
			Attribute soeAttribute = soeAttributes.next();
			switch (soeAttribute.getName()) {
			case "ID":
				soePoint.setId(soeAttribute.getValue());
				break;
			case "Name":
				soePoint.setName(soeAttribute.getValue());
				break;
			case "SOEType":
				soePoint.setType(soeAttribute.getValue());
				break;
			case "SOEAlarmType":
				soePoint.setAlarmType(soeAttribute.getValue());
				break;
			case "SOEAlarmSound":
				soePoint.setAlarmSound(soeAttribute.getValue());
				break;
			case "SoeActionNameList":
				soePoint.setActionNameList(soeAttribute.getValue());
				break;
			case "SoeActionUnitList":
				soePoint.setActionUnitList(soeAttribute.getValue());
				break;
			case "Channel":
				soePoint.setChannelIndex(soeAttribute.getValue());
				break;
			case "Module":
				soePoint.setModuleIndex(soeAttribute.getValue());
				break;
			case "Dot":
				soePoint.setAddressIndex(soeAttribute.getValue());
				break;
			default:
				break;
			}
		}
		return soePoint;
	}

	/**
	 * 初始化调用，解析HZ3000runtime配置文件 得到整个配置文件的实体类 转化为json时对应整个配置文件
	 * 
	 * @author ypj
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static List<Factory> parseXMLSetting() throws DocumentException, IOException {
		Document document = getDocument();
		Element root = document.getRootElement();
		/* 解析工厂信息 */
		List<Factory> factories = new ArrayList<Factory>();
		for (@SuppressWarnings("unchecked")
		Iterator<Element> factoryElements = root.elementIterator(); factoryElements.hasNext();) {
			Element factoryElement = factoryElements.next();
			if ("0".equals(factoryElement.valueOf("@ChildrenCount"))) {
				continue;
			}
			/* 解析工厂属性 */
			Factory factory = generateFactory(factoryElement);

			/* 解析数据组 */
			List<DataGroup> groupList = new ArrayList<DataGroup>();
			for (@SuppressWarnings("unchecked")
			Iterator<Element> groupElements = factoryElement.elementIterator(); groupElements.hasNext();) {
				Element groupElement = groupElements.next();
				if ("0".equals(groupElement.valueOf("@ChildrenCount"))) {
					continue;
				}
				DataGroup dataGroup = generateGroup(groupElement);

				/* 解析数据点 */
				for (@SuppressWarnings("unchecked")
				Iterator<Element> pointGroupElements = groupElement.elementIterator(); pointGroupElements.hasNext();) {
					Element pointGroupElement = pointGroupElements.next();
					if ("0".equals(pointGroupElement.valueOf("@ChildrenCount"))) {
						continue;
					}
					switch (pointGroupElement.getName()) {
					case "AIS":
						List<YcPoint> telemetryPointList = new ArrayList<YcPoint>();
						for (@SuppressWarnings("unchecked")
						Iterator<Element> telemetryElements = pointGroupElement.elementIterator(); telemetryElements
								.hasNext();) {
							Element telemetryElement = telemetryElements.next();
							YcPoint telemetryPoint = generateYcPoint(telemetryElement);
							telemetryPoint.setFactoryName(factory.getName());
							telemetryPoint.setFactoryId(factory.getId());
							telemetryPoint.setGroupName(dataGroup.getName());
							telemetryPoint.setGroupId(dataGroup.getId());
							telemetryPointList.add(telemetryPoint);
						}
						dataGroup.setTelemetryPointList(telemetryPointList);
						break;
					case "DIS":
						List<YxPoint> telesignalPointList = new ArrayList<YxPoint>();
						for (@SuppressWarnings("unchecked")
						Iterator<Element> telesignalElements = pointGroupElement.elementIterator(); telesignalElements
								.hasNext();) {
							Element telesignalElement = telesignalElements.next();
							YxPoint telesignalPoint = generateYxPoint(telesignalElement);
							telesignalPoint.setFactoryName(factory.getName());
							telesignalPoint.setFactoryId(factory.getId());
							telesignalPoint.setGroupName(dataGroup.getName());
							telesignalPoint.setGroupId(dataGroup.getId());
							telesignalPointList.add(telesignalPoint);
						}
						dataGroup.setTelesignalPointList(telesignalPointList);
						break;
					case "DOS":
						List<YkPoint> telecontrolPointList = new ArrayList<YkPoint>();
						for (@SuppressWarnings("unchecked")
						Iterator<Element> telecontrolElements = pointGroupElement.elementIterator(); telecontrolElements
								.hasNext();) {
							Element telecontrolElement = telecontrolElements.next();
							YkPoint telecontrolPoint = generateYkPoint(telecontrolElement);
							telecontrolPoint.setFactoryName(factory.getName());
							telecontrolPoint.setFactoryId(factory.getId());
							telecontrolPoint.setGroupName(dataGroup.getName());
							telecontrolPoint.setGroupId(dataGroup.getId());
							telecontrolPointList.add(telecontrolPoint);
						}
						dataGroup.setTelecontrolPointList(telecontrolPointList);
						break;
					case "SOES":
						List<SoePoint> eventPointList = new ArrayList<SoePoint>();
						for (@SuppressWarnings("unchecked")
						Iterator<Element> eventElements = pointGroupElement.elementIterator(); eventElements
								.hasNext();) {
							Element eventElement = eventElements.next();
							SoePoint eventPoint = generateSoePoint(eventElement);
							eventPoint.setFactoryName(factory.getName());
							eventPoint.setFactoryId(factory.getId());
							eventPoint.setGroupName(dataGroup.getName());
							eventPoint.setGroupId(dataGroup.getId());
							eventPointList.add(eventPoint);
						}
						dataGroup.setEventPointList(eventPointList);
						break;
					default:
						break;
					}

				}
				dataGroup.setFactoryId(factory.getId());
				groupList.add(dataGroup);
			}
			factory.setGroups(groupList);
			factories.add(factory);
		}
		return factories;
	}

	/**
	 * 得到工厂、站点列表,包含全信息 *
	 * 
	 * @author ypj
	 * @return
	 */
	public static List<Factory> getFactoriesEx() {
		return getDatas();
	}

	/**
	 * 得到工厂、站点列表,不包含全信息 *
	 * 
	 * @author ypj
	 * @return
	 */
	public static List<Factory> getFactories() {
		List<Factory> source = getFactoriesEx();
		List<Factory> list = new ArrayList<Factory>();
		for (int i = 0; i < source.size(); i++) {
			Factory fac = new Factory();
			fac.setName(source.get(i).getName());
			fac.setId(source.get(i).getId());
			list.add(fac);
		}
		return list;
	}

	/**
	 * 按工厂id获得组,包含全信息 *
	 * 
	 * @author ypj
	 * @param id
	 * @return
	 */
	public static List<DataGroup> getGroupByFactoryIdEx(String id) {
		List<Factory> data = getDatas();
		for (int i = 0; i < data.size(); i++) {
			if (id.equals(data.get(i).getId())) {
				List<DataGroup> ret = data.get(i).getGroups();
				if (ret == null)
					return new ArrayList<DataGroup>();
				else
					return data.get(i).getGroups();
			}
		}
		return new ArrayList<DataGroup>();
	}

	/**
	 * 按工厂id获得组,不含全信息 *
	 * 
	 * @author ypj
	 * @param id
	 * @return
	 */
	public static List<DataGroup> getGroupByFactoryId(String id) {
		List<DataGroup> source = getGroupByFactoryIdEx(id);
		List<DataGroup> list = new ArrayList<DataGroup>();
		for (int i = 0; i < source.size(); i++) {
			DataGroup fac = new DataGroup();
			fac.setName(source.get(i).getName());
			fac.setId(source.get(i).getId());
			list.add(fac);
		}
		return list;
	}

	/**
	 * 按组id获得组,包含全信息,不存在返回null *
	 * 
	 * @author ypj
	 * @param id
	 * @return
	 */
	public static DataGroup getGroupByIdEx(String id) {
		List<Factory> data = getDatas();
		for (int i = 0; i < data.size(); i++) {
			List<DataGroup> groups = datas.get(i).getGroups();
			for (int j = 0; j < groups.size(); j++) {
				if (id.equals(groups.get(j).getId())) {
					return groups.get(j);
				}
			}
		}
		return null;
	}

	/**
	 * 由组id得到遥测点
	 * 
	 * @author ypj
	 * @param id
	 * @return
	 */
	public static List<YcPoint> getYcPointByGroupId(String id) {
		List<Factory> data = getDatas();
		for (int i = 0; i < data.size(); i++) {
			List<DataGroup> groups = datas.get(i).getGroups();
			for (int j = 0; j < groups.size(); j++) {
				if (id.equals(groups.get(j).getId())) {
					List<YcPoint> ret = groups.get(j).getTelemetryPointList();
					if (ret == null)
						return new ArrayList<YcPoint>();
					else
						return ret;
				}
			}
		}
		return new ArrayList<YcPoint>();
	}

	public static YcPoint getYcPointById(String id) {
		List<Factory> data = getDatas();
		for (int i = 0; i < data.size(); i++) {
			List<DataGroup> groups = datas.get(i).getGroups();
			for (int j = 0; j < groups.size(); j++) {
				List<YcPoint> points = groups.get(j).getTelemetryPointList();
				for (int k = 0; k < points.size(); k++) {
					YcPoint point = points.get(k);
					if (point.getId().equals(id)) {
						return point;
					}
				}
			}
		}
		return new YcPoint();
	}

	/**
	 * 根据路径返回路径下文件名组成的列表
	 * 
	 * @author ypj
	 * @param path
	 * @return
	 */
	public static List<String> getFileMenuList(String path) {
		List<String> menuList = new ArrayList<String>();
		File file = new File(path);
		File[] fileList = file.listFiles();
		for (File singleFile : fileList) {
			if (!singleFile.isDirectory()) {
				menuList.add(singleFile.getName());
			}
		}
		Collections.sort(menuList, Collator.getInstance(java.util.Locale.CHINA));
		return menuList;
	}

	/**
	 * 根据id得到数据类型
	 * 
	 * @author ypj
	 * @param id
	 * @return
	 * @throws DocumentException
	 */
	public static int getDataType(String id) throws DocumentException {
		BasePoint point = getDataPointDef(id);
		if (point != null)
			return point.getDataType();
		else
			return RealDataUtil.IS_NULL;
	}

	/**
	 * 根据id得到厂组点信息
	 * 
	 * @author hx
	 * @param id
	 * @return
	 * @throws DocumentException
	 */
	public static BasePoint getDataPointDef(String id) throws DocumentException {
		List<Factory> data = getDatas();
		for (int i = 0; i < data.size(); i++) {
			List<DataGroup> groups = datas.get(i).getGroups();
			for (int j = 0; j < groups.size(); j++) {
				// soe
				List<SoePoint> soepoints = groups.get(j).getEventPointList();
				if (soepoints != null) {
					for (int k = 0; k < soepoints.size(); k++) {
						if (id.equals(soepoints.get(k).getId())) {
							BasePoint point = soepoints.get(k);
							point.setFactoryName(data.get(i).getName());
							point.setGroupName(groups.get(j).getName());

							return soepoints.get(k);
						}
					}
				}
				// yk
				List<YkPoint> ykpoints = groups.get(j).getTelecontrolPointList();
				if (ykpoints != null) {
					for (int k = 0; k < ykpoints.size(); k++) {
						if (id.equals(ykpoints.get(k).getId())) {
							BasePoint point = ykpoints.get(k);
							point.setFactoryName(data.get(i).getName());
							point.setGroupName(groups.get(j).getName());
							return ykpoints.get(k);
						}
					}
				}
				// yc
				List<YcPoint> ycpoints = groups.get(j).getTelemetryPointList();
				if (ycpoints != null) {
					for (int k = 0; k < ycpoints.size(); k++) {
						if (id.equals(ycpoints.get(k).getId())) {
							BasePoint point = ycpoints.get(k);
							point.setFactoryName(data.get(i).getName());
							point.setGroupName(groups.get(j).getName());
							return ycpoints.get(k);
						}
					}
				}
				// yx
				List<YxPoint> yxpoints = groups.get(j).getTelesignalPointList();
				if (yxpoints != null) {
					for (int k = 0; k < yxpoints.size(); k++) {
						if (id.equals(yxpoints.get(k).getId())) {
							BasePoint point = yxpoints.get(k);
							point.setFactoryName(data.get(i).getName());
							point.setGroupName(groups.get(j).getName());
							return yxpoints.get(k);
						}
					}
				}
			}
		}
		NullPoint point = new NullPoint();
		return point;
	}

	/**
	 * 根据通道、模块、地址和遥信遥测类型得到点信息
	 * 
	 * @author ypj
	 * @param channel
	 * @param module
	 * @param address
	 * @param type
	 * @return
	 */
	public static BasePoint getDataPoint(long channel, int module, int address, int type) {
		List<Factory> data = getDatas();
		String channelString = channel + "";
		String moduleString = module + "";
		String addressString = address + "";
		for (int i = 0; i < data.size(); i++) {
			List<DataGroup> groups = datas.get(i).getGroups();
			for (int j = 0; j < groups.size(); j++) {
				switch (type) {
				case 0:
					List<YcPoint> ycpoints = groups.get(j).getTelemetryPointList();
					if (ycpoints != null) {
						for (int k = 0; k < ycpoints.size(); k++) {
							YcPoint point = ycpoints.get(k);
							if (channelString.equals(point.getChannelIndex())
									&& moduleString.equals(point.getModuleIndex())
									&& addressString.equals(point.getAddressIndex())) {
								return point;
							}
						}
					}
				case 1:
					List<YxPoint> yxpoints = groups.get(j).getTelesignalPointList();
					if (yxpoints != null) {
						for (int k = 0; k < yxpoints.size(); k++) {
							YxPoint point = yxpoints.get(k);
							if (channelString.equals(point.getChannelIndex())
									&& moduleString.equals(point.getModuleIndex())
									&& addressString.equals(point.getAddressIndex())) {
								return point;
							}
						}
					}
				}
			}
		}
		NullPoint point = new NullPoint();
		return point;
	}

	/**
	 * @param id
	 * @return
	 */
	public static List<YxPoint> getYxPointByGroupId(String id) {
		List<Factory> data = getDatas();
		for (int i = 0; i < data.size(); i++) {
			List<DataGroup> groups = datas.get(i).getGroups();
			for (int j = 0; j < groups.size(); j++) {
				if (id.equals(groups.get(j).getId())) {
					List<YxPoint> ret = groups.get(j).getTelesignalPointList();
					if (ret == null)
						return new ArrayList<YxPoint>();
					else
						return ret;
				}
			}
		}
		return new ArrayList<YxPoint>();
	}

	/**
	 * @param id
	 * @return
	 */
	public static YxPoint getYxPointById(String id) {
		List<Factory> data = getDatas();
		for (int i = 0; i < data.size(); i++) {
			List<DataGroup> groups = datas.get(i).getGroups();
			for (int j = 0; j < groups.size(); j++) {
				List<YxPoint> points = groups.get(j).getTelesignalPointList();
				for (int k = 0; k < points.size(); k++) {
					YxPoint point = points.get(k);
					if (point.getId().equals(id)) {
						return point;
					}
				}
			}
		}
		return new YxPoint();
	}

}

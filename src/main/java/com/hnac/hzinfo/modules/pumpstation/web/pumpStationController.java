package com.hnac.hzinfo.modules.pumpstation.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hnac.hzinfo.common.utils.DataGridModel;
import com.hnac.hzinfo.common.utils.IdGen;
import com.hnac.hzinfo.common.web.BaseController;
import com.hnac.hzinfo.modules.hz3000data.entity.DataGroup;
import com.hnac.hzinfo.modules.hz3000data.entity.Factory;
import com.hnac.hzinfo.modules.hz3000data.entity.YcPoint;
import com.hnac.hzinfo.modules.hz3000data.entity.YxPoint;
import com.hnac.hzinfo.modules.hz3000data.utils.RealDataUtil;
import com.hnac.hzinfo.modules.pumpstation.entity.DataBind;
import com.hnac.hzinfo.modules.pumpstation.entity.PumpEquipment;
import com.hnac.hzinfo.modules.pumpstation.entity.PumpLine;
import com.hnac.hzinfo.modules.pumpstation.entity.PumpPipe;
import com.hnac.hzinfo.modules.pumpstation.entity.PumpStation;
import com.hnac.hzinfo.modules.pumpstation.entity.PumpUnit;

/**
 * 用户Controller
 * 
 * @author Hx
 * @version 2017-3-23
 */
@Controller
@RequestMapping(value = "/pumpStation")
public class pumpStationController extends BaseController {
	@RequestMapping(value = "/runningChartByDay")
	public String runningChartByDay(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pumpstation/runningChartByDay";
	}

	@RequestMapping(value = "/runningChartByMonth")
	public String runningChartByMonth(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pumpstation/runningChartByMonth";
	}

	@RequestMapping(value = "/runningChartByYear")
	public String runningChartByYear(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pumpstation/runningChartByYear";
	}

	@RequestMapping(value = "/runningGridByMonth")
	public String runningGridByMonth(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pumpstation/runningGridByMonth";
	}

	@RequestMapping(value = "/runningGridByYear")
	public String runningGridByYear(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pumpstation/runningGridByYear";
	}

	@RequestMapping(value = "/stationGridByDay")
	public String stationGridByDay(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pumpstation/stationGridByDay";
	}

	@RequestMapping(value = "/stationGridByMonth")
	public String stationGridByMonth(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pumpstation/stationGridByMonth";
	}


	@RequestMapping(value = "/hydrograph")
	public String hydrograph(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pumpstation/hydrograph";
	}

	@RequestMapping(value = "/stationEdit")
	public String stationEdit() {
		return "modules/pumpstation/stationEdit";
	}

	@RequestMapping(value = "/stationManage")
	public String stationManage() {
		return "modules/pumpstation/stationManage";
	}

	@RequestMapping(value = "/stationView")
	public String stationView() {
		return "modules/pumpstation/stationView";
	}

	@RequestMapping(value = "/unitManage")
	public String unitManage() {
		return "modules/pumpstation/unitManage";
	}

	@RequestMapping(value = "/unitEdit")
	public String unitEdit() {
		return "modules/pumpstation/unitEdit";
	}

	@RequestMapping(value = "/unitView")
	public String unitView() {
		return "modules/pumpstation/unitView";
	}

	@RequestMapping(value = "/pipeManage")
	public String pipeManage() {
		return "modules/pumpstation/pipeManage";
	}

	@RequestMapping(value = "/pipeEdit")
	public String pipeEdit() {
		return "modules/pumpstation/pipeEdit";
	}
	
	@RequestMapping(value = "/pipeView")
	public String pipeView() {
		return "modules/pumpstation/pipeView";
	}

	@RequestMapping(value = "/lineManage")
	public String lineManage() {
		return "modules/pumpstation/lineManage";
	}

	@RequestMapping(value = "/lineEdit")
	public String lineEdit() {
		return "modules/pumpstation/lineEdit";
	}

	@RequestMapping(value = "/equipmentManage")
	public String equipmentManage() {
		return "modules/pumpstation/epuipmentManage";
	}

	@RequestMapping(value = "/equipmentEdit")
	public String equipmentEdit() {
		return "modules/pumpstation/equipmentEdit";
	}

	@RequestMapping(value = "/equipmentView")
	public String equipmentView() {
		return "modules/pumpstation/equipmentView";
	}

	@RequestMapping(value = "/dataBind")
	public String dataBind() {
		return "modules/pumpstation/dataBind";
	}

	/************************************************************************************************************/

	@RequestMapping(value = "/getStationList", method = RequestMethod.POST)
	@ResponseBody
	public DataGridModel<pumpstation> getStationList() {
		try {
			List<pumpstation> list = new ArrayList<pumpstation>();
			list.add(new pumpstation("0001", "泵站1"));
			list.add(new pumpstation("0002", "泵站2"));
			list.add(new pumpstation("0003", "泵站3"));
			DataGridModel<pumpstation> ret = new DataGridModel<pumpstation>();
			ret.setTotal(list.size());
			ret.setRows(list);
			return ret;
		} catch (Exception ex) {

		}
		return new DataGridModel<pumpstation>();
	}

	@RequestMapping(value = "/getStation", method = RequestMethod.POST)
	@ResponseBody
	public DataGridModel<PumpStation> getStation() {
		DataGridModel<PumpStation> stations = new DataGridModel<PumpStation>();
		try {
			List<PumpStation> list = new ArrayList<PumpStation>();
			for (int i = 0; i < 5; i++) {
				list.add(generateStationWithUnitList());
			}
			stations.setRows(list);
			stations.setTotal(list.size());
		} catch (Exception ex) {

		}
		return stations;
	}

	@RequestMapping(value = "/getUnitByStationId", method = RequestMethod.POST)
	@ResponseBody
	public DataGridModel<PumpUnit> getUnitByStationId() {
		DataGridModel<PumpUnit> units = new DataGridModel<PumpUnit>();
		try {
			List<PumpUnit> list = new ArrayList<PumpUnit>();
			for (int i = 0; i < 100; i++) {
				PumpUnit unit = generateUnit();
				unit.setCode("" + (i + 1));
				list.add(unit);
			}
			units.setRows(list);
			units.setTotal(list.size());
		} catch (Exception ex) {

		}
		return units;
	}

	@RequestMapping(value = "/getPipeByUnitId", method = RequestMethod.POST)
	@ResponseBody
	public DataGridModel<PumpPipe> getPipeByUnitId() {
		DataGridModel<PumpPipe> result = new DataGridModel<PumpPipe>();
		try {
			List<PumpPipe> list = new ArrayList<PumpPipe>();
			for (int i = 0; i < 100; i++) {
				PumpPipe object = generatePipe();
				list.add(object);
			}
			result.setRows(list);
			result.setTotal(list.size());
		} catch (Exception ex) {

		}
		return result;
	}

	@RequestMapping(value = "/getPipeByStationId", method = RequestMethod.POST)
	@ResponseBody
	public DataGridModel<PumpPipe> getPipeByStationId() {
		DataGridModel<PumpPipe> result = new DataGridModel<PumpPipe>();
		try {
			List<PumpPipe> list = new ArrayList<PumpPipe>();
			for (int i = 0; i < 100; i++) {
				PumpPipe object = generatePipe();
				list.add(object);
			}
			result.setRows(list);
			result.setTotal(list.size());
		} catch (Exception ex) {

		}
		return result;
	}

	@RequestMapping(value = "/getLineByStationId", method = RequestMethod.POST)
	@ResponseBody
	public DataGridModel<PumpLine> getLineByStationId() {
		DataGridModel<PumpLine> result = new DataGridModel<PumpLine>();
		try {
			List<PumpLine> list = new ArrayList<PumpLine>();
			for (int i = 0; i < 100; i++) {
				PumpLine object = generateLine();
				list.add(object);
			}
			result.setRows(list);
			result.setTotal(list.size());
		} catch (Exception ex) {

		}
		return result;
	}

	@RequestMapping(value = "/getEquipmentByStationId", method = RequestMethod.POST)
	@ResponseBody
	public DataGridModel<PumpEquipment> getEquipmentByStationId() {
		DataGridModel<PumpEquipment> result = new DataGridModel<PumpEquipment>();
		try {
			List<PumpEquipment> list = new ArrayList<PumpEquipment>();
			for (int i = 0; i < 100; i++) {
				PumpEquipment object = generateEquipment();
				list.add(object);
			}
			result.setRows(list);
			result.setTotal(list.size());
		} catch (Exception ex) {

		}
		return result;
	}

	/**
	 * @author ypj
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getDataBindById", method = RequestMethod.POST)
	@ResponseBody
	public DataGridModel<DataBind> getDataBindById(String id, boolean isStation) {
		DataGridModel<DataBind> result = new DataGridModel<DataBind>();
		try {
			//Random random = new Random();
			List<DataBind> list = new ArrayList<DataBind>();
			int number = 1;
			if (isStation) {
				number = 3;
			}
			for (int i = 0; i < number; i++) {
				DataBind object = generateDataBind(id, isStation);
				switch (i) {
				case 0:
					if(isStation){
						object.setName("前池水位");
					}else{
						object.setName("开机遥信");
					}
					break;
				case 1:
					object.setName("后池水位");;
					break;
				case 2:
					object.setName("流量");;
					break;
				}
				list.add(object);
			}
			result.setRows(list);
			result.setTotal(list.size());
		} catch (Exception ex) {

		}
		return result;
	}

	/**
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
	public List<DataGroup> getGroupByFactoryid(String id) {
		return RealDataUtil.getGroupByFactoryId(id);
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
	@RequestMapping("/getYcPointByGroupId")
	public List<YcPoint> getYcPointByGroupId(String id) {
		List<YcPoint> list = null;
		try {
			list = RealDataUtil.getYcPointByGroupId(id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return list;
	}

	/**
	 * 由数据组id获得遥控列表
	 * 
	 * @author ypj
	 * @param factoryId
	 *            厂、站id
	 * @return 组序列
	 */
	@ResponseBody
	@RequestMapping("/getYxPointbygroupid")
	public List<YxPoint> getYxPointbygroupid(String id) {
		List<YxPoint> list = null;
		try {
			list = RealDataUtil.getYxPointByGroupId(id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return list;
	}

	/**
	 * @author ypj
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getYcPointById")
	public YcPoint getYcPointById(String id) {
		return RealDataUtil.getYcPointById(id);
	}

	/**
	 * @author ypj
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getYxPointById")
	public YxPoint getYxPointById(String id) {
		return RealDataUtil.getYxPointById(id);
	}

	/****************************************************** 模拟数据 *********************************************************/

	class pumpstation {
		// 这是一个提供给第三方演示而临时定义的类，真正实现时应删除该类
		String ID;
		String Name;

		public String getId() {
			return ID;
		}

		public String getName() {
			return Name;
		}

		public pumpstation(String id, String name) {
			ID = id;
			Name = name;
		}
	}

	PumpStation generateStation() {
		PumpStation station = new PumpStation();
		Date date = new Date();
		Random random = new Random();
		station.setId(IdGen.uuid());
		station.setName("泵站" + (date.getTime() - random.nextInt(8000)));
		station.setAuxiliaryHouse(random.nextFloat() * 1000);
		station.setBuildDate(new Date(date.getTime() - random.nextInt(600000)));
		station.setCode("" + (date.getTime() - random.nextInt(8000)));
		station.setDesignedDelivery(random.nextFloat() * 1000);
		station.setInPoolShape("圆形" + random.nextInt(3000));
		station.setInPoolSize(random.nextInt(100) + "×" + random.nextInt(100) + "×" + random.nextInt(100));
		station.setInPoolVolume(random.nextFloat() * 1000);
		station.setInstalledPower(random.nextFloat() * 1000);
		station.setManageHouse(random.nextFloat() * 1000);
		station.setMainHouse(random.nextFloat() * 1000);
		station.setNetHead(random.nextFloat() * 1000);
		station.setOutPoolShape("方形");
		station.setOutPoolSize(random.nextInt(100) + "×" + random.nextInt(100) + "×" + random.nextInt(100));
		station.setOutPoolVolume(random.nextFloat() * 1000);
		station.setPosition("山上" + date.getTime());
		station.setRealDelivery(random.nextFloat() * 1000);
		station.setRebuildDate(new Date(date.getTime() - random.nextInt(600000)));
		station.setTotalHead(random.nextFloat() * 1000);
		station.setTotalUnit(random.nextInt(300));
		return station;
	}

	PumpStation generateStationWithUnitList() {
		PumpStation station = new PumpStation();
		Date date = new Date();
		Random random = new Random();
		station.setId(IdGen.uuid());
		station.setName("泵站" + (date.getTime() - random.nextInt(8000)));
		station.setAuxiliaryHouse(random.nextFloat() * 1000);
		station.setBuildDate(new Date(date.getTime() - random.nextInt(600000)));
		station.setCode("" + (date.getTime() - random.nextInt(8000)));
		station.setDesignedDelivery(random.nextFloat() * 1000);
		station.setInPoolShape("圆形" + random.nextInt(3000));
		station.setInPoolSize(random.nextInt(100) + "×" + random.nextInt(100) + "×" + random.nextInt(100));
		station.setInPoolVolume(random.nextFloat() * 1000);
		station.setInstalledPower(random.nextFloat() * 1000);
		station.setManageHouse(random.nextFloat() * 1000);
		station.setMainHouse(random.nextFloat() * 1000);
		station.setNetHead(random.nextFloat() * 1000);
		station.setOutPoolShape("方形");
		station.setOutPoolSize(random.nextInt(100) + "×" + random.nextInt(100) + "×" + random.nextInt(100));
		station.setOutPoolVolume(random.nextFloat() * 1000);
		station.setPosition("山上" + date.getTime());
		station.setRealDelivery(random.nextFloat() * 1000);
		station.setRebuildDate(new Date(date.getTime() - random.nextInt(600000)));
		station.setTotalHead(random.nextFloat() * 1000);
		station.setTotalUnit(random.nextInt(300));
		List<PumpUnit> list = new ArrayList<PumpUnit>();
		for (int i = 0; i < 8; i++) {
			list.add(generateUnit(station.getId()));
		}
		station.setUnitList(list);
		return station;
	}

	PumpUnit generateUnit(String stationId) {
		PumpUnit unit = new PumpUnit();
		Date date = new Date();
		Random random = new Random();
		long sign = date.getTime();
		unit.setCode("" + (date.getTime() - random.nextInt(8000)));
		unit.setName("机组" + random.nextInt(600));
		unit.setCurrent(random.nextFloat() * 1000);
		unit.setFlowRate(random.nextFloat() * 1000);
		unit.setHead(random.nextFloat() * 1000);
		unit.setId(IdGen.uuid());
		unit.setImpellerDiameter(random.nextInt(3000));
		unit.setImpellerHole(random.nextInt(unit.getImpellerDiameter()));
		unit.setMotorCommissioningDate(new Date(sign - random.nextInt(80000)));
		unit.setMotorManufactureDate(new Date(sign - random.nextInt(80000)));
		unit.setMotorManufacturer("厂家" + random.nextInt(6000));
		unit.setMotorModel("电机" + random.nextInt(600));
		unit.setPower(random.nextFloat() * 1000);
		unit.setPumpBearing(random.nextInt(unit.getImpellerHole()));
		unit.setPumpCommissioningDate(new Date(sign - random.nextInt(80000)));
		unit.setPumpManufactureDate(new Date(sign - random.nextInt(80000)));
		unit.setPumpManufacturer("厂家" + random.nextInt(3000));
		unit.setPumpModel("泵" + random.nextInt(300));
		unit.setRotateSpeed(random.nextInt(6000));
		unit.setStationId(stationId);
		unit.setType(random.nextInt(3));
		unit.setVacuumDegree(random.nextFloat() * 1000);
		unit.setValveIn("进水阀" + random.nextInt(300));
		unit.setValveOut("出水阀 " + random.nextInt(300));
		return unit;
	}

	PumpUnit generateUnit() {
		PumpUnit unit = new PumpUnit();
		Date date = new Date();
		Random random = new Random();
		long sign = date.getTime();

		unit.setCurrent(random.nextFloat() * 1000);
		unit.setFlowRate(random.nextFloat() * 1000);
		unit.setHead(random.nextFloat() * 1000);
		unit.setCode(sign - random.nextInt(8000000) + "");
		unit.setName("机组" + random.nextInt(600));
		unit.setId(IdGen.uuid());
		unit.setImpellerDiameter(random.nextInt(3000));
		unit.setImpellerHole(random.nextInt(unit.getImpellerDiameter()));
		unit.setMotorCommissioningDate(new Date(sign - random.nextInt(80000)));
		unit.setMotorManufactureDate(new Date(sign - random.nextInt(80000)));
		unit.setMotorManufacturer("厂家" + random.nextInt(6000));
		unit.setMotorModel("电机" + random.nextInt(600));
		unit.setPower(random.nextFloat() * 1000);
		unit.setPumpBearing(random.nextInt(unit.getImpellerHole()));
		unit.setPumpCommissioningDate(new Date(sign - random.nextInt(80000)));
		unit.setPumpManufactureDate(new Date(sign - random.nextInt(80000)));
		unit.setPumpManufacturer("厂家" + random.nextInt(3000));
		unit.setPumpModel("泵" + random.nextInt(300));
		unit.setRotateSpeed(random.nextInt(6000));
		unit.setType(random.nextInt(3));
		unit.setVacuumDegree(random.nextFloat() * 1000);
		unit.setValveIn("进水阀" + random.nextInt(300));
		unit.setValveOut("出水阀 " + random.nextInt(300));
		return unit;
	}

	PumpPipe generatePipe() {
		PumpPipe pipe = new PumpPipe();
		Date date = new Date();
		Random random = new Random();
		long sign = date.getTime();
		pipe.setDiameter(random.nextInt(600));
		pipe.setId(IdGen.uuid());
		pipe.setInstalldate(new Date(sign - random.nextInt(8000000)));
		pipe.setLength(random.nextFloat());
		pipe.setMaterial("材质" + random.nextInt(100));
		pipe.setStationId(IdGen.uuid());
		pipe.setType(random.nextInt(4));
		pipe.setUnitId(IdGen.uuid());
		return pipe;
	}

	PumpLine generateLine() {
		PumpLine line = new PumpLine();
		Random random = new Random();

		line.setLength(random.nextFloat() * 100);
		line.setLineModel("型号" + random.nextInt(300));
		line.setStationId(IdGen.uuid());
		line.setUnitId(IdGen.uuid());

		return line;
	}

	PumpEquipment generateEquipment() {
		PumpEquipment equipment = new PumpEquipment();
		Date date = new Date();
		Random random = new Random();
		long sign = date.getTime();
		String[] cites = { "长沙", "九江", "北京", "广州", "上海", "扬州", "兰州" };

		equipment.setId(IdGen.uuid());
		equipment.setInstalldate(new Date(sign - random.nextInt(8000000)));
		equipment.setManufactureDate(new Date(sign - random.nextInt(8000000)));
		equipment.setModel("型号" + random.nextInt(300));
		equipment.setNumber(1 + random.nextInt(300));
		equipment.setName("机电设备" + random.nextInt(600));
		equipment.setProductionPlace(cites[random.nextInt(7)]);
		equipment.setStationId(IdGen.uuid());
		equipment.setType(random.nextInt(3));
		return equipment;
	}

	/**
	 * @param id
	 * @return
	 */
	private DataBind generateDataBind(String id, boolean isStation) {
		DataBind data = new DataBind();
		Random random = new Random();
		data.setObjectId(id);
		List<Factory> factories = RealDataUtil.getFactoriesEx();
		Factory factory = factories.get(random.nextInt(factories.size()));
		List<DataGroup> groups = factory.getGroups();
		DataGroup group = groups.get(random.nextInt(groups.size()));
		if (isStation) {
			List<YcPoint> points = group.getTelemetryPointList();
			data.setRealId(points.get(random.nextInt(points.size())).getId());
		} else {
			List<YxPoint> points = group.getTelesignalPointList();
			data.setRealId(points.get(random.nextInt(points.size())).getId());
		}
		data.setType(random.nextInt(3));
		data.setId(IdGen.uuid());
		return data;
	}
}

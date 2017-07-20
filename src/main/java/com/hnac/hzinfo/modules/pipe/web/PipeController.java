package com.hnac.hzinfo.modules.pipe.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/pipe")
public class PipeController {

	@RequestMapping(value = "/chargeEdit")
	public String chargeEdit() {
		return "modules/pipe/chargeEdit";
	}

	@RequestMapping(value = "/chargeManage")
	public String chargeManage() {
		return "modules/pipe/chargeManage";
	}

	@RequestMapping(value = "/equipmentEdit")
	public String equipmentEdit() {
		return "modules/pipe/equipmentEdit";
	}

	@RequestMapping(value = "/equipmentManage")
	public String equipmentManage() {
		return "modules/pipe/equipmentManage";
	}

	@RequestMapping(value = "/equipmentBindData")
	public String equipmentBindData() {
		return "modules/pipe/equipmentBindData";
	}
	
	@RequestMapping(value = "/infoEdit")
	public String infoEdit() {
		return "modules/pipe/infoEdit";
	}

	@RequestMapping(value = "/infoManage")
	public String infoManage() {
		return "modules/pipe/infoManage";
	}

	@RequestMapping(value = "/maintainrecordEdit")
	public String maintainrecordEdit() {
		return "modules/pipe/maintainrecordEdit";
	}

	@RequestMapping(value = "/maintainrecordManage")
	public String maintainrecordManage() {
		return "modules/pipe/maintainrecordManage";
	}

	@RequestMapping(value = "/maintainrecordView")
	public String maintainrecordView() {
		return "modules/pipe/maintainrecordView";
	}

	@RequestMapping(value = "/pipeEdit")
	public String pipeEdit() {
		return "modules/pipe/pipeEdit";
	}

	@RequestMapping(value = "/pipeManage")
	public String pipeManage() {
		return "modules/pipe/pipeManage";
	}

	@RequestMapping(value = "/pollingEdit")
	public String pollingEdit() {
		return "modules/pipe/pollingEdit";
	}

	@RequestMapping(value = "/pollingManage")
	public String pollingManage() {
		return "modules/pipe/pollingManage";
	}

	@RequestMapping(value = "/pollingView")
	public String pollingView() {
		return "modules/pipe/pollingView";
	}

	@RequestMapping(value = "/processlineChartByDay")
	public String processlineChartByDay() {
		return "modules/pipe/processlineChartByDay";
	}

	@RequestMapping(value = "/processlineChartByMonth")
	public String processlineChartByMonth() {
		return "modules/pipe/processlineChartByMonth";
	}

	@RequestMapping(value = "/processlineChartByYear")
	public String processlineChartByYear() {
		return "modules/pipe/processlineChartByYear";
	}

	@RequestMapping(value = "/procurementEdit")
	public String procurementEdit() {
		return "modules/pipe/procurementEdit";
	}

	@RequestMapping(value = "/procurementManage")
	public String procurementManage() {
		return "modules/pipe/procurementManage";
	}

	@RequestMapping(value = "/procurementView")
	public String procurementView() {
		return "modules/pipe/procurementView";
	}

	@RequestMapping(value = "/procurementList")
	public String procurementList() {
		return "modules/pipe/procurementList";
	}

	@RequestMapping(value = "/stationData")
	public String stationData() {
		return "modules/pipe/stationData";
	}

	@RequestMapping(value = "/stationGraph")
	public String stationGraph() {
		return "modules/pipe/stationGraph";
	}

	@RequestMapping(value = "/trendChart")
	public String trendChart() {
		return "modules/pipe/trendChart";
	}

	@RequestMapping(value = "/trendChartByMonth")
	public String trendChartByMonth() {
		return "modules/pipe/trendChartByMonth";
	}

	@RequestMapping(value = "/unitEdit")
	public String unitEdit() {
		return "modules/pipe/unitEdit";
	}

	@RequestMapping(value = "/unitManage")
	public String unitManage() {
		return "modules/pipe/unitManage";
	}

	@RequestMapping(value = "/watercutEdit")
	public String watercutEdit() {
		return "modules/pipe/watercutEdit";
	}

	@RequestMapping(value = "/watercutManage")
	public String watercutManage() {
		return "modules/pipe/watercutManage";
	}

	@RequestMapping(value = "/watercutView")
	public String watercutView() {
		return "modules/pipe/watercutView";
	}

	@RequestMapping(value = "/watersettleManage")
	public String watersettleManage() {
		return "modules/pipe/watersettleManage";
	}

	@RequestMapping(value = "/watersettleView")
	public String watersettleView() {
		return "modules/pipe/watersettleView";
	}

	@RequestMapping(value = "/watermanageinput")
	public String watermanageinput(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pipe/watermanageinput";
	}

	@RequestMapping(value = "/watermanageshow")
	public String watermanageshow(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pipe/watermanageshow";
	}

	@RequestMapping(value = "/waterdistriinput")
	public String waterdistriinput(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pipe/waterdistriinput";
	}

	@RequestMapping(value = "/waterdistrishow")
	public String watedistrishow(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pipe/waterdistrishow";
	}

	@RequestMapping(value = "/waterusepreinput")
	public String waterusepreinput(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pipe/waterusepreinput";
	}

	@RequestMapping(value = "/waterusepreshow")
	public String wateusepreshow(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pipe/waterusepreshow";
	}

	@RequestMapping(value = "/waterusepreyearinput")
	public String waterusepreyearinput(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pipe/waterusepreyearinput";
	}

	@RequestMapping(value = "/waterusepreyearshow")
	public String wateusepreyearshow(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pipe/waterusepreyearshow";
	}

	@RequestMapping(value = "/waterusepremonthshow")
	public String wateusepremonthshow(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pipe/waterusepremonthshow";
	}

	@RequestMapping(value = "/watermanageruleinput")
	public String watermanageruleinput(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pipe/watermanageruleinput";
	}

	@RequestMapping(value = "/watermanageruleshow")
	public String watermanageruleshow(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pipe/watermanageruleshow";
	}

	@RequestMapping(value = "/waterusemonthquery")
	public String waterusemonthquery(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pipe/waterusemonthquery";
	}

	@RequestMapping(value = "/waterusemonthperiodinput")
	public String waterusemonthperiodinput(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pipe/waterusemonthperiodinput";
	}

	@RequestMapping(value = "/waterusemonthperiodcheck")
	public String waterusemonthperiodcheck(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pipe/waterusemonthperiodcheck";
	}

	@RequestMapping(value = "/waterusepreyearanalyse")
	public String waterusepreyearanalyse(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pipe/waterusepreyearanalyse";
	}

	@RequestMapping(value = "/waterusepremonthanalyse")
	public String waterusepremonthanalyse(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pipe/waterusepremonthanalyse";
	}

	@RequestMapping(value = "/wateruseyearanalyse")
	public String wateruseyearanalyse(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pipe/wateruseyearanalyse";
	}

	@RequestMapping(value = "/waterusemonthanalyse")
	public String waterusemonthanalyse(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pipe/waterusemonthanalyse";
	}

	@RequestMapping(value = "/waterusefacmanage")
	public String waterusefacmanage(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pipe/waterusefacmanage";
	}

	@RequestMapping(value = "/waterReservoirInfo")
	public String waterReservoirInfo(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pipe/waterReservoirInfo";
	}

	@RequestMapping(value = "/waterReservoirline")
	public String waterReservoirline(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pipe/waterReservoirline";
	}

	@RequestMapping(value = "/waterReservoirShow")
	public String waterReservoirShow(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pipe/waterReservoirShow";
	}
}

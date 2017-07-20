package com.hnac.hzinfo.modules.pumpstation.entity;

import java.util.List;

public class PumpStation {
	private java.lang.String id;
	/** @pdOid e6842d37-31a2-45f4-b703-1cc90afdd886 */
	private java.lang.String code;
	/** @pdOid 652e2577-5848-4dc9-ab0a-92da2312dcbe */
	private java.lang.String name;
	/** @pdOid 2351fa8e-edd1-4bf0-8757-486ba853a258 */
	private float installedPower;
	/** @pdOid 6854e6fa-0b45-4ce0-9f01-60bf37d65c40 */
	private int totalUnit;
	/** @pdOid 3bd97e49-fe34-486c-89a5-ed90514c6335 */
	private float totalHead;
	/** @pdOid 75a33046-ddd9-4008-b635-9ec0993799ec */
	private float netHead;
	/** @pdOid dfeb9952-ae88-480c-a400-de1ec9653952 */
	private float designedDelivery;
	/** @pdOid 3662e6bd-a094-441a-9b48-996397182450 */
	private float realDelivery;
	/** @pdOid d8f04cdb-c559-40ba-9124-583ac3fce24e */
	private java.lang.String position;
	/** @pdOid ee41fef9-915b-4a41-96a8-e0bd6cfe347f */
	private java.util.Date buildDate;
	/** @pdOid 3c878f29-9c33-407b-90c1-a017cacf53f4 */
	private java.util.Date rebuildDate;
	/** @pdOid 6c4014cb-210c-4e7a-9422-2653befa8f45 */
	private float mainHouse;
	/** @pdOid 6579fa87-49b5-4d23-837c-cce334019c41 */
	private float auxiliaryHouse;
	/** @pdOid a082a2a8-aa3b-48fa-8e0b-161b14dead67 */
	private float manageHouse;
	/** @pdOid af95fce8-b5e1-41a4-a2fb-91fd27e58b6e */
	private java.lang.String inPoolShape;
	/** @pdOid a6ac36ca-4313-459c-9fa7-c04de5d2edf1 */
	private java.lang.String inPoolSize;
	/** @pdOid 2979da37-0182-49d7-b237-227f21674afa */
	private float inPoolVolume;
	/** @pdOid 6ef5dc4c-6f48-491e-9c5c-747cad1c3c46 */
	private java.lang.String outPoolShape;
	/** @pdOid 940f7cdd-42be-45f1-bb75-a5af40ce76fc */
	private java.lang.String outPoolSize;
	/** @pdOid c6ccf423-293a-43b8-ac51-24ac45a6b097 */
	private float outPoolVolume;

	private List<PumpUnit> unitList;

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public java.lang.String getCode() {
		return code;
	}

	public void setCode(java.lang.String code) {
		this.code = code;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public float getInstalledPower() {
		return installedPower;
	}

	public void setInstalledPower(float installedPower) {
		this.installedPower = installedPower;
	}

	public int getTotalUnit() {
		return totalUnit;
	}

	public void setTotalUnit(int totalUnit) {
		this.totalUnit = totalUnit;
	}

	public float getTotalHead() {
		return totalHead;
	}

	public void setTotalHead(float totalHead) {
		this.totalHead = totalHead;
	}

	public float getNetHead() {
		return netHead;
	}

	public void setNetHead(float netHead) {
		this.netHead = netHead;
	}

	public float getDesignedDelivery() {
		return designedDelivery;
	}

	public void setDesignedDelivery(float designedDelivery) {
		this.designedDelivery = designedDelivery;
	}

	public float getRealDelivery() {
		return realDelivery;
	}

	public void setRealDelivery(float realDelivery) {
		this.realDelivery = realDelivery;
	}

	public java.lang.String getPosition() {
		return position;
	}

	public void setPosition(java.lang.String position) {
		this.position = position;
	}

	public java.util.Date getBuildDate() {
		return buildDate;
	}

	public void setBuildDate(java.util.Date buildDate) {
		this.buildDate = buildDate;
	}

	public java.util.Date getRebuildDate() {
		return rebuildDate;
	}

	public void setRebuildDate(java.util.Date rebuildDate) {
		this.rebuildDate = rebuildDate;
	}

	public float getMainHouse() {
		return mainHouse;
	}

	public void setMainHouse(float mainHouse) {
		this.mainHouse = mainHouse;
	}

	public float getAuxiliaryHouse() {
		return auxiliaryHouse;
	}

	public void setAuxiliaryHouse(float auxiliaryHouse) {
		this.auxiliaryHouse = auxiliaryHouse;
	}

	public float getManageHouse() {
		return manageHouse;
	}

	public void setManageHouse(float manageHouse) {
		this.manageHouse = manageHouse;
	}

	public java.lang.String getInPoolShape() {
		return inPoolShape;
	}

	public void setInPoolShape(java.lang.String inPoolShape) {
		this.inPoolShape = inPoolShape;
	}

	public java.lang.String getInPoolSize() {
		return inPoolSize;
	}

	public void setInPoolSize(java.lang.String inPoolSize) {
		this.inPoolSize = inPoolSize;
	}

	public float getInPoolVolume() {
		return inPoolVolume;
	}

	public void setInPoolVolume(float inPoolVolume) {
		this.inPoolVolume = inPoolVolume;
	}

	public java.lang.String getOutPoolShape() {
		return outPoolShape;
	}

	public void setOutPoolShape(java.lang.String outPoolShape) {
		this.outPoolShape = outPoolShape;
	}

	public java.lang.String getOutPoolSize() {
		return outPoolSize;
	}

	public void setOutPoolSize(java.lang.String outPoolSize) {
		this.outPoolSize = outPoolSize;
	}

	public float getOutPoolVolume() {
		return outPoolVolume;
	}

	public void setOutPoolVolume(float outPoolVolume) {
		this.outPoolVolume = outPoolVolume;
	}

	/**
	 * @return the unitList
	 */
	public List<PumpUnit> getUnitList() {
		return unitList;
	}

	/**
	 * @param unitList
	 *            the unitList to set
	 */
	public void setUnitList(List<PumpUnit> unitList) {
		this.unitList = unitList;
	}

}

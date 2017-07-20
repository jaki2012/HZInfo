package com.hnac.hzinfo.modules.pumpstation.entity;

public class PumpUnit {
	/** @pdOid 1482da29-ec1f-4cdb-acf3-7ce5b157ef37 */
	private java.lang.String id;
	/** @pdOid be34c904-e364-4c6c-9c9d-414b89ce1ee4 */
	private int type;
	private java.lang.String stationId;
	private java.lang.String code;
	private java.lang.String name;
	/** @pdOid ad71ac7c-3df6-42e0-a274-29646337c868 */
	private java.lang.String motorModel;
	/** @pdOid ca2f77d1-0cf4-449e-8e96-80221b40bec9 */
	private java.lang.String motorManufacturer;
	/** @pdOid 063c1ae6-4106-41bc-b05b-6d61ee6972f1 */
	private java.util.Date motorManufactureDate;
	/** @pdOid ab46378d-c43c-4059-b25f-896a582ba13c */
	private java.util.Date motorCommissioningDate;
	/** @pdOid f36eac0f-cc98-4d15-9f4b-5cd065c52099 */
	private java.lang.String pumpModel;
	/** @pdOid ee6c398a-3e79-44db-81ab-cb7542bd525d */
	private java.lang.String pumpManufacturer;
	/** @pdOid f0619c97-a210-429b-a46b-32d2895524cb */
	private java.util.Date pumpManufactureDate;
	/** @pdOid 0de04de3-2499-48fb-bee5-ce839289959a */
	private java.util.Date pumpCommissioningDate;
	/** @pdOid 93126c0e-9542-4a1e-940a-7f447372b39d */
	private float head;
	/** @pdOid dc891821-f0f1-4305-9ae5-8dcf28d7aba1 */
	private float flowRate;
	/** @pdOid 52d86683-8689-4a87-a9da-a0ca958b2340 */
	private float power;
	/** @pdOid 027e86b0-80a9-4a5c-9b11-cea8d8db258d */
	private float current;
	/** @pdOid 3157197b-5be9-47df-9f05-259a7613e855 */
	private float vacuumDegree;
	/** @pdOid e43f8572-f7e2-498c-b1a1-ae4ee9466746 */
	private float rotateSpeed;
	/** @pdOid d2a98ada-1603-4ca0-a889-b8a1af14777a */
	private int pumpBearing;
	/** @pdOid 79985e66-9baf-4155-92e8-5fb894344415 */
	private int impellerDiameter;
	/** @pdOid e1b0bf12-ac84-4c52-95aa-4c378df2c3df */
	private int impellerHole;
	/** @pdOid 4a1d9ae0-42d3-4a0c-9f04-686b01674880 */
	private java.lang.String valveIn;
	/** @pdOid 1b8f73b1-84a6-46f3-bffd-af531ebbcaa3 */
	private java.lang.String valveOut;

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the stationId
	 */
	public java.lang.String getStationId() {
		return stationId;
	}

	/**
	 * @param stationId
	 *            the stationId to set
	 */
	public void setStationId(java.lang.String stationId) {
		this.stationId = stationId;
	}

	public java.lang.String getCode() {
		return code;
	}

	public void setCode(java.lang.String code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.String getMotorModel() {
		return motorModel;
	}

	public void setMotorModel(java.lang.String motorModel) {
		this.motorModel = motorModel;
	}

	public java.lang.String getMotorManufacturer() {
		return motorManufacturer;
	}

	public void setMotorManufacturer(java.lang.String motorManufacturer) {
		this.motorManufacturer = motorManufacturer;
	}

	public java.util.Date getMotorManufactureDate() {
		return motorManufactureDate;
	}

	public void setMotorManufactureDate(java.util.Date motorManufactureDate) {
		this.motorManufactureDate = motorManufactureDate;
	}

	public java.util.Date getMotorCommissioningDate() {
		return motorCommissioningDate;
	}

	public void setMotorCommissioningDate(java.util.Date motorCommissioningDate) {
		this.motorCommissioningDate = motorCommissioningDate;
	}

	public java.lang.String getPumpModel() {
		return pumpModel;
	}

	public void setPumpModel(java.lang.String pumpModel) {
		this.pumpModel = pumpModel;
	}

	public java.lang.String getPumpManufacturer() {
		return pumpManufacturer;
	}

	public void setPumpManufacturer(java.lang.String pumpManufacturer) {
		this.pumpManufacturer = pumpManufacturer;
	}

	public java.util.Date getPumpManufactureDate() {
		return pumpManufactureDate;
	}

	public void setPumpManufactureDate(java.util.Date pumpManufactureDate) {
		this.pumpManufactureDate = pumpManufactureDate;
	}

	public java.util.Date getPumpCommissioningDate() {
		return pumpCommissioningDate;
	}

	public void setPumpCommissioningDate(java.util.Date pumpCommissioningDate) {
		this.pumpCommissioningDate = pumpCommissioningDate;
	}

	public float getHead() {
		return head;
	}

	public void setHead(float head) {
		this.head = head;
	}

	public float getFlowRate() {
		return flowRate;
	}

	public void setFlowRate(float flowRate) {
		this.flowRate = flowRate;
	}

	public float getPower() {
		return power;
	}

	public void setPower(float power) {
		this.power = power;
	}

	public float getCurrent() {
		return current;
	}

	public void setCurrent(float current) {
		this.current = current;
	}

	public float getVacuumDegree() {
		return vacuumDegree;
	}

	public void setVacuumDegree(float vacuumDegree) {
		this.vacuumDegree = vacuumDegree;
	}

	public float getRotateSpeed() {
		return rotateSpeed;
	}

	public void setRotateSpeed(float rotateSpeed) {
		this.rotateSpeed = rotateSpeed;
	}

	public int getPumpBearing() {
		return pumpBearing;
	}

	public void setPumpBearing(int pumpBearing) {
		this.pumpBearing = pumpBearing;
	}

	public int getImpellerDiameter() {
		return impellerDiameter;
	}

	public void setImpellerDiameter(int impellerDiameter) {
		this.impellerDiameter = impellerDiameter;
	}

	public int getImpellerHole() {
		return impellerHole;
	}

	public void setImpellerHole(int impellerHole) {
		this.impellerHole = impellerHole;
	}

	public java.lang.String getValveIn() {
		return valveIn;
	}

	public void setValveIn(java.lang.String valveIn) {
		this.valveIn = valveIn;
	}

	public java.lang.String getValveOut() {
		return valveOut;
	}

	public void setValveOut(java.lang.String valveOut) {
		this.valveOut = valveOut;
	}

}

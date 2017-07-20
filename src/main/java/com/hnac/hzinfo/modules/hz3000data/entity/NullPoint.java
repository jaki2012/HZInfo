package com.hnac.hzinfo.modules.hz3000data.entity;

import com.hnac.hzinfo.modules.hz3000data.utils.RealDataUtil;

public class NullPoint extends BasePoint{
	private static final long serialVersionUID = -4528390287219403134L;
	public NullPoint(){
		this.setDataType(RealDataUtil.IS_NULL);
	}
}

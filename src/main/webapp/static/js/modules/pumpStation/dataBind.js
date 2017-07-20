/**
 * 
 */
var dataBindList ;
	var dataBindTable;
	var factoryList;
	
	function loadInfoList(){
		$.ajax({
			url:"getStation.do",
     	   dataType:"json",
     	   type:"POST",
     	   async: false,
     	  success:function(data){
     		  var treeData = [];
     		  
     		  for(var i = 0; i < data.total; i ++){
     			 var station = data.rows[i];
     			 treeData[i] =new Object();
     			 treeData[i].title = station.name;
     			 treeData[i].key = "station" +station.id;
     			 treeData[i].tooltip = station.name;
     			 var units = station.unitList;
     			 var childobj = [];
     			 if(units && units.length > 0){
     				for(var j = 0 ; j < units.length ; j++){
     					childobj[j] = new Object();
						childobj[j].title = units[j].name;
						childobj[j].key =  units[j].id;
						childobj[j].tooltip = units[j].name;
        			 }
     				 treeData[i].children = childobj;
     			 }   			 
     		  }
     		  if(treeData.length > 0){
     			 treeData[0].expanded = true;
     		  }
     		 $("#infoList").fancytree({
			    	icon: false,
			    	selectMode: 3,
					source: treeData,
					autoScroll: true,
					activate: function(event, data) {
						var activeNode = data.node;
						loadDataList(activeNode.key);		
					} 
 		   });
     	  }
		});
	}
	
	function loadDataList(id){
		var isStation = true;
		if (id.indexOf("station") < 0 ){
			isStation = false;
		}else{
			id = id.substring(7);
		}
		$.ajax({
			url : "getDataBindById.do",
			type : "POST",
			data : {"id" : id ,"isStation" : isStation},
			dataType : "json",
			async: false,
			success : function(data){				
				if(data.rows && data.rows.length > 0){
					dataBindList = 	data.rows;
				}else{
					dataBindList = [];			
				}
								
				initDataTable(isStation);
			}
		});
	}
	
	function initDataTable(isStation){
		
		dataBindTable = $("#dataList").DataTable({
			"destroy": true,//如果需要重新加载的时候请加上这个
			"data": dataBindList,
			"dom": "t",
			"iDisplayLength": 100,
			"ordering": false,
			"oLanguage": {
                	"sProcessing":   "处理中...",
                	"sLoadingRecords": "载入中...",
			    	"sLengthMenu": "每页显示 _MENU_ 条记录",
			    	"sZeroRecords": "抱歉， 没有找到",
			    	"sInfo": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
			    	"sInfoEmpty": "没有数据",
			    	"sInfoFiltered": "(从 _MAX_ 条数据中检索)",
			    	"oPaginate": {
			    	"sFirst": "首页",
			    	"sEmptyTable":     "表中数据为空",
			    	"sPrevious": "前一页",
			    	"sNext": "后一页",
			    	"sLast": "尾页"
			    	},
			    	"sSearch": "查找",
			    	"sZeroRecords": "没有检索到数据"
		    	},
			"columns" : [
			          { title:"id", data: 'id', "visible": false},
			          { title:'<div style="width:68px">名称</div>',data : "name","width" : "113px" },
			          { title:"厂站"},
			          { title:"组"},
			          { title:"数据点"},
			          { title:'<div style="width:68px">操作</div>',"width" : "68px"}
				],
			"columnDefs" : [ 
				 {
					"targets" : 1,//操作按钮目标列
					"orderable" : false,
					"width" : "118px"
				},
				{
					"targets" : 2,//操作按钮目标列
					"orderable" :false,
					"width" : "197px",
					"render" : function(data, type,row) {
						if (row.id == "")
							return "";
						else{
							if (typeof(data)=="undefined")
								data = "";
							return '<select id = "factoryList' + row.id + '" class = "selectpicker show-tick form-control" data-width="180px" style="width: 100px;"></select>';
						}
					}
				}, 
				{
					"targets" : 3,//操作按钮目标列
					"orderable" :false,
					"width" : "197px",
					"render" : function(data, type,row) {
						if (row.id == "")
							return "";
						else{
							if (typeof(data)=="undefined")
								data = "";
							return '<select id = "groupList' + row.id + '" class = "selectpicker show-tick form-control" data-width="180px" style="width: 100px;"></select>';
						}
					}
				}, 
				{
					"targets" : 4,//操作按钮目标列
					"orderable" :false,
					"width" : "197px",
					"render" : function(data, type,row) {
						if (row.id == "")
							return "";
						else{
							if (typeof(data)=="undefined")
								data = "";
							return '<select id = "pointList' + row.id + '" class = "selectpicker show-tick form-control" data-width="180px" style="width: 100px;"></select>';
						}
					}
				}, 
				{
					// 定义操作列
					"targets" : 5,//操作按钮目标列
					"orderable" :false,
					"data" : null,
					"width" : "73px",
					"render" : function(data, type,row) {						
						if (row.id == ""){
							var html = "<div><input type='button' class='btn btn-primary' onclick='dataGridbtnAdd()' style='margin:0px;height:25px;line-height:5px;' value='新增'/>";
							html += "</div>";
							return html;
						}
						else{
							var id = '"' + row.id + '"';
							var html = "<input type='button' class='btn btn-primary' onclick='dataGridbtnDel("+ id + ")' style='margin:0px;height:25px;line-height:5px;' value='提交'/>";
							html += "</div>";
							return html;
						}
					}
				}],
				"initComplete": function(settings, json) {		
					var pointUrl;
					if(isStation){
						pointUrl = "getYcPointById.do";
					}else{
						pointUrl = "getYxPointById.do";
					}
					
					for(var i = 0 ; i< dataBindList.length ; i++){							
						$.ajax({
							url : pointUrl,
							dataType:"json",
							data: {"id" : dataBindList[i].realId},
							type:"POST",
							async: false,
							success:function(ycPoint){
								var $factoryList = $("#factoryList" + dataBindList[i].id);
								
								$factoryList.empty();
								for(var j = 0 ; j < factoryList.length ; j++){
									var $factory = $(["<option value = '",factoryList[j].id,"' >",factoryList[j].name,"</option>"].join(""));
									$factoryList.append($factory);
								}
								
								$factoryList.selectpicker("refresh");						
								$factoryList.selectpicker("val",ycPoint.factoryId);
								
								$.ajax({
									url : "getgroupbyfactoryid.do",
									dataType:"json",
									data: {"id" : ycPoint.factoryId},
									type:"POST",
									async: false,
									success:function(groupList){
										var $groupList = $("#groupList" + dataBindList[i].id );
										$groupList.empty();
										for(var k = 0 ; k < groupList.length ; k++){
											var $group = $(["<option value = '",groupList[k].id,"' >",groupList[k].name,"</option>"].join(""));
											$groupList.append($group);
										}					
										$groupList.selectpicker("refresh");
										$groupList.selectpicker("val",ycPoint.groupId);
										
										var $url ;
										if(isStation){
											$url = "getYcPointByGroupId.do";
										}else{
											$url = "getYxPointbygroupid.do";
										}
										
										$.ajax({
											url : $url,
											dataType:"json",
											data: {"id" : ycPoint.groupId},
											type:"POST",
											async: false,
											success:function(pointList){
												var $pointList = $("#pointList" + dataBindList[i].id );
												$pointList.empty();
												for(var l = 0 ; l < pointList.length ; l++){
													var $group = $(["<option value = '",pointList[l].id,"' >",pointList[l].name,"</option>"].join(""));
													$pointList.append($group);
												}					
												$pointList.selectpicker("refresh");
												$pointList.selectpicker("val",ycPoint.id);
											}
										});
									}
								});
							}
						});
						
					}
				  }
		});
		
	}
	
	function initFactories(){
		$.ajax({
			url : "getfactories.do",
			dataType:"json",
			type:"POST",
			async: false,
			success:function(data){
				factoryList = data;
			}
		});
	}
	
	$(function(){
		initFactories();
		
		loadInfoList();
	});
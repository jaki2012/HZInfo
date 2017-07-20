/**
 * 
 */
function showtipSearching(type){
			if (type == 0){
				$('#btnSearch').html('查询中...');
				$('#btnSearch').attr("disabled",true);
			}
			else if (type == 1){
				$('#btnSearch').attr("disabled",false);
				$('#btnSearch').html("查询");
			}
		}
		function Appendzero(obj)  
	    {  
	        if(obj<10) return "0" +""+ obj;  
	        else return obj;  
	    }  
		function initSelFactory(){
			$.ajax({
	         	   url:"../pumpStation/getStationList.do",
	         	   type:"POST",
	         	   dataType:"json",
	         	   success:function(data){
	         		   var i = 0;
	         		   for (i = 0; i < data.total; i++){
	         		  		$("#selStation").append("<option value='"+data.rows[i].id+"'>"+data.rows[i].name+"</option>");
	         		   }
	         		   $("#selStation").selectpicker('refresh');
	         	   }
	        });
		}
						
		function loadDataTable(data){
			
			$('#datatable').DataTable( {
				dom: 'Bfrt',
				"iDisplayLength" : 40,
				"searching":false,
				"data": data,
				"order": [[ 0, "asc" ]],
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
			    "initComplete": function(settings, json) {
			    		showtipSearching(1);
			    	},
			    "bDestroy":true,
			    columns: [
				          { title:'时间(day)', data: 'datatime'},
				          { title:"1号机组状态", data: 'ps1', "defaultContent": "" },
				          { title:"2号机组状态", data: 'ps2', "defaultContent": "" },
				          { title:"3号机组状态", data: 'ps3', "defaultContent": "" },
				          { title:"4号机组状态", data: 'ps4', "defaultContent": "" },
				          { title:"前池(m)", data: 'ps5', "defaultContent": "" },
				          { title:"后池(m)", data: 'ps6', "defaultContent": "" },
				          { title:"流量(m³/s)", data: 'ps7', "defaultContent": "" }
					]
			} );
		}
		
		function dataAjaxQuery(stationId, start){
			//todo:一个staion可能有多个机组，也从后台获取,这里假设为4个
			//模拟数据
			data = [];
			for (var i = 0; i <= 30; i++){
				data[i] = new Object();
				data[i].datatime = i + 1;
				data[i].ps1 = parseInt(10*Math.random()) > 3 ? "运行" : "停止";
				data[i].ps2 = parseInt(10*Math.random()) > 3 ? "运行" : "停止";
				data[i].ps3 = parseInt(10*Math.random()) > 3 ? "运行" : "停止";
				data[i].ps4 = parseInt(10*Math.random()) > 3 ? "运行" : "停止";
				data[i].ps5 = parseInt(10*Math.random());
				data[i].ps6 = parseInt(10*Math.random());
				data[i].ps7 = parseInt(10*Math.random());
			}
			loadDataTable(data);
		}
		function dataSearch(){
			showtipSearching(0);
			var stationId = $("#selStation").val();
			var start = $("#inputStart").val();
			dataAjaxQuery(stationId, start);
   		  	showtipSearching(1);
		}
		function timeInit(){
			var d = new Date();
			var start;
			start = "" + Appendzero(d.getFullYear()) + "-" + Appendzero(d.getMonth() + 1) ;
			$("#inputStart").val(start);
			var inputStart = document.getElementById("divStart");
			inputStart.setAttribute("data-date-format","yyyy-mm");
			$('.form_datetime').datetimepicker({
				language: 'zh-CN',
				weekStart: 0,
				todayBtn:  1,
				autoclose: 1,
				todayHighlight: 1,
				startView: 3,
				minView: 3,
				forceParse: 1
			});
		}
		$(function(){
			timeInit();
			initSelFactory();
			$("#btnSearch").click(function(event){
				dataSearch();
	    	});
		});
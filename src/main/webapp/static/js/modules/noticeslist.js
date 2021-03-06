var deleteType = 0;
var globalTable = null;
var searchConditions = {};

// 自写扩展对象的方法
function extend(destination, source) {
    for (var property in source)
        destination[property] = source[property];
    return destination;
}

function editIndex(noticeIndex) {
    $.ajax({
        url: "/sys/user/noticePublish",
        type: "get",
        dataType: "json",
        data: {
            noticeIndex: noticeIndex
        }
    })
}

function deleteNotices() {
    console.log("i am executed!")
    if (1 === deleteType) {
        deleteSpecificNotice();
    } else {
        deleteSelectedNotices();
    }
}

function deleteSpecificNotice() {
    var singleArr = [];
    singleArr.push(parseInt($("#deletingNoticeIndex").text()));
    $.ajax({
        url: "/sys/notice/notice",
        dataType: "json",
        type: "delete",
        contentType: "application/json",
        data: JSON.stringify({
            "noticesIndexes": singleArr
        }),
        success: function () {
            alert("删除成功！");
            $('#deleteConfirm').modal('hide');
            // 重新加载数据
            globalTable.ajax.reload();
        }
    })
}

function deleteSelectedNotices() {
    var selectedNotices = $(".checkchild:checked");

    if (0 >= selectedNotices.length) {
        alert("您未选中任何数据！");
        return;
    }

    var noticesIndexes = [];
    selectedNotices.each(function () {
        noticesIndexes.push(parseInt($(this).val()));
    })

    console.log(noticesIndexes)

    $.ajax({
        "url": "/sys/notice/notice",
        "type": "delete",
        "dataType": "json",
        "contentType": "application/json",
        "data": JSON.stringify({
            "noticesIndexes": noticesIndexes
        }),
        "success": function () {
            alert("删除成功！")
            $('#deleteConfirm').modal('hide');
            // 重新加载数据
            // 通过console.log可以发现此时table未定义 需要全局变量来调控
            // 因此需要注释下面的这一语句 因为js遇错就会停止执行
            // console.log(table)
            globalTable.ajax.reload();
        }
    })
}
function advancedSearch() {
    var titleCondition = $("#notice-title").val();
    var senderCondition = $("#notice-sender").val();
    var contentCondition = $("#notice-content").val();
    var minSendTimeCondition = $("#notice-minSendTime").val();
    var maxSendTimeCondition = $("#notice-maxSendTime").val();
    searchConditions.titleCondition = titleCondition;
    // searchConditions.titleCondtion = titleCondition;
    searchConditions.senderCondition = senderCondition;
    searchConditions.contentCondition = contentCondition;
    // 目前的时间
    var now = new Date();
    // 时间处理逻辑
    if (minSendTimeCondition != "") {
        var tempMinSendTimeCondition = minSendTimeCondition + " 00:00:00";
        var minSendTime = new Date(tempMinSendTimeCondition);
        if (minSendTime > now) {
            alert("选择的发布日期不能晚于今天！")
            return
        }
        if (maxSendTimeCondition != "") {
            var tempMaxSendTimeCondition = maxSendTimeCondition + " 23:59:59";
            var maxSendTime = new Date(tempMaxSendTimeCondition);
            // 以下逻辑可以暂时省略
            // if(maxSendTime > now ) {
            //     alert("选择的发布日期不能晚于今天！")
            //     return
            // }
            if (maxSendTime < minSendTime) {
                alert("最晚发布时间不能早于最早发布时间！")
                return
            }
        }
    }
    searchConditions.minSendTimeCondition
        = minSendTimeCondition + (minSendTimeCondition != "" ? " 00:00:00" : "");
    searchConditions.maxSendTimeCondition
        = maxSendTimeCondition + (maxSendTimeCondition != "" ? " 23:59:59" : "");
    // console.log(searchConditions)
    globalTable.draw();
    alert("查询成功！");
    $(".searchBar input").val("");
    // $('#advancedSearch').modal('hide');
    // alert($("#searchBtn").is(":focus") ? "focus" : "blur")
    $("div.searchResultHint b").html("当前为您展示条件筛选后的结果，若想查看全部结果，请点击<a class='delete btn btn-default btn-xs'><i class='fa fa-times'></i> 重新加载数据</a>按钮。");
}

function initBeforeDelete(index, title) {
    deleteType = 1;
    var html = "您将删除以下公告："
    html += "<p><span class='deletingNoticesTitle'>" + title + "</span></p>"
    html += "<p><span id='deletingNoticeIndex' class='deletingNoticesIndex'>" + index + "</span><p>"
    html += "删除公告后数据将不可恢复，请确认无误后再继续操作"
    $("#deleteConfirm .modal-body").html(html);
}

function initBeforeDeleteBatch() {
    deleteType = 0;
    var html = "您将删除以下公告："
    var selectedNotices = $(".checkchild:checked");
    if (0 >= selectedNotices.length) {
        alert("您未选中任何数据！");
        return;
    }

    var collapseThreshold = 5;
    var indexesCount = selectedNotices.length;
    if (collapseThreshold >= selectedNotices.length) {
        selectedNotices.each(function () {
            var rowNum = $(this).get(0).parentNode.parentNode.rowIndex;
            var colNum = $(this).get(0).parentNode.colIndex;
            var selectorStr = "#noticesList tr:eq(" + rowNum + ")";
            var title = $(selectorStr).get(0).cells[2].innerText;
            html += "<p><span class='deletingNoticesTitle'>" + title + "</span><p>";
        })
    } else {
        var count = 0;
        html += '<dl class="dl-horizontal">'
        selectedNotices.each(function () {
            if (count >= 2) {
                // 在jquery each函数里面 return false相当于break
                return false;
            }
            var rowNum = $(this).get(0).parentNode.parentNode.rowIndex;
            var colNum = $(this).get(0).parentNode.colIndex;
            var selectorStr = "#noticesList tr:eq(" + rowNum + ")";
            var title = $(selectorStr).get(0).cells[2].innerText;
            html += '<dt>公告 ' + (count + 1) + '：</dt>';
            html += '<dd>' + title + '</dd>';
            //html += "<p><span class='deletingNoticesTitle'>" + title + "</span><p>";
            count++;
        })
        html += '</dl>';
        html += '<div><a href="#demo" class="btn" data-toggle="collapse" data-target="#notices-collapse"><span class="glyphicon glyphicon-chevron-down"></span>点击查看/隐藏其余' + (indexesCount - 2) + '条数据</a></div>'
        html += '<div class="pre-scrollable collapse" id="notices-collapse">';
        count = 0;
        html += '<dl class="dl-horizontal">'
        selectedNotices.each(function () {
            if (count < 2) {
                count++;
                // 在jquery each函数里面 return true相当于continue
                return true;
            }

            var rowNum = $(this).get(0).parentNode.parentNode.rowIndex;
            var colNum = $(this).get(0).parentNode.colIndex;
            var selectorStr = "#noticesList tr:eq(" + rowNum + ")";
            var title = $(selectorStr).get(0).cells[2].innerText;
            html += '<dt>公告 ' + (count + 1) + '：</dt>';
            html += '<dd>' + title + '</dd>';
            //html += "<p><span class='deletingNoticesTitle'>" + title + "</span><p>";
            count++;
        })
        html += '</dl>';
        html += '</div>';
    }


    html += "删除公告后数据将不可恢复，请确认无误后再继续操作";
    $("#deleteConfirm .modal-body").html(html);
    $('#deleteConfirm').modal('show');
}

$(document).ready(function () {
    var table = $('#noticesList').DataTable({
        // 开启服务端模式
        "serverSide": true,
        // 开启读取服务器数据时显示正在加载中……特别是大数据量的时候，开启此功能比较好
        "processing": true,
        // ajax配置为function,手动调用异步查询
        ajax: function (data, callback, settings) {
            // console.log($.fn.dataTable.ext.search);
            // 只取自己所需要的参数
            var param = {}
            // 添加排序参数
            extend(param, searchConditions);
            switch (data.order[0].column) {
                case 2: {
                    param.orderColumn = "title";
                    break;
                }
                case 3: {
                    param.orderColumn = "sender";
                }
                case 4: {
                    param.orderColumn = "sendTime";
                    break;
                }
                default: {
                    param.orderColumn = "sendTime";
                    break;
                }
            }

            param.orderType = data.order[0].dir;
            param.length = data.length;
            param.start = data.start;
            param.page = (data.start / data.length) + 1;
            $.ajax({
                type: "POST",
                url: "/sys/notice/allNoticesWithPage",
                cache: false, //禁用缓存
                //data: data, //传入已封装的参数
                data: JSON.stringify(param),
                contentType: "application/json",
                dataType: "json",
                success: function (res) {
                    //封装返回数据，这里仅演示了修改属性名
                    var returnData = {};
                    // returnData.draw = res.data.draw;//这里直接自行返回了draw计数器,应该由后台返回
                    // returnData.recordsTotal = res.data.recordsTotal;
                    // returnData.recordsFiltered = res.data.recordsFiltered;//后台不实现过滤功能，每次查询均视作全部结果
                    // returnData.data = res.data.data;
                    returnData.draw = data.draw;
                    returnData.recordsTotal = res.recordsTotal;
                    // 调试源码分析得出
                    returnData.recordsFiltered = res.recordsFiltered;
                    returnData.data = res.data;
                    //调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
                    //此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
                    callback(returnData);
                },
                error: function (
                    XMLHttpRequest,
                    textStatus,
                    errorThrown) {
                    alert("查询失败");
                }
            });
        },
        // 去掉默认的f 搜索框
        dom: 'lB<"searchResultHint">r<"searchBar">tip',
        buttons: [
            {
                text: '发布新的公告',
                action: function (e, dt, node, config) {
                    window.location.href = "/sys/user/noticePublish";
                }
            },
            {
                text: '重新加载数据',
                // 重新加载数据会按照当前的排序方式、页码重新发一次ajax请求
                action: function (e, dt, node, config) {
                    // 将搜索条件置空
                    searchConditions = {};
                    $("div.searchResultHint b").html("");
                    dt.ajax.reload();
                }
            },
            {
                text: '删除选中数据',
                action: function (e, dt, node, config) {
                    initBeforeDeleteBatch();
                }
            },
            {
                text: '根据条件查询',
                action: function (e, dt, node, config) {
                    // node代表这个button
                    // e代表点击或触发事件
                },
                init: function (dt, node, config) {
                    // 绑定模态框属性
                    node.attr("id", "showSearchBarBtn");
                }
            }
        ],
        // 设置默认按时间降序排列
        "order": [[4, "desc"]],
        // 语言国际化 可以设置language.url 返回对应条目的翻译
        "language": {
            "sProcessing": "处理中...",
            "sLengthMenu": "显示 _MENU_ 项结果",
            "sZeroRecords": "没有匹配结果",
            "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
            "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
            "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
            "sInfoPostFix": "",
            "sSearch": "搜索:",
            "pagingType": "simple_numbers",
            "sUrl": "",
            "sEmptyTable": "表中数据为空",
            "sLoadingRecords": "载入中...",
            "sInfoThousands": ",",
            "oPaginate": {
                "sFirst": "首页",
                "sPrevious": "上页",
                "sNext": "下页",
                "sLast": "末页"
            },
            "oAria": {
                "sSortAscending": ": 以升序排列此列",
                "sSortDescending": ": 以降序排列此列"
            }
        },
        "aoColumnDefs": [
            {
                "aTargets": [0], "mData": "index", "bSortable": false
            },
            {
                "aTargets": [1], "mData": "index", "bSortable": false, "mRender": function (data) {
                    return '<input type="checkbox"  class="checkchild"  value="' + data + '" />';
                }
            },
            {
                "aTargets": [2], "mData": "title", "mRender": function (data, type, row) {
                    return "<a href='/sys/user/noticeDetail/" + row.index + "'>" + data + "</a>"
                }
            },
            {
                "aTargets": [3], "mData": "sender"
            },
            {
                "aTargets": [4], "mData": "sendTime"
            },
            {
                "aTargets": [5], "mData": "index", "mRender": function (data, type, row) {
                    var html = "<a href='/sys/user/noticeDetail/" + data + "'  class='delete btn btn-default btn-xs'  ><i class='fa fa-times'></i> 查看</a>";
                    //   /sys 和 sys开头差别很大  绝对路径与相对路径
                    html += "<a class='delete btn btn-default btn-xs' target='_blank' href='/sys/user/noticePublish?noticeIndex=" + row.index + "'> <i class='fa fa-times'></i> 修改</a>"
                    html += '<a onclick=\"initBeforeDelete(' + row.index + ',\'' + row.title + '\')\" data-toggle="modal" data-target="#deleteConfirm" class="delete btn btn-default btn-xs"  ><i class="fa fa-times"></i> 删除</a>';
                    return html;
                }, "bSortable": false
            }
        ],
        "createdRow": function (row, data, index) {
            // 设置第一列表格内容居中
            $(row).children('td').eq(1).attr('style', 'text-align:center;')
        }
    });

    globalTable = table;
    // 光是page.dt不行 因为page.dt后会再触发draw.dt 此时前面page.dt的效果就被覆盖了
    table.on('order.dt search.dt draw.dt', function () {
        table.column(0, {
            search: 'applied',
            order: 'applied'
        }).nodes().each(function (cell, i) {
            cell.innerHTML = i + 1;
        });
    })
    // 搜索工具栏
    // 附加点击button
    var btn = '<div class="control-group searchBtn"><div class="controls"><button type="button" onclick="advancedSearch()" class="btn btn-primary">开始查找</button></div></div>';
    $("div.searchBar").html($("#advancedSearch .modal-body").html());
    $("div.searchBar").addClass("collapse");
    $("div.searchBar").append(btn);
    // 搜索结果提示
    $("div.searchResultHint").html('<b></b>');
    var lastIdx = null;
    // 悬停列高亮
    $('#noticesList tbody')
        .on('mouseover', 'td', function () {
            // 判断没有匹配结果时的操作
            var col = table.cell(this).index();
            if (typeof (col) == "undefined") {
                return
            }
            var colIdx = table.cell(this).index().column;
            var rowIdx = table.cell(this).index().row;
            if (colIdx !== lastIdx) {
                $(table.cells().nodes()).removeClass('highlight');
                $(table.column(colIdx).nodes()).addClass('highlight');
            }
        })
        .on('mouseleave', function () {
            $(table.cells().nodes()).removeClass('highlight');
        });

    // 使用prop实现全选
    $(".checkall").on("click", function () {
        var check = $(this).prop("checked");
        $(".checkchild").prop("checked", check);
    })

    $("#noticesList").on("init.dt", function () {
        // 当取消勾选一个子checkbox则把全选的钩子去了
        $(".checkchild").on("click", function () {
            var check = $(this).prop("checked");
            if (!check) {
                $(".checkall").prop("checked", check);
            }
        })
    });
    // 绑定删除事件
    $("#delete").on("click", deleteNotices);

    // 绑定打开模态框条件
    // $("#searchBtn").on("click", function() {
    //     $('#advancedSearch').modal('show'); 
    // })
    $("#showSearchBarBtn").attr("data-toggle", "collapse");
    $("#showSearchBarBtn").attr("data-target", ".searchBar")
});
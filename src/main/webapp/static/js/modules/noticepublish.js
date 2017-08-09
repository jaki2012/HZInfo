/**
 * Created by lijiechu on 17/7/20.
 */
// 获取header
function getNoticeIndex(url, callback) {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if( xhr.readyState === 4 && xhr.status === 200 ) {
            callback( xhr.getResponseHeader("noticeIndex") );
        }
    }
    xhr.open("HEAD", url, true);
    xhr.send();
}

// 验证表单输入是否合理
// 如果缜密编程的话 后端也应对数据的有孝心进行验证
function validateForm(){
    // 手动触发验证
    $("form").data("bootstrapValidator").validate();
    // 没有通过验证
    if(!$("form").data("bootstrapValidator").isValid()){
        return false;
    } else {
        return true;
    }
}

// 用于检测是否上传文件后但最后放弃了发布公告
var normalExist = false;
// 离开页面前发送的请求 清除上传的多余公告
window.onbeforeunload = function (event) {
    if (!normalExist && uploadedFiles.length != 0) {
        // 初始化附件索引
        var annexesIDs = new Array()
        for (index in uploadedFiles) {
            annexesIDs.push(uploadedFiles[index].fileID)
        }
        $.ajax({
            url: "/sys/notice/annexes",
            type: "delete",
            contentType: "application/json",
            data: JSON.stringify(annexesIDs)
        })
    }
}

function preDeleteAnnex(annexID) {
    deleteAnnexes.push(annexID);
    $("#existedAnnex-"+ annexID).hide();
}
        
// 生成UUID的方法
function uuid() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";

    var uuid = s.join("");
    return uuid;
}

// 修改附件上传栏初始化附件列表
function initialAnnexesList(annexFileIndex) {
    // 判断有没有附件
    if(null == annexFileIndex || "" === annexFileIndex) {
        
        existedAnnexes = ""
        return
    }
    existedAnnexes = annexFileIndex;
    $("#annexesList").show();
    $.ajax({
        "url": "/sys/notice/annexes",
        "type": "get",
        "data": {
            annexFileIndex: annexFileIndex,
        },
        "success": function (data) {
            var html = '<ul class="list-unstyled">';
            for (index in data) {
                // 不是parseInt(index+1)
                var count = parseInt(index) + 1;
                html += '<li id="existedAnnex-' + data[index].annexID + '">附件' + count + '：';
                html += '<a href="/sys/notice/annex?annexID=' + data[index].annexID + '">' +data[index].originalName +'</a>'
                html += '<a onclick="preDeleteAnnex(' + data[index].annexID + ')" class="btn btn-primary">删除</a></li>'                       
            }
            html += '</ul>';
            $("#annexesList .controls").html(html);
        }
    })
}

// 用于确保发布成功弹窗是在公告发布成功之后
var clickPublish = false;
var noticeIndex = -1;

$("#returnNoticesList").on("click", function() {
    window.location.href = "/sys/user/noticesList";
})
$(function(){
    // 覆盖原有bootstrap 上传文件样式
    $('input[id=lefile]').change(function() {
        $('#photoCover').val($(this).val());
    });

    $("#publishBtn").on("click", function() {
        if(!validateForm()){
            alert("输入信息内容不符合要求")
            return
        } else {
            $("#publishConfirm").modal("show");
        }
    })

    $("#editBtn").on("click", function () {
        if (!validateForm()) {
            alert("输入信息内容不符合要求")
            return
        } else {
            $("#publishConfirm").modal("show");
        }
    })

    // 更新公告put请求
    $("#edit").on("click", function(){
        // 初始化附件索引
        var annexFileIndex = new Array()
        for (index in uploadedFiles) {
            annexFileIndex.push(uploadedFiles[index].fileID)
        }
        var annexFileIndexStr = annexFileIndex.join(",");
        var filesCount = $('#notice-annexes').fileinput('getFilesCount');
        if(filesCount > 0)
            $("#notice-annexes").fileinput("upload");
        else {
            var deleteAnnexesStr = deleteAnnexes.length > 0 ? 
                    "?deleteAnnexes="+ deleteAnnexes.join(",") : ""; 
            if(0 != annexFileIndexStr.length){
                if(0 != existedAnnexes.length){
                    existedAnnexes += ',';
                }      
                existedAnnexes += annexFileIndexStr;
            }
            var existedAnnexesStr = existedAnnexes;
            $.ajax({
                url: "/sys/notice/update" + deleteAnnexesStr,
                type: "put",
                dataType: "json",
                data: JSON.stringify({
                    index: parseInt(noticeIndex),
                    title: $("#notice-title").val(),
                    content: UE.getEditor('ueditorContainer').getContent(),
                    sender: "胡晓Huxiao",
                    annexFileIndex: existedAnnexesStr
                }),
                contentType: "application/json",
                success:function(data){
                    clickPublish = true;
                    normalExist = true;
                    // 手动隐藏模态框
                    $('#publishConfirm').modal('hide'); 
                }
            })
        } 

    })
    // 新增公告post请求
    $("#publish").on("click", function(){
        // 是fileinput不是fileInput!
        var filesCount = $('#notice-annexes').fileinput('getFilesCount');
        if(filesCount > 0)
            $("#notice-annexes").fileinput("upload");
        else {
            // 初始化附件索引
            var annexFileIndex = new Array()
            for (index in uploadedFiles) {
                annexFileIndex.push(uploadedFiles[index].fileID)
            }
            $.ajax({
            url: "/sys/notice/add",
            type: "POST",
            data: JSON.stringify({
                title: $("#notice-title").val(),
                content: UE.getEditor('ueditorContainer').getContent(),
                sender: "胡晓Huxiao",
                annexFileIndex: annexFileIndex.join(",")
            }),
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Content-Type', 'application/json');
            },
            dataType: "json",
            success: function (data) {
                if(data == 1) 
                    console.log("发布失败，没有相应的权限")
                else {
                    clickPublish = true;
                    normalExist = true;
                    // 手动隐藏模态框
                    $('#publishConfirm').modal('hide');
                }
            }
        })           
        }

    })

    // 绑定消失事件
    $("#publishConfirm").on("hidden.bs.modal", function(){
        if(clickPublish) {
            alert("发布成功！")
            clickPublish = false;
            window.location.href = "/sys/user/noticesList"; 
        }
    })
    
});
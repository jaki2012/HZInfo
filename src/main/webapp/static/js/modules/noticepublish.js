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

    // 更新公告put请求
    $("#edit").on("click", function(){
        var filesCount = $('#notice-annexes').fileinput('getFilesCount');
        if(filesCount > 0)
            $("#notice-annexes").fileinput("upload");
        else {
            var deleteAttachmentsStr = deleteAttachments.length > 0 ? 
                    "?deleteAttachments="+ deleteAttachments.join(",") : ""; 
            var existedAttachmentsStr = existedAttachments;
            $.ajax({
                url: "/sys/notice/update" + deleteAttachmentsStr,
                type: "put",
                dataType: "json",
                data: JSON.stringify({
                    index: parseInt(noticeIndex),
                    title: $("#notice-title").val(),
                    content: UE.getEditor('ueditorContainer').getContent(),
                    sender: "胡晓Huxiao",
                    annexFileIndex: existedAttachmentsStr
                }),
                contentType: "application/json",
                success:function(data){
                    clickPublish = true;
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
            $.ajax({
            url: "/sys/notice/add",
            type: "POST",
            data: JSON.stringify({
                title: $("#notice-title").val(),
                content: UE.getEditor('ueditorContainer').getContent(),
                sender: "胡晓Huxiao",
                //annexFileIndex: annexFileIndex.join(",")
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
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

$(function(){
    // 覆盖原有bootstrap 上传文件样式
    $('input[id=lefile]').change(function() {
        $('#photoCover').val($(this).val());
    });

    // 更新公告put请求
    $("#edit").on("click", function(){
        $.ajax({
            url: "/sys/notice/update",
            type: "put",
            dataType: "json",
            data: JSON.stringify({
                index: parseInt(noticeIndex),
                title: $("#notice-title").val(),
                content: UE.getEditor('ueditorContainer').getContent(),
                sender: "马海莲"
            }),
            contentType: "application/json",
            success:function(data){
                clickPublish = true;
                // 手动隐藏模态框
                $('#publishConfirm').modal('hide'); 
            }
        })
    })
    // 新增公告post请求
    $("#publish").on("click", function(){
        $.ajax({
            url: "/sys/notice/add",
            type: "POST",
            data: JSON.stringify({
                title: $("#notice-title").val(),
                content: UE.getEditor('ueditorContainer').getContent(),
                sender: "胡晓"
            }),
            beforeSend: function(xhr){
              xhr.setRequestHeader('Content-Type','application/json');
            },
            dataType: "json",
            success: function(data){
                clickPublish = true;
                // 手动隐藏模态框
                $('#publishConfirm').modal('hide'); 
            }
        })
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
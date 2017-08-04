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
        var filesCount = $('#notice-annexes').fileinput('getFilesCount');
        if(filesCount > 0)
        $("#notice-annexes").fileinput("upload");
    })
    // 新增公告post请求
    $("#publish").on("click", function(){
        // 是fileinput不是fileInput!
        var filesCount = $('#notice-annexes').fileinput('getFilesCount');
        if(filesCount > 0)
        $("#notice-annexes").fileinput("upload");

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
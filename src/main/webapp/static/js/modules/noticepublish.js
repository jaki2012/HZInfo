/**
 * Created by lijiechu on 17/7/20.
 */

// 用于确保发布成功弹窗是在公告发布成功之后
var clickPublish = false;

$(function(){
    // 覆盖原有bootstrap 上传文件样式
    $('input[id=lefile]').change(function() {
        $('#photoCover').val($(this).val());
    });

    // 新增公告post请求
    $("#publish").on("click", function(){
        $.ajax({
            url: "/sys/notice/add",
            type: "POST",
            data: JSON.stringify({
                title: $("#notice-title").val(),
                content: $("#notice-content").val(),
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
        }
    })
    
});
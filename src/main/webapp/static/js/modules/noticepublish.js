/**
 * Created by lijiechu on 17/7/20.
 */
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
                sender: "薛之谦"
            }),
            beforeSend: function(xhr){
              xhr.setRequestHeader('Content-Type','application/json');
            },
            dataType: "json",
            success: function(data){
                console.log(data)
            }
        })
    })
});
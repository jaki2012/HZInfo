// 获取路径的最后一个参数，也就是公告的id
function getNoticeID() {
    var sURLVariables, i, sPageURL = window.location.pathname.substring(1);
    if (sPageURL) {
        sURLVariables = sPageURL.split("/");
        return sURLVariables[sURLVariables.length - 1];
    }
}
$(document).ready(function () {
    $.ajax({
        "url": "/sys/notice/notice/" + getNoticeID(),
        "type": "get",
        "success": function (data) {
            $("#title").text(data.title);
            $("#sender").text("发布者: " + data.sender);
            $("#sendTime").text("发布时间: " + data.sendTime);
            // 富文本内容为html方法
            $("#content").html(data.content);
            // 判断有没有附件
            if (null == data.annexFileIndex || "" === data.annexFileIndex) {
                $("#annexesIndication").hide();
                return
            }

            $.ajax({
                "url": "/sys/notice/annexes",
                "type": "get",
                "data": {
                    annexFileIndex: data.annexFileIndex,
                },
                "success": function (data2) {
                    var html = '<dl class="dl-horizontal">';
                    for (index in data2) {
                        // 不是parseInt(inedex+1)
                        var count = parseInt(index) + 1
                        html += '<dt>附件 ' + count + '：</dt>';
                        html += '<dd><a href="/sys/notice/annex?annexID=' + data2[index].annexID + '">' + data2[index].originalName + '</a></dd>'

                    }
                    html += '</dl>';
                    $("#annexes").html(html);
                }
            })
        }
    })
});
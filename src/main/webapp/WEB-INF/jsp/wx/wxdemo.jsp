<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>微信demo</title>
    <meta name="viewport" content="width=device-width, initial-scale=1,  user-scalable=no">
    <%@include file="../common/taglibs.jsp" %>
    <script src="${path}/assets/common/jquery-1.11.3.js"></script>
    <script src="https://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
    <script>
        wx.config({
            debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
            appId: '${jsConfig.appId}', // 必填，企业号的唯一标识，此处填写企业号corpid
            timestamp: '${jsConfig.timestamp}', // 必填，生成签名的时间戳
            nonceStr: '${jsConfig.nonceStr}', // 必填，生成签名的随机串
            signature: '${jsConfig.signature}',// 必填，签名，见附录1
            jsApiList: [
                'chooseImage',
                'uploadImage'
            ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
        });

        wx.ready(function () {
            $(".picture").click(function () {
                wx.chooseImage({
                    count: 1, // 一次性选择图片的数量，默认9
                    sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
                    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
                    success: function (result) {
                        var localIds = result.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
                        wx.uploadImage({
                            localId: localIds.toString(), // 需要上传的图片的本地ID，由chooseImage接口获得
                            isShowProgressTips: 1, // 默认为1，显示进度提示
                            success: function (res) {
                                var mediaId = res.serverId; // 返回图片的服务器端ID，即mediaId
                                var reqMap = {
                                    "orderId": "",
                                    "mediaId": ""
                                };
                                reqMap.orderId = $('#inp-orderId').val();
                                reqMap.mediaId = mediaId;

                            },
                            fail: function (res) {
                            }
                        });
                    },
                    cancel: function () {
                    },
                    fail:function () {
                    }
                });
            });

        });

        wx.error(function (res) {
        });

    </script>

</head>
<body>
<div class="picture">调用图片接口</div>
</body>
</html>

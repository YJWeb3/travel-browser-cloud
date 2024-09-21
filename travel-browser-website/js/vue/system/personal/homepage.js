var vue = new Vue({
    el:"#app",
    data:{
        owner:{},
        visitors:[
            {id:1, nickname:"lyf", headImgUrl: "/images/test/personal/lyf.jpg"},
            {id:2, nickname:"qsz", headImgUrl: "/images/test/personal/qsz.jpg"},
            {id:3, nickname:"zhy", headImgUrl: "/images/test/personal/zhy.jpg"},
            {id:4, nickname:"xiaofei", headImgUrl: "/images/default.jpg"}
            ],
        totalView:200,
        todayView:20
    },
    mounted:function () {
        var param = getParams();
        var ownerId = param.ownerId;

    }
});


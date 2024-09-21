var user = getUserInfo();
var vue = new Vue({
    el: "#app",
    data: {
        param: {},
        strategy: {},
        page: {},
        hash: {},
        content: {}
    },
    methods: {
        //查看明细
        queryDetail: function () {
            ajaxGet("article", "/strategies/detail", {id: this.param.id}, function (data) {
                vue.strategy = data.data;
                vue.content = data.data.content;
            })
        },
        //查询最新的统计数据
        queryStatData: function () {
            ajaxGet("article", "/strategies/stat/data", {id: this.strategy.id}, function (data) {
                let map = data.data;
                vue.strategy.viewnum = map.viewnum;
                vue.strategy.replynum = map.replynum;
                vue.strategy.sharenum = map.sharenum;
                vue.strategy.favornum = map.favornum;
                vue.strategy.thumbsupnum = map.thumbsupnum;
            })
        },
        //攻略点赞
        strategyThumbup: function () {
            ajaxGet("article", "/strategies/thumbnumIncr", {sid: vue.strategy.id}, function (data) {
                if (data.data) {
                    popup("顶成功啦");
                    // 刷新统计数据
                    vue.queryStatData();
                } else {
                    popup("今天你已经顶过了");
                }
            })
        },
        //攻略收藏
        favor: function () {
            ajaxPost("u", "/users/favor/strategies", {sid: vue.strategy.id}, function (data) {
                if (data.data) {
                    popup("收藏成功");
                    vue.strategy.favorite = true;
                } else {
                    popup("已取消收藏");
                    vue.strategy.favorite = false;
                }
                // 刷新统计数据
                vue.queryStatData();
            })
        },
        //光标定位
        contentFocus: function () {
            $("#content").focus();
        },
        //评论点赞
        commentThumb: function (commentId) {
            var page = $("#pagination").find("a.active").html() || 1;
            ajaxPost("comment", "/strategies/comments/likes", {cid: commentId}, function (data) {
                vue.commentPage(page, getParams().id);
            })
        },
        //鼠标移上
        mouseover: function (even) {
            $(even.currentTarget).find(".rep-del").css("display", "block");
        },
        //鼠标移出
        mouseout: function (even) {
            $(even.currentTarget).find(".rep-del").css("display", "none");
        },
        //评论分页
        commentPage: function (page, strategyId) {//分页
            strategyId = strategyId || vue.strategy.id;
            ajaxGet("comment", "/strategies/comments/query", {current: page, articleId: strategyId}, function (data) {
                vue.page = data.data;
                vue.page.number = parseInt(vue.page.number) + 1;
                buildPage(vue.page.number, vue.page.totalPages, vue.commentPage);
            })
        },
        //评论添加
        commentAdd: function () { //添加评论
            var param = {}
            param.strategyId = vue.strategy.id;
            param.strategyTitle = vue.strategy.title;

            var content = $("#content").val();
            if (!content) {
                popup("评论内容必填");
                return;
            }
            param.content = content;
            $("#content").val('');

            ajaxPost("comment", "/strategies/comments/save", param, function (data) {
                // 刷新评论列表
                vue.commentPage(1, param.strategyId);

                // 刷新统计数据
                vue.queryStatData();
            })
        },
        //点赞小手
        thumbEcho: function (thumbuplist) {
            if (user) {
                var id = parseInt(user.id)
                return $.inArray(id, thumbuplist) != -1;
            }
        }
    },
    filters: {
        dateFormat: function (date) {
            return dateFormat(date, "YYYY-MM-DD HH:mm:ss")
        }
    },
    mounted: function () {
        this.param = getParams();
        //查明细
        this.queryDetail();
        //攻略评论分页
        this.commentPage(1, this.param.id);

    }
});


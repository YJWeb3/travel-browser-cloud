var vue = new Vue({
    el: "#app",
    data: {
        abroadRank: [],
        chinaRank: [],
        hotRank: [],
        themeCds: [],
        banners: [],

        chinaCondition: [],
        abroadCondition: [],
        themeCondition: [],

        page: {},
    },
    methods: {
        //攻略排行
        queryRank: function (type) {
            //攻略排行--热门攻略  List<StrategyRank> list;
            ajaxGet("article", "/strategies/ranks", {}, function (data) {
                let {abroadRank, chinaRank, hotRank} = data.data;
                vue.abroadRank = abroadRank;
                vue.chinaRank = chinaRank;
                vue.hotRank = hotRank;
            })
        },
        //主题推荐
        queryThemeCds: function () {
            ajaxGet("article", "/strategies/themeCds", {}, function (data) {
                vue.themeCds = data.data;
            })
        },

        //条件列表
        queryCondition: function () {
            ajaxGet("article", "/strategies/conditions", {}, function (resp) {
                let map = resp.data;
                vue.chinaCondition = map.chinaCondition;
                vue.abroadCondition = map.abroadCondition;
                vue.themeCondition = map.themeCondition;
            })

        },

        //攻略查询
        strategySearch: function (e, type, refid, name) {
            $(".gl_wrap ol li a").css("background", "none").css("color", "#666").removeClass("s-on");
            $(e.currentTarget).css("background", "#ff9d00").css("color", "#fff").addClass("s-on");
            ;
            if (name) {
                $("#currentNav").html(name).css("color", "red");
                $("#searchForm input[name='type']").val(type);
                $("#searchForm input[name='refid']").val(refid);
                $("#searchForm input[name='orderBy']").val($(".upt_on").data("v"));
                this.doPage(1);
            }
        },
        //排序查询
        orderSearch: function (e, type) {
            $(".uptime_p a").removeClass("upt_on");
            $(e.currentTarget).addClass("upt_on");

            $("#searchForm input[name='type']").val($(".s-on").data("type"));
            $("#searchForm input[name='refid']").val($(".s-on").data("refid"));
            $("#searchForm input[name='orderBy']").val($(".upt_on").data("v"));
            this.doPage(1);

        },
        //分页
        doPage: function (page) {
            $("#searchForm input[name='current']").val(page);
            ajaxGet("article", "/strategies/query", $("#searchForm").serialize(), function (data) {
                vue.page = data.data;
                buildPage(vue.page.current, vue.page.pages, vue.doPage);
            })
        }

    },
    filters: {
        dateFormat: function (date) {
            return dateFormat(date, "YYYY-MM-DD HH:mm:ss")
        }
    },
    mounted: function () {
        var _this = this;
        this.queryRank();


        //主题推荐
        this.queryThemeCds();

        //攻略条件--主题攻略  List<StrategyCondition> list
        this.queryCondition();

        ajaxGet("article", "/banners/strategy", {}, function (data) {
            vue.banners = data.data;
        })

        //分页
        this.doPage(1);
    }
});


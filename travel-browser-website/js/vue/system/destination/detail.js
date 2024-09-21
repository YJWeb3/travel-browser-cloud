var vue = new Vue({
    el:"#app",
    data:{
        param:{},
        toasts:[],  //吐司
        dest:{},  //目的地
        catalogs:[], //概况中攻略分类
        strategies:[],  //点击量前3
        page:{}  //游记分页
    },
    methods:{
        //查吐司
        queryToasts:function (){
            ajaxGet("article","/destinations/toasts", {destId:this.param.id}, function (data) {
                // _this.toasts = data.data;
                var list = data.data;  //中国  广东  广州
                vue.dest = list.pop();  //数组弹出最后一个---广州额外维护
                vue.toasts = list;
            })

        },
        //查询攻略分类
        queryCatalog:function (){
            ajaxGet("article","/strategies/groups", {destId:this.param.id}, function (data) {
                vue.catalogs = data.data;
            })
        },
        //查询点击量前三
        queryViewnnumTop3:function (){
            ajaxGet("article","/strategies/viewnumTop3", {destId:this.param.id}, function (data) {
                vue.strategies = data.data;
            })

        },
        //游记分页
        queryTravelPage:function (){
            ajaxGet("article","/travels/query", {destId:this.param.id}, function (data) {
                vue.page = data.data;
                buildPage(vue.page.number, vue.page.totalPages, vue.doPage)
            })
        },

        commPage:function (page) {
            var p = $("#travelForm").serialize() + "&destId="+this.param.id + "&currentPage=" + page;
            //游记分页
            ajaxGet("article","/travels/query?"+p,{}, function (data) {
                vue.page = data.data;
                buildPage(vue.page.number, vue.page.totalPages, vue.doPage)
            })
        },
        doPage:function(page){
            this.commPage(page);
        },
        conditionChange:function(){
            this.commPage(1);
        }
    },
    mounted:function () {
        this.param = getParams();
        //查吐司
        this.queryToasts();
        //攻略概括-
        this.queryCatalog();
        //点击量前3--list<Strategy> list
        this.queryViewnnumTop3();
        //游记分页
        this.queryTravelPage();
    }
});


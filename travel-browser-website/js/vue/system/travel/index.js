var user = getUserInfo();
var vue = new Vue({
    el:"#app",
    data:{
        page:{},
        user:{}
    },
    methods:{
        //排序
        orderChange:function (orderType, event) {
            var el = event.currentTarget;
            $(".orderBy").closest("div").removeClass("on");
            $(el).closest("div").addClass("on");
            $("#orderType").val(orderType);
            this.commPage(1);
        },
        //分页
        commPage:function (page) {
            var param = getParams();
            var p = $("#travelForm").serialize() + "&currentPage=" + page;
            //游记分页
            ajaxGet("article","/travels/query?"+p,{}, function (data) {
                vue.page = data.data;
                buildPage(vue.page.current, vue.page.pages,vue.doPage);
            })
        },
        doPage:function(page){
            this.commPage(page);
        },
        conditionChange:function(){
            this.commPage(1);
        },
        queryPage:function (){
            ajaxGet("article","/travels/query",{}, function (data) {
                vue.page = data.data;
                buildPage(vue.page.current, vue.page.pages,vue.doPage);
            })
        }

    },
    mounted:function () {
        //游记分页
        this.queryPage();
    }
});


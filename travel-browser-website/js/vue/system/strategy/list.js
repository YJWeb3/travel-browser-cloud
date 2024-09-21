var vue = new Vue({
    el:"#app",
    data:{
        param:{},
        page:{},
        themes:[],
        toasts:[]
    },
    methods:{
        themeSelect:function(themeId, event){
            $("._j_tag").removeClass("on")
            $(event.currentTarget).addClass("on");
            vue.doPage(1);
        },
        doPage:function(page){
            var param = getParams();
            var themeId = $("._j_tag.on").data("tid");
            ajaxGet("article","/strategies/query",{themeId:themeId, destId:param.destId, current:page}, function (data) {
                vue.page = data.data;
                buildPage(vue.page.current, vue.page.pages,vue.doPage);
            })
        },
        queryToasts:function (){
            ajaxGet("article","/destinations/toasts",{destId:this.param.destId}, function (data) {
                var list = data.data;
                vue.toasts = list;
            })
        },
        queryTheme:function (){
            ajaxGet("article","/strategies/themes/list", {}, function (data) {
                vue.themes = data.data;
            })

        }
    },
    mounted:function () {
        this.param = getParams();
        this.queryToasts();
        this.queryTheme();
        this.doPage(1);
    }
});


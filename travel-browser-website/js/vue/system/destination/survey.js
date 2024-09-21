var vue = new Vue({
    el:"#app",
    data:{
        param:{},
        toasts:[],
        dest:{},
        catalogs:[],
        catalog:'',
        strategy:{},
        content:""
    },
    methods:{
        //吐司
        queryToasts: function (){
            ajaxGet("article","/destinations/toasts",{destId:this.param.destId}, function (data) {
                var list = data.data;
                vue.dest = list.pop();
                vue.toasts = list;
            })
        },
        //概括
        querySurery:function (){
            //List<StrategyCatalog> list
            ajaxGet("article","/strategies/groups",{destId:this.param.destId}, function (data) {
                //[{长隆度假区}, {最广州},{广式美味}]
                vue.catalogs = data.data;
                $.each(vue.catalogs, function(index, item){
                    if(item.id == vue.param.catalogId){
                        vue.catalog = item;  //选中攻略分类
                        vue.strategy = item.strategies[0]
                        //攻略分类下所有攻略文章第一篇, 需要在页面显示文章内容
                    }
                })
                //获取内容
                ajaxGet("article","/strategies/content", {id:vue.strategy.id}, function (data) {
                    vue.content = data.data.content;
                })
            })
        }
    },
    mounted:function () {
        this.param = getParams();
        //吐司
        this.queryToasts();
        //概括
        this.querySurery()
    }
});


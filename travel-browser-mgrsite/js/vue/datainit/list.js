var vue = new Vue({
    el:"#app",
    data:{
    },
    methods:{
        //刷新redis中缓存攻略统计数
        refreshStrategyStatis: function (){
            layer.confirm('确认要刷新吗？', function (index) {
                ajaxPost("article", "/strategies/statisDataInit", {}, function (data){
                    layer.msg('操作成功!', {icon: 1, time: 200}, function (){
                        location.reload();
                    });
                })
            });
        },
        //统计数据持久化
        strategyStatisPersistence:function (){
            layer.confirm('确认要持久化吗？', function (index) {
                ajaxPost("article", "/strategies/statisDataPersistence", {}, function (data){
                    layer.msg('操作成功!', {icon: 1, time: 200}, function (){
                        location.reload();
                    });
                })
            });
        },
        //es数据初始化
        esDataInit:function (){
            layer.confirm('确认要初始化ES数据吗？时间会很久，稍等~', function (index) {
                ajaxPost("search", "/dataInit", {}, function (data){
                    layer.msg('操作成功!', {icon: 1, time: 200}, function (){
                        location.reload();
                    });
                })
            });
        },
        delete:function (id){
            layer.confirm('确认要删除吗？', function (index) {
                ajaxPost("article", "/travels/delete/" + id, {}, function (data){
                    layer.msg('已删除!', {icon: 1, time: 200}, function (){
                        location.reload();
                    });
                })
            });
        }
    },
    mounted:function (){
    }
})
var vue = new Vue({
    el:"#app",
    data:{
        result:{},
        dest:{},
        qo:{}
    },
    methods:{
        searchChange:function (type) {
            searchByType(type, $("#_j_search_input").val());
        },
        querySearch:function (){
            var params = getParams();
            ajaxGet("search","/q", params, function (data) {
                var map = data.data;
                vue.result = map.result;
                vue.dest = map.dest;
                vue.qo = map.qo;
                $("#_j_search_input").val(vue.qo.keyword);
                $("#searchType").val(0);  //目的地
            })
        }
    },
    filters:{
        dateFormat:function(date){
            return dateFormat(date, "YYYY-MM-DD HH:mm:ss")
        }
    },
    mounted:function () {
        this.querySearch();
    }
});


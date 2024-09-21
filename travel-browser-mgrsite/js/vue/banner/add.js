var vue = new Vue({
    el:"#app",
    data:{
        articles:[]
    },
    methods:{
        //初始化
        init:function (){
            var imgFile = new ImgUploadeFiles('.box',function(e){
                this.init({
                    MAX : 1,
                    MH : 5800, //像素限制高度
                    MW : 5900, //像素限制宽度
                    callback : function(arr){
                        console.log(arr)
                        if(arr[0]){
                            $("#coverUrl").val(arr[0].src);
                        }
                    }
                });
            });



        },
        //关联文章
        searchArticle:function(event, type){
            //选择类型
            if(event){
                type = $(event.target).val();
            }
            var url = "/travels";
            if(type != 1){
                url = "/strategies";
            }
            ajaxGet("article", url + "/list", {}, function (data) {
                vue.articles = data.data;
            })
        },


        //banner保存
        bannerSave:function (){
            var param = {};
            var arrs = $("#form-article-add").serializeArray();
            for (var i = 0; i < arrs.length; i++) {
                param[arrs[i].name] = arrs[i].value;
            }

            ajaxPost("article", "/banners/save",param, function (data){
                window.location.href = "/views/banner/list.html"
            })

        },
        saveCancel:function (){
            window.location.href = "/views/banner/list.html"
        }
    },
    mounted:function (){
        this.init();
        this.searchArticle(null,1);  //默认查游记
    }
})
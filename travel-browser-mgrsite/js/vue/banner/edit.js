var vue = new Vue({
    el:"#app",
    data:{
        articles:[],
        banner:{},
        imgFile:{}
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
            this.imgFile = imgFile;
        },
        //关联文章
        searchArticle:function(event, type){
            //选择类型
            if(event){
                type = $(event.target).val();
                vue.banner.refid = null;
            }
            var url = "/travels";
            if(type != 1){
                url = "/strategies";
            }
            ajaxGet("article", url + "/list", {}, function (data) {
                vue.articles = data.data;
            })
        },
        //banner编辑
        bannerEdit:function (){
            var param = {};
            var arrs = $("#form-article-add").serializeArray();
            for (var i = 0; i < arrs.length; i++) {
                param[arrs[i].name] = arrs[i].value;
            }

            ajaxPost("article", "/banners/update",param, function (data){
                window.location.href = "/views/banner/list.html"
            })

        },
        editCancel:function (){
            window.location.href = "/views/banner/list.html"
        },
        //数据回显
        getDetail:function (id) {
            ajaxGet("article", "/banners/detail", {id:id}, function (data) {
                vue.banner = data.data;
                vue.imgFile.setImage(data.data.coverUrl)
            })
        }
    },
    mounted:function (){
        this.init();
        this.getDetail(getParams().id);
        this.searchArticle(null, getParams().type);
    }
})
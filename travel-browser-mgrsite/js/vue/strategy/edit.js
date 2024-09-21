var vue = new Vue({
    el:"#app",
    data:{
        themes:[],
        catalogVOs:[],
        ck:{},
        strategy:{},
        content:{},
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

            var ck = CKEDITOR.replace( 'strategyContent',{
                filebrowserUploadUrl: '/strategies/uploadImg'
            });
            this.ck = ck;

        },
        //查询主题
        queryTheme:function (){
            ajaxGet("article", "/strategies/themes/list", {}, function (data){
                vue.themes = data.data;
            });
        },
        //攻略分类分组下拉框
        queryCatalogGroup:function (){
            ajaxGet("article", "/strategies/catalogs/groups", {}, function (data){
                vue.catalogVOs = data.data;
                //vue.getDetail(getParams().id)
            })
        },
        strategyEdit:function (){
            var param = {};
            var arrs = $("#form-article-add").serializeArray();
            for (var i = 0; i < arrs.length; i++) {
                param[arrs[i].name] = arrs[i].value;
            }
            param["content.content"] = vue.ck.getData();


            ajaxPost("article", "/strategies/update",param, function (data){
                window.location.href = "/views/strategy/list.html"
            })

        },
        editCancel:function (){
            window.location.href = "/views/strategy/list.html"
        },
        //回显数据
        getDetail:function (id){
            var timer = setInterval(function () {
                ajaxGet("article", "/strategies/detail", {id:id}, function (data){
                    vue.strategy = data.data;
                    vue.content = data.data.content;
                    vue.ck.setData(vue.content.content)
                    vue.imgFile.setImage(data.data.coverUrl)
                })
                clearInterval(timer)
            }, 200);

        }
    },
    mounted:function (){
        this.init();
        this.queryTheme();
        this.queryCatalogGroup();
        this.getDetail(getParams().id);
    }
})
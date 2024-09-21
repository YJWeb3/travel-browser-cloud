var vue = new Vue({
    el:"#app",
    data:{
        themes:[],
        catalogVOs:[],
        ck:{}
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

            this.ck = CKEDITOR.replace( 'strategyContent',{
                filebrowserUploadUrl: getServiceUrl("article") + '/strategies/uploadImg'
            });
        },
        //查询主题
        queryTheme:function (){
            ajaxGet("article", "/strategies/themes/list", {}, function (data){
                vue.themes = data.data;
            });
        },
        //攻略分类分组下拉框--List<CatalogVO>
        queryCatalogGroup:function (){
            ajaxGet("article", "/strategies/catalogs/groups", {}, function (data){
                vue.catalogVOs = data.data;
            })
        },
        strategySave:function (){
            var param = {};
            var arrs = $("#form-article-add").serializeArray();
            for (var i = 0; i < arrs.length; i++) {
                param[arrs[i].name] = arrs[i].value;
            }
            param["content.content"] = vue.ck.getData();


            ajaxPost("article", "/strategies/save",param, function (data){
                window.location.href = "/views/strategy/list.html"
            })

        },
        saveCancel:function (){
            window.location.href = "/views/strategy/list.html"
        }
    },
    mounted:function (){
        this.init();
        this.queryTheme();
        this.queryCatalogGroup();
    }
})
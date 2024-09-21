var vue = new Vue({
    el:"#app",
    data:{
        content: ""
    },
    methods:{
        //查询
        search:function (){
            $('#bootstrap-table').bootstrapTable('selectPage', 1);
        },

        //初始化列表
        init:function (){
            var options = {
                url: getServiceUrl("article") + "/banners/query",
                contentType: "application/x-www-form-urlencoded",   //重要选项,必填
                uniqueId: "id",                     //每一行的唯一标识，一般为主键列
                striped : true, //是否显示行间隔色
                pagination : true,//是否分页
                sidePagination : 'server',//server:服务器端分页|client：前端分页
                pageNumber:1, //当前地基页
                pageSize:10, //每页显示数据条数
                pageList:[3,5, 10, 15],

                paginationFirstText: "首页",
                paginationPreText: "上一页",
                paginationNextText: "下一页",
                paginationLastText: "末页",

                responseHandler:function (data){
                    var retData = {}
                    retData.total = data.data.total;
                    retData.rows = data.data.records;
                    return retData;
                },
                queryParams:function (data){
                    var params = {};
                    if(data.limit){
                        params.pageSize = data.limit;
                    }
                    if(data.limit){
                        params.currentPage = data.offset / data.limit + 1;
                    }
                    params.keyword = $("#keyword").val();

                    return params;
                },
                columns: [{
                    checkbox: true
                },
                    {
                        field: 'id',
                        title: 'id'
                    },
                    {
                        field: 'title',
                        //align: 'left',
                        title: '标题'

                    },
                    {
                        field: 'coverUrl',
                        title: '封面',
                        formatter:function (value, row, index){
                            var actions = [];
                            actions.push('<img src="'+value+'" width="100px"/>')
                            return actions.join('');
                        }
                    },
                    {
                        field: 'seq',
                        title: '序号'
                    },
                    {
                        field: 'state',
                        title: '状态',
                        formatter: function(value, row, index) {
                            return value== 0?'正常':'禁用';
                        }
                    },
                    {
                        field: 'type',
                        title: '类型',
                        formatter: function(value, row, index) {
                            return value==1?"游记":"攻略";
                        }
                    },
                    {
                        title: '操作',
                        align: 'center',
                        formatter: function(value, row, index) {
                            var actions = [];
                            actions.push('<a title="编辑" onclick="vue.editBanner('+row.id+','+row.type +')" href="javascript:;"  class="btn btn-xs btn-info" ><i class="icon-edit bigger-120"></i></a> ')
                            actions.push('<a title="删除" onclick="vue.delete('+row.id+')" href="javascript:;"  class="btn btn-xs btn-danger" ><i class="icon-remove bigger-120"></i></a> ')
                            return actions.join('');
                        }
                    }]
            };
            $('#bootstrap-table').bootstrapTable(options);


        },
        editBanner:function (id, type){
            window.location.href = "/views/banner/edit.html?id="+id+"&type=" + type;
        },
        delete:function (id){
            layer.confirm('确认要删除吗？', function (index) {
                ajaxPost("article", "/banners/delete/" + id, {}, function (data){
                    layer.msg('已删除!', {icon: 1, time: 200}, function (){
                        location.reload();
                    });
                })
            });
        }
    },
    mounted:function (){
        this.init();
    }
})
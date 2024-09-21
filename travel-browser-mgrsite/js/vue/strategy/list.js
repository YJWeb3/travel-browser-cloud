var vue = new Vue({
    el:"#app",
    data:{
        region:{},
        dests:[]
    },
    methods:{
        //查询
        search:function (){
            $('#bootstrap-table').bootstrapTable('selectPage', 1);
        },

        //初始化列表
        init:function (){
            var options = {
                url: getServiceUrl("article") + "/strategies/query",
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
                        params.size = data.limit;
                    }
                    if(data.limit){
                        params.current = data.offset / data.limit + 1;
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
                        field: 'subTitle',
                        title: '标题'
                    },
                    {
                        field: 'destName',
                        title: '目的地'
                    },
                    {
                        field: 'catalogName',
                        title: '分类'
                    },
                    {
                        field: 'themeName',
                        title: '主题'
                    },
                    {
                        field: 'isabroad',
                        title: '是否国外',
                        formatter: function(value, row, index) {
                            return value == 1 ? '是' : '否'
                        }
                    },
                    {
                        title: '操作',
                        align: 'center',
                        formatter: function(value, row, index) {
                            var actions = [];
                            actions.push('<a title="编辑" onclick="vue.editStrategy('+row.id+')" href="javascript:;"  class="btn btn-xs btn-info" ><i class="icon-edit bigger-120"></i></a> ')

                            actions.push('<a title="删除" onclick="vue.delete('+row.id+')" href="javascript:;"  class="btn btn-xs btn-danger" ><i class="icon-remove bigger-120"></i></a> ')
                            return actions.join('');
                        }
                    }]
            };
            $('#bootstrap-table').bootstrapTable(options);


        },
        editStrategy:function (id){
            window.location.href = "/views/strategy/edit.html?id="+id;
        },
        delete:function (id){
            layer.confirm('确认要删除吗？', function (index) {
                ajaxPost("article", "/strategies/delete/" + id, {}, function (data){
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
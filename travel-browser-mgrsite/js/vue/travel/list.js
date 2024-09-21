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
                url: getServiceUrl("article") + "/travels/query",
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
                        title: '标题'
                    },
                    {
                        field: 'destName',
                        title: '目的地'
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
                        field: 'author.nickname',
                        title: '作者'
                    },

                    {
                        field: 'state',
                        title: '状态',
                        formatter: function(value, row, index) {
                                if (value ==0 ){
                                    return "草稿";
                                }else if(value == 1 ){
                                    return "待审核";
                                }else if(value == 2 ){
                                    return "已发布";
                                }else if(value == 3 ){
                                    return "已拒绝";
                                }
                                return "";
                        }
                    },
                    {
                        title: '操作',
                        align: 'center',
                        formatter: function(value, row, index) {
                            var actions = [];
                            actions.push('<a title="审核" onclick="vue.audit('+ row.id+', '+row.state+')" href="javascript:;"  class="btn btn-xs btn-info" ><i class="icon-anchor bigger-120"></i></a> ')
                            actions.push('<a title="查看" onclick="vue.showTravel('+row.id+')" href="javascript:;"  class="btn btn-xs btn-message" ><i class="icon-adjust   bigger-120"></i></a> ')
                            actions.push('<a title="删除" onclick="vue.delete('+row.id+')" href="javascript:;"  class="btn btn-xs btn-danger" ><i class="icon-remove bigger-120"></i></a> ')
                            return actions.join('');
                        }
                    }]
            };
            $('#bootstrap-table').bootstrapTable(options);


        },
        audit:function (id, state){
            if(state != 1){
                layer.alert("已经审核通过请勿重复审核", {
                    title: '提示框',
                    icon: 5,
                });
                return;
            }else{
                layer.open({
                    type: 1,
                    title: "游记审核",
                    maxmin: true,
                    shadeClose: true, //点击遮罩关闭层
                    area: ['600px', '200px'],
                    content: $('#auditModal'),
                    btn: ['审核'],
                    yes: function (index, layero) {
                        ajaxPost("article", "/travels/audit", {id:id,state:$("#status").val()}, function (data){
                            window.location.reload();
                        });
                    }
                });
            }
        },
        showTravel:function (id){
            ajaxGet("article", "/travels/content", {id:id}, function (data){
                vue.content = data.data.content;
                layer.open({
                    type: 1,
                    title: "游记内容",
                    maxmin: true,
                    shadeClose: true, //点击遮罩关闭层
                    area: ['900px', '700px'],
                    content: $('#showModal'),
                    btn: ['OK'],
                    yes: function (index, layero) {
                        layer.close(index);
                    }
                });
            })
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
        this.init();
    }
})
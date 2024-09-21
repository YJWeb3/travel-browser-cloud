var vue = new Vue({
    el:"#app",
    data:{
        toasts:[]  //吐司
    },
    methods:{
        //查询
        search:function (){
            $('#bootstrap-table').bootstrapTable('selectPage', 1);
        },
        //初始化列表
        init:function (){
            var options = {
                url: getServiceUrl("article") + "/destinations",
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

                    params.parentId = $("#parentId").val();

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
                        field: 'name',
                        title: '目的地名称',
                        formatter:function (value, row, index){
                            var actions = [];
                            actions.push('<a title="编辑" onclick="vue.searchChildren(' + row.id + ')" ' + 'href="javascript:;"  class="btn-link" >'+value+'</a> ')
                            return actions.join('');
                        }
                    },
                    {
                        field: 'english',
                        title: '编号'
                    },
                    {
                        field: 'parentName',
                        title: '上级',
                        formatter:function (value, row, index){
                            return value ? value : "顶级"
                        }
                    },
                    {
                        field: 'info',
                        title: '简介',
                        formatter:function (value, row, index){
                            return tooltip(value);
                        }
                    },
                    {
                        title: '操作',
                        align: 'center',
                        formatter: function(value, row, index) {
                            var actions = [];
                            var info = encodeURI(row.info);
                            actions.push('<a title="编辑" onclick="vue.editDest(' + row.id + ', \'' + info + '\')" href="javascript:;"  class="btn btn-xs btn-info" ><i class="icon-edit bigger-120"></i></a> ')
                            actions.push('<a title="删除" onclick="vue.delete('+row.id+')" href="javascript:;"  class="btn btn-xs btn-danger" ><i class="icon-remove bigger-120"></i></a> ')
                            return actions.join('');
                        }
                    }]
            };
            $('#bootstrap-table').bootstrapTable(options);
        },
        editDest:function (id, info){
            info = decodeURI(info);
            var info = (info=='null' || info == '')?'':info;
            $("#info").val(info);
            layer.open({
                type: 1,
                title: '编辑',
                maxmin: true,
                shadeClose: true, //点击遮罩关闭层
                area: ['600px', '400px'],
                content: $('#editModal'),
                btn: ['提交', '取消'],
                yes: function (index, layero) {
                    ajaxPost("article", "/destinations/updateInfo", {info: $("#info").val(),id:id},function (data){
                        layer.alert("编辑成功", {
                            title: '提示框',
                            icon: 1,
                        },function (){
                            layer.close(index);
                            window.location.reload();
                        });

                    })
                }
            });
        },
        delete:function (id){
            layer.confirm('确认要删除吗？', function (index) {
                ajaxPost("article", "/destinations/delete/" + id, {}, function (data){
                    layer.msg('已删除!', {icon: 1, time: 200}, function (){
                        location.reload();
                    });
                })
            });
        },
        searchChildren:function (id){
            $("#parentId").val(id);//设置父id
            //刷新列表
            $('#bootstrap-table').bootstrapTable('selectPage', 1);

            //吐司
            ajaxGet("article", "/destinations/toasts", {destId:id}, function (data){
                vue.toasts = data.data;
            })
        }
    },
    mounted:function (){
        this.init();

    }
})
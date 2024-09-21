var vue = new Vue({
    el:"#app",
    data:{
        theme:{}
    },
    methods:{
        //更新时方法
        getEditParam:function (type){
            if(type == 'save'){
                return $("#saveForm").serialize()
            }else{
                return this.theme;
            }
        },
        //查询
        search:function (){
            $('#bootstrap-table').bootstrapTable('selectPage', 1);
        },

        //初始化列表
        init:function (){
            var options = {
                url: getServiceUrl("article") + "/strategies/themes/query",
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
                        field: 'name',
                        title: '主题名称'
                    },
                    {
                        field: 'state',
                        title: '是否正常',
                        formatter: function(value, row, index) {
                            return value == 1 ? '否' : '是'
                        }
                    },
                    {
                        field: 'seq',
                        title: '序号'
                    },
                    {
                        title: '操作',
                        align: 'center',
                        formatter: function(value, row, index) {
                            var actions = [];
                            actions.push('<a title="编辑" onclick="vue.editTheme('+row.id+')" href="javascript:;"  class="btn btn-xs btn-info" ><i class="icon-edit bigger-120"></i></a> ')
                            actions.push('<a title="删除" onclick="vue.delete('+row.id+')" href="javascript:;"  class="btn btn-xs btn-danger" ><i class="icon-remove bigger-120"></i></a> ')
                            return actions.join('');
                        }
                    }]
            };
            $('#bootstrap-table').bootstrapTable(options);
        },

        //添加编辑模态框
        showModel:function (url, msg, type){
            $("#saveForm").clearForm(true);
            $("#editForm").clearForm(true);

            layer.open({
                type: 1,
                title: type=='save'?'添加':'编辑' + '主题',
                maxmin: true,
                shadeClose: true, //点击遮罩关闭层
                area: ['600px', '400px'],
                content: $('#'+type+'Modal'),
                btn: ['提交', '取消'],
                yes: function (index, layero) {
                    var params = vue.getEditParam(type)
                    ajaxPost("article", url, params,function (data){
                        layer.alert(msg, {
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
        addTheme:function (){
            vue.showModel("/strategies/themes/save", "添加成功", "save");
        },
        editTheme:function (id){
            ajaxGet("article", "/strategies/themes/detail", {id:id}, function (data){
                vue.theme = data.data
                vue.showModel("/strategies/themes/update", "编辑成功", "edit");
            })
        },
        delete:function (id){
            layer.confirm('确认要删除吗？', function (index) {
                ajaxPost("article", "/strategies/themes/delete/" + id, {}, function (data){
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
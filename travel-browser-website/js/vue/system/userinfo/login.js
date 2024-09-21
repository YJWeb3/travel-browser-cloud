var vue = new Vue({
    el:"#app",
    data:{
    },
    methods:{
        login:function (){
            $.post(getServiceUrl('u') + "/users/login", $("#_j_login_form").serialize(), function (data){
                console.log(data);
                /**
                 * JsonReslut   --- data
                 * {
                 *    code:200,
                 *    msg:xxx,
                 *    data:{
                 *        token:xx
                 *        user:{...}
                 *    }
                 * }
                 *
                 */
                if(data.code == 200){
                    var map = data.data;
                    var token = map.token;  //后续后端获取当前登录用户信息
                    var user = map.user;  //前端页面需要显示用户信息

                    //sessionStorage  客户端技术可以在浏览器窗口存储数据, 一但关闭窗口,
                    // 数据就没了, 是如果多个窗口, 数据无法共享

                    //localStorage  客户端技术可以在浏览器窗口存储数据, 数据操作是永久

                    //cookie 客户端技术可以在浏览器窗口存储数据, 特点有时效性
                    //参数1:cookie的key值, 参数2: cookie的value值, 参数3: 有效时间, 单位天
                    Cookies.set('user', JSON.stringify(user), { expires: 1/48,path:'/'});
                    Cookies.set('token', token, { expires: 1/48,path:'/'});

                    //document.referrer 上一个请求路径
                    var url = document.referrer ? document.referrer : "/";
                    if(url.indexOf("regist.html") > -1 || url.indexOf("login.html") > -1){
                        url = "/";
                    }
                    window.location.href = url
                }else{
                    popup(data.msg);
                }
            })
        }
    },
    mounted:function () {
    }
});



//api ip与端口
var serverUrlMap = {
    member : "http://localhost:8081",
    article : "http://localhost:8082",
    data : "http://localhost:8083",
    comment : "http://localhost:8084",
    search : "http://localhost:8085",
    gateway : "http://localhost:9999"
}

function getServiceUrl(service){
    //return serverUrlMap[service]
    return serverUrlMap["gateway"] + "/" + service
}



//异步请求
function ajaxRequest(server, url,type, param, success, fail){
    url = getServiceUrl(server) +  url;
    $.ajax({
        type: type,
        url: url,
        data: param,
        dataType: 'json',
        beforeSend:function(xhr){
            xhr.setRequestHeader("token","token");
        },
        success:function (data) {
            if(!data){
                layer.alert('请求异常！', {
                    title: '提示框',
                    icon: 1,
                });
            }else{
                if(data.code == 200){
                    if(success){
                        success(data);
                    }
                }else if(data.code == 401){
                    //未登录
                    layer.alert('请先登录！', {
                        title: '提示框',
                        icon: 1,
                    });
                }else{
                    if(fail){
                        fail(data);
                    }else{
                        layer.alert(data.msg, {
                            title: '提示框',
                            icon: 5,
                        });
                    }
                }
            }
        },
        error:function () {
            layer.alert('网络不通，请联系管理员~！', {
                title: '提示框',
                icon: 5,
            });
        }

    })
}

//异步请求 get方式
function ajaxGet(server, url, params, success, fail) {
    ajaxRequest(server, url, "GET", params, success, fail);
}
//异步请求post方式
function ajaxPost(server, url, params, success, fail){
    ajaxRequest(server, url, "POST", params, success, fail);
}

//获取url上的请求参数
function getParams() {
    //获取问号及问号后面的内容
    var url = window.location.search;
    var params = new Object();
    if (url.indexOf("?") != -1) {
        //截取问号后面的内容,再使用&分割多个属性
        var arr = url.substr(1).split("&");
        for (var i = 0; i < arr.length; i++) {
            //使用=分割为keyvalue
            var keyValue = arr[i].split("=");
            params[keyValue[0]] = keyValue[1];
        }
    }
    return params;
}


//弹出，3秒消失
function popup(msg) {
    $('body').append('<div id="over_container"><div id="over_message">'+msg+'</div></div>')
    setTimeout(function () {
        $('#over_container').remove();
    }, 3000)
}

//格式转换
function dateFormat(date, pattern){
    if(!pattern){
        pattern = "YYYY-MM-DD"
    }
    return moment(date).format(pattern)
}

//分页方法
function buildPage(current, totalPages, doPage){
    $("#pagination").html('');
    $("#pagination").jqPaginator({
        totalPages: totalPages||1,
        visiblePages: 5,
        currentPage: current,
        prev: '<a class="prev" href="javascript:void(0);">上一页<\/a>',
        next: '<a class="next" href="javascript:void(0);">下一页<\/a>',
        page: '<a href="javascript:void(0);">{{page}}<\/a>',
        last: '<a class="last" href="javascript:void(0);" >尾页<\/a>',
        onPageChange: function(page, type) {
            if(type == 'change'){
                if(doPage){
                    doPage(page);
                }
            }
        }
    })
}



function getIndexTime(){
    var yy = new Date().getFullYear();
    var mm = new Date().getMonth()+1;
    var dd = new Date().getDate();
    var my = '';
    if(mm == 1){
        my = 'Jan';
    }else if(mm == 2){
        my = 'Feb';
    }else if(mm == 3){
        my = 'Mar';
    }else if(mm == 4){
        my = 'Apr';
    }else if(mm == 5){
        my = 'May';
    }else if(mm == 6){
        my = 'Jun';
    }else if(mm == 7){
        my = 'Jul';
    }else if(mm == 8){
        my = 'Aug';
    }else if(mm == 9){
        my = 'Sept';
    }else if(mm == 10){
        my = 'Oct';
    }else if(mm == 11){
        my = 'Nov';
    }else if(mm == 12){
        my = 'Dec';
    }

    return  '<span class="day">'+dd+'</span>/'+my+'.'+yy;
}


function tooltip (value) {
    var _value = value ? value : "-";
    if (_value.length > 20) {
        _value = _value.substr(0, 20) + "...";
        var action = [];
        action.push('<span title="'+value+'">'+_value+'</span>');
        return action.join("");
    }
    return _value;

}

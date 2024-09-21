var vue = new Vue({
    el:"#app",
    data:{
        regions:[],  //热门排序的区域集合
        destListLeft:[],
        destListRight:[],
        regionId:''
    },
    methods:{
        //热门区域
        hotRegion:function (){
           //查询热门区域：List<Region>
            ajaxGet("article", "/regions/hotList", {}, function (data) {
                vue.regions = data.data
            })


        },
        //区域切换
        regionChange:function (event, rid) {
            var _this =$(event.currentTarget);
            if(!rid){
                return;
            }
            $('.row-hot .r-navbar a').removeClass('on');
            _this.addClass('on');
            //非国内数据列表
            this.queryRegion(rid);
        },
        queryRegion:function (rid) {
            //list<Destination>
            ajaxGet("article","/destinations/hotList",{rid:rid}, function (data) {
                var list = data.data;
                vue.regionId=rid;
                var destListLeft = [];  //左边
                var destListRight = []; //右边
                //将list集合分成左右2边, 进行遍历显示
                for(var i = 0; i < list.length; i++){
                    if(i % 2 == 0){
                        destListLeft.push(list[i])
                    }else{
                        destListRight.push(list[i])
                    }
                }
                vue.destListLeft = destListLeft;
                vue.destListRight = destListRight;

            })
        }
    },
    //数据初始化位置
    mounted:function () {
        //热门区域
        this.hotRegion();
        //查询默认区域值-国内
        this.queryRegion(-1)
    }
});


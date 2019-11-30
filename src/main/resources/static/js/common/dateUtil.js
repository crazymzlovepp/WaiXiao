//将时间戳转换成对应日期格式
function dateUtil(dateStr,sdf){
     if(dateStr == null || dateStr == "" || dateStr == undefined || dateStr == "undefined"){
        return null;
     }
    if(sdf == null || sdf == "" || sdf == undefined || sdf == "undefined"){
        sdf = "yyyy-MM-dd HH:mm:ss";
    }

    var date = new Date(dateStr); //获取一个时间对象
    //下面是获取时间日期的方法，需要什么样的格式自己拼接起来就好了
    var year = date.getFullYear(); // 获取完整的年份(4位,1970)
    var month = date.getMonth()+1; // 获取月份(0-11,0代表1月,用的时候记得加上1)
    var day = date.getDate(); // 获取日(1-31)
    var milliseconds= date.getTime(); // 获取时间(从1970.1.1开始的毫秒数)
    var hour = date.getHours(); // 获取小时数(0-23)
    var minute = date.getMinutes(); // 获取分钟数(0-59)
    var second = date.getSeconds(); // 获取秒数(0-59)

    if(sdf == "yyyy-MM-dd HH:mm:ss"){
        return year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
    }
    if(sdf == "yyyy-MM-dd"){
        return year+"-"+month+"-"+day;
    }
    if(sdf == "HH:mm:ss"){
        return hour+"-"+minute+"-"+second;
    }
}
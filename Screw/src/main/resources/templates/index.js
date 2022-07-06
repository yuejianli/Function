$(function () {
    $("#downFile").hide();
})

//当点击自动生成时,进行处理
$("#autoCode").click(function () {
    let info = getInfo();
    console.log("输出值:" + JSON.stringify(info));
    $.ajax({
        async: false,
        type: "post",
        url: '/screw/self',
        data: JSON.stringify(info),
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        success: function (result) {
            Flavr.falert("文档生成成功");
        }
    });
})

//处理信息
function getInfo() {
    //1. 获取信息,进行组装
    let filePath = convertStr($("#filePath").val());
    let fileType = convertStr($("#fileType").val());
    let fileName = convertStr($("#fileName").val());
    let version = convertStr($("#version").val());
    let description = convertStr($("#description").val());

    //数据库的相关配置
    let driverClassName = convertStr($("#driverClassName").val());
    let url = convertStr($("#url").val());
    let userName = convertStr($("#userName").val());
    let password = convertStr($("#password").val());

    //项目配置
    //表配置

    let tableNameStr = convertStr($("#tableNameStr").val());
    let prefixTableNameStr = convertStr($("#prefixTableNameStr").val());
    let suffixTableNameStr = convertStr($("#suffixTableNameStr").val());
    let ignoreTableNameStr = convertStr($("#ignoreTableNameStr").val());
    let ignorePrefixTableNameStr = convertStr($("#ignorePrefixTableNameStr").val());
    let ignoreSuffixTableNameStr = convertStr($("#ignoreSuffixTableNameStr").val());


    return {
        "filePath": filePath,
        "fileType": fileType,
        "fileName": fileName,
        "version": version,
        "description": description,
        "driverClassName": driverClassName,
        "url": url,
        "userName": userName,
        "password": password,
        "tableNameStr": tableNameStr,
        "prefixTableNameStr": prefixTableNameStr,
        "suffixTableNameStr": suffixTableNameStr,
        "ignoreTableNameStr": ignoreTableNameStr,
        "ignorePrefixTableNameStr": ignorePrefixTableNameStr,
        "ignoreSuffixTableNameStr": ignoreSuffixTableNameStr,
    }

}

//测试数据库连接
$("#sqlConnection").click(function () {
    let info = getInfo();
    $.ajax({
        async: false,
        type: "post",
        url: '/screw/sqlConnection',
        data: JSON.stringify(info),
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        success: function (result) {
            Flavr.falert("测试成功");
        },
        fail: function () {
            Flavr.falert("测试失败");
        }
    });

})

function convertStr(content) {
    if (typeof content === "undefined" || content === null || content.trim() === "") {
        return "";
    }
    return content;
}
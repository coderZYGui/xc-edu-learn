<!DOCTYPE html>
<html>
<head>
    <meta charset="utf‐8">
    <title>Hello World!</title>
</head>
<body>
    Hello ${name}!
<hr>
    遍历数据模型中的list学生信息(数据模型中名称为 stus)
    <table border="1px" cellpadding="0px">
        <tr>
            <td>序号</td>
            <td>姓名</td>
            <td>年龄</td>
            <td>金额</td>
<#--            <td>出生日期</td>-->
        </tr>
        <#if stus??> <#-- 判断stus是否存在,如果不存在就不执行下面的了 -->
            <#list stus as stu>
                <tr>
                    <td>${stu_index+1}</td>
                    <td <#if stu.name='小明'>style="background: skyblue" </#if>>${stu.name}</td>
                    <td>${stu.age}</td>
                    <td <#if (stu.money > 300)>style="background: skyblue" </#if>>${stu.money}</td>
<#--                    <td>${stu.birthday}</td>-->
                </tr>
            </#list>
            <br>
            list的大小: ${stus?size}
        </#if>
    </table>
<hr>
    遍历数据模型中的stuMap(map数据)
    <br>
    第一种方法: 在中括号中填写map的key<br>
    姓名: ${stuMap['stu1'].name}<br>
    年龄: ${stuMap['stu1'].age}<br>

    第二种方法: 在map后面直接加"点 key"<br>
    姓名: ${stuMap.stu1.name}<br>
    年龄: ${stuMap.stu1.age}<br>

    第三种方法: 遍历map中的key.stuMap?keys就是key列表(是一个list)<br>
    <#list stuMap?keys as k>
        姓名: ${stuMap[k].name}<br>
        年龄: ${stuMap[k].age}<br>
    </#list>

</body>
</html>
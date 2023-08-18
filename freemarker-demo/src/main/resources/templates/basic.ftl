<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>01-basic</title>
</head>
<body>
<b>普通文本 String 展示：</b><br><br>
Hello ${name} <br>
<hr>
<b>对象Student中的数据展示：</b><br/>
姓名：${stu.name}<br/>
年龄：${stu.age}
<hr>

<table>
    <#list stus as stu>
        <#if stu.name='zhangsan'>
            <tr style="color: green">
            <td>${stu_index+1}</td>
            <td>${stu.name}</td>
            <td>${stu.age}</td>
            </tr>
            <#else>
                <tr >
                    <td>${stu_index+1}</td>
                    <td>${stu.name}</td>
                    <td>${stu.age}</td>
                </tr>
        </#if>

        </#list>
</table>


<ul>
    <#list userMap?keys as key>
        <li>key:${key}--value:${userMap["${key}"]}</li>
    </#list>
</ul>
</body>
</html>

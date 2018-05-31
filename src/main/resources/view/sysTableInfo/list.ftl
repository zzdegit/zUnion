<!DOCTYPE HTML>
<html>
	<header>
		<meta charset="UTF-8">
		<title></title>
		<style type="text/css">
			table,th,td{
				border:1px solid black;
				border-collapse: collapse;
			}
		</style>
	</header>
	<body>
		<a href="/sysTableInfo/toSaveOrUpdatePage">添加</a>
		</br>
		<table>
			<tr>
				<th>#</th>
				<th>name</th>
				<th>operateTime</th>
				<th>操作</th>
			</tr>
			<#list returnData as item>
				<tr>
					<td>${item_index}</td>
					<td>${item.name}</td>
					<td>${item.operateTime}</td>
					<td><a href="/sysTableInfo/delete?id=${item.id}">删除</a>&nbsp;<a href="/sysTableInfo/toSaveOrUpdatePage?id=${item.id}">修改</a></td>
				</tr>
			</#list>
		</table>
	</body>
</html>
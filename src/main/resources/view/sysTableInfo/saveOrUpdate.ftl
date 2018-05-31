<!DOCTYPE HTML>
<html>
	<header>
		<meta charset="UTF-8">
		<title></title>
		<style type="text/css">
			
		</style>
	</header>
	<body>
		<a href="/sysTableInfo/list">返回列表页</a>
		<form method="post" action="/sysTableInfo/saveOrUpdate">
			<input type="hidden" name="id" value="<#if returnData??>${returnData.id}</#if>">
			name:<input type="text" name="name" value="<#if returnData??>${returnData.name}</#if>"><br>
			operateTime :<input type="text" name="operateTime" value="<#if returnData??>${returnData.operateTime}</#if>"><br>
			<input type="submit" value="保存"/>
		</form>
	</body>
</html>

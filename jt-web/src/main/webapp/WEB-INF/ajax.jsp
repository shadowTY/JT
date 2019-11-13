<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>您好Springboot</title>
<!-- 引入jQuery函数类库  -->
<script type="text/javascript" src="/js/jquery-3.4.1.min.js"></script>
<script type="text/javascript">
	/* 实现异步请求，获取服务器数据*/
	 $(function(){
		// alert("页面加载完成");
		//  $.get("/findAjax",function(result){
				// alert("请求调用成功")
				/* each(data需要遍历的数据，遍历之后的数据需要做的操作)*/
				//$.each(result,function(index,user){
					/* index代表下标，user代表当前下标元素*/
						/* var user = result[index];
						var name = user.name; */
						//var id = user.id;
						//var name = user.name;
						//var age = user.age;
						//var sex = user.sex;
						//var tr = "<tr align='center'><td>"+id+"</td><td>"+name+"</td><td>"+age+"</td><td>"+sex+"</td></tr>";
						//$("#t1").append(tr);
					//});
			//});

			// $.load(); // 负责加载远程的html代码

			// 最重要的常规写法
			$.ajax({
				type:"get",
				url:"/findAjax",
				dataType:"json",
				success:function(result){
					// alert("调用成功")
					$.each(result,function(index,user){
						var tr = "<tr align='center'><td>"+user.id+"</td><td>"+user.name+"</td><td>"+user.age+"</td><td>"+user.sex+"</td></tr>";
						$("#t1").append(tr);
						})
					},
				error:function(){
					alert("调用失败")
					}
				});


			
		});
</script>
</head>
<body>
	<table id="t1" border="1px" width="65%" align="center">
		<tr>
			<td colspan="6" align="center"><h3>学生信息</h3></td>
		</tr>
		<tr>
			<th>编号</th>
			<th>姓名</th>
			<th>年龄</th>
			<th>性别</th>
		</tr>
	</table>
</body>
</html>
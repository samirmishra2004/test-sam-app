
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Dhan Vriksh</title>
<meta name="Generator" content="EditPlus">
<meta name="Author" content="">
<meta name="Keywords" content="">
<meta name="Description" content="">

<link href="<c:out value='/css/ShareMarket.css'/>" rel="stylesheet"
	type="text/css">

<script type="text/javascript">

function goBack(){
	document.forms[0].method="post";
	document.forms[0].action='/tradehome.do';
	document.forms[0].submit();
}
function save(){
	document.forms[0].method="post";
	document.forms[0].action='/updateBrokerDetail.do';
	document.forms[0].submit();
}
</script>
</head>
<body>
	<table width="100%" height="80" bgcolor="#FDF0CD" border=1>
		<tr height="100%">
			<td height="100%" align=center><b><font size="5px">Dhan
						Vriksh</font></b></td>
		</tr>
	</table>
	<form method=post>
	<table class="maintable" width="80%" border=1 height="100%" align=center>
	<th align=center height="20%" colspan=2>
	Broker Detail
	</th>
	<tr height="">
		<td width="50%" align=right>
		User Id
		</td>
		<td>
		<input type=text name=brkrUserId value="${brokerDetail.userid }" />
		</td>
	</tr>
	<tr height="">
		<td width="50%" align=right>
		Password1
		</td>
		<td>
		<input type=text name=brkrPwd1 value="${brokerDetail.pwd1 }"/>
		</td>
	</tr>
	<tr height="">
		<td width="50%" align=right>
		Password2
		</td>
		<td width="50%">
		<input type=text name=brkrPwd2 value="${brokerDetail.pwd2 }"/>
		</td>
	</tr>
	<tr height="">
		<td colspan=2 align=center>
		<table>
		<td>
		<input type=button name=saveBtn value="Save" onclick="save();"/>
		</td>
		<td>
		<input type=button name=brkrPwd2 value="Back" onclick="goBack();"/>
		</td>
		</table>
		</td>
	</tr>
	</table>
	</form>
	</body>
	</html>
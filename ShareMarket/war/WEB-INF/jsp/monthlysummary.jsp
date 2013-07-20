<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.TimeZone"%>
<%@page import="java.text.SimpleDateFormat"%>
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
<%

DecimalFormat df2 = new DecimalFormat("###.##");
%>
<script type="text/javascript">

function goBack(){
	document.forms[0].method="post";
	document.forms[0].action='/tradehome.do';
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

		<table class="maintable" width="100%" border=1 height="100%">
			<th align=center height="20%" colspan=5>Trade Summary</th>

			<tr>
				<th>MONTH</th>
				<th>Script Name</th>
				<th>Buy Value</th>
				<th>Sell Value</th>
				<th>Profit/Loss</th>
			</tr>
			<c:forEach var="tradeSummary" items="${tradeSummaryList}" varStatus="loopStatus">
	<c:set var="buyAmt" value="${tradeSummary.buyPrice }" scope="request"/>
	<c:set var="sellAmt" value="${tradeSummary.sellPrice }" scope="request"/>
	
	<c:set var="profitLoss" value="${sellAmt -buyAmt }" scope="request"/>
	
	<tr height="80%"><td>${tradeSummary.formatedDate}</td>
				<td>${tradeSummary.script}</td>
				<td><%=df2.format((Double)request.getAttribute("buyAmt")) %></td>
				<td><%=df2.format((Double)request.getAttribute("sellAmt")) %></td>
				
				<td>
				<c:if test="${buyAmt gt 0 and sellAmt gt 0 }">
				<%=df2.format((Double)request.getAttribute("profitLoss"))%>
				</c:if>
				</td>
		
		
	</tr>
	</c:forEach>
	<tr height="80%">
	<td colspan=3 align=center>&nbsp;</td>
				<td colspan=1 align="right">Total :</td>
				<td colspan=1 align=center>${totalProfitLoss}</td>
				
			</tr>
			
			<tr height="80%">
				<td colspan=5 align=center><input type=button name="backBtn"
					value="Back" onclick="goBack();" /></td>
				
			</tr>
		</table>
	</form>
</body>
</html>

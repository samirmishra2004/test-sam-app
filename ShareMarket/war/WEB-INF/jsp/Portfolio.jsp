<%@page import="com.share.trade.vo.ShareBucket"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
<title>Portfoliyo</title>
<%
List shareList=(List)request.getAttribute("ShareList");
%>
<style>
th {
	font: bold 11px "Trebuchet MS", Verdana, Arial, Helvetica,
	sans-serif;
	color: #6D929B;
	border-right: 1px solid #C1DAD7;
	border-bottom: 1px solid #C1DAD7;
	border-top: 1px solid #C1DAD7;
	letter-spacing: 2px;
	text-transform: uppercase;	
	padding: 6px 6px 6px 12px;
	background: #CAE8EA url(images/bg_header.jpg) no-repeat;
}

th.nobg {
	border-top: 0;
	border-left: 0;
	border-right: 1px solid #C1DAD7;
	background: none;
}

th.spec {	
	border-left: 1px solid #C1DAD7;
	border-top: 0;
	background: #fff url(images/bullet1.gif) no-repeat;
	font: bold 10px "Trebuchet MS", Verdana, Arial, Helvetica,
	sans-serif;
}

th.specalt {
	border-left: 1px solid #C1DAD7;
	border-top: 0;
	background: #f5fafa url(images/bullet2.gif) no-repeat;
	font: bold 10px "Trebuchet MS", Verdana, Arial, Helvetica,
	sans-serif;
	color: #B4AA9D;
}
td {
	border-right: 1px solid #C1DAD7;
	border-bottom: 1px solid #C1DAD7;
	background: #fff;
	padding: 6px 6px 6px 12px;
	color: #6D929B;
}


td.alt {
	background: #F5FAFA;
	color: #B4AA9D;
}
</style>
<script type="text/javascript">
function addSymbolToPortfoliyo(){
	frm=document.forms[0];
	frm.action="addToPortfoliyo.do";
	if(confirm("Are you sure?")){
	frm.submit();
	//alert("gdg");
	}
}

var deleteList="";
function addToDeleteList(val){
	//alert(""+deleteList);
	deleteList=deleteList+val+"|";
}
function deleteShares(){
	//alert(deleteList);
	frm=document.forms[0];
	frm.deleteList.value=deleteList;
	frm.action="removePortfoliyo.do";
	if(confirm("Are you sure?")){
	frm.submit();	
	}
}

</script>
</head>
<body>
<form name="portfolioForm" action="">
<table id="mytable" align=center cellspacing="0" summary="The technical
specifications of the Apple PowerMac G5 series">
<caption><b>PORTFOLIO</b></caption>
<tr>
  <th scope="col" abbr="SYMBOL">SYMBOL</th>
  <th scope="col" abbr="BUY PRICE">QUANTITY</th>
  <th scope="col" abbr="BUY PRICE">BUY PRICE</th>
  <th scope="col" abbr="BUY DATE">BUY DATE</th>
  <th scope="col" abbr="CURRENT PRICE">CURRENT PRICE</th>
  <th scope="col" abbr="update/delet">SELECT</th>
</tr>
<%
if(shareList!=null){
	ShareBucket bucket=null;
	System.out.print("shareList"+ shareList);
for(int i=0;i<shareList.size();i++){
	 bucket=(ShareBucket)shareList.get(i);
	%>
	<tr>
	<th scope="row" class="spec"><%=bucket.getsSymbol() %></th>
	 <td><%=bucket.getsQuantity()%></td>
	 <td><%=bucket.getsBuyPrice()%></td>
	 <td><%=bucket.getsBuyDate()%></td>
	 <td>&nbsp;</td>
	 <td><input type="checkbox" name="check_<%=i%>" value="<%=bucket.getKey()%>" onclick="addToDeleteList(this.value);"></td>
  
  
</tr>
	<%
}
}
%>
<input type=hidden name="deleteList" value=""/>
<tr>
  <th colspan=5 class=nobg align="right"><input type=button name=updateSymbol value=update>
  <input type=button name=deleteSymbol value=delete onclick="deleteShares();"></th>
  
  
</tr>
<tr>
<td colspan=5>


<table id="mytable" align=center cellspacing="0" summary="The technical
specifications of the Apple PowerMac G5 series">
<caption><b>Add Symbol to Portfolio</b></caption>
<tr>
  <th scope="col" abbr="SYMBOL">SYMBOL</th>
  <th scope="col" abbr="BUY PRICE">BUY PRICE</th>
   <th scope="col" abbr="CURRENT PRICE">QUANTITY</th>
  <th scope="col" abbr="BUY DATE">BUY DATE</th>
 
  
</tr>

<tr>
<spring:bind path="portFoliyoForm.adSymbol"> 
  <td><input type=text name="adSymbol" value=""></td>
</spring:bind>
  <td><input type=text name="adBuyPrice" value=""></td>
  <td><input type=text name="adQuantity" value=""></td>
  <td><input type=text name="adBuyDate" value=""></td>
  
  
</tr>
<tr>
  <td colspan=4 align=right><input type=button name="addSymbol" value="Add" onclick="javascript:addSymbolToPortfoliyo()"></td>
  
  
</tr>
</table>
</td>
</tr>
</table>
</form>
</body>
</html>
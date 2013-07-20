<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
  <title> Dhan Vriksh </title>
  <meta name="Generator" content="EditPlus">
  <meta name="Author" content="">
  <meta name="Keywords" content="">
  <meta name="Description" content="">
  <script type="text/javascript">
function goBack(){
	document.forms[0].method="post";
	document.forms[0].action='/tradehome.do';
	document.forms[0].submit();
}
function save(){
	document.forms[0].method="post";
	document.forms[0].action='/strategy/savestrategy.do';
	document.forms[0].submit();
}
  </script>
  </head>
  <body>
  <table width="100%" height="80"  bgcolor="#FDF0CD" border=1>
<tr height="100%">
	<td height="100%" align=center> <b><font  size="5px">Dhan Vriksh</font></b></td>
</tr>
</table>
  <form method=post>
  
  <table width="100%" border=1 height="100%">
	<th align=center height="20%" colspan=2>
	Strategy 
	</th>
	
	<tr height="80%">
		<td align="center">
		Parameter Name
		</td>
		<td align="center">
		Parameter Value
		</td>
	</tr>
	
	<tr height="80%">
		<td>
		Trade 
		</td>
		<td>
		ON:<input type=radio name=tradeOnOff value="1" <c:if test="${strategy.tradeOnOff eq '1' }">checked</c:if>/>
		&nbsp;&nbsp;&nbsp;
		OFF:<input type=radio name=tradeOnOff value="0" <c:if test="${strategy.tradeOnOff eq '0' }">checked</c:if>/>
		</td>
	</tr>
	<tr height="80%">
		<td>
		AutoTrade 
		</td>
		<td>
		ON:<input type=radio name=autoTrade value="true" <c:if test="${strategy.autoTrade}">checked</c:if>/>
		&nbsp;&nbsp;&nbsp;
		OFF:<input type=radio name=autoTrade value="false" <c:if test="${!strategy.autoTrade}">checked</c:if>/>
		</td>
	</tr>
	<tr height="80%">
		<td>
		Global Sentiment 
		</td>
		<td>
		<select id=globalSentiment name=globalSentiment>
		<option value="1" <c:if test="${strategy.globalSentiment eq '1' }">selected</c:if>>BULISH</option>
		<option value="0" <c:if test="${strategy.globalSentiment eq '0' }">selected</c:if>>NEUTRAL</option>
		<option value="-1" <c:if test="${strategy.globalSentiment eq '-1' }">selected</c:if>>BEARISH</option>
		</select>
		</td>
	</tr>
	<tr height="80%">
		<td>
		Buy Factor 
		</td>
		<td>
		<input type=text name=buyFactor value="${strategy.buyFactor }"/>
		</td>
	</tr>
	<tr height="80%">
		<td>
		Sell Factor 
		</td>
		<td>
		<input type=text name=sellFactor value="${strategy.sellFactor }"/>
		</td>
	</tr>
	<tr height="80%">
		<td>
		Total Trade Value 
		</td>
		<td>
		<input type=text name=tradeAmount value="${strategy.tradeAmount }"/>
		</td>
	</tr>
	<tr height="80%">
		<td>
		Trade Segment 
		</td>
		<td>
		<select id=tradeSegment name=tradeSegment>
		<option value="FUTURE" <c:if test="${strategy.tradeSegment eq 'FUTURE' }">selected</c:if>>FUTURE</option>
		<option value="EQUITY" <c:if test="${strategy.tradeSegment eq 'EQUITY' }">selected</c:if>>EQUITY</option>
		
		</select>
		</td>
	</tr>
	<tr height="80%">
		<td>
		Force Squareoff 
		</td>
		
		<td>
		ON:<input type=radio name=forceSquareOff value="true" <c:if test="${strategy.forcesSquareOff}">checked</c:if>/>
		&nbsp;&nbsp;&nbsp;
		OFF:<input type=radio name=forceSquareOff value="false" <c:if test="${!strategy.forcesSquareOff}">checked</c:if>/>
		</td>
	</tr>
	
	<tr height="80%">
		<td>
		Open position before 
		</td>
		<td>
		HH:<input type=text name=openPositionHour value="${strategy.positionOpenHour }"/>
		MM:<input type=text name=openPositionMinut value="${strategy.positionOpenMinut }"/>
		</td>
	</tr>
	<tr height="80%">
		<td>
		Squareoff position after 
		</td>
		<td>
		HH:<input type=text name=closePositionHour value="${strategy.positionCloseHour }"/>
		MM:<input type=text name=closePositionMinut value="${strategy.positionCloseMinut }"/>
		</td>
	</tr>
	<tr height="80%">
		<td colspan=1 align=right>
		<input type=button name="strategyBtn" value="Save" onclick="save();"/>
		</td>
		<td colspan=1 align=left>
		<input type=button name="backBtn" value="Back" onclick="goBack();"/>
		</td>
	</tr>
	</table>
	</form>
</body>
</html>	
	
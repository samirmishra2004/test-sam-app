<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<table width="100%" border=1 height="100%">
	<th align=center height="20%">
	Script Mapper &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	
	<a href="#" onclick="showAddScriptPopUp();">Add Script</a>
	</th>
	<tr height="80%">
		<td>
		
	<table width="100%" id="scriptTable"  border=1>
	<tr>
	<td align=center>Buy</td>
	<td align=center>Sell</td>
		<td align=center>Watcher Script</td>
			<td align=center>Trading Script</td>
			<td align=center>Quantity</td>
			<td align=center>HighBeta</td>
				<td align=center>Active</td>
				<td align=center>Edit/Delete</td>
				
	</tr>
	

<c:if test="${mapperDTOs != null }">
<c:forEach var="script" items="${mapperDTOs}" varStatus="loop">
<tr>
		<input type="hidden" name="scrtId_${loop.index}" id="scriptId_${loop.index}" value="${script.key}"/>
		<td align=center><input type=button id="buyBtn_${loop.index}" value="Buy" <c:if test="${script.isActive eq '0' }">disabled</c:if> onClick="buy('${loop.index}');"/></td>
		<td align=center><input type=button id="sellBtn_${loop.index}" value="Sell" <c:if test="${script.isActive eq '0' }">disabled</c:if> onClick="sell('${loop.index}');"/></td>
		<td align=center><input type="text" name="watcherScript" id="watcherScript_${loop.index}" value="${script.wtcher_script}" style="border:0;"  readonly=true /></td>
			<td align=center><input type="text" name="tradingScript" id="tradingScript_${loop.index}" value="${script.broker_script}" style="border:0;"  readonly=true /></td>
			<td align=center><input type="text" name="tradeQuantity" id="tradeQuantity_${loop.index}" value="${script.tradeQuantity}" style="border:0;"  readonly=true /></td>
			
			<td align=center><input type="checkbox" name="highBeta" id="highBeta_${loop.index}" disabled  <c:if test="${script.highBeta eq '1' }">checked='true'</c:if> /></td>
				<td align=center><input type="checkbox" name="scriptActive" id="scriptActive_${loop.index}" disabled  <c:if test="${script.isActive eq '1' }">checked='true'</c:if> /></td>
				<td align=center><table><tr><td>
				<img src="images/edit.jpg" width="20" height="20" border="0" alt=""  id="scriptEdit_${loop.index}" style="cursor:hand;"  onclick="cancelEditing(previousEdit);enableEdit(${loop.index});" /> 
				</td><td>
				<img src="images/save.jpg" width="20" height="20" border="0" alt="Update"   id="scriptUpdate_${loop.index}" style="display:none;" onclick="updateScript(${loop.index})"/>
				</td><td>
				<img src="images/delete.jpg" width="20" height="20" border="0" alt="Delete"  id="scriptDelete_${loop.index}" onclick="deleteScript(${loop.index});"/>
				</td><td>
				<img src="images/cancel.jpg" width="20" height="20" border="0" alt="Cancel"  id="scriptCancell_${loop.index}" style="display:none;" onclick="cancelEditing(${loop.index});"/>
				</td>
				</tr></table>
				</td>
	</tr>
</c:forEach>
</c:if>
<c:if test="${mapperDTOs eq null }">
	<tr>
		<td align=center colspn=3>Please add a script</td>
	</tr>
</c:if>
	</table>
		</td>
	</tr>
	</table>
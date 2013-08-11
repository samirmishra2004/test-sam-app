<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<table width="80%" border=0 height="" class="searchResult">
	<th align=center height="20%">
	Future Scripts &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	
	<a href="#" onclick="showAddScriptPopUp();">Add Script</a>
	</th>
	<tr>
		<td>
		
	<table width="80%" id=""  border=0>
	<tr>
	
		<td align=center  class=headerCellLong>Watcher Script URL</td>
			<td align=center  class=headerCell>Exp. Date 1</td>
			<td align=center class=headerCell>Exp. Date 2</td>
				<td align=center class=headerCell>Trdate Script1</td>
				<td align=center class=headerCell>Trdate Script2</td>
				<td align=center class=headerCell>LOT Size</td>
				<td align=center class=headerCell>Max Gap</td>
				<td align=center class=headerCell>Min Gap</td>
				<td align=center class=headerCell>Trade Status</td>				
				<td align=center class=headerCell>Edit/Delete</td>
				
	</tr>
	

<c:if test="${mapperDTOs != null }">
<c:forEach var="script" items="${mapperDTOs}" varStatus="loop">
<tr>
		<input type="hidden" name="scrtId_${loop.index}" id="scriptId_${loop.index}" value="${script.key}"/>
		
			<td align=center class=><input type="text" name="watcherScriptUrl" id="watcherScriptUrl_${loop.index}" value="${script.watcherScriptUrl}" style="border:0;"  readonly=true />&nbsp;</td>
			<td align=center   class=><input type="text" name="expDate1" id="expDate1_${loop.index}" value="${script.expDate1}" style="border:0;"  readonly=true /></td>
			<td align=center   class=><input type="text" name="expDate2" id="expDate2_${loop.index}" value="${script.expDate2}" style="border:0;"  readonly=true /></td>
			<td align=center   class=><input type="text" name="tradeScript1" id="tradeScript1_${loop.index}" value="${script.tradeScript1}" style="border:0;"  readonly=true /></td>
			<td align=center  class=><input type="text" name="tradeScript2" id="tradeScript2_${loop.index}" value="${script.tradeScript2}" style="border:0;"  readonly=true /></td>
			<td align=center  class=><input type="text" name="lotSize" id="lotSize_${loop.index}" value="${script.lotSize}" style="border:0;"  readonly=true /></td>
			
			<td align=center  class=><input type="text" name="maxGap" id="maxGap_${loop.index}" value="${script.maxGap}" style="border:0;"  readonly=true /></td>
			<td align=center  class=><input type="text" name="minGap" id="minGap_${loop.index}" value="${script.maxGap}" style="border:0;"  readonly=true /></td>
			<td align=center  class=><input type="text" name="tradeOpenClose" id="tradeStatus_${loop.index}" value="${script.tradeOn}" style="border:0;"  readonly=true /></td>
				
			<td align=center  class=><table><tr><td>
				<img src="images/edit.jpg" width="20" height="10" border="0" alt=""  id="scriptEdit_${loop.index}" style="cursor:hand;"  onclick="cancelEditing(previousEdit);enableEdit(${loop.index});" /> 
				</td><td>
				<img src="images/save.jpg" width="20" height="10" border="0" alt="Update"   id="scriptUpdate_${loop.index}" style="display:none;" onclick="updateScript(${loop.index})"/>
				</td><td>
				<img src="images/delete.jpg" width="20" height="10" border="0" alt="Delete"  id="scriptDelete_${loop.index}" onclick="deleteScript(${loop.index});"/>
				</td><td>
				<img src="images/cancel.jpg" width="20" height="10" border="0" alt="Cancel"  id="scriptCancell_${loop.index}" style="display:none;" onclick="cancelEditing(${loop.index});"/>
				</td>
				</tr></table>
			</td>
	</tr>
</c:forEach>
</c:if>
<tr>
		
		<td colspan=10 align=center>
		<input type=button name="backBtn" value="Back" onclick="goBack();"/>
		</td>
	</tr>
<c:if test="${mapperDTOs eq null }">
	<tr>
		<td align=center colspan=9>Please add a script</td>
	</tr>
</c:if>
	</table>
		</td>
	</tr>
	</table>
	<script>
	document.getElementById("addScript").style.display="none";
	</script>
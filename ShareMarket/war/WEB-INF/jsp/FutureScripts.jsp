
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title> Dhan Vriksh </title>
  <meta name="Generator" content="EditPlus">
  <meta name="Author" content="">
  <meta name="Keywords" content="">
  <meta name="Description" content="">
  <link href="<c:out value='/css/ui.css'/>" rel="stylesheet"
	type="text/css">
  <style type="text/css">
  
  .PopupPanel
{
    visibility:visible;
    border: solid 1px black;
    position: absolute;
    left: 50%;
    top: 50%;
    background-color: white;
    z-index: 50;

    height: 100px;
    margin-top: -200px;

    width: 500px;
    margin-left: -300px;
    
}
  </style>
  <script src='js/jquery-1.9.1.min.js' type="text/javascript"></script>
   <script type="text/javascript">
   var wsurl;
	var expd1;
	var expd2;
	var lsize;
	var trdsc1;
	var trdsc2;
	var previousEdit=-1;
 
	$(function(){
		
			// setInterval( "refreshLog()",120000);
			
	});
	function showAddScriptPopUp(){
		document.getElementById('addScript').style.display='block';
		document.getElementById('addScript').className='PopupPanel';
	}

	function hidePopup(event){
		
		 if(document.getElementById('addScript').style.display=='block'){
			 document.getElementById('addScript').style.display='none';
		 }
		
	};
	function goBack(){
		document.forms[0].method="post";
		document.forms[0].action='/tradehome.do';
		document.forms[0].submit();
	}
	function enableEdit(index){
		//alert("in edit");
			var watcherScriptUrl=document.getElementById('watcherScriptUrl_'+index);
			var exp1=document.getElementById('expDate1_'+index);
			var exp2=document.getElementById('expDate2_'+index);
			var tradeScr1=document.getElementById('tradeScript1_'+index);
			var tradeScr2=document.getElementById('tradeScript2_'+index);
			var lotSize=document.getElementById('lotSize_'+index);

			var editBtn=document.getElementById('scriptEdit_'+index);
			var deleteBtn=document.getElementById('scriptDelete_'+index);
			var saveBtn=document.getElementById('scriptUpdate_'+index);
			var cancelBtn=document.getElementById('scriptCancell_'+index);

			wsurl=watcherScriptUrl.value;
			expd1=exp1.value;
			expd2=exp2.value;
			lsize=lotSize.value;
			trdsc1=tradeScr1.value;
			trdsc2=tradeScr2.value;

			previousEdit=index;
		
			watcherScriptUrl.style.border="";
			watcherScriptUrl.readOnly=false;
		//alert(1)
			exp1.style.border="";
			exp1.readOnly=false;

			exp2.style.border="";
			exp2.readOnly=false;

			//alert(2)
			lotSize.style.border="";
			lotSize.readOnly=false;

			tradeScr1.style.border="";
			tradeScr1.readOnly=false;
			tradeScr2.style.border="";
			tradeScr2.readOnly=false; 
			
			

			editBtn.style.display="none";
			deleteBtn.style.display="none";
			saveBtn.style.display="block";
			cancelBtn.style.display="block";

		}

	function cancelEditing(index){
		if(index>-1){
		//alert(index)
		var watcherScriptUrl=document.getElementById('watcherScriptUrl_'+index);
		var exp1=document.getElementById('expDate1_'+index);
		var exp2=document.getElementById('expDate2_'+index);
		var tradeScr1=document.getElementById('tradeScript1_'+index);
		var tradeScr2=document.getElementById('tradeScript2_'+index);
		var lotSize=document.getElementById('lotSize_'+index);

		editBtn=document.getElementById('scriptEdit_'+index);
		deleteBtn=document.getElementById('scriptDelete_'+index);
		saveBtn=document.getElementById('scriptUpdate_'+index);
		cancelBtn=document.getElementById('scriptCancell_'+index);


		watcherScriptUrl.value=wsurl;
		exp1.value=expd1;
		exp2.value=expd2;
		lotSize.value=lsize;
		tradeScr1.value=trdsc1;
		tradeScr2.value=trdsc2;

		
		watcherScriptUrl.style.border=0;
		watcherScriptUrl.readOnly=true;
	//alert(1)
		exp1.style.border=0;
		exp1.readOnly=true;

		exp2.style.border=0;
		exp2.readOnly=true;

		//alert(2)
		lotSize.style.border=0;
		lotSize.readOnly=true;

		tradeScr1.style.border=0;
		tradeScr1.readOnly=true;
		tradeScr2.style.border=0;
		tradeScr2.readOnly=true; 


		

		editBtn.style.display="block";
		deleteBtn.style.display="block";
		saveBtn.style.display="none";
		cancelBtn.style.display="none";
		previousEdit=-1;
		
		}
	}
		
		function addNewScript(){
			//alert("add new script");
			watcherScriptUrl=document.getElementById("watcherScriptUrl").value;
			exp1=document.getElementById("exp1").value;
			exp2=document.getElementById("exp2").value;
			$("#contentDiv").load("addOrUpdateFnoScript.do?watcherScriptUrl="+watcherScriptUrl+"&exp1="+exp1+"&exp2="+exp2);
		}

		function updateScript(index){
			
			scriptId=document.getElementById('scriptId_'+index).value;
			//alert("update script"+scriptId);
			var watcherScriptUrl=document.getElementById('watcherScriptUrl_'+index).value;
			var exp1=document.getElementById('expDate1_'+index).value;
			var exp2=document.getElementById('expDate2_'+index).value;
			var tradeScr1=document.getElementById('tradeScript1_'+index).value;
			var tradeScr2=document.getElementById('tradeScript2_'+index).value;
			var lotSize=document.getElementById('lotSize_'+index).value;
			
			var url="addOrUpdateFnoScript.do?watcherScriptUrl="+watcherScriptUrl+"&exp1="+exp1;
			url=url+"&exp2="+exp2+"&trdsc1="+tradeScr1+"&trdsc2="+tradeScr2;
			url=url+"&lotSize="+lotSize+"&scId="+scriptId;
			url=encodeURI(url);
			//alert(url)
			$("#contentDiv").load(url);
		}

		function deleteScript(index){
			if(confirm("Are you sure you want to delete?")){
			scriptId=document.getElementById('scriptId_'+index).value;
			url="deleteFnoScript.do?scriptId="+scriptId;
			
			$("#contentDiv").load(url);
			}
		}
	
	</script>
</head>
<body>
  <form>
  <!--header-->
<table width="100%" height="80"  bgcolor="#FDF0CD" border=0>
<tr height="100%">
	<td height="100%" align=center> <b><font  size="5px">Dhan Vriksh</font></b></td>
</tr>
</table>
<div id="contentDiv">
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
			<td align=center  class=><input type="text" name="minGap" id="minGap_${loop.index}" value="${script.minGap}" style="border:0;"  readonly=true /></td>
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
	</div>
</form>

 <div id="addScript" class="" style="display:none;">
 <table>
 <tr>
		<td align=center>Watcher Url</td>
			<td align=center>Exp1</td>
			<td align=center>Exp2</td>				
				<td align=center>&nbsp;</td>
				
	</tr>
	<tr>
		<td align=center><input type="text" name="watcherScriptUrl" id="watcherScriptUrl" value=""></td>
			<td align=center><input type="text" name="exp1" id="exp1" value=""></td>
			<td align=center><input type="text" name="exp2" id="exp2" value=""></td>				
				<td align=center><input type="button" name="addScript" value="Add" onclick="addNewScript();"><label id="hidePopUp" onclick="hidePopup();">hide</label></td>
				
	</tr>
 </table>
 </div>
</body>
</html>
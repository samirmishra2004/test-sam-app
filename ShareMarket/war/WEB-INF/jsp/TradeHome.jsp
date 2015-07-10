<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
  <title> Dhan Vriksh </title>
  <meta name="Generator" content="EditPlus">
  <meta name="Author" content="">
  <meta name="Keywords" content="">
  <meta name="Description" content="">
  <style type="text/css">
  
  .PopupPanel
{
    visibility:visible;
    border: solid 1px black;
    position: absolute;
    left: 50%;
    top: 50%;
    background-color: white;
    z-index: 100;

    height: 50px;
    margin-top: -200px;

    width: 400px;
    margin-left: -300px;
    
}
  </style>
  <script src='js/jquery-1.9.1.min.js' type="text/javascript"></script>

 <script type="text/javascript">

	var wsov;
	var tsov;
	var asc;
	var tq;
	var hb;
	var previousEdit=-1;
 
	$(function(){
		
			// setInterval( "refreshLog()",120000);
			
	});
	function showAddScriptPopUp(){
		document.getElementById('addScript').style.visibility='visible';
		document.getElementById('addScript').className='PopupPanel';
	}

	function hidePopup(event){
		
		 if(document.getElementById('addScript').style.visibility=='visible'){
			 document.getElementById('addScript').style.visibility='hidden';
		 }
		
	};

	function refreshLog(){
		$("#contentDiv").load("refreshlog.do")
	}

	function loadScriptMapper(){
		$("#contentDiv").load("showMappedScript.do")
		
	}

	function enableEdit(index){
		//alert("in edit");
			watcherScript=document.getElementById('watcherScript_'+index);
			tradingScript=document.getElementById('tradingScript_'+index);
			activeChkBox=document.getElementById('scriptActive_'+index);
			tradeQuantity=document.getElementById('tradeQuantity_'+index);
			highBeta=document.getElementById('highBeta_'+index);
			editBtn=document.getElementById('scriptEdit_'+index);
			deleteBtn=document.getElementById('scriptDelete_'+index);
			saveBtn=document.getElementById('scriptUpdate_'+index);
			cancelBtn=document.getElementById('scriptCancell_'+index);

			asc=activeChkBox.checked;
			tsov=tradingScript.value;
			wsov=watcherScript.value;
			tq=tradeQuantity.value;
			hb=highBeta.checked;
			previousEdit=index;
		//$("watcherScript_0").css("background-color","yellow");

		//$("watcherScript_0").css("border","1");
		//alert(watcherScript.style.border)
		//alert(tradingScript)
			watcherScript.style.border="";
			watcherScript.readOnly=false;
		//alert(1)
			tradingScript.style.border="";
			tradingScript.readOnly=false;

			//alert(2)
			tradeQuantity.style.border="";
			tradeQuantity.readOnly=false;

			
			highBeta.disabled=false;
			
			activeChkBox.disabled=false;

			editBtn.style.display="none";
			deleteBtn.style.display="none";
			saveBtn.style.display="block";
			cancelBtn.style.display="block";

		}

		function cancelEditing(index){
			if(index>-1){
			//alert(index)
			watcherScript=document.getElementById('watcherScript_'+index);
			tradingScript=document.getElementById('tradingScript_'+index);
			tradeQuantity=document.getElementById('tradeQuantity_'+index);
			highBeta=document.getElementById('highBeta_'+index);
			activeChkBox=document.getElementById('scriptActive_'+index);
			editBtn=document.getElementById('scriptEdit_'+index);
			deleteBtn=document.getElementById('scriptDelete_'+index);
			saveBtn=document.getElementById('scriptUpdate_'+index);
			cancelBtn=document.getElementById('scriptCancell_'+index);



			watcherScript.value=wsov;
			tradingScript.value=tsov;
			tradeQuantity.value=tq;
			highBeta.checked=hb;
			activeChkBox.checked=asc;
			
			watcherScript.style.border=0;
			watcherScript.readOnly=true;
			//alert(1)
			tradingScript.style.border=0;
			tradingScript.readOnly=true;

			//alert(2)
			tradeQuantity.style.border=0;
			tradeQuantity.readOnly=true;

			
			highBeta.disabled=true;
			
			activeChkBox.disabled=true;

			editBtn.style.display="block";
			deleteBtn.style.display="block";
			saveBtn.style.display="none";
			cancelBtn.style.display="none";
			previousEdit=-1;
			
			}
		}

		function addNewScript(){
			//alert("add new script");
			watcherScript=document.getElementById("watcherScriptName").value;
			traderScript=document.getElementById("traderScriptName").value;
			trdAmt="0.0";
			$("#contentDiv").load("addnewscript.do?watcherScript="+watcherScript+"&traderScript="+traderScript+"&tradeQuantity=0.00");
		}

		function updateScript(index){
			
			scriptId=document.getElementById('scriptId_'+index).value;
			//alert("update script"+scriptId);
			watcherScript=document.getElementById('watcherScript_'+index).value;
			tradingScript=document.getElementById('tradingScript_'+index).value;
			activeChkBox=document.getElementById('scriptActive_'+index);
			tradeQuantity=document.getElementById('tradeQuantity_'+index).value;
			highBeta=document.getElementById('highBeta_'+index);
			isActive="0";
			isHighBeta="0";
			if(activeChkBox.checked){
				isActive="1";
			}
			if(highBeta.checked){
				isHighBeta="1";
			}
			url="updatescript.do?watcherScript="+watcherScript+"&traderScript="+tradingScript;
			url=url+"&activeChkBox="+isActive+"&scriptId="+scriptId+"&tradeQuantity="+tradeQuantity;
			url=url+"&highBeta="+isHighBeta;
			url=encodeURI(url);
			//alert(url)
			$("#contentDiv").load(url);
		}

		function deleteScript(index){
			if(confirm("Are you sure you want to delete?")){
			scriptId=document.getElementById('scriptId_'+index).value;
			url="deletescript.do?scriptId="+scriptId;
			
			$("#contentDiv").load(url);
			}
		}
 function buy(index){
	 	watcherScript=document.getElementById('watcherScript_'+index).value;
		tradingScript=document.getElementById('tradingScript_'+index).value;
		//activeChkBox=document.getElementById('scriptActive_'+index);
		tradeQuantity=document.getElementById('tradeQuantity_'+index).value;
		buyOrSell="B";
		requrl="placeorder.do?watcherScript="+watcherScript;
		requrl=requrl+"&tradingScript="+tradingScript+"&tradeQuantity="+tradeQuantity;
		requrl=requrl+"&buyOrSell="+buyOrSell;
		requrl=encodeURI(requrl);
		if(confirm("LONG: Are you sure ?")){
		$.ajax({
	        url : requrl,
	        type: 'GET',
	        success : handleData
	    })
		}
		//alert(index+ "watcherScript:"+watcherScript+" tradingScript:"+tradingScript+" tradeQuantity:"+tradeQuantity);
 }
 function sell(index){
	 	watcherScript=document.getElementById('watcherScript_'+index).value;
		tradingScript=document.getElementById('tradingScript_'+index).value;
		//activeChkBox=document.getElementById('scriptActive_'+index);
		tradeQuantity=document.getElementById('tradeQuantity_'+index).value;
		buyOrSell="S";

		requrl="placeorder.do?watcherScript="+watcherScript;
		requrl=requrl+"&tradingScript="+tradingScript+"&tradeQuantity="+tradeQuantity;
		requrl=requrl+"&buyOrSell="+buyOrSell;
		requrl=encodeURI(requrl);

if(confirm("SORT: Are you sure ?")){
		$.ajax({
	        url : requrl,
	        type: 'GET',
	        success : handleData
	    })
}	
		//alert(index+ "watcherScript:"+watcherScript+" tradingScript:"+tradingScript+" tradeQuantity:"+tradeQuantity);
 }
 function handleData(obj){

alert("Message: "+obj);

 }
 </script>
 
 </head>

 <body>
  <form>
  <!--header-->
<table width="100%" height="80"  bgcolor="#FDF0CD" border=1>
<tr height="100%">
	<td height="100%" align=center> <b><font  size="5px">Dhan Vriksh</font></b></td>
</tr>
</table>

<table width="100%" height="100%" border=1>
<tr>
<!--Menu List-->
	<td width="20%" valign=top>
	<table valign=top height="120">
	<tr>
		<td><a href="">Daily Event Log</a></td>
	</tr>
	
	<tr>
		<td><a href="#" onclick="loadScriptMapper();">Script Mapper</a></td>
	</tr>
	<tr>
		<td><a href='<c:url  value="viewFnOGapStrategy.do"/>'>Future Gap Strategy </a></td>
	</tr>
	<tr>
		<td><a href='<c:url  value="viewBrokerDetail.do"/>'>Broker </a></td>
	</tr>

	<tr>
		<td><a href='<c:url  value="/strategy/viewstrategy.do"/>'>Strategy</a></td>
	</tr>

	<tr>
		<td><a href="viewtradesumary.do">Turnover</a></td>
	</tr>

	

	</table>
<table height="70%">

<tr>
	<td></td>
</tr>
</table>
	
	</td>




<!--content -->
	<td width="80%" valign=top>
	<div id="contentDiv"  height="100%" style="aligntop;">
	<table width="100%" border=1 height="100%">
	<th align=center height="20%" colspan=2>
	Event Log
	</th>
	<tr height="80%">
		<td>
		Time
		</td>
		<td>
		Message
		</td>
	</tr>
	<c:forEach var="logMap" items="${logs}" varStatus="loopStatus">
	<tr height="80%">
		<td width="100%" colspan=2>
		${logMap.timeStamp} : ${logMap.processName}: ${logMap.log}
		</td>
	</tr>
	</c:forEach>
	</table>
	</div>

</tr>
</table>

  </form>
 </body>
 <div id="addScript" class="" style="visibility:hidden;">
 <table>
 <tr>
		<td align=center>Watcher Script</td>
			<td align=center>Trading Script</td>				
				<td align=center>&nbsp;</td>
				
	</tr>
	<tr>
		<td align=center><input type="text" name="watcherScriptName" id="watcherScriptName" value=""></td>
			<td align=center><input type="text" name="traderScriptName" id="traderScriptName" value=""></td>				
				<td align=center><input type="button" name="addScript" value="Add" onclick="addNewScript();"><label id="hidePopUp" onclick="hidePopup();">hide</label></td>
				
	</tr>
 </table>
 </div>
</html>

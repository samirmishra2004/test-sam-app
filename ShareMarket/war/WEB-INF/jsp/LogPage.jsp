<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<table width="100%" border=1 height="120" valign=top>
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
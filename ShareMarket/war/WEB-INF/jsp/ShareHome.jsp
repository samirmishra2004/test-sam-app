<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Share Home</title>
</head>
<body>


<table>

<tr><td align="center" ><font color="red"><b>Most dynamic Shares in the market</b></font></td></tr><br>
<tr>
<td>Symbol: <input type=text name=quoteSymbol value=""><input type=button name="symbolBtn" value="update" onclick=""/></td>
</tr>
<tr>

<td><font color="#HHF1"><%out.print(request.getAttribute("homePageString"));%></font></td>
</tr>

</table>

</body>
</html>
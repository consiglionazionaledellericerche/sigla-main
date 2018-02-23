<%@ page pageEncoding="UTF-8"  session="false" %>
<html>
<head>
<title><%=pageContext.getServletContext().getAttribute("APPLICATION_TITLE_VERSION")%></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<% it.cnr.jada.util.jsp.JSPUtils.printBaseUrl(pageContext); %>
<link rel="stylesheet" href="style.css">
<script language="JavaScript" src="scripts/util.js"></script>
</head>

<body class="ColoredFrame">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td rowspan="2" valign="bottom" nowrap><img src="img/logo_mini.gif"><a style="cursor:hand" onClick="window.open('../cert.html','Cert','width=500,height=350,menubar=no,toolbar=no,location=no,scrollbar=yes,resizable=yes');"><img src="img/padl.gif" border=0></a></td>
    <td width="100%" align="center" style="font : bold 12px sans-serif;"><%=pageContext.getServletContext().getAttribute("APPLICATION_TITLE")%></td>
    <td rowspan="2" valign="bottom" style="font : 8px sans-serif;" nowrap><img src="img/cnr2.gif" width="32" height="32"></td>
  </tr>
  <tr>
  	<td width="100%" align="center" style="font : bold 8px sans-serif;"><%=pageContext.getServletContext().getAttribute("APPLICATION_VERSION") %></td>
  </tr>
</table>
</body>
</html>
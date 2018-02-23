<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.cori00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<% JSPUtils.printBaseUrl(pageContext);%>
</head>
<script language="javascript" src="scripts/css.js"></script>

<% LiquidGruppoCentroBP bp = (LiquidGruppoCentroBP)BusinessProcess.getBusinessProcess(request); %>

<title>Ribaltamento Liquidazioni CoRi accentrate</title>

<body class="Form">

<% bp.openFormWindow(pageContext); %>

	<table class="Panel">
		<tr><td>
		 	  <% bp.writeHTMLTable(pageContext,"100%","400px"); %> 
			</td>
		</tr> 
		<tr><td>
			<% bp.writeHTMLNavigator(out); %>
		</td></tr>
	</table>


<%bp.closeFormWindow(pageContext); %>
</body>
<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,
		it.cnr.contab.inventario00.tabrif.bulk.*,
		it.cnr.contab.inventario00.docs.bulk.*,
		it.cnr.contab.inventario00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<!-- 
 ?ResourceName "lista_beni_da_inventariare.jsp"
-->

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<title>Elenco Beni da Inventariare</title>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<%
	CRUDSelezionatoreBeniInTransitoBP bp = (CRUDSelezionatoreBeniInTransitoBP)BusinessProcess.getBusinessProcess(request);
%>
</head>

<body class="Form">
<% bp.openFormWindow(pageContext); %>
<%	if (bp.getParentRoot().isBootstrap()) { %>
	<% bp.writeHTMLTable(pageContext,"100%","65vh"); %>
	<% bp.writeHTMLNavigator(out); %>
<% } else {%>
	<table class="Panel" height="100%" width="100%">
		<tr height="100%"><td>
		<% bp.writeHTMLTable(pageContext,"100%","100%"); %>
		</td></tr>
		<tr><td>
		<% bp.writeHTMLNavigator(out); %>
		</td></tr>
	</table>
<% } %>
<%bp.closeFormWindow(pageContext); %>
</body>
</html>
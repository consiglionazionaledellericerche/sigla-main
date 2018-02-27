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
		it.cnr.contab.pdg00.bulk.*,
		it.cnr.contab.pdg00.action.*,
		it.cnr.contab.pdg00.bp.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title> Eliminazione dettagli per GAE</title>
</head>
<body class="Form">

<%
	PdGPreventivoBP bp = (PdGPreventivoBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
%>

<table class="Panel" height="150px">
	<tr><% bp.getController().writeFormField(out,"linea_attivita_eliminazione");%></tr>
	<tr>
		<td><center><%JSPUtils.button(out, "img/closeup.gif", "Eliminazione", "if (disableDblClick()) javascript:submitForm('doDelDetByLA')", bp.getParentRoot().isBootstrap());%></center></td>
	</tr>
</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
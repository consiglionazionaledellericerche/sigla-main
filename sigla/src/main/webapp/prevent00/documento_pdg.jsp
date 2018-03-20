<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.prevent00.bulk.*,
		it.cnr.contab.prevent00.action.*,
		it.cnr.contab.prevent00.bp.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa aggregato CDR di I livello/Area</title>
</head>
<body class="Form">

<%
	DocumentoPdGBP bp = (DocumentoPdGBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
%>

<table class="Panel">
    <tr>
		<td colspan=4> </td>
	</tr>

	<tr>
		<td colspan=4><b>Stampa documento dei piani di gestione</b>
		</td>
	</tr>
	<tr>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per funzione", "if (disableDblClick()) javascript:submitForm('doStampaDocPdgFunzione')", bp.getParentRoot().isBootstrap());%></td>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per funzione/natura", "if (disableDblClick()) javascript:submitForm('doStampaDocPdgNatura')", bp.getParentRoot().isBootstrap());%></td>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per funzione/natura/voce", "if (disableDblClick()) javascript:submitForm('doStampaDocPdgCapitolo')", bp.getParentRoot().isBootstrap());%></td>
		<td></td>
	</tr>


		<td colspan=4> </td>
	<tr>	
	<tr>
		<td colspan=4><b>Stampa documento PDG per ENTE</b>
		</td>
	</tr>
	<tr>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per funzione", "if (disableDblClick()) javascript:submitForm('doStampaDocPdgEnteFunzione')", bp.getParentRoot().isBootstrap());%></td>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per funzione/natura", "if (disableDblClick()) javascript:submitForm('doStampaDocPdgEnteNatura')", bp.getParentRoot().isBootstrap());%></td>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per funzione/natura/voce", "if (disableDblClick()) javascript:submitForm('doStampaDocPdgEnteCapitolo')", bp.getParentRoot().isBootstrap());%></td>
		<td></td>
	</tr>


		<td colspan=4> </td>
	<tr>	
		
</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
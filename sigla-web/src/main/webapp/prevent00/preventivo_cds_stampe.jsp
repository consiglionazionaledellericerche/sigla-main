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
<title>Preventivo finanziario CDS</title>
</head>
<body class="Form">

<%
	BilancioStampePreventivoCDSBP bp = (BilancioStampePreventivoCDSBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
%>

<table class="Panel">
    <tr>
		<td colspan=3> </td>
	</tr>
	<tr>
		<td colspan=3><b>Stampa spese</b>
		</td>
	</tr>
	<tr>
		<td><%JSPUtils.button(out, "img/print16.gif", "Spese", "if (disableDblClick()) javascript:submitForm('doStampaSpesa')", bp.getParentRoot().isBootstrap());%></td>
		<td><%JSPUtils.button(out, "img/print16.gif", "Spese (importi diversi da 0)", "if (disableDblClick()) javascript:submitForm('doStampaSpesaDZ')", bp.getParentRoot().isBootstrap());%></td>
	</tr>
		<td colspan=3> </td>
	<tr>

	<tr>
		<td colspan=3><b>Stampa entrate</b>
		</td>
	</tr>
	<tr>
		<td><%JSPUtils.button(out, "img/print16.gif", "Entrate", "if (disableDblClick()) javascript:submitForm('doStampaEntrata')", bp.getParentRoot().isBootstrap());%></td>
		<td><%JSPUtils.button(out, "img/print16.gif", "Entrate (importi diversi da 0)", "if (disableDblClick()) javascript:submitForm('doStampaEntrataDZ')", bp.getParentRoot().isBootstrap());%></td>
	</tr>
	
</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
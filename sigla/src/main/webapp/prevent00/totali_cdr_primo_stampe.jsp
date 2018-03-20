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
	CRUDTotaliPdGAggregatoBP bp = (CRUDTotaliPdGAggregatoBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
%>

<table class="Panel">
    <tr>
		<td colspan=4> </td>
	</tr>

	<tr>
		<td colspan=4><b>Stampa per funzione/natura/voce</b>
		</td>
	</tr>
	<tr>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per funzione", "if (disableDblClick()) javascript:submitForm('doStampaAggFunzione')", bp.getParentRoot().isBootstrap());%></td>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per funzione/natura", "if (disableDblClick()) javascript:submitForm('doStampaAggNatura')", bp.getParentRoot().isBootstrap());%></td>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per funzione/natura/voce", "if (disableDblClick()) javascript:submitForm('doStampaAggCapitolo')", bp.getParentRoot().isBootstrap());%></td>
		<td></td>
	</tr>

		<td colspan=4> </td>
	<tr>	
		

	<tr>
		<td colspan=4><b>Stampa variazioni aggregato del centro indotte da proposte di modifica a pdg</b>
		</td>
	</tr>
	<tr>
		<td><%JSPUtils.button(out, "img/print16.gif", "Entrate", "if (disableDblClick()) javascript:submitForm('doStampaAggEtrVar')", bp.getParentRoot().isBootstrap());%></td>
		<td><%JSPUtils.button(out, "img/print16.gif", "Spese", "if (disableDblClick()) javascript:submitForm('doStampaAggSpeVar')", bp.getParentRoot().isBootstrap());%></td>
		<td></td>
		<td></td>
	</tr>

	<tr>
		<td colspan=4><b>Stampa differenze non nulle tra aggregato dei piani di gestione (con eventuali proposte), e aggregato del centro</b>
		</td>
	</tr>
	<tr>
		<td><%JSPUtils.button(out, "img/print16.gif", "Entrate", "if (disableDblClick()) javascript:submitForm('doStampaAggEtrDelta')", bp.getParentRoot().isBootstrap());%></td>
		<td><%JSPUtils.button(out, "img/print16.gif", "Spese", "if (disableDblClick()) javascript:submitForm('doStampaAggSpeDelta')", bp.getParentRoot().isBootstrap());%></td>
		<td></td>
		<td></td>
	</tr>
	

		<td colspan=4> </td>
	<tr>	
</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
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
<title>Piano di Gestione aggregato</title>
</head>
<body class="Form">

<%
	CRUDPdGAggregatoBP bp = (CRUDPdGAggregatoBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
	Pdg_aggregatoBulk pdg_aggregato = (Pdg_aggregatoBulk)bp.getModel();
	boolean pdg_selezionato = pdg_aggregato.getCdr() != null && pdg_aggregato.getCdr().getCrudStatus() == OggettoBulk.NORMAL;
%>

<table>
	<tr>
		<td><%JSPUtils.button(out, "img/compressed.gif", "img/compressed.gif", "Contrattazione entrate", "if (disableDblClick()) submitForm('doContrattazioneEntrate')",null,pdg_selezionato, bp.getParentRoot().isBootstrap());%></td>
		<td><%JSPUtils.button(out, "img/transfer.gif", "img/transfer.gif", "Contrattazione spese", "if (disableDblClick()) submitForm('doContrattazioneSpese')",null,pdg_selezionato, bp.getParentRoot().isBootstrap());%></td>
		<td><%JSPUtils.button(out, "img/print24.gif", "img/print24.gif", "Totali piano aggregati", "if (disableDblClick()) submitForm('doTotaliPianoAggregati')", null, pdg_selezionato, bp.getParentRoot().isBootstrap());%></td>
	</tr>
</table>

<table class="Panel">
	<tr>
		<td><% bp.getController().writeFormLabel(out,"cdr");%></td>
		<td colspan="2"><% bp.getController().writeFormInput(out,"default","cdr",false,null,"onchange=\"if (disableDblClick()) submitForm('doCambiaCdR')\"");%></td>
	</tr>
	<% if (pdg_selezionato) { %>
	<tr>
		<% bp.getController().writeFormField(out,"stato");%>
		<td><%JSPUtils.button(out, "img/import24.gif", "img/import24.gif", "Cambia stato", "if (disableDblClick()) javascript:submitForm('doCambiaStato')",null,pdg_selezionato && !bp.isInputReadonly(), bp.getParentRoot().isBootstrap());%></td>
	</tr>
	<% } %>
</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
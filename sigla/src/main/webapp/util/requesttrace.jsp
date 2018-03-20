<!-- 
 ?ResourceName ".jsp"
 ?ResourceTimestamp ""
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<% JSPUtils.printBaseUrl(pageContext); %>
</head>
<title>Gestione trace sessione HTTP</title>
<body class="Form">

<% it.cnr.contab.devutil00.bp.RequestTraceBP bp = (it.cnr.contab.devutil00.bp.RequestTraceBP)BusinessProcess.getBusinessProcess(request);
	 it.cnr.contab.devutil00.bulk.RequestTracerVBulk tracer = (it.cnr.contab.devutil00.bulk.RequestTracerVBulk)bp.getModel();
	 bp.openFormWindow(pageContext); %>
	<table class="Panel">
		<tr><td colspan="2"><span class="FormLabel">Utenti tracciati</span></td></tr>
		<tr>
			<td><input class=null type="text" name="newTraceUser"></td>
			<td><button style="width:7em" onclick="if (disableDblClick()) submitForm('doAddUser')">Aggiungi</button></td></tr>
		<tr valign="top">
			<td rowspan="2">
				<select class=null name="tracingUsers" multiple style="width:100%">
				<% for (java.util.Iterator i = tracer.getTracingUsers().iterator();i.hasNext();) { %>
					<option><%= i.next()%>
				<% }%>
				</select>
			<td><button style="width:7em" onclick="if (disableDblClick()) submitForm('doRemoveUsers')">Rimuovi</button></tr>
		<tr><td colspan="2"><button style="width:7em" onclick="if (disableDblClick()) submitForm('doAggiorna')">Aggiorna</button></tr>
	</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
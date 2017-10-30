<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.ordmag.anag00.*,
		it.cnr.contab.ordmag.richieste.*,
		it.cnr.contab.ordmag.richieste.bp.GenerazioneOrdineDaRichiesteBP"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<% JSPUtils.printBaseUrl(pageContext);%>
</head>
<script language="javascript" src="scripts/css.js"></script>

<% GenerazioneOrdineDaRichiesteBP bp = (GenerazioneOrdineDaRichiesteBP)BusinessProcess.getBusinessProcess(request); %>

	<title>Generazione Ordine da Richieste</title>

<body class="Form">


<% bp.openFormWindow(pageContext); %>

	<table class="Panel">
		<tr><td>
		<% bp.writeHTMLTable(pageContext,"100%","200px"); %>
		</td></tr>
		<tr><td>
		<% bp.writeHTMLNavigator(out); %>
		</td></tr>
	</table>


<%bp.closeFormWindow(pageContext); %>
</body>
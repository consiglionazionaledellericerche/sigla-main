<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		java.text.*,
		it.gov.fatturapa.sdi.monitoraggio.v1.*,
		it.cnr.contab.docamm00.bp.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Carica Fattura Elettronica</title>
</head>
<body class="Form">

<%
	CaricaFatturaElettronicaBP bp = (CaricaFatturaElettronicaBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
	SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy hh:mm");
%>
<div class="Group">	
	<table class="Panel">
		<tr>
			<td><%JSPUtils.button(out, "img/import24.gif", "Carica Fattura", "if (disableDblClick()) javascript:submitForm('doCaricaFattura')", bp.getParentRoot().isBootstrap());%></td>
			<td><%JSPUtils.button(out, "img/export24.gif", "Controlla Fatture Ricevute", "if (disableDblClick()) javascript:submitForm('doControllaFatture')", bp.getParentRoot().isBootstrap());%></td>
			<td><%JSPUtils.button(out, "img/export24.gif", "Allinea Notifiche di Esito", "if (disableDblClick()) javascript:submitForm('doAllineaNotifiche')", bp.getParentRoot().isBootstrap());%></td>
			<td><%JSPUtils.button(out, "img/import24.gif", "Scan PEC", "if (disableDblClick()) javascript:submitForm('doScaricaFatture')", bp.getParentRoot().isBootstrap());%></td>
		</tr>				
	</table>
</div>
<div class="Group">	
	<table class="Panel">
	<%bp.writeForm(out, "default"); %>	
	</table>
</div>
</body>
</html>
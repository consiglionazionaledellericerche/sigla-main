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
	<table class="Panel ">
		<tr>
			<td>
                <%JSPUtils.button(out,
                        bp.getParentRoot().isBootstrap() ? "fa fa-external-link faa-shake" : "img/import24.gif",
                        bp.getParentRoot().isBootstrap() ? "fa fa-external-link faa-shake" : "img/import24.gif",
                        "Carica Fattura Elettronica",
                        "if (disableDblClick()) submitForm('doCaricaFattura')",
                        "btn-primary btn-block btn-title faa-parent animated-hover",
                        true,
                        bp.getParentRoot().isBootstrap());%>
			</td>
			<td>
                <%JSPUtils.button(out,
                        bp.getParentRoot().isBootstrap() ? "fa fa-check" : "img/export24.gif",
                        bp.getParentRoot().isBootstrap() ? "fa fa-check" : "img/export24.gif",
                        "Controlla Fatture Ricevute",
                        "if (disableDblClick()) submitForm('doControllaFatture')",
                        "btn-success btn-block btn-title",
                        true,
                        bp.getParentRoot().isBootstrap());%>
			</td>
			<td>
                <%JSPUtils.button(out,
                        bp.getParentRoot().isBootstrap() ? "fa fa-check" : "img/export24.gif",
                        bp.getParentRoot().isBootstrap() ? "fa fa-check" : "img/export24.gif",
                        "Allinea Notifiche di Esito",
                        "if (disableDblClick()) submitForm('doAllineaNotifiche')",
                        "btn-warning btn-block btn-title",
                        true,
                        bp.getParentRoot().isBootstrap());%>
			</td>
			<td>
                <%JSPUtils.button(out,
                        bp.getParentRoot().isBootstrap() ? "fa fa-refresh" : "img/export24.gif",
                        bp.getParentRoot().isBootstrap() ? "fa fa-refresh" : "img/export24.gif",
                        "Scarica Fatture dalla PEC",
                        "if (disableDblClick()) submitForm('doScaricaFatture')",
                        "btn-danger btn-block btn-title",
                        true,
                        bp.getParentRoot().isBootstrap());%>
			</td>
		</tr>				
	</table>
</div>
<div class="Group mt-2">
	<table class="Panel card p-2">
	<%bp.writeForm(out, "default"); %>	
	</table>
</div>
</body>
</html>
<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
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
%>
<div class="Group">	
	<table class="Panel">
		<tr><td><%JSPUtils.button(out, "img/import24.gif", "Carica Fattura", "if (disableDblClick()) javascript:submitForm('doCaricaFattura')");%></td></tr>	
	</table>
</div>
<div class="Group">	
	<table class="Panel">
	<%bp.writeForm(out, "default"); %>	
	</table>
</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
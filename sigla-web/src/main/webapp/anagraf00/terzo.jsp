<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.anagraf00.bp.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.anagraf00.core.bulk.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Gestione terzo</title>
</head>

<body class="Form">

<%
	CRUDTerzoBP bp = (CRUDTerzoBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
%>

<table class="card w-100 p-2 mb-2">
	<tr>
		<td>
			<%bp.writeFormLabel(out,"cd_anag");%>
		</td>
		<td>
			<%bp.writeFormInput(out,null,"cd_anag",bp.getAnagrafico() != null,null,null);%>
		</td>
		<%bp.writeFormField(out,"cd_terzo"); %>
	</tr>
</table>
<%	JSPUtils.tabbed(
			pageContext,
			"tab",
			new String[][] {
					{ "tabAnagrafica","Anagrafica","/anagraf00/tab_terzo_anagrafica.jsp" },
					{ "tabModalitaPagamento","Modalità di pagamento","/anagraf00/tab_terzo_modalita_pagamento.jsp" },
					{ "tabTerminiPagamento","Termini di pagamento","/anagraf00/tab_terzo_termini_pagamento.jsp" },
					{ "tabRecapiti","Recapiti","/anagraf00/tab_terzo_recapiti.jsp" },
					{ "tabContatti","Contatti","/anagraf00/tab_terzo_contatti.jsp" } },
			bp.getTab("tab"),
			"center",
			"100%",
			null);
%>
<%	bp.closeFormWindow(pageContext); %>

</body>
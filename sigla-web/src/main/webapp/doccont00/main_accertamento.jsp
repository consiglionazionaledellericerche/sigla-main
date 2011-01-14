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
		it.cnr.contab.doccont00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<% JSPUtils.printBaseUrl(pageContext); %>
</head>
<script language="javascript" src="scripts/css.js"></script>

<%  
	CRUDAccertamentoBP bp = (CRUDAccertamentoBP)BusinessProcess.getBusinessProcess(request);
%>

<% if ( bp.getModel() instanceof it.cnr.contab.doccont00.core.bulk.AccertamentoResiduoBulk ) {%>
		<title>Gestione Accertamento Residuo</title>
<%} else {%>
		<title>Gestione Accertamento</title>
<%}%>
	
<body class="Form">
<% bp.openFormWindow(pageContext);%>

<table class="Panel">
	<tr><td>
		<%	if (bp.isEditingScadenza())
				JSPUtils.tabbed(
						pageContext,
						"tab",
						new String[][] {
								{ "tabAccertamento","Accertamento","/doccont00/tab_accertamento.jsp" } ,
								{ "tabImputazioneFin","Imputazione Finanziaria","/doccont00/tab_imputazione_fin_accertamento.jsp" },
								{ "tabScadenziario","Scadenziario","/doccont00/tab_scadenziario_accertamento.jsp" } },
						bp.getTab("tab"),
						"center",
						null, null,
						false);
			else
				JSPUtils.tabbed(
						pageContext,
						"tab",
						new String[][] {
								{ "tabAccertamento","Accertamento","/doccont00/tab_accertamento.jsp" } ,
								{ "tabImputazioneFin","Imputazione Finanziaria","/doccont00/tab_imputazione_fin_accertamento.jsp" },
								{ "tabScadenziario","Scadenziario","/doccont00/tab_scadenziario_accertamento.jsp" } },
						bp.getTab("tab"),
						"center",
						null, null,
						true);
		%>
	</td></tr>
</table>

<%	bp.closeFormWindow(pageContext); %>

</body>
</html>
<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
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
		<% JSPUtils.printBaseUrl(pageContext);%>
	</head>
	<script language="javascript" src="scripts/css.js"></script>
	<title>Mandato automatico</title>
	<body class="Form">
<%  
		MandatoAutomaticoWizardBP bp = (MandatoAutomaticoWizardBP)BusinessProcess.getBusinessProcess(request);
		bp.openFormWindow(pageContext); 
%>

	<table class="Panel">
		<tr><td colspan=2>
		<%
			if (bp.isMandatoAutomaticoTabEnabled())
				JSPUtils.tabbed(
							pageContext,
							"tab",
							new String[][] {
									{ "tabCriteriRicerca","Criteri Ricerca","/doccont00/tab_criteri_ricerca_wizard.jsp" },
									{ "tabMandatoAutomatico","Dettaglio","/doccont00/tab_mandato_automatico_wizard.jsp" },
									},
							bp.getTab("tab"),
							"center",
							null,null);
			else
				JSPUtils.tabbed(
							pageContext,
							"tab",
							new String[][] {
									{ "tabCriteriRicerca","Criteri Ricerca","/doccont00/tab_criteri_ricerca_wizard.jsp" },
									},
							bp.getTab("tab"),
							"center",
							null,null);
		%>
		</td></tr>	
	</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
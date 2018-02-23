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
		it.cnr.contab.config00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<% JSPUtils.printBaseUrl(pageContext);%>
</head>
<script language="javascript" src="scripts/css.js"></script>
<title>Gestione Centri di Spesa</title>
<body class="Form">

<%  
		CRUDConfigCdSBP bp = (CRUDConfigCdSBP)BusinessProcess.getBusinessProcess(request);
		bp.openFormWindow(pageContext); 
%>

<table class="Panel">
	<tr><td colspan=2>
	<%
		JSPUtils.tabbed(
						pageContext,
						"tab",
						new String[][] {
								{ "tabCds","Centri di spesa","/config00/tab_centri_di_spesa.jsp" },
								{ "tabPercentuali","Percentuali copertura impegni","/config00/tab_percentuali.jsp" }},
						bp.getTab("tab"),
						"center",
						null,null);		
	%>
	</td></tr>	
	</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
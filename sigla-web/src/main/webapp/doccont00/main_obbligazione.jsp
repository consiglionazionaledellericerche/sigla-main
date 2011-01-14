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
<%  
   CRUDObbligazioneBP bp = (CRUDObbligazioneBP)BusinessProcess.getBusinessProcess(request);
%>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<% JSPUtils.printBaseUrl(pageContext);%>
</head>
<script language="javascript" src="scripts/css.js"></script>
<title><%=bp.getBulkInfo().getShortDescription()%></title>
<body class="Form">

<%  
		bp.openFormWindow(pageContext); 
%>

<table class="Panel">
	<tr><td colspan=2>
	<%	
		if (bp.isEditingScadenza())
		
			JSPUtils.tabbed(
						pageContext,
						"tab",
						new String[][] {
								{ "tabObbligazione","Impegni","/doccont00/tab_obbligazione.jsp" },
								{ "tabImputazioneFin","Imputazione Finanziaria","/doccont00/tab_imputazione_fin_obbligazione.jsp" },
								{ "tabScadenzario","Scadenzario","/doccont00/tab_scadenzario_obbligazione.jsp" } ,
								{ "tabCdrCapitoli","Cdr","/doccont00/tab_cdr_capitoli.jsp" } },						
						bp.getTab("tab"),
						"center",
						null, null,
						false);
		else
			JSPUtils.tabbed(
						pageContext,
						"tab",
						new String[][] {
								{ "tabObbligazione","Impegni","/doccont00/tab_obbligazione.jsp" },
								{ "tabImputazioneFin","Imputazione Finanziaria","/doccont00/tab_imputazione_fin_obbligazione.jsp" },
								{ "tabScadenzario","Scadenzario","/doccont00/tab_scadenzario_obbligazione.jsp" },
								{ "tabCdrCapitoli","Cdr","/doccont00/tab_cdr_capitoli.jsp" } },												
						bp.getTab("tab"),
						"center",
						null, null,						
						true);		
		%>
	</td></tr>	
	</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
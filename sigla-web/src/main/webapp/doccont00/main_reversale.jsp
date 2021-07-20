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
<title>Gestione Reversale</title>
<body class="Form">

<%  
		CRUDReversaleBP bp = (CRUDReversaleBP)BusinessProcess.getBusinessProcess(request);
		bp.openFormWindow(pageContext); 
%>
<script language="JavaScript">
function doVisualizzaContabile() {	
  doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/<%=bp.getContabileFileName()%>?methodName=scaricaContabile&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>
<table class="Panel">
	<tr><td colspan=2>
	<%
		// if ( bp.isRicercaDocumentiTabEnabled() )
		 if ( bp.isInserting() )
			JSPUtils.tabbed(
						pageContext,
						"tab",
						new String[][] {
								{ "tabReversale","Reversale","/doccont00/tab_reversale.jsp" },
								{ "tabRicercaDocAttivi","Ricerca documenti","/doccont00/tab_ricerca_doc_attivi.jsp" },
								{ "tabDettaglioReversale","Dettaglio","/doccont00/tab_dettaglio_reversale.jsp" },
								{ "tabSospesi","Sospesi","/doccont00/tab_reversale_sospesi.jsp" },
								{ "tabMandati","Doc.Contabili associati","/doccont00/tab_reversale_mandati.jsp" },
							    { "tabFatturaPassivaEconomica", "Economico/Patrimoniale", "/coepcoan00/tab_docamm_economica.jsp" }
								},
						bp.getTab("tab"),
						"center",
						null,null);
		else
			JSPUtils.tabbed(
						pageContext,
						"tab",
						new String[][] {
								{ "tabReversale","Reversale","/doccont00/tab_reversale.jsp" },
								{ "tabDettaglioReversale","Dettaglio","/doccont00/tab_dettaglio_reversale.jsp" },
								{ "tabSospesi","Sospesi","/doccont00/tab_reversale_sospesi.jsp" },
								{ "tabMandati","Doc.Contabili associati","/doccont00/tab_reversale_mandati.jsp" },
							    { "tabFatturaPassivaEconomica", "Economico/Patrimoniale", "/coepcoan00/tab_docamm_economica.jsp" }
								},
						bp.getTab("tab"),
						"center",
						null,null);
		
	%>
	</td></tr>	
	</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
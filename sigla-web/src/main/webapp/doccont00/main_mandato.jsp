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
<% JSPUtils.printBaseUrl(pageContext);%>
</head>
<script language="javascript" src="scripts/css.js"></script>
<title>Gestione Mandato</title>
<body class="Form">

<%  
		CRUDMandatoBP bp = (CRUDMandatoBP)BusinessProcess.getBusinessProcess(request);
		bp.openFormWindow(pageContext); 
%>

<table class="Panel">
	<tr><td colspan=2>
	<%
		// if ( bp.isRicercaDocumentiTabEnabled() )
		if ( bp.isInserting() )
			JSPUtils.tabbed(
						pageContext,
						"tab",
						new String[][] {
								{ "tabMandato","Mandato","/doccont00/tab_mandato.jsp" },
								{ "tabRicercaDocPassivi","Ricerca documenti","/doccont00/tab_ricerca_doc_passivi.jsp" },
								{ "tabDettaglioMandato","Dettaglio","/doccont00/tab_dettaglio_mandato.jsp" },
								{ "tabSospesi","Sospesi","/doccont00/tab_mandato_sospesi.jsp" },
								{ "tabReversali","Doc.Contabili associati","/doccont00/tab_mandato_reversali.jsp" }								
								},
						bp.getTab("tab"),
						"center",
						null,null);
		else
			JSPUtils.tabbed(
						pageContext,
						"tab",
						new String[][] {
								{ "tabMandato","Mandato","/doccont00/tab_mandato.jsp" },
								{ "tabDettaglioMandato","Dettaglio","/doccont00/tab_dettaglio_mandato.jsp" },
								{ "tabSospesi","Sospesi","/doccont00/tab_mandato_sospesi.jsp" },
								{ "tabReversali","Doc.Contabili associati","/doccont00/tab_mandato_reversali.jsp" }								
								},
						bp.getTab("tab"),
						"center",
						null,null);
		
	%>
	</td></tr>	
	</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
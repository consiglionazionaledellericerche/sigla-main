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
<title>Gestione Mandato</title>
<%  
		CRUDMandatoBP bp = (CRUDMandatoBP)BusinessProcess.getBusinessProcess(request);
		bp.openFormWindow(pageContext); 
%>
<script language="JavaScript">
function doVisualizzaContabile() {	
  doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/<%=bp.getContabileFileName()%>?methodName=scaricaContabile&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
function doVisualizzaMandato() {	
  doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/<%=bp.getMandatoFileName()%>?methodName=scaricaMandato&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>

<body class="Form">
	<%
		if ( bp.isInserting())
			JSPUtils.tabbed(
						pageContext,
						"tab",
						new String[][] {
								{ "tabMandato","Mandato","/doccont00/tab_mandato.jsp" },
								{ "tabRicercaDocPassivi","Ricerca documenti","/doccont00/tab_ricerca_doc_passivi.jsp" },
								{ "tabDettaglioMandato","Dettaglio","/doccont00/tab_dettaglio_mandato.jsp" },
								{ "tabSospesi","Sospesi","/doccont00/tab_mandato_sospesi.jsp" },
								{ "tabReversali","Doc.Contabili associati","/doccont00/tab_mandato_reversali.jsp" },
								{ "tabEconomica", "Economico/Patrimoniale", "/coepcoan00/tab_doc_economica.jsp" }
								},
						bp.getTab("tab"),
						"center",
						"100%","100%");
		else
			JSPUtils.tabbed(
						pageContext,
						"tab",
						new String[][] {
								{ "tabMandato","Mandato","/doccont00/tab_mandato.jsp" },
								{ "tabDettaglioMandato","Dettaglio","/doccont00/tab_dettaglio_mandato.jsp" },
								{ "tabSospesi","Sospesi","/doccont00/tab_mandato_sospesi.jsp" },
								{ "tabReversali","Doc.Contabili associati","/doccont00/tab_mandato_reversali.jsp" },
							    { "tabEconomica", "Economico/Patrimoniale", "/coepcoan00/tab_doc_economica.jsp" },
                        },
						bp.getTab("tab"),
						"center",
						"100%","100%");
		
	%>
<%	bp.closeFormWindow(pageContext); %>
</body>
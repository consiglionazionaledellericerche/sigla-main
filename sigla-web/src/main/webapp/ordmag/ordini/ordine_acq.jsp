<%@page import="it.cnr.contab.ordmag.ordini.bp.CRUDOrdineAcqBP"%>
<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.ordmag.anag00.*,
		it.cnr.contab.ordmag.ordini.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<% CRUDBP bp = (CRUDOrdineAcqBP)BusinessProcess.getBusinessProcess(request); %>
<script language="JavaScript">
function doStampaOrdine() {
	doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/stampaOrdineAcq.html?methodName=stampaOrdine&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>', 
			'Ordine di Acquisto', 'toolbar=no, location=no, directories=no, status=no, menubar=no,resizable,scrollbars,width=800,height=600').focus() ;
}
</script>
<title>Ordine d'Acquisto</title>
</head>
<body class="Form">
<%  bp.openFormWindow(pageContext); %>
	<div class="Group card p-2 mb-2">
		<table cellpadding="2">
			<tr>
				<%
					bp.getController().writeFormField(out, "esercizio");
				%>
				<%
					bp.getController().writeFormField(out, "cdNumeratore");
				%>
				<%
					bp.getController().writeFormField(out, "numero");
				%>
				<%
					bp.getController().writeFormField(out, "dataOrdine");
				%>
				<%
					bp.getController().writeFormField(out, "percProrata");
				%>
			</tr>
			<tr>
				<%
				bp.getController().writeFormField(out, "imImponibile");
				bp.getController().writeFormField(out, "imIva");
				bp.getController().writeFormField(out, "imIvaD");
				bp.getController().writeFormField(out, "imTotaleOrdine");
				%>
			</tr>
			<tr><% 
				   if (bp.isInserting()) {
					 bp.getController().writeFormField(out, "stato");
				   } else if (bp.isSearching()) {
				     bp.getController().writeFormField(out, "statoForSearch");
				   } else {
				     bp.getController().writeFormField(out, "statoForUpdate");
				   }
				%>
				<%
					bp.getController().writeFormField(out, "dataOrdineDef");
				%>
			</tr>
		</table>
	</div>
	<%
	JSPUtils.tabbed(
				pageContext,
				"tab",
				new String[][] {
				{ "tabOrdineAcq","Ordine d'Acquisto","/ordmag/ordini/tab_ordine_acq.jsp" },
				{ "tabOrdineFornitore","Fornitore","/ordmag/ordini/tab_ordine_fornitore.jsp" },
				{ "tabOrdineAcqDettaglio","Dettaglio","/ordmag/ordini/tab_ordine_acq_dettagli.jsp" },
				{ "tabOrdineAcqObbligazioni","Obbligazioni Collegate","/ordmag/ordini/tab_ordine_acq_obbligazioni.jsp" },
				{ "tabAllegati","Allegati","/ordmag/ordini/tab_ordine_acq_allegati.jsp" }},
				bp.getTab("tab"),
				"center",
				"100%",
				null );
	%>	
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
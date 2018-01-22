<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->
<%@page import="it.cnr.contab.ordmag.ordini.bp.CRUDEvasioneOrdineBP"%>
<%@ page 
	import="it.cnr.jada.util.jsp.*,
			it.cnr.jada.action.*,
			java.util.*,
			it.cnr.jada.util.action.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
 <% CRUDEvasioneOrdineBP bp= (CRUDEvasioneOrdineBP)BusinessProcess.getBusinessProcess(request); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
 <title>Evasione Ordini</title>
<body class="Form">
<% 	bp.openFormWindow(pageContext); %>
<div class="Group card p-2">
	<table class="Panel" width="100%">
		<tr class="w-100">
			<td><% bp.getController().writeFormLabel(out, "findUnitaOperativaOrd"); %></td>
			<td colspan="5"><% bp.getController().writeFormInput(out, "findUnitaOperativaOrd"); %></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel(out, "findMagazzino"); %></td>
			<td colspan="5"><% bp.getController().writeFormInput(out, "findMagazzino"); %></td>
		</tr>
		<tr>
			<% bp.getController().writeFormField(out, "dataBolla");%>
			<% bp.getController().writeFormField(out, "numeroBolla");%>
			<% bp.getController().writeFormField(out, "dataConsegna");%>
		</tr>
	</table>
</div>		

<%
	JSPUtils.tabbed(
					pageContext,
					"tab",
					new String[][] {
						{ "tabEvasioneConsegne", "Consegne", "/ordmag/ordini/evasione_ordini/tab_evasione_consegne.jsp" },
		      			{ "tabEvasioneRichieste", "Richieste", "/ordmag/ordini/evasione_ordini/tab_evasione_richieste.jsp" }},
					bp.getTab("tab"),
					"center",
					"100%",
					null);

	bp.closeFormWindow(pageContext);
%>
</body>
</html>
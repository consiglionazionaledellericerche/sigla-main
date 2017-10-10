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
	<table border="0" cellspacing="0" cellpadding="2">
		
				<tr>
					<%
						bp.getController().writeFormField(out, "findUnitaOperativaOrd");
					%>
					<%
						bp.getController().writeFormField(out, "findNumerazioneMag");
					%>
				</tr>
			
		
			<% bp.getController().writeFormField(out, "dataBolla");%>
			<% bp.getController().writeFormField(out, "numeroBolla");%>
			<% bp.getController().writeFormField(out, "dataConsegna");%>
		
	
	</table>
		
	<table >
<tr><td >
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
					null );
	      %>
</td></tr>
	</table>
	<%
		bp.closeFormWindow(pageContext);
	%>
</body>
</html>
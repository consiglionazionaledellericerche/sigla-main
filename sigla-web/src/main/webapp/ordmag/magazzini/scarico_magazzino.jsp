<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->
<%@page import="it.cnr.contab.ordmag.magazzino.bulk.ScaricoMagazzinoBulk"%>
<%@page import="it.cnr.contab.ordmag.magazzino.bp.ScaricoManualeMagazzinoBP"%>
<%@page import="it.cnr.contab.ordmag.magazzino.bulk.ScaricoMagazzinoRigaBulk"%>
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
 <% ScaricoManualeMagazzinoBP bp= (ScaricoManualeMagazzinoBP)BusinessProcess.getBusinessProcess(request); 
    ScaricoMagazzinoBulk model = (ScaricoMagazzinoBulk)bp.getModel();%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Scarico Manuale Magazzino</title>
<body class="Form">
<% 	bp.openFormWindow(pageContext); %>
<div class="Group">
	<table class="Panel card p-2" width="100%">
		<tr>
			<td><% bp.getController().writeFormLabel(out, "findUnitaOperativaOrd"); %></td>
			<td colspan="5"><% bp.getController().writeFormInput(out, "findUnitaOperativaOrd"); %></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel(out, "findMagazzino"); %></td>
			<td colspan="5"><% bp.getController().writeFormInput(out, "findMagazzino"); %></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel(out, "findTipoMovimentoMag"); %></td>
			<td class="w-30"><% bp.getController().writeFormInput(out, "findTipoMovimentoMag"); %></td>
			<% bp.getController().writeFormField(out, "dataMovimento");%>
			<% bp.getController().writeFormField(out, "dataCompetenza");%>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel(out, "findUnitaOperativaOrdRic"); %></td>
			<td colspan="5"><% bp.getController().writeFormInput(out, "findUnitaOperativaOrdRic"); %></td>
		</tr>
	</table>
</div>
<div class="Group">
	<table border="0" cellspacing="0" cellpadding="0" width="100%">
		<tr><td>
			<% bp.getBeniServiziColl().writeHTMLTable(pageContext,null,true,false,true,"100%","100px", false);%>
		</td></tr>
	</table>
</div>	
<div class="Group card">
	<table border="0" cellspacing="0" cellpadding="2" width="100%">
	  	<tr>
	  		<td>
		      <%	JSPUtils.tabbed(
						pageContext,
						"tabArticolo",
						bp.getTabsArticolo(),
						bp.getTab("tabArticolo"),
						"center", 
						"100%", null,
						true );
			%>
			</td>
		</tr>
	</table>
</div>
	<% bp.closeFormWindow(pageContext); %>

</body>
</html>
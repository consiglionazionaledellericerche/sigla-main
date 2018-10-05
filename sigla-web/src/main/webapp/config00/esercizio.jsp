<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	        it.cnr.jada.action.*,
	        java.util.*, 
	        it.cnr.jada.util.action.*, 
	        it.cnr.contab.config00.bp.CRUDConfigEsercizioBP"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Gestione Esercizio Contabile</title>
<body class="Form">

<%  CRUDConfigEsercizioBP bp = (CRUDConfigEsercizioBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
	it.cnr.contab.config00.esercizio.bulk.EsercizioBulk esercizio = (it.cnr.contab.config00.esercizio.bulk.EsercizioBulk)bp.getModel();
%>

	<table class="Panel card p-2">
	<tr>
        <td><% bp.getController().writeFormLabel( out, "cds"); %></td>
        <td><% bp.getController().writeFormInput( out, "cds"); %></td>
	</tr>
	<tr>
        <td><% bp.getController().writeFormLabel( out, "esercizio"); %></td>
        <td><% bp.getController().writeFormInputByStatus( out, "esercizio"); %></td>
	</tr>
	<tr>
        <td><% bp.getController().writeFormLabel( out, "ds_esercizio"); %></td>
        <td><% bp.getController().writeFormInput( out, "ds_esercizio"); %></td>
	</tr>
	<% if ( !bp.isApriPdGButtonHidden() ) {%>
			<tr>
			<td><% bp.getController().writeFormInput( out, "st_apertura_chiusura" ); %></td>
			<td> 
				<% JSPUtils.button(out,
				        bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-refresh" : bp.encodePath("img/refresh24.gif"),
				        bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-refresh" : bp.encodePath("img/refresh24.gif"),
				        "Cambia stato",
				        "javascript:submitForm('doCambiaStato')",
				        "btn-outline-primary btn-title btn-block",
				        bp.isCambiaStatoButtonEnabled(),
				        bp.getParentRoot().isBootstrap()); %>
				<% JSPUtils.button(out,
				        bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-folder-open-o" : bp.encodePath("img/refresh24.gif"),
				        bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-folder-open-o" : bp.encodePath("img/refresh24.gif"),
				        "Apri PdG",
				        "javascript:submitForm('doApriPdG')",
				        "btn-outline-success btn-title btn-block",
				        bp.isApriPdGButtonEnabled(),
				        bp.getParentRoot().isBootstrap()); %>
			</td>	
			</tr>
		<%}
		else 
		{%>
			<tr>
			<td><% bp.getController().writeFormInput( out, "st_apertura_chiusura" ); %></td>
			<td ALIGN="CENTER"> 
				<% JSPUtils.button(out,
				        bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-refresh" : bp.encodePath("img/refresh24.gif"),
				        bp.encodePath("img/refresh24.gif"),
				        "Cambia stato",
				        "javascript:submitForm('doCambiaStato')",
				        "btn-outline-primary btn-title btn-block",
				        bp.isCambiaStatoButtonEnabled(),
				        bp.getParentRoot().isBootstrap()); %>
			</td>	
			</tr>
		<%}
	%>
	
	</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
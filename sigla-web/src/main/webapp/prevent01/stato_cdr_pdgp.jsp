<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page 
	import="it.cnr.jada.util.jsp.*,
	        it.cnr.jada.action.*,
	        java.util.*, 
	        it.cnr.jada.util.action.*, 
	        it.cnr.contab.prevent01.bp.*,
	        it.cnr.contab.prevent01.bulk.*,
	        it.cnr.contab.config00.sto.bulk.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Gestione Stato CdR-PdGP</title>
<body class="Form">

<%  CRUDStatoCdrPdGPBP bp = (CRUDStatoCdrPdGPBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
	CdrBulk esercizio = (CdrBulk)bp.getModel();
	SimpleDetailCRUDController controller = ((CRUDStatoCdrPdGPBP)bp).getCrudDettagli();
%>

	<div class="Group">
	<table border="0" cellspacing="0" cellpadding="0">
	<td>
		<%	controller.writeHTMLTable(pageContext,null,false,false,false,"100%","250px"); %>
	</td>
	</table>

	<table border="0" cellspacing="0" cellpadding="20">
	<td><% 
		if (bp.getParametriCnr().getFl_pdg_contrattazione())
			controller.writeFormInput( out, "stato" ); 
		else
			controller.writeFormInput( out, "statoSenzaContrattazione" ); 
		%>
	<td ALIGN="CENTER"> 
		<% JSPUtils.button(out,bp.encodePath("img/refresh24.gif"),bp.encodePath("img/refresh24.gif"),"Cambia stato","javascript:submitForm('doCambiaStato')",bp.isCambiaStatoButtonEnabled(),bp.getParentRoot().isBootstrap()); %>
	</td>	

	<td ALIGN="CENTER"> 
		<% JSPUtils.button(out,bp.encodePath("img/undo24.gif"),bp.encodePath("img/undo24.gif"),"Riporta a stato precedente","javascript:submitForm('doRiportaStatoPrecedente')",bp.isRiportaStatoPrecedenteButtonEnabled(),bp.getParentRoot().isBootstrap()); %>
	</td>	
	</table>
	</div>

<%	bp.closeFormWindow(pageContext); %>
</body>
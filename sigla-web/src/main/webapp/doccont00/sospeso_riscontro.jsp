<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="JavaScript" src="scripts/disableRightClick.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Gestione sospeso/riscontro</title>
<body class="Form">

<%  it.cnr.contab.doccont00.bp.CRUDSospesoBP bp = (it.cnr.contab.doccont00.bp.CRUDSospesoBP)BusinessProcess.getBusinessProcess(request);
    it.cnr.contab.doccont00.core.bulk.SospesoBulk sospeso = (it.cnr.contab.doccont00.core.bulk.SospesoBulk)bp.getModel();
	bp.openFormWindow(pageContext); %>

	<table class="Panel card p-2">
	<tr>
		<td><% bp.getController().writeFormLabel( out, "esercizio"); %></td>
		<td><% bp.getController().writeFormInput( out, "esercizio"); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "cd_cds"); %></td>
		<td><% bp.getController().writeFormInput( out, "cd_cds"); %>
			<% bp.getController().writeFormInput( out, "ds_cds"); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "cd_sospeso"); %></td>
		<td><% bp.getController().writeFormInput( out, "cd_sospeso"); %>
			<% bp.getController().writeFormLabel( out, "ti_sospeso_riscontro"); %>
			<% bp.getController().writeFormInput( out,"default", "ti_sospeso_riscontro", false, null, "onchange=\"submitForm('doCambiaTipoSospesoRiscontro')\"" ); %>
			<% bp.getController().writeFormLabel( out, "dt_registrazione"); %>
			<% bp.getController().writeFormInput( out, "dt_registrazione"); %></td>
		
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "ti_entrata_spesa"); %></td>
		<td><% bp.getController().writeFormInput( out,"default", "ti_entrata_spesa", false, null, "onchange=\"submitForm('doCambiaTipoEntrataSpesa')\"" ); %>
			<% bp.getController().writeFormLabel( out, "ti_cc_bi"); %>
			<% bp.getController().writeFormInput( out, "ti_cc_bi"); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "ds_anagrafico"); %></td>
		<td><% bp.getController().writeFormInput( out, "ds_anagrafico"); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "causale"); %></td>
		<td><% bp.getController().writeFormInput( out, "causale"); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "im_sospeso"); %></td>
		<td><% bp.getController().writeFormInput( out,"im_sospeso"); %>
			<% bp.getController().writeFormLabel( out, "im_associato_figli"); %>
			<% bp.getController().writeFormInput( out, "im_associato_figli"); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "cd_avviso_pagopa"); %></td>
		<td><% bp.getController().writeFormInput( out,"cd_avviso_pagopa"); %>
	</tr>
	</table>
  <% if( sospeso.getTi_sospeso_riscontro() != null && (sospeso.getTi_sospeso_riscontro().equals( sospeso.TI_RISCONTRO )) ) { %>
  <div class="Group">		
  <table border="0" cellspacing="0" cellpadding="2">
	<tr>	
		<td colspan=2 ALIGN="LEFT">
			<b ALIGN="CENTER"><big>Mandato/Reversale associato al riscontro</big></b>
		</td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "pg_documento_cont"); %></td>
		<td><% bp.getController().writeFormInput( out, "pg_documento_cont"); %>
			<% bp.getController().writeFormInput( out, "ds_documento_cont"); %>
			<% if (bp.isSearching() )
			      bp.getController().writeFormInput( out, "find_documento_cont_for_search"); 
			   else
			      bp.getController().writeFormInput( out, "find_documento_cont"); %>
		</td>
	</tr>
  </table>
  </div>
  <% } %>
  <% if( sospeso.getReversaliAccertamentiColl() != null && ( sospeso.getReversaliAccertamentiColl().size() > 0) ) { %>
	<table class="Panel">
		<tr>
			<td><% bp.getReversaliAccertamenti().writeHTMLTable(pageContext,null, false,false,false,"100%","100px", true); %></td>
		</tr>
	</table>
   <% } %>
  <% if( sospeso.getMandatiImpegniColl() != null && ( sospeso.getMandatiImpegniColl().size() > 0) ) { %>
	<table class="Panel" >
		<tr>
			<td><% bp.getMandatiImpegni().writeHTMLTable(pageContext,null, false,false,false,"100%","100px", true); %></td>
		</tr>
	</table>
   <% } %>
   <% if( sospeso.getLettereColl() != null && ( sospeso.getLettereColl().size() > 0) ) { %>
	<table class="Panel" >
	<tr>	
		<td colspan=2 ALIGN="LEFT">
			<b ALIGN="CENTER"><big>Lettera 1210</big></b>
		</td>
	</tr>
	<tr>
		<td><% bp.getLettere().writeHTMLTable(pageContext,"all", false,false,false,"100%","100px", true); %></td>
	</tr>
	</table>
   <% } %>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
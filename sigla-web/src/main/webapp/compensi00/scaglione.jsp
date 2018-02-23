<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.compensi00.tabrif.bulk.*,
		it.cnr.contab.compensi00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Scaglione</title>
</head>
<body class="Form">

<% 	CRUDScaglioneBP bp = (CRUDScaglioneBP)BusinessProcess.getBusinessProcess(request);
 	bp.openFormWindow(pageContext);
%>

<table class="Group">
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_contributo_ritenuta"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_contributo_ritenuta"); %>
		<% bp.getController().writeFormInput(out,"ds_contributo_ritenuta"); %>
		<% bp.getController().writeFormInput(out,"find_contributo_ritenuta"); %></td>				 
  </tr>
</table>

<table class="Form">
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_regione"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_regione"); %>
		<% bp.getController().writeFormInput(out,"ds_regione"); %>
		<% bp.getController().writeFormInput(out,"find_regione");%></td>	
	<td><% bp.getController().writeFormField(out,"fl_addreg_aliqmax"); %></td>				 
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_provincia"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_provincia"); %>
		<% bp.getController().writeFormInput(out,"ds_provincia"); %>
		<% bp.getController().writeFormInput(out,"find_provincia"); %></td>				 
  </tr>		
  <tr>
	<td><% bp.getController().writeFormLabel(out,"pg_comune"); %></td>
	<td><% bp.getController().writeFormInput(out,"pg_comune"); %>
		<% bp.getController().writeFormInput(out,"ds_comune"); %>
		<% bp.getController().writeFormInput(out,"find_comune"); %></td>				 
  </tr>
</table>

<table class="Group">
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_inizio_validita"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_inizio_validita");%></td>
	<td align="right">
		<% bp.getController().writeFormLabel(out,"dataFineValidita"); %>
		<% bp.getController().writeFormInput(out,"dataFineValidita");%>
	</td>
  </tr>
</table>

<table class="Group">
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ti_ente_percipiente"); %></td>	
	<td><% bp.getController().writeFormInput(out,"ti_ente_percipiente"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ti_anagrafico"); %></td>
	<td><% bp.getController().writeFormInput(out,"ti_anagrafico"); %></td>
  </tr>
</table>

<table class="Group">
  <tr>	
	<td><% bp.getController().writeFormLabel(out,"im_inferiore"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_inferiore"); %></td>
	<td><% bp.getController().writeFormLabel(out,"im_superiore"); %></td>
	<td><% bp.getController().writeFormInput(out,"im_superiore"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"aliquota"); %></td>
	<td><% bp.getController().writeFormInput(out,"aliquota"); %></td>
	<td><% bp.getController().writeFormLabel(out,"base_calcolo"); %></td>
	<td><% bp.getController().writeFormInput(out,"base_calcolo"); %></td>
  </tr>
  <% if (!bp.isEditing() && !bp.isSearching()) { %>
  <tr>
	<td colspan="4" align="center">
		<% JSPUtils.button(out,null,null,"Aggiungi","if (disableDblClick()) submitForm('doAggiungiScaglione')",null,bp.isBottoneAggiungiScaglioneEnabled(), bp.getParentRoot().isBootstrap());%>
		<% JSPUtils.button(out,null,null,"Elimina","if (disableDblClick()) submitForm('doEliminaScaglione')",null,bp.isBottoneEliminaScaglioneEnabled(), bp.getParentRoot().isBootstrap());%>
	</td>
  </tr>
  <% } %>
</table>

<% if (!bp.isSearching()) { %>
<table class="Group" style="width:100%">
  <tr>
	<td rowspan = "4">
	  <% java.util.List coll = ((ScaglioneBulk)bp.getModel()).getScaglioni();
	  if((coll != null) && (coll.size()!=0))
	  	bp.getScaglioniCRUDController().writeHTMLTable(
					pageContext,
					"scaglioneCori",
					false,
					false,
					false,
					"100%",
					null,
					true); %>
	</td>
  </tr>
</table>
<% } %>
	
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
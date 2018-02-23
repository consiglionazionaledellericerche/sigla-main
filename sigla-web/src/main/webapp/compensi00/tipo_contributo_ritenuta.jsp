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
<title>Tipo Contributo Ritenuta</title>
</head>
<body class="Form">

<% 	CRUDTipoContributoRitenutaBP bp = (CRUDTipoContributoRitenutaBP)BusinessProcess.getBusinessProcess(request);
 	bp.openFormWindow(pageContext);
 	Tipo_contributo_ritenutaBulk cori = (Tipo_contributo_ritenutaBulk) bp.getModel();
%>

<table class="Panel">


  <% if (bp.isSearching()) { %>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "cd_contributo_ritenutaForSearch"); %></td>
		<td><% bp.getController().writeFormInput( out, "cd_contributo_ritenutaForSearch"); %></td>	
	</tr>			
  <% } else { %>  
	<tr>
		<td><% bp.getController().writeFormLabel( out, "cd_contributo_ritenuta"); %></td>
		<td><% bp.getController().writeFormInput( out, "cd_contributo_ritenuta"); %></td>	
	</tr>			
  <% } %>  
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ds_contributo_ritenuta"); %></td>
	<td colspan=3><% bp.getController().writeFormInput(out,"ds_contributo_ritenuta"); %></td>
  </tr>

  <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_inizio_validita"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_inizio_validita"); %></td>
	<td align="right"><% bp.getController().writeFormLabel(out,"dataFineValidita"); %>
		<% bp.getController().writeFormInput(out,"dataFineValidita"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"precisione"); %></td>
	<td><% bp.getController().writeFormInput(out,"precisione"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"pg_classificazione_montanti"); %></td>
	<td><% bp.getController().writeFormInput(out,"pg_classificazione_montanti"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_classificazione_cori"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_classificazione_cori"); %></td>
  </tr>
</table>

<table class="Panel">
  <tr>
	<td colspan="6"><% bp.getController().writeFormInput(out,"ti_cassa_competenza"); %></td>
  </tr>			
  <tr>
	<td><% bp.getController().writeFormInput(out,"fl_assistenza_fiscale");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_assistenza_fiscale");%></td>
	<td><% bp.getController().writeFormInput(out,"fl_uso_in_lordizza");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_uso_in_lordizza");%></td>
	<td><% bp.getController().writeFormInput(out,"fl_scrivi_montanti");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_scrivi_montanti");%></td>
	<td><% bp.getController().writeFormInput(out,"fl_gla");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_gla");%></td>
	<td><% bp.getController().writeFormInput(out,"cd_ente_prev_sti");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"cd_ente_prev_sti");%></td>
  </tr>
  <tr>
  	<td><% bp.getController().writeFormInput(out,"fl_sospensione_irpef");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_sospensione_irpef");%></td>
  </tr>
 <tr>
  	<td><% bp.getController().writeFormInput(out,"fl_credito_irpef");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_credito_irpef");%></td>
  </tr>

</table>

<table class="Group" style="width:100%">
  <tr>
	<td rowspan = "4">
	  <% java.util.List coll = ((Tipo_contributo_ritenutaBulk)bp.getModel()).getIntervalli();
	  if((coll != null) && (coll.size()!=0))
	  	bp.getIntervalliCRUDController().writeHTMLTable(
					pageContext,
					null,
					false,
					false,
					false,
					null,
					null,
					true); %>
	</td>
  </tr>
</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
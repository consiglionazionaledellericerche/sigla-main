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
<title>Tipo Trattamento</title>
</head>
<body class="Form">

<% 	CRUDTipoTrattamentoBP bp = (CRUDTipoTrattamentoBP)BusinessProcess.getBusinessProcess(request);
 	bp.openFormWindow(pageContext); %>

<table class="Panel card p-2">
  <% if (bp.isSearching()) { %>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "cd_trattamentoForSearch"); %></td>
		<td><% bp.getController().writeFormInput( out, "cd_trattamentoForSearch"); %></td>	
	</tr>			
  <% } else { %>  
	<tr>
		<td><% bp.getController().writeFormLabel( out, "cd_trattamento"); %></td>
		<td><% bp.getController().writeFormInput( out, "cd_trattamento"); %></td>	
	</tr>			
  <% } %>  
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ds_ti_trattamento"); %></td>
	<td colspan=3><% bp.getController().writeFormInput(out,"ds_ti_trattamento"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_inizio_validita"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_inizio_validita"); %></td>
	<td align="right"><% bp.getController().writeFormLabel(out,"dataFineValidita"); %>
		<% bp.getController().writeFormInput(out,"dataFineValidita"); %></td>
  </tr>
</table>

<table class="Panel card p-2 mt-2">
  <tr>
	<td colspan=4><% bp.getController().writeFormInput(out,"ti_anagrafico"); %></td>
  </tr>
</table>

<table class="Panel card p-2 mt-2">
  <tr>
	<td colspan=4><% bp.getController().writeFormInput(out,"ti_commerciale"); %></td>
  </tr>
</table>

<table class="Panel card p-2 mt-2">
  <tr>
	<td><% bp.getController().writeFormInput(out,"fl_detrazioni_familiari");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_detrazioni_familiari");%></td>
	<td><% bp.getController().writeFormInput(out,"fl_detrazioni_dipendente");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_detrazioni_dipendente");%></td>
	<td><% bp.getController().writeFormInput(out,"fl_detrazioni_altre");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_detrazioni_altre");%></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormInput(out,"fl_soggetto_conguaglio");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_soggetto_conguaglio");%></td>
	<td><% bp.getController().writeFormInput(out,"fl_senza_calcoli");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_senza_calcoli");%></td>
	<td><% bp.getController().writeFormInput(out,"fl_registra_fattura");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_registra_fattura");%></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormInput(out,"fl_diaria");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_diaria");%></td>
	<td><% bp.getController().writeFormInput(out,"fl_irpef_annualizzata");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_irpef_annualizzata");%></td>
	<td><% bp.getController().writeFormInput(out,"fl_default_conguaglio");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_default_conguaglio");%></td>
  </tr>	
  <tr>
	<td><% bp.getController().writeFormInput(out,"fl_agevolazioni_cervelli");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_agevolazioni_cervelli");%></td>
	<td><% bp.getController().writeFormInput(out,"fl_utilizzabile_art35");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_utilizzabile_art35");%></td>
	<td><% bp.getController().writeFormInput(out,"fl_anno_prec");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_anno_prec");%></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormInput(out,"fl_tassazione_separata");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_tassazione_separata");%></td>
	<td><% bp.getController().writeFormInput(out,"fl_incarico");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_incarico");%></td>
	<td><% bp.getController().writeFormInput(out,"fl_agevolazioni_rientro_lav");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_agevolazioni_rientro_lav");%></td>
	<td><% bp.getController().writeFormInput(out,"fl_solo_inail_ente");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_solo_inail_ente");%></td>
  </tr> 
  <tr>	 
	<td><% bp.getController().writeFormInput(out,"fl_visibile_a_tutti");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_visibile_a_tutti");%></td>
	<td><% bp.getController().writeFormInput(out,"fl_pignorato_obbl");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_pignorato_obbl");%></td> 
	<td><% bp.getController().writeFormInput(out,"fl_split_payment");%></td>
	<td align="left"><% bp.getController().writeFormLabel(out,"fl_split_payment");%></td>
  </tr>
</table>

<table class="Panel card p-2 mt-2">
  <tr>
	<td><% bp.getController().writeFormField(out,"tipoDebitoSiope"); %></td>
  </tr>
</table>

<table class="Group card p-2 mt-2" style="width:100%">
  <tr>
	<td>
	  <% java.util.List coll = ((Tipo_trattamentoBulk)bp.getModel()).getIntervalli();
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

<% bp.closeFormWindow(pageContext); %>
</body>
</html>
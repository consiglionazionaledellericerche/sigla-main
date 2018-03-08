<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.missioni00.tabrif.bulk.*,
		it.cnr.contab.missioni00.bp.*"				
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Tipo Spesa</title>
</head>
<body class="Form">

<% 	CRUDMissioneTipoSpesaBP bp = (CRUDMissioneTipoSpesaBP)BusinessProcess.getBusinessProcess(request);
 	bp.openFormWindow(pageContext);
	Missione_tipo_spesaBulk tipoSpesa = (Missione_tipo_spesaBulk)bp.getModel(); 	 	
%>

<table class="Panel">

  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_ti_spesa"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"cd_ti_spesa"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ds_ti_spesa"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"ds_ti_spesa"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"rif_inquadramento"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"rif_inquadramento"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ti_area_geografica"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,null,"ti_area_geografica",bp.isEditing(),null,"onChange=\"submitForm('doSelezioneTipoAreaGeografica')\""); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"pg_nazione"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"pg_nazione"); %>
		<% bp.getController().writeFormInput(out,"ds_nazione"); %>
		<% bp.getController().writeFormInput(out,"find_nazione"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_divisa"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"cd_divisa"); %>
		<% bp.getController().writeFormInput(out,"ds_divisa"); %>
		<% bp.getController().writeFormInput(out,"find_divisa"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel( out, "limite_max_spesa"); %></td>
	<td><% bp.getController().writeFormInput( out, "limite_max_spesa"); %></td>
	<td><% bp.getController().writeFormLabel( out, "percentuale_maggiorazione"); %></td>
	<td><% bp.getController().writeFormInput( out, "percentuale_maggiorazione"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_inizio_validita"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_inizio_validita"); %></td>
	<td><% bp.getController().writeFormLabel(out,"dataFineValidita"); %></td>
	<td><% bp.getController().writeFormInput(out,"dataFineValidita"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_cancellazione"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_cancellazione"); %></td>
  </tr>
</table>

&nbsp;

<table class="Panel" >
  <tr><td><% bp.getController().writeFormField( out, "fl_giustificativo_richiesto"); %></td></tr>
  <tr><td><% bp.getController().writeFormField( out, "fl_ammissibile_con_rimborso"); %></td></tr>
</table>
<table class="Panel" > 
  <tr><td><% bp.getController().writeFormInput(out,"default","tipoSpeseRadioGroup", false,null, null);%></td></tr>	
</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
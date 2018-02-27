<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.missioni00.bp.*"				
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Missione Abbattimenti</title>
</head>
<body class="Form">

<% 	CRUDMissioneAbbattimentiBP bp = (CRUDMissioneAbbattimentiBP)BusinessProcess.getBusinessProcess(request);
 	bp.openFormWindow(pageContext);
%>

<table class="Panel">

  <tr>
	<td><% bp.getController().writeFormLabel( out, "ti_area_geografica"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,null,"ti_area_geografica",bp.isEditing(),null,"onChange=\"submitForm('doSelezioneTipoAreaGeografica')\""); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"pg_nazione"); %></td>
	<td colspan="3">
		<% bp.getController().writeFormInput(out,"pg_nazione"); %>
		<% bp.getController().writeFormInput(out,"ds_nazione"); %>
		<% bp.getController().writeFormInput(out,"find_nazione"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel( out, "rif_inquadramento"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput( out, "rif_inquadramento"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"durata_ore"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,null,"durata_ore",false,null,"onChange=\"submitForm('doDefault')\""); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"percentuale_abbattimento"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"percentuale_abbattimento"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_inizio_validita"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_inizio_validita"); %></td>
	<td><% bp.getController().writeFormLabel(out,"dataFineValidita"); %></td>
	<td><% bp.getController().writeFormInput(out,"dataFineValidita"); %></td>
  </tr>
  <tr>
  	<td><% bp.getController().writeFormLabel(out, "dt_cancellazione"); %></td>
 	<td colspan="3"><% bp.getController().writeFormInput(out,null,"dt_cancellazione",bp.isInserting(),null,null); %></td>
  </tr>
</table>

&nbsp;

<table class="Panel">
 	<tr>
	<td><% bp.getController().writeFormInput(out,"default","vittoAlloggioNavigazioneRadioGroup", false,null, null);%></td>	
	</tr>
	<tr>
	<td><% bp.getController().writeFormInput(out,"default","tipoSpeseRadioGroup", false,null, null);%></td>	
	</tr>	
</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>
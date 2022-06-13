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
<title>Detrazioni Lavoro</title>
</head>
<body class="Form">

<% CRUDDetrazioniLavoroBP bp = (CRUDDetrazioniLavoroBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext);
	 Detrazioni_lavoroBulk detraz = (Detrazioni_lavoroBulk)bp.getModel(); %>

<table class="Panel">
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ti_lavoro");%></td> 
	<td colspan=4><% bp.getController().writeFormInput(out,"ti_lavoro");%></td> 
  </tr>
  <tr>
    <td><% bp.getController().writeFormLabel(out,"im_detrazione");%></td>
    <td><% bp.getController().writeFormInput(out,"im_detrazione");%></td>
  </tr>
  <tr>
    <td><% bp.getController().writeFormLabel(out,"im_inferiore");%></td>
    <td><% bp.getController().writeFormInput(out,"im_inferiore");%></td>
	<td><% bp.getController().writeFormLabel(out,"im_superiore");%></td>
    <td><% bp.getController().writeFormInput(out,"im_superiore");%></td>
  </tr>
   <tr>
    <td><% bp.getController().writeFormLabel(out,"im_maggiorazione");%></td>
    <td><% bp.getController().writeFormInput(out,"im_maggiorazione");%></td>
    <td><% bp.getController().writeFormLabel(out,"im_detrazione_minima");%></td>
    <td><% bp.getController().writeFormInput(out,"im_detrazione_minima");%></td>  
  </tr>
  </table>
  <fieldset class="fieldset">
	<legend class="GroupLabel">Formula calcolo Detrazioni</legend>
	<table>
   <tr>
    <td><% bp.getController().writeFormLabel(out,"moltiplicatore");%></td>
    <td><% bp.getController().writeFormInput(out,"moltiplicatore");%></td>
    <td><% bp.getController().writeFormLabel(out,"numeratore");%></td>
    <td><% bp.getController().writeFormInput(out,"numeratore");%></td>    
    <td><% bp.getController().writeFormLabel(out,"denominatore");%></td>
    <td><% bp.getController().writeFormInput(out,"denominatore");%></td>
  </tr>
  </table>
  </fieldset>
  <fieldset class="fieldset">
	<legend class="GroupLabel">Importo Incremento Fisso</legend>
	<table>
   <tr>
    <td><% bp.getController().writeFormLabel(out,"imIncrFisso");%></td>
    <td><% bp.getController().writeFormInput(out,"imIncrFisso");%></td>
    <td><% bp.getController().writeFormLabel(out,"imInfIncrFisso");%></td>
    <td><% bp.getController().writeFormInput(out,"imInfIncrFisso");%></td>
    <td><% bp.getController().writeFormLabel(out,"imSupIncrFisso");%></td>
    <td><% bp.getController().writeFormInput(out,"imSupIncrFisso");%></td>
  </tr>
  </table>
  </fieldset>
  <table>
  <tr>
    <td><% bp.getController().writeFormLabel(out,"dt_inizio_validita");%></td>
    <td><% bp.getController().writeFormInput(out,"dt_inizio_validita");%></td>
	<td><% bp.getController().writeFormLabel(out,"dataFineValidita");%></td>
	<td><% bp.getController().writeFormInput(out,"dataFineValidita");%></td>
  </tr>
</table>

<% bp.closeFormWindow(pageContext); %>
</body>
</html>
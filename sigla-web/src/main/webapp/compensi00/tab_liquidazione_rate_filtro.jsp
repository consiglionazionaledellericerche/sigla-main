<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.compensi00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<% 	CRUDLiquidazioneRateMinicarrieraBP bp = (CRUDLiquidazioneRateMinicarrieraBP)BusinessProcess.getBusinessProcess(request); %>

<div class="Panel" style="width:100%">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"nrMinicarrieraDa"); %></td>
	<td><% bp.getController().writeFormInput(out,"nrMinicarrieraDa"); %></td>
	<td><% bp.getController().writeFormLabel(out,"nrMinicarrieraA"); %></td>
	<td><% bp.getController().writeFormInput(out,"nrMinicarrieraA"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dtRegistrazioneDa"); %></td>
	<td><% bp.getController().writeFormInput(out,"dtRegistrazioneDa"); %></td>
	<td><% bp.getController().writeFormLabel(out,"dtRegistrazioneA"); %></td>
	<td><% bp.getController().writeFormInput(out,"dtRegistrazioneA"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dtInizioMinicarrieraDa"); %></td>
	<td><% bp.getController().writeFormInput(out,"dtInizioMinicarrieraDa"); %></td>
	<td><% bp.getController().writeFormLabel(out,"dtInizioMinicarrieraA"); %></td>
	<td><% bp.getController().writeFormInput(out,"dtInizioMinicarrieraA"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dtScadenzaDa"); %></td>
	<td><% bp.getController().writeFormInput(out,"dtScadenzaDa"); %></td>
	<td><% bp.getController().writeFormLabel(out,"dtScadenzaA"); %></td>
	<td><% bp.getController().writeFormInput(out,"dtScadenzaA"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cdTerzo"); %></td>
	<td colspan="3">
		<% bp.getController().writeFormInput(out,"cdTerzo"); %>
		<% bp.getController().writeFormInput(out,"findTerzo"); %>
	</td>
  </tr>
</table>
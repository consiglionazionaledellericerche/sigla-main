<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.compensi00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<% 	CRUDLiquidazioneRateMinicarrieraBP bp = (CRUDLiquidazioneRateMinicarrieraBP)BusinessProcess.getBusinessProcess(request); %>

<div class="Group" style="width:100%">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findElementoVoce"); %></td>
	<td><% bp.getController().writeFormInput(out,"cdElementoVoce"); %></td>
	<td><% bp.getController().writeFormInput(out,"dsElementoVoce"); %></td>
	<td><% bp.getController().writeFormInput(out,"findElementoVoce"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"tipoGestione"); %></td>
	<td><% bp.getController().writeFormInput(out,"tipoGestione"); %></td>
	<td colspan="2">
		<% bp.getController().writeFormLabel(out,"tipoAppartenenza"); %>
		<% bp.getController().writeFormInput(out,"tipoAppartenenza"); %>
	</td>
  </tr>
</table>
</div>

<div class="Group" style="width:100%">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findLineaAttivita"); %></td>
	<td><% bp.getController().writeFormInput(out,"cdLineaAttivita"); %></td>
	<td><% bp.getController().writeFormInput(out,"dsLineaAttivita"); %></td>
	<td><% bp.getController().writeFormInput(out,"findLineaAttivita"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cdCentroResponsabilita"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"cdCentroResponsabilita"); %></td>
  </tr>
</table>
</div>

<div class="Group" style="width:100%">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cdVoce"); %></td>
	<td><% bp.getController().writeFormInput(out,"cdVoce"); %></td>
  </tr>
</table>
</div>

<div class="Panel" style="width:100%">
<%	if (bp.isViewing()) {
		bp.getRateCRUDController().setMultiSelection(false);
	} else {
		bp.getRateCRUDController().setMultiSelection(true);
	}
	bp.getRateCRUDController().writeHTMLTable(
		pageContext,
		"LIQUIDAZIONE_RATE_COLUMN_SET",
		false,
		false,
		false,
		"100%",
		"150px",
		true);
%>
</div>
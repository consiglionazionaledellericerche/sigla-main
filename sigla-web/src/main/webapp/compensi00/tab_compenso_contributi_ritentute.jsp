<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.compensi00.bp.*,
		it.cnr.contab.compensi00.docs.bulk.*"
%>

<%	CRUDCompensoBP bp = (CRUDCompensoBP)BusinessProcess.getBusinessProcess(request);
	CompensoBulk compenso = (CompensoBulk)bp.getModel();  %>
	
<% bp.getContributiCRUDController().writeHTMLTable(
				pageContext,
				null,
				false,
				false,
				false,
				"100%",
				"200px",
				true); %>

<% if (compenso.isSenzaCalcoli()) { %>

<fieldset>
  <% Contributo_ritenutaBulk cori = (Contributo_ritenutaBulk)bp.getContributiCRUDController().getModel();
	 if (cori==null) { %>
		<legend ACCESSKEY=G TABINDEX=1 style="font-weight:bold; font-family:sans-serif; font-size:12px;">Ente/Percipiente</legend>
  <% } else if (cori.getTi_ente_percipiente().equals(cori.TIPO_ENTE)) { %>
		<legend ACCESSKEY=G TABINDEX=1 style="font-weight:bold; font-family:sans-serif; font-size:12px;">Ente</legend>
  <% } else if (cori.getTi_ente_percipiente().equals(cori.TIPO_PERCEPIENTE)) { %>
		<legend ACCESSKEY=G TABINDEX=1 style="font-weight:bold; font-family:sans-serif; font-size:12px;">Percipiente</legend>
  <% } %>

<table>
  <tr>
	<td><% bp.getContributiCRUDController().writeFormLabel(out,"cd_contributo_ritenuta"); %></td>
	<td><% bp.getContributiCRUDController().writeFormInput(out,"cd_contributo_ritenuta"); %>
		<% bp.getContributiCRUDController().writeFormInput(out,"ds_contributo_ritenuta"); %></td>
  </tr>
  <tr>
	<td><% bp.getContributiCRUDController().writeFormLabel(out,"imponibile"); %></td>
	<td><% bp.getContributiCRUDController().writeFormInput(out,"imponibile"); %>
		<% bp.getContributiCRUDController().writeFormLabel(out,"ammontare_lordo"); %>
		<% bp.getContributiCRUDController().writeFormInput(out,"ammontare_lordo"); %></td>
  </tr>
  <tr>
	<td colspan=2 align=center>
		<% JSPUtils.button(out,"img/save24.gif","img/save24.gif", "Conferma","javascript:submitForm('doConfermaModificaCORI')", null, bp.isBottoneConfermaModificaCORIEnabled()); %>
		<% JSPUtils.button(out,"img/undo24.gif","img/undo24.gif", "Annulla","javascript:submitForm('doAnnullaModificaCORI')", null, bp.isBottoneAnnullaModificaCORIEnabled()); %>
	</td>
  </tr>
</table>

</fieldset>

<% } %>

<div class="Group">
<table>
  <tr>
  	<td><span class="FormLabel">Totale complessivo</span></td>
	<td align="left"><% bp.getController().writeFormInput(out,"im_totale_compenso"); %></td>
  </tr>
  <tr>
  	<td><span class="FormLabel">Totale ritenute carico Ente</span></td>
	<td align="left"><% bp.getController().writeFormInput(out,"im_cr_ente"); %></td>
  </tr>
  <tr>
  	<td><span class="FormLabel">Lordo Percipiente</span></td>
	<td align="left"><% bp.getController().writeFormInput(out,"imLordoPercipiente"); %></td>
  </tr>
  <tr>
  	<td><span class="FormLabel">Totale ritenute carico Percipiente</span></td>
	<td align="left"><% bp.getController().writeFormInput(out,"im_cr_percipiente"); %></td>
  </tr>
  <tr>
  	<td><span class="FormLabel">Importo netto a pagare</span></td>
	<td align="left"><% bp.getController().writeFormInput(out,"im_netto_percipiente"); %></td>
  </tr>
</table>
</div>
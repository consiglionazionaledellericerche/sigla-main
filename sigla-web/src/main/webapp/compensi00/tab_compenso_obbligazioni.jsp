<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.compensi00.bp.*,
		it.cnr.contab.compensi00.docs.bulk.*"
%>

<%	CRUDCompensoBP bp = (CRUDCompensoBP)BusinessProcess.getBusinessProcess(request);
	CompensoBulk compenso = (CompensoBulk)bp.getModel(); %>

<div class="Group" style="width:100%">
<table>

  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio_ori_obbligazione"); %></td>
	<td><% bp.getController().writeFormInput(out,"esercizio_ori_obbligazione"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"pg_obbligazione"); %></td>
	<td><% bp.getController().writeFormInput(out,"pg_obbligazione"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"pg_obbligazione_scadenzario"); %></td>
	<td><% bp.getController().writeFormInput(out,"pg_obbligazione_scadenzario"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_cds_obbligazione"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_cds_obbligazione"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio_obbligazione"); %></td>
	<td><% bp.getController().writeFormInput(out,"esercizio_obbligazione"); %></td>
  </tr>

  <tr>
	<td><% bp.getController().writeFormLabel(out,"scadenza_dt_scadenza"); %></td>
	<td><% bp.getController().writeFormInput(out,"scadenza_dt_scadenza"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"scadenza_im_scadenza"); %></td>
	<td><% bp.getController().writeFormInput(out,"scadenza_im_scadenza"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"scadenza_ds_scadenza"); %></td>
	<td><% bp.getController().writeFormInput(out,"scadenza_ds_scadenza"); %></td>
  </tr>

  <tr>
	<td colspan=2>
		<% if (compenso.isStatoCompensoEseguiCalcolo()) { %>
			<span class="FormLabel" style="color:red">E' necessario eseguire il calcolo prima di continuare</span>
		<% } %>
	</td>
  </tr>
  <tr>
	<td colspan=2>
		<% JSPUtils.button(out,"img/new24.gif","img/new24.gif","Crea/Ricerca<br>impegno","if (disableDblClick()) submitForm('doRicercaObbligazione')",null,bp.isBottoneCreaObbligazioneEnabled());%>
		<% JSPUtils.button(out,"img/remove24.gif","img/remove24.gif","Elimina<br>impegno","if (disableDblClick()) submitForm('doEliminaObbligazione')",null,bp.isBottoneEliminaObbligazioneEnabled());%>
		<% JSPUtils.button(out,"img/redo24.gif","img/redo24.gif","Aggiorna in<br>manuale","if (disableDblClick()) submitForm('doModificaManualeObbligazione')",null,bp.isBottoneModificaManualeObbligazioneEnabled());%>
		<% JSPUtils.button(out,"img/refresh24.gif","img/refresh24.gif","Aggiorna in<br>automatico","if (disableDblClick()) submitForm('doModificaAutomaticaObbligazione')",null,bp.isBottoneModificaAutomaticaObbligazioneEnabled());%>
	</td>
  </tr>

</table>
</div>
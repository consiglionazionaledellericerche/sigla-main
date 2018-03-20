<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.compensi00.bp.*,
		it.cnr.contab.compensi00.docs.bulk.*"
%>

<%	CRUDCompensoBP bp = (CRUDCompensoBP)BusinessProcess.getBusinessProcess(request);
	CompensoBulk compenso = (CompensoBulk)bp.getModel(); %>

<div class="Group card">
<table>

  <tr>
	<% bp.getController().writeFormField(out,"esercizio_ori_obbligazione"); %>
  </tr>
  <tr>
	<% bp.getController().writeFormField(out,"pg_obbligazione"); %>
  </tr>
  <tr>
	<% bp.getController().writeFormField(out,"pg_obbligazione_scadenzario"); %>
  </tr>
  <tr>
	<% bp.getController().writeFormField(out,"cd_cds_obbligazione"); %>
  </tr>
  <tr>
	<% bp.getController().writeFormField(out,"esercizio_obbligazione"); %>
  </tr>

  <tr>
	<% bp.getController().writeFormField(out,"scadenza_dt_scadenza"); %>
  </tr>
  <tr>
	<% bp.getController().writeFormField(out,"scadenza_im_scadenza"); %>
  </tr>
  <tr>
	<% bp.getController().writeFormField(out,"scadenza_ds_scadenza"); %>
  </tr>
  <tr>
	<% bp.getController().writeFormField(out,"cig"); %>
  </tr>
  <tr>
	<td colspan="2">
		<% if (compenso.isStatoCompensoEseguiCalcolo()) { %>
			<span class="FormLabel" style="color:red">E' necessario eseguire il calcolo prima di continuare</span>
		<% } %>
	</td>
  </tr>
</table>
<table>
  <tr>
	<td>
		<% JSPUtils.button(out,
				bp.getParentRoot().isBootstrap()?"fa fa-fw fa-plus text-info":"img/new24.gif",
				bp.getParentRoot().isBootstrap()?"fa fa-fw fa-plus text-info":"img/new24.gif",
				bp.getParentRoot().isBootstrap()?"Crea/Ricerca impegno":"Crea/Ricerca<br>impegno",
				"if (disableDblClick()) submitForm('doRicercaObbligazione')",
				"btn-secondary btn-outline-secondary btn-title text-primary",
				bp.isBottoneCreaObbligazioneEnabled(), 
				bp.getParentRoot().isBootstrap());%>
		<% JSPUtils.button(out,
				bp.getParentRoot().isBootstrap()?"fa fa-fw fa-trash text-danger":"img/remove24.gif",
				bp.getParentRoot().isBootstrap()?"fa fa-fw fa-trash text-danger":"img/remove24.gif",
				bp.getParentRoot().isBootstrap()?"Elimina impegno":"Elimina<br>impegno",
				"if (disableDblClick()) submitForm('doEliminaObbligazione')",
				"btn-secondary btn-outline-secondary btn-title text-primary",
				bp.isBottoneEliminaObbligazioneEnabled(), 
				bp.getParentRoot().isBootstrap());%>
		<% JSPUtils.button(out,
				bp.getParentRoot().isBootstrap()?"fa fa-fw fa-repeat text-info":"img/redo24.gif",
				bp.getParentRoot().isBootstrap()?"fa fa-fw fa-repeat text-info":"img/redo24.gif",
				bp.getParentRoot().isBootstrap()?"Aggiorna in manuale":"Aggiorna in<br>manuale",
				"if (disableDblClick()) submitForm('doModificaManualeObbligazione')",
				"btn-secondary btn-outline-secondary btn-title text-primary",
				bp.isBottoneModificaManualeObbligazioneEnabled(), 
				bp.getParentRoot().isBootstrap());%>
		<% JSPUtils.button(out,
				bp.getParentRoot().isBootstrap()?"fa fa-fw fa-refresh text-info":"img/refresh24.gif",
				bp.getParentRoot().isBootstrap()?"fa fa-fw fa-refresh text-info":"img/refresh24.gif",
				bp.getParentRoot().isBootstrap()?"Aggiorna in automatico":"Aggiorna in<br>automatico",
				"if (disableDblClick()) submitForm('doModificaAutomaticaObbligazione')",
				"btn-secondary btn-outline-secondary btn-title text-primary",
				bp.isBottoneModificaAutomaticaObbligazioneEnabled(), 
				bp.getParentRoot().isBootstrap());%>
	</td>
  </tr>
</table>
</div>
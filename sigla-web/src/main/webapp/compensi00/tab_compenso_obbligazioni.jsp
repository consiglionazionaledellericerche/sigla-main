<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.compensi00.bp.*,
		it.cnr.contab.doccont00.core.bulk.*,
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
    <div class="Group card">
        <table class="w-100">
           <tr>
                <td><% bp.getController().writeFormLabel(out,"cig");%></td>
		        <td>
		         	<div style="float:left"><% bp.getController().getBulkInfo().writeFormInput(out,bp.getController().getModel(),null, "cig", !bp.isCigModificabile(),null,
	                        "",
	                        bp.getController().getInputPrefix(),
	                        bp.getController().EDIT,
	                        bp.getController().getFieldValidationMap(),
	                        bp.getParentRoot().isBootstrap());%></div>
		        </td>
                <td><% bp.getController().writeFormLabel(out,"motivo_assenza_cig");%></td>
                <td><% bp.getController().getBulkInfo().writeFormInput(
                        out,
                        bp.getController().getModel(),
                        null,
                        "motivo_assenza_cig",
                        !bp.isCigModificabile(),
                        null,
                        "",
                        bp.getController().getInputPrefix(),
                        bp.getController().EDIT,
                        bp.getController().getFieldValidationMap(),
                        bp.getParentRoot().isBootstrap()
                    );
                %></td>
          </tr>
        </table>
    </div>
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
				"btn-outline-secondary btn-title text-primary",
				bp.isBottoneCreaObbligazioneEnabled(), 
				bp.getParentRoot().isBootstrap());%>
		<% JSPUtils.button(out,
				bp.getParentRoot().isBootstrap()?"fa fa-fw fa-trash text-danger":"img/remove24.gif",
				bp.getParentRoot().isBootstrap()?"fa fa-fw fa-trash text-danger":"img/remove24.gif",
				bp.getParentRoot().isBootstrap()?"Elimina impegno":"Elimina<br>impegno",
				"if (disableDblClick()) submitForm('doEliminaObbligazione')",
				"btn-outline-secondary btn-title text-primary",
				bp.isBottoneEliminaObbligazioneEnabled(), 
				bp.getParentRoot().isBootstrap());%>
		<% JSPUtils.button(out,
				bp.getParentRoot().isBootstrap()?"fa fa-fw fa-repeat text-info":"img/redo24.gif",
				bp.getParentRoot().isBootstrap()?"fa fa-fw fa-repeat text-info":"img/redo24.gif",
				bp.getParentRoot().isBootstrap()?"Aggiorna in manuale":"Aggiorna in<br>manuale",
				"if (disableDblClick()) submitForm('doModificaManualeObbligazione')",
				"btn-outline-secondary btn-title text-primary",
				bp.isBottoneModificaManualeObbligazioneEnabled(), 
				bp.getParentRoot().isBootstrap());%>
		<% JSPUtils.button(out,
				bp.getParentRoot().isBootstrap()?"fa fa-fw fa-refresh text-info":"img/refresh24.gif",
				bp.getParentRoot().isBootstrap()?"fa fa-fw fa-refresh text-info":"img/refresh24.gif",
				bp.getParentRoot().isBootstrap()?"Aggiorna in automatico":"Aggiorna in<br>automatico",
				"if (disableDblClick()) submitForm('doModificaAutomaticaObbligazione')",
				"btn-outline-secondary btn-title text-primary",
				bp.isBottoneModificaAutomaticaObbligazioneEnabled(), 
				bp.getParentRoot().isBootstrap());%>
	</td>
  </tr>
</table>
</div>
<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*,it.cnr.contab.doccont00.bp.*"
%>


<%  
		CRUDObbligazioneBP bp = (CRUDObbligazioneBP)BusinessProcess.getBusinessProcess(request);
		it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = (it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk)bp.getModel();
		it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk scadenza = (it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk)bp.getScadenzario().getModel();
%>

	<table border="0" cellspacing="0" cellpadding="2">
	<% if ( bp.isEditingScadenza() && scadenza != null && scadenza.getScadenza_iniziale() != null ) {%>
	<tr>			
	<td><% bp.getScadenzario().writeFormLabel( out, "dt_scadenza"); %></td>
	<td>
    	<% bp.getScadenzario().writeFormInput( out,"default","dt_scadenza",!bp.isEditingScadenza(),null,null); %>
		<% bp.getScadenzario().writeFormLabel( out, "fl_aggiorna_scad_successiva"); %>
    	<% bp.getScadenzario().writeFormInput( out,"default","fl_aggiorna_scad_successiva",!bp.isEditingScadenza(),null,null); %>		
	</td>
	</tr>
	<%}

	 else {%>
	<tr>			
	<td><% bp.getScadenzario().writeFormLabel( out, "dt_scadenza"); %></td>
	<td>
    	<% bp.getScadenzario().writeFormInput( out,"default","dt_scadenza",!bp.isEditingScadenza(),null,null); %>
    </td>	
	</tr>
	<%}%>
	
	<tr>
	<td><% bp.getScadenzario().writeFormLabel( out, "im_scadenza"); %></td>	
	<td><% bp.getScadenzario().writeFormInput( out,"default","im_scadenza",!bp.isEditingScadenza(),null,null); %></td>
	<% if ( scadenza != null && scadenza.getFlAssociataOrdine()) {%>
	<td><% bp.getScadenzario().writeFormLabel( out, "im_associato_doc_amm"); %></td>
	<td><% bp.getScadenzario().writeFormInput( out,"default","im_associato_doc_amm",true,null,null); %></td>
	<%}%>
	</tr>
	<tr>			
	<td><% bp.getScadenzario().writeFormLabel( out, "ds_scadenza"); %></td>
	<td>
    	<% bp.getScadenzario().writeFormInput( out,"default","ds_scadenza",!bp.isEditingScadenza(),null,null); %>
    </td>	

	</tr>
	<tr>
	<td colspan=2 ALIGN="CENTER">
		<div class="btn-group mr-2" role="group">
			<% JSPUtils.button(out,
					bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-pencil-square-o text-success" : "img/edit24.gif",
					bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-pencil-square-o text-success" : "img/edit24.gif",
					"Modifica",
					"javascript:submitForm('doEditaScadenza')", 
					"btn-secondary btn-outline-secondary btn-title",
					bp.isEditScadenzaButtonEnabled(),
					bp.getParentRoot().isBootstrap()); %>
			<% JSPUtils.button(out,
					bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-floppy-o text-primary" : "img/save24.gif",
					bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-floppy-o text-primary" : "img/save24.gif",
					"Conferma",
					"javascript:submitForm('doConfermaScadenza')",
					"btn-secondary btn-outline-secondary btn-title",
					bp.isConfermaScadenzaButtonEnabled(),
					bp.getParentRoot().isBootstrap()); %>
			<% JSPUtils.button(out,
					bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-undo text-warning" : "img/undo24.gif",
					bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-undo text-warning" : "img/undo24.gif",
					"Annulla",
					"javascript:submitForm('doUndoScadenza')", 
					"btn-secondary btn-outline-secondary btn-title",
					bp.isUndoScadenzaButtonEnabled(),
					bp.getParentRoot().isBootstrap()); %>				
		</div>
	</td>
	</tr>
  </table>